package com.eventually.view;

import com.eventually.controller.MyEventsController;
import com.eventually.model.UsuarioModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Esta classe representa a visualização da tela de programação de eventos do usuário
 * @version 1.09
 * @author Gabriela Tavares Costa Corrêa (Criação, documentação e revisão da classe)
 * @since 2025-04-06
 */
public class MyEventsView extends BorderPane {
    private MyEventsController myEventsController;

    private BarraBuilder barraBuilder;

    private ToggleButton btnEventosCriados, btnInscricoes, btnEventosFinalizados;

    private Label lbNomeUsuario, lbEmailUsuario;
    private ImageView avatarView;

    private VBox listaEventos;

    private ToggleGroup groupFiltroEventos;

    private ScrollPane scrollEventos;

    public record EventoMM(
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
            List<UsuarioModel> participantes,
            String linkAcesso,
            LocalDate dataI,
            LocalDate dataF,
            String horaI,
            String horaF
    ) {}
    /**
     *Construtor da classe {@code MyEventsView}.
     */
    public MyEventsView() {
        setupUI();
    }

    public void setMyEventsViewController(MyEventsController myEventsController) {this.myEventsController = myEventsController;}

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

    /**
     * Este método constrói um subcabeçalho da interface gráfica, contendo botões de
     * navegação e informações do usuário logado.
     * @return um componente HBox do JavaFX
     */
    private HBox criarCabecalhoPrincipal() {
        HBox cabecalho = new HBox(15);
        cabecalho.setPadding(new Insets(20, 40, 10, 40));
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

        btnEventosFinalizados = new ToggleButton("Finalizados");
        btnEventosFinalizados.setToggleGroup(groupFiltroEventos);
        btnEventosFinalizados.getStyleClass().add("filter-button");

        HBox filtroBox = new HBox(10, btnEventosCriados, btnInscricoes, btnEventosFinalizados);

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

        Color corDaBorda = Color.web("#f1f1f1");
        BorderWidths larguraDaBorda = new BorderWidths(0, 0, 3, 0);
        cabecalho.setBorder(new Border(new BorderStroke(corDaBorda,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, larguraDaBorda)));

        cabecalho.getChildren().addAll(filtroBox, spacer, userDisplayBox);

        return cabecalho;
    }

    private ScrollPane criarAreaEventos() {
        listaEventos = new VBox(15);
        listaEventos.setAlignment(Pos.TOP_CENTER);
        listaEventos.getStyleClass().add("event-list-container");
        listaEventos.setPadding(new Insets(20, 40, 20, 40));
        scrollEventos = new ScrollPane(listaEventos);
        scrollEventos.setFitToWidth(true);
        scrollEventos.getStyleClass().add("events-scroll-pane");
        scrollEventos.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollEventos.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox.setVgrow(scrollEventos, Priority.ALWAYS);

        return scrollEventos;
    }

    /**
     * Limpa o grid e exibe os eventoUS fornecidos.
     * Este método é chamado pelo HomeController para popular a interface.
     * @param eventoUS A lista de eventoUS a serem exibidos.
     */
    public void setEventos(List<MyEventsView.EventoMM> eventoUS) {
        listaEventos.getChildren().clear();

        if (eventoUS == null || eventoUS.isEmpty()) {
            Label placeholder = new Label("Nenhum evento disponível para a sessão selecionada.");
            placeholder.getStyleClass().add("placeholder-label");
            listaEventos.getChildren().add(placeholder);
            return;
        }

        Locale brasil = new Locale("pt", "BR");
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("EEE dd, MMM yyyy", brasil);
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        for (MyEventsView.EventoMM eventoMe: eventoUS) {
            EventoMECartao cardEvento = new EventoMECartao();

            String inscritos = String.valueOf(eventoMe.inscritos());
            String max = String.valueOf(eventoMe.capacidade());
            cardEvento.setLblCapacidadeValor(inscritos+"/"+max);

            cardEvento.setLblTitulo(eventoMe.titulo());
            cardEvento.setLblLocal(eventoMe.local());

            String dataHoraInicio = eventoMe.dataI().format(formatoData) + " " + eventoMe.horaI().format(String.valueOf(formatoHora));
            String dataHoraFim = eventoMe.dataF().format(formatoData) + " " + eventoMe.horaF().format(String.valueOf(formatoHora));

            cardEvento.setLblDataLinha1(dataHoraInicio);
            cardEvento.setLblDataLinha2(dataHoraFim);

            listaEventos.getChildren().add(cardEvento);
        }
    }

    /**
     * O método monta a parte central da interface com um container, organizando o cabeçalho de controle,
     *  a lista de eventos em um layout vertical flexível e estilizado.
     * @return um VBox contendo todo o conteúdo central
     */
    private VBox criarContainerCentral() {
        HBox controlesSubCabecalho = criarCabecalhoPrincipal();
        ScrollPane areaEventos = criarAreaEventos();

        VBox centerContent = new VBox(0);
        centerContent.getStyleClass().add("center-content-area");
        centerContent.getChildren().addAll(controlesSubCabecalho, areaEventos);

        return centerContent;
    }

    /**
     * Métodos de encapsulamento getters e setters
     */
    public BarraBuilder getBarraBuilder() {return barraBuilder;}
    public VBox getListaEventos() { return listaEventos; }

    public ToggleGroup getGroupFiltroEventos() {return groupFiltroEventos;}

    public Label getLbNomeUsuario() {return lbNomeUsuario;}
    public Label getLbEmailUsuario() { return lbEmailUsuario; }

    public ToggleButton getBtnEventosCriados() {return btnEventosCriados;}
    public ToggleButton getBtnInscricoes() {return btnInscricoes;}
    public ToggleButton getBtnEventosFinalizados() {return this.btnEventosFinalizados;}

    public void setNomeUsuario(String nome) {if (nome != null) {this.lbNomeUsuario.setText(nome);}}
    public void setEmailUsuario(String email) {if (email != null) {this.lbEmailUsuario.setText(email);}}
    public void setAvatar(Image avatar) {if (avatar != null) {this.avatarView.setImage(avatar);}}
}