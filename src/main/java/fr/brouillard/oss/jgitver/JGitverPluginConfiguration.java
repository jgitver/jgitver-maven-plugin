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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.codehaus.plexus.util.xml.Xpp3Dom;

class JGitverPluginConfiguration {
    private Optional<Xpp3Dom> pomPluginConfiguration;

    JGitverPluginConfiguration(Optional<Xpp3Dom> pomPluginConfiguration) {
        this.pomPluginConfiguration = pomPluginConfiguration;
    }
    
    boolean mavenLike() {
        return booleanConfigChild("mavenLike", true);
    }

    boolean autoIncrementPatch() {
        return booleanConfigChild("autoIncrementPatch", true);
    }

    boolean useCommitDistance() {
        return booleanConfigChild("useCommitDistance", true);
    }
    
    boolean useGitCommitId() {
        return booleanConfigChild("useGitCommitId", false);
    }
    
    boolean useDirty() {
        return booleanConfigChild("useDirty", false);
    }
    
    int gitCommitIdLength() {
        return intConfigChild("gitCommitIdLength", 8);
    }

    List<String> nonQualifierBranches() {
        return pomPluginConfiguration
          .map(node -> node.getChild("nonQualifierBranches").getChildren())
          .map(xpp3Dom -> Arrays.stream(xpp3Dom)
            .map(Xpp3Dom::getValue)
            .collect(Collectors.toList()))
          .orElse(Collections.singletonList("master"));
    }

    private boolean booleanConfigChild(String childName, boolean defaultValue) {
        return pomPluginConfiguration.map(node -> node.getChild(childName))
                .map(Xpp3Dom::getValue).map(Boolean::valueOf).orElse(Boolean.valueOf(defaultValue));
    }

    private int intConfigChild(String childName, int defaultValue) {
        return pomPluginConfiguration.map(node -> node.getChild(childName))
                .map(Xpp3Dom::getValue).map(Integer::parseInt).orElse(Integer.valueOf(defaultValue));
    }
}
