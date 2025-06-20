package com.eventually.controller;
import com.eventually.dto.AutenticarUsuarioDto;
import com.eventually.model.UsuarioModel;
import com.eventually.service.AlertaService;
import com.eventually.service.NavegacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/** PASSÍVEL DE ALTERAÇÃÕES
 * Classe controladora da tela de login, é responsável pela comunicação da tela de login com o backend.
 * Contém todos os métodos como privados para que seu acesso seja somente por esta classe.
 * @author Yuri Garcia Maia (Estrutura base)
 * @version 1.01
 * @since 2025-05-07
 * @author Gabriella Tavares Costa Corrêa (Documentação e revisão da da estrutura e parte lógica da classe)
 * @since 2025-05-14
 */
public class LoginController {
    private final LoginView loginView;
    private final Stage primaryStage;

    private UsuarioSessaoService usuarioSessaoService;
    private NavegacaoService navegacaoService;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLog = LoggerFactory.getLogger(LoginController.class);

    /**
     * Construtor do {@code LoginController} que obtém a instância única de UsuarioSessaoService
     * para acessar a lista de usuários, inicializa a view de sessão de usuário e o servico de
     * navegação entre telas.
     * @param loginView a interface de sessão associada.
     * @param primaryStage o palco principal da aplicação,
     */
    public LoginController(LoginView loginView, Stage primaryStage){
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        sistemaDeLog.info("Inicializado e conectado ao UsuarioSessaoService.");

        this.loginView = loginView;
        this.loginView.setLoginController(this);

        this.primaryStage = primaryStage;

        this.navegacaoService = new NavegacaoService(primaryStage);

        configManipuladoresDeEventoLogin();
    }

    /**
     * Configura os manipuladores de evento para os componentes da tela de login.
     * Este método associa as ações dos botões e links da interface de login e em caso
     * de falha na configuração de algum manipulador de evento, uma mensagem de erro
     * é exibida no console.
     */
    private void configManipuladoresDeEventoLogin() {
        sistemaDeLog.info("Método configManipuladoresDeEventoLogin() chamado.");
        try {
            loginView.getEsqueceuSenhaLink().setOnAction(e -> processarEsqueceuSenhaLink());
            loginView.getBtnLogin().setOnAction(e -> processarLogin());
            loginView.getBtnRegistrar().setOnAction(e -> processarRegistrar());
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao configurar manipuladores de login: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Manipula o evento de clique no link "Esqueceu sua senha", abre o modal e em caso
     * de falha, uma mensagem de erro é exibida no console.
     */
    private void processarEsqueceuSenhaLink() {
        sistemaDeLog.info("Método processarEsqueceuSenhaLink() chamado.");
        try {
            sistemaDeLog.info("Botão de esqueceu sua senha clicado!");
            abrirModalEsqueceuSenha();
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao configurar manipulador de senha esquecida: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método é acionado quando o botão de registrar é clicado, ele chama o método que
     * abre a tela de cadastro e em caso de falha, uma mensagem de erro é exibida no console.
     */
    private void processarRegistrar() {
        sistemaDeLog.info("Método processarRegistrar() chamado.");
        try {
            sistemaDeLog.info("Botão de registrar clicado!");
            navegacaoService.navegarParaRegistro();
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao configurar manipulador de registro: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método que estabelece a lógica para quando o botão de login é clicado
     * e em caso de falha, uma mensagem de erro é exibida no console.
     */
    private void processarLogin() {
        sistemaDeLog.info("Método processarLogin() chamado.");
        try {
            sistemaDeLog.info("Botão de login clicado!");

            String email = loginView.getEmailField().getText();
            String senha = loginView.getPasswordField().getText();

            AutenticarUsuarioDto usuarioASerAutenticado = new AutenticarUsuarioDto(email, senha);

            Optional<UsuarioModel> usuarioAutenticado = usuarioSessaoService.validarUsuario(usuarioASerAutenticado.email(), usuarioASerAutenticado.senha());

            if (usuarioAutenticado.isPresent()) {
                sistemaDeLog.info("Usuário autenticado com sucesso!");
                navegacaoService.navegarParaHome(usuarioAutenticado.get());

            } else {
                sistemaDeLog.info("Email não cadastrado ou senha incorretos.");
                //falta: implementar a chamada de um método que instancia um elemento visual de "Senha incorreta ou email não cadastrado".
                //PROVISORIO:
                alertaService.alertarWarn("Falha no Login", "Email não cadastrado ou senha incorretos.");
            }
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao configurar manipulador de login: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método que instancia e chama o modal de "Esqueceu sua senha" e em caso de falha na configuração
     * do modal, uma mensagem de erro é exibida no console.
     */
    private void abrirModalEsqueceuSenha() {
        sistemaDeLog.info("Método abrirModalEsqueceuSenha() chamado.");
        try{
            //verificar funcionamento
            EsqueceuSenhaModal esqueceuSenhaModal = new EsqueceuSenhaModal();
            esqueceuSenhaModal.showForgotPasswordModal(primaryStage);
            EsqueceuSenhaController fpController = new EsqueceuSenhaController(esqueceuSenhaModal);
            esqueceuSenhaModal.setForgotPasswordController(fpController);
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao abrir o modal: "+e.getMessage());
            e.printStackTrace();
        }
    }
}