package com.eventually.frontend;

import com.eventually.frontend.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        // Adiciona ícone
        primaryStage.getIcons().add(new Image(getClass().getResource("/icons/app-icon.png").toExternalForm()));

        primaryStage.setTitle("Eventually");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
