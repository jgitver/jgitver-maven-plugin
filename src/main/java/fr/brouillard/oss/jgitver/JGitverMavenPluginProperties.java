// @formatter:off
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JGitverMavenPluginProperties {
    static Properties p = new Properties();
    static {
        try {
            InputStream is = JGitverMavenPluginProperties.class.getResourceAsStream("/META-INF/jgitver-maven-plugin-project.properties");
            if (is != null) {
                p.load(is);
            }
        } catch (IOException e) {
            // ignore
        }
    }

    public static String getVersion() {
        return p.getProperty("version", "Unknown");
    }

    public static String getSHA1() {
        return p.getProperty("sha1", "not git sha1");
    }
}
