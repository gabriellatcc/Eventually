package com.eventually.dto;

import com.eventually.model.Comunidade;
import com.eventually.model.FormatoSelecionado;
import java.util.Optional;
import java.util.Set;

/**
 * @version 1.00
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-07-03
 */
public record FiltroDto(
        Set<Comunidade> comunidades,
        Optional<FormatoSelecionado> formato
) {}