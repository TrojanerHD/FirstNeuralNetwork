package de.trojaner.neurons.picture;

import de.trojaner.neurons.Neuron;
import de.trojaner.neurons.picture.Picture;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PictureInput extends Picture {
  private Neuron neuron;
  private File[] pictures;

  public PictureInput(File[] pictures, Neuron neuron) {
    super(pictures);
    this.pictures = pictures;
    this.neuron = neuron;
  }

  public void input() {
    for (int i = 0; i < this.pictures.length; i++) analyze(i);
  }

  private void analyze(int pictureCount) {
    try {
      List<List<Color>> pixels = getPixels(pictureCount);
      for (int i = 0; i < pixels.size(); i++) {
        List<Color> yPixels = pixels.get(i);
        for (int j = 0; j < yPixels.size(); j++) {
          Color pixel = yPixels.get(j);
          if (!pixel.equals(Color.BLACK)) continue;
          //System.out.println(String.format("Pixel: x:%d,y:%d", j, i));
          for (int y = i > 0 ? i - 1 : i; y <= (i < pixels.size() - 1 ? i + 1 : i); y++)
            for (int x = j > 0 ? j - 1 : y == i ? j + 1 : j; x <= (j < yPixels.size() - 1 ? j + 1 : j); x += y == i ? 2 : 1) {
              //System.out.println(String.format("x:%d, y:%d", x, y));
              int count = y - i != 0 ? x - j + (y - i == -1 ? 1 : 6) : x - j > 0 ? 4 : 3;
              //System.out.println(count);
              Neuron subneuron = this.neuron.getSubneurons()[count];
              subneuron.changeWeight(pixels.get(y).get(x).equals(Color.BLACK) ? 1 : -1);
            }
        }

      }
      //for (Neuron subneuron: this.neuron.getSubneurons()) System.out.println(subneuron.getWeight());
    } catch (IOException e) {
      System.err.println(e);
    }
  }
}
