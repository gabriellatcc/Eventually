package com.eventually.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton para gerenciar os comentários.
 */
public class ComentarioService {
    private static ComentarioService instancia;
    private List<String> comentarios;

    private ComentarioService() {
        comentarios = new ArrayList<>();
    }

    public static ComentarioService getInstancia() {
        if (instancia == null) {
            instancia = new ComentarioService();
        }
        return instancia;
    }

    public void adicionarComentario(String comentario) {
        comentarios.add(comentario);
    }

    public List<String> getComentarios() {
        return comentarios;
    }
}