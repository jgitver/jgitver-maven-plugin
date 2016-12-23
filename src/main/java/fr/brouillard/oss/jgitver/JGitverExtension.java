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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "jgitver")
public class JGitverExtension extends AbstractMavenLifecycleParticipant {
    @Requirement
    private Logger logger;

    @Requirement
    private PlexusContainer container;

    @Requirement
    private ModelProcessor modelProcessor;

    @Override
    public void afterProjectsRead(MavenSession mavenSession) throws MavenExecutionException {
        MavenProject rootProject = mavenSession.getTopLevelProject();
        List<MavenProject> projects = locateProjects(mavenSession, rootProject.getModules());

        Map<GAV, String> newProjectVersions = new LinkedHashMap<>();
        final Consumer<? super CharSequence> c = cs -> logger.warn(cs.toString());

        if (JGitverModelProcessor.class.isAssignableFrom(modelProcessor.getClass())) {
            JGitverModelProcessor jGitverModelProcessor = JGitverModelProcessor.class.cast(modelProcessor);
            JGitverModelProcessorWorkingConfiguration workingConfiguration = jGitverModelProcessor.getWorkingConfiguration();

            if (workingConfiguration == null) {
                JGitverUtils.failAsOldMechanism(c);
            }
            
            newProjectVersions = workingConfiguration.getNewProjectVersions();
        } else {
            JGitverUtils.failAsOldMechanism(c);
        }

        newProjectVersions.entrySet().forEach(e -> logger.info("    " + e.getKey().toString() + " -> " + e.getValue()));
    }

    private List<MavenProject> locateProjects(MavenSession session, List<String> modules) {
        List<MavenProject> projects;
        projects = session.getProjects();
        List<MavenProject> allProjects = null;
        boolean multiModule = (modules != null) && (modules.size() > 0);
        try {
            allProjects = session.getAllProjects();
            if (allProjects != null) {
                projects = allProjects;
            }
        } catch (Throwable error) {
            if ((error instanceof NoSuchMethodError) || (error instanceof NoSuchMethodException)) {
                logger.warn("your maven version is <= 3.2.0 ; you should upgrade to enable jgitver-maven-plugin full "
                        + "integration");
            } else {
                // rethrow
                throw error;
            }
        }

        if (allProjects == null && multiModule) {
            // warn only in case of multimodules
            logger.warn("maven object model partially initialized, " + "jgitver-maven-plugin will use filtered list "
                    + "of maven projects in case reactor was filtered " + "with -pl");
        }

        return projects;
    }
}
