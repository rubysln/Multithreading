package ru.rubysln.multithreading.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.rubysln.multithreading.MainApplication;
import ru.rubysln.multithreading.models.MathData;

public class MainController implements Initializable {

  private int z; // z = 2 * x - 1
  private int y; // y = 3 * x - 1
  private int firstSliderValue; // Значение слайдера для редактирования скорости работы первого потока
  private int secondSliderValue; // Значение слайдера для редактирования скорости работы второго потока
  private Thread calculateThread; // Первый поток
  private Thread createThread; // Второй поток
  private Semaphore calculateSemaphore; // Семафор первого потока
  private Semaphore createSemaphore; // Семафор второго потока

  private ArrayList<Stage> additionalStage; // Хранилище для ссылок на дополнительные окна
  private int stageCount = 0; // Счётчик окон

  // Далее создаются модели из интерфейса

  @FXML
  private TableColumn<Integer, Integer> column1;

  @FXML
  private TableColumn<Integer, Integer> column2;

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
  private TableView<MathData> table1;

  // Здесь создание моделей завершается

  @FXML
  private void startButtonClicked(ActionEvent event) throws InterruptedException {
    // Обработка нажатия кнопки "Start"
    if (calculateThread.isInterrupted()) {
      calculateThread = new Thread(this::firstThreadMethod);
    }
    if(createThread.isInterrupted()){
      createThread = new Thread(this::secondThreadMethod);
    }
    calculateThread.start();
    createThread.start();
  }

  @FXML
  private void resetButtonClicked() {
    // Обработка нажатия кнопки "Reset"
    for(int index = 0; index < stageCount; index++){
      if(additionalStage.get(index) == null) continue;
      additionalStage.get(index).close();
    }
    table1.getItems().clear();
  }

  @FXML
  private void stopButtonClicked() {
    // Обработка нажатия кнопки "Stop"
    calculateThread.interrupt();
    createThread.interrupt();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Метод при запуске окна
    createThread = new Thread(this::secondThreadMethod);
    calculateThread = new Thread(this::firstThreadMethod);
    calculateSemaphore = new Semaphore(0);
    createSemaphore = new Semaphore(1);
    firstSliderValue = (int) (5000 * (slider1.getValue() / 100));
    secondSliderValue = (int) (5000 * (slider2.getValue() / 100));
    additionalStage = new ArrayList<>();

    // Добавление слушателей для фиксаций изменения слайдеров
    slider1.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observableValue, Number number,
          Number t1) {

        firstSliderValue = (int) (5000 * (slider1.getValue() / 100));
      }
    });
    slider2.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observableValue, Number number,
          Number t1) {

        secondSliderValue = (int) (5000 * (slider2.getValue() / 100));
      }
    });
  }

  private void firstThreadMethod() {
    // Метод первого потока
    while (!calculateThread.isInterrupted()) {
      for (int x = 3; x <= 6; x++) {
        y = 3 * x - 1;
        z = 2 * x - 1;
        x += 1;
        table1.getItems().add(new MathData(y, z));
        try {
          Thread.sleep(firstSliderValue);
          createSemaphore.acquire();
          createNewWindowWithFields(x + z);
          calculateSemaphore.release();
        } catch (InterruptedException e) {
          System.out.println("Поток остановлен");
          calculateThread.interrupt();
        }
      }
    }
  }

  private void secondThreadMethod() {
    // Метод второго потока
    while (!createThread.isInterrupted()){
      try {
        Thread.sleep(secondSliderValue);
        calculateSemaphore.acquire();
        createNewWindowWithButtons();
        createSemaphore.release();
      } catch (InterruptedException e) {
        createThread.interrupt();
      }
    }
  }

  private void createNewWindowWithButtons(){
    Platform.runLater(() -> {
      Stage stage = new Stage();

      additionalStage.add(stage);
      stageCount++;

      Image icon = new Image(
          Objects.requireNonNull(MainApplication.class.getResourceAsStream("images/icon.png")));
      stage.getIcons().add(icon);

      stage.setTitle(y + " кнопок");

      VBox root = new VBox();
      for (int index = 0; index < y; index++) {
        Button button = new Button("Кнопка - " + index);
        root.getChildren().add(button);
      }

      Scene scene = new Scene(root, 400, 300);
      stage.setScene(scene);

      stage.setY(100);
      stage.setX(1000);
      stage.show();
    });
  }

  private void createNewWindowWithFields(int result) {
    // Метод создания окон со строками ввода равным (x + z)
    Platform.runLater(() -> {
      Stage stage = new Stage();

      additionalStage.add(stage);
      stageCount++;

      Image icon = new Image(
          Objects.requireNonNull(MainApplication.class.getResourceAsStream("images/icon.png")));
      stage.getIcons().add(icon);

      stage.setTitle(result + " строк ввода");

      VBox root = new VBox();
      for (int index = 0; index < result; index++) {
        TextField textField = new TextField("Строка ввода - " + index);
        root.getChildren().add(textField);
      }

      Scene scene = new Scene(root, 400, 300);
      stage.setScene(scene);

      stage.setY(100);
      stage.setX(100);
      stage.show();
    });
  }
}
