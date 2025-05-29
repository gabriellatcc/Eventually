package com.eventually.controller;

import com.eventually.service.TelaService;
import com.eventually.view.HomeView;
import com.eventually.view.LoginView;
import com.eventually.view.SettingsView;
import com.eventually.view.UserScheduleView;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Classe controller da tela de programação do usuário.
 * Esta classe é responsável pela comunicação
 * da tela de programação com o backend.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.01
 * @since 2025-04-25 (Construção da documentação da classe e revisão da estrutura)
 */
public class UserScheduleController {

    private final UserScheduleView userScheduleView;
    private final Stage primaryStage;

    /**
     * Construtor do {@code UserScheduleController}, inicializa a view de programação de usuário.
     * @param userScheduleView a interface de programação associada
     * @param primaryStage o palco principal da aplicação
     */
    public UserScheduleController(UserScheduleView userScheduleView, Stage primaryStage) {
        this.userScheduleView = userScheduleView;
        this.primaryStage = primaryStage;
        this.userScheduleView.setUserScheduleController(this);
        setupEventHandlersUserSchedule();
    }

    public void setupEventHandlersUserSchedule() {
        userScheduleView.getBtnInicio().setOnAction(e -> handleHomeButton());
        userScheduleView.getBtnMeusEventos().setOnAction(e -> handleMyEventsButton());
        userScheduleView.getBtnConfiguracoes().setOnAction(e -> handleSettingsButton());
        userScheduleView.getBtnSair().setOnAction(e -> handleSairButton());


        userScheduleView.getBtnProgramacao().setOnAction(e -> handleProgramacaoButton());
        userScheduleView.getBtnNovoEvento().setOnAction(e -> abrirNovoModal());

        userScheduleView.getGrupoDatas().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                handleDateSelection(newToggle);
            }
        });
    }

    private void handleSairButton()
    {
        abrirModalEscerrrarSessão();
        System.out.println("USController: botão de sair clicado");
        LoginView loginView = new LoginView();
        LoginController loginController = new LoginController(loginView, primaryStage);
        loginView.setLoginController(loginController);

        TelaService service = new TelaService();
        Scene loginScene = new Scene(loginView,service.medirWidth(),service.medirHeight());

        loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
        primaryStage.setTitle("Eventually - Login");
        primaryStage.setScene(loginScene);
    }

    private void abrirModalEscerrrarSessão() {
        System.out.println("USController: modal escerrar sessão clicado");
    }

    private void handleDateSelection(javafx.scene.control.Toggle toggle) {System.out.println("Controller: Data selecionada: " + toggle.getUserData());}


    private void handleHomeButton() {
        System.out.println("Controller: Página inicial clicada");
        HomeView homeView = new HomeView();
        HomeController homeController = new HomeController(homeView, primaryStage);
        homeView.setHomeController(homeController);

        TelaService service = new TelaService();
        Scene sceneHome = new Scene(homeView,service.medirWidth(),service.medirHeight());

        sceneHome.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());
        primaryStage.setTitle("Eventually - Página inicial");
        primaryStage.setScene(sceneHome);
    }


    private void handleMyEventsButton() {
        System.out.println("Controller: Meus eventos clicado");
    }


    private void handleSettingsButton() {
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


    private void handleProgramacaoButton() {System.out.println("Controller: Programação clicado");}

    private void abrirNovoModal() {System.out.println("Controller: Novo Modal clicado");}
}