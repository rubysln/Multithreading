package ru.rubysln.multithreading.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.fxml.Initializable;
import ru.rubysln.multithreading.models.MathData;

public class MainController implements Initializable {
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

  @FXML
  private TableView<MathData> table2;

  @FXML
  private void startButtonClicked() {
    // Обработка нажатия кнопки "Start"
    int x = (int) slider1.getValue();
    int z = 2 * x - 1;
    int y = 3 * x - 1;

    fillTable(table1);
  }

  @FXML
  private void resetButtonClicked() {
    // Обработка нажатия кнопки "Reset"
    table1.getItems().clear();
    table2.getItems().clear();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fillTable(table1);
  }

  private void fillTable(TableView table1) {
    ObservableList<MathData> data = FXCollections.observableArrayList(); // Создаем список для данных

    for (int i = 1; i <= 60; i++) {
      int value1 = i * 10; // Пример значения для столбца C1
      int value2 = i * 20; // Пример значения для столбца C2
      data.add(new MathData(value1, value2)); // Создаем объект Data и добавляем его в список
    }

    table1.setItems(data); // Устанавливаем список данных в таблицу
  }
}
