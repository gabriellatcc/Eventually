package com.eventually.controller;

import com.eventually.service.SettingsService;
import com.eventually.view.SettingsView;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Controller para a tela de Configurações.
 * Gerencia as interações do usuário e a lógica de negócios.
 */
public class SettingsController {

    private SettingsView view;
    private SettingsService service;

    // Padrões para validação simples
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    // Adicione mais padrões conforme necessário (telefone, senha forte, etc.)

    /**
     * Construtor do SettingsController.
     * @param view A instância da SettingsView associada.
     * @param service A instância do SettingsService para manipulação de dados.
     */
    public SettingsController(SettingsView view, SettingsService service) {
        this.view = view;
        this.service = service;
        this.view.setController(this); // Permite que a view chame métodos do controller
        loadInitialData();
    }

    /**
     * Carrega os dados iniciais do usuário e preferências na view.
     */
    public void loadInitialData() {
        Map<String, Object> userSettings = service.getUserSettings();
        view.displayUserData(userSettings);

        List<String> contentPreferences = service.getContentPreferences();
        view.displayContentPreferences(contentPreferences);
    }

    /**
     * Manipula a atualização do nome do usuário.
     * @param newName O novo nome a ser salvo.
     */
    public void handleUpdateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            view.showFieldError("name", "Nome não pode ser vazio.");
            return;
        }
        if (service.updateUserName(newName.trim())) {
            view.updateDisplayField("name", newName.trim());
            view.showSuccessMessage("Nome atualizado com sucesso!");
        } else {
            view.showErrorMessage("Falha ao atualizar o nome.");
        }
    }

    /**
     * Manipula a atualização do email do usuário.
     * @param newEmail O novo email a ser salvo.
     */
    public void handleUpdateEmail(String newEmail) {
        if (newEmail == null || !EMAIL_PATTERN.matcher(newEmail).matches()) {
            view.showFieldError("email", "Formato de email inválido.");
            return;
        }
        if (service.updateUserEmail(newEmail)) {
            view.updateDisplayField("email", newEmail);
            view.showSuccessMessage("Email atualizado com sucesso!");
        } else {
            view.showErrorMessage("Falha ao atualizar o email.");
        }
    }

    /**
     * Manipula a atualização do telefone do usuário.
     * @param newPhone O novo telefone a ser salvo.
     */
    public void handleUpdatePhone(String newPhone) {
        // Adicionar validação de telefone se necessário
        if (newPhone == null || newPhone.trim().isEmpty()) {
            view.showFieldError("phone", "Telefone não pode ser vazio.");
            return;
        }
        if (service.updateUserPhone(newPhone.trim())) {
            view.updateDisplayField("phone", newPhone.trim());
            view.showSuccessMessage("Telefone atualizado com sucesso!");
        } else {
            view.showErrorMessage("Falha ao atualizar o telefone.");
        }
    }

    /**
     * Manipula a atualização da senha do usuário.
     * @param newPassword A nova senha a ser salva.
     */
    public void handleUpdatePassword(String newPassword) {
        // Adicionar validação de força de senha robusta aqui
        if (newPassword == null || newPassword.length() < 6) { // Exemplo simples
            view.showFieldError("password", "Senha deve ter pelo menos 6 caracteres.");
            return;
        }
        if (service.updateUserPassword(newPassword)) {
            view.updateDisplayField("password", "********"); // Mostra asteriscos na UI
            view.showSuccessMessage("Senha atualizada com sucesso!");
        } else {
            view.showErrorMessage("Falha ao atualizar a senha.");
        }
    }

    /**
     * Manipula a atualização da cidade do usuário.
     * @param newCity A nova cidade a ser salva.
     */
    public void handleUpdateCity(String newCity) {
        if (newCity == null || newCity.trim().isEmpty()) {
            view.showFieldError("city", "Cidade não pode ser vazia.");
            return;
        }
        if (service.updateUserCity(newCity.trim())) {
            view.updateDisplayField("city", newCity.trim());
            view.showSuccessMessage("Cidade atualizada com sucesso!");
        } else {
            view.showErrorMessage("Falha ao atualizar a cidade.");
        }
    }

    /**
     * Manipula a atualização da data de nascimento do usuário.
     * @param newDob A nova data de nascimento a ser salva.
     */
    public void handleUpdateDateOfBirth(LocalDate newDob) {
        if (newDob == null) {
            view.showFieldError("dateOfBirth", "Data de nascimento não pode ser vazia.");
            return;
        }
        if (service.updateUserDateOfBirth(newDob)) {
            view.updateDisplayField("dateOfBirth", newDob.format(view.getAppDateFormatter())); // Usa o formatter da view
            view.showSuccessMessage("Data de nascimento atualizada com sucesso!");
        } else {
            view.showErrorMessage("Falha ao atualizar data de nascimento.");
        }
    }

    /**
     * Manipula a tentativa de alterar a foto do perfil.
     * (Implementação de FileChooser seria na View, Controller recebe o caminho/arquivo)
     */
    public void handleChangeProfilePhoto() {
        System.out.println("CONTROLLER: Solicitação para alterar foto de perfil.");
        // Simula a obtenção de um novo caminho de foto (a view lidaria com FileChooser)
        String newPhotoPath = "/path/to/new_profile_pic.png"; // Exemplo
        if (service.updateUserProfilePhoto(newPhotoPath)) {
            view.updateDisplayField("profilePhotoPath", newPhotoPath); // A view decidiria como mostrar
            view.showSuccessMessage("Foto de perfil alterada (simulação).");
        } else {
            view.showErrorMessage("Falha ao alterar foto de perfil.");
        }
    }

    /**
     * Manipula o salvamento das preferências de conteúdo.
     * @param selectedPreferences Lista das preferências selecionadas.
     */
    public void handleUpdateContentPreferences(List<String> selectedPreferences) {
        if (service.updateContentPreferences(selectedPreferences)) {
            view.showSuccessMessage("Preferências de conteúdo salvas!");
        } else {
            view.showErrorMessage("Falha ao salvar preferências de conteúdo.");
        }
    }

    /**
     * Manipula a ação de excluir conta.
     */
    public void handleDeleteAccount() {
        // Confirmação seria ideal (na view)
        System.out.println("CONTROLLER: Solicitação para excluir conta.");
        if (view.confirmAccountDeletion()) { // A view deve ter um método de confirmação
            if (service.deleteAccount()) {
                view.showSuccessMessage("Conta excluída com sucesso. Você será desconectado.");
                // Lógica para deslogar e voltar para tela de login viria aqui
                view.navigateToLoginScreen(); // Assumindo que a view tem este método
            } else {
                view.showErrorMessage("Falha ao excluir a conta.");
            }
        } else {
            System.out.println("CONTROLLER: Exclusão de conta cancelada pelo usuário.");
        }
    }
    /**
     * Manipula a navegação para a tela inicial.
     */
    public void navigateToHome() {
        System.out.println("SettingsController: Navegando para Home.");
        view.navigateToHome();
    }
    /**
     * Manipula a navegação para a tela de meus eventos.
     */
    public void navigateToMyEvents() {
        System.out.println("SettingsController: Navegando para Meus Eventos.");
        view.navigateToMyEvents();
    }
    /**
     * Manipula a navegação para a tela de Programação (igual a Home neste contexto).
     */
    public void navigateToProgramacao() {
        System.out.println("SettingsController: Navegando para Programação.");
        view.navigateToHome(); // Ou uma tela específica de programação
    }

    /**
     * Manipula a navegação para a tela Minha Agenda (igual a Home neste contexto).
     */
    public void navigateToMinhaAgenda() {
        System.out.println("SettingsController: Navegando para Minha Agenda.");
        view.navigateToHome(); // Ou uma tela específica de agenda
    }

    /**
     * Manipula a ação de abrir o modal/tela de novo evento.
     */
    public void navigateToNovoEvento() {
        System.out.println("SettingsController: Abrindo Novo Evento.");
        // Lógica para abrir a tela de novo evento
    }
}