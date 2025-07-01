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
 * @version 1.03
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
    private Button btnVerParticipantes;
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

        VBox contentContainer = new VBox(10);
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

        btnVerParticipantes = new Button("Ver Participantes");
        btnVerParticipantes.getStyleClass().add("modal-interact-button-secondary");
        btnVerParticipantes.setMaxWidth(Double.MAX_VALUE);

        btnEditar = new Button("Editar");
        btnEditar.getStyleClass().add("modal-interact-button-secondary");
        btnEditar.setMaxWidth(Double.MAX_VALUE);

        btnExcluir = new Button("Excluir Evento");
        btnExcluir.getStyleClass().add("modal-close-button");
        btnExcluir.setMaxWidth(Double.MAX_VALUE);

        vbBotoesAcao = new VBox(10);

        HBox hboxLocalizacaoFormato = new HBox();
        hboxLocalizacaoFormato.setAlignment(Pos.CENTER_LEFT);

        Label lblHeaderLocalizacao = new Label("Localização");
        lblHeaderLocalizacao.getStyleClass().add("title-label-modal");
        lblHeaderLocalizacao.setStyle("-fx-font-size: 20px;");

        Pane espacad = new Pane();
        HBox.setHgrow(espacad, Priority.ALWAYS);

        lblFormato = new Label();
        lblFormato.getStyleClass().add("formato-label");

        hboxLocalizacaoFormato.getChildren().addAll(lblHeaderLocalizacao, espacad, lblFormato);

        lblLocalizacao = new Label();
        lblLocalizacao.setWrapText(true);
        lblLocalizacao.getStyleClass().add("label-modal");

        flowPaneTags = new FlowPane(10, 10);
        flowPaneTags.setAlignment(Pos.CENTER_LEFT);

        btnSair = new Button("Sair");
        btnSair.setAlignment(Pos.CENTER);
        btnSair.getStyleClass().add("modal-interact-button");
        btnSair.setMaxWidth(130);
        HBox hboxSair = new HBox(btnSair);
        hboxSair.setAlignment(Pos.CENTER);

        contentContainer.getChildren().addAll(
                lblTituloEvento, hbDataHora,
                lblHeaderDescricao, lblDescricao,
                lblHeaderVagas, hbVagas,
                vbBotoesAcao,
                hboxLocalizacaoFormato, lblLocalizacao,
                flowPaneTags, hboxSair
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
    public Button getBtnVerParticipantes() {return btnVerParticipantes;}
    public Button getBtnEditar() { return btnEditar; }
    public Button getBtnExcluir() { return btnExcluir; }
    public Button getBtnSair() {return btnSair;}

    public VBox getVbBotoesAcao() { return vbBotoesAcao; }

    //get titulo
    public Label getLblTituloEvento() {return lblTituloEvento;}

    //get data
    public Label getLblDataHoraFim() {return lblDataHoraFim;}
    public Label getLblDataHoraInicio() {return lblDataHoraInicio;}

    //get descricao
    public Label getLblDescricao() {return lblDescricao;}

    //get inscritos
    public Label getLblParticipantesInscritos() {return lblParticipantesInscritos;}

    //get n participantes
    public Label getLblVagasDisponiveis() {return lblVagasDisponiveis;}

    // get imagem
    public ImageView getImgTopoEvento() {return imgTopoEvento;}

    //get localizacao
    public Label getLblLocalizacao() {return lblLocalizacao;}

    // get foramto
    public Label getLblFormato() {return lblFormato;}

    //get preferencias -> loop de preferencias
    public FlowPane getFlowPaneTags() {return flowPaneTags;}
}