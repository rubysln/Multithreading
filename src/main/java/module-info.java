module ru.rubysln.multithreading {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  opens ru.rubysln.multithreading to javafx.fxml;
  exports ru.rubysln.multithreading;
  exports ru.rubysln.multithreading.controllers;
  opens ru.rubysln.multithreading.controllers to javafx.fxml;
  exports ru.rubysln.multithreading.models;
  opens ru.rubysln.multithreading.models to javafx.base;
}