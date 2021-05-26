package de.trojaner.neurons;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    /*
     * Path inputPath = Paths.get("inputs"); Path correct =
     * inputPath.resolve("true"); Path incorrect = inputPath.resolve("false");
     * File[] correctFiles = correct.toFile().listFiles(); File[] incorrectFiles =
     * incorrect.toFile().listFiles();
     * 
     * File[] fileInputs = new File[correctFiles.length + incorrectFiles.length];
     * System.arraycopy(correctFiles, 0, fileInputs, 0, correctFiles.length);
     * System.arraycopy(incorrectFiles, 0, fileInputs, correctFiles.length,
     * incorrectFiles.length);
     */

    // int[][] inputs = PictureAnalyzer.analyzeFiles(fileInputs);
    /*
     * float[][] inputs = { { 0, 0, 1, 0, 1, 0, 1, 0, 0 }, { 0, 1, 1, 1, 1, 1, 1, 1,
     * 0 }, { 1, 0, 0, 0, 1, 0, 0, 0, 1, }, { 0, 1, 1, 0, 1, 0, 1, 1, 0, } };
     * NeuralNetwork network = new NeuralNetwork(inputs);
     * network.learn(List.of(true, true, false, false));
     */
    float[][] inputs = { { 1, 0, 0 }, { 1, 1, 0 }, { 1, 1, 1 }, { 0, 1, 1 }, { 0, 0, 0 } };

    NeuralNetwork network = new NeuralNetwork(inputs);
    network.learn(List.of(true, true, true, false, false), 100);

    System.out.println(network.solve(new float[] { 1, 0, 1 }));
    System.out.println(network.solve(new float[] { 0, 0, 1 }));

    /*
     * List<Neuron> subneurons = new ArrayList(); for (int i = 0; i < 8; i++)
     * subneurons.add(new Neuron()); Neuron[] subneuronArray = new
     * Neuron[subneurons.size()]; subneuronArray =
     * subneurons.toArray(subneuronArray); Neuron mainNeuron = new
     * Neuron(subneuronArray); File inputPath = new File("input"); File[] inputs =
     * inputPath.listFiles(); PictureInput inputPicture = new PictureInput(inputs,
     * mainNeuron); inputPicture.input(); for (Neuron subneuron:
     * mainNeuron.getSubneurons()) System.out.println(subneuron.getWeight()); File
     * analyzePath = new File("analyze"); File[] analyzeFiles =
     * analyzePath.listFiles(); PictureAnalyzer analyzer = new
     * PictureAnalyzer(analyzeFiles, mainNeuron); List<Boolean> results =
     * analyzer.analyze(); for (int i = 0; i < analyzeFiles.length; i++)
     * System.out.println(String.format("%s: %s", analyzeFiles[i].getName(),
     * results.get(i)));
     */
  }
}
