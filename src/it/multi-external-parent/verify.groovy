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

// Check the main version was used by the plugin execution
def foundLines = new File("$basedir", "build.log").readLines().findAll { it =~ /fr.brouillard.oss.it.multi.with.extparents::main::0 -> 1.0.1-SNAPSHOT/ } 
assert 1 == foundLines.size

// Check the child version was used by the plugin execution
foundLines = new File("$basedir", "build.log").readLines().findAll { it =~ /fr.brouillard.oss.it.multi.with.extparents::child::0 -> 1.0.1-SNAPSHOT/ } 
assert 1 == foundLines.size

// Check the child with ext parent version was used by the plugin execution
foundLines = new File("$basedir", "build.log").readLines().findAll { it =~ /fr.brouillard.oss.it.multi.with.extparents::child-with-extparent::0 -> 1.0.1-SNAPSHOT/ } 
assert 1 == foundLines.size

// And check that the produced artifact was installed with the good version
File installedPomFile = new File("$basedir" + "/../../local-repo/fr/brouillard/oss/it/multi/with/extparents/main/1.0.1-SNAPSHOT/", "main-1.0.1-SNAPSHOT.pom")
assert installedPomFile.isFile()
assert 1 == installedPomFile.readLines().findAll { it =~ /<version>1.0.1-SNAPSHOT<\/version>/ }.size()

File installedJarPomFile = new File("$basedir" + "/../../local-repo/fr/brouillard/oss/it/multi/with/extparents/child/1.0.1-SNAPSHOT/", "child-1.0.1-SNAPSHOT.pom")
assert installedJarPomFile.isFile()
assert 2 == installedJarPomFile.readLines().findAll { it =~ /<version>1.0.1-SNAPSHOT<\/version>/ }.size()

installedJarPomFile = new File("$basedir" + "/../../local-repo/fr/brouillard/oss/it/multi/with/extparents/child-with-extparent/1.0.1-SNAPSHOT/", "child-with-extparent-1.0.1-SNAPSHOT.pom")
assert installedJarPomFile.isFile()
assert 1 == installedJarPomFile.readLines().findAll { it =~ /<version>1.0.1-SNAPSHOT<\/version>/ }.size()
assert 1 == installedJarPomFile.readLines().findAll { it =~ /<version>1.5.9.RELEASE<\/version>/ }.size()

return true
