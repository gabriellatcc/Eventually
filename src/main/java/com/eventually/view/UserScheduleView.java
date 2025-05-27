package com.eventually.view;

import com.eventually.controller.UserScheduleController;
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

/**
 * Esta classe representa a visualização da tela de programação de eventos do usuário
 * @author Yuri Garcia Maia
 * @version 1.04
 * @author Gabriela Tavares Costa Corrêa (Documentação e revisão da classe)
 * @since 2025-04-06
 */
public class UserScheduleView extends BorderPane {

    private Button btnInicio;
    private Button btnMeusEventos;
    private Button btnConfiguracoes;
    private Button btnProgramacao;
    private Button btnNovoEvento;
    private Button btnSair;

    private Circle avatar;
    private ToggleGroup grupoDatas;
    private List<ToggleButton> btnsData;
    private VBox listaEventos;

    private Label lbNomeUsuario;
    private Stage primaryStage;
    private LocalDate dataSelecionada;

    private UserScheduleController userScheduleController;

    /**
     *Construtor da classe {@code UserScheduleView}.
     * Inicializa e organiza os elementos visuais da tela principal chamando métodos
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}, posicionando a barra lateral à esquerda,
     * a barra superior no topo e o conteúdo principal no centro.
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}.
     */
    public UserScheduleView() {
        this.btnsData = new ArrayList<>();
        this.getStyleClass().add("user-schedule-view");
        this.dataSelecionada = LocalDate.now();

        VBox barraLateral = criarBarraLateral();
        HBox barraSuperior = criarBarraSuperior();
        VBox conteudoCentral = criarContainerCentral();

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    public void setUserScheduleController(UserScheduleController userScheduleController) {this.userScheduleController = userScheduleController;}

    /**
     * Este método criarBarraLateral() cria uma barra lateral de navegação vertical da interface.
     * Essa barra aparece na lateral esquerda da tela e contém botões para acessar
     * diferentes seções do aplicativo:
     * Página Inicial, Meus eventos e Configurações
     * @return a barra lateral.
     */
    private VBox criarBarraLateral() {
        VBox barraLateral = new VBox(20);
        barraLateral.setPadding(new Insets(20));
        barraLateral.getStyleClass().add("sidebar");
        barraLateral.setPrefWidth(200);
        barraLateral.setAlignment(Pos.TOP_CENTER);

        this.btnInicio = new Button("Página inicial");
        this.btnInicio.getStyleClass().add("menu-button");
        this.btnInicio.setMaxWidth(Double.MAX_VALUE);

        this.btnMeusEventos = new Button("Meus eventos");
        this.btnMeusEventos.getStyleClass().add("menu-button");
        this.btnMeusEventos.setMaxWidth(Double.MAX_VALUE);

        this.btnConfiguracoes = new Button("Configurações");
        this.btnConfiguracoes.getStyleClass().add("menu-button");
        this.btnConfiguracoes.setMaxWidth(Double.MAX_VALUE);

        this.btnSair = new Button("Sair");
        this.btnSair.getStyleClass().add("menu-button");
        this.btnSair.setMaxWidth(Double.MAX_VALUE);

        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        barraLateral.getChildren().addAll(this.btnInicio, this.btnMeusEventos, espacador, this.btnConfiguracoes,this.btnSair);
        return barraLateral;
    }

    /**
     * Este método cria a classe do container da barra superior com o nome
     * do programa.
     * @return a barra superior
     */
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

    /**
     * Este método constrói um subcabeçalho da interface gráfica, contendo botões de
     * navegação e informações do usuário logado.
     * @return um componente HBox do JavaFX
     */
    private HBox criarControlesSubCabecalho() {
        HBox subCabecalho = new HBox(15);
        subCabecalho.setPadding(new Insets(10, 20, 10, 20));
        subCabecalho.setAlignment(Pos.CENTER_LEFT);
        subCabecalho.getStyleClass().add("sub-header-controls");

        this.btnProgramacao = new Button("Programação");
        this.btnProgramacao.getStyleClass().add("top-button");

        this.btnNovoEvento = new Button("+ Novo Evento");
        this.btnNovoEvento.getStyleClass().add("new-event-button");

        this.lbNomeUsuario = new Label("Usuário");
        this.lbNomeUsuario.getStyleClass().add("user-display-label");

        this.avatar = new Circle(18);
        this.avatar.getStyleClass().add("avatar-circle");
        this.avatar.setFill(Color.LIGHTGRAY);

        HBox userDisplayBox = new HBox(8, this.lbNomeUsuario, this.avatar);
        userDisplayBox.setAlignment(Pos.CENTER);
        userDisplayBox.getStyleClass().add("user-display-box");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        subCabecalho.getChildren().addAll(this.btnProgramacao, this.btnNovoEvento, spacer, userDisplayBox);

        return subCabecalho;
    }

    /**
     * Este método  constrói um seletor de data da semana atual,
     * permitindo ao usuário escolher um dos dias da semana (de
     * segunda a domingo) por meio de botões alternáveis (ToggleButtons).
     * @return um componente visual do JavaFX (HBox)
     */
    private HBox criarSeletorDeData() {
        HBox datePickerContainer = new HBox();
        datePickerContainer.setAlignment(Pos.CENTER);
        datePickerContainer.setPadding(new Insets(10, 20, 10, 20));

        HBox datePicker = new HBox(10);
        datePicker.setAlignment(Pos.CENTER);
        datePicker.getStyleClass().add("date-picker-bar");
        datePicker.setPadding(new Insets(5, 15, 5, 15));

        this.grupoDatas = new ToggleGroup();
        this.btnsData.clear();

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
            btn.setToggleGroup(this.grupoDatas);
            btn.setUserData(date);

            if (date.equals(today)) {
                btn.setSelected(true);
                this.dataSelecionada = date;
            }
            this.btnsData.add(btn);
            datePicker.getChildren().add(btn);
        }
        datePickerContainer.getChildren().add(datePicker);
        return datePickerContainer;
    }

