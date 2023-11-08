package ru.rubysln.multithreading.models;

public class MathData {
  private final int value1;
  private final int value2;

  public MathData(int value1, int value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  public int getValue1() {
    return value1;
  }

  public int getValue2() {
    return value2;
  }
}