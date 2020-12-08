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

import com.google.inject.AbstractModule;

/**
 * Guice binding for {@link fr.brouillard.oss.jgitver.JGitverConfiguration}.
 *
 * <p>Without a guice binding looking up the ProjectBuilder on other plugins will throw an exception
 * trying to load {@link fr.brouillard.oss.jgitver.JGitverConfiguration} (only started on maven
 * 3.6.3).
 */
public class JGitverGuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(JGitverConfiguration.class).to(JGitverConfigurationComponent.class);
  }
}
