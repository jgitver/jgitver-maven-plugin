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
package fr.brouillard.oss.jgitver.mojos;

import java.util.List;

import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import fr.brouillard.oss.jgitver.JGitverUtils;

/**
 * jgitver plugin, should only be used as an extension. We use it for configuration purpose of the extension.
 */
@Mojo(name = "jgitver", defaultPhase = LifecyclePhase.NONE)
public class JGitverMojo extends AbstractMojo {
    @Parameter(property = "jgitver.mavenLike", defaultValue = "true")
    private Boolean mavenLike;

    @Parameter(property = "jgitver.autoIncrementPatch", defaultValue = "true")
    private Boolean autoIncrementPatch;

    @Parameter(property = "jgitver.useCommitDistance", defaultValue = "true")
    private Boolean useCommitDistance;

    @Parameter(property = "jgitver.useGitCommitId", defaultValue = "false")
    private Boolean useGitCommitId;

    @Parameter(property = "jgitver.gitCommitIdLength", defaultValue = "8")
    private Integer gitCommitIdLength;

    @Parameter(property = "jgitver.nonQualifierBranches", defaultValue = "master")
    private String nonQualifierBranches;

    @Parameter(property = "jgitver.nonQualifierBranchesList", defaultValue = "master")
    private List<String> nonQualifierBranchesList;

    @Parameter(property = "jgitver.useDirty", defaultValue = "false")
    private Boolean useDirty;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession mavenSession;

    @Override
    public void execute() throws MojoExecutionException {
        final Log logger = getLog();
        if (!JGitverUtils.shouldSkip(mavenSession)) {
            try {
                JGitverUtils.failAsOldMechanism(logger::warn);
            } catch (MavenExecutionException ex) {
                throw new MojoExecutionException("cannot use jgitver as maven plugin anymore", ex);
            }
        }
    }
}
