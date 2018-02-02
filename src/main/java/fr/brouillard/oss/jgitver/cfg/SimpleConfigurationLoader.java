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
package fr.brouillard.oss.jgitver.cfg;

import org.apache.maven.MavenExecutionException;
import org.codehaus.plexus.logging.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.File;

public class SimpleConfigurationLoader {
    public static Configuration loadFromFile(File configurationXml) throws MavenExecutionException {
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);

        try {
            return serializer.read(Configuration.class, configurationXml);
        } catch (Exception e) {
            throw new MavenExecutionException("failure reading " + configurationXml, e);
        }
    }

    public static Configuration loadFromRoot(File rootDirectory, Logger logger) throws MavenExecutionException {
        File extensionMavenCoreDirectory = new File(rootDirectory, ".mvn");
        File configurationXml = new File(extensionMavenCoreDirectory, "jgitver.config.xml");
        if (!configurationXml.canRead()) {
            logger.debug("no configuration file found under " + configurationXml + ", looking under backwards-compatible file name");
            configurationXml = new File(extensionMavenCoreDirectory, "jgtiver.config.xml");
            if (!configurationXml.canRead()) {
                logger.debug("no configuration file found under " + configurationXml + ", using defaults");
                return new Configuration();
            }
        }

        return loadFromFile(configurationXml);
    }
}
