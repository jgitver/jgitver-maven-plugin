// @formatter:off
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
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.building.Source;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Scm;
import org.apache.maven.model.building.DefaultModelProcessor;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.plugin.LegacySupport;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import fr.brouillard.oss.jgitver.metadata.Metadatas;
import fr.brouillard.oss.jgitver.mojos.JGitverAttachModifiedPomsMojo;

;

/**
 * Replacement ModelProcessor using jgitver while loading POMs in order to adapt versions.
 */
@Component(role = ModelProcessor.class)
public class JGitverModelProcessor extends DefaultModelProcessor {
    @Requirement
    private Logger logger = null;

    @Requirement
    private LegacySupport legacySupport = null;
    
    @Requirement
    private JGitverConfiguration configurationProvider;

    @Requirement
    private JGitverSessionHolder jgitverSession;

    public JGitverModelProcessor() {
        super();
    }

    @Override
    public Model read(File input, Map<String, ?> options) throws IOException {
        return provisionModel(super.read(input, options), options);
    }

    @Override
    public Model read(Reader input, Map<String, ?> options) throws IOException {
        return provisionModel(super.read(input, options), options);
    }

    @Override
    public Model read(InputStream input, Map<String, ?> options) throws IOException {
        return provisionModel(super.read(input, options), options);
    }

    private Model provisionModel(Model model, Map<String, ?> options) throws IOException {
        Optional<JGitverSession> optSession = jgitverSession.session();
        if (!optSession.isPresent()) {
            // don't do anything in case no jgitver is there (execution could have been skipped)
            return model;
        } else {

            Source source = (Source) options.get(ModelProcessor.SOURCE);
            if (source == null) {
                return model;
            }

            File location = new File(source.getLocation());
            if (!location.isFile()) {
                // their JavaDoc says Source.getLocation "could be a local file path, a URI or just an empty string."
                // if it doesn't resolve to a file then calling .getParentFile will throw an exception,
                // but if it doesn't resolve to a file then it isn't under getMultiModuleProjectDirectory,
                return model; // therefore the model shouldn't be modified.
            }

            if (configurationProvider.ignore(location)) {
                logger.debug("file " + location + " ignored by configuration");
                return model;
            }

            JGitverSession jgitverSession = optSession.get();
            File relativePath = location.getParentFile().getCanonicalFile();
            File multiModuleDirectory = jgitverSession.getMultiModuleDirectory();
            String calculatedVersion = jgitverSession.getVersion();

            if (StringUtils.containsIgnoreCase(relativePath.getCanonicalPath(),
                    multiModuleDirectory.getCanonicalPath())) {
                logger.debug("handling version of project Model from " + location);

                jgitverSession.addProject(GAV.from(model.clone()));

                if (Objects.nonNull(model.getVersion())) {
                    // TODO evaluate how to set the version only when it was originally set in the pom file
                    model.setVersion(calculatedVersion);
                }

                if (Objects.nonNull(model.getParent())) {
                    // if the parent is part of the multi module project, let's update the parent version
                    String modelParentRelativePath = model.getParent().getRelativePath();
                    File relativePathParent = new File(
                            relativePath.getCanonicalPath() + File.separator + modelParentRelativePath)
                            .getParentFile().getCanonicalFile();
                    if (StringUtils.isNotBlank(modelParentRelativePath) 
                            && StringUtils.containsIgnoreCase(relativePathParent.getCanonicalPath(),
                            multiModuleDirectory.getCanonicalPath())) {
                        model.getParent().setVersion(calculatedVersion);
                    }
                }

                // we should only register the plugin once, on the main project
                if (relativePath.getCanonicalPath().equals(multiModuleDirectory.getCanonicalPath())) {
                    addAttachPomMojo(model);
                    updateScmTag(jgitverSession.getCalculator(), model);
                }

                try {
                    legacySupport.getSession().getUserProperties().put(
                            JGitverUtils.SESSION_MAVEN_PROPERTIES_KEY,
                            JGitverSession.serializeTo(jgitverSession));
                } catch (JAXBException ex) {
                    throw new IOException("cannot serialize JGitverSession", ex);
                }
            } else {
                logger.debug("skipping Model from " + location);
            }
        }

        return model;
    }

