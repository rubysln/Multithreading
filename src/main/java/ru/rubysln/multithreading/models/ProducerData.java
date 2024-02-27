package ru.rubysln.multithreading.models;


public class ProducerData {
  private String actual;

  public ProducerData(String actual, int iterator) {
    this.actual = actual + " " + iterator;
  }

  public String getActual() {
    return actual;
  }

  public void setActual(String actual){
    this.actual = actual;
  }
}
