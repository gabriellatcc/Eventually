package com.eventually.controller;

import com.eventually.model.Comunidade;
import com.eventually.service.*;
import com.eventually.view.*;
import com.eventually.view.modal.DeletarContaConfirmModal;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * Classe controladora da tela de Configurações do usuário, é responsável pela comunicação da tela de de configurações
 * com o backend.
 * Contém todos os métodos como privados para que seu acesso seja somente por esta classe.
 * @version 1.08
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
            settingsView.getBarraBuilder().getBtnConfiguracoes().setDisable(true);
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
            settingsView.getHlAlterarSenha().setOnAction(e -> navegacaoService.abrirModalMudanca(settingsView, emailRecebido, "senha"));
            settingsView.getHlAlterarCidade().setOnAction(e -> navegacaoService.abrirModalMudanca(settingsView, emailRecebido, "cidade"));
            settingsView.getHlAlterarDataNasc().setOnAction(e -> navegacaoService.abrirModalMudanca(settingsView, emailRecebido, "data de nascimento"));

            settingsView.getHlAlterarFoto().setOnAction(e->navegacaoService.abrirMudancaImagemModal(settingsView, emailRecebido, "foto"));

            settingsView.getbtnAlterarPreferencias().setOnAction(e -> {abrirModalParaEdicao();});
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de configurações: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirModalParaEdicao() {
        navegacaoService.abrirModalEditarFiltros(emailRecebido);

        sistemaDeLogger.info("Modal de edição de comunidades fechado. Atualizando a tela de configurações.");
        atualizarVisualizacaoPreferencias();
    }

    /**
     * Pega as preferências salvas do usuário e atualiza o estado
     * dos checkboxes individuais na tela para refletir essas preferências.
     * @param email o email do usuário para buscar as preferências.
     */
    private void definirCheckBox(String email) {
        Set<Comunidade> comunidadesSalvas = usuarioSessaoService.procurarPreferencias(email);;

        if (comunidadesSalvas == null) {
            sistemaDeLogger.info("Usuário não possui preferências de comunidades salvas. Desmarcando todos os checkboxes.");
            settingsView.getCbCorporativo().setSelected(false);
            settingsView.getCbEducacional().setSelected(false);
            settingsView.getCbCultural().setSelected(false);
            settingsView.getCbEsportivo().setSelected(false);
            settingsView.getCbBeneficente().setSelected(false);
            settingsView.getCbReligioso().setSelected(false);
            settingsView.getCbSocial().setSelected(false);
            return;
        }

        settingsView.getCbCorporativo().setSelected(comunidadesSalvas.contains(Comunidade.CORPORATIVO));
        settingsView.getCbEducacional().setSelected(comunidadesSalvas.contains(Comunidade.EDUCACIONAL));
        settingsView.getCbCultural().setSelected(comunidadesSalvas.contains(Comunidade.CULTURAL));
        settingsView.getCbEsportivo().setSelected(comunidadesSalvas.contains(Comunidade.ESPORTIVO));
        settingsView.getCbBeneficente().setSelected(comunidadesSalvas.contains(Comunidade.BENEFICENTE));
        settingsView.getCbReligioso().setSelected(comunidadesSalvas.contains(Comunidade.RELIGIOSO));
        settingsView.getCbSocial().setSelected(comunidadesSalvas.contains(Comunidade.SOCIAL));
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
     * Carrega as preferências de tema mais recentes do serviço
     * e atualiza o estado das checkboxes na tela de configurações.
     */
    public void atualizarVisualizacaoPreferencias() {
        sistemaDeLogger.info("Atualizando visualização das preferências de conteúdo.");

        Set<Comunidade> comumAtuais = usuarioSessaoService.procurarPreferencias(emailRecebido);

        Map<Comunidade, CheckBox> mapaCheckBoxes = settingsView.getMapaDeCheckBoxesDeComunidades();

        mapaCheckBoxes.values().forEach(cb -> cb.setSelected(false));

        for (Comunidade comm : comumAtuais) {
            if (mapaCheckBoxes.containsKey(comm)) {
                mapaCheckBoxes.get(comm).setSelected(true);
            }
        }
    }

    /**
     * Este método abre o modal no qual o usuário seleciona se quer deletar a conta ou não, na operação é procurado o ID
     * do usuario e seu email, para que seja alterado o estado do objeto usuário com o email específico para "false", ou
     * seja, inativo.
     */
    private void processarDeletarConta() {
        try {
            DeletarContaConfirmModal confirmModal = new DeletarContaConfirmModal();

            boolean usuarioConfirmou = confirmModal.showAndWait(primaryStage);

            if (usuarioConfirmou) {
                int idEncontrado = usuarioSessaoService.procurarID(emailRecebido);;
                usuarioExclusaoService.alterarEstadoDoUsuario(idEncontrado,false);

                alertaService.alertarInfo("Sua conta foi removida com sucesso.");
                navegacaoService.navegarParaLogin();
            } else {
                sistemaDeLogger.info("Usuário cancelou a exclusão da conta.");
            }

        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao processar a exclusão da conta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}