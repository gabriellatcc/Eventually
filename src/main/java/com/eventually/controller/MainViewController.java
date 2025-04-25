package com.eventually.controller;

import com.eventually.view.MainView;

/**
 * Esta classe {@code MainViewController} é responsável por gerenciar as interações da interface principal da aplicação
 * Eventually, ela conecta os botões da {@link com.eventually.view.MainView} às ações correspondentes, garantindo a
 * separação entre a lógica de interface e a lógica de negócio.
 * @author Gabriella Tavares Costa Corrêa
 * @since 2025-04-25 (Construção da base e da dcumentação da classe)
 */
public class MainViewController {

    private final MainView view;

    public MainViewController(MainView view) {
        this.view = view;
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        view.getHomeButton().setOnAction(e -> handleHomeButton());
        view.getMyEventsButton().setOnAction(e -> handleMyEventsButton());
        view.getSettingsButton().setOnAction(e -> handleSettingsButton());
    }

    private void handleHomeButton() {
        System.out.println("Controller: Página inicial clicada");
    }

    private void handleMyEventsButton() {
        System.out.println("Controller: Meus eventos clicado");
    }

    private void handleSettingsButton() {
        System.out.println("Controller: Configurações clicado");
    }
}