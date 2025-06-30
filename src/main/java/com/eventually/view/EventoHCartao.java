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
 * @version 1.01
 * @author Yuri Garcia Maia
 * @since 2025-06-22
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-06-28
 *
 *
 */
public class EventoHCartao extends VBox {
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
    public EventoHCartao() {
        setupUI();
    }

    private void setupUI() {
        this.setBorder(new Border(new BorderStroke(Color.RED,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.setPrefSize(CARD_WIDTH, CARD_HEIGHT);

        this.setAlignment(Pos.TOP_CENTER);

        StackPane imageContainer = new StackPane();
        imageContainer.setBorder(new Border(new BorderStroke(Color.DEEPPINK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
        infoPane.setBorder(new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        infoPane.setPadding(new Insets(10, 15, 15, 15));
        infoPane.getStyleClass().add("hcard-info-pane");

        lblDataHora = new Label("SEX 14, MAR 2025 - 18:20");
        lblDataHora.setBorder(new Border(new BorderStroke(Color.GRAY,
                BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        lblDataHora.getStyleClass().add("hcard-date");

        lblTitulo = new Label("Título do evento");
        lblTitulo.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        lblTitulo.getStyleClass().add("hcard-title");

        lblLocal = new Label("Local do evento");
        lblLocal.setBorder(new Border(new BorderStroke(Color.BROWN,
                BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        lblLocal.getStyleClass().add("hcard-location");

        lblTipo = new Label("Tipo do evento");
        lblTipo.setBorder(new Border(new BorderStroke(Color.TEAL,
                BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        lblTipo.getStyleClass().add("hcard-type");

        VBox titleLocationBox = new VBox(0, lblTitulo, lblLocal);
        titleLocationBox.setBorder(new Border(new BorderStroke(Color.ORANGE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bottomInfoBox = new HBox(10, titleLocationBox, spacer, lblTipo);
        bottomInfoBox.setBorder(new Border(new BorderStroke(Color.LIGHTGOLDENRODYELLOW,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        bottomInfoBox.setAlignment(Pos.BOTTOM_LEFT);

        infoPane.getChildren().addAll(lblDataHora, bottomInfoBox);

        this.getChildren().addAll(imageContainer, infoPane);
    }
    public void setLblDataHora(String dataHora) {this.lblDataHora.setText(dataHora);}
    public void setLblTitulo(String titulo) {this.lblTitulo.setText(titulo);}
    public void setLblLocal(String local) {this.lblLocal.setText(local);}
    public void setLblTipo(String tipo) {this.lblTipo.setText(tipo);}
    public void setImagem(Image imagem) {this.imagemEventoView.setImage(imagem);}
}
