package com.eventually.controller;

import com.eventually.service.TelaService;
import com.eventually.view.SettingsView;
import com.eventually.view.UserScheduleView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Controller para a tela de Configurações.
 * Gerencia as interações do usuário e a lógica de negócios.
 */
public class SettingsController {

    private SettingsView settingsView;
    private final Stage primaryStage;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    /**
     * Construtor do SettingsController.
     * @param settingsView A instância da SettingsView associada.
     */
    public SettingsController(SettingsView settingsView, Stage primaryStage) {
        this.settingsView = settingsView;
        this.primaryStage = primaryStage;
        this.settingsView.setSettingsController(this);
        loadInitialData();
    }
    /**
     * Este método navega para a tela principal/Home (UserScheduleView).
     */
    public void navigateToSchedule() {
        System.out.println("Settings controller: botão de registro clicado");

        UserScheduleView userScheduleView = new UserScheduleView();
        UserScheduleController userScheduleController = new UserScheduleController(userScheduleView, primaryStage);
        userScheduleView.setUserScheduleController(userScheduleController);

        TelaService service = new TelaService();
        Scene sceneUserSchedule = new Scene(userScheduleView,service.medirWidth(),service.medirHeight());

        sceneUserSchedule.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());
        primaryStage.setTitle("Eventually - Progamação do Usuário");
        primaryStage.setScene(sceneUserSchedule);
    }
    /**
     * Carrega os dados iniciais do usuário e preferências na settingsView.
     */
    public void loadInitialData() {
  //      Map<String, Object> userSettings = settingsService.getUserSettings();
    //    settingsView.displayUserData(userSettings);

      //  List<String> contentPreferences = settingsService.getContentPreferences();
     //   settingsView.displayContentPreferences(contentPreferences);
    }

    /**
     * Manipula a atualização do nome do usuário.
     * @param newName O novo nome a ser salvo.
     */
    public void handleUpdateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            settingsView.showFieldError("name", "Nome não pode ser vazio.");
            return;
        }
      //  if (settingsService.updateUserName(newName.trim())) {
            settingsView.updateDisplayField("name", newName.trim());
            settingsView.showSuccessMessage("Nome atualizado com sucesso!");
       // } else {
            settingsView.showErrorMessage("Falha ao atualizar o nome.");
        //}
    }

    /**
     * Manipula a atualização do email do usuário.
     * @param newEmail O novo email a ser salvo.
     */
    public void handleUpdateEmail(String newEmail) {
      /*  if (newEmail == null || !EMAIL_PATTERN.matcher(newEmail).matches()) {
            settingsView.showFieldError("email", "Formato de email inválido.");
            return;
        }
        if (settingsService.updateUserEmail(newEmail)) {
            settingsView.updateDisplayField("email", newEmail);
            settingsView.showSuccessMessage("Email atualizado com sucesso!");
        } else {
            settingsView.showErrorMessage("Falha ao atualizar o email.");
        }

       */
    }

    /**
     * Manipula a atualização do telefone do usuário.
     * @param newPhone O novo telefone a ser salvo.
     */
    public void handleUpdatePhone(String newPhone) {
     /*   if (newPhone == null || newPhone.trim().isEmpty()) {
            settingsView.showFieldError("phone", "Telefone não pode ser vazio.");
            return;
        }
        if (settingsService.updateUserPhone(newPhone.trim())) {
            settingsView.updateDisplayField("phone", newPhone.trim());
            settingsView.showSuccessMessage("Telefone atualizado com sucesso!");
        } else {
            settingsView.showErrorMessage("Falha ao atualizar o telefone.");
        }

      */
    }

    /**
     * Manipula a atualização da senha do usuário.
     * @param newPassword A nova senha a ser salva.
     */
    public void handleUpdatePassword(String newPassword) {
        /*
        if (newPassword == null || newPassword.length() < 6) { // Exemplo simples
            settingsView.showFieldError("password", "Senha deve ter pelo menos 6 caracteres.");
            return;
        }
        if (settingsService.updateUserPassword(newPassword)) {
            settingsView.updateDisplayField("password", "********"); // Mostra asteriscos na UI
            settingsView.showSuccessMessage("Senha atualizada com sucesso!");
        } else {
            settingsView.showErrorMessage("Falha ao atualizar a senha.");
        }

         */
    }

    /**
     * Manipula a atualização da cidade do usuário.
     * @param newCity A nova cidade a ser salva.
     */
    public void handleUpdateCity(String newCity) {
      /*
        if (newCity == null || newCity.trim().isEmpty()) {
            settingsView.showFieldError("city", "Cidade não pode ser vazia.");
            return;
        }
        if (settingsService.updateUserCity(newCity.trim())) {
            settingsView.updateDisplayField("city", newCity.trim());
            settingsView.showSuccessMessage("Cidade atualizada com sucesso!");
        } else {
            settingsView.showErrorMessage("Falha ao atualizar a cidade.");
        }
       */
    }

    /**
     * Manipula a atualização da data de nascimento do usuário.
     * @param newDob A nova data de nascimento a ser salva.
     */
    public void handleUpdateDateOfBirth(LocalDate newDob) {
       /*
        if (newDob == null) {
            settingsView.showFieldError("dateOfBirth", "Data de nascimento não pode ser vazia.");
            return;
        }
        if (settingsService.updateUserDateOfBirth(newDob)) {
            settingsView.updateDisplayField("dateOfBirth", newDob.format(settingsView.getAppDateFormatter())); // Usa o formatter da settingsView
            settingsView.showSuccessMessage("Data de nascimento atualizada com sucesso!");
        } else {
            settingsView.showErrorMessage("Falha ao atualizar data de nascimento.");
        }
        */
    }

    /**
     * Manipula a tentativa de alterar a foto do perfil.
     * (Implementação de FileChooser seria na View, Controller recebe o caminho/arquivo)
     */
    public void handleChangeProfilePhoto() {
        /*
        System.out.println("CONTROLLER: Solicitação para alterar foto de perfil.");
        // Simula a obtenção de um novo caminho de foto (a settingsView lidaria com FileChooser)
        String newPhotoPath = "/path/to/new_profile_pic.png"; // Exemplo
        if (settingsService.updateUserProfilePhoto(newPhotoPath)) {
            settingsView.updateDisplayField("profilePhotoPath", newPhotoPath); // A settingsView decidiria como mostrar
            settingsView.showSuccessMessage("Foto de perfil alterada (simulação).");
        } else {
            settingsView.showErrorMessage("Falha ao alterar foto de perfil.");
        }
        */
    }

    /**
     * Manipula o salvamento das preferências de conteúdo.
     * @param selectedPreferences Lista das preferências selecionadas.
     */
    public void handleUpdateContentPreferences(List<String> selectedPreferences) {
        /*
        if (settingsService.updateContentPreferences(selectedPreferences)) {
            settingsView.showSuccessMessage("Preferências de conteúdo salvas!");
        } else {
            settingsView.showErrorMessage("Falha ao salvar preferências de conteúdo.");
        }
        */

    }

    /**
     * Manipula a ação de excluir conta.
     */
    public void handleDeleteAccount() {
       /*
        // Confirmação seria ideal (na settingsView)
        System.out.println("CONTROLLER: Solicitação para excluir conta.");
        if (settingsView.confirmAccountDeletion()) { // A settingsView deve ter um método de confirmação
            if (settingsService.deleteAccount()) {
                settingsView.showSuccessMessage("Conta excluída com sucesso. Você será desconectado.");
                // Lógica para deslogar e voltar para tela de login viria aqui
                navigateToLoginScreen(); // Assumindo que a settingsView tem este método
            } else {
                settingsView.showErrorMessage("Falha ao excluir a conta.");
            }
        } else {
            System.out.println("CONTROLLER: Exclusão de conta cancelada pelo usuário.");
        }
        tela
        */
    }
    /**
     * Este método navega para a tela de login.
     * (Chamado pelo controller após exclusão de conta, por exemplo)
     */
    public void navigateToLoginScreen() {
        /*
        LoginView loginView = new LoginView();
        LoginController loginController = new LoginController(loginView, primaryStage);
        loginView.setLoginController(loginController);

        TelaService settingsService = new TelaService();
        Scene loginScene = new Scene(loginView,settingsService.medirWidth(),settingsService.medirHeight());

        loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
        primaryStage.setTitle("Eventually - Login");
        primaryStage.setScene(loginScene);
        */
    }
    /**
     * Manipula a navegação para a tela inicial.
     */
    public void navigateToHome() {
        /*
        System.out.println("SettingsController: Navegando para Home.");
        settingsView.navigateToHome();

         */
    }
    /**
     * Manipula a navegação para a tela de meus eventos.
     */
    public void navigateToMyEvents() {
        /*
        System.out.println("SettingsController: Navegando para Meus Eventos.");
        settingsView.navigateToMyEvents();
        */
    }
    /**
     * Manipula a navegação para a tela de Programação (igual a Home neste contexto).
     */
    public void navigateToProgramacao() {
        /*System.out.println("SettingsController: Navegando para Programação.");
        settingsView.navigateToHome(); // Ou uma tela específica de programação
        */
    }

    /**
     * Manipula a navegação para a tela Minha Agenda (igual a Home neste contexto).
     */
    public void navigateToMinhaAgenda() {
        /*
        System.out.println("SettingsController: Navegando para Minha Agenda.");
        settingsView.navigateToHome();
        */
    }

    /**
     * Manipula a ação de abrir o modal/tela de novo evento.
     */
    public void navigateToNovoEvento() {
        System.out.println("SettingsController: Abrindo Novo Evento.");
        // lógica para abrir a tela de novo evento
    }
}