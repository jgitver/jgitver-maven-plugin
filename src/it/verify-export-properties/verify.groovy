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
def exportedPropsFileStr = 'jgitver-output.properties'

File actions = new File(baseDir, "verify-actions.log")
actions.write 'Actions started at: ' + new Date() + '\n'

actions << 'rm -rf .git'.execute(null, baseDir).text

def logLines = new File("$basedir", "build.log").readLines()
foundLines = logLines.findAll { it =~ /Properties exported to/ }
assert 1 == foundLines.size

File exportedPropsFile = new File(basedir, exportedPropsFileStr)
assert exportedPropsFile.isFile()

Properties properties = new Properties()
exportedPropsFile.withInputStream {
    properties.load(it)
}

// The should be a decent amount of properties 
assert properties.size() >= 10




return true
