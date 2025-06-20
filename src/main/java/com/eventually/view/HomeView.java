package com.eventually.view;

import com.eventually.controller.HomeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * PASSÍVEL DE ALTERAÇÕES
 * Classe da tela inicial do sistema.
 * Esta classe é responsável por exibir a página principal
 * com filtros de eventos e listagem de eventos disponíveis.
 * @author Yuri Garcia Maia (Estrutura base)
 * @version 1.01
 * @since 2025-05-23
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-05-29
 */
public class HomeView extends BorderPane {
    private Button btnInicio;
    private Button btnMeusEventos;
    private Button btnConfiguracoes;
    private Button btnProgramacao;
    private Button btnSair;
    private Button btnCriarEvento;
    private Button btnFiltros;

    private Label lbNomeUsuario;
    private Image avatarImagem;
    private Label lbEmailUsuario;
    private Label lbSaudacao;
    private Label lbEncontrarEventos;

    private ScrollPane scrollEventos;
    private GridPane gridEventos;
    private VBox listaEventos;

    private HomeController homeController;

    /**
     * Construtor da classe {@code HomeView}.
     * Inicializa e organiza os elementos visuais da tela inicial chamando métodos
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}, posicionando a barra lateral à esquerda,
     * a barra superior no topo e o conteúdo principal no centro.
     */
    public HomeView() {
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

        lbNomeUsuario = new Label();
        lbNomeUsuario.getStyleClass().add("greeting-label");
        lbNomeUsuario.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        lbSaudacao = new Label("Bem-vindo, ");
        lbSaudacao.getStyleClass().add("greeting-label");
        lbSaudacao.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        HBox saudacaoNomeBox = new HBox(0);
        saudacaoNomeBox.getChildren().addAll(lbSaudacao, lbNomeUsuario);
        saudacaoBox.getChildren().add(saudacaoNomeBox);

        lbEmailUsuario = new Label();
        lbEmailUsuario.getStyleClass().add("user-display-label");

        avatarImagem = new Image(getClass().getResourceAsStream("/images/icone-padrao-usuario.png"));
        ImageView avatar = new ImageView(avatarImagem);
        avatar.setFitWidth(40);
        avatar.setFitHeight(40);
        avatar.setPreserveRatio(true);

        HBox userDisplayBox = new HBox(8, lbEmailUsuario, avatar);
        userDisplayBox.setAlignment(Pos.CENTER);
        userDisplayBox.getStyleClass().add("user-display-box");
        userDisplayBox.setPrefHeight(35);

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

        btnFiltros = new Button("🔍 Filtros");
        btnFiltros.getStyleClass().add("filters-button");
        // FALTA: personalizar aparência botão de filtro
        // FALTA: fazer funcionar

        lbEncontrarEventos = new Label("Encontrar eventos por filtro");
        lbEncontrarEventos.getStyleClass().add("filter-description-label");

        btnCriarEvento = new Button("Criar evento");
        btnCriarEvento.getStyleClass().add("create-event-button");
        //FALTA: personalizar o botao de criar evento

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        areaFiltros.getChildren().addAll(
                btnFiltros,
                lbEncontrarEventos,
                spacer,
                btnCriarEvento
        );
        return areaFiltros;
    }

    /**
     * Este método cria o grid de eventos que será exibido na área principal.
     * Os eventos são organizados em uma grade de 2 colunas.
     * @return um componente ScrollPane contendo o grid de eventos
     */
    private ScrollPane criarGridEventos() {
        gridEventos = new GridPane();
        gridEventos.setPadding(new Insets(0, 20, 20, 20));
        gridEventos.setHgap(20);
        gridEventos.setVgap(20);
        gridEventos.getStyleClass().add("events-grid");

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col2.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);
        col2.setHgrow(Priority.ALWAYS);
        gridEventos.getColumnConstraints().addAll(col1, col2);

        carregarEventos();

        scrollEventos = new ScrollPane(gridEventos);
        scrollEventos.setFitToWidth(true);
        scrollEventos.getStyleClass().add("events-scroll-pane");
        scrollEventos.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollEventos.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return scrollEventos;
    }

    /**
     * Este método carrega os eventos de exemplo no grid.
     * Na implementação real, estes dados viriam do controller/service.
     */
    private void carregarEventos() {
        gridEventos.getChildren().clear();

        //FALTA: puxar a lista de eventos existntes (getall()) do EventoVisualizacaoServico e criar cartões
        // conforme existem objetos de evento

        String[][] eventosExemplo = {
                {"Título do evento", "Local do evento", "SEX 14, MAR 2025 - 18:30", "Educacional"},
                {"Título do evento", "Local do evento", "SEX 14, MAR 2025 - 18:30", "Religioso"},
                {"Título do evento", "Local do evento", "SEX 14, MAR 2025 - 18:30", "Corporativo"},
                {"Título do evento", "Local do evento", "SEX 14, MAR 2025 - 18:30", "Social"}
        };

        for (int i = 0; i < eventosExemplo.length; i++) {
            //FALTA: mudar proporção da exibição dos cartões de evento do início para 3 colunas ao invés de 2
            //FALTA:  criar/aumentar espaçamento entre a área dos cartôes de evento e cabeçalho,
            //FALTA: criar classe para os CARTÕES de evento exibidos no INÍCIO, dentro do cartão os elementos visuais
            // são instanciados vazios e têm métodos set() e no controlador é acessado servico que puxa os atributos de
            // cada objeto existente
            VBox cardEvento = criarCardEvento(
                    eventosExemplo[i][0],
                    eventosExemplo[i][1],
                    eventosExemplo[i][2],
                    eventosExemplo[i][3]
            );

            int row = i / 2;
            int col = i % 2;
            gridEventos.add(cardEvento, col, row);
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
    public Label getLbNomeUsuario() {return lbNomeUsuario;}
    public Label getLbEmailUsuario() {return lbEmailUsuario; }

    public Button getBtnInicio() { return btnInicio; }
    public Button getBtnMeusEventos() { return btnMeusEventos; }
    public Button getBtnConfiguracoes() { return btnConfiguracoes; }
    public Button getBtnProgramacao() { return btnProgramacao; }
    public Button getBtnSair() { return btnSair; }
    public Button getBtnCriarEvento() { return btnCriarEvento; }
    public Button getBtnFiltros() { return btnFiltros; }

    public void setAvatarImagem(Image avatarImagem) {this.avatarImagem=avatarImagem;}
}