package com.eventually.view.modal;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Classe responsável pelo modal de comentários.
 * Permite que os usuários comentem e anexem imagens.
 */
public class ComentarioModal extends Parent {
    private TextArea txtComentario;
    private Button btnEnviar;
    private Button btnAnexarImagem;
    private VBox vbComentarios;

    /**
     * Construtor padrão da classe.
     */
    public ComentarioModal() {
        setup();
    }

    /**
     * Configura e constrói o layout inicial do modal.
     */
    private void setup() {
        VBox modalContent = criarLayoutPrincipal();

        StackPane wrapper = new StackPane(modalContent);
        StackPane.setAlignment(modalContent, Pos.CENTER);

        this.getChildren().add(wrapper);
    }

    /**
     * Cria o layout principal e todos os seus componentes aninhados.
     * @return um VBox contendo toda a estrutura do modal.
     */
    public VBox criarLayoutPrincipal() {
        final double MODAL_WIDTH = 450;
        final double MODAL_HEIGHT = 600;

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setPrefSize(MODAL_WIDTH, MODAL_HEIGHT);

        txtComentario = new TextArea();
        txtComentario.setPromptText("Escreva seu comentário aqui...");
        txtComentario.setWrapText(true);

        btnEnviar = new Button("Enviar Comentário");
        btnAnexarImagem = new Button("Anexar Imagem");

        vbComentarios = new VBox();
        vbComentarios.setSpacing(10);
        vbComentarios.setAlignment(Pos.TOP_LEFT);

        layout.getChildren().addAll(txtComentario, btnAnexarImagem, btnEnviar, vbComentarios);
        return layout;
    }

    /**
     * Fecha a janela (Stage) do modal.
     */
    public void close() {
        Stage stage = (Stage) this.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    public Button getBtnEnviar() {
        return btnEnviar;
    }

    public Button getBtnAnexarImagem() {
        return btnAnexarImagem;
    }

    public VBox getComentariosContainer() {
        return vbComentarios;
    }

    public TextArea getTxtComentario() {
        return txtComentario;
    }
}