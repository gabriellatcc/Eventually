package com.eventually.controller;
import com.eventually.dto.CadastrarUsuarioDto;
import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.service.AlertService;
import com.eventually.service.UsuarioCadastroService;
import com.eventually.service.TelaService;
import com.eventually.view.LoginView;
import com.eventually.view.RegisterView;
import com.eventually.view.UserScheduleView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

/**
 * Classe controller da tela de registro.
 * Esta classe é responsável pela comunicação
 * da tela de registro com o backend.
 * @author Yuri Garcia Maia
 * @version 1.01
 * @since 2025-05-13
 * @author Gabriella Tavares Costa Corrêa (Documentação, revisão da estrutura e lógica da classe)
 * @since 2025-05-14
 */
public class RegisterController {
    private final RegisterView registerView;
    private final Stage primaryStage;

    private AlertService alertService=new AlertService();
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
            System.err.println("RC: Erro ao navegar para a tela de Login: " + ex.getMessage());
            ex.printStackTrace();
            alertService.alertarErro("Erro ao navegar para a tela de Login.");
        }
    }

    /**
     * Este método manipula o clique no botão de cadastro, redirecionando o usuário à tela de programação após cadastro.
     */
    private void handleCadastro() {
        System.out.println("RController: botão de registro clicado");
        try {
            String nome = registerView.getFldNome().getText();
            String email = registerView.getFldEmail().getText();
            String senha = registerView.getFldSenha().getText();
            String cidade = registerView.getFldCidade().getText();
            LocalDate data = registerView.getFldDataNascimento().getValue();

            PreferenciasUsuarioDto preferencias = new PreferenciasUsuarioDto(
                    registerView.getCbCorporativo().isSelected(),
                    registerView.getCbBeneficente().isSelected(),
                    registerView.getCbEducacional().isSelected(),
                    registerView.getCbCultural().isSelected(),
                    registerView.getCbEsportivo().isSelected(),
                    registerView.getCbReligioso().isSelected(),
                    registerView.getCbSocial().isSelected()
            );

            CadastrarUsuarioDto dto = new CadastrarUsuarioDto(nome, email, senha, cidade, data, preferencias);

            UsuarioCadastroService cadastroService = new UsuarioCadastroService();
            boolean cadastroFoiOk = cadastroService.cadastrarUsuarioSeValido(dto);

            if (!cadastroFoiOk) {
                // Erro já foi alertado dentro do service
                return;
            }

            // Agora sim pode mudar de tela
            UserScheduleView userScheduleView = new UserScheduleView();
            UserScheduleController userScheduleController = new UserScheduleController(userScheduleView, primaryStage);
            userScheduleView.setUserScheduleController(userScheduleController);

            TelaService service = new TelaService();
            Scene sceneUserSchedule = new Scene(userScheduleView, service.medirWidth(), service.medirHeight());

            sceneUserSchedule.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());
            primaryStage.setTitle("Eventually - Programação do Usuário");
            primaryStage.setScene(sceneUserSchedule);

        } catch (Exception ex) {
            System.err.println("RController: Erro ao navegar para a tela de Menu: " + ex.getMessage());
            ex.printStackTrace();
            alertService.alertarErro("Erro ao navegar para a tela de Menu.");
        }
    }


    /**
     * Esté método lê os campos da interface de registro e envia os dados para cadastro via {@link UsuarioCadastroService}.
     */
    private void handleCamposPreenchidos() {
        try {
            String nome = registerView.getFldNome().getText();
            String email = registerView.getFldEmail().getText();
            String senha = registerView.getFldSenha().getText();
            String cidade = registerView.getFldCidade().getText();
            LocalDate data = registerView.getFldDataNascimento().getValue();

            CadastrarUsuarioDto cadastroUsuarioDto = new CadastrarUsuarioDto(
                    nome,
                    email,
                    senha,
                    cidade,
                    data,
                    handlePreferenciasSelecionadas()
            );

            boolean sucesso = usuarioCadastroService.cadastrarUsuarioSeValido(cadastroUsuarioDto);
            if (sucesso) {
                System.out.println("RC: usuário cadastrado com sucesso!");
                alertService.alertarInfo("Sucesso ao fazer o cadastro!");
            }

        } catch (Exception ex) {
            System.err.println("RC: erro ao obter dados do formulário.");
            ex.printStackTrace();
            alertService.alertarErro("Erro ao obter dados do formulário");
        }
    }

    /**
     * Neste método as preferências selecionadas pelo usuário na interface são lidas e empacotadas em um DTO.
     * @return um objeto {@link PreferenciasUsuarioDto} contendo as preferências selecionadas.
     */
    private PreferenciasUsuarioDto handlePreferenciasSelecionadas() {
        try{
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
        } catch (Exception ex) {
          System.err.println("RC: Erro ao receber preferências");
          ex.printStackTrace();
          alertService.alertarErro("Erro ao receber preferências.");
          return null;
      }
    }

    /**
     * Este método valida se o nome inserido atende às regras do sistema.
     * @param novoValorNome o nome a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean conferirNome(String novoValorNome) {
        try{
            return usuarioCadastroService.isRegraNomeCumprida(novoValorNome);
        }
        catch (RuntimeException e) {
            System.out.println("RC: ocorreu um erro ao conferir o nome.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método valida se o email inserido atende às regras do sistema.
     * @param novoValorEmail o email a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean conferirEmail(String novoValorEmail, boolean exibirAlertas) {
        try{
            return usuarioCadastroService.isRegraEmailCumprida(novoValorEmail, exibirAlertas);
        }
        catch (RuntimeException e) {
            System.out.println("RC: ocorreu um erro ao conferir o email.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método valida se a senha inserida atende às regras do sistema.
     * @param novoValorSenha a senha a ser validada
     * @return um {@code Map} com cada regra e o resultado booleano da validação
     */
    public  Map<String, Boolean> conferirSenhaParaUI(String novoValorSenha) {
        try {
            return usuarioCadastroService.isRegraSenhaCumprida(novoValorSenha);
        } catch (RuntimeException e) {
            System.out.println("RC: ocorreu um erro ao conferir a senha para UI.");
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    /**
     * Este método valida se a data inserida atende às regras do sistema.
     * @param novoValorData a data a ser validada
     * @return true se válido, false caso contrário
     */
    public boolean conferirDataNasc(LocalDate novoValorData) {
        try{
            return usuarioCadastroService.isRegraDataCumprida(novoValorData);
        }
        catch (RuntimeException e) {
            System.out.println("RC: ocorreu um erro ao conferir a data de nascimento.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método valida se a cidade inserida atende às regras do sistema.
     * @param novoValorCidade a cidade a ser validada
     * @return true se válido, false caso contrário
     */
    public boolean conferirCidade(String novoValorCidade) {
        try{
            return usuarioCadastroService.isRegraCidadeCumprida(novoValorCidade);
        }
        catch (RuntimeException e) {
            System.out.println("RC: ocorreu um erro ao conferir a cidade.");
            e.printStackTrace();
            return false;
        }
    }
}