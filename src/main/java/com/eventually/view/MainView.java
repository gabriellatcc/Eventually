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
import java.util.ArrayList;
import java.util.List;

// representa a visualização principal da aplicação
public class MainView extends BorderPane {

    // --- campos para os componentes interativos ---
    private Button botaoInicio;
    private Button botaoMeusEventos;
    private Button botaoConfiguracoes;
    private Button programBtn;
    private Button agendaBtn;
    private Button userBtn;
    private ToggleGroup dateGroup;
    private List<ToggleButton> dateButtons;
    private VBox eventList;

    // construtor
    public MainView() {
        this.dateButtons = new ArrayList<>();

        // Cria os containers (seções) usando métodos auxiliares
        VBox barraLateral = criarBarraLateral();
        HBox barraSuperior = criarBarraSuperior();
        VBox conteudoCentral = createCenterContent(); // O conteúdo central agora é criado em seu próprio método (adaptado)

        // Define a posição dos containers no BorderPane
        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    // cria a barra lateral
    private VBox criarBarraLateral() {
        VBox barraLateral = new VBox(20);
        barraLateral.setPadding(new Insets(20));
        barraLateral.setStyle("-fx-background-color: #7A2074;");
        barraLateral.setPrefWidth(200);
        barraLateral.setAlignment(Pos.TOP_CENTER);

        this.botaoInicio = new Button("Página inicial");
        this.botaoMeusEventos = new Button("Meus eventos");
        this.botaoConfiguracoes = new Button("Configurações");

        this.botaoInicio.getStyleClass().add("menu-button");
        this.botaoMeusEventos.getStyleClass().add("menu-button");
        this.botaoConfiguracoes.getStyleClass().add("menu-button");

        // Espaçador para empurrar configurações para baixo
        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        barraLateral.getChildren().addAll(this.botaoInicio, this.botaoMeusEventos, espacador, this.botaoConfiguracoes);
        return barraLateral;
    }

    // cria a barra superior
    private HBox criarBarraSuperior() {
        HBox barraSuperior = new HBox(40);
        barraSuperior.setPadding(new Insets(20));
        barraSuperior.setAlignment(Pos.CENTER_LEFT);
        // Define a cor de fundo E a borda inferior
        String desiredBackgroundColor = "#5F115A";
        barraSuperior.setStyle(
                "-fx-background-color: " + desiredBackgroundColor + "; " +
                        "-fx-border-color: lightgray; " +
                        "-fx-border-width: 0 0 1 0;"
        );

        Label logo = new Label("Eventually");
        logo.getStyleClass().add("logo");

        this.programBtn = new Button("Programação");
        this.agendaBtn = new Button("Minha agenda");
        this.userBtn = new Button("Usuário");

        Circle avatar = new Circle(20, Color.LIGHTGRAY);

        this.programBtn.getStyleClass().add("top-button");
        this.agendaBtn.getStyleClass().add("top-button");
        // userBtn também poderia ter um estilo 'top-button' ou similar

        // Container para o botão de usuário e avatar
        HBox userBox = new HBox(10, this.userBtn, avatar);
        userBox.setAlignment(Pos.CENTER); // Centraliza itens dentro do userBox

        // Espaçador para empurrar userBox para a direita
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        barraSuperior.getChildren().addAll(logo, spacer, this.programBtn, this.agendaBtn, userBox);
        return barraSuperior;
    }

    // cria o seletor de datas
    private HBox createDatePicker() {
        HBox datePicker = new HBox(10);
        datePicker.setAlignment(Pos.CENTER);
        datePicker.setPadding(new Insets(10, 20, 10, 20)); // Adicionado padding horizontal
        datePicker.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;"); // Adiciona uma borda inferior

        this.dateGroup = new ToggleGroup();
        String[] dates = {"Ter\n01", "Qua\n02", "Qui\n03", "Sex\n04", "Sab\n05"};

        this.dateButtons.clear();

        for (int i = 0; i < dates.length; i++) {
            ToggleButton btn = new ToggleButton(dates[i]);
            btn.getStyleClass().add("date-button");
            btn.setToggleGroup(this.dateGroup);
            // Mantém a lógica de selecionar o primeiro por padrão
            if (i == 0) {
                btn.setSelected(true);
            }
            this.dateButtons.add(btn);
            datePicker.getChildren().add(btn);
        }
        return datePicker;
    }

    // cria a lista de eventos (atualmente apenas o container)
    private VBox createEventList() {
        // Container vazio
        this.eventList = new VBox(15);
        this.eventList.setPadding(new Insets(20));
        // Exemplo: Adicionar um placeholder
        // Label placeholder = new Label("Nenhum evento para exibir.");
        // eventList.getChildren().add(placeholder);
        return this.eventList;
    }

    // cria o conteúdo central (agrupando date picker e lista de eventos)
    private VBox createCenterContent() {
        HBox datePicker = createDatePicker();
        createEventList(); // inicializa o campo this.eventList

        // Não precisa de espaçamento aqui se os filhos já têm padding
        VBox centerContent = new VBox();
        centerContent.getChildren().addAll(datePicker, this.eventList);
        // Faz a lista de eventos crescer para preencher o espaço
        VBox.setVgrow(this.eventList, Priority.ALWAYS);

        return centerContent;
    }

    // --- getters para os componentes interativos ---

    // retorna o botão "Página inicial"
    public Button getHomeButton() {
        return botaoInicio;
    }

    // retorna o botão "Meus eventos"
    public Button getMyEventsButton() {
        return botaoMeusEventos;
    }

    // retorna o botão "Configurações"
    public Button getSettingsButton() {
        return botaoConfiguracoes;
    }

    // retorna o botão "Programação"
    public Button getProgramButton() {
        return programBtn;
    }

    // retorna o botão "Minha agenda"
    public Button getAgendaButton() {
        return agendaBtn;
    }

    // retorna o botão "Usuário"
    public Button getUserButton() {
        return userBtn;
    }

    // retorna o grupo de botões de data
    public ToggleGroup getDateGroup() {
        return dateGroup;
    }

    // retorna a lista de botões de data
    public List<ToggleButton> getDateButtons() {
        return dateButtons;
    }

    // retorna o container da lista de eventos
    public VBox getEventList() {
        return eventList;
    }
}