package com.eventually.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO que torna a entrada de dados no registro um objeto a ser analisado
 * @param nomePessoa o nome do usuario a ser validado
 * @param email o email do usuario a ser validado
 * @param senha a senha do usuario a ser validado
 * @param localizacaoUsuario a cidade do usuario a ser validado
 * @param data a data de nascimento do usuario a ser validado
 * @param preferencias as preferencias de tema do usuário
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.1
 * @since 2025-05-15
 */
public record CadastrarUsuarioDto(String nomePessoa, String email, String senha, String localizacaoUsuario,
                                  LocalDate data, PreferenciasUsuarioDto preferencias) {
    public CadastrarUsuarioDto {
        Objects.requireNonNull(nomePessoa);
        Objects.requireNonNull(email);
        Objects.requireNonNull(senha);
        Objects.requireNonNull(localizacaoUsuario);
        Objects.requireNonNull(data);
        Objects.requireNonNull(preferencias);
    }
}