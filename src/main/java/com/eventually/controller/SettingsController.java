package com.eventually.controller;

import com.eventually.service.*;
import com.eventually.view.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe controladora da tela de Configurações do usuário, é responsável pela comunicação da tela de de configurações
 * com o backend.
 * Contém todos os métodos como privados para que seu acesso seja somente por esta classe.
 * @version 1.00
 * @author Yuri Garcia Maia (Estrutura base)
 * @since 2025-05-22
 * @author Gabriella Tavares Costa Corrêa (Revisão de documentação, estrutura e refatoração da parte lógica da classe)
 * @since 2025-05-22
 */
public class SettingsController {
    private SettingsView settingsView;
    private final Stage primaryStage;

    private UsuarioSessaoService usuarioSessaoService;
    private UsuarioExclusaoService usuarioExclusaoService;
    private NavegacaoService navegacaoService;

    private String emailRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(RegisterController.class);

    /**
     * Construtor do {@code SettingsController}, inicializa a view de configurações.
     * @param settingsView a interface de configurações associada
     * @param primaryStage o palco principal da aplicação
     */
    public SettingsController(String email, SettingsView settingsView, Stage primaryStage) {
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        this.usuarioExclusaoService = UsuarioExclusaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService e UsuarioExclusaoService.");

        this.emailRecebido = email;

        this.settingsView = settingsView;
        this.settingsView.setSettingsController(this);

        this.primaryStage = primaryStage;

        configManiouladoresEventoConfig();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de configurações e, em caso de falha,
     * exibe uma mensagem no console.
     */
    private void configManiouladoresEventoConfig() {
        sistemaDeLogger.info("Método configManiouladoresEventoConfig() chamado.");
        try {
            settingsView.getBtnConfiguracoes().setOnAction(e -> processarNavegacaoConfiguracoes());

            settingsView.getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
            settingsView.getBtnMeusEventos().setOnAction(e -> navegacaoService.navegarParaMeusEventos(emailRecebido));
            settingsView.getBtnProgramacao().setOnAction(e -> navegacaoService.navegarParaProgramacao(emailRecebido));

            settingsView.getBtnSair().setOnAction(e -> navegacaoService.abrirModalEscerrrarSessão());
            settingsView.getBtnDeleteAccount().setOnAction(e -> processarDeletarConta());
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de configurações: "+e.getMessage());
            e.printStackTrace();
        }
    }

    //
    //metodo publico exibir valores igual o de outros lugares
    //metodo publico puxar servico de edicao

    /**
     * Este método exibe mensagem informando que o usuário já está na tela de configurações e, em caso de falha na exibição
     * do alerta, é exibida uma mensagem de erro.
     */
    private void processarNavegacaoConfiguracoes() {
        sistemaDeLogger.info("Método processarNavegacaoConfiguracoes() chamado.");
        try{
            sistemaDeLogger.info("Botão de configurções clicado!");
            alertaService.alertarInfo("Você já está na tela de configurações!");
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao ir para tela de configurações: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método abre o modal no qual o usuário seleciona se quer deletar a conta ou não, na operação é procurado o ID
     * do usuario e seu email, para que seja alterado o estado do objeto usuário com o email específico para "false", ou
     * seja, inativo.
     */
    private void processarDeletarConta() {
        try{
            //chamar modal
            //boolean respostaModal = getResposta
            //if (getresposta) - > ovbiamente true:
            int idEncontrado = usuarioSessaoService.procurarID(emailRecebido);
            usuarioExclusaoService.alterarEstadoDoUsuario(idEncontrado, false);
            //entao:
            alertaService.alertarInfo("Conta removida com sucesso!");
            navegacaoService.navegarParaLogin();
        }
        catch (Exception e) {
            sistemaDeLogger.info("Erro ao deletar conta: "+e.getMessage());
            e.printStackTrace();
        }
    }
}