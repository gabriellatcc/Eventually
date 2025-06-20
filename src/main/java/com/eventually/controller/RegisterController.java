package com.eventually.controller;
import com.eventually.dto.CadastrarUsuarioDto;
import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.service.*;
import com.eventually.view.RegisterView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

/**
 * Classe controladora da tela de registro, é responsável pela comunicação com o backend.
 * Contém métodos como privados para que seu acesso seja somente por esta classe e métodos públicos para
 * validações dinâmicas e visuais na interface.
 * @version 1.01
 * @author Yuri Garcia Maia (Estrutura base)
 * @since 2025-05-13
 * @author Gabriella Tavares Costa Corrêa (Documentação, revisão da estrutura e refatoração da parte lógica da classe)
 * @since 2025-05-14
 */
public class RegisterController {
    private final RegisterView registerView;
    private final Stage primaryStage;

    private UsuarioCadastroService usuarioCadastroService;
    private UsuarioSessaoService usuarioSessaoService;
    private NavegacaoService navegacaoService;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(RegisterController.class);

    /**
     * Construtor do {@code RegisterController} que obtém a instância única de UsuarioCadastroService para acessar a
     * lista de usuários e inicializa a view de registro.
     * @param registerView a interface de registro associada.
     * @param primaryStage o palco principal da aplicação.
     */
    public RegisterController(RegisterView registerView, Stage primaryStage) {
        this.usuarioCadastroService = UsuarioCadastroService.getInstancia();
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioCadastroService e UsuarioSessaoService.");

        this.registerView = registerView;
        this.registerView.setRegisterController(this);

        this.primaryStage = primaryStage;
        this.navegacaoService = new NavegacaoService(primaryStage);

        configManipuladoresEventoRegistro();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de registro e em caso
     * de falha na configuração de algum manipulador de evento, uma mensagem de erro é exibida no console.
     */
    private void configManipuladoresEventoRegistro() {
        sistemaDeLogger.info("Método configManipuladoresEventoRegistro() chamado.");
        try {
            registerView.getBtnRegistrar().setOnAction(e -> processarCadastro());
            registerView.getVoltarLoginLink().setOnAction(e -> navegacaoService.navegarParaLogin());
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de registro: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método manipula o clique no botão de cadastro, redirecionando o usuário à tela de programação
     * após cadastro e em caso de falha, uma mensagem de erro é exibida no console.
     */
    private void processarCadastro() {
        sistemaDeLogger.info("Método processarCadastro() chamado.");
        try {
            sistemaDeLogger.info("Botão de registro clicado!");

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

            boolean cadastroFoiOk = usuarioCadastroService.cadastrarUsuarioSeValido(dto);

            if (cadastroFoiOk) {
                navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(dto.email()));
            }
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Menu: " + ex.getMessage());
            ex.printStackTrace();
            alertaService.alertarErro("Erro ao navegar para a tela de Menu.");
        }
    }

    /**
     * Este método valida se o nome inserido atende às regras do sistema e em caso de falha na validação, uma mensagem
     * de erro é exibida no console.
     * @param novoValorNome o nome a ser validado.
     * @return true se válido, false caso contrário.
     */
    public boolean conferirNome(String novoValorNome) {
        sistemaDeLogger.info("Método conferirNome() chamado.");
        try{
            return usuarioCadastroService.isRegraNomeCumprida(novoValorNome);
        } catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao conferir o nome:"+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método valida se o email inserido atende às regras do sistema e em caso de falha na validação, uma mensagem
     * de erro é exibida no console.
     * @param novoValorEmail o email a ser validado.
     * @return true se válido, false caso contrário.
     */
    public boolean conferirEmail(String novoValorEmail) {
        sistemaDeLogger.info("Método conferirEmaile() chamado.");
        try{
            return usuarioCadastroService.isRegraEmailCumprida(novoValorEmail);
        } catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao conferir o email: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método valida se a senha inserida atende às regras do sistema e em caso de falha na validação, uma mensagem
     * de erro é exibida no console.
     * @param novoValorSenha a senha a ser validada.
     * @return um {@code Map} com cada regra e o resultado booleano da validação.
     */
    public  Map<String, Boolean> conferirSenha(String novoValorSenha) {
        sistemaDeLogger.info("Método conferirSenha() chamado.");
        try {
            return usuarioCadastroService.isRegraSenhaCumprida(novoValorSenha);
        } catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao conferir a senha: "+e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    /**
     * Este método valida se a data inserida atende às regras do sistema e em caso de falha na validação, uma mensagem
     * de erro é exibida no console.
     * @param novoValorData a data a ser validada.
     * @return true se válido, false caso contrário.
     */
    public boolean conferirDataNasc(LocalDate novoValorData) {
        sistemaDeLogger.info("Método conferirDataNasc() chamado.");
        try{
            return usuarioCadastroService.isRegraDataCumprida(novoValorData);
        } catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao conferir a data de nascimento: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método valida se a cidade inserida atende às regras do sistema e em caso de falha na validação, uma mensagem
     * de erro é exibida no console.
     * @param novoValorCidade a cidade a ser validada.
     * @return true se válido, false caso contrário.
     */
    public boolean conferirCidade(String novoValorCidade) {
        sistemaDeLogger.info("Método conferirCidade() chamado.");
        try{
            return usuarioCadastroService.isRegraCidadeCumprida(novoValorCidade);
        } catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao conferir a cidade: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}