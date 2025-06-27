package com.eventually.view;

import com.eventually.controller.UserScheduleController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Esta classe representa a visualização da tela de programação de eventos do usuário
 * @author Yuri Garcia Maia
 * @version 1.06
 * @author Gabriela Tavares Costa Corrêa (Documentação e revisão da classe)
 * @since 2025-04-06
 */
public class UserScheduleView extends BorderPane {
    private UserScheduleController userScheduleController;

    private BarraBuilder barraBuilder;

    private Button btnNovoEvento;

    private Label lbNomeUsuario;
    private ImageView avatarView;
    private Label lbEmailUsuario;

    private ToggleGroup grupoDatas;
    private VBox listaEventos;

    private LocalDate dataSelecionada;

    private HBox seletorDataContainer;

    /**
     *Construtor da classe {@code UserScheduleView}.
     */
    public UserScheduleView() {
        setupUIUserSchedule();
    }

    public void setUserScheduleController(UserScheduleController userScheduleController) {this.userScheduleController = userScheduleController;}

    /**
     * Inicializa e organiza os elementos visuais da tela principal chamando métodos
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}, posicionando a barra lateral à esquerda,
     * a barra superior no topo e o conteúdo principal no centro.
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}.
     */
    public void setupUIUserSchedule() {
        dataSelecionada = LocalDate.now();
        this.grupoDatas = new ToggleGroup();

        this.barraBuilder = new BarraBuilder();
        VBox barraLateral = this.barraBuilder.construirBarraLateral();
        HBox barraSuperior = this.barraBuilder.construirBarraSuperior();

        VBox conteudoCentral = criarContainerCentral();

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    /**
     * Este método constrói um subcabeçalho da interface gráfica, contendo botões de
     * navegação e informações do usuário logado.
     * @return um componente HBox do JavaFX
     */
    private HBox criarControlesSubCabecalho() {
        HBox subCabecalho = new HBox(15);
        subCabecalho.setPadding(new Insets(10, 40, 10, 40));
        subCabecalho.setAlignment(Pos.CENTER_LEFT);
        subCabecalho.getStyleClass().add("sub-header-controls");

        btnNovoEvento = new Button("+ Criar novo evento");
        btnNovoEvento.getStyleClass().add("new-event-button-bottom");

        lbNomeUsuario = new Label();
        lbNomeUsuario.getStyleClass().add("user-display-label");

        lbEmailUsuario = new Label();
        lbEmailUsuario.getStyleClass().add("user-email-label");

        VBox userInfoText = new VBox(-2);
        userInfoText.setAlignment(Pos.CENTER_LEFT);
        userInfoText.getChildren().addAll(lbNomeUsuario, lbEmailUsuario);

        Image avatarImage = new Image(getClass().getResourceAsStream("/images/icone-padrao-usuario.png"));
        avatarView = new ImageView(avatarImage);
        avatarView.setFitWidth(40);
        avatarView.setFitHeight(40);
        avatarView.setPreserveRatio(true);
        Circle clip = new Circle(20, 20, 20);
        avatarView.setClip(clip);

        HBox userDisplayBox = new HBox(10, userInfoText, avatarView);
        userDisplayBox.setAlignment(Pos.CENTER);
        userDisplayBox.getStyleClass().add("user-display-box");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        subCabecalho.getChildren().addAll(btnNovoEvento, spacer, userDisplayBox);

        subCabecalho.setBorder(new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return subCabecalho;
    }

    /**
     * Este método  constrói um seletor de data da semana atual,
     * permitindo ao usuário escolher um dos dias da semana (de
     * segunda a domingo) por meio de botões alternáveis (ToggleButtons).
     * @return um componente visual do JavaFX (HBox)
     */
    private HBox criarSeletorDeDatas() {
        seletorDataContainer = new HBox(10);
        seletorDataContainer.setAlignment(Pos.CENTER);
        seletorDataContainer.setPadding(new Insets(10, 40, 10, 40));
        seletorDataContainer.getStyleClass().add("date-picker-bar");
        seletorDataContainer.setBorder(new Border(new BorderStroke(Color.INDIANRED,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return seletorDataContainer;
    }

    /** Este método cria a classe do container que representa a lista de eventos
     * da interface gráfica. Essa lista será preenchida com eventos correspondentes
     * à data atualmente selecionada.
     * @return a lista de eventos a serem exibidos no dia selecionado
     */
    private VBox criarListaEventos() {
        listaEventos = new VBox(15);
        listaEventos.setAlignment(Pos.CENTER);
        listaEventos.getStyleClass().add("event-list-container");
        listaEventos.setPadding(new Insets(20, 40, 20, 40));
        listaEventos.setBorder(new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        carregarEventosPorData(dataSelecionada);
        return listaEventos;
    }

    /** Esta classe cria um container que exibe a lista de eventos para o dia do mês e ano específicos, ao selecionar
     * outros dias da semana, é limpa a mensagem e exibida outra para o dia especifico.
     *
      * @param date é a data do dia de hoje no padrão java e dentro da classe é refatorado para ser
     *  exibida em formato EEE dd MMM yy
     */
    private void carregarEventosPorData(LocalDate date) {
        listaEventos.getChildren().clear();

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE dd,", new Locale("pt", "BR")).withLocale(new Locale("pt", "BR"));
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM yy", new Locale("pt", "BR")).withLocale(new Locale("pt", "BR"));

        String dayStr = date.format(dayFormatter).toUpperCase();
        String monthYearStr = date.format(monthYearFormatter).toUpperCase();

        Label placeholder = new Label("Eventos para " + dayStr + " " + monthYearStr + ".");
        listaEventos.getChildren().add(placeholder);
    }

    /**
     * O método monta a parte central da interface com um container, organizando o cabeçalho de controle,
     * o seletor de datas e a lista de eventos em um layout vertical flexível e estilizado.
     * @return
     */
    private VBox criarContainerCentral() {
        HBox controlesSubCabecalho = criarControlesSubCabecalho();
        HBox seletorDeDatas = criarSeletorDeDatas();
        VBox listaEventosVBox = criarListaEventos();

        StackPane stackPane = new StackPane(listaEventosVBox);
        VBox.setVgrow(stackPane, Priority.ALWAYS);

        VBox centerContent = new VBox(0);
        centerContent.getStyleClass().add("center-content-area");
        centerContent.getChildren().addAll(controlesSubCabecalho, seletorDeDatas, stackPane);

        return centerContent;
    }

    /**
     * Métodos de encapsulamento getters e setters
     */
    public BarraBuilder getBarraBuilder() { return barraBuilder; }
    public Button getBtnNovoEvento() { return btnNovoEvento; }
    public Label getLbNomeUsuario() { return lbNomeUsuario; }
    public Label getLbEmailUsuario() { return lbEmailUsuario; }
    public void setAvatarImagem(Image avatarImagem) {
        if (this.avatarView != null && avatarImagem != null) {
            this.avatarView.setImage(avatarImagem);
        }
    }
    public ToggleGroup getGrupoDatas() { return grupoDatas; }
    public HBox getSeletorDataContainer() { return seletorDataContainer; }
    public VBox getListaEventos() { return listaEventos; }
    public void setNomeUsuario(String nome) { if (nome != null) this.lbNomeUsuario.setText(nome); }
    public void setEmailUsuario(String email) { if (email != null) this.lbEmailUsuario.setText(email); }
}