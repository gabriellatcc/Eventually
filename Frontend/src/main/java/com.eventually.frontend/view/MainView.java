package com.eventually.frontend.view;

import com.eventually.frontend.view.EventCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MainView extends BorderPane {

    public MainView() {
        // Sidebar
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #6D1A80;");
        sidebar.setPrefWidth(200);

        Button homeButton = new Button("Página inicial");
        Button myEventsButton = new Button("Meus eventos");
        Button settingsButton = new Button("Configurações");

        homeButton.getStyleClass().add("menu-button");
        myEventsButton.getStyleClass().add("menu-button");
        settingsButton.getStyleClass().add("menu-button");

        VBox.setVgrow(settingsButton, Priority.ALWAYS);
        sidebar.getChildren().addAll(homeButton, myEventsButton, new Region(), settingsButton);

        // Top bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label logo = new Label("Eventually");
        logo.getStyleClass().add("logo");

        Button programBtn = new Button("Programação");
        Button agendaBtn = new Button("Minha agenda");
        Button userBtn = new Button("Usuário");

        Circle avatar = new Circle(20, Color.LIGHTGRAY);

        programBtn.getStyleClass().add("top-button");
        agendaBtn.getStyleClass().add("top-button");

        HBox userBox = new HBox(10, userBtn, avatar);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(userBox, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, spacer, programBtn, agendaBtn, userBox);

        // Date Picker
        HBox datePicker = new HBox(10);
        datePicker.setAlignment(Pos.CENTER);
        datePicker.setPadding(new Insets(10));

        ToggleGroup dateGroup = new ToggleGroup();
        String[] dates = {"Ter\n01", "Qua\n02", "Qui\n03", "Sex\n04", "Sab\n05"};

        for (int i = 0; i < dates.length; i++) {
            ToggleButton btn = new ToggleButton(dates[i]);
            btn.getStyleClass().add("date-button");
            btn.setToggleGroup(dateGroup);
            if (i == 0) btn.setSelected(true);
            datePicker.getChildren().add(btn);
        }

        // Event List
        VBox eventList = new VBox(15);
        eventList.setPadding(new Insets(20));

        for (int i = 0; i < 3; i++) {
            eventList.getChildren().add(new EventCard());
        }

        VBox centerContent = new VBox(datePicker, eventList);
        centerContent.setSpacing(10);

        setLeft(sidebar);
        setTop(topBar);
        setCenter(centerContent);
    }
}
