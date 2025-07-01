package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.view.HomeView;
import com.eventually.view.EventoModal;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Set;

/**
 * Controller para o modal de Inscrição/Visualização de Evento.
 * Gerencia as interações do usuário com o modal.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e parte lógica)
 * @version 1.01
 * @since 2025-06-27
 */
public class EventoController {
    private final EventoModal view;
    private HomeView.EventoH eventoH;
    private List<EventoModel> eventosCriadosPeloUsuario;
    private List<EventoModel> eventosInscritosPeloUsuario;

    /**
     * Construtor que associa a View (o modal) com este Controller.
     * @param view a instância de EventoModal que este controller gerenciará.
     * @param eventoH o evento a ser exibido
     */
    public EventoController(EventoModal view, HomeView.EventoH eventoH,List<EventoModel> eventosCriados, List<EventoModel> eventosInscritos) {
        this.view = view;
        this.eventoH = eventoH;
        this.eventosCriadosPeloUsuario = eventosCriados;
        this.eventosInscritosPeloUsuario = eventosInscritos;
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
        view.getLblParticipantesInscritos().setText(eventoH.inscritos()+ " participantes inscritos");

        //get n participantes
        int sobras = eventoH.inscritos()-eventoH.capacidade();
        if(sobras<0){
            sobras = 0;
        }
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
        
        configurarBotoesDeAcao();
        view.getBtnSair().setOnAction(e -> view.close());
    }

    private void configurarBotoesDeAcao() {
        VBox containerDeBotoes = view.getVbBotoesAcao();
        containerDeBotoes.getChildren().clear();

        if (eventosCriadosPeloUsuario.contains(eventoH)) {
            Button btnEditar = view.getBtnEditar();
            Button btnExcluir = view.getBtnExcluir();

  //          btnEditar.setOnAction(e -> handleEditar());
    //        btnExcluir.setOnAction(e -> handleExcluir());

            HBox hboxOrganizerButtons = new HBox(10, btnEditar, btnExcluir);
            hboxOrganizerButtons.setAlignment(Pos.CENTER);

            containerDeBotoes.getChildren().add(hboxOrganizerButtons);

        } else if (eventosInscritosPeloUsuario.contains(eventoH)) {
            Button btnCancelar = view.getBtnCancelarInscricao();
            Button btnVer = view.getBtnVerParticipantes();

      //      btnCancelar.setOnAction(e -> handleCancelarInscricao());
        //    btnVer.setOnAction(e -> handleVerParticipantes());

            containerDeBotoes.getChildren().addAll(btnCancelar, btnVer);

        } else {
            Button btnInscrever = view.getBtnInscrever();
          //  btnInscrever.setOnAction(e -> handleInscrever());

            containerDeBotoes.getChildren().add(btnInscrever);
        }
    }
/*
    private void handleEditar() {
       navegacaoService.abrirModalEdicao();
    }
    private void handleExcluir() {
        navegacaoService.abrirModalConfimarExclusao();
    }
    private void handleCancelarInscricao() {
        navegacaoService.abrirModalCancInscricao();
    }
    private void handleVerParticipantes() {
        navegacaoService.abrirModalParticipantes();
    }
    private void handleInscrever() {
        //se inscrever e alert info

    }
    */
}