    /** Este método cria a classe do container que representa a lista de eventos
     * da interface gráfica. Essa lista será preenchida com eventos correspondentes
     * à data atualmente selecionada.
     * @return a lista de eventos a serem exibidos no dia selecionado
     */
    private VBox criarListaEventos() {
        this.listaEventos = new VBox(15);
        this.listaEventos.setPadding(new Insets(20));
        this.listaEventos.getStyleClass().add("event-list-container");

        carregarEventosPorData(dataSelecionada);
        return this.listaEventos;
    }

    /** Esta classe cria um container que exibe a lista de eventos para o dia do mês e ano específicos, ao selecionar
     * outros dias da semana, é limpa a mensagem e exibida outra para o dia especifico.
     *
      * @param date é a data do dia de hoje no padrão java e dentro da classe é refatorado para ser
     *  exibida em formato EEE dd MMM yy
     */
    private void carregarEventosPorData(LocalDate date) {
        this.listaEventos.getChildren().clear();

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE dd,", new Locale("pt", "BR")).withLocale(new Locale("pt", "BR"));
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM yy", new Locale("pt", "BR")).withLocale(new Locale("pt", "BR"));

        String dayStr = date.format(dayFormatter).toUpperCase();
        String monthYearStr = date.format(monthYearFormatter).toUpperCase();

        Label placeholder = new Label("Eventos para " + dayStr + " " + monthYearStr + ".");
        this.listaEventos.getChildren().add(placeholder);
    }

    /**
     * O método monta a parte central da interface com um container, organizando o cabeçalho de controle,
     * o seletor de datas e a lista de eventos em um layout vertical flexível e estilizado.
     * @return
     */
    private VBox criarContainerCentral() {
        HBox controlesSubCabecalho = criarControlesSubCabecalho();
        HBox datePicker = criarSeletorDeData();
        criarListaEventos();

        VBox centerContent = new VBox(0);
        centerContent.getStyleClass().add("center-content-area");
        centerContent.getChildren().addAll(controlesSubCabecalho, datePicker, this.listaEventos);
        VBox.setVgrow(this.listaEventos, Priority.ALWAYS);

        return centerContent;
    }

    public Button getBtnInicio() {return btnInicio;}
    public Button getBtnMeusEventos() {return btnMeusEventos;}
    public Button getBtnConfiguracoes() {return btnConfiguracoes;}
    public Button getBtnProgramacao() {return btnProgramacao;}
    public Button getBtnNovoEvento() {return btnNovoEvento;}
    public Button getBtnSair() {return btnSair;}
    public Label getLbNomeUsuario() {return lbNomeUsuario;}
    public Circle getAvatar() {return avatar;}
    public ToggleGroup getGrupoDatas() {return grupoDatas;}
    public List<ToggleButton> getBtnsData() {return btnsData;}
    public VBox getListaEventos() {return listaEventos;}
}