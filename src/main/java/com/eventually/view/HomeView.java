package com.eventually.view;

import com.eventually.controller.HomeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Classe da tela inicial do sistema.
 * Esta classe é responsável por exibir a página principal
 * com filtros de eventos e listagem de eventos disponíveis.
 *
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-05-23
 * @author Gabriella Tavares Costa Corrêa (Documentação e revisão da classe)
 * @since 2025-05-23
 */
public class HomeView extends BorderPane {

    private Button btnInicio;
    private Button btnMeusEventos;
    private Button btnConfiguracoes;
    private Button btnProgramacao;
    private Button btnSair;
    private Button btnCriarEvento;
    private Button btnFiltros;

    private Circle avatar;
    private Label lbNomeUsuario;
    private Label lbBoaTarde;
    private Label lbEncontrarEventos;
    private Label lbEventosPagina;

    private ScrollPane scrollEventos;
    private GridPane gridEventos;
    private VBox listaEventos;

    private Stage primaryStage;
    private HomeController homeController;

    /**
     * Construtor da classe {@code HomeView}.
     * Inicializa e organiza os elementos visuais da tela inicial chamando métodos
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}, posicionando a barra lateral à esquerda,
     * a barra superior no topo e o conteúdo principal no centro.
     */
    public HomeView() {
        this.getStyleClass().add("home-view");

        VBox barraLateral = criarBarraLateral();
        HBox barraSuperior = criarBarraSuperior();
        VBox conteudoCentral = criarContainerCentral();

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

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

        this.btnProgramacao= new Button("Programação");
        this.btnProgramacao.getStyleClass().add("menu-button");
        this.btnProgramacao.setMaxWidth(Double.MAX_VALUE);

        this.btnConfiguracoes = new Button("Configurações");
        this.btnConfiguracoes.getStyleClass().add("menu-button");
        this.btnConfiguracoes.setMaxWidth(Double.MAX_VALUE);

        this.btnSair = new Button("Sair");
        this.btnSair.getStyleClass().add("menu-button");
        this.btnSair.setMaxWidth(Double.MAX_VALUE);

        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        barraLateral.getChildren().addAll(this.btnInicio, this.btnMeusEventos, this.btnProgramacao, espacador, this.btnConfiguracoes,this.btnSair);
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
     * Este método constrói o cabeçalho da área principal da interface,
     * contendo saudação ao usuário, informações do usuário logado e botão de criar evento.
     * @return um componente HBox do JavaFX
     */
    private HBox criarCabecalhoPrincipal() {
        HBox cabecalhoPrincipal = new HBox();
        cabecalhoPrincipal.setPadding(new Insets(20, 20, 10, 20));
        cabecalhoPrincipal.setAlignment(Pos.CENTER_LEFT);
        cabecalhoPrincipal.getStyleClass().add("main-header");

        VBox saudacaoBox = new VBox(5);
        saudacaoBox.setAlignment(Pos.CENTER_LEFT);

        this.lbBoaTarde = new Label("Boa tarde, usuário");
        this.lbBoaTarde.getStyleClass().add("greeting-label");
        this.lbBoaTarde.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        saudacaoBox.getChildren().add(this.lbBoaTarde);

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

        cabecalhoPrincipal.getChildren().addAll(saudacaoBox, spacer, userDisplayBox);
        return cabecalhoPrincipal;
    }

    /**
     * Este método cria a área de filtros e controles para busca de eventos,
     * incluindo botão de filtros, texto explicativo e botão de criar evento.
     * @return um componente HBox do JavaFX
     */
    private HBox criarAreaFiltros() {
        HBox areaFiltros = new HBox(15);
        areaFiltros.setPadding(new Insets(10, 20, 20, 20));
        areaFiltros.setAlignment(Pos.CENTER_LEFT);
        areaFiltros.getStyleClass().add("filters-area");

        this.btnFiltros = new Button("🔍 Filtros");
        this.btnFiltros.getStyleClass().add("filters-button");

        this.lbEncontrarEventos = new Label("Encontrar eventos por filtro");
        this.lbEncontrarEventos.getStyleClass().add("filter-description-label");

        this.lbEventosPagina = new Label("Eventos / Página inicial");
        this.lbEventosPagina.getStyleClass().add("breadcrumb-label");

        this.btnCriarEvento = new Button("Criar evento");
        this.btnCriarEvento.getStyleClass().add("create-event-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        areaFiltros.getChildren().addAll(
                this.btnFiltros,
                this.lbEncontrarEventos,
                spacer,
                this.btnCriarEvento
        );
        return areaFiltros;
    }

    /**
     * Este método cria o grid de eventos que será exibido na área principal.
     * Os eventos são organizados em uma grade de 2 colunas.
     * @return um componente ScrollPane contendo o grid de eventos
     */
    private ScrollPane criarGridEventos() {
        this.gridEventos = new GridPane();
        this.gridEventos.setPadding(new Insets(0, 20, 20, 20));
        this.gridEventos.setHgap(20);
        this.gridEventos.setVgap(20);
        this.gridEventos.getStyleClass().add("events-grid");

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col2.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);
        col2.setHgrow(Priority.ALWAYS);
        this.gridEventos.getColumnConstraints().addAll(col1, col2);

        carregarEventos();

        this.scrollEventos = new ScrollPane(this.gridEventos);
        this.scrollEventos.setFitToWidth(true);
        this.scrollEventos.getStyleClass().add("events-scroll-pane");
        this.scrollEventos.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollEventos.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return this.scrollEventos;
    }

