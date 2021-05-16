package de.trojaner.neurons;

public class Neuron {
  private int weight = 4;
  private Neuron[] subneurons;

  Neuron(Neuron[] subneurons) {
    this.subneurons = subneurons;
  }

  Neuron() {
    this.subneurons = new Neuron[0];
  }

  public int getWeight() {
    return this.weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public void changeWeight(int change) {
    this.weight += change;
  }

  public Neuron[] getSubneurons() {
    return this.subneurons;
  }
}
