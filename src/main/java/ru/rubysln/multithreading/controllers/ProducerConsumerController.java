package ru.rubysln.multithreading.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import ru.rubysln.multithreading.models.BufferData;
import ru.rubysln.multithreading.models.ConsumerData;
import ru.rubysln.multithreading.models.ProducerData;

public class ProducerConsumerController implements Initializable {

    // Создание очередей для организации работы потребителя и производителя
    private BlockingDeque<ProducerData> queue = new LinkedBlockingDeque<>();

    private List<Integer> queueList = new ArrayList<Integer>();

    private int firstSliderValue; // Значение слайдера для редактирования скорости работы первого потока
    private int secondSliderValue; // Значение слайдера для редактирования скорости работы второго потока

    private Thread producerThread; // Поток для работы производителя
    private Thread consumerThread; // Поток для работы потребителя

    // Поля для реализации алгоритма Деккера
    private static final int N = 2;
    private static boolean[] flag = new boolean[N];
    private static int turn = 0;

    private static int item = 0;

    // Далее создаются модели из интерфейса
    @FXML
    private TableView<ProducerData> table1;

    @FXML
    private TableView<ConsumerData> table2;

    @FXML
    private TableView<BufferData> table3;

    @FXML
    private TableColumn<String, String> column1;

    @FXML
    private TableColumn<String, String> column2;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button resetButton;

    @FXML
    private Slider slider1;

    @FXML
    private Slider slider2;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label stoppedLabel;


    // Создание моделей завершается

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Метод при запуске окна
        producerThread = new Thread(this::producerThreadMethod);
        consumerThread = new Thread(this::consumerThreadMethod);
        firstSliderValue = (int) (5000 * (slider1.getValue() / 100));
        secondSliderValue = (int) (5000 * (slider2.getValue() / 100));
        label1.setText(String.valueOf(firstSliderValue));
        label2.setText(String.valueOf(secondSliderValue));
        stoppedLabel.setVisible(false);
    }

    public void resetButtonClicked(ActionEvent event) {
        // Обработка нажатия кнопки Reset
        table1.getItems().clear();
        table2.getItems().clear();
        table3.getItems().clear();
    }

    public void firstSliderChanged(MouseEvent mouseEvent) {
        // Обработка изменения позиции первого слайдера
        firstSliderValue = (int) (5000 * (slider1.getValue() / 100));
        label1.setText(String.valueOf(firstSliderValue));
    }

    public void secondSliderChanged(MouseEvent mouseEvent) {
        // Обработка изменения позиции второго слайдера
        secondSliderValue = (int) (5000 * (slider2.getValue() / 100));
        label2.setText(String.valueOf(secondSliderValue));
    }

    public void stopButtonClicked(ActionEvent event) {
        // Обработка нажатия кнопки Stop
        producerThread.interrupt();
        consumerThread.interrupt();

        stoppedLabel.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
        visiblePause.setOnFinished(e -> stoppedLabel.setVisible(false)); // Скрываем надпись после паузы
        visiblePause.play();
    }

    public void startButtonClicked(ActionEvent event) {
        // Обработка нажатия кнопки Start
        if (producerThread.isInterrupted()) {
            producerThread = new Thread(this::producerThreadMethod);
        }
        if (consumerThread.isInterrupted()) {
            consumerThread = new Thread(this::consumerThreadMethod);
        }
        producerThread.start();
        consumerThread.start();
    }

    private void consumerThreadMethod() {
        while (!consumerThread.isInterrupted()) {
            try {
                if (!table3.getItems().isEmpty()) {
                    Thread.sleep(firstSliderValue);
                    ProducerData producerData = queue.take();
                    String iterator = producerData.getActual().substring(14);
                    ConsumerData consumerData = new ConsumerData("Consuming item " + iterator);
                    table2.getItems().add(consumerData);
                    table3.getItems().removeFirst();
                }
            } catch (InterruptedException e) {
                System.out.println("Поток остановлен");
                return;
            }
        }
    }

    private void producerThreadMethod() {
        while (!producerThread.isInterrupted()) {
            try {
                Thread.sleep(secondSliderValue);
                if (queueList.isEmpty()) queueList.add(1);
                else queueList.add(queueList.size() + 1);
                ProducerData producerData = new ProducerData("Producing item", queueList.get(queueList.size() - 1));
                queue.put(producerData);
                table1.getItems().add(producerData);
                table3.getItems().add(new BufferData("Item", queueList.get(queueList.size() - 1)));
            } catch (InterruptedException e) {
                System.out.println("Поток остановлен");
                return;
            }
        }
    }
}
