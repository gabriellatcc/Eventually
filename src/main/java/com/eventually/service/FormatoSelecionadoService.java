package com.eventually.service;

import com.eventually.dto.PreferenciaFormatoDto;
import com.eventually.model.FormatoSelecionado;

public class FormatoSelecionadoService {
    /**
     * Converte o DTO de preferência de formato para o Enum correspondente.
     * O método é estático para que possa ser chamado diretamente da classe, sem precisar criar um objeto.
     * @param formatoDto o DTO vindo da requisição.
     * @return o valor do Enum FormatoSelecionado.
     * @throws IllegalArgumentException se nenhum formato for selecionado.
     */
    public static FormatoSelecionado mapearFormato(PreferenciaFormatoDto formatoDto) {
        if (formatoDto.presencial()) {
            return FormatoSelecionado.PRESENCIAL;
        }
        if (formatoDto.online()) {
            return FormatoSelecionado.ONLINE;
        }
        if (formatoDto.hibrido()) {
            return FormatoSelecionado.HIBRIDO;
        }
        throw new IllegalArgumentException("Nenhum formato de evento (Presencial, Online ou Híbrido) foi selecionado.");
    }
}