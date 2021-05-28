package de.trojaner.neurons;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The class for the neural network
 */
public class NeuralNetwork {
  private float[][] hiddenLayer1Weights;
  private float[][] outputLayerWeights;
  private int numberHiddenLayer1Neurons = 150;
  private int numberOutputNeurons = 3;
  private float[][] inputSet;
  private List<float[]> hiddenLayer1ValuesPerRun = new ArrayList<>();

  /**
   * Intializes a neural network and weights for the neurons
   * 
   * @param inputSet The set of inputs to analyze
   */
  public NeuralNetwork(float[][] inputSet) {
    this.inputSet = inputSet;
    this.initWeights();
  }

  /**
   * Uses the neural network to get an output to a provided input
   * 
   * @param input The input to use
   * @return The output to the input
   */
  public float[] solve(float[] input) {
    float[] hiddenLayer1Values = new float[numberHiddenLayer1Neurons];
    for (int i = 0; i < numberHiddenLayer1Neurons; i++) {
      float tempValue = 0;
      for (int j = 0; j < input.length; j++)
        tempValue += input[j] * hiddenLayer1Weights[i][j];

      hiddenLayer1Values[i] = sigmoid(tempValue);
    }

    hiddenLayer1ValuesPerRun.add(hiddenLayer1Values);

    float[] outputs = new float[this.numberOutputNeurons];
    for (int i = 0; i < outputs.length; i++) {
      float tempValue = 0;
      for (int j = 0; j < hiddenLayer1Values.length; j++)
        tempValue += hiddenLayer1Values[j] * outputLayerWeights[i][j];

      outputs[i] = sigmoid(tempValue);
    }
    return outputs;
  }

  /**
   * The sigmoid function to normalize neuron values between 0 and 1
   * 
   * @param x The neuron value
   * @return The normalized value using the sigmoid function
   */
  private float sigmoid(float x) {
    return (float) (1d / (1 + Math.exp(-x)));
  }

  /**
   * The derivative of the sigmoid function used for backward propagation
   * 
   * @param x The neuron value
   * @return The modified value using the derivative
   */
  private float deriv(float x) {
    return x * (1 - x);
  }

  /**
   * Lets the network learn and changes weights to work for new inputs of the same
   * problem
   * 
   * @param expectedOutputs The expected outputs arranged in a matrix
   * @param iterations      The number of iterations the network should learn
   */
  public void learn(float[][] expectedOutputs, int iterations) {
    for (int iteration = 0; iteration < iterations; iteration++) {
      StringBuilder debug = new StringBuilder("Iteration: ").append(iteration).append(" Wrong: ");
      List<Integer> wrong = new ArrayList<>();
      float[][] results = new float[this.inputSet.length][this.numberOutputNeurons];
      int count = 0;
      for (float[] inputs : this.inputSet)
        results[count++] = this.solve(inputs);

      if (Arrays.equals(expectedOutputs, results))
        return;
      for (int i = 0; i < results.length; i++) {
        float[] result = results[i];
        float[] expectedOutput = expectedOutputs[i];
        if (Arrays.equals(result, expectedOutput))
          continue; // outputDelta == 0

        wrong.add(i);

        float[] hiddenLayer1Values = this.hiddenLayer1ValuesPerRun.get(i);

        float[] input = this.inputSet[i];

        float[] outputDeltas = new float[expectedOutput.length];
        count = 0;
        for (float output : expectedOutput)
          outputDeltas[count] = output - result[count++];

        float[] hiddenLayer1Deltas = new float[this.numberHiddenLayer1Neurons];
        for (int j = 0; j < this.outputLayerWeights.length; j++)
          for (int k = 0; k < this.outputLayerWeights[j].length; k++)
            hiddenLayer1Deltas[k] = outputDeltas[j] * this.outputLayerWeights[j][k] * deriv(hiddenLayer1Values[k]);

        this.changeWeights(this.hiddenLayer1Weights, hiddenLayer1Deltas, input); // hiddenLayer1Weights = input
        this.changeWeights(this.outputLayerWeights, outputDeltas, hiddenLayer1Values); //
      }
      debug.append(wrong);
      // System.out.println(debug.toString());
    }
  }

  /**
   * Change weights to delta * input
   * 
   * @param weights The weights to change
   * @param deltas  The deltas for each weight
   * @param input   The input of each neuron in the previous layer
   */
  private void changeWeights(float[][] weights, float[] deltas, float[] input) {
    for (int i = 0; i < weights.length; i++) {
      float delta = deltas[i];
      for (int j = 0; j < weights[i].length; j++)
        weights[i][j] += STATIC.SCALE * delta * input[j];
    }
  }

  /**
   * Initializes weights by giving them random float values between -1 and 1
   */
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

  /**
   * Get the number of neurons in the output layer
   * 
   * @return The number of neurons in the output layer
   */
  public int getNumberOfOutputNeurons() {
    return this.numberOutputNeurons;
  }
}
