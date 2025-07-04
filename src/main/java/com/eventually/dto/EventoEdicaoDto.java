package com.eventually.dto;
import com.eventually.model.Comunidade;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.UsuarioModel;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

/**
 * DTO utilizado para representar os dados de edição de um evento.
 * Apenas os campos não nulos serão utilizados para atualizar o {@code EventoModel}.
 *
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.01
 * @since 2025-05-18
 */
public record   EventoEdicaoDto(
        String nome,
        String descricao,
        String localizacao,
        String link,
        int capacidade,
        FormatoSelecionado formato,
        Set<Comunidade> comunidades,
        LocalDate dataInicio,
        LocalTime horaInicio,
        LocalDate dataFim,
        LocalTime horaFim,
        Image novaImagem
){}