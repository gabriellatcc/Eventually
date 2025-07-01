package com.eventually.view.modal;

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
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-07-01
 */
public class ModalConfirmarExclusao {
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
        modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

        final double MODAL_WIDTH = 400;
        final double MODAL_HEIGHT = 250;

        VBox rootLayout = new VBox(20);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPadding(new Insets(30));
        rootLayout.getStyleClass().add("layout-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(40);
        rect.setArcHeight(40);
        rootLayout.setClip(rect);

        Label title = new Label("Excluir evento");
        title.getStyleClass().add("title-label-modal");
        title.setStyle("-fx-font-size: 26px;");

        Label message = new Label("Tem certeza que deseja\ndeletar o evento?\uD83D\uDE14");
        message.getStyleClass().add("label-modal");
        message.setStyle("-fx-font-weight: normal; -fx-text-alignment: center; -fx-font-size: 16px;");

        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);

        Button btnSim = new Button("Sim");
        btnSim.getStyleClass().add("modal-interact-button");
        btnSim.setPrefWidth(120);
        btnSim.setOnAction(e -> {
            result = true;
            modalStage.close();
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("modal-interact-button");
        btnCancelar.setPrefWidth(120);
        btnCancelar.setOnAction(e -> {
            result = false;
            modalStage.close();
        });

        buttonsBox.getChildren().addAll(btnSim, btnCancelar);

        rootLayout.getChildren().addAll(title, message, buttonsBox);

        Scene modalScene = new Scene(rootLayout, MODAL_WIDTH, MODAL_HEIGHT, Color.TRANSPARENT);
        modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());

        modalStage.setScene(modalScene);
        modalStage.showAndWait();

        return result;
    }
}
