package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.TemaPreferencia;
import com.eventually.model.UsuarioModel;
import com.eventually.service.*;
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
import java.util.Set;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe controladora da tela inicial responsável pela comunicação com o backend e navegação entre telas.
 * Contém métodos privados para que os acesso sejam somente por esta classe e métodos públicos para serem acessados
 * por outras classes.
 * @version 1.03
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
    private EventoCriacaoService eventoCriacaoService;
    private EventoLeituraService eventoLeituraService;

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
        this.eventoCriacaoService = EventoCriacaoService.getInstancia();
        this.eventoLeituraService = EventoLeituraService.getInstancia();

        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService, EventoCriacaoService e EventoLeituraService.");

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

            homeView.getBtnCriarEvento().setOnAction(e ->
                    navegacaoService.abrirModalCriarEvento(emailRecebido, this::processarCarregamentoEventos)
            );
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
            sistemaDeLogger.info("Carregando eventos reais do serviço...");

            Set<EventoModel> todosOsEventosModel = eventoCriacaoService.getAllEventos();

            List<HomeView.Evento> eventosParaView = new ArrayList<>();

            for (EventoModel model : todosOsEventosModel) {
                eventosParaView.add(converterParaView(model));
            }

            homeView.setEventos(eventosParaView);
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao carregar eventos: " + ex.getMessage());
            ex.printStackTrace();
            homeView.setEventos(new ArrayList<>());
        }
    }

    /**
     * Converte um EventoModel em um registro HomeView.Evento para popular a UI.
     * @param model O modelo de dados do evento.
     * @return Um registro pronto para a view.
     */
    private HomeView.Evento converterParaView(EventoModel model) {
        String titulo = model.getNomeEvento();

        String local;
        if (model.getFormato() == FormatoSelecionado.ONLINE) {
            local = "Evento Online";
        } else {
            local = model.getLocalizacao();
        }

        String categoria;
        Set<TemaPreferencia> temas = model.getTemasEvento();

        if (temas == null || temas.isEmpty()) {
            categoria = "Geral";
        } else {
            categoria = temas.stream()
                    .findFirst()
                    .map(t -> t.toString().substring(0, 1).toUpperCase() + t.toString().substring(1).toLowerCase())
                    .get();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd 'de' MMM", new Locale("pt", "BR"));
        String dataFormatada = model.getDataInicial().format(formatter).toUpperCase();
        String dataHora = String.format("%s - %s", dataFormatada, model.getHoraInicial());

        Image imagem = model.getFotoEvento();

        return new HomeView.Evento(titulo, local, dataHora, categoria, imagem);
    }
}