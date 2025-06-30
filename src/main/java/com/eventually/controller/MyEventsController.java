package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.UsuarioModel;
import com.eventually.service.AlertaService;
import com.eventually.service.EventoCriacaoService;
import com.eventually.service.NavegacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.*;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
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
import java.util.stream.Collectors;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe controladora da tela de eventos participados e criados pelo usuário, é responsável pela comunicação
 * com o backend.
 * Contém métodos privados para que os acesso sejam somente por esta classe.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.04
 * @since 2025-06-18
 */
public class MyEventsController {
    private final MyEventsView myEventsView;
    private final Stage primaryStage;

    private NavegacaoService navegacaoService;
    private UsuarioSessaoService usuarioSessaoService;

    private String emailRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(MyEventsController.class);

    /**
     * Construtor do {@code MyEventsController}, inicializa a view de eventos do usuário.
     * @param email o email do usuário logado.
     * @param myEventsView a interface de visualização de eventos associada.
     * @param primaryStage o palco principal da aplicação.
     */
    public MyEventsController(String email ,MyEventsView myEventsView, Stage primaryStage) {
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService.");

        this.emailRecebido = email;

        this.myEventsView = myEventsView;
        this.myEventsView.setMyEventsViewController(this);

        this.primaryStage = primaryStage;
        this.navegacaoService = new NavegacaoService(primaryStage);

        configManipuladoresDeEventoMeusEventos();
        configurarSeletorEventos();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de "Meus Eventos" e, em caso de falha,
     * exibe uma mensagem no console.
     */
    private void configManipuladoresDeEventoMeusEventos() {
        sistemaDeLogger.info("Método configManipuladoresDeEventoMeusEventos() chamado.");
        try {
            myEventsView.getBarraBuilder().getBtnMeusEventos().setDisable(true);

            myEventsView.getBarraBuilder().getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
            myEventsView.getBarraBuilder().getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));
            myEventsView.getBarraBuilder().getBtnProgramacao().setOnAction(e -> navegacaoService.navegarParaProgramacao(emailRecebido));

            myEventsView.getBarraBuilder().getBtnSair().setOnAction(e -> navegacaoService.abrirModalEncerrarSessao());

            myEventsView.setNomeUsuario(definirNome(emailRecebido));
            myEventsView.setEmailUsuario(emailRecebido);
            myEventsView.setAvatar(definirImagem(emailRecebido));

            processarCarregamentoEventos();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores da tela de Meus Eventos: "+e.getMessage());
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
            return usuarioSessaoService.procurarImagem(email);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter imagem do usuário.", e);
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
            return usuarioSessaoService.procurarNome(email);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter nome do usuário.", e);
            return null;
        }
    }

    private void configurarSeletorEventos() {
        ToggleGroup grupoTipoEvento = myEventsView.getGroupFiltroEventos();

        if (grupoTipoEvento == null) {
            sistemaDeLogger.error("O ToggleGroup para seleção de eventos não foi encontrado na MyEventsView!");
            return;
        }

        grupoTipoEvento.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                myEventsView.setEventos(new ArrayList<>());
                return;
            }

            if (newToggle == myEventsView.getBtnEventosCriados()) {
                sistemaDeLogger.info("Botão 'Eventos Criados' selecionado. Carregando eventos do organizador.");
                carregarEventosParaOrganizador();
            } else if (newToggle == myEventsView.getBtnInscricoes()) {
                sistemaDeLogger.info("Botão 'Inscrições' selecionado. Carregando eventos inscritos.");
                carregarEventosParaInscrito();
            }
        });

        myEventsView.getBtnEventosCriados().setSelected(true);
    }

    private void carregarEventosParaOrganizador() {
        List<EventoModel> todosOsEventos = new ArrayList<>();
        List<EventoModel> listaDeEventosCriados = usuarioSessaoService.procurarEventosCriados(emailRecebido);

        if (listaDeEventosCriados != null) todosOsEventos.addAll(listaDeEventosCriados);

        List<EventoModel> eventosFiltrados = todosOsEventos.stream()
                .filter(evento -> evento.getDataInicial() != null && evento.getDataFinal() != null)
                .collect(Collectors.toList());

        List<MyEventsView.EventoMM> eventosParaView = eventosFiltrados.stream()
                .map(this::converterParaView)
                .collect(Collectors.toList());

        myEventsView.setEventos(eventosParaView);
    }

    private void carregarEventosParaInscrito() {
        List<EventoModel> todosOsEventos = new ArrayList<>();
        List<EventoModel> listaDeEventosInscritos = usuarioSessaoService.procurarEventosInscritos(emailRecebido);

        if (listaDeEventosInscritos != null) todosOsEventos.addAll(listaDeEventosInscritos);

        List<EventoModel> eventosFiltrados = todosOsEventos.stream()
                .filter(evento -> evento.getDataInicial() != null && evento.getDataFinal() != null)
                .collect(Collectors.toList());

        List<MyEventsView.EventoMM> eventosParaView = eventosFiltrados.stream()
                .map(this::converterParaView)
                .collect(Collectors.toList());

        myEventsView.setEventos(eventosParaView);
    }


    /**
     * Converte um EventoModel em um registro HomeView.EventoUS para popular a UI.
     * (Este método não precisou de alterações)
     * @param model O modelo de dados do evento.
     * @return Um registro pronto para a view.
     */
    private MyEventsView.EventoMM converterParaView(EventoModel model) {
        String titulo = model.getNomeEvento();

        String local = (model.getFormato() == FormatoSelecionado.ONLINE) ? "Evento Online" : model.getLocalizacao();

        String categoria = model.getTemasEvento().stream()
                .findFirst()
                .map(t -> t.toString().substring(0, 1).toUpperCase() + t.toString().substring(1).toLowerCase())
                .orElse("Geral");

        List<UsuarioModel> listaInscritos = model.getParticipantes();
        int nInscritos = (listaInscritos != null) ? listaInscritos.size() : 0;
        int nParticipantes = model.getnParticipantes();

        return new MyEventsView.EventoMM(
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

        List<MyEventsView.EventoMM> eventosParaView = new ArrayList<>();

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

            cartao.setLblTitulo(todosOsEventos.get(i).getNomeEvento());
            cartao.setLblLocal(todosOsEventos.get(i).getLocalizacao());

            List<UsuarioModel> listaDeInscritos = todosOsEventos.get(i).getParticipantes();
            int numeroDeInscritos = (listaDeInscritos == null) ? 0 : listaDeInscritos.size();
            String inscritos = String.valueOf(numeroDeInscritos);
            String max = String.valueOf(todosOsEventos.get(i).getnParticipantes());

            cartao.setLblCapacidadeValor(inscritos+"/"+max);
            configurarDataDoCartao(cartao, todosOsEventos.get(i));
            //se o cartao exibir errado o erro vai estar aqui!
            myEventsView.getListaEventos().getChildren().add(cartao);
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