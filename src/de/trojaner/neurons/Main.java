package de.trojaner.neurons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The main method, starting point for the program
 */
public class Main {
  /**
   * Entry point
   * 
   * @param args
   */
  public static void main(String[] args) {
    try {
      // Getting the path of the input preset
      Path inputPath = Paths.get("input-presets", STATIC.INPUT_PRESET);
      // Getting the folder where all "A" samples are stored
      Path aPath = inputPath.resolve("a");
      // Getting the folder where all "B" samples are stored
      Path bPath = inputPath.resolve("b");
      // Getting the folder where all samples are stored that are neither "A" nor "B"
      Path incorrectPath = inputPath.resolve("nothing");
      // Getting all "A" sample files
      File[] aFiles = aPath.toFile().listFiles();
      // Getting all "B" sample files
      File[] bFiles = bPath.toFile().listFiles();
      // Getting all sample files that are neither "A" nor "B"
      File[] incorrectFiles = incorrectPath.toFile().listFiles();

      // Creating an array with the length of all collected files
      File[] fileInputs = new File[aFiles.length + bFiles.length + incorrectFiles.length];
      // Filling the first part of the array with all "A" samples
      System.arraycopy(aFiles, 0, fileInputs, 0, aFiles.length);
      // Filling the second part of the array with all "B" samples
      System.arraycopy(bFiles, 0, fileInputs, aFiles.length, bFiles.length);
      // Filling the third part of the array with all samples that are neither "A" nor
      // "B"
      System.arraycopy(incorrectFiles, 0, fileInputs, aFiles.length + bFiles.length, incorrectFiles.length);

      // Initialize a picture analyzer with all pictures
      PictureAnalyzer pictureAnalyzer = new PictureAnalyzer(fileInputs);
      // Store the analyzed pictures as inputs values
      float[][] inputs = pictureAnalyzer.getInput();

      // Initialize a network with all inputs
      NeuralNetwork network = new NeuralNetwork(inputs);
      // Create a list of all expected outputs
      float[][] expectedOutputs = new float[inputs.length][network.getNumberOfOutputNeurons()];
      // Initialize counter
      int i = 0;
      // Add an expected output that the probability of the first neuron is 1 while
      // the others are 0 (thus leading to an "A") for each "A" sample
      for (File aFile : aFiles)
        expectedOutputs[i++] = new float[] { 1, 0, 0 };

      // Do the same for "B" samples
      for (File bFile : bFiles)
        expectedOutputs[i++] = new float[] { 0, 1, 0 };

      // Do the same for the other files
      for (File incorrectFile : incorrectFiles)
        expectedOutputs[i++] = new float[] { 0, 0, 1 };

      System.out.println("Training…");
      // Let the network learn with the expected outputs with a set amount of
      // iterations
      network.learn(expectedOutputs, 10000);

      // Get the path of the analyze folder
      Path testPath = inputPath.resolve("analyze");
      // Get all files to analyze
      File[] testFiles = testPath.toFile().listFiles();

      // Set the pictures to analyze to the new test files
      pictureAnalyzer.setPictures(testFiles);
      // Reset counter
      i = 0;
      // Test for each picture to how likely it is that the picture matches "A", "B",
      // or none of them according to the trained network
      for (float[] input : pictureAnalyzer.getInput())
        System.out.println(String.format("%s: %s", testFiles[i++].getName(), makeOutputReadable(network.solve(input))));
    } catch (IOException e) {
      // If the pictures could not be read print this error message
      System.err.println("Could not read input/output files");
    }
  }

  /**
   * Makes the output of the neural network readable for a human
   * 
   * @param output The output of the network
   * @return A string containing percentages to how likely it is that the picture
   *         matches something learned
   */
  private static String makeOutputReadable(float[] output) {
    // Create a new StringBuilder
    StringBuilder readableOutput = new StringBuilder();
    // Create a counter
    int i = 0;
    // Check for each value of the output neurons
    for (float value : output) {
      readableOutput.append("\n  ");
      switch (i++) {
        case 0:
          // Append "A" if it is the first neuron
          readableOutput.append("A");
          break;
        case 1:
          // Append "B" if it is the second neuron
          readableOutput.append("B");
          break;
        case 2:
          // Append "Nothing" if it is the third neuron
          readableOutput.append("Nothing");
      }
      // Append the percentage by multiplying the value with 100 and rounding that to
      // an integer
      readableOutput.append(": ").append(Math.round(value * 100)).append("% ");
    }
    // Return the created output
    return readableOutput.toString();
  }
}
