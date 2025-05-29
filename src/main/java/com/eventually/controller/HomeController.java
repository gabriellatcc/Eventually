package com.eventually.controller;

import com.eventually.service.TelaService;
import com.eventually.view.HomeView;
import com.eventually.view.SettingsView;
import com.eventually.view.UserScheduleView;
import com.eventually.view.LoginView;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe controller da tela inicial.
 * Esta classe é responsável pela comunicação
 * da tela inicial com o backend e navegação entre telas.
 *
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-05-23
 */
public class HomeController {
    private final HomeView homeView;
    private final Stage primaryStage;

    /**
     * Construtor do {@code HomeController}, inicializa a view inicial e define os manipuladores de eventos.
     * @param homeView a interface inicial associada
     * @param primaryStage o palco principal da aplicação
     */
    public HomeController(HomeView homeView, Stage primaryStage) {
        this.homeView = homeView;
        this.primaryStage = primaryStage;
        this.homeView.setHomeController(this);
        setupEventHandlersHome();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela inicial.
     */
    public void setupEventHandlersHome() {
        homeView.getBtnMeusEventos().setOnAction(e -> handleNavegacaoMeusEventos());
        homeView.getBtnProgramacao().setOnAction(e -> handleNavegacaoProgramacao());
        homeView.getBtnConfiguracoes().setOnAction(e -> handleNavegacaoConfiguracoes());
        homeView.getBtnSair().setOnAction(e -> handleSair());
        homeView.getBtnCriarEvento().setOnAction(e -> handleCriarEvento());
        homeView.getBtnFiltros().setOnAction(e -> handleFiltros());
    }

    /**
     * Neste método é manipulado o clique no botão "Meus eventos", navegando para a tela correspondente.
     */
    private void handleNavegacaoMeusEventos() {
        System.out.println("HomeController: botão de Meus eventos clicado");
        try {
            System.out.println("HomeController: Navegação para Meus Eventos - implementar quando a tela for criada");
        } catch (Exception ex) {
            System.err.println("HomeController: Erro ao navegar para Meus Eventos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Programação", navegando para a tela de programação.
     */
    private void handleNavegacaoProgramacao() {
        System.out.println("HomeController: botão de Programação clicado");
        try {
            UserScheduleView userScheduleView = new UserScheduleView();
            UserScheduleController userScheduleController = new UserScheduleController(userScheduleView, primaryStage);
            userScheduleView.setUserScheduleController(userScheduleController);

            TelaService service = new TelaService();
            Scene sceneUserSchedule = new Scene(userScheduleView, service.medirWidth(), service.medirHeight());

            sceneUserSchedule.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());
            primaryStage.setTitle("Eventually - Programação do Usuário");
            primaryStage.setScene(sceneUserSchedule);
        } catch (Exception ex) {
            System.err.println("HomeController: Erro ao navegar para a Programação: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Configurações", navegando para a tela correspondente.
     */
    private void handleNavegacaoConfiguracoes() {
        System.out.println("Controller: Configurações clicado");

        SettingsView settingsView = new SettingsView();
        SettingsController settingsController = new SettingsController(settingsView, primaryStage);
        settingsView.setSettingsController(settingsController);

        TelaService service = new TelaService();
        Scene sceneSettings = new Scene(settingsView,service.medirWidth(),service.medirHeight());

        sceneSettings.getStylesheets().add(getClass().getResource("/styles/settings-styles.css").toExternalForm());
        primaryStage.setTitle("Eventually - Configuração do Usuário");
        primaryStage.setScene(sceneSettings);
    }

    /**
     * Neste método é manipulado o clique no botão "Sair", retornando para a tela de login.
     */
    private void handleSair() {
        System.out.println("HomeController: botão de Sair clicado");
        try {
            LoginView loginView = new LoginView();

            LoginController loginController = new LoginController(loginView, primaryStage);
            loginView.setLoginController(loginController);

            TelaService service = new TelaService();
            Scene loginScene = new Scene(loginView, service.medirWidth(), service.medirHeight());

            loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
            primaryStage.setTitle("Eventually - Login");
            primaryStage.setScene(loginScene);
        } catch (Exception ex) {
            System.err.println("HomeController: Erro ao fazer logout: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Criar evento", navegando para a tela de criação de eventos.
     */
    private void handleCriarEvento() {
        System.out.println("HomeController: botão de Criar evento clicado");
        try {
            System.out.println("HomeController: Navegação para Criar Evento - implementar quando a tela for criada");
        } catch (Exception ex) {
            System.err.println("HomeController: Erro ao navegar para Criar Evento: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Filtros", abrindo o painel de filtros.
     */
    private void handleFiltros() {
        System.out.println("HomeController: botão de Filtros clicado");
        try {
            System.out.println("HomeController: Abertura de filtros - implementar lógica de filtros");
        } catch (Exception ex) {
            System.err.println("HomeController: Erro ao abrir filtros: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Este método carrega os eventos da página inicial através do service.
     */
    public void carregarEventos() {
        try {
            // TODO: Implementar carregamento real de eventos via EventoService
            System.out.println("HomeController: Carregando eventos da página inicial");
        } catch (Exception ex) {
            System.err.println("HomeController: Erro ao carregar eventos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Este método atualiza a saudação baseada no horário atual.
     */
    public void atualizarSaudacao() {
        try {
            java.time.LocalTime agora = java.time.LocalTime.now();
            String saudacao;

            if (agora.isBefore(java.time.LocalTime.of(12, 0))) {
                saudacao = "Bom dia, usuário"; // commit feito aqui, mas ainda é preciso colocar o label do usuário
            } else if (agora.isBefore(java.time.LocalTime.of(18, 0))) {
                saudacao = "Boa tarde, usuário";
            } else {
                saudacao = "Boa noite, usuário";
            }

            homeView.getLbBoaTarde().setText(saudacao);
        } catch (Exception ex) {
            System.err.println("HomeController: Erro ao atualizar saudação: " + ex.getMessage());
        }
    }
}