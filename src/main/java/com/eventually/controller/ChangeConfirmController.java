package com.eventually.controller;

import com.eventually.service.SettingsService;
import com.eventually.view.ChangeConfirmModal;
import javafx.scene.control.Alert;
import java.util.regex.Pattern;

/**
 * Controller para o modal de Alteração de Senha.
 * Gerencia a lógica de validação e atualização de senha.
 * @author Yuri Garcia Maia (Estrutura base)
 * @version 1.02
 * @since 2025-05-23
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-05-29
 */
public class ChangeConfirmController {

    private ChangeConfirmModal view;
    private SettingsService settingsService;

    private static final Pattern PASSWORD_LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern PASSWORD_UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern PASSWORD_DIGIT_PATTERN = Pattern.compile(".*[0-9].*");

    /**
     * Construtor do ChangeConfirmController.
     * @param view A instância da ChangeConfirmModal associada.
     * @param settingsService O serviço para interagir com os dados do usuário.
     */
    public ChangeConfirmController(ChangeConfirmModal view, SettingsService settingsService) {
        this.view = view;
        this.settingsService = settingsService;
    }

    /**
     * Este método exibe um Alert simples.
     *
     * @param alertType O tipo de alerta (ERROR, INFORMATION, etc.).
     * @param title O título do alerta.
     * @param message A mensagem do alerta.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (view != null && view.getModalScene() != null && view.getModalScene().getWindow() != null && view.getModalScene().getWindow().isShowing()) {
            alert.initOwner(view.getModalScene().getWindow());
        }
        alert.showAndWait();
    }

    /**
     * Manipula a solicitação de alteração de senha vinda da view.
     *
     * @param currentPassword A senha atual fornecida pelo usuário.
     * @param newPassword A nova senha fornecida pelo usuário.
     * @param confirmPassword A confirmação da nova senha.
     */
    public void handleChangePasswordRequest(String currentPassword, String newPassword, String confirmPassword) {
        if (view == null || settingsService == null) {
            System.err.println("ChangeConfirmController: View ou Service não configurado.");
            return;
        }

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Todos os campos de senha são obrigatórios.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "A nova senha e a confirmação não coincidem.");
            return;
        }

        if (newPassword.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Senha Inválida", "A nova senha deve ter no mínimo 8 caracteres.");
            return;
        }
        boolean hasLower = PASSWORD_LOWERCASE_PATTERN.matcher(newPassword).find();
        boolean hasUpper = PASSWORD_UPPERCASE_PATTERN.matcher(newPassword).find();
        boolean hasDigit = PASSWORD_DIGIT_PATTERN.matcher(newPassword).find();

        if (!hasLower || !hasUpper || !hasDigit) {
            showAlert(Alert.AlertType.ERROR, "Senha Inválida", "A nova senha deve conter letras maiúsculas, minúsculas e números.");
            return;
        }
    }
}