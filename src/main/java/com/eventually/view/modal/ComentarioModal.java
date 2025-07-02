package com.eventually.view.modal;

import com.eventually.model.ComentarioModel;
import com.eventually.model.EventoModel;
import com.eventually.model.UsuarioModel;
import com.eventually.service.ComentarioService;
import com.eventually.service.UsuarioSessaoService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class ComentarioModal extends VBox {
    private final EventoModel evento;
    private final ComentarioService comentarioService;
    private final UsuarioSessaoService sessaoService;
    private final VBox listaComentariosVBox;

    private String email;

    public ComentarioModal(EventoModel evento, String email) {
        super(15);
        this.evento = evento;
        this.email=email;
        this.comentarioService = ComentarioService.getInstancia();
        this.sessaoService = UsuarioSessaoService.getInstancia();
        this.listaComentariosVBox = new VBox(15);

        setupUI();
    }

    private void setupUI() {
        final double MODAL_WIDTH = 450;
        final double MODAL_HEIGHT = 400;

        this.getStyleClass().add("layout-pane");
        this.setMaxSize(MODAL_WIDTH, MODAL_HEIGHT);
        this.setPrefSize(MODAL_WIDTH, MODAL_HEIGHT);
        this.setPadding(new Insets(15));

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        this.setClip(rect);

        HBox headerPane = criarHeader();

        ScrollPane scrollPane = criarAreaDeComentarios();

        VBox areaDeInput = criarAreaDeInput();

        this.getChildren().addAll(headerPane, scrollPane, areaDeInput);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        atualizarListaDeComentarios();
    }

    private HBox criarHeader() {
        Label lblTitulo = new Label("Comentários do Evento");
        lblTitulo.getStyleClass().add("modal-title");
        Button btnCloseX = new Button("X");
        btnCloseX.getStyleClass().add("close-button-x");
        btnCloseX.setOnAction(e -> fecharModal());
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return new HBox(lblTitulo, spacer, btnCloseX);
    }

    private ScrollPane criarAreaDeComentarios() {
        listaComentariosVBox.setPadding(new Insets(10, 5, 10, 5));
        ScrollPane scrollPane = new ScrollPane(listaComentariosVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("comentarios-scroll-pane");
        return scrollPane;
    }

    private VBox criarAreaDeInput() {
        TextArea txtNovoComentario = new TextArea();
        txtNovoComentario.setPromptText("Escreva um comentário...");
        txtNovoComentario.setWrapText(true);
        txtNovoComentario.setPrefHeight(80);

        Button btnPublicar = new Button("Publicar");
        btnPublicar.getStyleClass().add("modal-interact-button");
        btnPublicar.setOnAction(e -> {
            publicarComentario(txtNovoComentario.getText());
            txtNovoComentario.clear();
        });

        HBox acoesInput = new HBox(btnPublicar);
        acoesInput.setAlignment(Pos.CENTER_RIGHT);

        return new VBox(5, new Separator(), txtNovoComentario, acoesInput);
    }

    private void publicarComentario(String texto) {
        if (texto == null || texto.isBlank()) return;

        UsuarioModel autor = sessaoService.procurarUsuario(email);

        ComentarioModel novoComentario = new ComentarioModel(texto, autor, evento);
        comentarioService.adicionarComentario(evento, novoComentario);

        atualizarListaDeComentarios();
    }

    private void excluirComentario(ComentarioModel comentario) {

        comentarioService.removerComentario(evento, comentario);
        atualizarListaDeComentarios();
    }
    private void atualizarListaDeComentarios() {
        listaComentariosVBox.getChildren().clear();

        if (evento.getComentarios().isEmpty()) {
            Label infoLabel = new Label("Nenhum comentário ainda. Seja o primeiro!");
            infoLabel.getStyleClass().add("info-label");
            listaComentariosVBox.getChildren().add(infoLabel);
        } else {
            for (ComentarioModel comentario : evento.getComentarios()) {
                VBox cardComentario = criarCardComentario(comentario);
                listaComentariosVBox.getChildren().add(cardComentario);
            }
        }
    }

    private VBox criarCardComentario(ComentarioModel comentario) {
        ImageView fotoAutorView = new ImageView(comentario.getAutor().getFoto());
        configurarFotoCircular(fotoAutorView, 40);

        Label lblNomeAutor = new Label(comentario.getAutor().getNome());
        lblNomeAutor.getStyleClass().add("comment-author-name");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        Label lblData = new Label(comentario.getDataHora().format(formatter));
        lblData.getStyleClass().add("comment-date");

        VBox infoAutorVBox = new VBox(2, lblNomeAutor, lblData);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox autorInfo = new HBox(10, fotoAutorView, infoAutorVBox, spacer);
        autorInfo.setAlignment(Pos.CENTER_LEFT);

        UsuarioModel usuarioLogado = sessaoService.procurarUsuario(email);
        if (usuarioLogado != null && usuarioLogado.equals(comentario.getAutor())) {
            Button btnExcluir = new Button("X");
            btnExcluir.getStyleClass().add("close-button-x");
            btnExcluir.setOnAction(e -> excluirComentario(comentario));
            autorInfo.getChildren().add(btnExcluir);
        }

        Label lblTexto = new Label(comentario.getTexto());
        lblTexto.setWrapText(true);
        lblTexto.setMaxWidth(380);
        lblTexto.getStyleClass().add("comment-text");

        VBox card = new VBox(8, autorInfo, lblTexto);
        if (comentario.getFotoAnexada() != null) {
            ImageView fotoAnexadaView = new ImageView(comentario.getFotoAnexada());
            fotoAnexadaView.setFitWidth(380);
            fotoAnexadaView.setPreserveRatio(true);
            card.getChildren().add(fotoAnexadaView);
        }

        card.getStyleClass().add("comment-card");
        return card;
    }

    private void configurarFotoCircular(ImageView imageView, double tamanho) {
        imageView.setFitWidth(tamanho);
        imageView.setFitHeight(tamanho);
        Circle clip = new Circle(tamanho / 2, tamanho / 2, tamanho / 2);
        imageView.setClip(clip);
    }

    private void fecharModal() {
        Stage stage = (Stage) this.getScene().getWindow();
        if (stage != null) stage.close();
    }
}