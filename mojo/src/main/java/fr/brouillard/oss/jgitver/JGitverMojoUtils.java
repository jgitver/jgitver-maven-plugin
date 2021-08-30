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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/** Misc utils used by the plugin. */
public final class JGitverMojoUtils {
  public static final String EXTENSION_PREFIX = "jgitver";
  public static final String EXTENSION_GROUP_ID = "fr.brouillard.oss";
  public static final String EXTENSION_ARTIFACT_ID = "jgitver-maven-plugin";
  public static final String EXTENSION_SKIP = EXTENSION_PREFIX + ".skip";
  public static final String EXTENSION_FORCE_COMPUTATION = EXTENSION_PREFIX + ".forceComputation";
  public static final String EXTENSION_FLATTEN = EXTENSION_PREFIX + ".flatten";
  public static final String EXTENSION_USE_VERSION = EXTENSION_PREFIX + ".use-version";
  public static final String SESSION_MAVEN_PROPERTIES_KEY = EXTENSION_PREFIX + ".session";
  public static final String PROJECT_VERSION = "${project.version}";

  public interface CLI {
    String OVERRIDE_CONFIG_FILE = EXTENSION_PREFIX + ".config";
  }

  private JGitverMojoUtils() {}

  /**
   * Loads initial model from pom file.
   *
   * @param pomFile pomFile.
   * @return Model.
   * @throws IOException IOException.
   * @throws XmlPullParserException XmlPullParserException.
   */
  private static Model loadInitialModel(File pomFile) throws IOException, XmlPullParserException {
    try (FileReader fileReader = new FileReader(pomFile)) {
      return new MavenXpp3Reader().read(fileReader);
    }
  }

  /**
   * Creates temporary file to save updated pom mode.
   *
   * @return File.
   * @throws IOException IOException.
   */
  private static File createPomDumpFile() throws IOException {
    File tmp = File.createTempFile("pom", ".jgitver-maven-plugin.xml");
    tmp.deleteOnExit();
    return tmp;
  }

  /**
   * Writes updated model to temporary pom file.
   *
   * @param mavenModel mavenModel.
   * @param pomFile pomFile.
   * @throws IOException IOException.
   */
  private static void writeModelPom(Model mavenModel, File pomFile) throws IOException {
    try (FileWriter fileWriter = new FileWriter(pomFile)) {
      new MavenXpp3Writer().write(fileWriter, mavenModel);
    }
  }

  /**
   * Changes basedir(dangerous).
   *
   * @param project project.
   * @param initialBaseDir initialBaseDir.
   * @throws NoSuchFieldException NoSuchFieldException.
   * @throws IllegalAccessException IllegalAccessException.
   */
  // It breaks the build process, because it changes the basedir to 'tmp'/... where other plugins
  // are not able to
  // find the classes and resources during the phases.
  private static void changeBaseDir(MavenProject project, File initialBaseDir)
      throws NoSuchFieldException, IllegalAccessException {
    Field basedirField = project.getClass().getField("basedir");
    basedirField.setAccessible(true);
    basedirField.set(project, initialBaseDir);
  }

  /**
   * Changes the pom file of the given project.
   *
   * @param project the project to change the pom
   * @param newPom the pom file to set on the project
   * @param logger a logger to use
   */
  private static void setProjectPomFile(MavenProject project, File newPom, Logger logger) {
    try {
      project.setPomFile(newPom);
    } catch (Throwable unused) {
      logger.warn("maven version might be <= 3.2.4, changing pom file using old mechanism");
      File initialBaseDir = project.getBasedir();
      project.setFile(newPom);
      File newBaseDir = project.getBasedir();
      try {
        if (!initialBaseDir.getCanonicalPath().equals(newBaseDir.getCanonicalPath())) {
          changeBaseDir(project, initialBaseDir);
        }
      } catch (Exception ex) {
        GAV gav = GAV.from(project);
        logger.warn("cannot reset basedir of project " + gav.toString(), ex);
      }
    }
  }


  /**
   * Attach modified POM files to the projects so install/deployed files contains new version.
   *
   * @param projects projects.
   * @param gavs list of registered GAVs of modified projects.
   * @param version the version to set
   * @param logger the logger to report to
   * @throws IOException if project model cannot be read correctly
   * @throws XmlPullParserException if project model cannot be interpreted correctly
   */
  public static void attachModifiedPomFilesToTheProject(
      List<MavenProject> projects,
      Set<GAV> gavs,
      String version,
      Boolean resolveProjectVersion,
      Logger logger)
      throws IOException, XmlPullParserException {
    for (MavenProject project : projects) {
      Model model = loadInitialModel(project.getFile());
      GAV initalProjectGAV = GAV.from(model); // SUPPRESS CHECKSTYLE AbbreviationAsWordInName

      logger.debug("about to change file pom for: " + initalProjectGAV);

      if (gavs.contains(initalProjectGAV)) {
        model.setVersion(version);

        if (model.getScm() != null && project.getModel().getScm() != null) {
          model.getScm().setTag(project.getModel().getScm().getTag());
        }
      }

      if (model.getParent() != null) {
        GAV parentGAV = GAV.from(model.getParent()); // SUPPRESS CHECKSTYLE AbbreviationAsWordInName

        if (gavs.contains(parentGAV)) {
          // parent has been modified
          model.getParent().setVersion(version);
        }
      }

      if (resolveProjectVersion) {
        resolveProjectVersionVariable(version, model);
      }

      File newPom = createPomDumpFile();
      writeModelPom(model, newPom);
      logger.debug("    new pom file created for " + initalProjectGAV + " under " + newPom);

      setProjectPomFile(project, newPom, logger);
      logger.debug("    pom file set");
    }
  }

  private static void resolveProjectVersionVariable(String version, Model model) {
    // resolve project.version in properties
    if (model.getProperties() != null) {
      for (Map.Entry<Object, Object> entry : model.getProperties().entrySet()) {
        if (PROJECT_VERSION.equals(entry.getValue())) {
          entry.setValue(version);
        }
      }
    }

    // resolve project.version in dependencies
    if (model.getDependencies() != null) {
      for (Dependency dependency : model.getDependencies()) {
        if (PROJECT_VERSION.equals(dependency.getVersion())) {
          dependency.setVersion(version);
        }
      }
    }

    // resole project.version in dependencyManagement
    if (model.getDependencyManagement() != null
        && model.getDependencyManagement().getDependencies() != null) {
      for (Dependency dependency : model.getDependencyManagement().getDependencies()) {
        if (PROJECT_VERSION.equals(dependency.getVersion())) {
          dependency.setVersion(version);
        }
      }
    }

    // resolve project.version in plugins
    if (model.getBuild() != null && model.getBuild().getPlugins() != null) {
      for (Plugin plugin : model.getBuild().getPlugins()) {
        if (plugin.getDependencies() != null) {
          for (Dependency dependency : plugin.getDependencies()) {
            if (PROJECT_VERSION.equals(dependency.getVersion())) {
              dependency.setVersion(version);
            }
          }
        }
      }
    }

    // resolve project.version in pluginManagement
    if (model.getBuild() != null
        && model.getBuild().getPluginManagement() != null
        && model.getBuild().getPluginManagement().getPlugins() != null) {
      for (Plugin plugin : model.getBuild().getPluginManagement().getPlugins()) {
        if (plugin.getDependencies() != null) {
          for (Dependency dependency : plugin.getDependencies()) {
            if (PROJECT_VERSION.equals(dependency.getVersion())) {
              dependency.setVersion(version);
            }
          }
        }
      }
    }
  }
}
