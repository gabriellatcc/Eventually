package com.eventually.controller;

/**
 * Controladora para gerenciar operações relacionadas aos usuários,
 * como autenticação, registro e gerenciamento de perfil.
 *
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-05-07
 */
public class UserController {

    /**
     * Tenta autenticar um usuário com as credenciais fornecidas.
     *
     * @param email O email do usuário
     * @param password A senha do usuário
     * @return null se o login for bem-sucedido, uma String com a mensagem de erro caso contrário
     */
    public String handleLoginRequest(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            return "Por favor, preencha todos os campos.";
        }

        boolean loginSuccess = performLogin(email, password);

        if (loginSuccess) {
            return null; // Sucesso
        } else {
            return "Email ou senha incorretos.";
        }
    }

    /**
     * Lógica interna de autenticação.
     *
     * @param email O email do usuário
     * @param password A senha do usuário
     * @return true se o login for bem-sucedido, false caso contrário
     */
    private boolean performLogin(String email, String password) {
        // Implementação temporária para teste

        // Considera qualquer email com "@" válido
        // e qualquer senha com pelo menos 4 caracteres
        if (email.contains("@") && password.length() >= 4) {
            return true;
        }
        return false;
    }

    /**
     * Lida com a solicitação de navegação para a tela de registro.
     */
    public void handleRegistrationRequest() {
    }


    /**
     * Registra um novo usuário no sistema.
     *
     * @param email O email do novo usuário
     * @param password A senha do novo usuário
     * @param name O nome do novo usuário
     * @return true se o registro for bem-sucedido, false caso contrário
     */
    public boolean register(String email, String password, String name) {
        // Verifica se o email já existe
        // e salva o novo usuário

        if (email.contains("@") && password.length() >= 6 && !name.isEmpty()) {
            return true;
        }
        return false;
    }

    public void handleForgotPasswordRequest() {
        System.out.println("UserController: Solicitacao para tela 'Esqueceu Senha' recebida. Logica para modal aqui.");
    }
}