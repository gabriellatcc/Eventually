package com.eventually.service;

import com.eventually.controller.*;
import com.eventually.model.UsuarioModel;
import com.eventually.view.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** PASSÍVEL DE ALTERÇÕES
 * Serviço responsável por gerenciar a navegação entre as diferentes telas da aplicação, centraliza a lógica de
 * inicialização de telas e controladores de telas para evitar a duplicação de código em diferentes classes
 * controladores.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.0
 * @since 2025-06-19
 */
public class NavegacaoService {
    private final Stage primaryStage;
    private final TelaService telaService;
    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(NavegacaoService.class);

    /**
     * Construtor para o NavegacaoService.
     *
     * @param primaryStage O palco principal da aplicação, onde as cenas serão definidas.
     */
    public NavegacaoService(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.telaService = new TelaService();
    }

    /**
     * Navega para a tela de Login.
     * Limpa a sessão atual (se houver) e redireciona para a tela inicial de login.
     */
    public void navegarParaLogin() {
        sistemaDeLogger.info("Navegando para a tela de Login.");
        try {
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView, primaryStage);
            loginView.setLoginController(loginController);

            Scene loginScene = new Scene(loginView, telaService.medirWidth(), telaService.medirHeight());
            loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Login");
            primaryStage.setScene(loginScene);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela de Registro de Usuário.
     */
    public void navegarParaRegistro() {
        sistemaDeLogger.info("Navegando para a tela de Registro.");
        try {
            RegisterView registerView = new RegisterView();
            RegisterController registerController = new RegisterController(registerView, primaryStage);
            registerView.setRegisterController(registerController);

            Scene sceneRegister = new Scene(registerView, telaService.medirWidth(), telaService.medirHeight());
            sceneRegister.getStylesheets().add(getClass().getResource("/styles/register-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Registro do Usuário");
            primaryStage.setScene(sceneRegister);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela inicial (HomeView) após um login bem-sucedido.
     * @param usuarioAutenticado O modelo de usuário autenticado, necessário para inicializar a HomeView.
     */
    public void navegarParaHome(UsuarioModel usuarioAutenticado) {
        sistemaDeLogger.info("Navegando para a tela inicial (HomeView) para o usuário: " + usuarioAutenticado.getNomePessoa());
        try {
            HomeView homeView = new HomeView();
            HomeController homeController = new HomeController(usuarioAutenticado.getEmail(), homeView, primaryStage);
            homeView.setHomeController(homeController);

            Scene sceneHomeView = new Scene(homeView, telaService.medirWidth(), telaService.medirHeight());
            sceneHomeView.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Início");
            primaryStage.setScene(sceneHomeView);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela inicial (HomeView): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela de Meus Eventos.
     * @param emailUsuario o e-mail do usuário logado, necessário para carregar os eventos.
     */
    public void navegarParaMeusEventos(String emailUsuario) {
        sistemaDeLogger.info("Navegando para a tela de Meus Eventos para o usuário: " + emailUsuario);
        try {
            MyEventsView myEventsView = new MyEventsView();
            MyEventsController myEventsController = new MyEventsController(emailUsuario, myEventsView, primaryStage);
            myEventsView.setMyEventsViewController(myEventsController);

            Scene myEventsScene = new Scene(myEventsView, telaService.medirWidth(), telaService.medirHeight());
            myEventsScene.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Meus Eventos");
            primaryStage.setScene(myEventsScene);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Meus Eventos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela de Configurações do Usuário.
     * @param emailUsuario o e-mail do usuário logado, necessário para carregar as configurações.
     */
    public void navegarParaConfiguracoes(String emailUsuario) {
        sistemaDeLogger.info("Navegando para a tela de Configurações para o usuário: " + emailUsuario);
        try {
            SettingsView settingsView = new SettingsView();
            SettingsController settingsController = new SettingsController(emailUsuario, settingsView, primaryStage);
            settingsView.setSettingsController(settingsController);

            Scene settingsScene = new Scene(settingsView, telaService.medirWidth(), telaService.medirHeight());
            settingsScene.getStylesheets().add(String.valueOf(getClass().getResource("/styles/user-schedule-styles.toExternalForm()")));

            primaryStage.setTitle("Eventually - Configurações");
            primaryStage.setScene(settingsScene);
            primaryStage.setMaximized(true);
            sistemaDeLogger.info("Tela de Configurações carregada com sucesso.");
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Configurações: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela de Programação do Usuário.
     * @param emailUsuario o e-mail do usuário logado.
     */
    public void navegarParaProgramacao(String emailUsuario) {
        sistemaDeLogger.info("Navegando para a tela de Programação para o usuário: " + emailUsuario);
        try {
            UserScheduleView userScheduleView = new UserScheduleView();
            UserScheduleController userScheduleController = new UserScheduleController(emailUsuario, userScheduleView, primaryStage);
            userScheduleView.setUserScheduleController(userScheduleController);

            Scene sceneUserSchedule = new Scene(userScheduleView, telaService.medirWidth(), telaService.medirHeight());
            sceneUserSchedule.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Programação do Usuário");
            primaryStage.setScene(sceneUserSchedule);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Programação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Sair", abre um modal que aguarda confirmação para
     * saída da sessão e, em caso de falha na abertura do modal, é exibida uma mensagem no console.
     */
    public void abrirModalEscerrrarSessão() {
        //modal de encerrarsessao
        processarSaida();
    }

    /**
     * Retornando para a tela de login e, em caso de erro, é exibida
     * uma mensagem no console.
     */
    private void processarSaida() {
        try {
            navegarParaLogin();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Criar evento", navegando para a tela de criação de eventos e, em
     * caso de erro, é exibida uma mensagem no console.
     */
    public void processarCriacaoEvento() {
        sistemaDeLogger.info("Método processarCriacaoEvento() chamado.");
        try {
            sistemaDeLogger.info("Botão de Criar evento clicado!");
            //revisar funcionamento
            CriaEventoModal changeConfirmModal = new CriaEventoModal();
            changeConfirmModal.showCreateEventModal(primaryStage);
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao abrir modal para Criar Evento: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}