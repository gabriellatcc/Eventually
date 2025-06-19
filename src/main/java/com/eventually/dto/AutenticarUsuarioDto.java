package com.eventually.dto;
import java.util.Objects;

/**
 * DTO que torna a entrada de dados no login um objeto a ser analisado
 * @param email o email informado pelo usuario a ser validado
 * @param senha a senha informada pelo usuario a ser validado
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação, estrutura e da parte lógica da classe)
 * @version 1.01
 * @since 2025-05-15
 */
public record AutenticarUsuarioDto(String email, String senha) {
    public AutenticarUsuarioDto {
        Objects.requireNonNull(email);
        Objects.requireNonNull(senha);
    }
}