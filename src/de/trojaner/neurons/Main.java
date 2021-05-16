package de.trojaner.neurons;

import de.trojaner.neurons.Neuron;
import de.trojaner.neurons.picture.PictureInput;
import de.trojaner.neurons.picture.PictureAnalyzer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<Neuron> subneurons = new ArrayList();
    for (int i = 0; i < 8; i++) subneurons.add(new Neuron());
    Neuron[] subneuronArray = new Neuron[subneurons.size()];
    subneuronArray = subneurons.toArray(subneuronArray);
    Neuron mainNeuron = new Neuron(subneuronArray);
    File inputPath = new File("input");
    File[] inputs = inputPath.listFiles();
    PictureInput inputPicture = new PictureInput(inputs, mainNeuron);
    inputPicture.input();
    for (Neuron subneuron: mainNeuron.getSubneurons()) System.out.println(subneuron.getWeight());
    File analyzePath = new File("analyze");
    File[] analyzeFiles = analyzePath.listFiles();
    PictureAnalyzer analyzer = new PictureAnalyzer(analyzeFiles, mainNeuron);
    List<Boolean> results = analyzer.analyze();
    for (int i = 0; i < analyzeFiles.length; i++) System.out.println(String.format("%s: %s", analyzeFiles[i].getName(), results.get(i)));
  }
}
