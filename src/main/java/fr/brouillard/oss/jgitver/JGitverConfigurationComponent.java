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

import fr.brouillard.oss.jgitver.cfg.Configuration;
import fr.brouillard.oss.jgitver.cfg.ConfigurationLoader;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.MavenExecutionException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component(role = JGitverConfiguration.class, instantiationStrategy = "singleton")
public class JGitverConfigurationComponent implements JGitverConfiguration {
  @Requirement private Logger logger = null;

  @Requirement private JGitverExecutionInformationProvider executionInformationProvider;

  private volatile Configuration configuration;

  private List<File> excludedDirectories = new LinkedList<>();

  @Override
  public Configuration getConfiguration() throws MavenExecutionException {
    if (configuration == null) {
      synchronized (this) {
        if (configuration == null) {
          final File rootDirectory = executionInformationProvider.getRootDirectory();

          logger.debug(
              "using " + JGitverUtils.EXTENSION_PREFIX + " on directory: " + rootDirectory);

          configuration = new ConfigurationLoader(rootDirectory, logger).load();

          excludedDirectories.add(
              new File(executionInformationProvider.getLocalRepository().getBasedir()));
          excludedDirectories.addAll(
              computeExcludedDirectoriesFromConfigurationExclusions(
                  rootDirectory, configuration.exclusions));
        }
      }
    }

    return configuration;
  }

  private Collection<File> computeExcludedDirectoriesFromConfigurationExclusions(
      File rootDirectory, List<String> exclusions) {
    return exclusions.stream()
        .map(
            dirName -> {
              File directory = new File(dirName);
              if (!directory.isAbsolute()) {
                directory = new File(rootDirectory, dirName);
              }
              logger.debug("ignoring directory (& sub dirs): " + directory);
              return directory;
            })
        .collect(Collectors.toList());
  }

  @Override
  public boolean ignore(File pomFile) throws IOException {
    for (File excludedDir : excludedDirectories) {
      if (StringUtils.containsIgnoreCase(
          pomFile.getParentFile().getCanonicalFile().getCanonicalPath(),
          excludedDir.getCanonicalPath())) {
        return true;
      }
    }
    return false;
  }
}
