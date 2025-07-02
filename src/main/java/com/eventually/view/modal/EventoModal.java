package com.eventually.view.modal;

import com.eventually.controller.EventoController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Classe responsável pelo modal de "Inscrição no Evento".
 * Exibe os detalhes de um evento específico e permite que o usuário interaja com ele.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e parte lógica)
 * @version 1.04
 * @since 2025-06-27
 */
public class EventoModal extends Parent {
    private EventoController eventoController;

    private ImageView imgTopoEvento;
    private Label lblTituloEvento;
    private Label lblDataHoraInicio;
    private Label lblDataHoraFim;

    private Label lblDescricao;
    private Label lblParticipantesInscritos;
    private Label lblVagasDisponiveis;
    private Label lblLocalizacao;
    private Label lblFormato;
    private FlowPane flowPaneTags;

    private Button btnInscrever;
    private Button btnCancelarInscricao;
    private Button btnVerParticipantes, btnComentarios, btnCompartilhar;
    private Button btnEditar;
    private Button btnExcluir;
    private Button btnSair;

    private VBox vbBotoesAcao;

    /**
     * Construtor padrão da classe.
     */
    public EventoModal() {setup();}

    /**
     * Define o controlador para este modal.
     * @param eventoController o controller a ser usado.
     */
    public void setInscricaoController(EventoController eventoController) {this.eventoController = eventoController;}

    /**
     * Configura e constrói o layout inicial do modal.
     */
    private void setup() {
        VBox modalContent = criarLayoutPrincipal();

        StackPane wrapper = new StackPane(modalContent);
        StackPane.setAlignment(modalContent, Pos.CENTER);

        this.getChildren().add(wrapper);
    }

