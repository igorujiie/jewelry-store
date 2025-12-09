package com.kazuhiko.jewelry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseConnection.initialize();
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        stage.setTitle("Jewelry Store - Sistema de Gestão");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
