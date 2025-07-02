package com.eventually.view;

import com.eventually.controller.MudancaController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Classe para o modal de "Alterar valorUsuario" dentro das configurações.
 * @author Yuri Garcia Maia (Estrutura base)
 * @since 22-06-2025
 * @author Gabriella Tavares Costa Corrêa (Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.05
 * @since 29-05-2025
 */
public class MudancaModal extends Parent {
    private MudancaController cpController;

    private Label lbMensagem;

    private TextField fldEditado;

    private Button btnSalvarSenha, btnFechar;

    /**
     * Construtor padrão da classe.
     */
    public MudancaModal() {setup();}

    /**
     * Define o controlador para este modal.
     * @param cpController o controlador a ser usado.
     */
    public void setMudancaControlador(MudancaController cpController) {this.cpController = cpController;}

    /**
     * Exibe a janela modal configurada para alteração de valor do usuário.
     */
    private void setup() {
        VBox layout = criarLayoutPrincipal();
        this.getChildren().add(layout);
    }

    /**
     * Configura a interface gráfica do modal.
     */
    private VBox criarLayoutPrincipal() {
        VBox layout = new VBox(20);
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

        Rectangle rect = new Rectangle(650, 400 );
        rect.setArcWidth(40);
        rect.setArcHeight(40);
        layout.setClip(rect);

        lbMensagem = new Label();
        lbMensagem.getStyleClass().add("title-label-modal");

        fldEditado = new TextField();
        fldEditado.setStyle("-fx-font-size: 14px;");
        fldEditado.getStyleClass().add("modal-field");

        HBox hbBotoes = new HBox(20);
        hbBotoes.setAlignment(Pos.CENTER);

        btnSalvarSenha = new Button("Salvar");
        btnSalvarSenha.getStyleClass().add("modal-save-button");
        btnSalvarSenha.setPrefWidth(100);

        btnFechar = new Button("Fechar");
        btnFechar.getStyleClass().add("modal-close-button");
        btnFechar.setPrefWidth(100);

        hbBotoes.getChildren().addAll(btnSalvarSenha, btnFechar);

        layout.getChildren().addAll(lbMensagem, fldEditado, hbBotoes);

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
     * Métodos de encapsulamento getters.
     */
    public TextField getFldEditado() {return fldEditado;}
    public Label getLbMensagem() {return lbMensagem;}
    public Button getBtnSalvarSenha() {return btnSalvarSenha;}
    public Button getBtnFechar() {return btnFechar;}
}