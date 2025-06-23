package com.eventually.view;

import com.eventually.controller.ConfirmaMudancaController;
import javafx.beans.value.ChangeListener;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Classe para o modal de "Alterar Senha" dentro das configurações.
 * @author Yuri Garcia Maia (Estrutura base)
 * @since 22-06-2025
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.04
 * @since 29-05-2025
 */
public class ConfirmaMudancaModal {
    private Stage modalStage;
    private Scene modalScene;

    private Button btnSalvarSenha;
    private Button btnFechar;
    private PasswordField fldSenhaAtual;
    private PasswordField fldNovaSenha;
    private PasswordField fldConfirmarNovaSenha;

    private Label lblPasso1, lblPasso2, lblPasso3;
    private Label lblRuleLength, lblRuleSpecial, lblRuleDigit, lblRuleLetter;

    private ConfirmaMudancaController cpController;

    /**
     * Construtor padrão da classe.
     */
    public ConfirmaMudancaModal() {
    }

    /**
     * Define o controller para este modal.
     * @param cpController O controller a ser usado.
     */
    public void setChangePasswordController(ConfirmaMudancaController cpController) {
        this.cpController = cpController;
    }

    /**
     * Exibe a janela modal configurada para alteração de senha.
     * @param parentStage Janela principal da aplicação que será usada como base para o modal.
     */
    public void showChangePasswordModal(Stage parentStage) {
        VBox layout = criarLayoutPrincipal();
        configurarListeners();

        modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(parentStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);

        try {
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));
        } catch (Exception e) {
            System.err.println("Ícone do app não encontrado para ChangePasswordModal: " + e.getMessage());
        }

        modalScene = new Scene(layout, 550, 650, Color.TRANSPARENT);
        try {
            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS não encontrado para ChangePasswordModal: " + e.getMessage());
        }

        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    private VBox criarLayoutPrincipal() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));
        layout.getStyleClass().add("root-pane-modal");

        Rectangle rect = new Rectangle(550, 650);
        rect.setArcWidth(40);
        rect.setArcHeight(40);
        layout.setClip(rect);

        Label title = new Label("Alterar Senha");
        title.getStyleClass().add("title-label-modal");

        VBox passosBox = criarPassosBox();

        fldSenhaAtual = new PasswordField();
        fldSenhaAtual.setPromptText("Senha Atual");
        fldSenhaAtual.setMaxWidth(350);
        fldSenhaAtual.getStyleClass().add("modal-field");

        fldNovaSenha = new PasswordField();
        fldNovaSenha.setPromptText("Nova Senha");
        fldNovaSenha.setMaxWidth(350);
        fldNovaSenha.getStyleClass().add("modal-field");

        fldConfirmarNovaSenha = new PasswordField();
        fldConfirmarNovaSenha.setPromptText("Confirmar Nova Senha");
        fldConfirmarNovaSenha.setMaxWidth(350);
        fldConfirmarNovaSenha.getStyleClass().add("modal-field");

        VBox passwordRulesBox = criarPasswordRulesBox();

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15,0,0,0));

        btnSalvarSenha = new Button("Salvar Alterações");
        btnSalvarSenha.getStyleClass().add("modal-save-button");
        btnSalvarSenha.setDisable(true);

        btnFechar = new Button("Fechar");
        btnFechar.getStyleClass().add("modal-close-button");

        buttons.getChildren().addAll(btnSalvarSenha, btnFechar);

        layout.getChildren().addAll(title, passosBox, fldSenhaAtual, fldNovaSenha, fldConfirmarNovaSenha, passwordRulesBox, buttons);

        return layout;
    }

    private void configurarListeners() {
        btnFechar.setOnAction(e -> close());

        if (cpController == null) {
            System.err.println("Controller não foi definido para ConfirmaMudancaModal. Os listeners não funcionarão.");
            return;
        }

        btnSalvarSenha.setOnAction(e -> cpController.processarMudancaDeSenha());

        ChangeListener<String> stepListener = (observable, oldValue, newValue) -> atualizarStatusPassos();
        fldSenhaAtual.textProperty().addListener(stepListener);
        fldNovaSenha.textProperty().addListener(stepListener);
        fldConfirmarNovaSenha.textProperty().addListener(stepListener);

        fldNovaSenha.textProperty().addListener((obs, oldVal, novoValorSenha) -> {
            Map<String, Boolean> rulesStatus;

            if (novoValorSenha == null || novoValorSenha.trim().isEmpty()) {
                rulesStatus = Map.of(
                        "hasSixChar", false,
                        "hasSpecial", false,
                        "hasDigit", false,
                        "hasLetter", false
                );
            } else {
                rulesStatus = cpController.conferirSenhaNova(novoValorSenha);
            }

            atualizarVisualizacaoSenha(rulesStatus);
        });
    }

    private VBox criarPassosBox() {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(350);
        box.setPadding(new Insets(0, 0, 10, 0));

        lblPasso1 = new Label("1. Digite sua senha atual");
        lblPasso2 = new Label("2. Digite a nova senha");
        lblPasso3 = new Label("3. Confirme a nova senha");

        for (Label passoLabel : List.of(lblPasso1, lblPasso2, lblPasso3)) {
            passoLabel.getStyleClass().add("step-label");
            passoLabel.getStyleClass().add("step-incomplete");
        }

        box.getChildren().addAll(lblPasso1, lblPasso2, lblPasso3);
        return box;
    }

    private VBox criarPasswordRulesBox() {
        VBox rulesBox = new VBox(5);
        rulesBox.setPadding(new Insets(5, 0, 0, 10));
        rulesBox.setMaxWidth(350);

        Label title = new Label("* A nova senha deve conter, no mínimo:");
        title.getStyleClass().add("password-rule-title");

        lblRuleLength = new Label("- 6 caracteres");
        lblRuleSpecial = new Label("- 1 caractere especial");
        lblRuleDigit = new Label("- 1 dígito");
        lblRuleLetter = new Label("- 1 letra");

        for (Label ruleLabel : List.of(lblRuleLength, lblRuleSpecial, lblRuleDigit, lblRuleLetter)) {
            ruleLabel.getStyleClass().add("password-rule");
            ruleLabel.getStyleClass().add("rule-invalid");
        }

        rulesBox.getChildren().addAll(title, lblRuleLength, lblRuleSpecial, lblRuleDigit, lblRuleLetter);
        return rulesBox;
    }

    private void atualizarStatusPassos() {
        boolean passo1Ok = !fldSenhaAtual.getText().isEmpty();
        boolean passo2Ok = !fldNovaSenha.getText().isEmpty();
        boolean passo3Ok = !fldConfirmarNovaSenha.getText().isEmpty();

        updateStepLabel(lblPasso1, passo1Ok);
        updateStepLabel(lblPasso2, passo2Ok);
        updateStepLabel(lblPasso3, passo3Ok);

        btnSalvarSenha.setDisable(!(passo1Ok && passo2Ok && passo3Ok));
    }

    private void updateStepLabel(Label label, boolean isComplete) {
        if (isComplete) {
            label.getStyleClass().remove("step-incomplete");
            label.getStyleClass().add("step-complete");
        } else {
            label.getStyleClass().remove("step-complete");
            label.getStyleClass().add("step-incomplete");
        }
    }

    public void atualizarVisualizacaoSenha(Map<String, Boolean> regras) {
        if (regras == null) {
            regras = Collections.emptyMap();
        }
        updateRuleLabel(lblRuleLength, regras.getOrDefault("hasSixChar", false));
        updateRuleLabel(lblRuleSpecial, regras.getOrDefault("hasSpecial", false));
        updateRuleLabel(lblRuleDigit, regras.getOrDefault("hasDigit", false));
        updateRuleLabel(lblRuleLetter, regras.getOrDefault("hasLetter", false));
    }

    private void updateRuleLabel(Label label, boolean isValid) {
        if (isValid) {
            label.getStyleClass().remove("rule-invalid");
            label.getStyleClass().add("rule-valid");
        } else {
            label.getStyleClass().remove("rule-valid");
            label.getStyleClass().add("rule-invalid");
        }
    }

    public void close() {
        if (modalStage != null) {
            modalStage.close();
        }
    }

    public Button getBtnSalvarSenha() {return btnSalvarSenha;}
    public Button getBtnFechar() {return btnFechar;}
    public PasswordField getFldSenhaAtual() {return fldSenhaAtual;}
    public PasswordField getFldNovaSenha() {return fldNovaSenha;}
    public PasswordField getFldConfirmarNovaSenha() {return fldConfirmarNovaSenha;}
    public Scene getModalScene() {return modalScene;}
}
