package com.eventually.controller;

import com.eventually.model.EventoModel;
import com.eventually.model.UsuarioModel;
import com.eventually.service.AlertaService;
import com.eventually.service.EventoLeituraService;
import com.eventually.view.HomeView;
import com.eventually.view.modal.CompartilharEventoModal;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Controlador para o modal de "Compartilhar evento".
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.02
 * @since 2025-07-02
 */
public class CompartilharEventoController {
    private final CompartilharEventoModal modalView;

    private static final String IMGUR_CLIENT_ID = "SEU_CLIENT_ID_AQUI";

    private final Random random = new Random();

    private static final List<String> FRASES_COMPARTILHAMENTO = List.of(
            "Eu vou no evento",
            "Presença confirmada no evento",
            "Contando os dias para o",
            "Partiu",
            "Ansioso(a) para o"
    );

    private HomeView.EventoH evento;
    private final AlertaService alertaService;
    private EventoLeituraService eventoLeituraService;

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(CompartilharEventoController.class);

    public CompartilharEventoController(CompartilharEventoModal modalView, HomeView.EventoH evento) {
        this.eventoLeituraService=EventoLeituraService.getInstancia();
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

    public void compartilharNoTwitter() {
        Image imagemDoEvento = evento.imagem();
        String nomeEvento = evento.titulo();

        String fraseAleatoria = FRASES_COMPARTILHAMENTO.get(random.nextInt(FRASES_COMPARTILHAMENTO.size()));
        Optional<EventoModel> real = eventoLeituraService.procurarEventoPorId(evento.id());

        String nomeOrganizador = real.map(EventoModel::getOrganizador)
                .map(UsuarioModel::getNome)
                .orElse("Organizador não encontrado");

        String textoCompleto = String.format("%s \"%s\", organizado por %s! #eventually",
                fraseAleatoria, nomeEvento, nomeOrganizador);

        try {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putImage(imagemDoEvento);
            clipboard.setContent(content);
            System.out.println("Imagem do evento copiada para a área de transferência!");
        } catch (Exception e) {
            System.err.println("Erro ao copiar imagem para o clipboard: " + e.getMessage());
        }

        try {
            String textoCodificado = URLEncoder.encode(textoCompleto, StandardCharsets.UTF_8);
            String twitterUrl = "https://twitter.com/intent/tweet?text=" + textoCodificado;
            abrirPaginaWeb(twitterUrl);
            alertaService.alertarInfo("A imagem do evento foi copiada!\nCole no Twitter com Ctrl+V.");
        } catch (Exception e) {
            e.printStackTrace();
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