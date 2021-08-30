/*
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
package fr.brouillard.oss.jgitver.mojos;

import fr.brouillard.oss.jgitver.JGitverCommons.Plugins.AttachModifiedPoms;
import fr.brouillard.oss.jgitver.JGitverExecutionInformationProvider;
import fr.brouillard.oss.jgitver.JGitverInformationStore;
import fr.brouillard.oss.jgitver.JGitverMojoUtils;
import fr.brouillard.oss.jgitver.JGitverSession;

import java.util.Optional;

import javax.inject.Inject;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.console.ConsoleLogger;

/** Works in conjunction with JGitverModelProcessor. */
@Mojo(name = AttachModifiedPoms.GOAL, defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresProject = true)
public class JGitverAttachModifiedPomsMojo extends AbstractMojo {
  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession mavenSession;

  @Parameter(property = "jgitver.resolve-project-version", defaultValue = "false")
  private Boolean resolveProjectVersion;

  private JGitverExecutionInformationProvider infoProvider;

  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  @Inject
  public JGitverAttachModifiedPomsMojo(JGitverExecutionInformationProvider infoProvider) {
    this.infoProvider = infoProvider;
  }
  
  @Override
  public void execute() throws MojoExecutionException {
    Optional<JGitverSession> optSession = infoProvider.session();

    if (!optSession.isPresent()) {
      getLog().warn(AttachModifiedPoms.GOAL + "shouldn't be executed alone. " +
          "This Mojo is part of the extension and is executed automatically.");
      return;
    }

    if (infoProvider.getRootDirectory().equals(project.getBasedir())) {
      try {
        JGitverSession jgitverSession = optSession.get();
        JGitverMojoUtils.attachModifiedPomFilesToTheProject(
            mavenSession.getAllProjects(),
            jgitverSession.getProjects(),
            jgitverSession.getVersion(),
            resolveProjectVersion,
            new ConsoleLogger()
            );
      } catch (Exception ex) {
        throw new MojoExecutionException(
            "Unable to execute goal: " + AttachModifiedPoms.GOAL, ex);
      }
    }
  }
}
