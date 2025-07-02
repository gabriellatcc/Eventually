package com.eventually.service;

import com.eventually.model.ComentarioModel;
import com.eventually.model.EventoModel;
import java.util.Collections;
import java.util.List;

/**
 * Serviço que gerencia a lógica de negócio para comentários em eventos.
 */
public class ComentarioService {
    private static final ComentarioService instancia = new ComentarioService();

    private ComentarioService() {}

    public static ComentarioService getInstancia() {
        return instancia;
    }

    /**
     * Adiciona um novo comentário à lista de comentários de um evento específico.
     * @param evento O evento que receberá o comentário.
     * @param novoComentario O objeto ComentarioModel a ser adicionado.
     */
    public void adicionarComentario(EventoModel evento, ComentarioModel novoComentario) {
        if (evento != null && novoComentario != null) {
            evento.getComentarios().add(novoComentario);
            System.out.println("Comentário adicionado ao evento: " + evento.getNome());
        }
    }

    /**
     * Retorna a lista de comentários de um evento específico.
     * @param evento O evento do qual se deseja obter os comentários.
     * @return Uma lista de ComentarioModel, ou uma lista vazia se o evento for nulo.
     */
    public List<ComentarioModel> getComentariosPorEvento(EventoModel evento) {
        if (evento != null) {
            return evento.getComentarios();
        }
        return Collections.emptyList();
    }
}