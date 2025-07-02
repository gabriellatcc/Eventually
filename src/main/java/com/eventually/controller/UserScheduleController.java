package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.UsuarioModel;
import com.eventually.service.*;
import com.eventually.view.*;
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
import java.util.stream.Collectors;

/**
 * Classe controladora da tela de programação do usuário, é responsável pela comunicação
 * da tela de programação com o backend.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.08
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

            processarCarregamentoEventos();

            if (userScheduleView.getGrupoDatas() != null) {
                userScheduleView.getGrupoDatas().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                    if (newToggle != null) {
                        processarSelecaoData(newToggle);
                    }
                });
            }
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

    /**
     * Aceita uma data e filtrar os eventos.
     * @param dataAlvo A data para a qual os eventos devem ser filtrados e exibidos.
     */
    private void carregarEventosParaData(LocalDate dataAlvo) {
        List<EventoModel> todosOsEventos = new ArrayList<>();
        List<EventoModel> listaDeEventosCriados = usuarioSessaoService.procurarEventosCriados(emailRecebido);
        List<EventoModel> listaDeEventosInscritos = usuarioSessaoService.procurarEventosInscritos(emailRecebido);

        if (listaDeEventosCriados != null) todosOsEventos.addAll(listaDeEventosCriados);
        if (listaDeEventosInscritos != null) todosOsEventos.addAll(listaDeEventosInscritos);

        List<EventoModel> eventosFiltrados = todosOsEventos.stream()
                .filter(evento -> {
                    if (evento.getDataInicial() == null || evento.getDataFinal() == null) {return false;}
                    boolean comecaNoDia = evento.getDataInicial().equals(dataAlvo);
                    boolean terminaNoDia = evento.getDataFinal().equals(dataAlvo);
                    boolean estaNoMeio = dataAlvo.isAfter(evento.getDataInicial()) && dataAlvo.isBefore(evento.getDataFinal());
                    return comecaNoDia || terminaNoDia || estaNoMeio;
                })
                .collect(Collectors.toList());

        List<UserScheduleView.EventoUS> eventosParaView = eventosFiltrados.stream()
                .map(this::converterParaView)
                .collect(Collectors.toList());

        userScheduleView.setEventos(eventosParaView);
        sistemaDeLogger.info("Encontrados {} eventos para a data {}.", eventosFiltrados.size(), dataAlvo);
    }

    /**
     * Converte um EventoModel em um registro HomeView.EventoUS para popular a UI.
     * (Este método não precisou de alterações)
     * @param model O modelo de dados do evento.
     * @return Um registro pronto para a view.
     */
    private UserScheduleView.EventoUS converterParaView(EventoModel model) {
        String titulo = model.getNome();

        String local = (model.getFormato() == FormatoSelecionado.ONLINE) ? "Evento Online" : model.getLocalizacao();

        String categoria = model.getTemas().stream()
                .findFirst()
                .map(t -> t.toString().substring(0, 1).toUpperCase() + t.toString().substring(1).toLowerCase())
                .orElse("Geral");

        List<UsuarioModel> listaInscritos = model.getParticipantes();
        int nInscritos = (listaInscritos != null) ? listaInscritos.size() : 0;
        int nParticipantes = model.getnParticipantes();

        return new UserScheduleView.EventoUS(
                titulo,
                local,
                categoria,
                nParticipantes,
                nInscritos,
                model.getDataInicial(),
                model.getHoraInicial(),
                model.getDataFinal(),
                model.getHoraFinal()
        );
    }

    private void processarCarregamentoEventos() {
        sistemaDeLogger.info("Carregando eventos reais do serviço...");

        List<UserScheduleView.EventoUS> eventosParaView = new ArrayList<>();

        List<EventoModel> listaDeEventosCriados = usuarioSessaoService.procurarEventosCriados(emailRecebido);
        List<EventoModel> listaDeEventosInscritos = usuarioSessaoService.procurarEventosInscritos(emailRecebido);

        int quantidadeCriados = (listaDeEventosCriados != null) ? listaDeEventosCriados.size() : 0;
        int quantidadeInscritos = (listaDeEventosInscritos != null) ? listaDeEventosInscritos.size() : 0;
        int valoreventos = quantidadeCriados + quantidadeInscritos;

        List<EventoModel> todosOsEventos = new ArrayList<>();
        todosOsEventos.addAll(listaDeEventosCriados);
        todosOsEventos.addAll(listaDeEventosInscritos);

        for (int i = 0; i < valoreventos; i++) {
            eventosParaView.add(converterParaView(todosOsEventos.get(i)));
            EventoMECartao cartao = new EventoMECartao();

            cartao.setLblTitulo(todosOsEventos.get(i).getNome());
            cartao.setLblLocal(todosOsEventos.get(i).getLocalizacao());
            cartao.setLblCapacidadeValor(String.valueOf(todosOsEventos.get(i).getnParticipantes()));
            configurarDataDoCartao(cartao, todosOsEventos.get(i));

            userScheduleView.getListaEventos().getChildren().add(cartao);
        }
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