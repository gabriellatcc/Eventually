package com.eventually.view;

import com.eventually.controller.MyEventsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
 * @version 1.06
 * @author Gabriela Tavares Costa Corrêa (Criação, documentação e revisão da classe)
 * @since 2025-04-06
 */
public class MyEventsView extends BorderPane {
    private MyEventsController myEventsController;
    private BarraBuilder barraBuilder;

    private Button btnEditarEvento;
    private ToggleButton btnEventosCriados, btnInscricoes;

    private Label lbNomeUsuario, lbEmailUsuario;
    private ImageView avatarView;

    private VBox listaEventos;

    private ToggleGroup groupFiltroEventos;

    private ScrollPane scrollEventos;
    private GridPane gridEventos;

    public record EventoME(String titulo,
                         String local,
                         String horaInicial,
                         String horaFinal,
                         String dataFormatadaI,
                         String dataFormatada2,
                         String nCapacidade) {}
    /**
     *Construtor da classe {@code UserScheduleView}.
     */
    public MyEventsView() {
        setupUI();
    }

    /**
     * Inicializa e organiza os elementos visuais da tela principal chamando métodos
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}, posicionando a barra lateral à esquerda,
     * a barra superior no topo e o conteúdo principal no centro.
     * A disposição dos componentes é gerenciada pelo {@code BorderPane}.
     */
    private void setupUI() {
        this.barraBuilder=new BarraBuilder();
        VBox barraLateral = this.barraBuilder.construirBarraLateral();
        HBox barraSuperior = this.barraBuilder.construirBarraSuperior();

        VBox conteudoCentral = criarContainerCentral();

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    public void setMyEventsViewController(MyEventsController myEventsController) {this.myEventsController = myEventsController;}

    /**
     * Este método constrói um subcabeçalho da interface gráfica, contendo botões de
     * navegação e informações do usuário logado.
     * @return um componente HBox do JavaFX
     */
    private HBox criarCabecalhoPrincipal() {
        HBox cabecalho = new HBox(15);
        cabecalho.setPadding(new Insets(10, 40, 10, 40));
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        cabecalho.getStyleClass().add("sub-header-controls");

        this.groupFiltroEventos = new ToggleGroup();

        btnEventosCriados = new ToggleButton("Eventos criados");
        btnEventosCriados.setToggleGroup(groupFiltroEventos);
        btnEventosCriados.getStyleClass().add("filter-button");
        btnEventosCriados.setSelected(true);

        btnInscricoes = new ToggleButton("Minhas Inscrições");
        btnInscricoes.setToggleGroup(groupFiltroEventos);
        btnInscricoes.getStyleClass().add("filter-button");

        HBox filtroBox = new HBox(10, btnEventosCriados, btnInscricoes);
        filtroBox.setAlignment(Pos.CENTER_LEFT);

        lbNomeUsuario = new Label();
        lbNomeUsuario.getStyleClass().add("user-display-label");

        lbEmailUsuario = new Label();
        lbEmailUsuario.getStyleClass().add("user-email-label");

        VBox userInfoText = new VBox(-2);
        userInfoText.setAlignment(Pos.CENTER_LEFT);
        userInfoText.getChildren().addAll(lbNomeUsuario, lbEmailUsuario);

        Image avatarImage = new Image(getClass().getResourceAsStream("/images/icone-padrao-usuario.png"));
        avatarView = new ImageView(avatarImage);
        avatarView = new ImageView();
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

        cabecalho.setBorder(new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        cabecalho.getChildren().addAll(filtroBox, spacer, userDisplayBox);

        return cabecalho;
    }

    /**
     * Método auxiliar para formatar a data no padrão "SEX, 28 DE JUN".
     * @param data A data a ser formatada.
     * @return A data formatada como String.
     */
    private String formatarDataParaCard(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd 'de' MMM", new Locale("pt", "BR"));
        return data.format(formatter).toUpperCase();
    }

    /**
     * Este método cria o grid de eventos que será exibido na área principal.
     * Os eventos são organizados em uma grade de 4 colunas.
     * @return um componente ScrollPane contendo o grid de eventos
     */
    private ScrollPane criarGridEventos() {
        gridEventos = new GridPane();
        gridEventos.setPadding(new Insets(10, 40, 20, 40));
        gridEventos.setHgap(25);
        gridEventos.setVgap(25);
        gridEventos.getStyleClass().add("events-grid");

        gridEventos.getColumnConstraints().clear();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);

        gridEventos.getColumnConstraints().addAll(col1);

        scrollEventos = new ScrollPane(gridEventos);
        scrollEventos.setFitToWidth(true);
        scrollEventos.getStyleClass().add("events-scroll-pane");
        scrollEventos.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollEventos.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return scrollEventos;
    }

    /**
     * Limpa o grid e exibe os eventos fornecidos.
     * Este método é chamado pelo HomeController para popular a interface.
     * @param eventos A lista de eventos a serem exibidos.
     */
    public void setEventos(List<MyEventsView.EventoME> eventos) {
        gridEventos.getChildren().clear();

        if (eventos == null || eventos.isEmpty()) {
            Label placeholder = new Label("Nenhum evento disponível no momento.");
            placeholder.getStyleClass().add("placeholder-label");
            StackPane placeholderPane = new StackPane(placeholder);
            placeholderPane.setAlignment(Pos.CENTER);
            gridEventos.add(placeholderPane, 0, 0, 3, 1);
            return;
        }

        for (int i = 0; i < eventos.size(); i++) {
            MyEventsView.EventoME evento = eventos.get(i);
            EventoMECartao cardEvento = new EventoMECartao();
            cardEvento.getStylesheets().add(getClass().getResource("/styles/event-list-card.css").toExternalForm());

            cardEvento.setLblTitulo(evento.titulo());
            cardEvento.setLblLocal(evento.local());
            cardEvento.setLblCapacidadeValor(evento.nCapacidade());

            if (evento.dataFormatadaI().equals(evento.dataFormatada2())) {
                String dataFormatada = formatarDataParaCard(LocalDate.parse(evento.dataFormatadaI()));
                String horario = evento.horaInicial() + " - " + evento.dataFormatada2();
                cardEvento.setDataUnica(dataFormatada, horario);
            } else {
                String inicioFormatado = formatarDataParaCard(LocalDate.parse(evento.dataFormatadaI())) + " às " + evento.horaInicial();
                String fimFormatado = formatarDataParaCard(LocalDate.parse(evento.dataFormatada2())) + " às " + evento.horaFinal();
                cardEvento.setDataMultipla(inicioFormatado, fimFormatado);
            }

            int row = i / 1;
            int col = i % 1;
            gridEventos.add(cardEvento, col, row);
        }
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
        carregarEventos();
        return listaEventos;
    }

    /** Esta classe cria um container que exibe a lista de eventos para o dia do mês e ano específicos, ao selecionar
     * outros dias da semana, é limpa a mensagem e exibida outra para o dia especifico.
     */
    private void carregarEventos() {
        listaEventos.getChildren().clear();

        Label placeholder = new Label("Eventos para " + lbEmailUsuario);
        listaEventos.getChildren().add(placeholder);
    }

    /**
     * O método monta a parte central da interface com um container, organizando o cabeçalho de controle,
     *  a lista de eventos em um layout vertical flexível e estilizado.
     * @return um VBox contendo todo o conteúdo central
     */
    private VBox criarContainerCentral() {
        HBox controlesSubCabecalho = criarCabecalhoPrincipal();
        VBox listaEventosVBox = criarListaEventos();

        ScrollPane gridEventosPane = criarGridEventos();

        VBox centerContent = new VBox(0);
        centerContent.getStyleClass().add("center-content-area");
        centerContent.getChildren().addAll(controlesSubCabecalho, gridEventosPane);

        VBox.setVgrow(gridEventosPane, Priority.ALWAYS);

        return centerContent;
    }

    /**
     * Métodos de encapsulamento getters e setters
     */
    public BarraBuilder getBarraBuilder() {return barraBuilder;}

    public Label getLbNomeUsuario() {return lbNomeUsuario;}
    public Label getLbEmailUsuario() { return lbEmailUsuario; }

    public ToggleButton getBtnEventosCriados() {return btnEventosCriados;}
    public ToggleButton getBtnInscricoes() {return btnInscricoes;}

    public void setNomeUsuario(String nome) {if (nome != null) {this.lbNomeUsuario.setText(nome);}}
    public void setEmailUsuario(String email) {if (email != null) {this.lbEmailUsuario.setText(email);}}
    public void setAvatar(Image avatar) {if (avatar != null) {this.avatarView.setImage(avatar);}}
}