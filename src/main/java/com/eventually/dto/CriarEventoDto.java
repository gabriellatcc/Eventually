package com.eventually.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * DTO que torna a entrada de dados na criação de um evento em um objeto a ser analisado.
 * @param emailOrganizador o email do organizador do evento a ser validado.
 * @param tituloEvento o título do evento a ser validado.
 * @param descricaoEvento a descrição do evento a ser validada.
 * @param preferenciaFormato o formato do evento a ser validado.
 * @param nParticipantes o número máximo de participantes do evento a ser validado.
 * @param horaInicial a hora de início do evento a ser validada.
 * @param diaInicial o dia de início do evento a ser validado.
 * @param horaFinal a hora de término do evento a ser validada.
 * @param diaFinal o dia de término do evento a ser validado.
 * @param preferenciasEvento as preferências de tema do evento a serem validadas
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação, estrutura e refatoração da parte lógica da classe)
 * @version 1.01
 * @since 2025-06-24
 */
public record CriarEventoDto(String emailOrganizador, String tituloEvento, String descricaoEvento, PreferenciaFormatoDto preferenciaFormato,
                             int nParticipantes, LocalTime horaInicial, LocalDate diaInicial, LocalTime horaFinal, LocalDate diaFinal,
                             PreferenciasUsuarioDto preferenciasEvento) {
    public CriarEventoDto {
        Objects.nonNull(emailOrganizador());
        Objects.requireNonNull(tituloEvento);
        Objects.requireNonNull(descricaoEvento);
        Objects.requireNonNull(preferenciaFormato);
        Objects.nonNull(horaInicial);
        Objects.nonNull(diaInicial);
        Objects.nonNull(horaFinal);
        Objects.nonNull(diaFinal);
        Objects.requireNonNull(preferenciasEvento);
    }
}
