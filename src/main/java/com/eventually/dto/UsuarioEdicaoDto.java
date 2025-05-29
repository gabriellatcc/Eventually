package com.eventually.dto;
import com.eventually.model.EventoModel;
import com.eventually.model.TemaPreferencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * DTO utilizado para representar os dados de edição de um usuário.
 * Apenas os campos não nulos serão utilizados para atualizar o {@code UsuarioModel}.
 *
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.1
 * @since 2025-05-18
 */
public record UsuarioEdicaoDto(
        String nomePessoa,
        String email,
        String senha,
        String localizacaoUsuario,
        LocalDate dataNascimento,
        String fotoUsuario,
        List<EventoModel> eventosParticipa,
        List<EventoModel> eventosOrganizados,
        Set<TemaPreferencia> temasPreferidos
) {}