package com.eventually.controller;

import com.eventually.service.*;
import com.eventually.view.modal.MudancaModal;
import com.eventually.view.SettingsView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe controladora para o modal de Confirmação de mudança de informação do usuário, gerencia a lógica de validação
 * dinâmica com para se comunicar com o modal e faz a atualização com método privados (acessados somente por esta
 * classe)
 * @author Yuri Garcia Maia (Estrutura base)
 * @version 1.04
 * @since 2025-05-23
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-05-29
 */
public class MudancaController {
    private final MudancaModal mudancaModal;

    private UsuarioAtualizacaoService usuarioAtualizacaoService;
    private UsuarioSessaoService usuarioSessaoService;
    private SettingsView settingsView;

    private String emailRecebido, valorRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(MudancaController.class);

    /**
     * Construtor do MudancaController que obtém a instância única de UsuarioAtualizacaoService e
     * UsuarioSessaoService para acessar a lista de usuários e inicializa o modal de alteração de informação do usuário.
     * @param mudancaModal a instância do modal MudancaModal associada.
     */
    public MudancaController(SettingsView settingsView, String email, String valor, MudancaModal mudancaModal) {
        this.settingsView = settingsView;
        this.usuarioAtualizacaoService = UsuarioAtualizacaoService.getInstancia();
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao SettingsView, UsuarioAtualizacaoService, UsuarioCadastroService e UsuarioSessaoService");

        this.emailRecebido = email;
        this.valorRecebido = valor;
        this.mudancaModal = mudancaModal;
        this.mudancaModal.setMudancaControlador(this);
        configManipuladoresEventoConfirmacaoMudanca();
    }

    /**
     * Configura os manipuladores de evento para os componentes do modal de alteração de informacao do usuário
     * Este método associa as ações dos botões e do modal e, em caso de falha na configuração
     * de algum manipulador de evento, uma mensagem de erro é exibida no console.
     */
    private void configManipuladoresEventoConfirmacaoMudanca() {
        sistemaDeLogger.info("Método configManipuladoresEventoConfirmacaoMudanca() chamado.");
        try {
            mudancaModal.getLbMensagem().setText("Alterar " + valorRecebido);
            mudancaModal.getFldEditado().setPromptText("Digite um novo valor para "+ valorRecebido);
            mudancaModal.getBtnSalvarSenha().setOnAction(e -> processarMudanca());
            mudancaModal.getBtnFechar().setOnAction(e -> processarFecharModal());
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de mudança: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Manipula a solicitação de alteração de um valor do usuário vinda da mudancaModal e, em caso de falha,
     * uma mensagem de erro é exibida no console.
     */
    public void processarMudanca() {
        sistemaDeLogger.info("Método processarMudanca() chamado.");
        String novoValorTexto = mudancaModal.getFldEditado().getText();
        try {
            int id = usuarioSessaoService.procurarID(emailRecebido);
            if (id == -1) {
                alertaService.alertarErro("Usuário não encontrado.");
                return;
            }
            boolean sucesso = false;

            switch (valorRecebido.toLowerCase()) {
                case "nome":
                    sucesso = usuarioAtualizacaoService.atualizarNome(id, novoValorTexto);
                    break;
                case "email":
                    sucesso = usuarioAtualizacaoService.atualizarEmail(id, novoValorTexto);
                    break;
                case "senha":
                    sucesso = usuarioAtualizacaoService.atualizarSenha(id, novoValorTexto);
                    break;
                case "cidade":
                    sucesso = usuarioAtualizacaoService.atualizarCidade(id, novoValorTexto);
                    break;
                case "data de nascimento":
                    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("ddMMuuuu");
                    LocalDate dataNascimentoConvertida = LocalDate.parse(novoValorTexto, formatador);
                    sucesso = usuarioAtualizacaoService.atualizarDataNascimento(id, dataNascimentoConvertida);
                    break;
                default:
                    sistemaDeLogger.warn("Tentativa de atualização de campo desconhecido: {}", valorRecebido);
                    alertaService.alertarErro("Operação desconhecida.");
                    return;
            }

            if (sucesso) {
                atualizarInterface(novoValorTexto);
                processarFecharModal();
            }

        } catch (DateTimeParseException e) {
            sistemaDeLogger.error("Formato de data inválido: {}", novoValorTexto, e);
            alertaService.alertarErro("Formato de Data Inválido: Use o formato ddMMyyyy.");

        } catch (Exception e) {
            sistemaDeLogger.error("Algum erro ocorreu na mudança de "+ valorRecebido +": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Atualiza o label correspondente na SettingsView após uma alteração bem-sucedida e, em caso de falha, uma
     * mensagem é exibida no console.
     * @param novoValor o novo texto a ser exibido na interface.
     */
    private void atualizarInterface(String novoValor) {
        sistemaDeLogger.info("Método atualizarInterface() chamado.");
        try {
            switch (valorRecebido.toLowerCase()) {
                case "nome":
                    settingsView.getLbNomeUsuario().setText(novoValor);
                    break;
                case "email":
                    settingsView.getLbEmailUsuario().setText(novoValor);
                    break;
                case "senha":
                    settingsView.getLbSenhaUsuario().setText("********");
                    break;
                case "cidade":
                    settingsView.getLbCidadeUsuario().setText(novoValor);
                    break;
                case "data de nascimento":
                    settingsView.getLbDataNascUsuario().setText(novoValor);
                    break;
            }
        } catch (Exception e) {
        sistemaDeLogger.error("Algum erro ocorreu na mudança de "+ valorRecebido +": " + e.getMessage());
        e.printStackTrace();
        }
    }

    /**
     * Fecha o modal e, em caso de falha uma mensagem de erro é exibida no console.
     * @return nulo para que o modal finalize sua exibição e operação.
     */
    private void processarFecharModal() {
        sistemaDeLogger.info("Método processarFecharModal   () chamado.");
        try {
            mudancaModal.close();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao fechar o modal: " + e.getMessage());
        }
    }
}