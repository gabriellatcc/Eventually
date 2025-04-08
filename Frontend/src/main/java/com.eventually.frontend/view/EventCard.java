package com.eventually.frontend.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class EventCard extends HBox {

    public EventCard() {
        setPadding(new Insets(15));
        setSpacing(20);
        setStyle("-fx-background-color: linear-gradient(to right, #8e2de2, #c84cf4); -fx-background-radius: 20;");
        setPrefHeight(100);

        VBox textBox = new VBox(5);
        Label title = new Label("Título do evento");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: white;");

        Label location = new Label("Local do evento");
        location.setStyle("-fx-text-fill: white;");

        textBox.getChildren().addAll(title, location);

        VBox timeBox = new VBox();
        timeBox.setStyle("-fx-text-fill: white;");
        timeBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        Label day = new Label("SEX 14,");
        Label monthYear = new Label("MAR 2025");
        Label hour = new Label("18:20");

        day.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        monthYear.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        hour.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        timeBox.getChildren().addAll(day, monthYear, hour);

        HBox.setHgrow(textBox, Priority.ALWAYS);
        this.getChildren().addAll(textBox, timeBox);
    }
}
