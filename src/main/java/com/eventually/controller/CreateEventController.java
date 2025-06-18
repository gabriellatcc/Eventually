package com.eventually.controller;

import com.eventually.service.EventService;
import com.eventually.view.CreateEventModal;
import javafx.scene.control.Alert;
import javafx.stage.Window;
import java.time.LocalDate;

/**
 * Classe responsável pela comunicação do modal de "Criar evento" com o backend.
 *
 * @author Yuri Garcia Maia
 * @version 1.0
 * @since 2025-06-18
 */
public class CreateEventController {

    private final CreateEventModal view;
    private final EventService eventService;

    /**
     * Construtor do CreateEventController.
     * @param view A instância da CreateEventModal associada.
     * @param eventService O serviço para interagir com os dados de eventos.
     */
    public CreateEventController(CreateEventModal view, EventService eventService) {
        this.view = view;
        this.eventService = eventService;
    }

    /**
     * Exibe um Alert de erro simples.
     * @param title O título da janela de alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (view != null && view.getModalScene() != null) {
            Window window = view.getModalScene().getWindow();
            if (window != null && window.isShowing()) {
                alert.initOwner(window);
            }
        }
        alert.showAndWait();
    }

    /**
     * Exibe um Alert de informação simples.
     * @param title O título da janela de alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (view != null && view.getModalScene() != null) {
            Window window = view.getModalScene().getWindow();
            if (window != null && window.isShowing()) {
                alert.initOwner(window);
            }
        }
        alert.showAndWait();
    }

    public void handleCreateEventRequest() {
        if (view == null || eventService == null) {
            System.err.println("CreateEventController: View ou Service não configurado.");
            showAlert("Erro Crítico", "Ocorreu um erro interno. Controller não inicializado corretamente.");
            return;
        }
        String eventName = view.getEventName().getText();
        String description = view.getDescription().getText();
        String formato = view.getFormato();
        int capacity = view.getParticipantCount();
        LocalDate startDate = view.getStartDate().getValue();
        LocalDate endDate = view.getEndDate().getValue();
        String startTime = view.getStartTime().getText();
        String endTime = view.getEndTime().getText();

        if (eventName.isEmpty() || description.isEmpty() || formato.isEmpty()) {
            showAlert("Erro de Validação", "Nome, descrição e formato do evento são obrigatórios.");
            return;
        }

        if (capacity <= 0) {
            showAlert("Erro de Validação", "A capacidade de participantes deve ser maior que zero.");
            return;
        }

        if (startDate == null || endDate == null || startTime.isEmpty() || endTime.isEmpty()) {
            showAlert("Erro de Validação", "As datas e horários de início e fim são obrigatórios.");
            return;
        }

        if (endDate.isBefore(startDate)) {
            showAlert("Erro de Validação", "A data de término não pode ser anterior à data de início.");
            return;
        }

        try {
            boolean success = eventService.createEvent(
                    eventName, description, formato, capacity, startDate, startTime, endDate, endTime
            );

            if (success) {
                showInfo("Sucesso", "Evento criado com sucesso!");
                view.close();
            } else {
                showAlert("Erro ao Criar Evento", "Não foi possível criar o evento. Verifique os dados ou tente novamente.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro de Sistema", "Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
}
