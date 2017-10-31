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

def log = new PrintWriter( new File(basedir, "verify.log").newWriter("UTF-8"), true )
log.println( "Verify started at: " + new Date() + " in: " + basedir )

[
  "chmod -R 755 .git",
  "rm -rf .git"
].each{ command ->

  def proc = command.execute(null, basedir)
  def sout = new StringBuilder(), serr = new StringBuilder()
  proc.waitForProcessOutput(sout, serr)

  log.println( "cmd: " + command )
  log.println( "out:" ) ; log.println( sout.toString().trim() )
  log.println( "err:" ) ; log.println( serr.toString().trim() )
  log.println( "ret: " + proc.exitValue() )

  assert proc.exitValue() == 0

}

def buildLog = new File(basedir, "build.log").readLines()

// Check the version was used by the plugin execution
def versionChanges = buildLog.findAll { it =~ /fr.brouillard.oss.it::issue-50-validate::0 -> 1.0.0/ }
log.println( "versionChanges: " + versionChanges )
assert 0 < versionChanges.size()

// And check that the pom in the produced artifact contains also the good version
URL jarFileURL = new File(basedir, "target/issue-50-validate-1.0.0.jar").toURI().toURL()
URL pomInJar = new URL("jar:" + jarFileURL + "!/META-INF/maven/fr.brouillard.oss.it/issue-50-validate/pom.xml")

def builtPomFileVersion = pomInJar.readLines().findAll { it =~ /<version>1.0.0<\/version>/ }
log.println( "builtPomFileVersion: " + builtPomFileVersion )
assert 1 == builtPomFileVersion.size()

log.println( "Verify completed at: " + new Date() )
log.close()
return true
