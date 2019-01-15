# jgitver-maven-plugin

[![Build Status](https://travis-ci.org/jgitver/jgitver-maven-plugin.svg?branch=master)](https://travis-ci.org/jgitver/jgitver-maven-plugin)
[![Open Hub project report for jgitver-maven-plugin](https://www.openhub.net/p/jgitver-maven-plugin/widgets/project_thin_badge.gif)](https://www.openhub.net/p/jgitver-maven-plugin?ref=sample) [![Discuss](https://badges.gitter.im/jgitver/jgitver.svg)](https://gitter.im/jgitver/Lobby)

This plugin allows to define the pom version of your project using the information from your git history.
It calculates the version, a little bit like `git describe` would do but in a more efficient way for maven projects:

- new commits have upper version than previous commit (in the way maven/semver interpret versions)
- version calculation is based on git tags & branches
- git lightweight tags allow for intermediate version controlling between releases
    - allow to define what is the _next_ version pattern to use
- minimal setup via maven extension

Here is an illustration of the capabilities of the plugin

![Example](src/doc/images/jgitver-maven-plugin-homepage.png?raw=true "Example")

## Usage

### Activation by maven core extension

Since version `0.3.0` [jgitver-maven-plugin](#jgitver-maven-plugin) needs to be run as a maven core extension.  
The installation scripts below will use the latest version available ; if you are updating find the latest version [here](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22fr.brouillard.oss%22%20AND%20a%3A%22jgitver-maven-plugin%22) or [there](http://search.maven.org/solrsearch/select?q=g:%22fr.brouillard.oss%22+AND+a:%22jgitver-maven-plugin%22&core=gav&rows=1&wt=json).

__via curl__

from the root directory of your project, run:

``` shell
sh -c "$(curl -fsSL https://raw.githubusercontent.com/jgitver/jgitver-maven-plugin/master/src/doc/scripts/install.sh)"
```

__via wget__

from the root directory of your project, run:

``` shell
sh -c "$(wget https://raw.githubusercontent.com/jgitver/jgitver-maven-plugin/master/src/doc/scripts/install.sh -O -)"
```

__manually__

1. Create a directory `.mvn` under the root directory of your project.
1. Create file `.mvn/extensions.xml`
1. Put the following content to `.mvn/extensions.xml` (adapt to [latest version](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22fr.brouillard.oss%22%20a%3A%22jgitver-maven-plugin%22)).

    ```xml
    <extensions xmlns="http://maven.apache.org/EXTENSIONS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/EXTENSIONS/1.0.0 http://maven.apache.org/xsd/core-extensions-1.0.0.xsd">
      <extension>
        <groupId>fr.brouillard.oss</groupId>
        <artifactId>jgitver-maven-plugin</artifactId>
        <version>1.4.4</version>
      </extension>
    </extensions>
    ```

### Configuration

In order to control [jgitver-maven-plugin](#jgitver-maven-plugin) behavior, you can provide a configuration
file under `$rootProjectDir/.mvn/jgitver.config.xml`.
The configuration file must be compliant with the latest [jgitver-configuration-v1_1_0.xsd](https://jgitver.github.io/maven/configuration/jgitver-configuration-v1_1_0.xsd) xml schema.

Here is an example configuration file:

``` xml
<configuration xmlns="http://jgitver.github.io/maven/configuration/1.1.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://jgitver.github.io/maven/configuration/1.1.0 https://jgitver.github.io/maven/configuration/jgitver-configuration-v1_1_0.xsd">
    <mavenLike>true/false</mavenLike>   <!-- deprecated, use 'strategy' instead -->
    <strategy>MAVEN|CONFIGURABLE|PATTERN</strategy>
    <policy>MAX|LATEST|NEAREST</policy>    <!-- LookupPolicy to select the base tag/commit for the version computation -->
    <autoIncrementPatch>true/false</autoIncrementPatch>
    <useCommitDistance>true/false</useCommitDistance>
    <useDirty>true/false</useDirty>
    <useGitCommitId>true/false</useGitCommitId>
    <gitCommitIdLength>integer</gitCommitIdLength>  <!-- between [8,40] -->
    <maxSearchDepth>integer</maxSearchDepth>  <!-- upper or equal to 1, ommited otherwise, default to infinite -->
    <nonQualifierBranches>master</nonQualifierBranches> <!-- comma separated, example "master,integration" -->
    <regexVersionTag>r([0-9]+)</regexVersionTag>  <!-- a java regular expression with a capture group matching only 
                                                       tags of the form r0, r1, ..., r34-->
    <exclusions>    <!-- Optional list of directory path -->
      <exclusion>relative directory path</exclusion>    <!-- relative path from project root directory -->
    </exclusions>
    <useDefaultBranchingPolicy>true/false</useDefaultBranchingPolicy>   <!-- uses jgitver#BranchingPolicy#DEFAULT_FALLBACK as fallback branch policy-->
    <branchPolicies>
        <branchPolicy>
            <pattern>pattern</pattern>                  <!-- regex pattern -->
            <!-- list of transformations to apply, if empty, defaults to REPLACE_UNEXPECTED_CHARS_UNDERSCORE, LOWERCASE_EN -->
            <transformations>                           
                <transformation>NAME</transformation> <!-- transformation name, one of jgitver#fr.brouillard.oss.jgitver.BranchingPolicy#BranchNameTransformations -->
                ...
            </transformations>
        </branchPolicy>
        ...
    </branchPolicies>
</configuration>
```

Please consult [jgitver](https://github.com/jgitver/jgitver#configuration-modes--strategies) documentation to fully understand what the parameters do.

_[Old](https://jgitver.github.io/maven/configuration/) xml schemas are kept for reference._

#### Command line arguments

- `-Djgitver.skip=true` : skips totally jgitver usage
- `-Djgitver.config=FILE` : overrides default config file and uses FILE instead
- `-Djgitver.use-version=VERSION` : execute jgitver but finally uses VERSION as the project version 

#### Working on a detached HEAD

When working on a detached HEAD, no branch information exists anymore from git.  
Since `1.3.0` it now possible to provide externally the _branch_ information via a system property or an envrionement variable.

- `-Djgitver.branch=SOME_BRANCH_NAME`
- `JGITVER_BRANCH=SOME_BRANCH_NAME && mvn validate` for bash like shells
- `SET JGITVER_BRANCH=SOME_BRANCH_NAME`  
    `mvn validate`  
    for windows CMD (I don't know a one iner solution)


### Available properties

Since `0.2.0`, the plugin exposes git calculated properties available during the maven build.
Those are available under the following properties name: "jgitver.meta" where `meta` is one of [Metadatas](https://github.com/jgitver/jgitver/blob/master/src/main/java/fr/brouillard/oss/jgitver/metadata/Metadatas.java#L25) name in lowercase.

You can then use them as standard maven properties in your build:

``` xml
<plugin>
    <artifactId>maven-antrun-plugin</artifactId>
    <executions>
        <execution>
            <phase>validate</phase>
            <goals>
                <goal>run</goal>
            </goals>
            <configuration>
                <tasks>
                    <echo>used version: ${jgitver.used_version}</echo>
                    <echo>version calculated: ${jgitver.calculated_version}</echo>
                    <echo>dirty: ${jgitver.dirty}</echo>
                    <echo>head_committer_name: ${jgitver.head_committer_name}</echo>
                    <echo>head_commiter_email: ${jgitver.head_commiter_email}</echo>
                    <echo>head_commit_datetime: ${jgitver.head_commit_datetime}</echo>
                    <echo>git_sha1_full: ${jgitver.git_sha1_full}</echo>
                    <echo>git_sha1_8: ${jgitver.git_sha1_8}</echo>
                    <echo>branch_name: ${jgitver.branch_name}</echo>
                    <echo>head_tags: ${jgitver.head_tags}</echo>
                    <echo>head_annotated_tags: ${jgitver.head_annotated_tags}</echo>
                    <echo>head_lightweight_tags: ${jgitver.head_lightweight_tags}</echo>
                    <echo>base_tag: ${jgitver.base_tag}</echo>
                    <echo>all_tags: ${jgitver.all_tags}</echo>
                    <echo>all_annotated_tags: ${jgitver.all_annotated_tags}</echo>
                    <echo>all_lightweight_tags: ${jgitver.all_lightweight_tags}</echo>
                    <echo>all_version_tags: ${jgitver.all_version_tags}</echo>
                    <echo>all_version_annotated_tags: ${jgitver.all_version_annotated_tags}</echo>
                    <echo>all_version_lightweight_tags: ${jgitver.all_version_lightweight_tags}</echo>
                </tasks>
            </configuration>
        </execution>
    </executions>
</plugin>
```

resulted in my case

```
[INFO] Executing tasks
     [echo] used version: 0.2.0-SNAPSHOT
     [echo] version calculated: 0.2.0-SNAPSHOT
     [echo] dirty: true
     [echo] head_committer_name: Matthieu Brouillard
     [echo] head_commiter_email: matthieu@brouillard.fr
     [echo] head_commit_datetime: Thu Jun 30 14:06:14 2016 +0200
     [echo] git_sha1_full: fadd88e04b25c794cea876b03d8234df5bf4e37b
     [echo] git_sha1_8: fadd88e0
     [echo] branch_name: master
     [echo] head_tags:
     [echo] head_annotated_tags:
     [echo] head_lightweight_tags:
     [echo] base_tag: v0.2.0
     [echo] all_tags: v0.2.0,0.1.1,0.1.0,0.0.3,0.0.2,0.0.1
     [echo] all_annotated_tags: 0.1.1,0.1.0,0.0.3,0.0.2,0.0.1
     [echo] all_lightweight_tags: v0.2.0
     [echo] all_version_tags: v0.2.0,0.1.1,0.1.0,0.0.3,0.0.2,0.0.1
     [echo] all_version_annotated_tags: 0.1.1,0.1.0,0.0.3,0.0.2,0.0.1
     [echo] all_version_lightweight_tags: v0.2.0
[INFO] Executed tasks
```

## Example

If you want to give it a try you can use the following script that will setup a demo project under `/tmp/jgitver-tester`

```
# let's create a fake maven project under /tmp
cd /tmp
mvn archetype:generate -B -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.1 -DgroupId=com.company -DartifactId=jgitver-tester -Dversion=0 -Dpackage=com.company.project
cd jgitver-tester

# init the created project with jgitver-maven-plugin
sh -c "$(wget https://raw.githubusercontent.com/jgitver/jgitver-maven-plugin/master/src/doc/scripts/install.sh -O -)"

# let's do some modifications/commits/tags
echo A > content
git init
git add .
git commit -m "initial commit"
echo B > content && git add -u && git commit -m "added B data"
git tag 1.0 -m "release 1.0"
echo C > content && git add -u && git commit -m "added C data"
git checkout -b cool-feature
echo D > content && git add -u && git commit -m "added D data"
git checkout master
echo E > content && git add -u && git commit -m "added E data"
mvn validate
```

Then play around with it doing:

- `mvn validate`
- `mvn install`
- `git checkout 1.0`
- `mvn validate`
- `git checkout cool-feature`
- `mvn validate`

## Requirements

### Maven requirements

[jgitver-maven-plugin](#jgitver-maven-plugin) requires at least maven-3.3.2 to work correctly.

Think to modify your IDE settings regarding maven version ; if required do not use the embedded maven version of your IDE but an external one that fulfill the maven minimal requirements.  

### Supported IDEs

- Eclipse: tested with Eclipse Mars.2 Release 4.5.2
- Netbeans: tested with NetBeans IDE 8.1 Build 201510222201
- Intellij IDEA: tested with 2016.1.3

## Build & release

### Github Markdown rendering

Before pushing try to always verify that the modifications pushed in MD files will be correctly rendered by Github.  
For that purpose you can use [grip](https://github.com/joeyespo/grip).

### Normal build

- `mvn -Prun-its clean install`

or using docker

- _Linux_: `docker run --rm -v $(pwd):/root/sources -w /root/sources maven:3.5.4-jdk-8 mvn -Prun-its clean install`
- _Windows_: `docker run --rm -v %CD%:/root/sources -w /root/sources maven:3.5.4-jdk-8 mvn -Prun-its clean install`
- _Old linux command_: `docker run --rm -v $(pwd):/root/sources -w /root/sources maven:3.5.4-jdk-8 ./src/ci/build-with-external-it-fallback.sh`

build and filter some IT tests

- `mvn -Prun-its clean install "-Dinvoker.test=issues/issue-36*"`

If needed, one can also add in above docker command a volume sharing 
with the maven local repository by adding something like `-v MLR_LOCATION:/root/.m2/repository` for example `-v D:\dev\mlr:/root/.m2/repository`.

### Release

- `mvn -Poss clean install`: this will simulate a full build for oss delivery (javadoc, source attachement, GPG signature, ...)
- `git tag -a -s -m "release X.Y.Z, additionnal reason" X.Y.Z`: tag the current HEAD with the given tag name. The tag is signed by the author of the release. Adapt with gpg key of maintainer.
    - Matthieu Brouillard command:  `git tag -a -s -u 2AB5F258 -m "release X.Y.Z, additionnal reason" X.Y.Z`
    - Matthieu Brouillard [public key](https://sks-keyservers.net/pks/lookup?op=get&search=0x8139E8632AB5F258)
- `mvn -Poss,release -DskipTests clean deploy`
- `git push --follow-tags origin master`

## Issues

### I want to temporary disable the plugin execution

Since `1.0.0`, it is possible to totally skip the plugin execution by launching maven with the user property `jgtiver.skip` set to `true`, example:

- `mvn clean install -Djgitver.skip=true`

### maven reports my project version to be 0 (or the one set in the pom.xml)

If your version is not calculated correctly by maven/jgitver, there are good chances that the plugin is not active.  
Please verify that you are using maven >= 3.3.2.

### build fail because all project plugins & dependencies resolve to the same version

if during a build all the plugins & dependencies are resolved to the exacts same version then chances are high that you have the _local maven repository_ as a subdirectory of your jgitver handled project.

To overcome this problem you have 2 possibilities:

- separate correctly your project from the MLR and make sure the MLR is not a subdirectory of your jgitver managed project
- configure jgitver (using `.mvn/jgitver.config.xml`) to ignore your subdirectory MLR, see the [configuration](#Configuration) paragraph
    ``` xml
    <configuration>
        <exclusions>
            <exclusion>path_to_your_mlr</exclusion>    <!-- can be .m2, .repository or something else -->
        </exclusions>
    </configuration>
    ```

see also [#90](https://github.com/jgitver/jgitver-maven-plugin/issues/90) && [#91](https://github.com/jgitver/jgitver-maven-plugin/issues/91) for discussions on this topic.
  
### the invoker tests of my maven plugin project do not work anymore

If you develop a maven plugin project, you normally run maven-invoker-plugin to test your plugin.  
Using default configuration, maven-invoker-plugin will use a temporary local repository under `target/local-repo` and the IT tests will be executed from `target/it/XYZ`.
In this context, when executing tests, maven will try to activate extensions starting from the `target/it/XYZ` directory ; and it will find your extensions definition in the root directory of the project. This will lead in the activation of `jgitver-maven-plugin` for all your IT projects AND for the poms inside the temporary local repository under `target/local-repository`.

To avoid such behavior, you need to tell `jgitver-maven-plugin` to ignore some directories. If you do not have already a jgitver configuration file, create one under `.mvn/jgitver.config.xml` and declare some exclusions (see [configuration](#configuration)):

``` xml
<configuration>
    <exclusions>
        <exclusion>target/local-repo</exclusion>
        <exclusion>target/it/**</exclusion>
    </exclusions>
</configuration>
```

You can have a look at the configuration of [jgitver-maven-plugin](.mvn/jgitver.config.xml) itself.

# License

jgitver-maven-plugin is delivered under the [Apache Licence, Version 2](https://opensource.org/licenses/Apache-2.0)
