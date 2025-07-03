package com.eventually.service;

import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.model.Comunidade;

import java.util.HashSet;
import java.util.Set;

/**
 * Serviço para mapear os dados recebidos dos DTOs para os modelos da aplicação,
 * em especial convertendo as preferências temáticas do usuário em um conjunto de enums
 * {@code Comunidade}, atua como uma camada de transformação entre os dados fornecidos
 * pelo frontend (DTOs) e os modelos utilizados internamente no sistema.
 * @author Gabriella Tavares Costa Corrêa (Criação,documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.02
 * @since 2025-05-15
 */
public class MapeamentoPreferenciasService {
    /**
     * Converte as preferências temáticas contidas em um {@code PreferenciasUsuarioDto}
     * para um conjunto de enumerações {@code Comunidade}.
     * @param dto o objeto DTO contendo as preferências do usuário em forma de campos booleanos.
     * @return o conjunto de temas representando as preferências selecionadas pelo usuário.
     */
    public static Set<Comunidade> mapearPreferencias(PreferenciasUsuarioDto dto) {
        Set<Comunidade> temas = new HashSet<>();

        if (dto.corporativo()) temas.add(Comunidade.CORPORATIVO);
        if (dto.beneficente()) temas.add(Comunidade.BENEFICENTE);
        if (dto.educacional()) temas.add(Comunidade.EDUCACIONAL);
        if (dto.cultural()) temas.add(Comunidade.CULTURAL);
        if (dto.esportivo()) temas.add(Comunidade.ESPORTIVO);
        if (dto.religioso()) temas.add(Comunidade.RELIGIOSO);
        if (dto.social()) temas.add(Comunidade.SOCIAL);
        return temas;
    }
}