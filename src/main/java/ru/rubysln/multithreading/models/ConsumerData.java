package ru.rubysln.multithreading.models;

/**
 * @author Artem Maklakov
 */
public class ConsumerData {
    private final String actual;

    public ConsumerData(String actual) {
        this.actual = actual;
    }

    public String getActual() {
        return actual;
    }
}
