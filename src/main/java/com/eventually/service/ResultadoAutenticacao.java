package com.eventually.service;

import com.eventually.model.StatusLogin;
import com.eventually.model.UsuarioModel;

import java.util.Optional;

/**
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-07-01
 */
public class ResultadoAutenticacao {

    private final StatusLogin status;
    private final Optional<UsuarioModel> usuario;

    public ResultadoAutenticacao(StatusLogin status, Optional<UsuarioModel> usuario) {
        this.status = status;
        this.usuario = usuario;
    }

    public StatusLogin getStatus() {
        return status;
    }

    public Optional<UsuarioModel> getUsuario() {
        return usuario;
    }
}