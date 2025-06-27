package com.eventually.controller;

import com.eventually.model.TemaPreferencia;
import com.eventually.service.*;
import com.eventually.view.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe controladora da tela de Configurações do usuário, é responsável pela comunicação da tela de de configurações
 * com o backend.
 * Contém todos os métodos como privados para que seu acesso seja somente por esta classe.
 * @version 1.01
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

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(SettingsController.class);

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
        this.navegacaoService = new NavegacaoService(primaryStage);

        configManiouladoresEventoConfig();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de configurações e, em caso de falha,
     * exibe uma mensagem no console.
     */
    private void configManiouladoresEventoConfig() {
        sistemaDeLogger.info("Método configManiouladoresEventoConfig() chamado.");
        try {
            settingsView.getBarraBuilder().getBtnConfiguracoes().setOnAction(e -> processarNavegacaoConfiguracoes());

            settingsView.getBarraBuilder().getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
            settingsView.getBarraBuilder().getBtnMeusEventos().setOnAction(e -> navegacaoService.navegarParaMeusEventos(emailRecebido));
            settingsView.getBarraBuilder().getBtnProgramacao().setOnAction(e -> navegacaoService.navegarParaProgramacao(emailRecebido));

            settingsView.getBarraBuilder().getBtnSair().setOnAction(e -> navegacaoService.abrirModalEncerrarSessao());
            settingsView.getBtnDeleteAccount().setOnAction(e -> processarDeletarConta());

            definirCheckBox(emailRecebido);

            settingsView.setAvatarImagem(definirImagem(emailRecebido));

            settingsView.getLbNomeUsuario().setText(definirNome(emailRecebido));
            settingsView.getLbEmailUsuario().setText(emailRecebido);
            settingsView.getLbSenhaUsuario().setText(definirSenha(emailRecebido));
            settingsView.getLbCidadeUsuario().setText(definirCidade(emailRecebido));
            settingsView.getLbDataNascUsuario().setText(definirDataNasc(emailRecebido));

            settingsView.getHlAlterarNome().setOnAction(e -> navegacaoService.abrirModalMudanca(settingsView, emailRecebido, "nome"));
            settingsView.getHlAlterarEmail().setOnAction(e -> navegacaoService.abrirModalMudanca(settingsView, emailRecebido, "email"));
            settingsView.getHlAlterarSenha().setOnAction(e -> navegacaoService.abrirModalMudanca(settingsView, emailRecebido, "senha"));
            settingsView.getHlAlterarCidade().setOnAction(e -> navegacaoService.abrirModalMudanca(settingsView, emailRecebido, "cidade"));
            settingsView.getHlAlterarDataNasc().setOnAction(e -> navegacaoService.abrirModalMudanca(settingsView, emailRecebido, "data de nascimento"));

            settingsView.getHlAlterarFoto().setOnAction(e->navegacaoService.abrirMudancaImagemModal(settingsView, emailRecebido, "foto"));

            //get all check boxes
        //    settingsView.getHlAlterarPreferencias().setOnAction(e-> confirmarAlteracoes(/"receber valores"/));
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de configurações: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Pega as preferências salvas do usuário e atualiza o estado
     * dos checkboxes individuais na tela para refletir essas preferências.
     * @param email o email do usuário para buscar as preferências.
     */
    private void definirCheckBox(String email) {
        Set<TemaPreferencia> temasSalvos = usuarioSessaoService.procurarPreferencias(email);;

        if (temasSalvos == null) {
            sistemaDeLogger.info("Usuário não possui preferências de tema salvas. Desmarcando todos os checkboxes.");
            settingsView.getCbCorporativo().setSelected(false);
            settingsView.getCbEducacional().setSelected(false);
            settingsView.getCbCultural().setSelected(false);
            settingsView.getCbEsportivo().setSelected(false);
            settingsView.getCbBeneficente().setSelected(false);
            settingsView.getCbReligioso().setSelected(false);
            settingsView.getCbSocial().setSelected(false);
            return;
        }

        settingsView.getCbCorporativo().setSelected(temasSalvos.contains(TemaPreferencia.CORPORATIVO));
        settingsView.getCbEducacional().setSelected(temasSalvos.contains(TemaPreferencia.EDUCACIONAL));
        settingsView.getCbCultural().setSelected(temasSalvos.contains(TemaPreferencia.CULTURAL));
        settingsView.getCbEsportivo().setSelected(temasSalvos.contains(TemaPreferencia.ESPORTIVO));
        settingsView.getCbBeneficente().setSelected(temasSalvos.contains(TemaPreferencia.BENEFICENTE));
        settingsView.getCbReligioso().setSelected(temasSalvos.contains(TemaPreferencia.RELIGIOSO));
        settingsView.getCbSocial().setSelected(temasSalvos.contains(TemaPreferencia.SOCIAL));
    }

    /**
     * Este método retorna a imagem de perfil do usuário, se foi recém cadastrado no sistema, terá a imagem padrão.
     * @param email informado no cadastro.
     * @return retorna a imagem do usuário relativo ao email cadastrado.
     */
    private Image definirImagem(String email) {
        sistemaDeLogger.info("Método definirImagem() chamado.");
        try {
            Image imagemUsuario = usuarioSessaoService.procurarImagem(email);
            return imagemUsuario;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter imagem do usuário: "+e.getMessage());
            e.printStackTrace();
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
            String nome = usuarioSessaoService.procurarNome(email);
            return nome;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter nome do usuário: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método retorna a senha do usuário e, em caso de falha, é exibida uma mensagem de erro no console.
     * @param email informado no cadastro.
     * @return retorna a senha do usuário relativo ao email cadastrado.
     */
    private String definirSenha(String email) {
        sistemaDeLogger.info("Método definirSenha() chamado.");
        try {
            String senha = usuarioSessaoService.procurarSenha(email);
            return senha;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter senha do usuário: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método retorna a cidade do usuário e, em caso de falha, é exibida uma mensagem de erro no console.
     * @param email informado no cadastro.
     * @return retorna a cidade do usuário relativo ao email cadastrado.
     */
    private String definirCidade(String email) {
        sistemaDeLogger.info("Método definirCidade() chamado.");
        try {
            String cidade = usuarioSessaoService.procurarCidade(email);
            return cidade;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter cidade do usuário: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método retorna a data de nascimento do usuário e, em caso de falha, é exibida uma mensagem de erro no console.
     * @param email informado no cadastro.
     * @return retorna a data de nascimento do usuário relativo ao email cadastrado.
     */
    private String definirDataNasc(String email) {
        sistemaDeLogger.info("Método definirDataNasc() chamado.");
        try {
            String dataNasc = usuarioSessaoService.procurarDataNasc(email);
            return dataNasc;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter data de nascimento do usuário: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

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