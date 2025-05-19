package com.eventually.service;

import com.eventually.dto.CadastrarUsuarioDto;
import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.model.EventoModel;
import com.eventually.model.TemaPreferencia;
import com.eventually.model.UsuarioModel;
import com.eventually.repository.EventoRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Serviço para mapear os dados recebidos dos DTOs para os modelos da aplicação,
 * em especial convertendo as preferências temáticas do usuário em um conjunto de enums
 * {@code TemaPreferencia}, atua como uma camada de transformação entre os dados fornecidos
 * pelo frontend (DTOs) e os modelos utilizados internamente no sistema.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-05-15
 */
public class MapeamentoPreferenciasService {

    /**
     * Mapeia os dados do DTO {@code CadastrarUsuarioDto} para um novo objeto {@code UsuarioModel},
     * incluindo a conversão das preferências temáticas para um conjunto de enumerações {@code TemaPreferencia}.
     * @param dto Objeto contendo os dados fornecidos pelo usuário no momento do cadastro.
     * @return Um novo objeto {@code UsuarioModel} com os dados preenchidos a partir do DTO.
     */
    public UsuarioModel mapearDtoParaUsuario(CadastrarUsuarioDto dto) {
        Set<TemaPreferencia> temas = mapearPreferencias(dto.preferencias());

        EventoRepository eventoRepo = new EventoRepository();
        List<EventoModel> eventosDisponiveis = eventoRepo.getAllEventos();

        return new UsuarioModel(
                dto.nomePessoa(),
                dto.email(),
                dto.senha(),
                dto.localizacaoUsuario(),
                dto.data(),
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                temas
        );
    }

    /**
     * Converte as preferências temáticas contidas em um {@code PreferenciasUsuarioDto}
     * para um conjunto de enumerações {@code TemaPreferencia}.
     * @param dto Objeto DTO contendo as preferências do usuário em forma de campos booleanos.
     * @return Conjunto de temas representando as preferências selecionadas pelo usuário.
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