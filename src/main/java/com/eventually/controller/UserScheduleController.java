package com.eventually.controller;

import com.eventually.service.AlertaService;
import com.eventually.service.NavegacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.UserScheduleView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** PASSÍVEL A ALTERAÇÃO
 * Classe controladora da tela de programação do usuário, é responsável pela comunicação
 * da tela de programação com o backend.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.01
 * @since 2025-04-25
 */
public class UserScheduleController {
    private final UserScheduleView userScheduleView;
    private final Stage primaryStage;

    private UsuarioSessaoService usuarioSessaoService;
    private NavegacaoService navegacaoService;

    private String emailRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(LoginController.class);

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

        configManipuladoresDeEventoUS();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de programação e, em caso de falha, exibe uma
     * mensagem no console.
     */
    private void configManipuladoresDeEventoUS() {
        sistemaDeLogger.info("Método configManipuladoresEventoRegistro() chamado.");
        try {
            userScheduleView.getBtnProgramacao().setOnAction(e -> processarNavegacaoProgramacao());

            userScheduleView.getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
            userScheduleView.getBtnMeusEventos().setOnAction(e -> navegacaoService.navegarParaMeusEventos(emailRecebido));
            userScheduleView.getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));

            userScheduleView.getBtnSair().setOnAction(e -> navegacaoService.abrirModalEscerrrarSessão());
            userScheduleView.getBtnNovoEvento().setOnAction(e -> navegacaoService.processarCriacaoEvento());

            userScheduleView.getGrupoDatas().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    processarSelecaoData(newToggle);
                }});
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de programação: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método exibe mensagem informando que o usuário já está na tela de programação e, em caso de falha na exibição
     * do aviso, é exibida uma mensagem de erro no console.
     */
    private void processarNavegacaoProgramacao() {
        sistemaDeLogger.info("Método processarNavegacaoProgramacao() chamado.");
        try{
            sistemaDeLogger.info("Botão de Programação clicado!");
            alertaService.alertarInfo("Você já está na tela de programação!");
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao ir para tela de programação: "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Seletor de datas que exibirá eventos do dia.
     * @param toggle o clique na data por um usuário.
     */
    private void processarSelecaoData(javafx.scene.control.Toggle toggle) {
        //falta: corrigir para exibir eventos (EventoUSCartao) que o usuario participa/é dele no dia clicado
        sistemaDeLogger.info("Data selecionada: " + toggle.getUserData());
    }
}