package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.service.AlertaService;
import com.eventually.view.HomeView;
import com.eventually.view.modal.CompartilharEventoModal;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Controlador para o modal de "Compartilhar evento".
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-07-02
 */
public class CompartilharEventoController {
    private final CompartilharEventoModal modalView;

    private HomeView.EventoH evento;
    private final AlertaService alertaService;

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(CompartilharEventoController.class);

    public CompartilharEventoController(CompartilharEventoModal modalView, HomeView.EventoH evento) {
        this.modalView = modalView;
        this.evento = evento;
        this.alertaService = new AlertaService();
        configurarAcoes();
    }

    private void configurarAcoes() {
        try {
            modalView.getTwitterItem().setOnAction(e -> compartilharNoTwitter());
            modalView.getFacebookItem().setOnAction(e -> compartilharNoFacebook());
            modalView.getCopyLinkItem().setOnAction(e -> copiarLinkParaClipboard());
            modalView.getBtnFechar().setOnAction(e -> fecharModal());
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar as ações do modal de compartilhamento: {}", e.getMessage());
        }
    }

    private void compartilharNoTwitter() {
        String text = String.format("EU VOU NO ", evento.titulo() +"!");

        try {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String twitterUrl = "https://twitter.com/intent/tweet?text=" + encodedText;
            abrirPaginaWeb(twitterUrl);
        } catch (Exception e) {
        }
    }

    private void compartilharNoFacebook() {
        String eventUrl = "https://www.eventually.com/eventos/" + evento.id();

        try {
            String encodedUrl = URLEncoder.encode(eventUrl, StandardCharsets.UTF_8);
            String facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=" + encodedUrl;
            abrirPaginaWeb(facebookUrl);
        } catch (Exception e) {
        }
    }

    private void copiarLinkParaClipboard() {
        String eventUrl = "https://www.seusite.com/eventos/" + evento.capacidade();

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(eventUrl);
        clipboard.setContent(content);

    }

    private void abrirPaginaWeb(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
            }
        } else {
            alertaService.alertarWarn("Funcionalidade Indisponível", "Seu sistema não suporta abrir links.");
        }
    }

    private void fecharModal() {
        try {
            modalView.close();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao fechar o modal de compartilhamento: {}", e.getMessage());
        }
    }
}