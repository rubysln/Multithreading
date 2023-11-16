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
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader firstTaskFXML = new FXMLLoader(MainApplication.class.getResource("multithreading-view.fxml"));
    FXMLLoader secondTaskFXML = new FXMLLoader(MainApplication.class.getResource("producer-consumer-view.fxml"));

    Scene firstTaskScene = new Scene(firstTaskFXML.load());
    Scene secondTaskScene = new Scene(secondTaskFXML.load());

    Stage firstStage = new Stage();
    firstStage.setTitle("MathTask");
    firstStage.setScene(firstTaskScene);

    Stage secondStage = new Stage();
    secondStage.setTitle("DekkerTask");
    secondStage.setScene(secondTaskScene);

    Image icon = new Image(
        Objects.requireNonNull(MainApplication.class.getResourceAsStream("images/icon.png")));

    firstStage.getIcons().add(icon);
    secondStage.getIcons().add(icon);

    firstStage.setAlwaysOnTop(true);
    secondStage.setAlwaysOnTop(true);
    firstStage.show();
    secondStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
