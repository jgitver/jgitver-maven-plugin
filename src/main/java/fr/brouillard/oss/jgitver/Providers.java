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

import java.util.Optional;

public class Providers {
    private Providers() {
    }

    public static JGitverInformationProvider decorate(final GitVersionCalculator calculator) {
        return new JGitverInformationProvider() {
            @Override
            public Version getVersionObject() {
                return calculator.getVersionObject();
            }

            @Override
            public Optional<String> meta(Metadatas meta) {
                return calculator.meta(meta);
            }
        };
    }

    public static JGitverInformationProvider fixVersion(final String version, final JGitverInformationProvider provider) {
        return new JGitverInformationProvider() {
            final Version fixed = Version.parse(version);
            @Override
            public Version getVersionObject() {
                return fixed;
            }

            @Override
            public Optional<String> meta(Metadatas meta) {
                return provider.meta(meta);
            }
        };
    }
}
