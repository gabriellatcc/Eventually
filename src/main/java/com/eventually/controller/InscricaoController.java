package com.eventually.controller;

import com.eventually.view.InscricaoModal;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Controller para o modal de Inscrição/Visualização de Evento.
 * Gerencia as interações do usuário com o modal.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e parte lógica)
 * @version 1.0
 * @since 2025-06-27
 */
public class InscricaoController {

    private final InscricaoModal view;

    /**
     * Construtor que associa a View (o modal) com este Controller.
     * @param view A instância de InscricaoModal que este controller gerenciará.
     */
    public InscricaoController(InscricaoModal view) {
        this.view = view;
        initialize();
    }

    /**
     * Inicializa os listeners e as ações dos componentes da view.
     */
    private void initialize() {
        view.getBtnSair().setOnAction(e -> handleSair());
        view.getBtnInscrever().setOnAction(e -> handleInscrever());
        view.getBtnVerParticipantes().setOnAction(e -> handleVerParticipantes());
    }

    /**
     * Ação executada ao clicar no botão "Sair".
     * Simplesmente fecha o modal.
     */
    private void handleSair() {
        view.close();
        System.out.println("Modal de inscrição fechado.");
    }

    /**
     * Ação executada ao clicar em "Inscreva-se".
     * TODO: Implementar a lógica de inscrição do usuário no evento.
     */
    private void handleInscrever() {
        System.out.println("Botão 'Inscreva-se' clicado. Implementar lógica.");

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Inscrição realizada com sucesso!", ButtonType.OK);
        alert.setHeaderText("Sucesso!");
        alert.showAndWait();;
    }

    /**
     * Ação executada ao clicar em "Ver Participantes".
     * TODO: Implementar a lógica para abrir uma nova janela ou painel com a lista de participantes.
     */
    private void handleVerParticipantes() {
        System.out.println("Botão 'Ver Participantes' clicado. Implementar lógica.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Funcionalidade ainda não implementada.", ButtonType.OK);
        alert.setHeaderText("Aviso");
        alert.showAndWait();
    }
}