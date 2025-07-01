package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.TemaPreferencia;
import com.eventually.service.*;
import com.eventually.view.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe controladora da tela inicial responsável pela comunicação com o backend e navegação entre telas.
 * Contém métodos privados para que os acesso sejam somente por esta classe e métodos públicos para serem acessados
 * por outras classes.
 * @version 1.07
 * @author Yuri Garcia Maia (Estrutura base)
 * @since 2025-05-23
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-05-29
 */
public class HomeController {
    private final HomeView homeView;
    private final Stage primaryStage;

    private NavegacaoService navegacaoService;
    private UsuarioSessaoService usuarioSessaoService;
    private EventoCriacaoService eventoCriacaoService;

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

        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService e EventoCriacaoService.");

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
            homeView.getBarraBuilder().getBtnInicio().setDisable(true);

            homeView.getBarraBuilder().getBtnMeusEventos().setOnAction(e -> navegacaoService.navegarParaMeusEventos(emailRecebido));
            homeView.getBarraBuilder().getBtnProgramacao().setOnAction(e -> navegacaoService.navegarParaProgramacao(emailRecebido));
            homeView.getBarraBuilder().getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));

            homeView.getBarraBuilder().getBtnSair().setOnAction(e -> navegacaoService.abrirModalEncerrarSessao());

            homeView.getBtnCriarEvento().setOnAction(e -> navegacaoService.abrirModalCriarEvento(emailRecebido, this::processarCarregamentoEventos));

            Set<TemaPreferencia> temasUsuario = usuarioSessaoService.procurarPreferencias(emailRecebido);

            Set<String> tags = temasUsuario.stream()
                    .map(TemaPreferencia::name)
                    .collect(Collectors.toSet());

            for (String nomeTag : tags) {
                Label tagLabel = new Label(nomeTag);
                tagLabel.getStyleClass().add("tag-label");
                homeView.getFlowPaneTags().getChildren().add(tagLabel);
            }

            homeView.getLbEmailUsuario().setText(emailRecebido);
            homeView.getLbNomeUsuario().setText(definirNome(emailRecebido));
            homeView.setAvatarImagem(definirImagem(emailRecebido));

            atualizarVisualizacaoPreferencias();
            homeView.getBtnFiltros().setOnAction(e -> {abrirModalParaEdicao();});

            processarCarregamentoEventos();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores da tela de início: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirModalParaEdicao() {
        navegacaoService.abrirModalEditarFiltros(emailRecebido);

        sistemaDeLogger.info("Modal de edição de temas fechado. Atualizando a tela de configurações.");
        atualizarVisualizacaoPreferencias();
    }

    /**
     * Carrega as preferências de tema mais recentes do serviço
     * e atualiza o estado das checkboxes na tela de configurações.
     */
    public void atualizarVisualizacaoPreferencias() {
        sistemaDeLogger.info("Atualizando visualização das tags de preferência na Home.");

        homeView.getFlowPaneTags().getChildren().clear();

        Set<TemaPreferencia> temasAtuais = usuarioSessaoService.procurarPreferencias(emailRecebido);

        Set<String> novasTags = temasAtuais.stream()
                .map(this::formatarNomeTema)
                .collect(Collectors.toSet());

        for (String nomeTag : novasTags) {
            Label tagLabel = new Label(nomeTag);
            tagLabel.getStyleClass().add("tag-label");
            homeView.getFlowPaneTags().getChildren().add(tagLabel);
        }
    }


    private String formatarNomeTema(TemaPreferencia tema) {
        String nomeEnum = tema.name();
        if (nomeEnum == null || nomeEnum.isEmpty()) return "";
        return nomeEnum.charAt(0) + nomeEnum.substring(1).toLowerCase();
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
     * Este método carrega os eventos da página inicial através do service e em caso de falha, é exibida uma mensagem no
     * console.
     */
    public void processarCarregamentoEventos() {
        sistemaDeLogger.info("Método processarCarregamentoEventos() chamado.");
        try {
            sistemaDeLogger.info("Carregando eventos reais do serviço...");

            Set<EventoModel> todosOsEventosModel = eventoCriacaoService.getAllEventos();

            List<HomeView.EventoH> eventosParaView = new ArrayList<>();

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
     * Converte um EventoModel em um registro HomeView.EventoH para popular a UI.
     * @param model O modelo de dados do evento.
     * @return Um registro pronto para a view.
     */
    private HomeView.EventoH converterParaView(EventoModel model) {
        String titulo = model.getNomeEvento();
        String local = model.getFormato() == FormatoSelecionado.ONLINE ? "Evento Online" : model.getLocalizacao();
        Image imagem = model.getFotoEvento();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd, MMM uuuu", new Locale("pt", "BR"));
        String dataHora1 = String.format("%s - %s", model.getDataInicial().format(formatter).toUpperCase(), model.getHoraInicial());
        String dataHora2 = String.format("%s - %s", model.getDataFinal().format(formatter).toUpperCase(), model.getHoraFinal());

        String descricao = model.getDescricao();

        int inscritos = model.getParticipantes().size();
        int capacidade = model.getnParticipantes();

        String formatoStr = model.getFormato().toString();
        formatoStr = formatoStr.substring(0, 1).toUpperCase() + formatoStr.substring(1).toLowerCase();

        Set<String> preferencias = model.getTemasEvento().stream()
                .map(Enum::toString)
                .collect(Collectors.toSet());

        String categoria = preferencias.stream().findFirst().orElse("Geral");

        return new HomeView.EventoH(
                titulo,
                local,
                dataHora1,
                dataHora2,
                categoria,
                imagem,
                descricao,
                inscritos,
                capacidade,
                formatoStr,
                preferencias
        );
    }

    public void abrir(HomeView.EventoH eventoH) {
        navegacaoService.abrirModalVerEvento(eventoH);
    }
}