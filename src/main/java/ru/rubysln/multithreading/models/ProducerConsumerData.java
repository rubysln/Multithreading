package ru.rubysln.multithreading.models;

public class ProducerConsumerData {
  private final String actual;

  public ProducerConsumerData(String actual) {
    this.actual = actual;
  }

  public String getActual() {
    return actual;
  }
}
