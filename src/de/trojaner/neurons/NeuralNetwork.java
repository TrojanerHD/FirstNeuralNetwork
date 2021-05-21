package de.trojaner.neurons;

import java.util.List;
import java.util.ArrayList;

public class NeuralNetwork {
  private float[][] hiddenLayer1Weights;
  private float[][] outputLayerWeights;
  private int numberInputNeurons = 9;
  private int numberHiddenLayer1Neurons = 9;
  private int numberOutputNeurons = 1;
  private boolean output = false;
  private float[][] inputSet;
  private List<float[]> hiddenLayer1ValuesPerRun = new ArrayList<>();

  public NeuralNetwork(float numberOfHiddenLayers, float[][] inputSet) {
    this.initWeights();
    this.inputSet = inputSet;
  }

  public NeuralNetwork(float[][] inputSet) {
    this.initWeights();
    this.inputSet = inputSet;
  }

  private List<Boolean> predict() {
    List<Boolean> results = new ArrayList<>();
    for (float[] inputs : this.inputSet) {
      float[] hiddenLayer1Values = new float[numberHiddenLayer1Neurons];
      for (int i = 0; i < inputs.length; i++)
        for (int j = 0; j < numberHiddenLayer1Neurons; j++)
          hiddenLayer1Values[j] += inputs[i] * hiddenLayer1Weights[j][i];

      hiddenLayer1ValuesPerRun.add(hiddenLayer1Values);

      float[] outputs = new float[numberOutputNeurons];
      for (int i = 0; i < hiddenLayer1Values.length; i++)
        for (int j = 0; j < outputs.length; j++)
          outputs[j] += hiddenLayer1Values[i] * outputLayerWeights[j][i];

      results.add(outputs[0] >= STATIC.THRESHOLD);

    }
    return results;
  }

  public void learn(List<Boolean> expectedOutputs) {
    for (int temp = 0; temp < 10; temp++) {
      System.out.println("Temp: " + temp);
      List<Boolean> results = this.predict();
      for (int i = 0; i < results.size(); i++) {
        System.out.println(String.format("Index: %d", i));
        boolean result = results.get(i); // false
        boolean expectedOutput = expectedOutputs.get(i); // true
        if (result == expectedOutput)
          continue; // 0

        float[] hiddenLayer1Values = this.hiddenLayer1ValuesPerRun.get(i); // 0, 0, 0, 0, 0, 0, 0

        float[] input = this.inputSet[i]; // 0, 0, 1, 0, 1, 0, 1, 0, 0

        /*
         * if (result && !expected) outputDelta = 1; else if (!result && expected)
         * outputDelta = -1;
         */
        float outputDelta = result ? 1 : -1;
        float[] hiddenLayerDeltas = new float[this.numberHiddenLayer1Neurons]; // new int[9]
        for (int j = 0; j < this.outputLayerWeights[0].length; j++) {
          hiddenLayerDeltas[j] = outputDelta * this.outputLayerWeights[0][j];
        }
        // hiddenLayerDelta = [0, 0, 0, 0, 0, 0, 0, 0, 0]
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
      for (int j = 0; j < weights[i].length; j++) {
        weights[i][j] -= STATIC.SCALE * delta * input[j] * 0.5;
      }
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
    this.hiddenLayer1Weights = new float[this.numberHiddenLayer1Neurons][this.numberInputNeurons];
    this.outputLayerWeights = new float[this.numberOutputNeurons][this.numberHiddenLayer1Neurons];
    for (int i = 0; i < hiddenLayer1Weights.length; i++) {
      for (int j = 0; j < hiddenLayer1Weights[i].length; j++) {
        hiddenLayer1Weights[i][j] = 1;
      }
    }
    for (int i = 0; i < outputLayerWeights.length; i++) {
      for (int j = 0; j < outputLayerWeights[i].length; j++) {
        outputLayerWeights[i][j] = 1;
      }
    }
  }

  public boolean getOutput() {
    return this.output;
  }
}
