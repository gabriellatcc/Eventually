package com.eventually.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Classe do modal de aviso de "Senha incorreta ou email não cadastrado" da tela de login.
 * @author Yuri Garcia Maia
 * @version 1.0
 * @since 2025-06-22
 */
public class AlertModal {

    /**
     * Exibe o modal de aviso com a mensagem especificada.
     *
     * @param parentStage A janela principal da aplicação.
     * @param title O título da janela do modal.
     * @param message A mensagem a ser exibida.
     */
    public void show(Stage parentStage, String title, String message) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(parentStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        try {
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));
        } catch (Exception e) {
            System.err.println("Ícone do app não encontrado para AlertModal: " + e.getMessage());
        }

        final double MODAL_WIDTH = 400;
        final double MODAL_HEIGHT = 250;

        VBox rootLayout = new VBox(20);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPadding(new Insets(30));
        rootLayout.getStyleClass().add("root-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(40);
        rect.setArcHeight(40);
        rootLayout.setClip(rect);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title-label");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #8E2392;");

        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("field-label");
        messageLabel.setStyle("-fx-font-weight: normal; -fx-text-alignment: center; -fx-font-size: 16px;");
        messageLabel.setWrapText(true);

        Button btnOk = new Button("OK");
        btnOk.getStyleClass().add("edit-event-button");
        btnOk.setPrefWidth(140);
        btnOk.setOnAction(e -> modalStage.close());
        VBox.setMargin(btnOk, new Insets(10, 0, 0, 0));

        rootLayout.getChildren().addAll(titleLabel, messageLabel, btnOk);

        Scene modalScene = new Scene(rootLayout, MODAL_WIDTH, MODAL_HEIGHT, Color.TRANSPARENT);
        try {
            modalScene.getStylesheets().add(getClass().getResource("/styles/my-events-styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS não encontrado para AlertModal: " + e.getMessage());
        }

        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }
}
