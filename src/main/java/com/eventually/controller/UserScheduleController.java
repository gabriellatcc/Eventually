package com.eventually.controller;

import com.eventually.view.UserScheduleView;

/**
 * Esta classe {@code UserScheduleController} é responsável por gerenciar as interações da interface principal da aplicação
 * Eventually, ela conecta os botões da {@link UserScheduleView} às ações correspondentes, garantindo a
 * separação entre a lógica de interface e a lógica de negócio.
 * @author Gabriella Tavares Costa Corrêa
 * @since 2025-04-25 (Construção da documentação da classe e revisão)
 */
public class UserScheduleController {

    private final UserScheduleView view;

    public UserScheduleController(UserScheduleView view) {
        this.view = view;
        this.view.setController(this);
        setupEventHandlers();
    }

    public void setupEventHandlers() {
        view.getBtnInicio().setOnAction(e -> handleHomeButton());
        view.getBtnMeusEventos().setOnAction(e -> handleMyEventsButton());
        view.getBtnConfiguracoes().setOnAction(e -> handleSettingsButton());

        view.getBtnProgramacao().setOnAction(e -> handleProgramacaoButton());
        view.getBtnAgenda().setOnAction(e -> handleAgendaButton());
        view.getBtnNovoEvento().setOnAction(e -> abrirNovoModal());

        // Configurar evento para seleção de data
        view.getGrupoDatas().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                handleDateSelection(newToggle);
            }
        });
    }


    private void handleDateSelection(javafx.scene.control.Toggle toggle) {System.out.println("Controller: Data selecionada: " + toggle.getUserData());}


    private void handleHomeButton() {
        System.out.println("Controller: Página inicial clicada");
    }


    private void handleMyEventsButton() {
        System.out.println("Controller: Meus eventos clicado");
    }


    private void handleSettingsButton() {
        System.out.println("Controller: Configurações clicado");
    }


    private void handleProgramacaoButton() {System.out.println("Controller: Programação clicado");}


    private void handleAgendaButton() {System.out.println("Controller: Minha agenda clicado");}


    private void abrirNovoModal() {System.out.println("Controller: Novo Modal clicado");}
}