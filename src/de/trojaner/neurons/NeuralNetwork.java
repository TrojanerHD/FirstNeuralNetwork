package de.trojaner.neurons;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class NeuralNetwork {
  private float[][] hiddenLayer1Weights;
  private float[][] outputLayerWeights;
  private int numberHiddenLayer1Neurons = 9;
  private int numberOutputNeurons = 1;
  private boolean output = false;
  private float[][] inputSet;
  private List<float[]> hiddenLayer1ValuesPerRun = new ArrayList<>();

  public NeuralNetwork(float numberOfHiddenLayers, float[][] inputSet) {
    this.inputSet = inputSet;
    this.initWeights();
  }

  public NeuralNetwork(float[][] inputSet) {
    this.inputSet = inputSet;
    this.initWeights();
  }

  public boolean solve(float[] inputs) {
    float[] hiddenLayer1Values = new float[numberHiddenLayer1Neurons];
    for (int i = 0; i < inputs.length; i++)
      for (int j = 0; j < numberHiddenLayer1Neurons; j++)
        hiddenLayer1Values[j] += inputs[i] * hiddenLayer1Weights[j][i];

    hiddenLayer1ValuesPerRun.add(hiddenLayer1Values);

    float[] outputs = new float[numberOutputNeurons];
    for (int i = 0; i < hiddenLayer1Values.length; i++)
      for (int j = 0; j < outputs.length; j++)
        outputs[j] += hiddenLayer1Values[i] * outputLayerWeights[j][i];

    return outputs[0] >= STATIC.THRESHOLD;

  }

  public void learn(List<Boolean> expectedOutputs, int iterations) {
    for (int iteration = 0; iteration < iterations; iteration++) {
      System.out.println("Iteration: " + iteration);
      List<Boolean> results = new ArrayList<>();
      for (float[] inputs : this.inputSet)
        results.add(this.solve(inputs));
      for (int i = 0; i < results.size(); i++) {
        System.out.println(String.format("Index: %d", i));
        boolean result = results.get(i);
        boolean expectedOutput = expectedOutputs.get(i);
        if (result == expectedOutput)
          continue; // outputDelta == 0

        float[] hiddenLayer1Values = this.hiddenLayer1ValuesPerRun.get(i);

        float[] input = this.inputSet[i];

        float outputDelta = result ? 1 : -1;
        float[] hiddenLayerDeltas = new float[this.numberHiddenLayer1Neurons]; // new int[9]
        for (int j = 0; j < this.outputLayerWeights[0].length; j++)
          hiddenLayerDeltas[j] = outputDelta * this.outputLayerWeights[0][j];

        this.changeWeights(this.hiddenLayer1Weights, hiddenLayerDeltas, input); // hiddenLayer1Weights = input
        this.changeWeights(this.outputLayerWeights, new float[] { outputDelta }, hiddenLayer1Values); //
        System.out.println(String.format("Output Error: %s%nHidden layer weights:", outputDelta));
        this.printWeights(hiddenLayer1Weights);
        System.out.println("Output layer weights:");
        this.printWeights(outputLayerWeights);
      }
    }
  }

  private void changeWeights(float[][] weights, float[] deltas, float[] input) {
    for (int i = 0; i < weights.length; i++) {
      float delta = deltas[i];
      for (int j = 0; j < weights[i].length; j++)
        weights[i][j] -= STATIC.SCALE * delta * input[j];
    }
  }

  private void printWeights(float[][] weights) {
    for (int i = 0; i < weights.length; i++) {
      System.out.println(String.format("%nNeuron: %d", i));
      for (int j = 0; j < weights[i].length; j++)
        System.out.println(weights[i][j]);
    }
  }

  private void initWeights() {
    this.hiddenLayer1Weights = new float[this.numberHiddenLayer1Neurons][this.inputSet[0].length];
    this.outputLayerWeights = new float[this.numberOutputNeurons][this.numberHiddenLayer1Neurons];
    Random r = new Random();
    for (int i = 0; i < hiddenLayer1Weights.length; i++)
      for (int j = 0; j < hiddenLayer1Weights[i].length; j++)
        hiddenLayer1Weights[i][j] = r.nextFloat() * 2 - 1;

    for (int i = 0; i < outputLayerWeights.length; i++)
      for (int j = 0; j < outputLayerWeights[i].length; j++)
        outputLayerWeights[i][j] = r.nextFloat() * 2 - 1;
  }

  public boolean getOutput() {
    return this.output;
  }
}
