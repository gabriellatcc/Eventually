package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.UsuarioModel;
import com.eventually.service.*;
import com.eventually.view.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
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
 * Classe controladora da tela de programação do usuário, é responsável pela comunicação
 * da tela de programação com o backend.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.11
 * @since 2025-04-25
 */
public class UserScheduleController {
    private final UserScheduleView userScheduleView;
    private final Stage primaryStage;

    private NavegacaoService navegacaoService;
    private UsuarioSessaoService usuarioSessaoService;
    private String emailRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(UserScheduleController.class);

    /**
     * Construtor do {@code UserScheduleController}, inicializa a view de programação de usuário.
     * @param userScheduleView a interface de programação associada
     * @param primaryStage o palco principal da aplicação
     */
    public UserScheduleController(String email, UserScheduleView userScheduleView, Stage primaryStage) {
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService.");

        this.emailRecebido = email;

        this.userScheduleView = userScheduleView;
        this.userScheduleView.setUserScheduleController(this);

        this.primaryStage = primaryStage;
        this.navegacaoService = new NavegacaoService(primaryStage);

        configManipuladoresEventoProg();
        configurarSeletorDeDatas();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de programação e, em caso de falha, exibe uma
     * mensagem no console.
     */
    private void configManipuladoresEventoProg() {
        sistemaDeLogger.info("Método configManipuladoresEventoProg() chamado.");
        try {
            userScheduleView.getBarraBuilder().getBtnProgramacao().setDisable(true);
            userScheduleView.getBarraBuilder().getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
            userScheduleView.getBarraBuilder().getBtnMeusEventos().setOnAction(e -> navegacaoService.navegarParaMeusEventos(emailRecebido));
            userScheduleView.getBarraBuilder().getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));

            userScheduleView.getBarraBuilder().getBtnSair().setOnAction(e -> navegacaoService.abrirModalEncerrarSessao());

            userScheduleView.getLbEmailUsuario().setText(emailRecebido);
            userScheduleView.getLbNomeUsuario().setText(definirNome(emailRecebido));
            userScheduleView.setAvatarImagem(definirImagem(emailRecebido));


            if (userScheduleView.getGrupoDatas() != null) {
                userScheduleView.getGrupoDatas().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                    if (newToggle != null) {
                        processarSelecaoData(newToggle);
                    }
                });
            }
            carregarEventosParaData(LocalDate.now());

        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores da tela de programação: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método retorna a imagem de perfil do usuário, se foi recém cadastrado no sistema, terá a imagem padrão.
     * @param email informado no cadastro.
     * @return retorna a imagem do usuário relativo ao email cadastrado.
     */
    private Image definirImagem(String email) {
        sistemaDeLogger.info("Método definirImagem() chamado.");
        try {
            Image imagemUsuario = usuarioSessaoService.procurarImagem(email);
            return imagemUsuario;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter imagem do usuário."+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método retorna o nome do usuário e, em caso de falha, é exibida uma mensagem de erro no console.
     * @param email informado no cadastro.
     * @return retorna o nome do usuário relativo ao email cadastrado.
     */
    private String definirNome(String email) {
        sistemaDeLogger.info("Método definirNome() chamado.");
        try {
            String nome = usuarioSessaoService.procurarNome(email);
            return nome;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter nome do usuário: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Configura o seletor de datas na view com os próximos 7 dias.
     */
    private void configurarSeletorDeDatas() {
        if (userScheduleView.getSeletorDataContainer() == null) {
            sistemaDeLogger.warn("O container do seletor de datas (seletorDataContainer) não foi encontrado na UserScheduleView.");
            return;
        }

        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatoDiaSemana = DateTimeFormatter.ofPattern("EEE", new Locale("pt", "BR"));
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd", new Locale("pt", "BR"));

        for (int i = 0; i < 7; i++) {
            LocalDate dataIteracao = hoje.plusDays(i);
            String diaSemana = dataIteracao.format(formatoDiaSemana).toUpperCase();
            String dia = dataIteracao.format(formatoDia);

            ToggleButton btnData = new ToggleButton(diaSemana + "\n" + dia);
            btnData.setUserData(dataIteracao);
            btnData.setToggleGroup(userScheduleView.getGrupoDatas());
            btnData.getStyleClass().add("date-button");
            userScheduleView.getSeletorDataContainer().getChildren().add(btnData);

            if (i == 0) {
                btnData.setSelected(true);
            }
        }
    }

    /**
     * Processa a seleção de uma nova data, buscando e carregando os eventos correspondentes.
     * @param toggle O ToggleButton que foi selecionado.
     */
    private void processarSelecaoData(Toggle toggle) {
        if (toggle == null) return;

        LocalDate dataSelecionada = (LocalDate) toggle.getUserData();
        sistemaDeLogger.info("Data selecionada pelo usuário: " + dataSelecionada);

        userScheduleView.atualizarCabecalho(dataSelecionada);

        carregarEventosParaData(dataSelecionada);
    }

    private void carregarEventosParaData(LocalDate dataAlvo) {
        userScheduleView.getListaEventos().getChildren().clear();

        List<EventoModel> todosOsEventos = new ArrayList<>();
        List<EventoModel> listaDeEventosCriados = usuarioSessaoService.procurarEventosCriados(emailRecebido);
        List<EventoModel> listaDeEventosInscritos = usuarioSessaoService.procurarEventosInscritos(emailRecebido);

        if (listaDeEventosCriados != null) todosOsEventos.addAll(listaDeEventosCriados);
        if (listaDeEventosInscritos != null) todosOsEventos.addAll(listaDeEventosInscritos);

        List<EventoModel> eventosFiltrados = todosOsEventos.stream()
                .filter(EventoModel::isEstado)
                .filter(evento -> {
                    if (evento.getDataInicial() == null || evento.getDataFinal() == null) { return false; }
                    boolean comecaNoDia = evento.getDataInicial().equals(dataAlvo);
                    boolean terminaNoDia = evento.getDataFinal().equals(dataAlvo);
                    boolean estaNoMeio = dataAlvo.isAfter(evento.getDataInicial()) && dataAlvo.isBefore(evento.getDataFinal());
                    return comecaNoDia || terminaNoDia || estaNoMeio;
                })
                .collect(Collectors.toList());

        sistemaDeLogger.info("Encontrados {} eventos para a data {}.", eventosFiltrados.size(), dataAlvo);

        if (eventosFiltrados.isEmpty()) {
            Label placeholder = new Label("Nenhum evento agendado para este dia.");
            placeholder.getStyleClass().add("placeholder-label");
            userScheduleView.getListaEventos().getChildren().add(placeholder);
            return;
        }

        for (EventoModel evento : eventosFiltrados) {
            EventoMECartao cartao = new EventoMECartao();

            cartao.setLblTitulo(evento.getNome());
            cartao.setLblLocal(evento.getLocalizacao());
            String textoCapacidade = evento.getParticipantes().size() + "/" + evento.getnParticipantes();
            cartao.setLblCapacidadeValor(textoCapacidade);
            configurarDataDoCartao(cartao, evento);

            Button botaoVer = cartao.getBtnVer();

            Runnable callbackDeAtualizacao = () -> {
                userScheduleView.getListaEventos().getChildren().remove(cartao);
                if (userScheduleView.getListaEventos().getChildren().isEmpty()) {
                    Label placeholder = new Label("Nenhum evento agendado para este dia.");
                    placeholder.getStyleClass().add("placeholder-label");
                    userScheduleView.getListaEventos().getChildren().add(placeholder);
                }
            };

            botaoVer.setOnAction(e -> {
                HomeView.EventoH eventoH = converterParaEventoH(evento);

                navegacaoService.abrirModalVerEvento(this.emailRecebido, eventoH, callbackDeAtualizacao);
            });

            userScheduleView.getListaEventos().getChildren().add(cartao);
        }
    }

    /**
     * Converte um EventoModel para o record EventoH usado pelo modal de visualização.
     */
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

        return new HomeView.EventoH(
                model.getId(), model.getNome(), local, dataHora1, dataHora2,
                categoria, model.getFoto(), model.getDescricao(), model.getParticipantes().size(),
                model.getnParticipantes(), formatoStr, preferencias, model.getParticipantes(),
                model.getLinkAcesso(), model.getDataInicial(), model.getDataFinal(), horaI, horaF
        );
    }

    /**
     * Método auxiliar para formatar e configurar as datas no cartão do evento.
     * @param cartao O cartão a ser configurado.
     * @param evento O evento com os dados de data/hora.
     */
    private void configurarDataDoCartao(EventoMECartao cartao, EventoModel evento) {
        Locale brasil = new Locale("pt", "BR");
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("EEE dd, MMM yyyy", brasil);
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate dataInicial = evento.getDataInicial();
        LocalDate dataFinal = evento.getDataFinal();

        if (dataInicial.equals(dataFinal)) {
            String dataFormatada = dataInicial.format(formatoData);

            String horaInicialFormatada = evento.getHoraInicial().format(formatoHora);
            String horaFinalFormatada = evento.getHoraFinal().format(formatoHora);
            String horarioCompleto = horaInicialFormatada + " - " + horaFinalFormatada;

            cartao.setDataUnica(dataFormatada, horarioCompleto);

        } else {
            String dataHoraInicio = dataInicial.format(formatoData) + " " + evento.getHoraInicial().format(formatoHora);
            String dataHoraFim = dataFinal.format(formatoData) + " " + evento.getHoraFinal().format(formatoHora);

            cartao.setDataMultipla(dataHoraInicio, dataHoraFim);
        }
    }
}