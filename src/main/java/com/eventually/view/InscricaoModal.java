package com.eventually.view;

import com.eventually.controller.InscricaoController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Classe responsável pelo modal de "Inscrição no Evento".
 * Exibe os detalhes de um evento específico e permite que o usuário interaja com ele.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e parte lógica)
 * @version 1.0
 * @since 2025-06-27
 */
public class InscricaoModal extends Parent {
    private InscricaoController inscricaoController;

    private ImageView imgTopoEvento;
    private Label lblTituloEvento;
    private Label lblDataHoraEvento;
    private Label lblDescricao;
    private Label lblParticipantesInscritos;
    private Label lblVagasDisponiveis;
    private ProgressBar pbVagas;
    private Label lblLocalizacao;
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

        preencherDadosExemplo();
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
        layout.getStyleClass().add("root-pane");
        layout.getStyleClass().add("layout-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        layout.setClip(rect);


        Image image = new Image(getClass().getResourceAsStream("/images/evento-padrao.jpg"));
        imgTopoEvento = new ImageView(image);
        imgTopoEvento.setFitWidth(MODAL_WIDTH);
        imgTopoEvento.setFitHeight(180);
        imgTopoEvento.setPreserveRatio(false);

        VBox contentContainer = new VBox(15);
        contentContainer.setPadding(new Insets(10, 25, 20, 25));
        contentContainer.setAlignment(Pos.TOP_LEFT);

        lblTituloEvento = new Label("Título do evento");
        lblTituloEvento.getStyleClass().add("title-label-modal");

        lblDataHoraEvento = new Label("Sexta-Feira, 20 de junho, 2025 - 15:00h às 17:00h");
        lblDataHoraEvento.getStyleClass().add("subtitle-label-modal");
        lblDataHoraEvento.setStyle("-fx-font-size: 14px;");

        Label lblHeaderDescricao = new Label("Descrição");
        lblHeaderDescricao.getStyleClass().add("title-label-modal");
        lblHeaderDescricao.setStyle("-fx-font-size: 20px;");
        lblDescricao = new Label("Um evento inovador com palestras e dinâmicas para melhor aprendizagem dos participantes");
        lblDescricao.setWrapText(true);
        lblDescricao.getStyleClass().add("label-modal");

        Label lblHeaderVagas = new Label("Vagas do Evento");
        lblHeaderVagas.getStyleClass().add("title-label-modal");
        lblHeaderVagas.setStyle("-fx-font-size: 20px;");

        HBox hbVagas = new HBox(10);
        hbVagas.setAlignment(Pos.CENTER_LEFT);

        Label iconParticipante = new Label("\uD83D\uDC64");
        iconParticipante.setStyle("-fx-font-size: 24px; -fx-text-fill: #9747FF;");

        lblParticipantesInscritos = new Label("30 participantes inscritos");
        lblParticipantesInscritos.getStyleClass().add("label-modal");

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        lblVagasDisponiveis = new Label("20 de 50 vagas Disponíveis");
        lblVagasDisponiveis.getStyleClass().add("label-modal");
        lblVagasDisponiveis.setAlignment(Pos.CENTER_RIGHT);

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
                lblTituloEvento, lblDataHoraEvento,
                new Separator(),
                lblHeaderDescricao, lblDescricao,
                new Separator(),
                lblHeaderVagas, hbVagas, pbVagas,
                vbBotoesAcao,
                new Separator(),
                lblHeaderLocalizacao, lblLocalizacao,
                new Separator(),
                flowPaneTags
        );

        layout.getChildren().addAll(imgTopoEvento, contentContainer, hbSair);
        return layout;
    }

    /**
     * Popula o modal com os dados de exemplo vistos na imagem.
     * Em uma aplicação real, um método como `popularDados(Evento evento)` seria chamado pelo Controller.
     */
    private void preencherDadosExemplo() {
        lblTituloEvento.setText("Título do evento");
        lblDataHoraEvento.setText("Sexta-Feira, 20 de junho, 2025 - 15:00h às 17:00h");
        lblDescricao.setText("Um evento inovador com palestras e dinâmicas para melhor aprendizagem dos participantes");

        int inscritos = 30;
        int totalVagas = 50;
        lblParticipantesInscritos.setText(inscritos + " participantes inscritos");
        lblVagasDisponiveis.setText((totalVagas - inscritos) + " de " + totalVagas + " vagas Disponíveis");
        pbVagas.setProgress((double) inscritos / totalVagas);

        lblLocalizacao.setText("Faculty Cruise Technology, Av. Rotary, 383 - Vila Paulista, Cruzeiro - SP, 12701-170");

        String[] tags = {"#educacionais", "#culturais", "#educacionais", "#culturais", "#educacionais", "#culturais"};
        for (String nomeTag : tags) {
            Label tagLabel = new Label(nomeTag);
            tagLabel.getStyleClass().add("tag-label");
            flowPaneTags.getChildren().add(tagLabel);
        }
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
}