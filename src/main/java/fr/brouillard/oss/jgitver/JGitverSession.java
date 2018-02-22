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

import org.simpleframework.xml.*;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Root(name = "jgitver")
@Default(DefaultType.FIELD)
public class JGitverSession {
    @Transient
    private JGitverInformationProvider calculator;
    @Element(name = "calculatedVersion")
    private String version;
    @Element(name = "multiModuleProjectDirectory")
    private File multiModuleDirectory;
    @ElementList(name = "projects", entry = "gav")
    private Set<GAV> projects = new LinkedHashSet<>();

    /* jaxb constructor */
    JGitverSession() {
    }

    /**
     * Standard constructor using mandatory fields.
     * The class does not use final attributes dues to its jaxb nature that requires an empty constructor.
     * @param gitVersionCalculator the jgitver computation
     * @param multiModuleDirectory the base maven directory
     */
    public JGitverSession(JGitverInformationProvider gitVersionCalculator, File multiModuleDirectory) {
        this.version = gitVersionCalculator.getVersion();
        this.calculator = gitVersionCalculator;
        this.multiModuleDirectory = multiModuleDirectory;
    }

    public String getVersion() {
        return version;
    }

    public JGitverInformationProvider getCalculator() {
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
     * @throws IOException if the serialized form cannot be written
     * @see JGitverSession#serializeFrom(String)
     */
    public static String serializeTo(JGitverSession session) throws Exception {
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        StringWriter sw = new StringWriter();
        serializer.write(session, sw);
        return sw.toString();
    }

    /**
     * De-serializes the given string as a {@link JGitverSession}.
     * @param content the string to de-serialize
     * @return a non null configuration object
     * @throws Exception if the given string could not be interpreted by simplexml
     */
    public static JGitverSession serializeFrom(String content) throws Exception {
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        return serializer.read(JGitverSession.class, content);
    }
}
