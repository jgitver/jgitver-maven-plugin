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
package fr.brouillard.oss.jgitver.cfg;

import fr.brouillard.oss.jgitver.JGitverUtils;
import org.apache.maven.MavenExecutionException;
import org.codehaus.plexus.logging.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationLoader {
    private static final String NAMESPACE_1_0_0_beta = "http://jgitver.github.io/maven/configuration/1.0.0-beta";
    private static final String NAMESPACE_1_0_0 = "http://jgitver.github.io/maven/configuration/1.0.0";
    private final Logger logger;

    private final List<File> configurationFiles;

    public ConfigurationLoader(File rootDirectory, Logger logger) {
        this.logger = logger;
        configurationFiles = new ArrayList<>();

        String cliConfigFile = System.getProperty(JGitverUtils.CLI.OVERRIDE_CONFIG_FILE);
        if (cliConfigFile != null) {
            logger.debug("jgitver configuration file overridden with " + cliConfigFile);
            configurationFiles.add(new File(cliConfigFile));
        }
        File extensionMavenCoreDirectory = new File(rootDirectory, ".mvn");
        File defaultConfigurationXml = new File(extensionMavenCoreDirectory, "jgitver.config.xml");
        File backwardCompatibleConfigurationFile = new File(extensionMavenCoreDirectory, "jgtiver.config.xml");

        configurationFiles.add(defaultConfigurationXml);
        configurationFiles.add(backwardCompatibleConfigurationFile);
    }

    public Configuration load() throws MavenExecutionException {
        for (File cfgFile: configurationFiles) {
            logger.debug("trying to load configuration from: " + cfgFile);
            Configuration c = loadFromFile(cfgFile, logger);
            if (c != null) {
                logger.info("Using jgitver configuration file: " + cfgFile);
                return c;
            }
        }

        logger.info("No suitable configuration file found, using defaults");
        return new Configuration();
    }

    /**
     * Loads a Configuration object from the root directory.
     * 
     * @param rootDirectory the root directory of the maven project
     * @param logger the logger to report activity
     * @return a non null Configuration object from the file
     *         $rootDirectory/.mvn/jgitver.config.xml or a default one with
     *         default values if the configuration file does not exist
     * @throws MavenExecutionException
     *             if the file exists but cannot be read correctly
     * @deprecated use new ConfigurationLoader(File, Logger).load() instead.
     */
    public static Configuration loadFromRoot(File rootDirectory, Logger logger) throws MavenExecutionException {
        return new ConfigurationLoader(rootDirectory, logger).load();
//        File extensionMavenCoreDirectory = new File(rootDirectory, ".mvn");
//        File configurationXml = new File(extensionMavenCoreDirectory, "jgitver.config.xml");
//        if (!configurationXml.canRead()) {
//            logger.debug("no configuration file found under " + configurationXml + ", looking under backwards-compatible file name");
//            configurationXml = new File(extensionMavenCoreDirectory, "jgtiver.config.xml");
//            if (!configurationXml.canRead()) {
//                logger.debug("no configuration file found under " + configurationXml + ", using defaults");
//                return new Configuration();
//            }
//        }
//
//        try {
//            logger.info("using jgitver configuration file: " + configurationXml);
//            Configuration c = loadFromFile(configurationXml);
//            return c;
//        } catch (Exception ex) {
//            throw new MavenExecutionException("cannot read configuration file " + configurationXml, ex);
//        }
    }

    private static Configuration loadFromFile(File configurationXml) throws MavenExecutionException {
        return loadFromFile(configurationXml, new InMemoryLogger());
    }

    private static Configuration loadFromFile(File configurationXml, Logger logger) throws MavenExecutionException {
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);

        if (configurationXml.exists()) {
            if (configurationXml.canRead()) {
                try {
                    return serializer.read(Configuration.class, configurationXml);
                } catch (Exception e) {
                    throw new MavenExecutionException("failure reading configuration from: " + configurationXml, e);
                }
            } else {
                logger.warn("jgitver configuration file " + configurationXml + " cannot be read, skipping it");
            }
        } else {
            logger.debug("jgitver configuration file " + configurationXml + " does not exists, skipping it");
        }

        return null;
    }
}
