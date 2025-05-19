package com.eventually.service;

import com.eventually.model.EventoModel;
import com.eventually.repository.EventoRepository;

import java.util.Optional;

/**
 * Serviço que fornece a operação de deletar (DELETE do CRUD) para eventos.
 * Esta classe utiliza o {@link EventoRepository} para acessar e manipular os dados dos eventos.
 *  @author Gabriella Tavares Costa Corrêa
 *  @version 1.00
 *  @since 2025-05-18
 */
public class EventoExclusaoService {
    private final EventoRepository eventoRepository;

    /**
     * Construtor da classe {@code EventoExclusaoService}.
     * Inicializa o {@code EventoRepository} a ser utilizado.
     */
    public EventoExclusaoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    /**
     * Deleta um evento do repositório com base no ID do {@code EventoModel} fornecido.
     * Este método representa a operação de exclusão (Delete) do CRUD.
     *
     * @param eventoParaExcluir O objeto {@code EventoModel} a ser deletado.
     */
    public void deletarEvento(EventoModel eventoParaExcluir) {
        if (eventoParaExcluir == null || eventoParaExcluir.getId() == 0) {
            System.out.println("EDelservice: Evento inválido para exclusão.");
            return;
        }

        int idEventoParaExcluir = eventoParaExcluir.getId();
        Optional<EventoModel> eventoEncontradoOptional = eventoRepository.buscarEventoPorId(idEventoParaExcluir);

        eventoEncontradoOptional.ifPresentOrElse(evento -> {
            if (eventoRepository.removerEvento(evento)) {
                System.out.println("EDelservice: Evento com ID " + idEventoParaExcluir + " ('" + evento.getNomeEvento() + "') deletado com sucesso.");
            } else {
                System.out.println("EDelservice: Erro ao deletar o evento com ID " + idEventoParaExcluir + ".");
            }
        }, () -> System.out.println("EDelservice: Evento com ID " + idEventoParaExcluir + " não encontrado."));
    }
}