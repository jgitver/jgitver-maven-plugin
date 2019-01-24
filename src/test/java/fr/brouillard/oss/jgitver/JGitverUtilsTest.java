/**
 * Copyright (C) 2019 Marco Jorge [https://github.com/marcobjorge/clock/tree/master] (marcobjorge@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.brouillard.oss.jgitver;

import junit.framework.Assert;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Properties;


public class JGitverUtilsTest {
    @Test
    public void normalizeSystemPropertyNameTest() {
        try {
            JGitverUtils.normalizeSystemPropertyName(null);
            Assert.fail("It's not possible to derive a compliant name from null");
        } catch (final IllegalStateException e) {
        }

        try {
            JGitverUtils.normalizeSystemPropertyName("   ");
            Assert.fail("It's not possible to derive a compliant name from an empty string");
        } catch (final IllegalStateException e) {
        }

        Assert.assertEquals("Non A-Z characters and underscore need to be replaced", "A__a___9_", JGitverUtils.normalizeSystemPropertyName("A%$a#%$9_"));
        Assert.assertEquals("Compliant names can start and finish with underscore", "_A__a___9_", JGitverUtils.normalizeSystemPropertyName("_A%$a#%$9_"));
        Assert.assertEquals("Compliant names cannot start with a digit", "__A__a___9_", JGitverUtils.normalizeSystemPropertyName("9_A%$a#%$9_"));
        Assert.assertEquals("Multiple initial digits cannot be collapsed", "_9A__a___9_", JGitverUtils.normalizeSystemPropertyName("99A%$a#%$9_"));
    }

    @Test
    public void getPropertyTest() {
        final Logger logger = new ConsoleLogger();

        final Properties userProperties = new Properties();
        userProperties.put("priority.1", "priority");
        userProperties.put("priority_1", "cannot be picked up");
        userProperties.put("priority_2", "priority");

        final Properties systemProperties = new Properties();
        userProperties.entrySet().forEach(e -> systemProperties.put(e.getKey(), "cannot be picked up"));
        userProperties.entrySet().forEach(e -> systemProperties.put("env." + e.getKey(), "cannot be picked up"));
        systemProperties.put("env.priority.3", "priority");
        systemProperties.put("env.priority_3", "cannot be picked up");
        systemProperties.put("priority.3", "cannot be picked up");
        systemProperties.put("priority_3", "cannot be picked up");
        systemProperties.put("env.priority_4", "priority");
        systemProperties.put("priority.4", "cannot be picked up");
        systemProperties.put("priority_4", "cannot be picked up");
        systemProperties.put("priority.5", "priority");
        systemProperties.put("priority_5", "cannot be picked up");
        systemProperties.put("priority_6", "priority");

        final MavenSession mavenSession = EasyMock.createMock(MavenSession.class);
        EasyMock.expect(mavenSession.getUserProperties()).andReturn(userProperties).anyTimes();
        EasyMock.expect(mavenSession.getSystemProperties()).andReturn(systemProperties).anyTimes();
        EasyMock.replay(mavenSession);

        Assert.assertEquals(
                "Exact match on user properties",
                "priority", JGitverUtils.getProperty(mavenSession, "priority.1", logger).get()
        );
        Assert.assertEquals(
                "Normalized match on user properties",
                "priority", JGitverUtils.getProperty(mavenSession, "priority.2", logger).get()
        );

        Assert.assertEquals(
                "Direct match on system properties as env property",
                "priority", JGitverUtils.getProperty(mavenSession, "priority.3", logger).get()
        );

        Assert.assertEquals(
                "Normalized match on system properties as env property",
                "priority", JGitverUtils.getProperty(mavenSession, "priority.4", logger).get()
        );

        Assert.assertEquals(
                "Direct match on system properties as system property",
                "priority", JGitverUtils.getProperty(mavenSession, "priority.5", logger).get()
        );

        Assert.assertEquals(
                "Normalized match on system properties as system property",
                "priority", JGitverUtils.getProperty(mavenSession, "priority.5", logger).get()
        );

        Assert.assertFalse(
                "Property is not defined",
                JGitverUtils.getProperty(mavenSession, "foo", logger).isPresent()
        );
    }
}