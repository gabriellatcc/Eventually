package com.eventually.view.modal;

import com.eventually.controller.MudancaImagemController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;

/**
 * Classe para o modal de seleção de imagem.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.01
 * @since 2025-06-24
 */
public class MudancaImagemModal extends Parent {
    private MudancaImagemController mudancaImagemController;

    private Label lbMensagem;

    private ImageView imgPreview;
    private Button btnEscolherImagem, btnSalvar, btnFechar;

    private File arquivoSelecionado;

    /**
     * Construtor padrão da classe.
     */
    public MudancaImagemModal() {
        setup();
    }

    /**
     * Define o controlador para este modal.
     * @param mudancaImagemController o controlador a ser usado.
     */
    public void setMudancaImagemController(MudancaImagemController mudancaImagemController) {this.mudancaImagemController = mudancaImagemController;}

    /**
     * Configura a interface gráfica do modal.
     */
    private void setup() {
        VBox layout = criarLayoutPrincipal();
        this.getChildren().add(layout);
    }

    /**
     * Configura a interface gráfica do modal.
     */
    private VBox criarLayoutPrincipal() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0.0, 0.0);"
        );

        Rectangle rect = new Rectangle(650, 450);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        layout.setClip(rect);

        lbMensagem = new Label("Selecione uma nova imagem");
        lbMensagem.getStyleClass().add("title-label-modal");

        Image provisoria = new Image(getClass().getResourceAsStream("/images/upload-icon.png"));
        imgPreview = new ImageView(provisoria);
        imgPreview.setFitWidth(200);
        imgPreview.setFitHeight(200);
        imgPreview.setPreserveRatio(true);
        imgPreview.setStyle(
                "-fx-border-color: #888888; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-style: dashed; " +
                        "-fx-border-radius: 5px;"
        );

        btnEscolherImagem = new Button("Escolher Arquivo");
        btnEscolherImagem.getStyleClass().add("modal-save-button");

        HBox hbBotoes = new HBox(20);
        hbBotoes.setAlignment(Pos.CENTER);

        btnSalvar = new Button("Salvar");
        btnSalvar.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 5px;"
        );
        btnSalvar.setPrefWidth(100);
        btnSalvar.setDisable(true);

        btnFechar = new Button("Fechar");
        btnFechar.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-color: #f44336;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 5px;"
        );
        btnFechar.setPrefWidth(100);

        hbBotoes.getChildren().addAll(btnSalvar, btnFechar);

        layout.getChildren().addAll(lbMensagem, imgPreview, btnEscolherImagem, hbBotoes);

        return layout;
    }

    /**
     * Fecha a janela do modal.
     */
    public void close() {
        Stage stage = (Stage) this.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Métodos de encapsulamento getters e setters
     */
    public void setPreviewImage(Image image) {
        this.imgPreview.setImage(image);
    }

    public File getArquivoSelecionado() {return arquivoSelecionado;}

    public void setArquivoSelecionado(File arquivoSelecionado) {
        this.arquivoSelecionado = arquivoSelecionado;
        btnSalvar.setDisable(arquivoSelecionado == null);
    }
    public Button getBtnEscolherImagem() {return btnEscolherImagem;}
    public Button getBtnSalvar() {return btnSalvar;}
    public Button getBtnFechar() {return btnFechar;}
}
