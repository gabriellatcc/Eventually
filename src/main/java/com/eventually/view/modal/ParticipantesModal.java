package com.eventually.view.modal;

import com.eventually.model.UsuarioModel;
import com.eventually.view.HomeView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * Modal que exibe a lista de participantes de um evento.
 * Este modal é um VBox projetado para ser adicionado sobre outra tela,
 * geralmente dentro de um StackPane.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-07-02
 */
public class ParticipantesModal extends VBox {

    /**
     * Construtor que cria e configura o modal com os participantes do evento.
     * @param evento O record do evento contendo a lista de participantes.
     */
    public ParticipantesModal(HomeView.EventoH evento) {
        super(15);
        this.setupUI(evento);
    }

    /**
     * Configura a interface gráfica do modal.
     * @param evento O evento cujos participantes serão exibidos.
     */
    private void setupUI(HomeView.EventoH evento) {
        this.getStyleClass().add("participantes-modal-pane");
        this.setMaxSize(350, 450);
        this.setPadding(new Insets(15));

        Label lblTitulo = new Label("Participantes do Evento");
        lblTitulo.getStyleClass().add("modal-title");

        Button btnCloseX = new Button("X");
        btnCloseX.getStyleClass().add("close-button-x");
        btnCloseX.setOnAction(e -> fecharModal());

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerPane = new HBox(lblTitulo, spacer, btnCloseX);
        headerPane.setAlignment(Pos.CENTER_LEFT);

        VBox listaParticipantesVBox = new VBox(10);
        listaParticipantesVBox.setPadding(new Insets(5));

        if (evento.participantes().isEmpty()) {
            Label infoLabel = new Label("Ainda não há participantes inscritos.");
            infoLabel.getStyleClass().add("info-label");
            listaParticipantesVBox.getChildren().add(infoLabel);
        } else {
            for (UsuarioModel participante : evento.participantes()) {
                HBox linhaParticipante = criarLinhaParticipante(participante);
                listaParticipantesVBox.getChildren().add(linhaParticipante);
            }
        }

        ScrollPane scrollPane = new ScrollPane(listaParticipantesVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("participantes-scroll-pane");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.getChildren().addAll(headerPane, scrollPane);
    }

    /**
     * Cria uma linha (HBox) para um único participante.
     * @param participante O modelo de usuário do participante.
     * @return um HBox contendo a foto redonda e o email.
     */

    private HBox criarLinhaParticipante(UsuarioModel participante) {
        ImageView fotoView = new ImageView(participante.getFoto());
        double tamanhoFoto = 40;
        fotoView.setFitWidth(tamanhoFoto);
        fotoView.setFitHeight(tamanhoFoto);

        Circle clip = new Circle(tamanhoFoto / 2, tamanhoFoto / 2, tamanhoFoto / 2);
        fotoView.setClip(clip);

        Label lblEmail = new Label(participante.getEmail());
        lblEmail.getStyleClass().add("participant-email");

        HBox linha = new HBox(15, fotoView, lblEmail);
        linha.setAlignment(Pos.CENTER_LEFT);

        return linha;
    }

    /**
     * Remove este modal do seu container pai (geralmente um StackPane).
     */
    private void fecharModal() {
        Stage stage = (Stage) this.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}