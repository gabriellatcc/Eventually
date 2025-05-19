package com.eventually.dto;
import com.eventually.model.UsuarioModel;

import java.util.Date;

/**
 * DTO utilizado para representar os dados de edição de um evento.
 * Apenas os campos não nulos serão utilizados para atualizar o {@code EventoModel}.
 *
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.00
 * @since 2025-05-18
 */
public record   EventoEdicaoDto(
        UsuarioModel novoOrganizador,
        String novoNome,
        String novaFotoEvento,
        String novaDescricao,
        String novoFormato,
        String novaLocalizacao,
        Date novaDataInicial,
        Date novaDataFinal,
        Integer novaQntDePessoas,
        Integer novaClassificacao,
        Boolean novaCertificacao
){}