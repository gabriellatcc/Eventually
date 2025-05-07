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
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// representa a visualização principal da aplicação
public class MainView extends BorderPane {

    private Button botaoInicio;
    private Button botaoMeusEventos;
    private Button botaoConfiguracoes;
    private Button programBtn;
    private Button agendaBtn;
    private Button newEventBtn;
    private Circle avatar;
    private ToggleGroup dateGroup;
    private List<ToggleButton> dateButtons;
    private VBox eventList;

    private Label userNameLabel;
    private Stage primaryStage;
    private LocalDate selectedDate;

    public MainView() {
        this.dateButtons = new ArrayList<>();
        this.getStyleClass().add("main-view");
        this.selectedDate = LocalDate.now();

        VBox barraLateral = criarBarraLateral();
        HBox barraSuperior = criarBarraSuperior();
        VBox conteudoCentral = createCenterContent();

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);

        setupEventHandlers();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    // cria a classe do container da sidebar
    private VBox criarBarraLateral() {
        VBox barraLateral = new VBox(20);
        barraLateral.setPadding(new Insets(20));
        barraLateral.getStyleClass().add("sidebar");
        barraLateral.setPrefWidth(200);
        barraLateral.setAlignment(Pos.TOP_CENTER);

        this.botaoInicio = new Button("Página inicial");
        this.botaoMeusEventos = new Button("Meus eventos");
        this.botaoConfiguracoes = new Button("Configurações");

        this.botaoInicio.getStyleClass().add("menu-button");
        this.botaoMeusEventos.getStyleClass().add("menu-button");
        this.botaoConfiguracoes.getStyleClass().add("menu-button");

        this.botaoInicio.setMaxWidth(Double.MAX_VALUE);
        this.botaoMeusEventos.setMaxWidth(Double.MAX_VALUE);
        this.botaoConfiguracoes.setMaxWidth(Double.MAX_VALUE);

        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        barraLateral.getChildren().addAll(this.botaoInicio, this.botaoMeusEventos, espacador, this.botaoConfiguracoes);
        return barraLateral;
    }
    // cria a classe do container da topbar
    private HBox criarBarraSuperior() {
        HBox barraSuperior = new HBox();
        barraSuperior.setPadding(new Insets(20));
        barraSuperior.setAlignment(Pos.CENTER);
        barraSuperior.getStyleClass().add("topbar");

        Label logo = new Label("Eventually");
        logo.getStyleClass().add("logo");
        barraSuperior.getChildren().add(logo);
        return barraSuperior;
    }
    // cria a classe do container dos botões do cabeçalho
    private HBox criarControlesSubCabecalho() {
        HBox subCabecalho = new HBox(15);
        subCabecalho.setPadding(new Insets(10, 20, 10, 20));
        subCabecalho.setAlignment(Pos.CENTER_LEFT);
        subCabecalho.getStyleClass().add("sub-header-controls");

        this.programBtn = new Button("Programação");
        this.agendaBtn = new Button("Minha agenda");
        this.newEventBtn = new Button("+ Novo Evento");

        this.userNameLabel = new Label("Usuário");
        this.userNameLabel.getStyleClass().add("user-display-label");

        this.avatar = new Circle(18);
        this.avatar.getStyleClass().add("avatar-circle");
        this.avatar.setFill(Color.LIGHTGRAY);

        HBox userDisplayBox = new HBox(8, this.userNameLabel, this.avatar);
        userDisplayBox.setAlignment(Pos.CENTER);
        userDisplayBox.getStyleClass().add("user-display-box");

        this.programBtn.getStyleClass().add("top-button");
        this.agendaBtn.getStyleClass().add("top-button");
        this.newEventBtn.getStyleClass().add("new-event-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        subCabecalho.getChildren().addAll(this.programBtn, this.agendaBtn, this.newEventBtn, spacer, userDisplayBox);

        return subCabecalho;
    }
    // cria a classe do container das datas
    private HBox createDatePicker() {
        HBox datePickerContainer = new HBox();
        datePickerContainer.setAlignment(Pos.CENTER);
        datePickerContainer.setPadding(new Insets(10, 20, 10, 20));

        HBox datePicker = new HBox(10);
        datePicker.setAlignment(Pos.CENTER);
        datePicker.getStyleClass().add("date-picker-bar");
        datePicker.setPadding(new Insets(5, 15, 5, 15));

        this.dateGroup = new ToggleGroup();
        this.dateButtons.clear();

        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
        Locale localeBR = new Locale("pt", "BR");

        for (int i = 0; i < 7; i++) {
            LocalDate date = monday.plusDays(i);
            String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, localeBR);
            String dayNumber = date.format(dayFormatter);

            ToggleButton btn = new ToggleButton(dayOfWeek + "\n" + dayNumber);
            btn.getStyleClass().add("date-button");
            btn.setToggleGroup(this.dateGroup);
            btn.setUserData(date);

            if (date.equals(today)) {
                btn.setSelected(true);
                this.selectedDate = date;
            }
            this.dateButtons.add(btn);
            datePicker.getChildren().add(btn);
        }
        datePickerContainer.getChildren().add(datePicker);
        return datePickerContainer;
    }
    // cria a classe do container dos eventos
    private VBox createEventList() {
        this.eventList = new VBox(15);
        this.eventList.setPadding(new Insets(20));
        this.eventList.getStyleClass().add("event-list-container");

        loadEventsForDate(selectedDate);
        return this.eventList;
    }
    // cria a classe do container da lista de eventos
    private void loadEventsForDate(LocalDate date) {
        this.eventList.getChildren().clear();

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE dd,", new Locale("pt", "BR")).withLocale(new Locale("pt", "BR"));
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM yy", new Locale("pt", "BR")).withLocale(new Locale("pt", "BR"));

        String dayStr = date.format(dayFormatter).toUpperCase();
        String monthYearStr = date.format(monthYearFormatter).toUpperCase();

        Label placeholder = new Label("Eventos para " + dayStr + " " + monthYearStr + " seriam carregados aqui.");
        this.eventList.getChildren().add(placeholder);
    }
    // cria a classe do container central
    private VBox createCenterContent() {
        HBox controlesSubCabecalho = criarControlesSubCabecalho();
        HBox datePicker = createDatePicker();
        createEventList();

        VBox centerContent = new VBox(0);
        centerContent.getStyleClass().add("center-content-area");
        centerContent.getChildren().addAll(controlesSubCabecalho, datePicker, this.eventList);
        VBox.setVgrow(this.eventList, Priority.ALWAYS);

        return centerContent;
    }
    // cria a classe para o botão de criar novo evento
    private void setupEventHandlers() {
        this.dateGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                LocalDate date = (LocalDate) newToggle.getUserData();
                this.selectedDate = date;
                loadEventsForDate(date);
            }
        });

        if (this.newEventBtn != null) {
            this.newEventBtn.setOnAction(event -> {
                openNewEventModal();
            });
        }
    }

    private void openNewEventModal() {
        System.out.println("Modal de novo evento seria aberto aqui.");
    }

    public Button getHomeButton() {
        return botaoInicio;
    }

    public Button getMyEventsButton() {
        return botaoMeusEventos;
    }

    public Button getSettingsButton() {
        return botaoConfiguracoes;
    }

    public Button getProgramButton() {
        return programBtn;
    }

    public Button getAgendaButton() {
        return agendaBtn;
    }

    public Button getNewEventButton() {
        return newEventBtn;
    }

    public Label getUserNameLabel() {
        return userNameLabel;
    }

    public Circle getAvatar() {
        return avatar;
    }

    public ToggleGroup getDateGroup() {
        return dateGroup;
    }

    public List<ToggleButton> getDateButtons() {
        return dateButtons;
    }

    public VBox getEventList() {
        return eventList;
    }
}