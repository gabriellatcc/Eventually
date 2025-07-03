
package com.eventually.view;

import com.eventually.controller.UserScheduleController;
import com.eventually.model.UsuarioModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
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
 * @author Yuri Garcia Maia
 * @version 1.11
 * @author Gabriela Tavares Costa Corrêa (Documentação e revisão da classe)
 * @since 2025-04-06
 */
public class UserScheduleView extends BorderPane {
    private UserScheduleController userScheduleController;

    private BarraBuilder barraBuilder;

    private Label lbNomeUsuario, lbEmailUsuario;
    private ImageView avatarView;

    private Label lbCabecalhoData;

    private ToggleGroup grupoDatas;
    private VBox listaEventos;

    private LocalDate dataSelecionada;

    private ScrollPane scrollEventos;

    private HBox seletorDataContainer;

    public record EventoUS(
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
    private HBox criarCabecalho() {
        HBox cabecalhoPrincipal = new HBox();
        cabecalhoPrincipal.setPadding(new Insets(20, 40, 10, 40));
        cabecalhoPrincipal.setAlignment(Pos.CENTER_LEFT);

        lbCabecalhoData = new Label();
        lbCabecalhoData.getStyleClass().add("greeting-label");
        atualizarCabecalho(LocalDate.now());

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

        cabecalhoPrincipal.getChildren().addAll(lbCabecalhoData, spacer, userDisplayBox);

        Color corDaBorda = Color.web("#f1f1f1");
        BorderWidths larguraDaBorda = new BorderWidths(0, 0, 3, 0);
        cabecalhoPrincipal.setBorder(new Border(new BorderStroke(corDaBorda,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, larguraDaBorda)));

        return cabecalhoPrincipal;
    }

    public void atualizarCabecalho(LocalDate data) {
        Locale locale = new Locale("pt", "BR");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE dd,", locale);
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM yy", locale);
        String dayStr = data.format(dayFormatter).toUpperCase();
        String monthYearStr = data.format(monthYearFormatter).toUpperCase();
        lbCabecalhoData.setText("Eventos para " + dayStr + " " + monthYearStr + ".");
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
        seletorDataContainer.setPadding(new Insets(10, 40, 20, 40));
        seletorDataContainer.getStyleClass().add("date-picker-bar");

        return seletorDataContainer;
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
    public void setEventos(List<UserScheduleView.EventoUS> eventoUS) {
        listaEventos.getChildren().clear();

        if (eventoUS == null || eventoUS.isEmpty()) {
            Label placeholder = new Label("Nenhum evento disponível para a data selecionada.");
            placeholder.getStyleClass().add("placeholder-label");
            listaEventos.getChildren().add(placeholder);
            return;
        }

        Locale brasil = new Locale("pt", "BR");
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("EEE dd, MMM yyyy", brasil);
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        for (UserScheduleView.EventoUS eventoU : eventoUS) {
            EventoMECartao cardEvento = new EventoMECartao();

            cardEvento.setLblTitulo(eventoU.titulo());
            cardEvento.setLblLocal(eventoU.local());

            String dataHoraInicio = eventoU.dataI().format(formatoData) + " " + eventoU.horaI().format(String.valueOf(formatoHora));
            String dataHoraFim = eventoU.dataF().format(formatoData) + " " + eventoU.horaF().format(String.valueOf(formatoHora));

            cardEvento.setLblDataLinha1(dataHoraInicio);
            cardEvento.setLblDataLinha2(dataHoraFim);

            String inscritos = String.valueOf(eventoU.inscritos());
            String max = String.valueOf(eventoU.capacidade());
            cardEvento.setLblCapacidadeValor(inscritos+"/"+max);

            listaEventos.getChildren().add(cardEvento);
        }
    }

    /**
     * O método monta a parte central da interface com um container, organizando o cabeçalho de controle,
     * o seletor de datas e a lista de eventos em um layout vertical flexível e estilizado.
     * @return
     */
    private VBox criarContainerCentral() {
        HBox cabecalhoPrincipal = criarCabecalho();
        HBox seletorDeDatas = criarSeletorDeDatas();

        ScrollPane areaEventos = criarAreaEventos();

        VBox centerContent = new VBox(10);
        centerContent.getStyleClass().add("center-content-area");
        centerContent.getChildren().addAll(cabecalhoPrincipal, seletorDeDatas, areaEventos);

        return centerContent;
    }

    /**
     * Métodos de encapsulamento getters e setters
     */
    public BarraBuilder getBarraBuilder() { return barraBuilder; }

    public Label getLbCabecalhoData() {return lbCabecalhoData;}
    public Label getLbNomeUsuario() { return lbNomeUsuario; }
    public Label getLbEmailUsuario() { return lbEmailUsuario; }
    public void setAvatarImagem(Image avatarImagem) {if (this.avatarView != null && avatarImagem != null) {this.avatarView.setImage(avatarImagem);}}
    public ToggleGroup getGrupoDatas() { return grupoDatas; }
    public HBox getSeletorDataContainer() { return seletorDataContainer; }
    public VBox getListaEventos() { return listaEventos; }
    public void setNomeUsuario(String nome) { if (nome != null) this.lbNomeUsuario.setText(nome); }
    public void setEmailUsuario(String email) { if (email != null) this.lbEmailUsuario.setText(email); }
}