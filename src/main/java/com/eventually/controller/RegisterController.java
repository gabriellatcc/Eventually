package com.eventually.controller;
import com.eventually.dto.CadastrarUsuarioDto;
import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.service.EmailService;
import com.eventually.service.UsuarioCadastroService;
import com.eventually.service.TelaService;
import com.eventually.view.LoginView;
import com.eventually.view.RegisterView;
import com.eventually.view.UserScheduleView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Classe controller da tela de registro.
 * Esta classe é responsável pela comunicação
 * da tela de registro com o backend.
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-05-13
 * @author Gabriella Tavares Costa Corrêa (Documentação, revisão da estrutura e lógica da classe)
 * @since 2025-05-14
 */
public class RegisterController {
    private final RegisterView registerView;
    private final Stage primaryStage;

    UsuarioCadastroService usuarioCadastroService = new UsuarioCadastroService();

    /**
     * Construtor do {@code RegisterController}, inicializa a view de registro.
     * @param registerView a interface de registro associada
     * @param primaryStage o palco principal da aplicação
     */
    public RegisterController(RegisterView registerView, Stage primaryStage) {
        this.registerView = registerView;
        this.primaryStage = primaryStage;
        this.registerView.setRegisterController(this);
        setupEventHandlersRegister();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de registro.
     */
    public void setupEventHandlersRegister() {
        registerView.getBtnRegistrar().setOnAction(e -> handleCadastro());
        registerView.getVoltarLoginLink().setOnAction(e -> handleVoltarParaSessao());
    }

    /**
     * Neste método é manipulado o clique no link "Voltar" da tela de registro, retornando para a tela de login.
     */
    private void handleVoltarParaSessao() {
        System.out.println("RController: hyperLink de voltar sessão clicado");
        try {
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView, primaryStage);
            loginView.setLoginController(loginController);

            TelaService service = new TelaService();
            Scene loginScene = new Scene(loginView,service.medirWidth(),service.medirHeight());

            loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
            primaryStage.setTitle("Eventually - Login");
            primaryStage.setScene(loginScene);
        } catch (Exception ex) {
            System.err.println("RController: Erro ao navegar para a tela de Login: " + ex.getMessage());
            ex.printStackTrace();
            if (registerView.getLbErroGeral() != null) {
                Label erroLabel = registerView.getLbErroGeral();
                erroLabel.setText("RController: Erro ao tentar voltar para tela de login.");
                erroLabel.setVisible(true);
            }
        }
    }

    /**
     * Este método manipula o clique no botão de cadastro, redirecionando o usuário à tela de programação após cadastro.
     */
    private void handleCadastro() {
        System.out.println("RController: botão de registro clicado");
        handleCamposPreenchidos();

        UserScheduleView userScheduleView = new UserScheduleView();
        UserScheduleController userScheduleController = new UserScheduleController(userScheduleView, primaryStage);
        userScheduleView.setUserScheduleController(userScheduleController);

        TelaService service = new TelaService();
        Scene sceneUserSchedule = new Scene(userScheduleView,service.medirWidth(),service.medirHeight());

        sceneUserSchedule.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());
        primaryStage.setTitle("Eventually - Programação do Usuário");
        primaryStage.setScene(sceneUserSchedule);
        //falta: trow runtime exception se da erro
    }

    /**
     * Esté método lê os campos da interface de registro e envia os dados para cadastro via {@link UsuarioCadastroService}.
     */
    private void handleCamposPreenchidos() {
        String nome = registerView.getFldNome().getText();
        String email = registerView.getFldEmail().getText();
        String senha = registerView.getFldSenha().getText();
        String cidade = registerView.getFldCidade().getText();
        LocalDate dataInformada = registerView.getFldDataNascimento().getValue();
        String data= dataInformada.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        CadastrarUsuarioDto cadastroUsuarioDto = new CadastrarUsuarioDto(
                nome,
                email,
                senha,
                cidade,
                data,
                handlePreferenciasSelecionadas()
        );

        try {
            usuarioCadastroService.cadastrarNovoUsuario(cadastroUsuarioDto);
            System.out.println("RController: usuário cadastrado com sucesso!");
            //falta: exibir mensagem de sucesso na interface
        } catch (RuntimeException e) {
            System.err.println("RController: Erro ao cadastrar usuário: " + e.getMessage());
            registerView.getLbErroGeral().setText(e.getMessage());
            registerView.getLbErroGeral().setVisible(true);
        }
    }

    /**
     * Neste método as preferências selecionadas pelo usuário na interface são lidas e empacotadas em um DTO.
     * @return um objeto {@link PreferenciasUsuarioDto} contendo as preferências selecionadas.
     */
    private PreferenciasUsuarioDto handlePreferenciasSelecionadas() {
        PreferenciasUsuarioDto preferenciasUsuarioDto = new PreferenciasUsuarioDto(
                registerView.getCbCorporativo().isSelected(),
                registerView.getCbBeneficente().isSelected(),
                registerView.getCbEducacional().isSelected(),
                registerView.getCbCultural().isSelected(),
                registerView.getCbEsportivo().isSelected(),
                registerView.getCbReligioso().isSelected(),
                registerView.getCbSocial().isSelected()
        );
        return preferenciasUsuarioDto;
    }

    /**
     * Este método valida se o nome inserido atende às regras do sistema.
     * @param newVal o nome a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean conferirNome(String newVal) {
        return usuarioCadastroService.isRegraNomeCumprida(newVal);
    }

    /**
     * Este método valida se a senha inserida atende às regras do sistema.
     * @param newVal a senha a ser validada
     * @return um {@code Map} com cada regra e o resultado booleano da validação
     */
    public Map<String, Boolean> conferirSenha(String newVal) {
        return usuarioCadastroService.validarRegrasSenha(newVal);
    }
}