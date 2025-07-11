package com.eventually.controller;
import com.eventually.service.NavegacaoService;
import com.eventually.service.ResultadoAutenticacao;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.*;
import com.eventually.view.modal.AlertModal;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe controladora da tela de login, é responsável pela comunicação da tela de login com o backend.
 * Contém todos os métodos como privados para que seu acesso seja somente por esta classe.
 * @author Yuri Garcia Maia (Estrutura base)
 * @version 1.03
 * @since 2025-05-07
 * @author Gabriella Tavares Costa Corrêa (Documentação e revisão da da estrutura e parte lógica da classe)
 * @since 2025-05-14
 */
public class LoginController {
    private final LoginView loginView;
    private final Stage primaryStage;

    private UsuarioSessaoService usuarioSessaoService;
    private NavegacaoService navegacaoService;

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
            navegacaoService.abrirEsqueceuSenhaModal();
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

            if (email.isBlank() || senha.isBlank()) {
                sistemaDeLog.warn("Tentativa de login com campos vazios.");
                AlertModal alertModal = new AlertModal();
                alertModal.show(primaryStage, "Atenção", "Por favor, preencha todos os campos.");
                return;
            }

            ResultadoAutenticacao resultado = usuarioSessaoService.validarUsuario(email, senha);

            AlertModal alertModal = new AlertModal();
            switch (resultado.getStatus()) {
                case SUCESSO:
                    sistemaDeLog.info("Usuário autenticado com sucesso!");
                    navegacaoService.navegarParaHome(resultado.getUsuario().get());
                    break;

                case FALHA_CREDENCIAL_INVALIDA:
                    sistemaDeLog.info("Email não cadastrado ou senha incorretos.");
                    alertModal.show(primaryStage, "Erro de Login", "Senha incorreta ou email não cadastrado.");
                    break;

                case FALHA_CONTA_INATIVA:
                    sistemaDeLog.warn("Tentativa de login com conta inativa.");
                    alertModal.show(primaryStage, "Login Bloqueado", "Esta conta está inativa e não pode realizar o login.");
                    break;
            }
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao configurar manipulador de login: "+e.getMessage());
            e.printStackTrace();
        }
    }
}