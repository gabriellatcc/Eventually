package com.eventually.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/seuprojeto/frontend/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600); // Define o tamanho da janela
        stage.setTitle("Minha Aplicação JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