    /**
     * Este método carrega os eventos de exemplo no grid.
     * Na implementação real, estes dados viriam do controller/service.
     */
    private void carregarEventos() {
        this.gridEventos.getChildren().clear();

        String[][] eventosExemplo = {
                {"Título do evento", "Local do evento", "SEX 14, MAR 2025 - 18:30", "Educacional"},
                {"Título do evento", "Local do evento", "SEX 14, MAR 2025 - 18:30", "Religioso"},
                {"Título do evento", "Local do evento", "SEX 14, MAR 2025 - 18:30", "Corporativo"},
                {"Título do evento", "Local do evento", "SEX 14, MAR 2025 - 18:30", "Social"}
        };

        for (int i = 0; i < eventosExemplo.length; i++) {
            VBox cardEvento = criarCardEvento(
                    eventosExemplo[i][0],
                    eventosExemplo[i][1],
                    eventosExemplo[i][2],
                    eventosExemplo[i][3]
            );

            int row = i / 2;
            int col = i % 2;
            this.gridEventos.add(cardEvento, col, row);
        }
    }

    /**
     * Este método cria um card individual para um evento.
     * @param titulo o título do evento
     * @param local o local do evento
     * @param dataHora a data e hora do evento
     * @param categoria a categoria do evento
     * @return um VBox representando o card do evento
     */
    private VBox criarCardEvento(String titulo, String local, String dataHora, String categoria) {
        VBox cardEvento = new VBox(10);
        cardEvento.setPadding(new Insets(15));
        cardEvento.getStyleClass().add("event-card");
        cardEvento.setPrefHeight(200);

        Region areaConteudo = new Region();
        areaConteudo.setPrefHeight(80);
        areaConteudo.getStyleClass().add("event-card-content");

        Label lbDataHora = new Label(dataHora);
        lbDataHora.getStyleClass().add("event-date-time");

        Label lbTitulo = new Label(titulo);
        lbTitulo.getStyleClass().add("event-title");
        lbTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label lbLocal = new Label(local);
        lbLocal.getStyleClass().add("event-location");

        HBox areaInferior = new HBox();
        areaInferior.setAlignment(Pos.CENTER_RIGHT);

        Label lbCategoria = new Label(categoria);
        lbCategoria.getStyleClass().add("event-category");
        lbCategoria.getStyleClass().add("category-" + categoria.toLowerCase());

        areaInferior.getChildren().add(lbCategoria);

        VBox.setVgrow(areaConteudo, Priority.ALWAYS);

        cardEvento.getChildren().addAll(
                areaConteudo,
                lbDataHora,
                lbTitulo,
                lbLocal,
                areaInferior
        );

        return cardEvento;
    }

    /**
     * O método monta a parte central da interface com um container, organizando o cabeçalho principal,
     * a área de filtros e o grid de eventos em um layout vertical flexível e estilizado.
     * @return um VBox contendo todo o conteúdo central
     */
    private VBox criarContainerCentral() {
        HBox cabecalhoPrincipal = criarCabecalhoPrincipal();
        HBox areaFiltros = criarAreaFiltros();
        ScrollPane gridEventos = criarGridEventos();

        VBox centerContent = new VBox(0);
        centerContent.getStyleClass().add("center-content-area");
        centerContent.getChildren().addAll(cabecalhoPrincipal, areaFiltros, gridEventos);
        VBox.setVgrow(gridEventos, Priority.ALWAYS);

        return centerContent;
    }

    /**
     * Métodos de encapsulamento getters
     */
    public Button getBtnInicio() { return btnInicio; }
    public Button getBtnMeusEventos() { return btnMeusEventos; }
    public Button getBtnConfiguracoes() { return btnConfiguracoes; }
    public Button getBtnProgramacao() { return btnProgramacao; }
    public Button getBtnSair() { return btnSair; }
    public Button getBtnCriarEvento() { return btnCriarEvento; }
    public Button getBtnFiltros() { return btnFiltros; }
    public Circle getAvatar() { return avatar; }
    public Label getLbNomeUsuario() { return lbNomeUsuario; }
    public Label getLbBoaTarde() { return lbBoaTarde; }
    public Label getLbEncontrarEventos() { return lbEncontrarEventos; }
    public Label getLbEventosPagina() { return lbEventosPagina; }
    public ScrollPane getScrollEventos() { return scrollEventos; }
    public GridPane getGridEventos() { return gridEventos; }
    public VBox getListaEventos() { return listaEventos; }
}