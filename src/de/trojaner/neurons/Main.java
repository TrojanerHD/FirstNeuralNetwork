package de.trojaner.neurons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {

    try {
      Path inputPath = Paths.get("inputs");
      Path correct = inputPath.resolve("true");
      Path incorrect = inputPath.resolve("false");
      File[] correctFiles = correct.toFile().listFiles();
      File[] incorrectFiles = incorrect.toFile().listFiles();

      File[] fileInputs = new File[correctFiles.length + incorrectFiles.length];
      System.arraycopy(correctFiles, 0, fileInputs, 0, correctFiles.length);
      System.arraycopy(incorrectFiles, 0, fileInputs, correctFiles.length, incorrectFiles.length);
      PictureAnalyzer picture = new PictureAnalyzer(fileInputs);
      float[][] inputs = picture.getInput();

      NeuralNetwork network = new NeuralNetwork(inputs);
      List<Boolean> expectedOutputs = new ArrayList<>();
      for (File correctFile : correctFiles)
        expectedOutputs.add(true);
      for (File incorrectFile : incorrectFiles)
        expectedOutputs.add(false);
      network.learn(expectedOutputs, 5000000);

      Path testPath = inputPath.resolve("analyze");
      File[] testFiles = testPath.toFile().listFiles();

      picture.setPictures(testFiles);
      int i = 0;
      for (float[] input : picture.getInput())
        System.out.println(String.format("%s: %s", testFiles[i++].getName(), network.solve(input)));
    } catch (IOException e) {
    }
  }
}
