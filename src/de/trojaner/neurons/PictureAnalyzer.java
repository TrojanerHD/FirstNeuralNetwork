package de.trojaner.neurons;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PictureAnalyzer {
  private File[] pictures;

  /**
   * Analyzes given pictures
   * 
   * @param pictures Pictures to analyze
   */
  public PictureAnalyzer(File[] pictures) {
    this.pictures = pictures;
  }

  /**
   * Set the pictures to analyze to some other pictures
   * 
   * @param pictures Some other pictures
   */
  public void setPictures(File[] pictures) {
    this.pictures = pictures;
  }

  /**
   * Get an input for the neural network based on the provided pictures as matrix
   * <p>
   * y = # of picture
   * <p>
   * x = # of pixel in picture (left to right, top to bottom)
   * <p>
   * value = whether the pixel x is black or not
   * 
   * @return The input for the neural network
   * @throws IOException If the pictures cannot be opened
   */
  public float[][] getInput() throws IOException {
    // Declare input array
    float[][] inputs = new float[0][0];
    // Check each picture
    for (int i = 0; i < this.pictures.length; i++) {
      // Get the pixels of the picture
      Color[][] pixels = getPixels(i);
      // Initialize input array since now we know the width and height of a picture
      // and thus the length of the array
      if (i == 0)
        inputs = new float[this.pictures.length][pixels.length * pixels[0].length];
      // Initialize a counter
      int count = 0;
      // Check whether each pixel is black or not and if it's black store a 1
      // otherwise a 0 for the pixel
      for (Color[] yPixels : pixels)
        for (Color pixel : yPixels)
          inputs[i][count++] = isBlack(pixel) ? 1f : 0f;
    }
    return inputs;
  }

  /**
   * Get the pixels of a picture stored as matrix
   * <p>
   * y = y coordinate
   * <p>
   * x = x coordinate
   * <p>
   * value: color of the pixel (x, y)
   * 
   * @param imageCount The image number in the this.pictures array
   * @return A matrix of pixels
   * @throws IOException If the picture cannot be opened
   */
  private Color[][] getPixels(int imageCount) throws IOException {
    BufferedImage imageBuffer = ImageIO.read(this.pictures[imageCount]);
    Color[][] pixels = new Color[imageBuffer.getHeight()][imageBuffer.getWidth()];
    for (int i = 0; i < imageBuffer.getHeight(); i++)
      for (int j = 0; j < imageBuffer.getWidth(); j++)
        pixels[i][j] = analyzePixel(imageBuffer, j, i);

    return pixels;
  }

  /**
   * Analyzes a pixel of a provided picture
   * 
   * @param image The picture to analyze
   * @param x     The x coordinate in the picture
   * @param y     The y coordinate in the picture
   * @return The color of the pixel
   */
  // Stolen from https://stackoverflow.com/a/9411208/9634099
  private Color analyzePixel(BufferedImage image, int x, int y) {
    int c = image.getRGB(x, y);
    int red = (c & 0x00ff0000) >> 16;
    int green = (c & 0x0000ff00) >> 8;
    int blue = c & 0x000000ff;

    return new Color(red, green, blue);
  }

  /**
   * Determines whether the given color is more black than white or vice versa.
   * Requires the color to be grayscaled to work properly
   * 
   * @param color The color to check
   * @return Whether the color is black
   */
  private boolean isBlack(Color color) {
    return color.getRed() < 128 && color.getBlue() < 128 && color.getGreen() < 128;
  }
}
