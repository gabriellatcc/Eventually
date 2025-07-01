package com.eventually.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Representa um card de evento.
 * Este componente encapsula a exibição de uma imagem e informações de um evento,
 * como título, data, local e tipo.
 * @version 1.02
 * @author Yuri Garcia Maia
 * @since 2025-06-22
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-06-28
 *
 */
public class EventoHCartao extends VBox {
    private static final double CARD_WIDTH = 400;
    private static final double CARD_HEIGHT = 280;
    private static final double IMAGE_HEIGHT = 200;

    private ImageView imagemEventoView;
    private Label lblDataHoraInicio, lblDataHoraFim;
    private Label lblTitulo;
    private Label lblLocal;
    private Label lblTipo;

    /**
     * Construtor padrão que inicializa a UI do card.
     */
    public EventoHCartao() {
        setupUI();
    }

    private void setupUI() {
        this.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        this.getStyleClass().add("event-h-card");
        this.setAlignment(Pos.TOP_CENTER);

        //
        imagemEventoView = new ImageView();
        imagemEventoView.setFitWidth(CARD_WIDTH);
        imagemEventoView.setFitHeight(IMAGE_HEIGHT);
        imagemEventoView.setPreserveRatio(false);

        StackPane imageContainer = new StackPane();
        imageContainer.getChildren().add(imagemEventoView);
        imageContainer.setPrefSize(CARD_WIDTH, IMAGE_HEIGHT);
        imageContainer.setStyle(
                "-fx-background-color: C7C7C7;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-width: 4px;" +
                        "-fx-border-radius: 20;"
        );

        Rectangle imageClip = new Rectangle(CARD_WIDTH, IMAGE_HEIGHT);
        imageClip.setArcWidth(20);
        imageClip.setArcHeight(20);
        imageContainer.setClip(imageClip);
//

        VBox infoPane = new VBox(5);
        infoPane.setPadding(new Insets(10, 15, 15, 15));
        infoPane.getStyleClass().add("hcard-info-pane");

        HBox hbDataHora = new HBox(0);
        hbDataHora.setAlignment(Pos.CENTER_LEFT);
        lblDataHoraInicio = new Label();
        lblDataHoraInicio.getStyleClass().add("hcard-date");
        Label lblSeparador = new Label(" | ");
        lblDataHoraFim = new Label();
        lblDataHoraFim.getStyleClass().add("hcard-date");
        hbDataHora.getChildren().addAll(lblDataHoraInicio, lblSeparador, lblDataHoraFim);

        lblTitulo = new Label("Título do evento");
        lblTitulo.getStyleClass().add("hcard-title");

        lblLocal = new Label("Local do evento");
        lblLocal.getStyleClass().add("hcard-location");

        lblTipo = new Label("Tipo do evento");
        lblTipo.getStyleClass().add("hcard-type");

        VBox titleLocationBox = new VBox(0, lblTitulo, lblLocal, lblTipo);

        HBox bottomInfoBox = new HBox(10, titleLocationBox);

        infoPane.getChildren().addAll(hbDataHora, bottomInfoBox);

        this.getChildren().addAll(imageContainer, infoPane);
    }

    public void setLblDataHoraInicio(String dataHora) {this.lblDataHoraInicio.setText(dataHora);}
    public void setLblDataHoraFim(String dataHora) {this.lblDataHoraFim.setText(dataHora);}
    public void setLblTitulo(String titulo) {this.lblTitulo.setText(titulo);}
    public void setLblLocal(String local) {this.lblLocal.setText(local);}
    public void setLblTipo(String tipo) {this.lblTipo.setText(tipo);}

    public void setImagem(Image imagem) {
        if(this.imagemEventoView != null &&  imagem != null ) {
            this.imagemEventoView.setImage(imagem);
        }
    }
}
