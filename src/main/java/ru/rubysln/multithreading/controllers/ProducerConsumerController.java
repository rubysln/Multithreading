package ru.rubysln.multithreading.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import ru.rubysln.multithreading.models.MathData;
import ru.rubysln.multithreading.models.ProducerConsumerData;

public class ProducerConsumerController implements Initializable {

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
  private TableView<ProducerConsumerData> table1;

  @FXML
  private TableView<ProducerConsumerData> table2;

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
    // Метод потока потребителя
    synchronized (ProducerConsumerController.class) {
      while (!consumerThread.isInterrupted()) {
        while (flag[0]) {
          try {
            ProducerConsumerController.class.wait();
          } catch (InterruptedException e) {
            return;
          }
        }

        table2.getItems().add(new ProducerConsumerData("Consuming item"));
        flag[1] = true;

        if (turn == 1) {
          turn = 0;
          flag[1] = false;
          try {
            Thread.sleep(firstSliderValue);
            ProducerConsumerController.class.notify();
          } catch (InterruptedException e) {
            System.out.println("Поток остановлен");
            consumerThread.interrupt();
          }
        } else {
          flag[0] = true;
          flag[1] = false;
          try {
            Thread.sleep(firstSliderValue);
            ProducerConsumerController.class.notify();
          } catch (InterruptedException e) {
            System.out.println("Поток остановлен");
            consumerThread.interrupt();
          }
        }
      }
    }
  }

  private void producerThreadMethod() {
    // Метод потока производителя
    synchronized (ProducerConsumerController.class) {
      while (!producerThread.isInterrupted()) {
        while (flag[1]) {
          try {
            ProducerConsumerController.class.wait();
          } catch (InterruptedException e) {
            return;
          }
        }

        table1.getItems().add(new ProducerConsumerData("Producing item"));
        flag[0] = true;

        if (turn == 0) {
          turn = 1;
          flag[0] = false;
          try {
            Thread.sleep(secondSliderValue);
            ProducerConsumerController.class.notify();
          } catch (InterruptedException e) {
            System.out.println("Поток остановлен");
            producerThread.interrupt();
          }
        } else {
          flag[0] = false;
          flag[1] = true;
          try {
            Thread.sleep(secondSliderValue);
            ProducerConsumerController.class.notify();
          } catch (InterruptedException e) {
            System.out.println("Поток остановлен");
            producerThread.interrupt();
          }
        }
      }
    }
  }
}
