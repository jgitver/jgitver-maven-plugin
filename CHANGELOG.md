# jgitver-maven-plugin changelog

Changelog of [jgitver-maven-plugin](https://github.com/jgitver/jgitver-maven-plugin) project.

## 1.4.4
### GitHub [#101](https://github.com/jgitver/jgitver-maven-plugin/issues/101) &gt;=1.4.0 freezes in life-lock when trying to edit configuration

**allow to configure the maximum search depth in commit graph**

 * for big repositories with plenty of path without tags the look for tags action will stop at this depth
 * #fixes #101

[f7ed5bc143004e2](https://github.com/jgitver/jgitver-maven-plugin/commit/f7ed5bc143004e2) Matthieu Brouillard *2018-12-07 08:20:43*


### GitHub [#102](https://github.com/jgitver/jgitver-maven-plugin/issues/102) study usage of maven-flatten-plugin

**introduce alternative usage of maven-flatten-plugin**

 * via activation of -Djgitver.flatten=true, maven-flatten-plugin will be used instead of
 * the own jgitver mechanism to materialize the pom from the POM (object model)
 * jgitver configuration `skipPomUpdate`, will additionally allow to users to tell jgitver
 * not to try to add its own maven-flatten-plugin execution. this parameter is usefull when
 * for example you have a parent pom defining maven-flatten-plugin
 * if jgitver.flatten=true and skipPomUpdate=false, then jgitver will look your pom to find
 * for an already defined maven-flatten-plugin execution, if one is found it stop there
 * otherwise jgitver will add its own execution to publish the pom.xml
 * fixes #102

[e851ee7c3e081ab](https://github.com/jgitver/jgitver-maven-plugin/commit/e851ee7c3e081ab) Matthieu Brouillard *2018-11-30 15:21:17*


### GitHub [#106](https://github.com/jgitver/jgitver-maven-plugin/issues/106) compilation failure on java 11

**add a dependency to jaxb-api to have the annotations in the classpath to be able to build to build on java11**

 * fixes #106

[01d6f38743f2cd4](https://github.com/jgitver/jgitver-maven-plugin/commit/01d6f38743f2cd4) Matthieu Brouillard *2018-11-30 07:29:12*


### GitHub [#107](https://github.com/jgitver/jgitver-maven-plugin/issues/107) switch dependency checks from ossindex to snyk.io

**replace ossindex by snyk.io**

 * fixes #107

[826043a0f29ac4f](https://github.com/jgitver/jgitver-maven-plugin/commit/826043a0f29ac4f) Matthieu Brouillard *2018-11-30 14:03:30*


## 1.4.2
### GitHub [#61](https://github.com/jgitver/jgitver-maven-plugin/issues/61) Java 1.7

**update jgitver dependency to 0.8.4 due to jgitver/jgitver#61**


[a883627a1f878dc](https://github.com/jgitver/jgitver-maven-plugin/commit/a883627a1f878dc) Matthieu Brouillard *2018-11-05 19:54:16*


## 1.4.1
### GitHub [#100](https://github.com/jgitver/jgitver-maven-plugin/issues/100) add enforce rule to force JDK8 when releasing

**add enforcer rules to protect releases**

 * fixes #99
 * fixes #100

[9d223d34a02facb](https://github.com/jgitver/jgitver-maven-plugin/commit/9d223d34a02facb) Matthieu Brouillard *2018-11-05 06:35:32*


### GitHub [#99](https://github.com/jgitver/jgitver-maven-plugin/issues/99) add enforce rule to publish without branch qualifier when releasing

**add enforcer rules to protect releases**

 * fixes #99
 * fixes #100

[9d223d34a02facb](https://github.com/jgitver/jgitver-maven-plugin/commit/9d223d34a02facb) Matthieu Brouillard *2018-11-05 06:35:32*


## 1.4.0
### GitHub [#91](https://github.com/jgitver/jgitver-maven-plugin/issues/91) Add default exclude / warning for &quot;.m2&quot; and/or &quot;repository&quot;

**add some documentation on ignoring subdiroctories with MLR**

 * fixes #91

[3dd317b9a6fee27](https://github.com/jgitver/jgitver-maven-plugin/commit/3dd317b9a6fee27) Matthieu Brouillard *2018-07-09 08:40:06*


### GitHub [#92](https://github.com/jgitver/jgitver-maven-plugin/issues/92) update to jgitver 0.6.1

**update to jgitver 0.6.1**

 * fixes #92

[099c2b581d222a0](https://github.com/jgitver/jgitver-maven-plugin/commit/099c2b581d222a0) Matthieu Brouillard *2018-07-10 05:55:33*


### GitHub [#98](https://github.com/jgitver/jgitver-maven-plugin/issues/98) support merges and max version resolution with jgitver-0.8.2

**correct support of merges with usage of jgitver 0.8.2**

 * fixes #98

[f3e7eab307bd10b](https://github.com/jgitver/jgitver-maven-plugin/commit/f3e7eab307bd10b) Matthieu Brouillard *2018-11-04 11:20:40*


## 1.3.0
### GitHub [#88](https://github.com/jgitver/jgitver-maven-plugin/issues/88) NPE when run without pom.xml

**ignoring jgitver results when project has no basedir**

 * fixes #88

[6a65666ff2aa32b](https://github.com/jgitver/jgitver-maven-plugin/commit/6a65666ff2aa32b) Matthieu Brouillard *2018-07-02 17:21:46*


### GitHub [#89](https://github.com/jgitver/jgitver-maven-plugin/issues/89) document external branch name on detached HEAD

**enhance README with external branch name providing on detached HEAD**

 * fixes #89

[ef8eec9f820d662](https://github.com/jgitver/jgitver-maven-plugin/commit/ef8eec9f820d662) Matthieu Brouillard *2018-07-05 09:04:58*


## 1.2.1
### GitHub [#79](https://github.com/jgitver/jgitver-maven-plugin/issues/79) allow to use externally defined version

**support external version definition**

 * fixes #79

[d28befa79d11d1c](https://github.com/jgitver/jgitver-maven-plugin/commit/d28befa79d11d1c) Matthieu Brouillard *2018-02-22 14:52:20*


## 1.2.0
### GitHub [#75](https://github.com/jgitver/jgitver-maven-plugin/issues/75) support for configuration options from command line

**add support for external configuration file**

 * by using `-Djgitver.config=PATH_TO_CONFIG_FILE` default configuration file
 * can be overriden in favor of another file
 * fixes #75

[b09df632a763d72](https://github.com/jgitver/jgitver-maven-plugin/commit/b09df632a763d72) Matthieu Brouillard *2018-02-08 18:19:52*


### GitHub [#76](https://github.com/jgitver/jgitver-maven-plugin/issues/76) expose jgitver-maven-plugin version inside the build

**expose jgitver-maven-plugin version**

 * fixes #76

[48d47dbd374d195](https://github.com/jgitver/jgitver-maven-plugin/commit/48d47dbd374d195) Matthieu Brouillard *2018-02-02 13:53:43*


## v1.2.0
### GitHub [#74](https://github.com/jgitver/jgitver-maven-plugin/issues/74) Plugin not compatible with java 9

**update plexus-component-metadata with a java 9 compliant version**

 * fixes #74

[5b7e68e91a7bcd0](https://github.com/jgitver/jgitver-maven-plugin/commit/5b7e68e91a7bcd0) Matthieu Brouillard *2018-02-02 13:00:58*


## 1.1.7
### GitHub [#70](https://github.com/jgitver/jgitver-maven-plugin/issues/70) Intellij + jgitver + multi-module reports problem &quot;detection of jgitver old setting mechanism&quot;

**skip evaluation also looks system properties**

 * closes #70

[55e14cbc032e4f2](https://github.com/jgitver/jgitver-maven-plugin/commit/55e14cbc032e4f2) Matthieu Brouillard *2018-01-16 07:25:16*


## 1.1.6
### GitHub [#70](https://github.com/jgitver/jgitver-maven-plugin/issues/70) Intellij + jgitver + multi-module reports problem &quot;detection of jgitver old setting mechanism&quot;

**support skip parameter even on old plugin mechanism**

 * allows to bypass jgitver in IDE not supporting it while loading the projects
 * in the IDE.
 * By setting `jgitver.skip`, no failure is raised anymore.
 * For Intellij IDEA:
 * - go into Settings &gt; Buld, Execution, Deployment &gt; Build Tools &gt; Maven &gt; Importing
 * - in VM options for importer add &quot;-Djgitver.skip=true&quot;
 * fixes #70

[93723b213426c5a](https://github.com/jgitver/jgitver-maven-plugin/commit/93723b213426c5a) Matthieu Brouillard *2017-12-12 07:35:41*


## 1.1.5
### GitHub [#69](https://github.com/jgitver/jgitver-maven-plugin/issues/69) Nice to have jgitver.commit_distance

**introduce COMMIT_DISTANCE as exposed properties**

 * update to jgitver-0.5.1
 * fixes #69

[1fb73a87737160d](https://github.com/jgitver/jgitver-maven-plugin/commit/1fb73a87737160d) Matthieu Brouillard *2017-12-05 18:47:45*


### Jira jgitver-0 

**introduce COMMIT_DISTANCE as exposed properties**

 * update to jgitver-0.5.1
 * fixes #69

[1fb73a87737160d](https://github.com/jgitver/jgitver-maven-plugin/commit/1fb73a87737160d) Matthieu Brouillard *2017-12-05 18:47:45*


## 1.1.4
### GitHub [#65](https://github.com/jgitver/jgitver-maven-plugin/issues/65) Use commit timestamp in version

**allow usage of commit timestamp as version qualifier**

 * update to jgitver 0.5.0
 * enhance configuration to use version 1.0.0 of xml schema
 * fixes #65

[f6978c916e6ac0d](https://github.com/jgitver/jgitver-maven-plugin/commit/f6978c916e6ac0d) Matthieu Brouillard *2017-11-20 19:44:20*


### GitHub [#66](https://github.com/jgitver/jgitver-maven-plugin/issues/66) docker build need a very recent version of git

**simplify travis build, remove docker**

 * fixes #66

[dc658912fec682f](https://github.com/jgitver/jgitver-maven-plugin/commit/dc658912fec682f) Matthieu Brouillard *2017-11-21 08:43:51*


## 1.1.3
### GitHub [#64](https://github.com/jgitver/jgitver-maven-plugin/issues/64) it-tests are failing for issue-30-* tests

**correct test for failure for files being ignored by configuration**

 * fixes #64

[212187df5ff070d](https://github.com/jgitver/jgitver-maven-plugin/commit/212187df5ff070d) Matthieu Brouillard *2017-11-02 12:27:16*


## 1.1.0
### GitHub [#35](https://github.com/jgitver/jgitver-maven-plugin/issues/35) Add support for SCM tag node update in POM files

**handle project>scm>tag in pom**

 * fixes #35

[b6f7438f7853e9d](https://github.com/jgitver/jgitver-maven-plugin/commit/b6f7438f7853e9d) Matthieu Brouillard *2017-11-01 12:22:37*


### GitHub [#62](https://github.com/jgitver/jgitver-maven-plugin/issues/62) Enable profile if release (of if dirty)

**standardize & correct it tests**

 * use .gitgnore in it tests
 * replace non working redirections in prebuild.groovy files with random file
 * generation
 * closes #62

[5408a5aa2859709](https://github.com/jgitver/jgitver-maven-plugin/commit/5408a5aa2859709) Matthieu Brouillard *2017-10-31 17:58:41*

**provide tests for #62**


[881603d3deca0da](https://github.com/jgitver/jgitver-maven-plugin/commit/881603d3deca0da) Matthieu Brouillard *2017-10-31 15:31:06*

**refactoring to prepare #62**


[5abc77b6dd9d28d](https://github.com/jgitver/jgitver-maven-plugin/commit/5abc77b6dd9d28d) Matthieu Brouillard *2017-10-31 13:31:13*


### GitHub [#63](https://github.com/jgitver/jgitver-maven-plugin/issues/63) JGITVER_LATEST_VERSION is incorrectly calculated in install.sh

**update fetch version command for python3 compatibility**

 * json formatting in python3 adds an unexpected comma that is now removed
 * fixes #63

[7682c25c9862980](https://github.com/jgitver/jgitver-maven-plugin/commit/7682c25c9862980) Matthieu Brouillard *2017-11-01 08:54:44*


### Jira issue-62 

**use .gitignore in issue-62.2 it test**


[2156d6a3c143916](https://github.com/jgitver/jgitver-maven-plugin/commit/2156d6a3c143916) Matthieu Brouillard *2017-10-31 16:57:00*


## 1.0.0
### GitHub [#44](https://github.com/jgitver/jgitver-maven-plugin/issues/44) allow to fail build when dirty

**add failIfDirty configuration parameter, closes #44**


[80f2d5d876f38cb](https://github.com/jgitver/jgitver-maven-plugin/commit/80f2d5d876f38cb) Matthieu Brouillard *2016-12-08 21:36:41*


### GitHub [#47](https://github.com/jgitver/jgitver-maven-plugin/issues/47) cleanup old plugin code

**cleanup for old plugin usage, fixes #47**


[1f065e535a627ab](https://github.com/jgitver/jgitver-maven-plugin/commit/1f065e535a627ab) Matthieu Brouillard *2016-12-23 11:03:29*


### GitHub [#48](https://github.com/jgitver/jgitver-maven-plugin/issues/48) Release Notes

**introduce CHANGELOG.md, fix #48**


[e4cec66c86da310](https://github.com/jgitver/jgitver-maven-plugin/commit/e4cec66c86da310) Matthieu Brouillard *2017-02-23 17:43:51*


### GitHub [#50](https://github.com/jgitver/jgitver-maven-plugin/issues/50) Run &quot;attach-modified-poms&quot; earlier

**run goal 'attach-modified-poms' as first plugin of 'prepare-package' phase**

 * by default goal &#39;attach-modified-poms&#39; is now run during phase &#39;prepare-package&#39;
 * System property &quot;jgitver.pom-replacement-phase&quot;, allows to control this default phase.
 * For example running: mvn install -Djgitver.pom-replacement-phase=validate will execute
 * the goal as the very first goal during a normal build flow.
 * fix #50

[60a5b787b2253a0](https://github.com/jgitver/jgitver-maven-plugin/commit/60a5b787b2253a0) Matthieu Brouillard *2017-02-20 17:05:52*


### GitHub [#51](https://github.com/jgitver/jgitver-maven-plugin/issues/51) IDENTITY BranchNameTransformation still replaces dashes with underscores in branchname

**correct loading of BranchPolicy objects when an XML schema is used**

 * fix #51

[3792fa3ee435152](https://github.com/jgitver/jgitver-maven-plugin/commit/3792fa3ee435152) Matthieu Brouillard *2017-02-24 11:51:34*


### GitHub [#58](https://github.com/jgitver/jgitver-maven-plugin/issues/58) Running jgitver should be optional

**introduce 'jgitver.skip' user property to skip plugin execution**

 * By launching maven with &#39;-Djgitver.skip=true&#39;, you can totally skip the plugin execution.
 * fixes #58

[770992b382c3d2d](https://github.com/jgitver/jgitver-maven-plugin/commit/770992b382c3d2d) Matthieu Brouillard *2017-09-12 07:11:11*


### GitHub [#59](https://github.com/jgitver/jgitver-maven-plugin/issues/59) Remove maven-jgit-buildnumber-plugin usage

**use jgitver property 'jgitver.git_sha1_full' for git SHA1 jar manifest entry**

 * fixes #59

[80154c263f2d589](https://github.com/jgitver/jgitver-maven-plugin/commit/80154c263f2d589) Matthieu Brouillard *2017-09-13 05:56:00*


## 0.4.0
### GitHub [#37](https://github.com/jgitver/jgitver-maven-plugin/issues/37) It would be helpful to have .xsd for jgitver.config.xml

**provide xml-schema for jgitver maven configuration file, fixes #37**


[de0ace338eb8b77](https://github.com/jgitver/jgitver-maven-plugin/commit/de0ace338eb8b77) Matthieu Brouillard *2016-12-02 22:09:26*


### GitHub [#41](https://github.com/jgitver/jgitver-maven-plugin/issues/41) allow configure regexp for tags recognition

**allow to configure tag version regexp, fixes #41**


[ccb7a67bddb4bc1](https://github.com/jgitver/jgitver-maven-plugin/commit/ccb7a67bddb4bc1) Matthieu Brouillard *2016-12-03 00:33:38*


## 0.3.1
### GitHub [#33](https://github.com/jgitver/jgitver-maven-plugin/issues/33) Version-Number modified problem with nexus-staging-maven-plugin

**moving to jgitver-maven-plugin-0.3.0, cleanup with usage of exclusions inside jgtiver.config.xml, see #33**


[3bbd806b8dbb79b](https://github.com/jgitver/jgitver-maven-plugin/commit/3bbd806b8dbb79b) Matthieu Brouillard *2016-08-05 14:29:39*


### GitHub [#36](https://github.com/jgitver/jgitver-maven-plugin/issues/36) Configuration option &#39;useDirty&#39; doesn&#39;t make any effect.

**update to jgitver 0.2.1, handle dirty state, closes #36**


[06b6902f4f8fc57](https://github.com/jgitver/jgitver-maven-plugin/commit/06b6902f4f8fc57) Matthieu Brouillard *2016-11-11 11:54:14*


### Jira plugin-0 

**moving to jgitver-maven-plugin-0.3.0, cleanup with usage of exclusions inside jgtiver.config.xml, see #33**


[3bbd806b8dbb79b](https://github.com/jgitver/jgitver-maven-plugin/commit/3bbd806b8dbb79b) Matthieu Brouillard *2016-08-05 14:29:39*


## 0.3.0
### GitHub [#16](https://github.com/jgitver/jgitver-maven-plugin/issues/16) Improved support for GitFlow like workflows: Differences between Branches

**add support for branching policy, see #16, fixes #32**


[25f0fde19e7ec1f](https://github.com/jgitver/jgitver-maven-plugin/commit/25f0fde19e7ec1f) Matthieu Brouillard *2016-08-04 21:14:55*


### GitHub [#32](https://github.com/jgitver/jgitver-maven-plugin/issues/32) allow configuration of branching policies

**add support for branching policy, see #16, fixes #32**


[25f0fde19e7ec1f](https://github.com/jgitver/jgitver-maven-plugin/commit/25f0fde19e7ec1f) Matthieu Brouillard *2016-08-04 21:14:55*


### GitHub [#33](https://github.com/jgitver/jgitver-maven-plugin/issues/33) Version-Number modified problem with nexus-staging-maven-plugin

**introduce directories exclusion pattern, fixes #33**


[fed0143ea0e6620](https://github.com/jgitver/jgitver-maven-plugin/commit/fed0143ea0e6620) Matthieu Brouillard *2016-08-04 20:41:12*


### Jira jgitver-0 

**update to jgitver-0.2.0, self use jgitver-maven-plugin-0.3.0-alpha4**


[ebafcbadbb54fc3](https://github.com/jgitver/jgitver-maven-plugin/commit/ebafcbadbb54fc3) Matthieu Brouillard *2016-08-04 21:29:04*


### Jira plugin-0 

**update to jgitver-0.2.0, self use jgitver-maven-plugin-0.3.0-alpha4**


[ebafcbadbb54fc3](https://github.com/jgitver/jgitver-maven-plugin/commit/ebafcbadbb54fc3) Matthieu Brouillard *2016-08-04 21:29:04*


## 0.3.0-alpha4
### GitHub [#29](https://github.com/jgitver/jgitver-maven-plugin/issues/29) missing explicit compile dependency to commons-lang3

**correct old extension/plugin IT tests, relates to #29**


[4013ebb2316ad59](https://github.com/jgitver/jgitver-maven-plugin/commit/4013ebb2316ad59) Matthieu Brouillard *2016-08-03 08:17:17*

**explicit declaration of commons-lang3, fixes #29**


[306bd9086fc01a5](https://github.com/jgitver/jgitver-maven-plugin/commit/306bd9086fc01a5) Matthieu Brouillard *2016-08-03 07:18:55*


### GitHub [#30](https://github.com/jgitver/jgitver-maven-plugin/issues/30) NullPointerException in JGitverModelProcessor.provisionModel when POM unavailable.

**Source.getLocation may return either a URI or a file path, protect against that, fixes #30, closes #31**


[36d50366477d09a](https://github.com/jgitver/jgitver-maven-plugin/commit/36d50366477d09a) Jeremy Heiner *2016-08-04 10:09:50*


### GitHub [#31](https://github.com/jgitver/jgitver-maven-plugin/pull/31) Fixes #30

**Source.getLocation may return either a URI or a file path, protect against that, fixes #30, closes #31**


[36d50366477d09a](https://github.com/jgitver/jgitver-maven-plugin/commit/36d50366477d09a) Jeremy Heiner *2016-08-04 10:09:50*


### Jira issue-30 

**add empty file to .mvn in issue-30-pre so that git does not remove the empty directory**


[d6b86e425ea2d0c](https://github.com/jgitver/jgitver-maven-plugin/commit/d6b86e425ea2d0c) Matthieu Brouillard *2016-08-04 10:50:23*


## 0.3.0-alpha3
### GitHub [#26](https://github.com/jgitver/jgitver-maven-plugin/issues/26) pom files install/deploy do not contain the good version for project with parent element

**allow pom attachement even when root module has a parent, fixes #26**


[7c8d3d72e4c13e1](https://github.com/jgitver/jgitver-maven-plugin/commit/7c8d3d72e4c13e1) Matthieu Brouillard *2016-07-27 15:58:31*


## 0.3.0-alpha2
### GitHub [#25](https://github.com/jgitver/jgitver-maven-plugin/issues/25) jgitver-maven-plugin does not correctly report groupId &amp; version for multi modules

**use groupId & version from parent pom when building a GAV object from a model, fixes #25**


[0f2d7afe68ba44f](https://github.com/jgitver/jgitver-maven-plugin/commit/0f2d7afe68ba44f) Matthieu Brouillard *2016-07-26 11:17:51*


## v0.3.0
### GitHub [#17](https://github.com/jgitver/jgitver-maven-plugin/issues/17) Expose more clearly minimal maven requirements

**expose maven requirements, fixes #17**


[fded00a25212a30](https://github.com/jgitver/jgitver-maven-plugin/commit/fded00a25212a30) Matthieu Brouillard *2016-07-07 13:09:56*


### GitHub [#18](https://github.com/jgitver/jgitver-maven-plugin/issues/18) activate IT tests on travis-ci build

**enhance IT tests, make them running on travis-ci, closes #18**


[ebe1ac8b52b1ba5](https://github.com/jgitver/jgitver-maven-plugin/commit/ebe1ac8b52b1ba5) Matthieu Brouillard *2016-07-07 12:15:37*


## 0.2.0-alpha2
### GitHub [#12](https://github.com/jgitver/jgitver-maven-plugin/issues/12) Annotated and lightweight tag on same commit result in -SNAPSHOT version

**fix version of dependencies & plugins, fixes #12**


[a1f852cfc056961](https://github.com/jgitver/jgitver-maven-plugin/commit/a1f852cfc056961) Matthieu Brouillard *2016-07-03 09:38:23*


## 0.2.0-alpha1
### GitHub [#12](https://github.com/jgitver/jgitver-maven-plugin/issues/12) Annotated and lightweight tag on same commit result in -SNAPSHOT version

**update to jgitver >= 0.2.0-alpha1, introduce maven properties, fixes #7, fixes #12**


[2458956a5277479](https://github.com/jgitver/jgitver-maven-plugin/commit/2458956a5277479) Matthieu Brouillard *2016-07-01 12:32:36*


### GitHub [#7](https://github.com/jgitver/jgitver-maven-plugin/issues/7) provide git metadata as maven properties

**update to jgitver >= 0.2.0-alpha1, introduce maven properties, fixes #7, fixes #12**


[2458956a5277479](https://github.com/jgitver/jgitver-maven-plugin/commit/2458956a5277479) Matthieu Brouillard *2016-07-01 12:32:36*


### GitHub [#8](https://github.com/jgitver/jgitver-maven-plugin/issues/8) Using the Maven Plugin makes IntelliJ Maven support useless

**add notice/warning for IntelliJ IDEA users, enhance README to bypass IDEA-155733 as in #8, fixes #9**


[fadd88e04b25c79](https://github.com/jgitver/jgitver-maven-plugin/commit/fadd88e04b25c79) Matthieu Brouillard *2016-06-30 13:51:13*


### GitHub [#9](https://github.com/jgitver/jgitver-maven-plugin/pull/9) add notice/warning for IntelliJ IDEA users

**add notice/warning for IntelliJ IDEA users, enhance README to bypass IDEA-155733 as in #8, fixes #9**


[fadd88e04b25c79](https://github.com/jgitver/jgitver-maven-plugin/commit/fadd88e04b25c79) Matthieu Brouillard *2016-06-30 13:51:13*


### Jira IDEA-155733 

**add notice/warning for IntelliJ IDEA users, enhance README to bypass IDEA-155733 as in #8, fixes #9**


[fadd88e04b25c79](https://github.com/jgitver/jgitver-maven-plugin/commit/fadd88e04b25c79) Matthieu Brouillard *2016-06-30 13:51:13*


## v0.2.0
### GitHub [#6](https://github.com/jgitver/jgitver-maven-plugin/issues/6) prevent IDEA to stop working with jgitver-maven-plugin

**protect against lower maven version & partial initialization in some IDE scenarios, fixes #6**


[821a387a0f45a69](https://github.com/jgitver/jgitver-maven-plugin/commit/821a387a0f45a69) awxgx *2016-05-24 08:19:12*


## 0.1.0
### Jira jgitver-0 

**adapt to jgitver-0.1.0 and the new strategies, enhance documentation**


[d799d7c6d811055](https://github.com/jgitver/jgitver-maven-plugin/commit/d799d7c6d811055) Matthieu Brouillard *2016-05-13 12:25:56*


## 0.0.3
### GitHub [#2](https://github.com/jgitver/jgitver-maven-plugin/issues/2) add IT test with pom project

**make IT tests running, fixes #2**


[eaa64cbea8d78fe](https://github.com/jgitver/jgitver-maven-plugin/commit/eaa64cbea8d78fe) Matthieu Brouillard *2016-04-28 17:20:45*


### GitHub [#3](https://github.com/jgitver/jgitver-maven-plugin/issues/3) add IT test with java project

**add more IT tests, fixes #3**


[64e4f2737e60044](https://github.com/jgitver/jgitver-maven-plugin/commit/64e4f2737e60044) Matthieu Brouillard *2016-04-29 08:27:18*


### GitHub [#4](https://github.com/jgitver/jgitver-maven-plugin/issues/4) use jgitver-maven-plugin inside jgitver-maven-plugin

**version calculation delegated to jgitver-maven-plugin, fixes #4**

 * travis-ci badge added to README

[8667e7af02b47d2](https://github.com/jgitver/jgitver-maven-plugin/commit/8667e7af02b47d2) Matthieu Brouillard *2016-04-28 09:46:41*


## 0.0.2
### GitHub [#1](https://github.com/jgitver/jgitver-maven-plugin/issues/1) version is not correct in deployed pom

**enhance documentation, add CI with travis-ci, fixes #1**


[4703a65c92dc57b](https://github.com/jgitver/jgitver-maven-plugin/commit/4703a65c92dc57b) Matthieu Brouillard *2016-04-28 09:33:14*


