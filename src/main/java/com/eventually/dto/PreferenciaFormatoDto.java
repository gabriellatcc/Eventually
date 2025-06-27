package com.eventually.dto;
/**
 * DTO que representa a preferência de fomato selecionado por um usuário.
 * @param presencial se o formato foi selecionado
 * @param online se o formato foi selecionado
 * @param hibrido se o formato foi selecionado
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-06-24
 */
public record PreferenciaFormatoDto(
        boolean presencial,
        boolean online,
        boolean hibrido ){
}