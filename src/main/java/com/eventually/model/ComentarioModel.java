package com.eventually.model;

import javafx.scene.image.Image;
import java.time.LocalDateTime;

public class ComentarioModel {
    private String texto;
    private UsuarioModel autor;
    private EventoModel evento;
    private Image fotoAnexada;
    private final LocalDateTime dataHora;

    public ComentarioModel(String texto, UsuarioModel autor, EventoModel evento) {
        this(texto, autor, evento, null);
    }

    public ComentarioModel(String texto, UsuarioModel autor, EventoModel evento, Image fotoAnexada) {
        this.texto = texto;
        this.autor = autor;
        this.evento = evento;
        this.fotoAnexada = fotoAnexada;
        this.dataHora = LocalDateTime.now();
    }

    public String getTexto() { return texto; }
    public UsuarioModel getAutor() { return autor; }
    public EventoModel getEvento() { return evento; }
    public Image getFotoAnexada() { return fotoAnexada; }
    public LocalDateTime getDataHora() { return dataHora; }

    public void setTexto(String texto) { this.texto = texto; }
}