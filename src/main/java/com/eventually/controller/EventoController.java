package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.model.UsuarioModel;
import com.eventually.service.*;
import com.eventually.view.HomeView;
import com.eventually.view.modal.EventoModal;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Set;

/**
 * Controller para o modal de Inscrição/Visualização de Evento.
 * Gerencia as interações do usuário com o modal.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e parte lógica)
 * @version 1.09
 * @since 2025-06-27
 */
public class EventoController {
    private final EventoModal view;
    private HomeView.EventoH eventoH;

    private UsuarioModel usuarioLogado;

    private UsuarioAtualizacaoService usuarioAtualizacaoService;
    private UsuarioSessaoService usuarioSessaoService;
    private EventoExclusaoService eventoExclusaoService;
    private EventoLeituraService eventoLeituraService;

    private Runnable aoFecharCallback;

    private EventoEdicaoService eventoEdicaoService;
    private NavegacaoService navegacaoService;

    private AlertaService alertaService =new AlertaService();

    private String email;

    private boolean usuarioEstaInscrito;

    /**
     * Construtor que associa a View (o modal) com este Controller.
     * @param view a instância de EventoModal que este controller gerenciará.
     * @param eventoH o evento a ser exibido
     */
    public EventoController(String email, EventoModal view, HomeView.EventoH eventoH, Stage primaryStage, Runnable aoFecharCallback) {
        this.view = view;
        this.eventoH = eventoH;

        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        this.usuarioAtualizacaoService = UsuarioAtualizacaoService.getInstancia();
        this.eventoEdicaoService = EventoEdicaoService.getInstance();
        this.eventoExclusaoService=EventoExclusaoService.getInstancia();
        this.eventoLeituraService=EventoLeituraService.getInstancia();
        this.navegacaoService = new NavegacaoService(primaryStage);

        this.email=email;
        this.aoFecharCallback = aoFecharCallback;

        initialize();
    }

    /**
     * Inicializa os listeners e as ações dos componentes da view.
     */
    private void initialize() {
        this.usuarioLogado = usuarioSessaoService.procurarUsuario(email);

        if (this.usuarioLogado == null) {
            System.err.println("Erro crítico: usuário não encontrado no EventoController.");
            view.close();
            return;
        }

        this.usuarioEstaInscrito = this.usuarioLogado.getEventosInscrito().stream()
                .anyMatch(eventoModel -> eventoModel.getId() == eventoH.id());

        view.getLblTituloEvento().setText(eventoH.titulo());
        view.getLblDataHoraInicio().setText(eventoH.dataHoraInicio());
        view.getLblDataHoraFim().setText(eventoH.dataHoraFim());
        view.getLblDescricao().setText(eventoH.descricao());
        view.getLblParticipantesInscritos().setText(eventoH.inscritos()+ " participantes inscritos");

        eventoLeituraService.procurarEventoPorId(eventoH.id()).ifPresent(eventoReal -> {
            atualizarContagemDeVagas(eventoReal);
        });

        view.getImgTopoEvento().setImage(eventoH.imagem());
        view.getLblLocalizacao().setText(eventoH.local());
        view.getLblFormato().setText(eventoH.formato());
        Set<String> tags = eventoH.preferencias();
        for (String nomeTag : tags) {
            Label tagLabel = new Label(nomeTag);
            tagLabel.getStyleClass().add("tag-label");
            view.getFlowPaneTags().getChildren().add(tagLabel);
        }
        
        atualizarVisualizacao();

        view.getBtnSair().setOnAction(e -> {
            if (aoFecharCallback != null) {
                aoFecharCallback.run();
            }
            view.close();
        });

        view.getBtnVerParticipantes().setOnAction(e -> {processarVerParticipantes(eventoH);});
        int id = eventoH.id();
        view.getBtnComentarios().setOnAction(e -> {navegacaoService.abrirModalComentarios(id,email);});
        view.getBtnCompartilhar().setOnAction(e -> {navegacaoService.abrirModalDeCompartilhamento(eventoH);});
    }


