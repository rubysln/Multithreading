package ru.rubysln.multithreading;

import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load());

    stage.setTitle("Multithreading");
    Image icon = new Image(
        Objects.requireNonNull(MainApplication.class.getResourceAsStream("images/icon.png")));
    stage.getIcons().add(icon);

    stage.setScene(scene);
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}