    /**
     * Cria o layout principal e todos os seus componentes aninhados.
     * @return um VBox contendo toda a estrutura do modal.
     */
    public VBox criarLayoutPrincipal() {
        final double MODAL_WIDTH = 450;
        final double MODAL_HEIGHT = 700;

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("layout-pane");

        layout.setPrefSize(MODAL_WIDTH, MODAL_HEIGHT);
        layout.setMaxSize(MODAL_WIDTH, MODAL_HEIGHT);

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        layout.setClip(rect);

        imgTopoEvento = new ImageView();
        imgTopoEvento.setFitWidth(MODAL_WIDTH);
        imgTopoEvento.setFitHeight(180);
        imgTopoEvento.setPreserveRatio(false);

        VBox contentContainer = new VBox(5);
        contentContainer.setAlignment(Pos.TOP_LEFT);
        contentContainer.setPadding(new Insets(0, 25, 20, 25));

        lblTituloEvento = new Label();
        lblTituloEvento.getStyleClass().add("hcard-title");

        HBox hbDataHora = new HBox(0);
        hbDataHora.setAlignment(Pos.CENTER_LEFT);
        lblDataHoraInicio = new Label();
        lblDataHoraInicio.getStyleClass().add("hcard-date");
        Label lblSeparador = new Label(" | ");
        lblDataHoraFim = new Label();
        lblDataHoraFim.getStyleClass().add("hcard-date");
        hbDataHora.getChildren().addAll(lblDataHoraInicio, lblSeparador, lblDataHoraFim);

        Label lblHeaderDescricao = new Label("Descrição");
        lblHeaderDescricao.getStyleClass().add("title-label-modal");
        lblHeaderDescricao.setStyle("-fx-font-size: 20px;");
        lblDescricao = new Label();
        lblDescricao.setWrapText(true);
        lblDescricao.getStyleClass().add("label-modal");

        Label lblHeaderVagas = new Label("Vagas do Evento");
        lblHeaderVagas.getStyleClass().add("title-label-modal");
        lblHeaderVagas.setStyle("-fx-font-size: 20px;");

        HBox hbVagas = new HBox(10);
        hbVagas.setMinHeight(70);
        hbVagas.setAlignment(Pos.CENTER_LEFT);

        Label iconParticipante = new Label("\uD83D\uDC64");
        iconParticipante.setPrefWidth(80);
        iconParticipante.setStyle("-fx-font-size: 24px; -fx-text-fill: #7A2074;");

        lblParticipantesInscritos = new Label();
        lblParticipantesInscritos.setWrapText(true);
        lblParticipantesInscritos.getStyleClass().add("label-modal");

        Circle dotCapacidade = new Circle(6);
        dotCapacidade.getStyleClass().add("card-dot");

        lblVagasDisponiveis = new Label();
        lblVagasDisponiveis.getStyleClass().add("label-modal");
        lblVagasDisponiveis.setWrapText(true);
        lblVagasDisponiveis.setAlignment(Pos.CENTER_RIGHT);

        hbVagas.getChildren().addAll(iconParticipante, lblParticipantesInscritos, dotCapacidade, lblVagasDisponiveis);

        btnInscrever = new Button("Inscreva-se");
        btnInscrever.setAlignment(Pos.CENTER);
        btnInscrever.getStyleClass().add("modal-interact-button");
        btnInscrever.setMaxWidth(200);

        btnCancelarInscricao = new Button("Cancelar Inscrição");
        btnCancelarInscricao.getStyleClass().add("modal-close-button");
        btnCancelarInscricao.setMaxWidth(Double.MAX_VALUE);

        FlowPane vbAcoes = new FlowPane(10, 10);
        vbAcoes.setPadding(new Insets(5,0,0,0));
        vbAcoes.setAlignment(Pos.CENTER);
        btnVerParticipantes = new Button("Participantes");
        btnVerParticipantes.getStyleClass().add("action-interact-button");
        btnVerParticipantes.setMaxWidth(Double.MAX_VALUE);

        btnCompartilhar = new Button("Compartilhar");
        btnCompartilhar.getStyleClass().add("action-interact-button");
        btnCompartilhar.setMaxWidth(Double.MAX_VALUE);

        btnComentarios = new Button("Comentários");
        btnComentarios.getStyleClass().add("action-interact-button");
        btnComentarios.setMaxWidth(Double.MAX_VALUE);

        btnSair = new Button("Sair");
        btnSair.getStyleClass().add("sair-button");
        btnSair.setMaxWidth(Double.MAX_VALUE);

        vbAcoes.getChildren().addAll(btnVerParticipantes, btnComentarios, btnCompartilhar, btnSair);

        vbBotoesAcao = new VBox(10);

        btnEditar = new Button("Editar");
        btnEditar.getStyleClass().add("edit-button");
        btnEditar.setMaxWidth(Double.MAX_VALUE);

        btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("delet-button");
        btnExcluir.setMaxWidth(Double.MAX_VALUE);

        HBox vbAcoesFechar = new HBox();
        vbAcoesFechar.setPadding(new Insets(5, 10, 0, 10));
        vbAcoesFechar.setAlignment(Pos.CENTER);

        Pane espacador = new Pane();
        vbAcoesFechar.setHgrow(espacador, Priority.ALWAYS);

        vbAcoesFechar.getChildren().addAll(vbBotoesAcao,espacador,btnSair);

        HBox hboxLocalizacaoFormato = new HBox(5);
        hboxLocalizacaoFormato.setPadding(new Insets(5, 0, 0, 0));
        hboxLocalizacaoFormato.setAlignment(Pos.CENTER_LEFT);

        Label lblHeaderLocalizacao = new Label("Localização");
        lblHeaderLocalizacao.getStyleClass().add("title-label-modal");
        lblHeaderLocalizacao.setStyle("-fx-font-size: 20px;");

        lblFormato = new Label();
        lblFormato.getStyleClass().add("formato-label");

        hboxLocalizacaoFormato.getChildren().addAll(lblHeaderLocalizacao, lblFormato);

        lblLocalizacao = new Label();
        lblLocalizacao.setWrapText(true);
        lblLocalizacao.getStyleClass().add("label-modal");

        flowPaneTags = new FlowPane(10, 10);
        flowPaneTags.setAlignment(Pos.CENTER_LEFT);

        contentContainer.getChildren().addAll(
                flowPaneTags, lblTituloEvento, hbDataHora,
                lblHeaderDescricao, lblDescricao,
                lblHeaderVagas, hbVagas,
                hboxLocalizacaoFormato, lblLocalizacao,
                vbAcoes, vbAcoesFechar
        );

        layout.getChildren().addAll(imgTopoEvento, contentContainer);
        return layout;
    }

    /**
     * Fecha a janela (Stage) do modal.
     */
    public void close() {
        Stage stage = (Stage) this.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    public Button getBtnInscrever() {return btnInscrever;}
    public Button getBtnCancelarInscricao() { return btnCancelarInscricao; }

    public Button getBtnComentarios() {return btnComentarios;}
    public Button getBtnCompartilhar() {return btnCompartilhar;}
    public Button getBtnVerParticipantes() {return btnVerParticipantes;}

    public Button getBtnEditar() { return btnEditar; }
    public Button getBtnExcluir() { return btnExcluir; }
    public Button getBtnSair() {return btnSair;}

    public VBox getVbBotoesAcao() { return vbBotoesAcao; }

    public Label getLblTituloEvento() {return lblTituloEvento;}
    public Label getLblDataHoraFim() {return lblDataHoraFim;}
    public Label getLblDataHoraInicio() {return lblDataHoraInicio;}
    public Label getLblDescricao() {return lblDescricao;}
    public Label getLblParticipantesInscritos() {return lblParticipantesInscritos;}
    public Label getLblVagasDisponiveis() {return lblVagasDisponiveis;}
    public ImageView getImgTopoEvento() {return imgTopoEvento;}
    public Label getLblLocalizacao() {return lblLocalizacao;}
    public Label getLblFormato() {return lblFormato;}
    public FlowPane getFlowPaneTags() {return flowPaneTags;}
}