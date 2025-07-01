package com.eventually.view.modal;
import com.eventually.controller.EsqueceuSenhaController;
import com.eventually.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe para o modal de "Esqueceu sua senha".
 * Contém métodos públicos para que sejam acessados por outras classes.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e da estrutura da classe)
 * @version 1.03
 * @since 23-05-2025
 */
public class EsqueceuSenhaModal extends Parent {
    private Button btnEnviar;
    private Button btnFechar;
    private TextField fldEmail;

    private EsqueceuSenhaController fpController;

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(LoginController.class);

    /**
     *  Construtor padrão da classe.
     */
    public EsqueceuSenhaModal() {setupUI();}

    /**
     * Define o controlador para este modal.
     * @param fpController a classe controladora a ser usada.
     */
    public void setForgotPasswordController(EsqueceuSenhaController fpController) {
        this.fpController = fpController;
    }

    private void setupUI() {
        VBox layout = criarLayoutPrincipal();
        this.getChildren().add(layout);
    }

    /**
     * Exibe a janela modal configurada para recuperação de senha.
     * @return vbox com elementos do modal
     */
    public VBox criarLayoutPrincipal() {
        final double MODAL_WIDTH = 400;
        final double MODAL_HEIGHT = 400;

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPrefSize(MODAL_WIDTH, MODAL_HEIGHT);
        layout.setPadding(new Insets(10, 20, 10, 20));
        layout.getStyleClass().add("layout-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        layout.setClip(rect);

        Image cadeadoImg = new Image(getClass().getResource("/images/cadeado.png").toExternalForm());
        ImageView cadeadoView = new ImageView(cadeadoImg);

        cadeadoView.setFitWidth(100);
        cadeadoView.setFitHeight(100);

        Label title = new Label("Esqueceu sua senha?");
        title.getStyleClass().add("title-label-modal");

        Label instruction = new Label("Por favor, insira o Email vinculado à sua conta que enviaremos as instruções para a restauração da sua senha.");
        instruction.getStyleClass().add("label-modal");
        instruction.setStyle("-fx-font-weight: normal; -fx-text-alignment: center; -fx-font-size: 16px;");
        instruction.setWrapText(true);

        fldEmail= new TextField();
        fldEmail.setPromptText("E-mail");
        fldEmail.setMaxWidth(350);
        fldEmail.getStyleClass().add("modal-field");

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        btnEnviar = new Button("Enviar");
        btnEnviar.getStyleClass().add("modal-interact-button");

        btnFechar = new Button("Fechar");
        btnFechar.getStyleClass().add("modal-interact-button");

        buttons.getChildren().addAll(btnEnviar, btnFechar);

        layout.getChildren().addAll(cadeadoView, title, instruction, fldEmail, buttons);
        return layout;
    }

    /**
     * Métodos de encapsulamento getters
     */
    public Button getBtnEnviar() {return btnEnviar;}
    public Button getBtnFechar() {return btnFechar;}
    public TextField getFldEmail() {return fldEmail;}

    public void close() {
        Stage stage = (Stage) this.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}