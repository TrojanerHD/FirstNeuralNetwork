package de.trojaner.neurons.picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import javax.imageio.ImageIO;

abstract class Picture {
  private File[] pictures;

  Picture(File[] pictures) {
    this.pictures = pictures;
  }

  List<List<Color>> getPixels(int imageCount) throws IOException {
    List<List<Color>> pixels = new ArrayList();
    BufferedImage imageBuffer = ImageIO.read(this.pictures[imageCount]);
    for (int i = 0; i < imageBuffer.getHeight(); i++) {
      List<Color> yPixels = new ArrayList();
      for (int j = 0; j < imageBuffer.getWidth(); j++) yPixels.add(analyzePixel(imageBuffer, i, j));
      pixels.add(yPixels);
    }
    return pixels;
  }

  //Stolen from https://stackoverflow.com/a/9411208/9634099
  private Color analyzePixel(BufferedImage image, int x, int y) {
    int c = image.getRGB(x, y);
    int red = (c & 0x00ff0000) >> 16;
    int green = (c & 0x0000ff00) >> 8;
    int blue = c & 0x000000ff;

    return new Color(red, green, blue);
  }
}
