package com.eventually.model;

import java.time.LocalDateTime;

/**
 * A classe {@code ComentarioModel} representa um comentário feito em um evento.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-07-02
 */
public class ComentarioModel {
    private String texto;
    private UsuarioModel autor;
    private LocalDateTime dataHora;

    public ComentarioModel(String texto, UsuarioModel autor) {
        this.texto = texto;
        this.autor = autor;
        this.dataHora = LocalDateTime.now();
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public UsuarioModel getAutor() {
        return autor;
    }

    public void setAutor(UsuarioModel autor) {
        this.autor = autor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        return "ComentarioModel{" +
                "texto='" + texto + '\'' +
                ", autor=" + autor.getNome() +
                ", dataHora=" + dataHora +
                '}';
    }
}