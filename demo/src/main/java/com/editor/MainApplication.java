package com.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("主页面");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
