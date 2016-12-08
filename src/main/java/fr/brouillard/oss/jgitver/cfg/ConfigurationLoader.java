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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.maven.MavenExecutionException;
import org.codehaus.plexus.logging.Logger;
import org.xml.sax.SAXException;

import fr.brouillard.oss.jgitver.cfg.schema.ConfigurationSchema;

public class ConfigurationLoader {
    private static final String NAMESPACE = "http://jgitver.github.io/maven/configuration/1.0.0-beta";

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
     */
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

        try {
            logger.info("using jgitver configuration file: " + configurationXml);
            String configurationContent = Files.readAllLines(configurationXml.toPath()).stream().collect(Collectors.joining("\n"));

            Configuration c = loadConfiguration(configurationContent);
            return c;
        } catch (JAXBException | IOException | SAXException ex) {
            throw new MavenExecutionException("cannot read configuration file " + configurationXml, ex);
        }
    }

    private static Configuration loadConfiguration(String configurationContent) throws JAXBException, SAXException, IOException {
        JAXBContext jaxbContext;
        Unmarshaller unmarshaller;

        if (configurationContent.contains(NAMESPACE)) {
            jaxbContext = JAXBContext.newInstance(ConfigurationSchema.class);

            StreamSource contentStreamSource = new StreamSource(new StringReader(configurationContent));
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(ConfigurationLoader.class.getResource("/schemas/jgitver-configuration-v1_0_0-beta.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(contentStreamSource);
            unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            ConfigurationSchema cs = (ConfigurationSchema) unmarshaller.unmarshal(new StringReader(configurationContent));
            return cs.asConfiguration();
        } else {
            jaxbContext = JAXBContext.newInstance(Configuration.class);
            unmarshaller = jaxbContext.createUnmarshaller();
            return (Configuration) unmarshaller.unmarshal(new StringReader(configurationContent));
        }
    }
}
