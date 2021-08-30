package fr.brouillard.oss.jgitver;

public interface JGitverCommons {
  interface Extension {
    String GROUP_ID = "fr.brouillard.oss";
    String ARTIFACT_ID = "jgitver-maven-plugin";
  }
  interface Common {
    String GROUP_ID = "fr.brouillard.oss";
    String ARTIFACT_ID = "jgitver-maven-plugin-common";
  }
  interface Plugins {
    interface Flatten {
      String GROUP_ID = "org.codehaus.mojo";
      String ARTIFACT_ID = "flatten-maven-plugin";
      String GOAL = "flatten";
    }
    interface AttachModifiedPoms {
      String GROUP_ID = "fr.brouillard.oss";
      String ARTIFACT_ID = "jgitver-maven-plugin-mojo";
      String GOAL = "attach-modified-poms";
    }
  }
}
