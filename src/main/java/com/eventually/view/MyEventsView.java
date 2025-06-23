package com.eventually.view;

import com.eventually.controller.MyEventsController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Esta classe representa a visualização da tela de programação de eventos do usuário
 * @version 1.05
 * @author Gabriela Tavares Costa Corrêa (Criação, documentação e revisão da classe)
 * @since 2025-04-06
 */
public class MyEventsView extends BorderPane {

    private Button btnInicio;
    private Button btnMeusEventos;
    private Button btnConfiguracoes;
    private Button btnProgramacao;
    private Button btnNovoEvento;
    private Button btnSair;
    private ToggleButton btnEventosCriados;
    private ToggleButton btnEventosFinalizados;

    private ImageView avatarView;
    private ToggleGroup grupoDatas;
    private VBox listaEventos;
    private HBox seletorDataContainer;

    private Label lbNomeUsuario;
    private Label lbEmailUsuario;

    private MyEventsController myEventsController;

    /**
     *Construtor da classe {@code UserScheduleView}.
     */
    public MyEventsView() {
        setupUI();
        try {
            this.getStylesheets().add(getClass().getResource("/styles/my-events-view.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Não foi possível carregar o arquivo CSS: my-events-view.css");
        }
    }

    public void setMyEventsViewController(MyEventsController myEventsController) {this.myEventsController = myEventsController;}

    /**
     * Inicializa e organiza os elementos visuais da tela principal chamando métodos
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}, posicionando a barra lateral à esquerda,
     * a barra superior no topo e o conteúdo principal no centro.
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}.
     */
    private void setupUI() {
        this.grupoDatas = new ToggleGroup();

        VBox barraLateral = criarBarraLateral();
        HBox barraSuperior = criarBarraSuperior();
        VBox conteudoCentral = criarContainerCentral();

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    /**
     * Este método criarBarraLateral() cria uma barra lateral de navegação vertical da interface.
     * Essa barra aparece na lateral esquerda da tela e contém botões para acessar
     * diferentes seções do aplicativo:
     * Página Inicial, Meus eventos Programação, Configurações e saída
     * @return a barra lateral.
     */
    private VBox criarBarraLateral() {
        VBox barraLateral = new VBox(20);
        barraLateral.setPadding(new Insets(20));
        barraLateral.getStyleClass().add("sidebar");
        barraLateral.setPrefWidth(200);
        barraLateral.setAlignment(Pos.TOP_CENTER);

        btnInicio = new Button("Página inicial");
        btnInicio.getStyleClass().add("menu-button");
        btnInicio.setMaxWidth(Double.MAX_VALUE);
        btnInicio.setPadding(new Insets(0,0,15,0));

        btnMeusEventos = new Button("Meus eventos");
        btnMeusEventos.getStyleClass().add("menu-button");
        btnMeusEventos.setMaxWidth(Double.MAX_VALUE);
        btnMeusEventos.setPadding(new Insets(0,0,15,0));
        btnMeusEventos.requestFocus();

        btnProgramacao = new Button("Programação");
        btnProgramacao.getStyleClass().add("menu-button");
        btnProgramacao.setMaxWidth(Double.MAX_VALUE);
        btnProgramacao.setPadding(new Insets(0,0,15,0));

        VBox parteSuperior = new VBox(15, btnInicio, btnMeusEventos, btnProgramacao);
        parteSuperior.setPadding(new Insets(20,15,15,15));

        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        btnConfiguracoes = new Button("Configurações");
        btnConfiguracoes.getStyleClass().add("menu-button");
        btnConfiguracoes.setMaxWidth(Double.MAX_VALUE);
        btnConfiguracoes.setPadding(new Insets(0,0,15,0));

        btnSair = new Button("Sair");
        btnSair.getStyleClass().add("menu-button");
        btnSair.setMaxWidth(Double.MAX_VALUE);
        btnSair.setPadding(new Insets(0,0,15,0));

        VBox parteInferior = new VBox(15, btnConfiguracoes, btnSair);
        parteInferior.setPadding(new Insets(0,15,40,15));

        barraLateral.getChildren().addAll(parteSuperior, espacador, parteInferior);
        barraLateral.setPadding(new Insets(0));
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
        subCabecalho.setPadding(new Insets(10, 40, 10, 40));
        subCabecalho.setAlignment(Pos.CENTER_LEFT);
        subCabecalho.getStyleClass().add("sub-header-controls");

        ToggleGroup groupFiltroEventos = new ToggleGroup();

        btnEventosCriados = new ToggleButton("Eventos criados");
        btnEventosCriados.setToggleGroup(groupFiltroEventos);
        btnEventosCriados.getStyleClass().add("filter-button");
        btnEventosCriados.setSelected(true);

        btnEventosFinalizados = new ToggleButton("Eventos Finalizados");
        btnEventosFinalizados.setToggleGroup(groupFiltroEventos);
        btnEventosFinalizados.getStyleClass().add("filter-button");

        HBox filtroBox = new HBox(10, btnEventosCriados, btnEventosFinalizados);
        filtroBox.setAlignment(Pos.CENTER_LEFT);

        lbNomeUsuario = new Label();
        lbNomeUsuario.getStyleClass().add("user-display-label");

        lbEmailUsuario = new Label();
        lbEmailUsuario.getStyleClass().add("user-email-label");

        VBox userInfoText = new VBox(-2);
        userInfoText.setAlignment(Pos.CENTER_LEFT);
        userInfoText.getChildren().addAll(lbNomeUsuario, lbEmailUsuario);

        try {
            Image avatarImage = new Image(getClass().getResourceAsStream("/images/icone-padrao-usuario.png"));
            avatarView = new ImageView(avatarImage);
        } catch (Exception e) {
            System.err.println("Imagem de avatar padrão não encontrada, usando placeholder.");
            avatarView = new ImageView();
        }
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

        subCabecalho.getChildren().addAll(filtroBox, spacer, userDisplayBox);

        return subCabecalho;
    }

    /** Este método cria a classe do container que representa a lista de eventos
     * da interface gráfica. Essa lista será preenchida com eventos correspondentes
     * à data atualmente selecionada.
     * @return a lista de eventos a serem exibidos no dia selecionado
     */
    private VBox criarListaEventos() {
        listaEventos = new VBox(15);
        listaEventos.setPadding(new Insets(20, 40, 20, 40));
        listaEventos.getStyleClass().add("event-list-container");
        return listaEventos;
    }

    private HBox criarSeletorDeDatas() {
        seletorDataContainer = new HBox(10);
        seletorDataContainer.setPadding(new Insets(10, 40, 10, 40));
        seletorDataContainer.setAlignment(Pos.CENTER);
        seletorDataContainer.getStyleClass().add("date-picker-bar");
        return seletorDataContainer;
    }

    /** Esta classe cria um container que exibe a lista de eventos para o dia do mês e ano específicos, ao selecionar
     * outros dias da semana, é limpa a mensagem e exibida outra para o dia especifico.
     *
     * @param eventosDoDia é a lista de eventos a ser exibida
     */
    public void carregarEventos(List<MyEventsController.Evento> eventosDoDia, EventHandler<ActionEvent> onEditHandler) {
        limparListaDeEventos();
        if (eventosDoDia == null || eventosDoDia.isEmpty()) {
            Label placeholder = new Label("Nenhum evento encontrado.");
            placeholder.getStyleClass().add("placeholder-label");
            listaEventos.getChildren().add(placeholder);
        } else {
            for (MyEventsController.Evento evento : eventosDoDia) {
                adicionarEvento(evento, onEditHandler);
            }
        }
    }

    private void adicionarEvento(MyEventsController.Evento evento, EventHandler<ActionEvent> onEditHandler) {
        HBox cardContainer = new HBox(15);
        cardContainer.setAlignment(Pos.CENTER);

        VBox infoPrincipal = new VBox(5);
        Label titulo = new Label(evento.titulo());
        titulo.getStyleClass().add("event-card-title");
        Label local = new Label(evento.local());
        local.getStyleClass().add("event-card-subtitle");
        infoPrincipal.getChildren().addAll(titulo, local);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox infoData = new VBox(5);
        infoData.setAlignment(Pos.CENTER_RIGHT);

        DateTimeFormatter agendadoFormatter = DateTimeFormatter.ofPattern("'AGENDADO PARA' EEE dd,", new Locale("pt", "BR"));
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("MMM uuuu", new Locale("pt", "BR"));

        Label agendado = new Label(evento.data().format(agendadoFormatter).toUpperCase());
        Label data = new Label(evento.data().format(dataFormatter).toUpperCase());
        Label hora = new Label(evento.hora());

        agendado.getStyleClass().add("event-card-date-text");
        data.getStyleClass().add("event-card-date-text");
        hora.getStyleClass().add("event-card-date-text");
        infoData.getChildren().addAll(agendado, data, hora);

        HBox eventCard = new HBox(15, infoPrincipal, spacer, infoData);
        eventCard.getStyleClass().add("event-card");
        eventCard.setPadding(new Insets(20));
        HBox.setHgrow(eventCard, Priority.ALWAYS);

        Button btnEditarEvento = new Button("Editar Evento");
        btnEditarEvento.getStyleClass().add("edit-event-button");
        btnEditarEvento.setUserData(evento.id());
        btnEditarEvento.setOnAction(onEditHandler);

        cardContainer.getChildren().addAll(eventCard, btnEditarEvento);
        listaEventos.getChildren().add(cardContainer);
    }

    public void limparListaDeEventos() {
        listaEventos.getChildren().clear();
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

        btnNovoEvento = new Button("+ Criar novo evento");
        btnNovoEvento.getStyleClass().add("new-event-button-bottom");

        StackPane stackPane = new StackPane(listaEventosVBox);
        stackPane.getChildren().add(btnNovoEvento);
        StackPane.setAlignment(btnNovoEvento, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(btnNovoEvento, new Insets(0, 40, 20, 0));

        VBox.setVgrow(stackPane, Priority.ALWAYS);

        VBox centerContent = new VBox(0);
        centerContent.getStyleClass().add("center-content-area");
        centerContent.getChildren().addAll(controlesSubCabecalho, seletorDeDatas, stackPane);

        return centerContent;
    }

    public Button getBtnInicio() {return btnInicio;}
    public Button getBtnMeusEventos() {return btnMeusEventos;}
    public Button getBtnConfiguracoes() {return btnConfiguracoes;}
    public Button getBtnProgramacao() {return btnProgramacao;}
    public Button getBtnNovoEvento() {return btnNovoEvento;}
    public Button getBtnSair() {return btnSair;}

    public Label getLbNomeUsuario() {return lbNomeUsuario;}
    public Label getLbEmailUsuario() { return lbEmailUsuario; }

    public HBox getSeletorDataContainer() { return seletorDataContainer; }
    public ToggleGroup getGrupoDatas() {return grupoDatas;}
    public ToggleButton getBtnEventosCriados() {return btnEventosCriados;}
    public ToggleButton getBtnEventosFinalizados() {return btnEventosFinalizados;}

    public void setNomeUsuario(String nome) {
        if (nome != null) {
            this.lbNomeUsuario.setText(nome);
        }
    }

    public void setEmailUsuario(String email) {
        if (email != null) {
            this.lbEmailUsuario.setText(email);
        }
    }

    public void setAvatar(Image avatar) {
        if (avatar != null) {
            this.avatarView.setImage(avatar);
        }
    }
}
