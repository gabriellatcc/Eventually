package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.service.AlertaService;
import com.eventually.service.EventoCriacaoService;
import com.eventually.service.NavegacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.*;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe controladora da tela de eventos participados e criados pelo usuário, é responsável pela comunicação
 * com o backend.
 * Contém métodos privados para que os acesso sejam somente por esta classe.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.01
 * @since 2025-06-18
 */
public class MyEventsController {
    private final MyEventsView myEventsView;
    private final Stage primaryStage;

    private NavegacaoService navegacaoService;
    private UsuarioSessaoService usuarioSessaoService;
    private EventoCriacaoService eventoCriacaoService;

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
        this.eventoCriacaoService = EventoCriacaoService.getInstancia();

        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService e UsuarioSessaoService.");

        this.emailRecebido = email;

        this.myEventsView = myEventsView;
        this.myEventsView.setMyEventsViewController(this);

        this.primaryStage = primaryStage;
        this.navegacaoService = new NavegacaoService(primaryStage);

        configManipuladoresDeEventoMeusEventos();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de "Meus Eventos" e, em caso de falha,
     * exibe uma mensagem no console.
     */
    private void configManipuladoresDeEventoMeusEventos() {
        sistemaDeLogger.info("Método configManipuladoresDeEventoMeusEventos() chamado.");
        try {
            myEventsView.getBarraBuilder().getBtnMeusEventos().setOnAction(e -> processarNavegacaoMeusEventos());
            myEventsView.getBarraBuilder().getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
            myEventsView.getBarraBuilder().getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));
            myEventsView.getBarraBuilder().getBtnProgramacao().setOnAction(e -> navegacaoService.navegarParaProgramacao(emailRecebido));

            myEventsView.getBarraBuilder().getBtnSair().setOnAction(e -> navegacaoService.abrirModalEncerrarSessao());

            myEventsView.setNomeUsuario(definirNome(emailRecebido));
            myEventsView.setEmailUsuario(emailRecebido);
            myEventsView.setAvatar(definirImagem(emailRecebido));
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores da tela de Meus Eventos: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private MyEventsView.EventoME converterModeloParaRecord(EventoModel model) {
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        String horaInicialStr = model.getHoraInicial().format(formatoHora);
        String horaFinalStr = model.getHoraFinal().format(formatoHora);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd 'de' MMM", new Locale("pt", "BR"));
        String dataFormatadaI = model.getDataInicial().format(formatter).toUpperCase();
        String dataFormatada2 = model.getDataFinal().format(formatter).toUpperCase();

        int capacidade = model.getnParticipantes();

        String n = String.valueOf(capacidade);

        return new MyEventsView.EventoME(
                model.getNomeEvento(),
                model.getLocalizacao(),
                horaInicialStr,
                horaFinalStr,
                dataFormatadaI,
                dataFormatada2,
                n
        );
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

    private void processarNavegacaoMeusEventos() {
        sistemaDeLogger.info("Botão 'Meus Eventos' clicado, mas já estamos na tela.");
        alertaService.alertarInfo("Você já está na tela de Meus Eventos!");
    }
}