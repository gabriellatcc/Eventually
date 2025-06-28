package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.TemaPreferencia;
import com.eventually.model.UsuarioModel;
import com.eventually.service.AlertaService;
import com.eventually.service.NavegacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe controladora da tela inicial responsável pela comunicação com o backend e navegação entre telas.
 * Contém métodos privados para que os acesso sejam somente por esta classe e métodos públicos para serem acessados
 * por outras classes.
 * @version 1.02
 * @author Yuri Garcia Maia (Estrutura base)
 * @since 2025-05-23
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-05-29
 */
public class HomeController {
    private final HomeView homeView;
    private final Stage primaryStage;

    private UsuarioSessaoService usuarioSessaoService;
    private NavegacaoService navegacaoService;
    // private EventoService eventoService;

    private String emailRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Construtor do {@code HomeController} que obtém a instância única de
     * UsuarioSessaoService para acessar a lista de usuários e inicializa a view de início.
     * @param email o nome do usuário a ser exibido.
     * @param homeView a interface inicial associada.
     * @param primaryStage o palco principal da aplicação.
     */
    public HomeController(String email, HomeView homeView, Stage primaryStage) {
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();

        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService.");

        this.emailRecebido = email;

        this.homeView = homeView;
        this.homeView.setHomeController(this);

        this.primaryStage = primaryStage;
        this.navegacaoService = new NavegacaoService(primaryStage);

        configManipuladoresEventoInicio();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela inicial e, em caso de falha, exibe uma
     * mensagem no console.
     */
    private void configManipuladoresEventoInicio() {
        sistemaDeLogger.info("Método configManipuladoresEventoInicio() chamado.");
        try {
            homeView.getBarraBuilder().getBtnInicio().setOnAction(e -> processarNavegacaoInicio());
            homeView.getBarraBuilder().getBtnMeusEventos().setOnAction(e -> navegacaoService.navegarParaMeusEventos(emailRecebido));
            homeView.getBarraBuilder().getBtnProgramacao().setOnAction(e -> navegacaoService.navegarParaProgramacao(emailRecebido));
            homeView.getBarraBuilder().getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));

            homeView.getBarraBuilder().getBtnSair().setOnAction(e -> navegacaoService.abrirModalEncerrarSessao());

            homeView.getBtnCriarEvento().setOnAction(e -> navegacaoService.abrirModalCriarEvento(emailRecebido));
            homeView.getBtnFiltros().setOnAction(e -> processarFiltros());

            homeView.getLbEmailUsuario().setText(emailRecebido);
            homeView.getLbNomeUsuario().setText(definirNome(emailRecebido));
            homeView.setAvatarImagem(definirImagem(emailRecebido));

            processarCarregamentoEventos();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores da tela de início: "+e.getMessage());
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
     * Este método exibe mensagem informando que o usuário já está na tela de iníco e, em caso de falha na exibição
     * do aviso, é exibida uma mensagem de erro no console.
     */
    private void processarNavegacaoInicio() {
        sistemaDeLogger.info("Método processarNavegacaoInicio() chamado.");
        try{
            sistemaDeLogger.info("Botão de Início clicado");
            alertaService.alertarInfo("Você já está na tela de Início!");
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao ir para tela de início: "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Filtros", abrindo o painel de filtros e, em caso de erro, é
     * exibida uma mensagem no console.
     */
    private void processarFiltros() {
        sistemaDeLogger.info("Método processarFiltros() chamado.");
        try {
            sistemaDeLogger.info("Botão de Filtros clicado!");
            // IMPLEMENTAR ELEMENTO UI PARA FILTRO
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao abrir filtros: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Este método carrega os eventos da página inicial através do service e em caso de falha, é exibida uma mensagem no
     * console.
     */
    public void processarCarregamentoEventos() {
        sistemaDeLogger.info("Método processarCarregamentoEventos() chamado.");
        try {
            sistemaDeLogger.info("Carregando eventos da página inicial");
            // List<HomeView.Evento> eventos = eventoService.buscarTodosEventos();
            List<HomeView.Evento> eventos = buscarEventosDeExemplo();


            homeView.setEventos(eventos);

        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao carregar eventos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Simula a busca de eventos no backend.
     * @return Uma lista de eventos para exibição.
     */
    private List<HomeView.Evento> buscarEventosDeExemplo() {
        List<HomeView.Evento> eventosParaView = new ArrayList<>();

        EventoModel evento1 = new EventoModel(
                null, "Conferência Tech Inovação", "Discussão sobre o futuro da tecnologia.",
                FormatoSelecionado.PRESENCIAL, null, "Centro de Convenções, SP", null, 200,
                LocalDate.of(2025, 8, 15), "09:00",
                LocalDate.of(2025, 8, 16), "18:00",
                null, null, true, false
        );
        eventosParaView.add(converterParaView(evento1));


        EventoModel evento2 = new EventoModel(
                null, "Workshop de Design UX/UI", "Aprenda na prática os fundamentos de UX.",
                FormatoSelecionado.ONLINE, "https://zoom.us/j/123456", "Online", null, 50,
                LocalDate.of(2025, 9, 5), "19:00",
                LocalDate.of(2025, 9, 5), "22:00",
                null, null, true, false
        );
        eventosParaView.add(converterParaView(evento2));


        EventoModel evento3 = new EventoModel(
                null, "Festival de Música Indie", "Bandas independentes em um evento único.",
                FormatoSelecionado.HIBRIDO, null, "Parque Ibirapuera, SP", null, 1000,
                LocalDate.of(2025, 9, 28), "14:00",
                LocalDate.of(2025, 9, 28), "23:00",
                null, null, true, false
        );
        eventosParaView.add(converterParaView(evento3));

        EventoModel evento4 = new EventoModel(
                null, "Feira de Tecnologia e Inovação", "Apresentação de startups e novas tecnologias do mercado.",
                FormatoSelecionado.PRESENCIAL, null, "Centro de Convenções Expo Center Norte, SP", null, 5000,
                LocalDate.of(2025, 10, 15), "09:00",
                LocalDate.of(2025, 10, 17), "18:00",
                null, null, true, true
        );
        eventosParaView.add(converterParaView(evento4));

        EventoModel evento5 = new EventoModel(
                null, "Workshop de Fotografia com Celular", "Aprenda a tirar fotos incríveis usando apenas o seu smartphone.",
                FormatoSelecionado.ONLINE, "https://zoom.us/j/1234567890", "Plataforma Zoom", null, 150,
                LocalDate.of(2025, 11, 22), "19:00",
                LocalDate.of(2025, 11, 22), "21:30",
                null, null, true, false
        );
        eventosParaView.add(converterParaView(evento5));

        EventoModel evento6 = new EventoModel(
                null, "Festival Gastronômico Sabores do Vale", "Chefs renomados da região do Vale do Paraíba em um só lugar.",
                FormatoSelecionado.PRESENCIAL, null, "Parque do Povo, Presidente Prudente, SP", null, 2500,
                LocalDate.of(2025, 12, 5), "17:00",
                LocalDate.of(2025, 12, 7), "23:00",
                null, null, true, true
        );
        eventosParaView.add(converterParaView(evento6));

        return eventosParaView;
    }

    /**
     * Converte um EventoModel em um registro HomeView.Evento para popular a UI.
     * @param model O modelo de dados do evento.
     * @return Um registro pronto para a view.
     */
    private HomeView.Evento converterParaView(EventoModel model) {
        String titulo = model.getNomeEvento();
        String local = model.getLocalizacao();
        String categoria = model.getFormato().toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd 'de' MMM", new Locale("pt", "BR"));
        String dataFormatada = model.getDataInicial().format(formatter).toUpperCase();
        String dataHora = String.format("%s - %s", dataFormatada, model.getHoraInicial());

        return new HomeView.Evento(titulo, local, dataHora, categoria);
    }

}