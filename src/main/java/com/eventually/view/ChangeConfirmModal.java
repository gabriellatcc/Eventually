package com.eventually.view;

import com.eventually.controller.ChangeConfirmController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Classe para o modal de "Alterar Senha" dentro das configurações.
 * @author Yuri Garcia Maia (Estrutura base)
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.02
 * @since 29-05-2025
 */
public class ChangeConfirmModal {
    private Stage modalStage;
    private Scene modalScene;

    private Button btnSalvarSenha;
    private Button btnFechar;
    private PasswordField fldSenhaAtual;
    private PasswordField fldNovaSenha;
    private PasswordField fldConfirmarNovaSenha;

    private ChangeConfirmController cpController;

    /**
     * Construtor padrão da classe.
     */
    public ChangeConfirmModal() {
    }

    /**
     * Define o controller para este modal.
     * @param cpController O controller a ser usado.
     */
    public void setChangePasswordController(ChangeConfirmController cpController) {this.cpController = cpController;}

    /**
     * Exibe a janela modal configurada para alteração de senha.
     * @param parentStage Janela principal da aplicação que será usada como base para o modal.
     */
    public void showChangePasswordModal(Stage parentStage) {
        modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(parentStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        try {
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));
        } catch (Exception e) {
            System.err.println("Ícone do app não encontrado para ChangePasswordModal: " + e.getMessage());
        }

        final double MODAL_WIDTH = 500;
        final double MODAL_HEIGHT = 450;

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));
        layout.setPrefSize(MODAL_WIDTH, MODAL_HEIGHT);
        layout.setStyle("-fx-background-color: white; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.5, 0, 4);");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(40);
        rect.setArcHeight(40);
        layout.setClip(rect);

        Label title = new Label("Alterar Senha");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#B6318D"));
        title.setPadding(new Insets(5, 0, 10, 0));

        Label instruction = new Label("Para sua segurança, preencha os campos abaixo:");
        instruction.setFont(Font.font("Arial", 14));
        instruction.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        instruction.setPadding(new Insets(0,0,10,0));

        fldSenhaAtual = new PasswordField();
        fldSenhaAtual.setPromptText("Senha Atual");
        fldSenhaAtual.setMaxWidth(350);
        fldSenhaAtual.getStyleClass().add("login-field");

        fldNovaSenha = new PasswordField();
        fldNovaSenha.setPromptText("Nova Senha");
        fldNovaSenha.setMaxWidth(350);
        fldNovaSenha.getStyleClass().add("login-field");

        fldConfirmarNovaSenha = new PasswordField();
        fldConfirmarNovaSenha.setPromptText("Confirmar Nova Senha");
        fldConfirmarNovaSenha.setMaxWidth(350);
        fldConfirmarNovaSenha.getStyleClass().add("login-field");

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15,0,0,0));

        btnSalvarSenha = new Button("Salvar Alterações");
        btnSalvarSenha.setPrefHeight(40);
        btnSalvarSenha.setPrefWidth(160);
        btnSalvarSenha.setStyle("-fx-background-color: #D64BCD; -fx-text-fill: white; -fx-background-radius: 20;");
        btnSalvarSenha.setOnAction(e -> {
            if (cpController != null) {
                cpController.handleChangePasswordRequest(
                        fldSenhaAtual.getText(),
                        fldNovaSenha.getText(),
                        fldConfirmarNovaSenha.getText()
                );
            }
        });

        btnFechar = new Button("Fechar");
        btnFechar.setPrefHeight(40);
        btnFechar.setPrefWidth(120);
        btnFechar.setStyle("-fx-background-color: #D64BCD; -fx-text-fill: white; -fx-background-radius: 20;");
        btnFechar.setOnAction(e -> close());

        buttons.getChildren().addAll(btnSalvarSenha, btnFechar);

        layout.getChildren().addAll(title, instruction, fldSenhaAtual, fldNovaSenha, fldConfirmarNovaSenha, buttons);

        modalScene = new Scene(layout, MODAL_WIDTH, MODAL_HEIGHT, Color.TRANSPARENT);
        try {
            modalScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS não encontrado para ChangePasswordModal: " + e.getMessage());
        }
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    /**
     * Métodos de encapsulamento getters
     */
    public Button getBtnSalvarSenha() {return btnSalvarSenha;}
    public Button getBtnFechar() {return btnFechar;}
    public PasswordField getFldSenhaAtual() {return fldSenhaAtual;}
    public PasswordField getFldNovaSenha() {return fldNovaSenha;}
    public PasswordField getFldConfirmarNovaSenha() {return fldConfirmarNovaSenha;}
    public Scene getModalScene() {return modalScene;}

    /**
     * Fecha o modal.
     */
    public void close() {
        if (modalStage != null) {
            System.out.println("ChangeConfirmModal: Fechando modalStage: " + modalStage);
            modalStage.close();
        } else {
            System.out.println("ChangeConfirmModal: modalStage é null. Não é possível fechar.");
        }
    }
}