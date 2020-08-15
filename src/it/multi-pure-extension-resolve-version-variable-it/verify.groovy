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
def baseDir = new File("$basedir")

File actions = new File(baseDir, "verify-actions.log")
actions.write 'Actions started at: ' + new Date() + '\n'

actions << 'rm -rf .git'.execute(null, baseDir).text

// Check the version was used by the plugin execution
def foundLines = new File("$basedir", "build.log").readLines().findAll { it =~ /fr.brouillard.oss.it.resolve-project-version::multi-pure-extension-it::0 -> 1.0.1-SNAPSHOT/ }
assert 0 < foundLines.size

// And check that the produced artifact was installed with the good version
File installedPomFile = new File("$basedir" + "/../../local-repo/fr/brouillard/oss/it/resolve-project-version/multi-pure-extension-it/1.0.1-SNAPSHOT/", "multi-pure-extension-it-1.0.1-SNAPSHOT.pom")
assert installedPomFile.isFile()
assert 4 == installedPomFile.readLines().findAll { it =~ /<version>1.0.1-SNAPSHOT<\/version>/ }.size()
assert 1 == installedPomFile.readLines().findAll { it =~ /<calculated-project.version>1.0.1-SNAPSHOT<\/calculated-project.version>/ }.size()

File installedJarPomFile = new File("$basedir" + "/../../local-repo/fr/brouillard/oss/it/resolve-project-version/multi-pure-extension-it-module/1.0.1-SNAPSHOT/", "multi-pure-extension-it-module-1.0.1-SNAPSHOT.pom")
assert installedJarPomFile.isFile()
assert 2 == installedJarPomFile.readLines().findAll { it =~ /<version>1.0.1-SNAPSHOT<\/version>/ }.size()

File installedJarFile = new File("$basedir" + "/../../local-repo/fr/brouillard/oss/it/resolve-project-version/multi-pure-extension-it-module/1.0.1-SNAPSHOT/", "multi-pure-extension-it-module-1.0.1-SNAPSHOT.jar")
assert installedJarFile.isFile()

File installedWarPomFile = new File("$basedir" + "/../../local-repo/fr/brouillard/oss/it/resolve-project-version/multi-pure-extension-it-app/1.0.1-SNAPSHOT/", "multi-pure-extension-it-app-1.0.1-SNAPSHOT.pom")
assert installedWarPomFile.isFile()
assert 3 == installedWarPomFile.readLines().findAll { it =~ /<version>1.0.1-SNAPSHOT<\/version>/ }.size()

File installedWarFile = new File("$basedir" + "/../../local-repo/fr/brouillard/oss/it/resolve-project-version/multi-pure-extension-it-app/1.0.1-SNAPSHOT/", "multi-pure-extension-it-app-1.0.1-SNAPSHOT.war")
assert installedWarFile.isFile()
return true
