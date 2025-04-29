package com.eventually.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 * Classe que representa a visualização principal da aplicação Eventually.
 * Esta tela organiza os principais componentes da interface, incluindo a barra lateral de navegação,
 * a barra superior com funcionalidades e a área central para exibição do conteúdo principal, como a lista de eventos.
 *
 * @author Yuri Garcia Maia
 * @version 1.01 - Refatorado para usar métodos de criação de containers
 * @since 2025-04-06
 * @author Gabriella Tavares Costa Corrêa
 * @since 2025-04-22 (Revisão e documentação da classe)
 */
public class MainView extends BorderPane {
    /**
     * Construtor da classe {@code MainView}.
     * Inicializa e organiza os elementos visuais da tela principal chamando métodos
     * auxiliares para criar cada seção (container) da interface:
     * <ul>
     * <li>Barra lateral ({@code VBox})</li>
     * <li>Barra superior ({@code HBox})</li>
     * <li>Conteúdo central ({@code VBox} contendo seletor de data e lista de eventos)</li>
     * </ul>
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}.
     */
    public MainView() {
        // Cria os containers (seções) usando métodos auxiliares
        VBox sidebar = createSidebar();
        HBox topBar = createTopBar();
        VBox centerContent = createCenterContent(); // O conteúdo central agora é criado em seu próprio método

        // Define a posição dos containers no BorderPane
        setLeft(sidebar);
        setTop(topBar);
        setCenter(centerContent);
    }

    /**
     * Cria e retorna o container da barra lateral (sidebar).
     * @return VBox configurado como sidebar.
     */
    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #6D1A80;");
        sidebar.setPrefWidth(200);
        sidebar.setAlignment(Pos.TOP_CENTER);

        Button homeButton = new Button("Página inicial");
        homeButton.setOnAction(event -> {
            System.out.println("Botão JavaFX 'Página inicial' clicado! (Lambda)");
            // Coloque aqui o código que você quer executar quando o botão for clicado
        });
        Button myEventsButton = new Button("Meus eventos");
        Button settingsButton = new Button("Configurações");

        homeButton.getStyleClass().add("menu-button");
        myEventsButton.getStyleClass().add("menu-button");
        settingsButton.getStyleClass().add("menu-button");

        // Espaçador para empurrar configurações para baixo
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(homeButton, myEventsButton, spacer, settingsButton);
        return sidebar;
    }

    /**
     * Cria e retorna o container da barra superior (top bar).
     * @return HBox configurado como top bar.
     */
    private HBox createTopBar() {
        HBox topBar = new HBox(40); // Aumentei o espaçamento para visualização
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        // Define a cor de fundo E a borda inferior
        String desiredBackgroundColor = "#5F115A"; // Exemplo: Azul claro (Hex code)
        // String desiredBackgroundColor = "lightblue"; // Exemplo: Azul claro (Nome da cor)
        // String desiredBackgroundColor = "rgba(173, 216, 230, 0.8)"; // Exemplo: Azul claro com 80% de opacidade

        topBar.setStyle(
                "-fx-background-color: " + desiredBackgroundColor + "; " +
                        "-fx-border-color: lightgray; " +
                        "-fx-border-width: 0 0 1 0;"
        );

        Label logo = new Label("Eventually");
        logo.getStyleClass().add("logo");

        Button programBtn = new Button("Programação");
        Button agendaBtn = new Button("Minha agenda");
        Button userBtn = new Button("Usuário");

        Circle avatar = new Circle(20, Color.LIGHTGRAY);

        programBtn.getStyleClass().add("top-button");
        agendaBtn.getStyleClass().add("top-button");
        // userBtn também poderia ter um estilo 'top-button' ou similar

        // Container para o botão de usuário e avatar
        HBox userBox = new HBox(10, userBtn, avatar);
        userBox.setAlignment(Pos.CENTER); // Centraliza itens dentro do userBox

        // Espaçador para empurrar userBox para a direita
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(logo, spacer, programBtn, agendaBtn, userBox);
        return topBar;
    }

    /**
     * Cria e retorna o container do seletor de datas (date picker).
     * @return HBox configurado como date picker.
     */
    private HBox createDatePicker() {
        HBox datePicker = new HBox(10);
        datePicker.setAlignment(Pos.CENTER);
        datePicker.setPadding(new Insets(10, 20, 10, 20)); // Adicionado padding horizontal
        datePicker.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;"); // Adiciona uma borda inferior

        ToggleGroup dateGroup = new ToggleGroup();
        String[] dates = {"Ter\n01", "Qua\n02", "Qui\n03", "Sex\n04", "Sab\n05"};

        for (int i = 0; i < dates.length; i++) {
            ToggleButton btn = new ToggleButton(dates[i]);
            btn.getStyleClass().add("date-button");
            btn.setToggleGroup(dateGroup);
            // Mantém a lógica de selecionar o primeiro por padrão
            if (i == 0) {
                btn.setSelected(true);
            }
            datePicker.getChildren().add(btn);
        }
        return datePicker;
    }

    /**
     * Cria e retorna o container para a lista de eventos.
     * (Atualmente, apenas cria o VBox; eventos seriam adicionados aqui posteriormente).
     * @return VBox configurado para a lista de eventos.
     */
    private VBox createEventList() {
        VBox eventList = new VBox(15);
        // Container vazio
        eventList.setPadding(new Insets(20));
        // Exemplo: Adicionar um placeholder
        // Label placeholder = new Label("Nenhum evento para exibir.");
        // eventList.getChildren().add(placeholder);
        return eventList;
    }

    /**
     * Cria e retorna o container principal da área central,
     * agrupando o seletor de datas e a lista de eventos.
     * @return VBox contendo o conteúdo central.
     */
    private VBox createCenterContent() {
        HBox datePicker = createDatePicker();
        VBox eventList = createEventList();

        VBox centerContent = new VBox(); // Não precisa de espaçamento aqui se os filhos já têm padding
        centerContent.getChildren().addAll(datePicker, eventList);
        VBox.setVgrow(eventList, Priority.ALWAYS); // Faz a lista de eventos crescer para preencher o espaço

        return centerContent;
    }
}