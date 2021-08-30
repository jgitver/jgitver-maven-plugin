package fr.brouillard.oss.jgitver;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class JGitverInformationStore {
  private File rootDirectory;

  public File getRootDirectory() {
    return rootDirectory;
  }

  public void setRootDirectory(File rootDirectory) {
    this.rootDirectory = rootDirectory;
  }

}
