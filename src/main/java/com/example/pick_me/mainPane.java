package com.example.pick_me;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainPane extends Application {
    @Override
    public void start(Stage Primarystage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(mainPane.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 800);
        Primarystage.setTitle("Pick Me");
        Primarystage.setScene(scene);
        Primarystage.setResizable(false);
        Primarystage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}