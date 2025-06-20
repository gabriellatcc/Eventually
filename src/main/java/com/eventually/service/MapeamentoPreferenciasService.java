package com.eventually.service;

import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.model.TemaPreferencia;
import java.util.HashSet;
import java.util.Set;

/**
 * Serviço para mapear os dados recebidos dos DTOs para os modelos da aplicação,
 * em especial convertendo as preferências temáticas do usuário em um conjunto de enums
 * {@code TemaPreferencia}, atua como uma camada de transformação entre os dados fornecidos
 * pelo frontend (DTOs) e os modelos utilizados internamente no sistema.
 * @author Gabriella Tavares Costa Corrêa (Criação,documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.01
 * @since 2025-05-15
 */
public class MapeamentoPreferenciasService {
    /**
     * Converte as preferências temáticas contidas em um {@code PreferenciasUsuarioDto}
     * para um conjunto de enumerações {@code TemaPreferencia}.
     * @param dto o objeto DTO contendo as preferências do usuário em forma de campos booleanos.
     * @return o conjunto de temas representando as preferências selecionadas pelo usuário.
     */
    public static Set<TemaPreferencia> mapearPreferencias(PreferenciasUsuarioDto dto) {
        Set<TemaPreferencia> temas = new HashSet<>();

        if (dto.corporativo()) temas.add(TemaPreferencia.CORPORATIVO);
        if (dto.beneficente()) temas.add(TemaPreferencia.BENEFICENTE);
        if (dto.educacional()) temas.add(TemaPreferencia.EDUCACIONAL);
        if (dto.cultural()) temas.add(TemaPreferencia.CULTURAL);
        if (dto.esportivo()) temas.add(TemaPreferencia.ESPORTIVO);
        if (dto.religioso()) temas.add(TemaPreferencia.RELIGIOSO);
        if (dto.social()) temas.add(TemaPreferencia.SOCIAL);
        return temas;
    }
}