package com.eventually.controller;

import com.eventually.view.HomeView;
import com.eventually.view.InscricaoModal;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Set;

/**
 * Controller para o modal de Inscrição/Visualização de Evento.
 * Gerencia as interações do usuário com o modal.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e parte lógica)
 * @version 1.01
 * @since 2025-06-27
 */
public class InscricaoController {
    private final InscricaoModal view;
    private HomeView.EventoH eventoH;

    /**
     * Construtor que associa a View (o modal) com este Controller.
     *
     * @param view    A instância de InscricaoModal que este controller gerenciará.
     * @param eventoH o evento a ser exibido
     */
    public InscricaoController(InscricaoModal view, HomeView.EventoH eventoH) {
        this.view = view;
        this.eventoH = eventoH;
        initialize();
    }

    /**
     * Inicializa os listeners e as ações dos componentes da view.
     */
    private void initialize() {
        //get titulo
        view.getLblTituloEvento().setText(eventoH.titulo());

        //get data
        view.getLblDataHoraInicio().setText(eventoH.dataHoraInicio());
        view.getLblDataHoraFim().setText(eventoH.dataHoraFim());

        //get descricao
        view.getLblDescricao().setText(eventoH.descricao());

        //get inscritos
        view.getLblParticipantesInscritos().setText(eventoH.inscritos()+ "participantes inscritos");

        //get n participantes
        int sobras = eventoH.inscritos()-eventoH.capacidade();
        view.getLblVagasDisponiveis().setText(sobras+" de "+eventoH.capacidade()+" vagas disponíveis");

        // get imagem
        view.getImgTopoEvento().setImage(eventoH.imagem());

        //get localizacao
        view.getLblLocalizacao().setText(eventoH.local());

        // get foramto
        view.getLblFormato().setText(eventoH.formato());

        // get preferencias
        Set<String> tags = eventoH.preferencias();
        for (String nomeTag : tags) {
            Label tagLabel = new Label(nomeTag);
            tagLabel.getStyleClass().add("tag-label");
            view.getFlowPaneTags().getChildren().add(tagLabel);
        }

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
        view.close();
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