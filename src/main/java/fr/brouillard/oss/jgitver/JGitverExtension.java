// @formatter:ooff
/**
 * Copyright (C) 2016 Matthieu Brouillard [http://oss.brouillard.fr/jgitver-maven-plugin] (matthieu@brouillard.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// @formatter:on
package fr.brouillard.oss.jgitver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.building.ModelProcessor;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import fr.brouillard.oss.jgitver.cfg.Configuration;
import fr.brouillard.oss.jgitver.metadata.Metadatas;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "jgitver")
public class JGitverExtension extends AbstractMavenLifecycleParticipant {
    @Requirement
    private Logger logger;

    @Requirement
    private PlexusContainer container;

    @Requirement
    private ModelProcessor modelProcessor;

    @Requirement
    private JGitverSessionHolder sessionHolder;

    @Requirement
    private JGitverConfiguration configurationProvider;

    @Override
    public void afterSessionStart(MavenSession mavenSession) throws MavenExecutionException {
        if (JGitverUtils.shouldSkip(mavenSession)) {
            logger.info("  jgitver execution has been skipped by request of the user");
            sessionHolder.setSession(null);
        } else {
            final File rootDirectory = mavenSession.getRequest().getMultiModuleProjectDirectory();

            logger.debug("using " + JGitverUtils.EXTENSION_PREFIX + " on directory: " + rootDirectory);

            Configuration cfg = configurationProvider.getConfiguration();

            try (GitVersionCalculator gitVersionCalculator = GitVersionCalculator.location(rootDirectory)) {
                if (cfg.strategy != null) {
                    gitVersionCalculator.setStrategy(cfg.strategy);
                } else {
                    gitVersionCalculator.setMavenLike(cfg.mavenLike);
                }

                if (cfg.policy != null) {
                    gitVersionCalculator.setLookupPolicy(cfg.policy);
                }

                gitVersionCalculator
                        .setAutoIncrementPatch(cfg.autoIncrementPatch)
                        .setUseDirty(cfg.useDirty)
                        .setUseDistance(cfg.useCommitDistance)
                        .setUseGitCommitTimestamp(cfg.useGitCommitTimestamp)
                        .setUseGitCommitId(cfg.useGitCommitId)
                        .setGitCommitIdLength(cfg.gitCommitIdLength)
                        .setUseDefaultBranchingPolicy(cfg.useDefaultBranchingPolicy)
                        .setNonQualifierBranches(cfg.nonQualifierBranches)
                        .setVersionPattern(cfg.versionPattern)
                        .setTagVersionPattern(cfg.tagVersionPattern);

                if (cfg.maxSearchDepth >= 1 && cfg.maxSearchDepth != Configuration.UNSET_DEPTH) {
                    // keep redundant test in case we change UNSET_DEPTH value
                    gitVersionCalculator.setMaxDepth(cfg.maxSearchDepth);
                }

                if (cfg.regexVersionTag != null) {
                    gitVersionCalculator.setFindTagVersionPattern(cfg.regexVersionTag);
                }

                if (cfg.branchPolicies != null && !cfg.branchPolicies.isEmpty()) {
                    List<BranchingPolicy> policies = cfg.branchPolicies.stream()
                            .map(bp -> new BranchingPolicy(bp.pattern, bp.transformations))
                            .collect(Collectors.toList());

                    gitVersionCalculator.setQualifierBranchingPolicies(policies);
                }

                logger.info(String.format(
                        "Using jgitver-maven-plugin [%s] (sha1: %s)",
                        JGitverMavenPluginProperties.getVersion(),
                        JGitverMavenPluginProperties.getSHA1())
                );
                long start = System.currentTimeMillis();

                String computedVersion = gitVersionCalculator.getVersion();
                long duration = System.currentTimeMillis() - start;
                logger.info(String.format("    version '%s' computed in %d ms", computedVersion, duration));
                logger.info("");

                boolean isDirty = gitVersionCalculator
                        .meta(Metadatas.DIRTY)
                        .map(Boolean::parseBoolean)
                        .orElse(Boolean.FALSE);

                if (cfg.failIfDirty && isDirty) {
                    throw new IllegalStateException("repository is dirty");
                }

                JGitverInformationProvider infoProvider = Providers.decorate(gitVersionCalculator);
                JGitverInformationProvider finalInfoProvider = infoProvider;
                infoProvider = JGitverUtils.versionOverride(mavenSession, logger)
                        .map(version -> Providers.fixVersion(version, finalInfoProvider))
                        .orElse(infoProvider);

                JGitverUtils.fillPropertiesFromMetadatas(mavenSession.getUserProperties(), infoProvider, logger);

                JGitverSession session = new JGitverSession(infoProvider, rootDirectory);
                sessionHolder.setSession(session);
            } catch (Exception ex) {
                logger.warn("cannot autoclose GitVersionCalculator object for project: " + rootDirectory, ex);
            }
        }
    }

    @Override
    public void afterSessionEnd(MavenSession session) throws MavenExecutionException {
        sessionHolder.setSession(null);
    }

    @Override
    public void afterProjectsRead(MavenSession mavenSession) throws MavenExecutionException {
        if (!JGitverUtils.shouldSkip(mavenSession)) {
            File projectBaseDir = mavenSession.getCurrentProject().getBasedir();
            try {
                if (projectBaseDir != null && !configurationProvider.ignore(new File(projectBaseDir, "pom.xml"))) {
                    final Consumer<? super CharSequence> c = cs -> logger.warn(cs.toString());

                    if (JGitverModelProcessor.class.isAssignableFrom(modelProcessor.getClass())) {

                        if (!mavenSession.getUserProperties().containsKey(JGitverUtils.SESSION_MAVEN_PROPERTIES_KEY)) {
                            JGitverUtils.failAsOldMechanism(c);
                        }
                    } else {
                        JGitverUtils.failAsOldMechanism(c);
                    }

                    sessionHolder.session().ifPresent(jgitverSession -> {
                        logger.info("jgitver-maven-plugin is about to change project(s) version(s)");

                        jgitverSession.getProjects().forEach(
                            gav -> logger.info("    " + gav.toString() + " -> " + jgitverSession.getVersion())
                        );
                    });
                }
            } catch (IOException ex) {
                new MavenExecutionException("cannot evaluate if jgitver should ignore base project directory: " + projectBaseDir, ex);
            }
        }
    }
}
