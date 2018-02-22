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

import fr.brouillard.oss.jgitver.metadata.Metadatas;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GitVersionCalculatorProxyTest {
    @Test
    public void can_proxy_calls() throws Exception {
        String expectedVersion = "1.0.0";
        File basedir = new File(System.getProperty("user.dir"));

        try (GitVersionCalculator gvc = GitVersionCalculator.location(basedir)) {
            JGitverInformationProvider infoProvider = Providers.fixVersion(expectedVersion, Providers.decorate(gvc));
            assertThat(expectedVersion, is(infoProvider.getVersion()));
            assertThat(expectedVersion, not(is(infoProvider.meta(Metadatas.CALCULATED_VERSION).get())));
        }
    }
}
