package com.eventually.controller;

import com.eventually.dto.UsuarioEdicaoDto;
import com.eventually.model.UsuarioModel;
import com.eventually.service.AlertaService;
import com.eventually.service.UsuarioCadastroService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.ConfirmaMudancaModal;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe controladora para o modal de Confirmação de mudança de Senha, gerencia a lógica de validação dinâmica com
 * métodos públicos para se comunicar com o modal e faz a atualização de senha com método privados (acessados somente
 * por esta classe)
 * @author Yuri Garcia Maia (Estrutura base)
 * @version 1.03
 * @since 2025-05-23
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-05-29
 */
public class ConfirmaMudancaController {
    private ConfirmaMudancaModal confirmaMudancaModal;

    private UsuarioCadastroService usuarioCadastroService;
    private UsuarioSessaoService usuarioSessaoService;

    private String emailRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(ConfirmaMudancaController.class);

    /**
     * Construtor do ConfirmaMudancaController que obtém a instância única de UsuarioCadastroService e
     * UsuarioSessaoService para acessar a lista de usuários e inicializa o modal de alteração de senha.
     * @param email o email do usuário que terá a senha alterada.
     * @param confirmaMudancaModal a instância do modal ConfirmaMudancaModal associada.
     */
    public ConfirmaMudancaController(String email, ConfirmaMudancaModal confirmaMudancaModal) {
        this.usuarioCadastroService = UsuarioCadastroService.getInstancia();
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioCadastroService e UsuarioSessaoService");

        this.emailRecebido = email;
        this.confirmaMudancaModal = confirmaMudancaModal;
        this.confirmaMudancaModal.setChangePasswordController(this);
        configManipuladoresEventoConfirmacaoMudanca();
    }

    /**
     * Configura os manipuladores de evento para os componentes do modal de alteração de senha
     * Este método associa as ações dos botões e do modal e, em caso de falha na configuração
     * de algum manipulador de evento, uma mensagem de erro é exibida no console.
     */
    private void configManipuladoresEventoConfirmacaoMudanca() {
        sistemaDeLogger.info("Método configManipuladoresEventoConfirmacaoMudanca() chamado.");
        try {
            confirmaMudancaModal.getBtnSalvarSenha().setOnAction(e -> processarMudancaDeSenha());
            confirmaMudancaModal.getBtnFechar().setOnAction(processarFecharModal());
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Manipula a solicitação de alteração de senha vinda da confirmaMudancaModal e, em caso de falha,
     * uma mensagem de erro é exibida no console.
     */
    public void processarMudancaDeSenha() {
        sistemaDeLogger.info("Método processarMudancaDeSenha() chamado.");
        try {
            sistemaDeLogger.info("Botão Salvar clicado!");

            String senhaAtual = confirmaMudancaModal.getFldSenhaAtual().getText();
            String senhaNova = confirmaMudancaModal.getFldNovaSenha().getText();
            String confirmacaoSenha = confirmaMudancaModal.getFldConfirmarNovaSenha().getText();

            if (senhaAtual.isEmpty() || senhaNova.isEmpty() || confirmacaoSenha.isEmpty()) {
                alertaService.alertarWarn("Mudança inválida","Todos os campos de senha devem ser preenchidos corretamente.");
                return;
            }
            if (!conferirSenhaAtual(senhaAtual)) {
                return;
            }
            if (!conferirConfirmacaoSenha(senhaNova, confirmacaoSenha)) {
                alertaService.alertarWarn("Falha na mudança", "A nova senha e a confirmação não coincidem.");
                return;
            }

            Map<String, Boolean> regras = usuarioCadastroService.isRegraSenhaCumprida(senhaNova);
            if (regras == null || !regras.values().stream().allMatch(b -> b)) {
                alertaService.alertarWarn("Edição Inválida", "A nova senha não cumpre todos os requisitos.");
                return;
            }

            int idUsuario = usuarioSessaoService.procurarID(emailRecebido);
            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.buscarUsuarioPorId(idUsuario);
            if (usuarioOptional.isPresent()) {
                UsuarioModel usuario = usuarioOptional.get();
                usuario.setSenha(senhaNova);
                alertaService.alertarInfo("Senha alterada com sucesso!");
                confirmaMudancaModal.close();
            } else {
                usuarioOptional = usuarioCadastroService.buscarUsuarioPorId(idUsuario);
                if (usuarioOptional.isPresent()) {
                    UsuarioModel usuario = usuarioOptional.get();
                    usuario.setSenha(confirmacaoSenha);
                    UsuarioEdicaoDto atributosEditados;

                } else {
                    alertaService.alertarWarn("Edição Inválida", "Senha não cumpre os requisitos.");
                    return;
                }
            }

        } catch (Exception e) {
            sistemaDeLogger.error("Algum erro ocorreu na mudança de senha: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fecha o modal e, em caso de falha uma mensagem de erro é exibida no console.
     * @return nulo para que o modal finalize sua exibição e operação.
     */
    private EventHandler<ActionEvent> processarFecharModal() {
        return event -> {
            sistemaDeLogger.info("Método processarFecharModal() chamado. ");
            try {
                sistemaDeLogger.info("Botão Fechar clicado!");
                confirmaMudancaModal.close();
            } catch (Exception e) {
                sistemaDeLogger.error("Erro ao fechar o modal: "+e.getMessage());
            }
        };
    }

    /**
     * Este método valida se a senha inserida corresponde a atual do usuário cadastrado no sistema
     * e, em caso de falha na validação, uma mensagem de erro é exibida no console.
     * @param valorSenhaAtual a senha a ser validada.
     * @return true se válido, false caso contrário.
     */
    public boolean conferirSenhaAtual(String valorSenhaAtual) {
        sistemaDeLogger.info("Método conferirSenhaAtual() chamado.");
        try {
            String senhaEncontrada = usuarioSessaoService.procurarSenha(emailRecebido);
            if (senhaEncontrada.equals(valorSenhaAtual)) {
                return true;
            } else {
                alertaService.alertarWarn("Falha na mudança", "A senha informada não coincide com a atual.");
                return false;}
        } catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao conferir a senha atual: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método valida se a nova senha inserida atende às regras do sistema.
     * Ele não atualiza a view diretamente, apenas retorna o resultado da validação.
     * @param valorSenhaNova a senha a ser validada.
     * @return um {@code Map} com cada regra e o resultado booleano da validação.
     */
    public Map<String, Boolean> conferirSenhaNova(String valorSenhaNova) {
        sistemaDeLogger.info("Método conferirSenhaNova() chamado.");
        try {
            if (valorSenhaNova == null || valorSenhaNova.isEmpty()) {
                return Map.of(
                        "hasSixChar", false,
                        "hasSpecial", false,
                        "hasDigit", false,
                        "hasLetter", false
                );
            }

            Map<String, Boolean> regrasValidadas = usuarioCadastroService.isRegraSenhaCumprida(valorSenhaNova);
            return regrasValidadas != null ? regrasValidadas : Collections.emptyMap();
        } catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao conferir a nova senha: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    /**
     * Este método valida se a nova senha inserida e a confirmação dela coincidem.
     * @param valorSenhaNova a nova senha informada.
     * @param valorConfirmaSenha a confirmação da nova senha.
     * @return true se válido, false caso contrário.
     */
    public boolean conferirConfirmacaoSenha(String valorSenhaNova, String valorConfirmaSenha) {
        sistemaDeLogger.info("Método conferirConfirmacaoSenha() chamado.");
        return valorSenhaNova.equals(valorConfirmaSenha);
    }
}
