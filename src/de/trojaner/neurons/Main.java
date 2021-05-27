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
      var test = "ab";
      Path inputPath = Paths.get("input-presets", test);
      Path aPath = inputPath.resolve("a");
      Path bPath = inputPath.resolve("b");
      Path incorrectPath = inputPath.resolve("nothing");
      File[] aFiles = aPath.toFile().listFiles();
      File[] bFiles = bPath.toFile().listFiles();
      File[] incorrectFiles = incorrectPath.toFile().listFiles();

      File[] fileInputs = new File[aFiles.length + bFiles.length + incorrectFiles.length];
      System.arraycopy(aFiles, 0, fileInputs, 0, aFiles.length);
      System.arraycopy(bFiles, 0, fileInputs, aFiles.length, bFiles.length);
      System.arraycopy(incorrectFiles, 0, fileInputs, aFiles.length + bFiles.length, incorrectFiles.length);
      PictureAnalyzer picture = new PictureAnalyzer(fileInputs);
      float[][] inputs = picture.getInput();

      NeuralNetwork network = new NeuralNetwork(inputs);
      List<float[]> expectedOutputs = new ArrayList<>();
      for (File aFile : aFiles)
        expectedOutputs.add(new float[]{1, 0, 0});

      for (File bFile : bFiles)
        expectedOutputs.add(new float[]{0, 1, 0});

      for (File incorrectFile : incorrectFiles)
        expectedOutputs.add(new float[]{0, 0, 1});

      network.learn(expectedOutputs, 10000);

      Path testPath = inputPath.resolve("analyze");
      File[] testFiles = testPath.toFile().listFiles();

      picture.setPictures(testFiles);
      int i = 0;
      for (float[] input : picture.getInput())
        System.out.println(String.format("%s: %s", testFiles[i++].getName(), makeOutputReadable(network.solve(input))));
    } catch (IOException e) {
    }
  }

  private static String makeOutputReadable(float[] output) {
    StringBuilder readableOutput = new StringBuilder();
    int i = 0;
    for (float value : output) {
      switch (i++) {
        case 0: readableOutput.append("A");
        break;
        case 1: readableOutput.append("B");
        break;
        case 2: readableOutput.append("Nothing");
      }
      readableOutput.append(": ").append(Math.round(value * 100)).append("% ");
    }
    return readableOutput.toString();
  }
}
