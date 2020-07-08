package me.ilnicki.bg.iolwjgl3opengl;

public final class Color {

  private static final float FACTOR = 1.0f / 255;

  private final byte red;
  private final byte green;
  private final byte blue;

  Color(byte red, byte green, byte blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  Color(int red, int green, int blue) {
    this((byte) red, (byte) green, (byte) blue);
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
    return red * FACTOR;
  }

  float getFloatG() {
    return green * FACTOR;
  }

  float getFloatB() {
    return blue * FACTOR;
  }

  @Override
  public String toString() {
    return String.format("#%02x%02x%02x", red, green, blue).toUpperCase();
  }
}
