Hi reader!

If you ended here then you probably want to contribute to this project: thanks for that!

In order to have your contribution more easily integrated please respect the following rules:

## Reporting an issue

This project uses GitHub issues to manage the issues. Open an issue directly in GitHub.
Use the provided issue template when submitting a new issue and as much as possible provide a reproducible example.

## Building master

Building the project is as simple as:

```
git clone git@github.com:jgitver/jgitver-maven-plugin.git
cd jgitver-maven-plugin
./mvnw package
```

## Contribution rules

1. contributions are provided by pull-request only (no email, or anything else)
1. before submitting your pull-request you have successfully run a local build using the following command
    - `mvn -Prun-its clean install`
1. if your contribution consist in some code modification then your change is covered by a unit or integration test
    - _for documentation only contribution, this requirement can be omitted)_
1. you have kept several commits in your PR only if those commits are relevant ; for typos commits or like please squash them.


### IDE Config and Code Style

jgitver has a strictly enforced code style using [google-java-format](https://github.com/google/google-java-format). Code formatting is checked during the build phase by the [spotless-maven-plugin](https://github.com/diffplug/spotless/tree/master/plugin-maven#google-java-format).

If you want to run the formatting without doing a full build, you can run `mvn spotless:apply`.

#### Eclipse Setup

Follow instructions from [google-java-format](https://github.com/google/google-java-format#eclipse) site.

> The eclipse plugin is outdated and uses the old `1.6` version of google-java-format _(see also [google-java-format#331](https://github.com/google/google-java-format/issues/331))_.  
> You can manually apply the styling by running: `mvn spotless:apply`

#### Intellij Setup

Follow instructions from [google-java-format](https://github.com/google/google-java-format#intellij-android-studio-and-other-jetbrains-ides) site.

### Integration warning

As we want to keep the project clean, __every commit__ in any branch (expect master & maintenance branches) can be __rewritten__.

From the last sentence we give 2 advices:

- initiate your PR from a branch in your fork and not from the master. When your PR will be integrated, it can be modified & adapted leading your fork with a master branch that as diverged from upstream
- do not initiate work in your fork from a PR that could be rewritten
