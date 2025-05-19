package com.eventually.repository;
import com.eventually.model.EventoModel;

import java.util.*;

/**
 * Esta classe {@code EventoRepository} é para gerenciar a coleção de eventos em memória,
 * fornece métodos para acessar e modificar a lista de eventos.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-04-22
 */
public class EventoRepository {
    /**
     * Retorna a lista de todos os eventos armazenados.
     * @return Uma lista de objetos {@code EventoModel}.
     */
    private List<EventoModel> listaEventos = new ArrayList<>(50);

    /**
     * Adiciona um novo evento à lista de eventos.
     *
     * @param evento O objeto {@code EventoModel} a ser adicionado.
     */
    public void adicionarEvento(EventoModel evento) {
        int id = System.identityHashCode(evento);
        evento.setId(id);
        listaEventos.add(evento);
    }

    /**
     * Remove um evento da lista de eventos.
     *
     * @param evento O objeto {@code EventoModel} a ser removido.
     * @return {@code true} se o evento foi removido com sucesso, {@code false} caso contrário.
     */
    public boolean removerEvento(EventoModel evento) {
        return listaEventos.remove(evento);
    }

    /**
     * Busca um evento na lista pelo seu ID.
     *
     * @param id O ID do evento a ser buscado.
     * @return Um {@code Optional} contendo o {@code EventoModel} correspondente ao ID,
     * ou um {@code Optional} vazio se não encontrado.
     */
    public Optional<EventoModel> buscarEventoPorId(int id) {
        return listaEventos.stream()
                .filter(evento -> evento.getId() == id)
                .findFirst();
    }

    /**
     * Método de encapsulamento getter para a lista de eventos.
     * @return Uma lista de objetos {@code EventoModel}.
     */
    public List<EventoModel> getAllEventos() {
        return listaEventos;
    }
}