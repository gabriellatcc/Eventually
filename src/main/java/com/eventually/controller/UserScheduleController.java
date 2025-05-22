package com.eventually.controller;

import com.eventually.service.TelaService;
import com.eventually.view.SettingsView;
import com.eventually.view.UserScheduleView;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Esta classe {@code SettingsController} é responsável por gerenciar as interações da interface principal da aplicação
 * Eventually, ela conecta os botões da {@link UserScheduleView} às ações correspondentes, garantindo a
 * separação entre a lógica de interface e a lógica de negócio.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.01
 * @since 2025-04-25 (Construção da documentação da classe e revisão)
 */
public class UserScheduleController {

    private final UserScheduleView userScheduleView;
    private final Stage primaryStage;

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

        userScheduleView.getBtnProgramacao().setOnAction(e -> handleProgramacaoButton());
        userScheduleView.getBtnAgenda().setOnAction(e -> handleAgendaButton());
        userScheduleView.getBtnNovoEvento().setOnAction(e -> abrirNovoModal());

        userScheduleView.getGrupoDatas().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                handleDateSelection(newToggle);
            }
        });
    }


    private void handleDateSelection(javafx.scene.control.Toggle toggle) {System.out.println("Controller: Data selecionada: " + toggle.getUserData());}


    private void handleHomeButton() {System.out.println("Controller: Página inicial clicada");}


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


    private void handleAgendaButton() {System.out.println("Controller: Minha agenda clicado");}


    private void abrirNovoModal() {System.out.println("Controller: Novo Modal clicado");}
}