package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.UsuarioModel;
import com.eventually.service.AlertaService;
import com.eventually.service.NavegacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe controladora da tela de eventos participados e criados pelo usuário, é responsável pela comunicação
 * com o backend.
 * Contém métodos privados para que os acesso sejam somente por esta classe.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.08
 * @since 2025-06-18
 */

public class MyEventsController {
    private final MyEventsView myEventsView;
    private final Stage primaryStage;
    private final NavegacaoService navegacaoService;
    private final UsuarioSessaoService usuarioSessaoService;
    private final String emailRecebido;
    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(MyEventsController.class);

    public MyEventsController(String email, MyEventsView myEventsView, Stage primaryStage) {
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        this.emailRecebido = email;
        this.myEventsView = myEventsView;
        this.myEventsView.setMyEventsViewController(this);
        this.primaryStage = primaryStage;
        this.navegacaoService = new NavegacaoService(primaryStage);

        configManipuladoresDeEventoMeusEventos();
        configurarSeletorEventos();
    }

    private void configManipuladoresDeEventoMeusEventos() {
        myEventsView.getBarraBuilder().getBtnMeusEventos().setDisable(true);
        myEventsView.getBarraBuilder().getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
        myEventsView.getBarraBuilder().getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));
        myEventsView.getBarraBuilder().getBtnProgramacao().setOnAction(e -> navegacaoService.navegarParaProgramacao(emailRecebido));
        myEventsView.getBarraBuilder().getBtnSair().setOnAction(e -> navegacaoService.abrirModalEncerrarSessao());

        myEventsView.setNomeUsuario(usuarioSessaoService.procurarNome(emailRecebido));
        myEventsView.setEmailUsuario(emailRecebido);
        myEventsView.setAvatar(usuarioSessaoService.procurarImagem(emailRecebido));
    }

    private void configurarSeletorEventos() {
        ToggleGroup grupoTipoEvento = myEventsView.getGroupFiltroEventos();
        if (grupoTipoEvento == null) return;

        grupoTipoEvento.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == myEventsView.getBtnEventosCriados()) {
                carregarEventosParaOrganizador();
            } else if (newToggle == myEventsView.getBtnInscricoes()) {
                carregarEventosParaInscrito();
            } else if (newToggle == myEventsView.getBtnEventosFinalizados()) {
                carregarEventosFinalizados();
            }
        });

        myEventsView.getBtnEventosCriados().setSelected(true);

        carregarEventosParaOrganizador();
    }

    private void carregarEventosParaOrganizador() {
        sistemaDeLogger.info("Carregando eventos criados pelo organizador...");
        List<EventoModel> eventosCriados = usuarioSessaoService.procurarEventosCriados(emailRecebido);
        List<EventoModel> eventosFuturos = eventosCriados.stream()
                .filter(e -> e.getDataFinal().isAfter(LocalDate.now().minusDays(1)))
                .collect(Collectors.toList());
        exibirListaDeEventos(eventosFuturos, this::carregarEventosParaOrganizador);
    }

    private void carregarEventosParaInscrito() {
        sistemaDeLogger.info("Carregando eventos em que o usuário está inscrito...");
        List<EventoModel> eventosInscritos = usuarioSessaoService.procurarEventosInscritos(emailRecebido);
        List<EventoModel> eventosFuturos = eventosInscritos.stream()
                .filter(e -> e.getDataFinal().isAfter(LocalDate.now().minusDays(1)))
                .collect(Collectors.toList());
        exibirListaDeEventos(eventosFuturos, this::carregarEventosParaInscrito);
    }

    private void carregarEventosFinalizados() {
        sistemaDeLogger.info("Carregando eventos finalizados...");
        List<EventoModel> todosOsEventos = new ArrayList<>();
        todosOsEventos.addAll(usuarioSessaoService.procurarEventosCriados(emailRecebido));
        todosOsEventos.addAll(usuarioSessaoService.procurarEventosInscritos(emailRecebido));

        List<EventoModel> eventosPassados = todosOsEventos.stream()
                .distinct()
                .filter(e -> e.getDataFinal().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
        exibirListaDeEventos(eventosPassados, this::carregarEventosFinalizados);
    }

    /**
     * MÉTODO CENTRALIZADO: Responsável por renderizar qualquer lista de eventos na tela.
     * @param listaDeEventos A lista de EventoModel a ser exibida.
     * @param refreshCallback A ação a ser executada para recarregar a lista atual.
     */
    private void exibirListaDeEventos(List<EventoModel> listaDeEventos, Runnable refreshCallback) {
        myEventsView.getListaEventos().getChildren().clear();

        if (listaDeEventos == null || listaDeEventos.isEmpty()) {
            Label placeholder = new Label("Nenhum evento encontrado para esta categoria.");
            placeholder.getStyleClass().add("placeholder-label");
            myEventsView.getListaEventos().getChildren().add(placeholder);
            return;
        }

        for (EventoModel evento : listaDeEventos) {
            EventoMECartao cartao = new EventoMECartao();

            cartao.setLblTitulo(evento.getNome());
            cartao.setLblLocal(evento.getLocalizacao());
            String textoCapacidade = evento.getParticipantes().size() + "/" + evento.getnParticipantes();
            cartao.setLblCapacidadeValor(textoCapacidade);
            configurarDataDoCartao(cartao, evento);

            Button botaoVer = cartao.getBtnVer();
            botaoVer.setOnAction(e -> {
                HomeView.EventoH eventoH = converterParaEventoH(evento);
                navegacaoService.abrirModalVerEvento(this.emailRecebido, eventoH, refreshCallback);
            });

            myEventsView.getListaEventos().getChildren().add(cartao);
        }
    }

    private void configurarDataDoCartao(EventoMECartao cartao, EventoModel evento) {
        Locale brasil = new Locale("pt", "BR");
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("EEE dd, MMM uuuu", brasil);
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        if (evento.getDataInicial().equals(evento.getDataFinal())) {
            String dataFormatada = evento.getDataInicial().format(formatoData);
            String horarioCompleto = evento.getHoraInicial().format(formatoHora) + " - " + evento.getHoraFinal().format(formatoHora);
            cartao.setDataUnica(dataFormatada, horarioCompleto);
        } else {
            String dataHoraInicio = evento.getDataInicial().format(formatoData) + " " + evento.getHoraInicial().format(formatoHora);
            String dataHoraFim = evento.getDataFinal().format(formatoData) + " " + evento.getHoraFinal().format(formatoHora);
            cartao.setDataMultipla(dataHoraInicio, dataHoraFim);
        }
    }

    private HomeView.EventoH converterParaEventoH(EventoModel model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd, MMM uuuu", new Locale("pt", "BR"));
        String dataHora1 = String.format("%s - %s", model.getDataInicial().format(formatter).toUpperCase(), model.getHoraInicial());
        String dataHora2 = String.format("%s - %s", model.getDataFinal().format(formatter).toUpperCase(), model.getHoraFinal());
        String formatoStr = model.getFormato().toString().substring(0, 1).toUpperCase() + model.getFormato().toString().substring(1).toLowerCase();
        Set<String> preferencias = model.getComunidades().stream().map(Enum::toString).collect(Collectors.toSet());
        String categoria = preferencias.stream().findFirst().orElse("Geral");
        String horaI = String.valueOf(model.getHoraInicial());
        String horaF = String.valueOf(model.getHoraFinal());
        String local = (model.getFormato() == FormatoSelecionado.ONLINE) ? "Evento Online" : model.getLocalizacao();
        return new HomeView.EventoH(model.getId(), model.getNome(), local, dataHora1, dataHora2, categoria, model.getFoto(), model.getDescricao(), model.getParticipantes().size(), model.getnParticipantes(), formatoStr, preferencias, model.getParticipantes(), model.getLinkAcesso(), model.getDataInicial(), model.getDataFinal(), horaI, horaF);
    }
}