package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.service.*;
import com.eventually.view.HomeView;
import com.eventually.view.modal.EventoModal;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;

/**
 * Controller para o modal de Inscrição/Visualização de Evento.
 * Gerencia as interações do usuário com o modal.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e parte lógica)
 * @version 1.03
 * @since 2025-06-27
 */
public class EventoController {
    private final EventoModal view;
    private HomeView.EventoH eventoH;

    private List<EventoModel> eventosCriadosPeloUsuario;
    private List<EventoModel> eventosInscritosPeloUsuario;

    private UsuarioAtualizacaoService usuarioAtualizacaoService;
    private EditaEventoService editaEventoService;
    private NavegacaoService navegacaoService;

    private AlertaService alertaService =new AlertaService();

    private String email;

    /**
     * Construtor que associa a View (o modal) com este Controller.
     * @param view a instância de EventoModal que este controller gerenciará.
     * @param eventoH o evento a ser exibido
     */
    public EventoController(String email, EventoModal view, HomeView.EventoH eventoH,List<EventoModel> eventosCriados,
                            List<EventoModel> eventosInscritos, Stage primaryStage) {
        this.view = view;
        this.eventoH = eventoH;
        this.eventosCriadosPeloUsuario = eventosCriados;
        this.eventosInscritosPeloUsuario = eventosInscritos;

        this.usuarioAtualizacaoService = UsuarioAtualizacaoService.getInstancia();
        this.editaEventoService = EditaEventoService.getInstance();

        this.email=email;
        this.navegacaoService = new NavegacaoService(primaryStage);

        initialize();
    }

    /**
     * Inicializa os listeners e as ações dos componentes da view.
     */
    private void initialize() {
        view.getLblTituloEvento().setText(eventoH.titulo());
        view.getLblDataHoraInicio().setText(eventoH.dataHoraInicio());
        view.getLblDataHoraFim().setText(eventoH.dataHoraFim());
        view.getLblDescricao().setText(eventoH.descricao());
        view.getLblParticipantesInscritos().setText(eventoH.inscritos()+ " participantes inscritos");
        int sobras = eventoH.inscritos()-eventoH.capacidade();
        if(sobras<0){
            sobras = 0;
        }
        view.getLblVagasDisponiveis().setText(sobras+" de "+eventoH.capacidade()+" vagas disponíveis");
        view.getImgTopoEvento().setImage(eventoH.imagem());
        view.getLblLocalizacao().setText(eventoH.local());
        view.getLblFormato().setText(eventoH.formato());
        Set<String> tags = eventoH.preferencias();
        for (String nomeTag : tags) {
            Label tagLabel = new Label(nomeTag);
            tagLabel.getStyleClass().add("tag-label");
            view.getFlowPaneTags().getChildren().add(tagLabel);
        }
        
        configurarBotoesDeAcao();
        view.getBtnSair().setOnAction(e -> view.close());
        view.getBtnVerParticipantes().setOnAction(e -> {processarVerParticipantes(eventoH);});
        int id = eventoH.id();
        view.getBtnComentarios().setOnAction(e -> {navegacaoService.abrirModalComentarios(id,email);});
        view.getBtnCompartilhar().setOnAction(e -> {navegacaoService.abrirModalDeCompartilhamento(eventoH);});
    }

    private void configurarBotoesDeAcao() {
        VBox containerDeBotoes = view.getVbBotoesAcao();
        containerDeBotoes.getChildren().clear();

        boolean ehCriador = eventosCriadosPeloUsuario.stream()
                .anyMatch(eventoModel -> eventoModel.getId() == eventoH.id());

        boolean estaInscrito = eventosInscritosPeloUsuario.stream()
                .anyMatch(eventoModel -> eventoModel.getId() == eventoH.id());

        if (ehCriador) {
            Button btnEditar = view.getBtnEditar();
            Button btnExcluir = view.getBtnExcluir();

            btnEditar.setOnAction(e -> processarEditar());
            btnExcluir.setOnAction(e -> processarExcluir());

            HBox hboxOrganizerButtons = new HBox(10, btnEditar, btnExcluir);
            hboxOrganizerButtons.setAlignment(Pos.CENTER);

            containerDeBotoes.getChildren().add(hboxOrganizerButtons);

        } else if (estaInscrito) {
            Button btnCancelar = view.getBtnCancelarInscricao();
            Button btnVer = view.getBtnVerParticipantes();

            btnCancelar.setOnAction(e -> processarCancelarInscricao());
            btnVer.setOnAction(e -> processarVerParticipantes(eventoH));

            containerDeBotoes.getChildren().addAll(btnCancelar, btnVer);

        } else {
            Button btnInscrever = view.getBtnInscrever();
            btnInscrever.setOnAction(e -> processarInscricao());
            HBox hboxInscrever = new HBox(btnInscrever);
            hboxInscrever.setAlignment(Pos.CENTER);
            containerDeBotoes.getChildren().add(hboxInscrever);
        }
    }

    private void processarEditar() {
       navegacaoService.abrirModalEdicao(eventoH);
       view.close();
    }
    private void processarExcluir() {
        navegacaoService.abrirModalConfimarExclusao(eventoH);
        view.close();
    }
    private void processarCancelarInscricao() {
        navegacaoService.abrirModalCancInscricao(eventoH);
        view.close();
    }
    private void processarVerParticipantes(HomeView.EventoH evento) {
        navegacaoService.abrirModalParticipantes(evento);
    }

    private void processarInscricao() {
        usuarioAtualizacaoService.atualizarEventoParticipado(email, eventoH);
        editaEventoService.adicionarParticipante(eventoH,email);
        alertaService.alertarInfo("Você está inscrito com sucesso!");
        view.close();
    }

}