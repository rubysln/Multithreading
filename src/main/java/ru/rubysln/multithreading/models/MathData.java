package ru.rubysln.multithreading.models;

public class MathData {
  // Дополнительный класс для хранения данных вычислительного потока
  private final int y; // y = 3 * x - 1
  private final int z; // z = 2 * x - 1

  public MathData(int value1, int value2) {
    this.y = value1;
    this.z = value2;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }
}