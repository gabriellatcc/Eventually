package com.eventually.view;

import com.eventually.controller.InscricaoController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Classe responsável pelo modal de "Inscrição no Evento".
 * Exibe os detalhes de um evento específico e permite que o usuário interaja com ele.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e parte lógica)
 * @version 1.01
 * @since 2025-06-27
 */
public class InscricaoModal extends Parent {
    private InscricaoController inscricaoController;

    private ImageView imgTopoEvento;
    private Label lblTituloEvento;
    private Label lblDataHoraInicio;
    private Label lblDataHoraFim;

    private Label lblDescricao;
    private Label lblParticipantesInscritos;
    private Label lblVagasDisponiveis;
    private ProgressBar pbVagas;
    private Label lblLocalizacao;
    private Label lblFormato;
    private FlowPane flowPaneTags;

    private Button btnInscrever;
    private Button btnVerParticipantes;
    private Button btnSair;

    /**
     * Construtor padrão da classe.
     */
    public InscricaoModal() {setup();}

    /**
     * Define o controlador para este modal.
     * @param inscricaoController o controller a ser usado.
     */
    public void setInscricaoController(InscricaoController inscricaoController) {this.inscricaoController = inscricaoController;}

    /**
     * Configura e constrói o layout inicial do modal.
     */
    private void setup() {
        VBox layout = criarLayoutPrincipal();
        this.getChildren().add(layout);
    }

    /**
     * Cria o layout principal e todos os seus componentes aninhados.
     * @return um VBox contendo toda a estrutura do modal.
     */
    public VBox criarLayoutPrincipal() {
        final double MODAL_WIDTH = 450;
        final double MODAL_HEIGHT = 750;

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(0));
        layout.getStyleClass().add("layout-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        layout.setClip(rect);

        imgTopoEvento = new ImageView();
        imgTopoEvento.setFitWidth(MODAL_WIDTH);
        imgTopoEvento.setFitHeight(180);
        imgTopoEvento.setPreserveRatio(false);

        VBox contentContainer = new VBox(15);
        contentContainer.setPadding(new Insets(10, 25, 20, 25));
        contentContainer.setAlignment(Pos.TOP_LEFT);

        lblTituloEvento = new Label();
        lblTituloEvento.getStyleClass().add("title-label-modal");


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
        iconParticipante.setStyle("-fx-font-size: 24px; -fx-text-fill: #c93acd;");

        lblParticipantesInscritos = new Label();
        lblParticipantesInscritos.getStyleClass().add("label-modal");

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        lblVagasDisponiveis = new Label();
        lblVagasDisponiveis.getStyleClass().add("label-modal");
        lblVagasDisponiveis.setAlignment(Pos.CENTER_RIGHT);

        lblFormato = new Label();
        lblFormato.getStyleClass().add("label-modal");
        lblFormato.setAlignment(Pos.CENTER_RIGHT);

        hbVagas.getChildren().addAll(iconParticipante, lblParticipantesInscritos, spacer, lblVagasDisponiveis);

        pbVagas = new ProgressBar(0.6);
        pbVagas.setMaxWidth(Double.MAX_VALUE);
        pbVagas.getStyleClass().add("progress-bar-vagas");

        btnInscrever = new Button("Inscreva-se");
        btnInscrever.getStyleClass().add("modal-interact-button");
        btnInscrever.setMaxWidth(Double.MAX_VALUE);

        btnVerParticipantes = new Button("Ver Participantes");
        btnVerParticipantes.getStyleClass().add("modal-interact-button-secondary");
        btnVerParticipantes.setMaxWidth(Double.MAX_VALUE);

        VBox vbBotoesAcao = new VBox(10, btnInscrever, btnVerParticipantes);

        Label lblHeaderLocalizacao = new Label("Localização");
        lblHeaderLocalizacao.getStyleClass().add("title-label-modal");
        lblHeaderLocalizacao.setStyle("-fx-font-size: 20px;");
        lblLocalizacao = new Label("Faculty Cruise Technology, Av. Rotary, 383 - Vila Paulista, Cruzeiro - SP, 12701-170");
        lblLocalizacao.setWrapText(true);
        lblLocalizacao.getStyleClass().add("label-modal");

        flowPaneTags = new FlowPane(10, 10);
        flowPaneTags.setAlignment(Pos.CENTER_LEFT);

        btnSair = new Button("Sair");
        btnSair.getStyleClass().add("modal-interact-button");
        btnSair.setMaxWidth(Double.MAX_VALUE);
        HBox hbSair = new HBox(btnSair);
        hbSair.setAlignment(Pos.CENTER);
        hbSair.setPadding(new Insets(10, 0, 0, 0));

        contentContainer.getChildren().addAll(
                lblTituloEvento, hbDataHora,
                lblHeaderDescricao, lblDescricao,
                lblHeaderVagas, lblFormato, hbVagas, pbVagas,
                vbBotoesAcao,
                lblHeaderLocalizacao, lblLocalizacao,
                flowPaneTags
        );

        layout.getChildren().addAll(imgTopoEvento, contentContainer, hbSair);
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
    public Button getBtnVerParticipantes() {return btnVerParticipantes;}
    public Button getBtnSair() {return btnSair;}

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