    private void updateScmTag(GitVersionCalculator calculator, Model model) {
        if (model.getScm() != null) {
            Scm scm = model.getScm();
            if (isVersionFromTag(calculator)) {
                scm.setTag(calculator.getVersion());
            } else {
                calculator.meta(Metadatas.GIT_SHA1_FULL).ifPresent(scm::setTag);
            }
        }
    }

    private boolean isVersionFromTag(GitVersionCalculator calculator) {
        List<String> versionTagsOnHead = Arrays.asList(calculator.meta(Metadatas.HEAD_VERSION_ANNOTATED_TAGS).orElse("").split(","));
        String baseTag = calculator.meta(Metadatas.BASE_TAG).orElse("");
        return versionTagsOnHead.contains(baseTag);
    }

    private void addAttachPomMojo(Model model) {
        if (Objects.isNull(model.getBuild())) {
            model.setBuild(new Build());
        }

        if (Objects.isNull(model.getBuild().getPlugins())) {
            model.getBuild().setPlugins(new ArrayList<>());
        }

        Optional<Plugin> pluginOptional = model.getBuild().getPlugins().stream()
                .filter(x -> JGitverUtils.EXTENSION_GROUP_ID.equalsIgnoreCase(x.getGroupId())
                        && JGitverUtils.EXTENSION_ARTIFACT_ID.equalsIgnoreCase(x.getArtifactId()))
                .findFirst();

        StringBuilder pluginVersion = new StringBuilder();

        try (InputStream inputStream = getClass()
                .getResourceAsStream("/META-INF/maven/" + JGitverUtils.EXTENSION_GROUP_ID + "/"
                        + JGitverUtils.EXTENSION_ARTIFACT_ID + "/pom" + ".properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            pluginVersion.append(properties.getProperty("version"));
        } catch (IOException ignored) {
            // TODO we should not ignore in case we have to reuse it
            logger.warn(ignored.getMessage(), ignored);
        }

        Plugin plugin = pluginOptional.orElseGet(() -> {
            Plugin plugin2 = new Plugin();
            plugin2.setGroupId(JGitverUtils.EXTENSION_GROUP_ID);
            plugin2.setArtifactId(JGitverUtils.EXTENSION_ARTIFACT_ID);
            plugin2.setVersion(pluginVersion.toString());

            model.getBuild().getPlugins().add(0, plugin2);
            return plugin2;
        });

        if (Objects.isNull(plugin.getExecutions())) {
            plugin.setExecutions(new ArrayList<>());
        }

        String pluginRunPhase = System.getProperty("jgitver.pom-replacement-phase", "prepare-package");
        Optional<PluginExecution> pluginExecutionOptional = plugin.getExecutions().stream()
                .filter(x -> pluginRunPhase.equalsIgnoreCase(x.getPhase())).findFirst();

        PluginExecution pluginExecution = pluginExecutionOptional.orElseGet(() -> {
            PluginExecution pluginExecution2 = new PluginExecution();
            pluginExecution2.setPhase(pluginRunPhase);

            plugin.getExecutions().add(pluginExecution2);
            return pluginExecution2;
        });

        if (Objects.isNull(pluginExecution.getGoals())) {
            pluginExecution.setGoals(new ArrayList<>());
        }

        if (!pluginExecution.getGoals().contains(JGitverAttachModifiedPomsMojo.GOAL_ATTACH_MODIFIED_POMS)) {
            pluginExecution.getGoals().add(JGitverAttachModifiedPomsMojo.GOAL_ATTACH_MODIFIED_POMS);
        }

        if (Objects.isNull(plugin.getDependencies())) {
            plugin.setDependencies(new ArrayList<>());
        }

        Optional<Dependency> dependencyOptional = plugin.getDependencies().stream()
                .filter(x -> JGitverUtils.EXTENSION_GROUP_ID.equalsIgnoreCase(x.getGroupId())
                        && JGitverUtils.EXTENSION_ARTIFACT_ID.equalsIgnoreCase(x.getArtifactId()))
                .findFirst();

        dependencyOptional.orElseGet(() -> {
            Dependency dependency = new Dependency();
            dependency.setGroupId(JGitverUtils.EXTENSION_GROUP_ID);
            dependency.setArtifactId(JGitverUtils.EXTENSION_ARTIFACT_ID);
            dependency.setVersion(pluginVersion.toString());

            plugin.getDependencies().add(dependency);
            return dependency;
        });
    }
}
