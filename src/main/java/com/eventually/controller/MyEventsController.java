package com.eventually.controller;

import com.eventually.service.AlertaService;
import com.eventually.service.NavegacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe controladora da tela de eventos participados e criados pelo usuário, é responsável pela comunicação
 * com o backend.
 * Contém métodos privados para que os acesso sejam somente por esta classe.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.00
 * @since 2025-06-18
 */
public class MyEventsController {
    private final MyEventsView myEventsView;
    private final Stage primaryStage;

    private NavegacaoService navegacaoService;
    private UsuarioSessaoService usuarioSessaoService;

    private String emailRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Construtor do {@code MyEventsController}, inicializa a view de eventos do usuário.
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
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de "Meus Eventos" e, em caso de falha,
     * exibe uma mensagem no console.
     */
    private void configManipuladoresDeEventoMeusEventos() {
        sistemaDeLogger.info("Método configManipuladoresDeEventoMeusEventos() chamado.");
        try {
            myEventsView.getBtnMeusEventos().setOnAction(e -> processarNavegacaoMeusEventos());

            myEventsView.getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
            myEventsView.getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));
            myEventsView.getBtnProgramacao().setOnAction(e -> navegacaoService.navegarParaProgramacao(emailRecebido));

            myEventsView.getBtnSair().setOnAction(e -> navegacaoService.abrirModalEscerrrarSessão());
            myEventsView.getBtnNovoEvento().setOnAction(e -> navegacaoService.processarCriacaoEvento());

            // FALTA:
            // BUTTON EVENTOS CRIADOS (POR USUARIO)
            // BUTTON EVENTOS FINALIZADOS (PARTICIPADOS E CRIADOS PELO USUARIO)
            // BUTTON EDICAO DE EVENTO
            // CORRIGIR POSICAO DO CRIAR EVENTO DO CANTO SUPERIOR PARA O CANTO INFERIOR DIREITO
            // CARTAO DE EVENTO para tela de meus eventos
            // MODAL DE EDICAO DE EVENTO

            myEventsView.getGrupoDatas().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    processarSelecaoData(newToggle);
                }
            });
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores da tela de Meus Eventos: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método exibe mensagem informando que o usuário já está na tela de Meus Eventos
     * e, em caso de falha na exibição do alerta, é exibida uma mensagem de erro no console.
     */
    private void processarNavegacaoMeusEventos() {
        sistemaDeLogger.info("Método processarNavegacaoMeusEventos() chamado.");
        try{
            sistemaDeLogger.info("Botão de Meus Eventos clicado!");
            alertaService.alertarInfo("Você já está na tela de Meus Eventos!");
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao ir para tela de início: "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void processarSelecaoData(javafx.scene.control.Toggle toggle) {
        sistemaDeLogger.info("Data selecionada: " + toggle.getUserData());
    }
}