package com.eventually.controller;

import com.eventually.service.EmailService;
import com.eventually.view.ForgotPasswordModal;

/**
 * Classe controller do modal de "Esqueceu sua senha"
 * Esta classe é responsável pela comunicação
 * do modal com o backend.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e da estrutura da classe)
 * @version 1.0
 * @since 23-05-2025
 */
public class ForgotPasswordController {
    private final ForgotPasswordModal forgotPasswordModal;
    public ForgotPasswordController(ForgotPasswordModal forgotPasswordModal) {
        this.forgotPasswordModal = forgotPasswordModal;
        this.forgotPasswordModal.setForgotPasswordController(this);
        setupEventHandlersForgotPModal();
    }

    /**
     * Configura os eventos de clique dos botões "Enviar" e "Fechar".
     */
    public void setupEventHandlersForgotPModal()
    {
        forgotPasswordModal.getBtnFechar().setOnAction(event -> {fecharModal();});
        forgotPasswordModal.getBtnEnviar().setOnAction(event -> {enviarEmail();});
    }

    /**
     * Aciona o envio de um e-mail com uma nova senha temporária,
     * utilizando o e-mail informado no campo de texto.
     */
    public void enviarEmail() {
        System.out.println("FPController: Botão de enviar email clicado");
        String email = forgotPasswordModal.getFldEmail().getText();
        if(email == null || email.isEmpty()) {
            System.out.println("FPController: Email deve ser preenchido");
        }
        EmailService emailService = new EmailService();
        emailService.enviarEmail(email);
    }

    /**
     * Fecha o modal atual, encerrando sua janela.
     */
    public void fecharModal() {
        System.out.println("FPController: Botão de fechar modal clicado");
        forgotPasswordModal.close();
    }
}