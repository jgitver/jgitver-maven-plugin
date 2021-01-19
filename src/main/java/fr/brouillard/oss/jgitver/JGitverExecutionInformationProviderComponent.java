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
package fr.brouillard.oss.jgitver;

import java.io.File;
import java.util.Optional;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.codehaus.plexus.component.annotations.Component;

@Component(role = JGitverExecutionInformationProvider.class, instantiationStrategy = "singleton")
public class JGitverExecutionInformationProviderComponent
    implements JGitverExecutionInformationProvider {
  private ArtifactRepository localRepository;
  private File rootDirectory;
  private JGitverSession session;

  @Override
  public ArtifactRepository getLocalRepository() {
    return localRepository;
  }

  @Override
  public void setLocalRepository(ArtifactRepository localRepository) {
    this.localRepository = localRepository;
  }

  @Override
  public File getRootDirectory() {
    return rootDirectory;
  }

  @Override
  public void setRootDirectory(File rootDirectory) {
    this.rootDirectory = rootDirectory;
  }

  @Override
  public void setSession(JGitverSession session) {
    this.session = session;
  }

  @Override
  public Optional<JGitverSession> session() {
    return Optional.ofNullable(session);
  }
}
