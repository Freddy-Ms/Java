package org.example.multithreading;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;


public class ApplicationWindow extends Application  {
    @Override
    public void start(Stage stage) throws IOException {
        LoggerService.log("Application started", "INFO", 0);

        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationWindow.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        stage.setTitle("Image Processing");
        stage.setWidth(750);
        stage.setHeight(500);
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> LoggerService.log("Application closed", "INFO", 0));
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
