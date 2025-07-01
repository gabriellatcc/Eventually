package com.eventually.controller;

import com.eventually.service.AlertaService;
import com.eventually.service.EmailService;
import com.eventually.view.EsqueceuSenhaModal;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe controladora do modal de "Esqueceu sua senha", é responsável pela comunicação do modal com o backend.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e da estrutura da classe)
 * @version 1.02
 * @since 2025-05-23
 */
public class  EsqueceuSenhaController {
    private final EsqueceuSenhaModal esqueceuSenhaModal;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(EsqueceuSenhaController.class);

    /**
     * Construtor da classe {@code EsqueceuSenhaController} que i nicializa o controlador com o modal de "Esqueceu a
     * Senha" e configura os manipuladores de eventos.
     * @param esqueceuSenhaModal a instância do modal de "Esqueceu a Senha" a ser controlada.
     */
    public EsqueceuSenhaController(EsqueceuSenhaModal esqueceuSenhaModal) {
        this.esqueceuSenhaModal = esqueceuSenhaModal;
        this.esqueceuSenhaModal.setForgotPasswordController(this);
        configManipuladoresEventoEsqueceuSenha();
    }

    /**
     * Configura os eventos de clique dos botões "Enviar" e "Fechar" no modal Esqueceu Senha e, em caso de falha na
     * configuração dos manipuladores de evento, é exibida uma mensagem no console.
     */
    private void configManipuladoresEventoEsqueceuSenha() {
        sistemaDeLogger.info("Método configManipuladoresEventoEsqueceuSenha() chamado.");
        try {
            esqueceuSenhaModal.getBtnEnviar().setOnAction(event -> {enviarEmail();});
            esqueceuSenhaModal.getBtnFechar().setOnAction(event-> fecharModal());
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores do modal de Esqueceu Senha: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Aciona o envio de um e-mail com uma nova senha temporária, utilizando o e-mail informado no campo de texto e, em
     * caso de falha na configuração dos manipuladores de evento, é exibida uma mensagem no console.
     */
    private void enviarEmail() {
        sistemaDeLogger.info("Método enviarEmail() chamado.");
        try {
            sistemaDeLogger.info("Botão de enviar email clicado!");
            String email = esqueceuSenhaModal.getFldEmail().getText();
            if (email == null || email.isEmpty()) {
                alertaService.alertarWarn("Erro ao enviar email","O campo de email deve ser preenchido.");
                return;
            } else {
                sistemaDeLogger.info("Enviando e-mail...");
                EmailService emailService = new EmailService(email);
                emailService.enviar();
            }} catch (Exception e) {
                sistemaDeLogger.error("Erro ao enviar email para o usuário: "+e.getMessage());
                e.printStackTrace();
        }
    }

    /**
     * Fecha o modal atual, encerrando sua janela e, em caso de falha na configuração dos manipuladores de evento, é
     * exibida uma mensagem no console.
     * @return nulo para que o modal finalize sua exibição e operação.
     */
    private void fecharModal() {
        sistemaDeLogger.info("Método fecharModal() chamado.");
        try {
            sistemaDeLogger.info("Botão de Fechar clicado!");
            esqueceuSenhaModal.close();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao fechar o modal: " + e.getMessage());
        }
    }
}