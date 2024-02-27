package ru.rubysln.multithreading.models;

/**
 * @author Artem Maklakov
 */


public class BufferData {

    private final String actual;

    public BufferData(String actual, int iterator) {
        this.actual = actual + " " + iterator;
    }

    public String getActual() {
        return actual;
    }
}
