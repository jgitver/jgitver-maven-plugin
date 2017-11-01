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
package fr.brouillard.oss.jgitver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "jgitver")
@XmlAccessorType(XmlAccessType.FIELD)
public class JGitverSession {
    @XmlTransient
    private GitVersionCalculator calculator;
    @XmlElement(name = "calculatedVersion")
    private String version;
    @XmlElement(name = "multiModuleProjectDirectory")
    private File multiModuleDirectory;
    @XmlElement(name = "projectsGAV")
    private Set<GAV> projects = new LinkedHashSet<>();

    /* jaxb constructor */
    JGitverSession() {
    }

    public JGitverSession(GitVersionCalculator gitVersionCalculator, File multiModuleDirectory) {
        this.version = gitVersionCalculator.getVersion();
        this.calculator = gitVersionCalculator;
        this.multiModuleDirectory = multiModuleDirectory;
    }

    public String getVersion() {
        return version;
    }

    public GitVersionCalculator getCalculator() {
        return calculator;
    }

    public File getMultiModuleDirectory() {
        return multiModuleDirectory;
    }

    public void addProject(GAV project) {
        projects.add(project);
    }

    public  Set<GAV> getProjects() {
        return Collections.unmodifiableSet(projects);
    }

    /**
     * Serializes as a String the given configuration object.
     * @param session the object to serialize
     * @return a non null String representation of the given object serialized
     * @throws JAXBException in case the given object could not be serialized by JAXB
     * @throws IOException if the serialized form cannot be written
     * @see JGitverSession#serializeFrom(String)
     */
    public static String serializeTo(JGitverSession session) throws
            JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(JGitverSession.class, GAV.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream)) {
            marshaller.marshal(session, bufferedOutputStream);
        }

        return byteArrayOutputStream.toString();
    }

    /**
     * De-serializes the given string as a {@link JGitverSession}.
     * @param content the string to de-serialize
     * @return a non null configuration object
     * @throws JAXBException if the given string could not be interpreted by JAXB
     * @throws IOException if the content of the serialized object could not be read in memory
     */
    public static JGitverSession serializeFrom(String content) throws JAXBException,
            IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(JGitverSession.class, GAV.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        JGitverSession session;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream)) {
            session = (JGitverSession) unmarshaller.unmarshal(bufferedInputStream);
        }

        return session;
    }
}
