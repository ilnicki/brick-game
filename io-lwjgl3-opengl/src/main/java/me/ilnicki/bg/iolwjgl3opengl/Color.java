package me.ilnicki.bg.iolwjgl3opengl;

public class Color {
  private final byte red;
  private final byte green;
  private final byte blue;

  private final float factor = 1.0f / 255;

  Color(int red, int green, int blue) {
    this.red = (byte) red;
    this.green = (byte) green;
    this.blue = (byte) blue;
  }

  byte getR() {
    return red;
  }

  byte getG() {
    return green;
  }

  byte getB() {
    return blue;
  }

  float getFloatR() {
    return red * factor;
  }

  float getFloatG() {
    return green * factor;
  }

  float getFloatB() {
    return blue * factor;
  }

  @Override
  public String toString() {
    return String.format("#%02x%02x%02x", this.red, this.red, this.blue).toUpperCase();
  }
}
