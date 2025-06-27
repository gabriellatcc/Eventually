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
 *
 * @author Yuri Garcia Maia
 * @version 1.0
 * @since 2025-06-22
 */
public class EventoHCard extends VBox {

    private static final double CARD_WIDTH = 320;
    private static final double CARD_HEIGHT = 280;
    private static final double IMAGE_HEIGHT = 200;

    private ImageView imagemEventoView;
    private Label lblDataHora;
    private Label lblTitulo;
    private Label lblLocal;
    private Label lblTipo;

    /**
     * Construtor padrão que inicializa a UI do card.
     */
    public EventoHCard() {
        setupUI();
    }

    private void setupUI() {
        this.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        this.getStyleClass().add("event-h-card");
        this.setAlignment(Pos.TOP_CENTER);

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(CARD_WIDTH, IMAGE_HEIGHT);
        imageContainer.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        imagemEventoView = new ImageView();
        imagemEventoView.setFitWidth(CARD_WIDTH);
        imagemEventoView.setFitHeight(IMAGE_HEIGHT);
        imagemEventoView.setPreserveRatio(false);

        Rectangle imageClip = new Rectangle(CARD_WIDTH, IMAGE_HEIGHT);
        imageClip.setArcWidth(20);
        imageClip.setArcHeight(20);
        imageContainer.setClip(imageClip);
        imageContainer.getChildren().add(imagemEventoView);

        VBox infoPane = new VBox(5);
        infoPane.setPadding(new Insets(10, 15, 15, 15));
        infoPane.getStyleClass().add("hcard-info-pane");

        lblDataHora = new Label("SEX 14, MAR 2025 - 18:20");
        lblDataHora.getStyleClass().add("hcard-date");

        lblTitulo = new Label("Título do evento");
        lblTitulo.getStyleClass().add("hcard-title");

        lblLocal = new Label("Local do evento");
        lblLocal.getStyleClass().add("hcard-location");

        lblTipo = new Label("Tipo do evento");
        lblTipo.getStyleClass().add("hcard-type");

        VBox titleLocationBox = new VBox(0, lblTitulo, lblLocal);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bottomInfoBox = new HBox(10, titleLocationBox, spacer, lblTipo);
        bottomInfoBox.setAlignment(Pos.BOTTOM_LEFT);

        infoPane.getChildren().addAll(lblDataHora, bottomInfoBox);

        this.getChildren().addAll(imageContainer, infoPane);
    }
    public Label getLblDataHora() {return lblDataHora;}
    public void setLblDataHora(String dataHora) {this.lblDataHora.setText(dataHora);}
    public Label getLblTitulo() {return lblTitulo;}
    public void setLblTitulo(String titulo) {this.lblTitulo.setText(titulo);}
    public Label getLblLocal() {return lblLocal;}
    public void setLblLocal(String local) {this.lblLocal.setText(local);}
    public Label getLblTipo() {return lblTipo;}
    public void setLblTipo(String tipo) {this.lblTipo.setText(tipo);}
    public ImageView getImagemEventoView() {return imagemEventoView;}
    public void setImagem(Image imagem) {this.imagemEventoView.setImage(imagem);}
}
