package com.eventually.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Esta classe representa o modal de confirmar encerrar sessão.
 * @author Yuri Garcia Maia
 * @version 1.0
 * @since 2025-06-22
 */
public class LogoutConfirmModal {

    private boolean result = false;

    /**
     * Exibe o modal de confirmação e aguarda a resposta do usuário.
     *
     * @param parentStage A janela principal da aplicação, usada como "dona" do modal.
     * @return {@code true} se o usuário confirmar a saída, {@code false} caso contrário.
     */
    public boolean showAndWait(Stage parentStage) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(parentStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        try {
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));
        } catch (Exception e) {
            System.err.println("Ícone do app não encontrado para LogoutConfirmModal: " + e.getMessage());
        }

        final double MODAL_WIDTH = 400;
        final double MODAL_HEIGHT = 280;

        VBox rootLayout = new VBox(20);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPadding(new Insets(30));
        rootLayout.getStyleClass().add("root-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(40);
        rect.setArcHeight(40);
        rootLayout.setClip(rect);

        Label title = new Label("Encerrar sessão?");
        title.getStyleClass().add("title-label");
        title.setStyle("-fx-font-size: 26px;");

        Label message = new Label("Tem certeza que deseja\nencerrar a sessão?");
        message.getStyleClass().add("field-label");
        message.setStyle("-fx-font-weight: normal; -fx-text-alignment: center; -fx-font-size: 16px;");

        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);

        Button btnSim = new Button("Sim");
        btnSim.getStyleClass().add("save-button");
        btnSim.setPrefWidth(120);
        btnSim.setOnAction(e -> {
            result = true;
            modalStage.close();
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("exit-button");
        btnCancelar.setPrefWidth(120);
        btnCancelar.setOnAction(e -> {
            result = false;
            modalStage.close();
        });

        buttonsBox.getChildren().addAll(btnSim, btnCancelar);

        Label idLabel = new Label("id xxxx-x");
        idLabel.getStyleClass().add("id-label");
        idLabel.setPadding(new Insets(10, 0, 0, 0));

        rootLayout.getChildren().addAll(title, message, buttonsBox, idLabel);

        Scene modalScene = new Scene(rootLayout, MODAL_WIDTH, MODAL_HEIGHT, Color.TRANSPARENT);
        try {
            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS não encontrado para LogoutConfirmModal: " + e.getMessage());
        }

        modalStage.setScene(modalScene);
        modalStage.showAndWait();

        return result;
    }
}
