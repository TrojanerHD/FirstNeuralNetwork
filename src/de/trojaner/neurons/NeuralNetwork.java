package de.trojaner.neurons;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetwork {
  private float[][] hiddenLayer1Weights;
  private float[][] outputLayerWeights;
  private int numberHiddenLayer1Neurons = 15;
  private int numberOutputNeurons = 3;
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

  public float[] solve(float[] input) {
    float[] hiddenLayer1Values = new float[numberHiddenLayer1Neurons];
    for (int i = 0; i < numberHiddenLayer1Neurons; i++) {
      float tempValue = 0;
      for (int j = 0; j < input.length; j++)
        tempValue += input[j] * hiddenLayer1Weights[i][j];

      hiddenLayer1Values[i] = sigmoid(tempValue);
    }

    hiddenLayer1ValuesPerRun.add(hiddenLayer1Values);

    float[] outputs = new float[numberOutputNeurons];
    for (int i = 0; i < outputs.length; i++) {
      float tempValue = 0;
      for (int j = 0; j < hiddenLayer1Values.length; j++)
        tempValue += hiddenLayer1Values[j] * outputLayerWeights[i][j];

      outputs[i] = sigmoid(tempValue);

    }

    return outputs;
  }

  private float sigmoid(float x) {
    return (float) (1d / (1 + Math.exp(-x)));
  }

  private float deriv(float x) {
    return x * (1 - x);
  }

  public void learn(List<float[]> expectedOutputs, int iterations) {
    for (int iteration = 0; iteration < iterations; iteration++) {
      StringBuilder debug = new StringBuilder("Iteration: ").append(iteration).append(" Wrong: ");
      List<Integer> wrong = new ArrayList<>();
      // System.out.println("Iteration: " + iteration);
      List<float[]> results = new ArrayList<>();
      for (float[] inputs : this.inputSet)
        results.add(this.solve(inputs));

      if (expectedOutputs.equals(results))
        return;
      for (int i = 0; i < results.size(); i++) {
        // System.out.println(String.format("Index: %d", i));
        float[] result = results.get(i);
        float[] expectedOutput = expectedOutputs.get(i);
        if (Arrays.equals(result, expectedOutput))
          continue; // outputDelta == 0

        wrong.add(i);

        float[] hiddenLayer1Values = this.hiddenLayer1ValuesPerRun.get(i);

        float[] input = this.inputSet[i];

        float[] outputDeltas = new float[expectedOutput.length];
        int count = 0;
        for (float output : expectedOutput)
          outputDeltas[count] = output - result[count++];

        float[] hiddenLayer1Deltas = new float[this.numberHiddenLayer1Neurons]; // new int[9]
        for (int j = 0; j < this.outputLayerWeights.length; j++)
          for (int k = 0; k < this.outputLayerWeights[j].length; k++)
            hiddenLayer1Deltas[k] = outputDeltas[j] * this.outputLayerWeights[j][k] * deriv(hiddenLayer1Values[k]);

        this.changeWeights(this.hiddenLayer1Weights, hiddenLayer1Deltas, input); // hiddenLayer1Weights = input
        this.changeWeights(this.outputLayerWeights, outputDeltas, hiddenLayer1Values); //
        // System.out.println(String.format("Output Error: %s%nHidden layer weights:",
        // outputDelta));
        // this.printWeights(hiddenLayer1Weights);
        // System.out.println("Output layer weights:");
        // this.printWeights(outputLayerWeights);
      }
      debug.append(wrong);
      //System.out.println(debug.toString());
    }
  }

  private void changeWeights(float[][] weights, float[] deltas, float[] input) {
    for (int i = 0; i < weights.length; i++) {
      float delta = deltas[i];
      for (int j = 0; j < weights[i].length; j++)
        weights[i][j] += STATIC.SCALE * delta * input[j];
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
}
