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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;
import org.apache.maven.MavenExecutionException;
import org.junit.Test;

public class Issue51Test {
  @Test
  public void can_load_correctly_provided_configuration()
      throws IOException, MavenExecutionException {
    InMemoryLogger inMemoryLogger = new InMemoryLogger();

    try (ResourceConfigurationProvider fromResource =
        ResourceConfigurationProvider.fromResource("/config/issue-51-cfg.xml")) {
      Configuration cfg =
          ConfigurationLoader.loadFromRoot(
              fromResource.getConfigurationDirectory(), inMemoryLogger);
      assertThat(cfg, notNullValue());

      List<BranchPolicy> branchPolicies = cfg.branchPolicies;

      assertThat(branchPolicies.size(), is(1));
      List<String> transformations = branchPolicies.get(0).transformations;
      assertThat(transformations.size(), is(1));
      assertThat(transformations.get(0), is("IDENTITY"));
    }
  }
}
