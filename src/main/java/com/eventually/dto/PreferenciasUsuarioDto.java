package com.eventually.dto;
/**
 * DTO que representa as preferências de temas selecionadas por um usuário
 * @param corporativo se o tema corporativo foi selecionado
 * @param beneficente se o tema beneficente foi selecionado
 * @param educacional se o tema educacional foi selecionado
 * @param cultural se o tema cultural foi selecionado
 * @param esportivo se o tema esportivo foi selecionado
 * @param religioso se o tema religioso foi selecionado
 * @param social se o tema social foi selecionado
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-05-15
 */
public record PreferenciasUsuarioDto(
        boolean corporativo,
        boolean beneficente,
        boolean educacional,
        boolean cultural,
        boolean esportivo,
        boolean religioso,
        boolean social) {
}