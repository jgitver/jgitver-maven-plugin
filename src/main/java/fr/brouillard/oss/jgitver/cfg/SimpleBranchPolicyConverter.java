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

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class SimpleBranchPolicyConverter implements Converter<BranchPolicy> {
    @Override
    public BranchPolicy read(InputNode node) throws Exception {
        BranchPolicy bp = new BranchPolicy();
        bp.transformations.clear();

        InputNode patternNode = node.getNext("pattern");
        if (patternNode != null) {
            bp.pattern = patternNode.getValue();
        }

        InputNode transformations = node.getNext("transformations");
        if (transformations != null) {
            InputNode transformationNode;
            while ((transformationNode = transformations.getNext("transformation")) != null) {
                bp.transformations.add(transformationNode.getValue());
            }
        }

        return bp;
    }

    @Override
    public void write(OutputNode node, BranchPolicy policy) throws Exception {
        node.getChild("pattern").setValue(policy.pattern);
        OutputNode transformationsNode = node.getChild("transformations");
        for (String transformation: policy.transformations) {
            transformationsNode.getChild("transformation").setValue(transformation);
        }
    }
}
