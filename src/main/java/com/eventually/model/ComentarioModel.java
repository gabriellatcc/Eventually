package com.eventually.model;

import javafx.scene.image.Image;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.01
 * @since 2025-07-03
 */
public class ComentarioModel {
    private String texto;
    private UsuarioModel autor;
    private EventoModel evento;
    private Image fotoAnexada;
    private final LocalDateTime dataHora;

    private final Set<UsuarioModel> usuariosQueCurtiram;

    public ComentarioModel(String texto, UsuarioModel autor, EventoModel evento) {
        this(texto, autor, evento, null);
    }

    public ComentarioModel(String texto, UsuarioModel autor, EventoModel evento, Image fotoAnexada) {
        this.texto = texto;
        this.autor = autor;
        this.evento = evento;
        this.fotoAnexada = fotoAnexada;
        this.dataHora = LocalDateTime.now();
        this.usuariosQueCurtiram = new HashSet<>();
    }

    public String getTexto() { return texto; }
    public UsuarioModel getAutor() { return autor; }
    public EventoModel getEvento() { return evento; }
    public Image getFotoAnexada() { return fotoAnexada; }
    public LocalDateTime getDataHora() { return dataHora; }

    public int getCurtidas() {
        return this.usuariosQueCurtiram.size();
    }

    public void curtir(UsuarioModel usuario) {
        this.usuariosQueCurtiram.add(usuario);
    }

    public void descurtir(UsuarioModel usuario) {
        this.usuariosQueCurtiram.remove(usuario);
    }

    public boolean isCurtidoPor(UsuarioModel usuario) {
        return this.usuariosQueCurtiram.contains(usuario);
    }
}