package com.eventually.controller;
import com.eventually.dto.AutenticarUsuarioDto;
import com.eventually.model.UsuarioModel;
import com.eventually.service.AutenticacaoUsuarioService;
import com.eventually.service.TelaService;
import com.eventually.view.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Classe controller da tela de login.
 * Esta classe é responsável pela comunicação
 * da tela de login com o backend.
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-05-07
 * @author Gabriella Tavares Costa Corrêa (Documentação e revisão da classe)
 * @since 2025-05-14
 */
public class LoginController {
    private final LoginView loginView;
    private final Stage primaryStage;

    /**
     * Construtor do {@code SettingsController}, inicializa a view de sessão de usuário.
     * @param loginView a interface de sessão associada
     * @param primaryStage o palco principal da aplicação
     */
    public LoginController(LoginView loginView, Stage primaryStage) {
        this.loginView = loginView;
        this.primaryStage = primaryStage;
        this.loginView.setLoginController(this);
        setupEventHandlersLogin();
    }

    public void setupEventHandlersLogin() {
        loginView.getEsqueceuSenhaLink().setOnAction(e -> handleEsqueceuSenhaLink());
        loginView.getBtnLogin().setOnAction(e -> testeModal());
        loginView.getBtnRegistrar().setOnAction(e -> handleRegistrar());
    }

    /**
     * Manipula o evento de clique no link "Esqueceu sua senha"e abre o modal
     */
    private void handleEsqueceuSenhaLink() {
        System.out.println("LController: Botão de esqueceu sua senha clicado!");
        abrirModalEsqueceuSenha();
    }

    /**
     * Este método é acionado quando o botão de registrar é clicado, ele chama o método que
     * abre a tela de cadastro.
     */
    private void handleRegistrar() {
        System.out.println("LController: Botão de registrar clicado!");
        this.abrirRegisterView();
    }

    private void testeModal(){
        System.out.println("LController: Abrir modal");

        ChangeConfirmModal changeConfirmModal = new ChangeConfirmModal();
        changeConfirmModal.showChangePasswordModal(primaryStage);
    }
    /**
     * Método que estabelece a lógica para quando o botão de login é clicado
     * e lida com o resultado.
     */
    private void handleLogin() {
        System.out.println("LController: botão de login clicado");

        String email = loginView.getEmailField().getText();
        String senha = loginView.getPasswordField().getText();

        AutenticarUsuarioDto usuarioASerAutenticado = new AutenticarUsuarioDto(email, senha);
        AutenticacaoUsuarioService autenticacaoUsuarioService = new AutenticacaoUsuarioService(usuarioASerAutenticado);

        Optional<UsuarioModel> usuarioAutenticado = autenticacaoUsuarioService.validarUsuario(usuarioASerAutenticado.email(),usuarioASerAutenticado.senha());

        if (usuarioAutenticado.isPresent()) {
            try {
                UserScheduleView userScheduleView = new UserScheduleView();
                UserScheduleController userScheduleController = new UserScheduleController(userScheduleView, primaryStage);
                userScheduleView.setUserScheduleController(userScheduleController);

                TelaService service = new TelaService();
                Scene sceneUserSchedule = new Scene(userScheduleView,service.medirWidth(),service.medirHeight());

                sceneUserSchedule.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());
                primaryStage.setTitle("Eventually - Progamação do Usuário");
                primaryStage.setScene(sceneUserSchedule);
            } catch (Exception e) {
                System.out.println("LController: Erro ao abrir a tela principal: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("LController: Email não cadastrado ou senha incorretos.");
            //falta: implementar a chamada de um método que instancia um elemento visual de "Senha incorreta ou email não cadastrado"
        }
    }

    /**
     * Tenta autenticar um usuário com as credenciais fornecidas.
     * @param email O email do usuário
     * @param password A senha do usuário
     * @return null se o login for bem-sucedido, uma String com a mensagem de erro caso contrário
     */
    public String handleRequisicaoLogin(String email, String password) {
        boolean emailVazio = (email == null || email.trim().isEmpty());
        boolean senhaVazia = (password == null || password.isEmpty());

        if (emailVazio && senhaVazia) {
            exibirAvisoEmailVazio();
            exibirAvisoSenhaVazia();
            return "Por favor, preencha o email e a senha.";
        } else if (emailVazio) {
            return "Por favor, preencha o seu email.";
        } else if (senhaVazia) {
            return "Por favor, preencha a sua senha.";
        } else {
            return null;
        }
    }

    private void exibirAvisoEmailVazio() {
        System.out.println("Aviso: O campo de email está vazio.");
        //falta: implementar chamada de método que exibe label de aviso que o campo EMAIL está vazio
    }

    private void exibirAvisoSenhaVazia() {
        System.out.println("Aviso: O campo de senha está vazio.");
        //falta: implementar chamada de método que exibe label de aviso que o campo SENHA está vazio
    }

    /**
     * Método que instancia e chama o modal de "Esqueceu sua senha"
     */
    public void abrirModalEsqueceuSenha() {
        System.out.println("LController: Abrir modal");

        ForgotPasswordModal forgotPasswordModal = new ForgotPasswordModal();
        forgotPasswordModal.showForgotPasswordModal(primaryStage);
        ForgotPasswordController fpController = new ForgotPasswordController(forgotPasswordModal);
        forgotPasswordModal.setForgotPasswordController(fpController);
    }

    /**
     * Abre a tela de registro.
     */
    private void abrirRegisterView(){
        RegisterView registerView = new RegisterView();
        RegisterController registerController = new RegisterController(registerView, primaryStage);
        registerView.setRegisterController(registerController);

        TelaService service = new TelaService();
        Scene sceneRegister = new Scene(registerView,service.medirWidth(), service.medirHeight());

        sceneRegister.getStylesheets().add(getClass().getResource("/styles/register-styles.css").toExternalForm());
        primaryStage.setTitle("Eventually - Registro do Usuário");
        primaryStage.setScene(sceneRegister);
        primaryStage.setMaximized(true);
    }
}