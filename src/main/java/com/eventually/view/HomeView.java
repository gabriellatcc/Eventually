package com.eventually.view;

import com.eventually.controller.HomeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Classe da tela inicial do sistema.
 * Esta classe é responsável por exibir a página principal
 * com filtros de eventoHS e listagem de evento disponíveis.
 * @author Yuri Garcia Maia (Estrutura base)
 * @version 1.10
 * @since 2025-06-22
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-05-29
 */
public class HomeView extends BorderPane {
    private HomeController homeController;

    private BarraBuilder barraBuilder;

    private Button btnCriarEvento, btnFiltros;

    private Label lbNomeUsuario, lbEmailUsuario;
    private ImageView avatarView;

    private Label lbSaudacao;

    private ScrollPane scrollEventos;
    private GridPane gridEventos;

    private FlowPane flowPaneTags;

    public record EventoH(
            int id,
            String titulo,
            String local,
            String dataHoraInicio,
            String dataHoraFim,
            String categoria,
            Image imagem,
            String descricao,
            int inscritos,
            int capacidade,
            String formato,
            Set<String> preferencias,
            String linkAcesso,
            LocalDate dataI,
            LocalDate dataF,
            String horaI,
            String horaF
    ) {}

    /**
     * Construtor da classe {@code HomeView}.
     * Inicializa e organiza os elementos visuais da tela inicial chamando métodos
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}, posicionando a barra lateral à esquerda,
     * a barra superior no topo e o conteúdo principal no centro.
     */
    public HomeView() {
        this.barraBuilder = new BarraBuilder();
        VBox barraLateral = this.barraBuilder.construirBarraLateral();
        HBox barraSuperior = this.barraBuilder.construirBarraSuperior();

        VBox conteudoCentral = criarContainerCentral();

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    public void setHomeController(HomeController homeController) {this.homeController = homeController;}

    /**
     * Este método constrói o cabeçalho da área principal da interface,
     * contendo saudação ao usuário, informações do usuário logado e botão de criar evento.
     * @return um componente HBox do JavaFX
     */
    private HBox criarCabecalhoPrincipal() {
        HBox cabecalhoPrincipal = new HBox();
        cabecalhoPrincipal.setPadding(new Insets(20, 40, 10, 40));
        cabecalhoPrincipal.setAlignment(Pos.CENTER_LEFT);

        HBox saudacaoBox = new HBox(8);
        saudacaoBox.setAlignment(Pos.BASELINE_LEFT);

        lbNomeUsuario = new Label();
        lbNomeUsuario.getStyleClass().add("greeting-label");

        lbSaudacao = new Label("Bem-vindo,");
        lbSaudacao.getStyleClass().add("greeting-label");

        saudacaoBox.getChildren().addAll(lbSaudacao, lbNomeUsuario);

        lbEmailUsuario = new Label();
        lbEmailUsuario.getStyleClass().add("user-display-label");

        Image avatarImage = new Image(getClass().getResourceAsStream("/images/icone-padrao-usuario.png"));
        avatarView = new ImageView(avatarImage);
        avatarView.setFitWidth(40);
        avatarView.setFitHeight(40);
        avatarView.setPreserveRatio(true);
        Circle clip = new Circle(20, 20, 20);
        avatarView.setClip(clip);

        HBox userDisplayBox = new HBox(10, lbEmailUsuario, avatarView);
        userDisplayBox.setAlignment(Pos.CENTER);
        userDisplayBox.getStyleClass().add("user-display-box");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        cabecalhoPrincipal.getChildren().addAll(saudacaoBox, spacer, userDisplayBox);
        return cabecalhoPrincipal;
    }

    /**
     * Este método cria a área de filtros e controles para busca de eventoHS,
     * incluindo botão de filtros, texto explicativo e botão de criar evento.
     * @return um componente HBox do JavaFX
     */
    private HBox criarAreaFiltros() {
        HBox areaFiltros = new HBox(15);
        areaFiltros.setPadding(new Insets(10, 40, 20, 40));
        areaFiltros.setAlignment(Pos.CENTER_LEFT);
        areaFiltros.getStyleClass().add("filters-area");

        btnFiltros = new Button("🔍 Filtros");
        btnFiltros.getStyleClass().add("filters-button");

        flowPaneTags = new FlowPane(5, 10);
        flowPaneTags.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnCriarEvento = new Button("+ Criar evento");
        btnCriarEvento.getStyleClass().add("create-event-button");

        areaFiltros.getChildren().addAll(
                btnFiltros,
                flowPaneTags,
                spacer,
                btnCriarEvento
        );
        return areaFiltros;
    }

    /**
     * Este método cria o grid de eventoHS que será exibido na área principal.
     * Os eventoHS são organizados em uma grade de 4 colunas.
     * @return um componente ScrollPane contendo o grid de eventoHS
     */
    private ScrollPane criarGridEventos() {
        gridEventos = new GridPane();
        gridEventos.setPadding(new Insets(0, 10, 20, 10));
        gridEventos.setHgap(30);
        gridEventos.setVgap(25);
        gridEventos.getStyleClass().add("events-grid");

        gridEventos.getColumnConstraints().clear();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMaxWidth(400);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMaxWidth(400);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setMaxWidth(400);

        gridEventos.getColumnConstraints().addAll(col1, col2, col3);

        scrollEventos = new ScrollPane(gridEventos);
        scrollEventos.setFitToWidth(true);
        scrollEventos.getStyleClass().add("events-scroll-pane");
        scrollEventos.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollEventos.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return scrollEventos;
    }

    /**
     * Limpa o grid e exibe os eventoHS fornecidos.
     * Este método é chamado pelo HomeController para popular a interface.
     * @param eventoHS A lista de eventoHS a serem exibidos.
     */
    public void setEventos(List<EventoH> eventoHS) {
        gridEventos.getChildren().clear();

        if (eventoHS == null || eventoHS.isEmpty()) {
            Label placeholder = new Label("Nenhum evento disponível no momento.");
            placeholder.getStyleClass().add("placeholder-label");
            StackPane placeholderPane = new StackPane(placeholder);
            placeholderPane.setAlignment(Pos.CENTER);
            gridEventos.add(placeholderPane, 0, 0, 3, 1);
            return;
        }

        for (int i = 0; i < eventoHS.size(); i++) {
            EventoH eventoH = eventoHS.get(i);

            EventoHCartao cardEvento = new EventoHCartao();
            cardEvento.getStylesheets().add(getClass().getResource("/styles/event-h-card.css").toExternalForm());

            cardEvento.setLblTitulo(eventoH.titulo());
            cardEvento.setLblLocal(eventoH.local());

            cardEvento.setLblDataHoraInicio(eventoH.dataHoraInicio());
            cardEvento.setLblDataHoraFim(eventoH.dataHoraFim());

            cardEvento.setLblTipo(eventoH.categoria());

            cardEvento.setImagem(eventoH.imagem());

            cardEvento.setCursor(Cursor.HAND);

            HomeView.EventoH eventocartaodavez= eventoHS.get(i);
            cardEvento.setOnMouseClicked(event -> {
                homeController.abrir(eventocartaodavez);
            });

            int row = i / 3;
            int col = i % 3;
            gridEventos.add(cardEvento, col, row);
        }
    }

    /**
     * O método monta a parte central da interface com um container, organizando o cabeçalho principal,
     * a área de filtros e o grid de eventoHS em um layout vertical flexível e estilizado.
     * @return um VBox contendo todo o conteúdo central
     */
    private VBox criarContainerCentral() {
        HBox cabecalhoPrincipal = criarCabecalhoPrincipal();
        HBox areaFiltros = criarAreaFiltros();
        ScrollPane gridEventosPane = criarGridEventos();

        VBox centerContent = new VBox(0);
        centerContent.getStyleClass().add("center-content-area");
        centerContent.getChildren().addAll(cabecalhoPrincipal, areaFiltros, gridEventosPane);

        VBox.setVgrow(gridEventosPane, Priority.ALWAYS);

        return centerContent;
    }

    /**
     * Métodos de encapsulamento getters
     */
    public BarraBuilder getBarraBuilder() {return barraBuilder;}

    public FlowPane getFlowPaneTags() {return flowPaneTags;}

    public Label getLbNomeUsuario() {return lbNomeUsuario;}
    public Label getLbEmailUsuario() {return lbEmailUsuario; }

    public Button getBtnCriarEvento() { return btnCriarEvento; }
    public Button getBtnFiltros() { return btnFiltros; }

    public void setAvatarImagem(Image avatarImagem) {if(this.avatarView != null && avatarImagem != null) {this.avatarView.setImage(avatarImagem);}}
}