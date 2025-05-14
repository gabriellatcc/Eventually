package com.eventually.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
/**
 * Classe controller da tela de registro.
 * Esta classe é responsável pela comunicação
 * da tela de cadastro com o backend.
 *
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-05-13
 */
public class RegisterController {

    // Padrões para validação
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern LETTER_PATTERN = Pattern.compile("[a-zA-Z]");
    private static final Pattern EMAIL_DOMAIN_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    // Estrutura para armazenar os dados após o registro
    private Map<String, Object> registeredUserData;

    public RegisterController() {
        this.registeredUserData = new HashMap<>();
    }

    // --- Métodos de validação de regras ao vivo ---

    public boolean isNameRuleMet(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Pelo menos duas palavras (simples) ou nome composto
        String[] parts = name.trim().split("\\s+");
        return parts.length >= 2;
    }

    public Map<String, Boolean> checkPasswordRules(String password) {
        Map<String, Boolean> ruleStatus = new HashMap<>();
        if (password == null) password = "";

        ruleStatus.put("hasSpecial", SPECIAL_CHAR_PATTERN.matcher(password).find());
        ruleStatus.put("hasDigit", DIGIT_PATTERN.matcher(password).find());
        ruleStatus.put("hasLetter", LETTER_PATTERN.matcher(password).find());

        return ruleStatus;
    }

    // --- Métodos de validação para submissão ---

    public String validateNameForSubmit(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Mensagem de erro: O campo nome é obrigatório.";
        }
        if (!isNameRuleMet(name)) {
            return "Mensagem de erro: O nome não segue os padrões (deve ter nome e sobrenome).";
        }
        return null; // Sem erro
    }

    public String validateEmailForSubmit(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Mensagem de erro: O campo email é obrigatório.";
        }
        if (!EMAIL_DOMAIN_PATTERN.matcher(email).matches()) {
            return "Mensagem de erro: O email não tem domínio válido.";
        }
        return null; // Sem erro
    }

    public String validatePasswordForSubmit(String password) {
        if (password == null || password.isEmpty()) {
            return "Mensagem de erro: O campo senha é obrigatório.";
        }
        Map<String, Boolean> rules = checkPasswordRules(password);
        if (!rules.get("hasSpecial") || !rules.get("hasDigit") || !rules.get("hasLetter")) {
            return "Mensagem de erro: A senha não segue os padrões.";
        }
        return null; // Sem erro
    }

    public String validateDobForSubmit(LocalDate dob) {
        if (dob == null) {
            return "Mensagem de erro: Data de nascimento é obrigatória.";
        }
        return null; // Sem erro
    }

    public String validateCityForSubmit(String city) {
        if (city == null || city.trim().isEmpty()) {
            return "Mensagem de erro: Cidade é obrigatória.";
        }
        return null; // Sem erro
    }

    public String validateThemesForSubmit(List<String> selectedThemes) {
        if (selectedThemes == null || selectedThemes.isEmpty()) {
            return "Mensagem de erro: Selecione ao menos um tema de interesse.";
        }
        return null;
    }


    public boolean processRegistration(String name, String email, String password,
                                       LocalDate dob, String city, List<String> selectedThemes) {

        // Revalidar tudo para garantir
        boolean isValid = true;
        if (validateNameForSubmit(name) != null) isValid = false;
        if (validateEmailForSubmit(email) != null) isValid = false;
        if (validatePasswordForSubmit(password) != null) isValid = false;
        if (validateDobForSubmit(dob) != null) isValid = false;
        if (validateCityForSubmit(city) != null) isValid = false;
        if (validateThemesForSubmit(selectedThemes) != null) isValid = false;


        if (isValid) {
            registeredUserData.put("name", name);
            registeredUserData.put("email", email);
            registeredUserData.put("password", "******"); // Não armazene senha em texto plano
            registeredUserData.put("dateOfBirth", dob);
            registeredUserData.put("city", city);
            registeredUserData.put("themes", selectedThemes);

            System.out.println("--- REGISTRO REALIZADO (simulação) ---");
            System.out.println("Dados coletados para backend e armazenados em memória (controller):");
            registeredUserData.forEach((key, value) -> System.out.println(key + ": " + value));
            System.out.println("-------------------------------------");
            // Aqui você normalmente navegaria para uma tela de sucesso ou login
            return true;
        } else {
            System.out.println("Falha no registro. Verifique os campos.");
            return false;
        }
    }

    public void handleNavigateToLogin() {
        System.out.println("RegisterController: Solicitacao para navegar para a tela de Login.");
        // Lógica de navegação para a tela de login viria aqui
    }
}