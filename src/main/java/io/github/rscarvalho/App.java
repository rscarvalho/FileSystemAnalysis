package io.github.rscarvalho;

import java.io.File;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;
import com.google.common.io.Files;
import com.google.common.primitives.Longs;

public class App {

  public static final Ordering<File> FILE_ORDERING = new Ordering<File>() {
    @Override
    public int compare(File fileA, File fileB) {
      return Longs.compare(fileA.length(), fileB.length());
    }
  };

  public static void main(String[] args) {
      if (args.length < 1) {
        throw new IllegalArgumentException("Please pass the path to be scanned");
      }

      File rootDir = new File(args[0]);

      if (!rootDir.exists() || !rootDir.isDirectory()) {
        throw new IllegalArgumentException("Please pass an existing directory");
      }

      FluentIterable<File> files = Files.fileTreeTraverser()
          .breadthFirstTraversal(rootDir)
          .filter(new Predicate<File>() {
            @Override
            public boolean apply(File file) {
              return file.isFile();
            }
          });

    if (files.isEmpty()) {
      System.out.println("No files in the folder");
      return;
    }

    File largestFile = FILE_ORDERING.max(files);

    System.out.println(String.format("Largest file: %s (%d bytes)", largestFile.getAbsolutePath(), largestFile.length()));
  }
}