    /**
     * MÉTODO CENTRALIZADO: Atualiza a visualização do modal (botões e localização)
     * com base no estado de inscrição do usuário e se ele é o criador do evento.
     */
    private void atualizarVisualizacao() {
        VBox containerDeBotoes = view.getVbBotoesAcao();
        containerDeBotoes.getChildren().clear();

        boolean ehCriador = this.usuarioLogado.getEventosOrganizados().stream()
                .anyMatch(eventoModel -> eventoModel.getId() == eventoH.id());

        if (ehCriador) {
            Button btnEditar = view.getBtnEditar();
            Button btnExcluir = view.getBtnExcluir();
            btnEditar.setOnAction(e -> processarEditar());
            btnExcluir.setOnAction(e -> processarExcluir());
            HBox hboxOrganizerButtons = new HBox(10, btnEditar, btnExcluir);
            hboxOrganizerButtons.setAlignment(Pos.CENTER);
            containerDeBotoes.getChildren().add(hboxOrganizerButtons);
        } else if (this.usuarioEstaInscrito) {
            Button btnCancelar = view.getBtnCancelarInscricao();
            btnCancelar.setOnAction(e -> processarCancelarInscricao());
            containerDeBotoes.getChildren().add(btnCancelar);
        } else {
            Button btnInscrever = view.getBtnInscrever();
            btnInscrever.setOnAction(e -> processarInscricao());
            HBox hboxInscrever = new HBox(btnInscrever);
            hboxInscrever.setAlignment(Pos.CENTER);
            containerDeBotoes.getChildren().add(hboxInscrever);
        }

        if (eventoH.formato().equalsIgnoreCase("Online")) {
            if (usuarioEstaInscrito || ehCriador) {
                view.getLblLocalizacao().setText(eventoH.linkAcesso());
            } else {
                view.getLblLocalizacao().setText("Evento Online");
            }
        } else {
            view.getLblLocalizacao().setText(eventoH.local());
        }
    }

    private void processarEditar() {
       navegacaoService.abrirModalEdicao(eventoH,aoFecharCallback);
       view.close();
    }

    private void processarExcluir() {
        boolean usuarioConfirmou = navegacaoService.abrirModalConfimarExclusao();

        if (usuarioConfirmou) {

            eventoExclusaoService.alterarEstadoDoEvento(eventoH.id(), false);

            alertaService.alertarInfo("Evento excluído com sucesso!");

            if (aoFecharCallback != null) {
                aoFecharCallback.run();
            }

            view.close();
        }
    }

    private void processarVerParticipantes(HomeView.EventoH evento) {
        navegacaoService.abrirModalParticipantes(evento);
    }

    /**
     * Atualiza o estado de inscrição do usuário e reconfigura os botões de ação na view.
     * @param novoEstado true se o usuário agora está inscrito, false caso contrário.
     */
    private void atualizarEstadoBotoes(boolean novoEstado) {
        this.usuarioEstaInscrito = novoEstado;
    }

    private void processarInscricao() {
        usuarioAtualizacaoService.atualizarEventoParticipado(email, eventoH);

        eventoEdicaoService.adicionarParticipante(eventoH,email);

        alertaService.alertarInfo("Você está inscrito com sucesso!");

        this.usuarioLogado = usuarioSessaoService.procurarUsuario(email);
        this.usuarioEstaInscrito = this.usuarioLogado.getEventosInscrito().stream()
                .anyMatch(eventoModel -> eventoModel.getId() == eventoH.id());

        eventoLeituraService.procurarEventoPorId(eventoH.id()).ifPresent(this::atualizarContagemDeVagas);


        atualizarEstadoBotoes(true);
        atualizarVisualizacao();

    }

    private void processarCancelarInscricao() {
        boolean usuarioConfirmou = navegacaoService.abrirModalCancInscricao();

        if (usuarioConfirmou) {
            usuarioAtualizacaoService.removerInscricao(email, eventoH);
            eventoEdicaoService.removerParticipante(eventoH, email);
            alertaService.alertarInfo("Sua inscrição foi cancelada.");

            this.usuarioLogado = usuarioSessaoService.procurarUsuario(email);
            this.usuarioEstaInscrito = this.usuarioLogado.getEventosInscrito().stream()
                    .anyMatch(eventoModel -> eventoModel.getId() == eventoH.id());

            eventoLeituraService.procurarEventoPorId(eventoH.id()).ifPresent(this::atualizarContagemDeVagas);

            atualizarVisualizacao();
        }
    }

    /**
     * Atualiza os labels da UI com os dados mais recentes de um EventoModel.
     * @param eventoAtualizado O objeto EventoModel com os dados frescos do serviço.
     */
    private void atualizarContagemDeVagas(EventoModel eventoAtualizado) {
        int capacidade = eventoAtualizado.getnParticipantes();
        int inscritosAtuais = eventoAtualizado.getParticipantes().size();
        int vagasRestantes = capacidade - inscritosAtuais;

        if (vagasRestantes < 0) {
            vagasRestantes = 0;
        }

        view.getLblParticipantesInscritos().setText(inscritosAtuais + " participantes inscritos");
        view.getLblVagasDisponiveis().setText(vagasRestantes + " de " + capacidade + " vagas disponíveis");
    }

}