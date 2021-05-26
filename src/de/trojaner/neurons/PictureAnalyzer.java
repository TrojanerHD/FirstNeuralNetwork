package de.trojaner.neurons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PictureAnalyzer {
  private File[] pictures;

  public PictureAnalyzer(File[] pictures) {
    this.pictures = pictures;
  }

  public void setPictures(File[] pictures) {
    this.pictures = pictures;
  }

  public float[][] getInput() throws IOException {
    List<List<Float>> inputs = new ArrayList<>();
    for (int i = 0; i < pictures.length; i++) {
      List<List<Color>> pixels = getPixels(i);
      List<Float> input = new ArrayList<>();
      for (List<Color> yPixels : pixels)
        for (Color pixel : yPixels)
          input.add(isBlack(pixel) ? 1f : 0f);

      inputs.add(input);
    }
    float[][] inputArray = new float[inputs.size()][inputs.get(0).size()];
    int i = 0;
    for (List<Float> input : inputs) {
      int j = 0;
      float[] tempInput = new float[input.size()];
      for (Float f : input)
        tempInput[j++] = f;
      inputArray[i++] = tempInput;
    }
    return inputArray;
  }

  private List<List<Color>> getPixels(int imageCount) throws IOException {
    List<List<Color>> pixels = new ArrayList<>();
    BufferedImage imageBuffer = ImageIO.read(this.pictures[imageCount]);
    for (int i = 0; i < imageBuffer.getHeight(); i++) {
      List<Color> yPixels = new ArrayList<>();
      for (int j = 0; j < imageBuffer.getWidth(); j++)
        yPixels.add(analyzePixel(imageBuffer, j, i));
      pixels.add(yPixels);
    }
    return pixels;
  }

  // Stolen from https://stackoverflow.com/a/9411208/9634099
  private Color analyzePixel(BufferedImage image, int x, int y) {
    int c = image.getRGB(x, y);
    int red = (c & 0x00ff0000) >> 16;
    int green = (c & 0x0000ff00) >> 8;
    int blue = c & 0x000000ff;

    return new Color(red, green, blue);
  }

  private boolean isBlack(Color color) {
    return color.getRed() < 128 && color.getBlue() < 128 && color.getGreen() < 128;
  }
}
