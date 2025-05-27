package com.eventually.view;
import com.eventually.controller.ForgotPasswordController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.shape.Rectangle;

/**
 * Classe para o modal de "Esqueceu sua senha";
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação e da estrutura da classe)
 * @version 1.0
 * @since 23-05-2025
 */
public class ForgotPasswordModal {
    private Stage modalStage;
    private Scene modalScene;

    private Button btnEnviar;
    private Button btnFechar;
    private TextField fldEmail;

    private ForgotPasswordController fpController;

    /**
     * Construtor padrão da classe.
     */
    public ForgotPasswordModal() {
    }

    /**
     * Define o controller para este modal.
     * @param fpController O controller a ser usado.
     */
    public void setForgotPasswordController(ForgotPasswordController fpController) {
        this.fpController = fpController;
    }

    /**
     * Exibe a janela modal configurada para recuperação de senha.
     * @param parentStage Janela principal da aplicação que será usada como base para o modal.
     */
    public void showForgotPasswordModal(Stage parentStage) {
        modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(parentStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

        final double MODAL_WIDTH = 500;
        final double MODAL_HEIGHT = 370;

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));

        layout.setPrefSize(MODAL_WIDTH, MODAL_HEIGHT);

        layout.setStyle("-fx-background-color: white; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.5, 0, 4);");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(40);
        rect.setArcHeight(40);

        layout.setClip(rect);

        Image cadeadoImg = new Image(getClass().getResource("/images/cadeado.png").toExternalForm());
        ImageView cadeadoView = new ImageView(cadeadoImg);

        cadeadoView.setFitWidth(100);
        cadeadoView.setFitHeight(100);

        Label title = new Label("Esqueceu sua senha?");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#B6318D"));
        title.setPadding(new Insets(5,0,0,0));

        Label instruction = new Label("Por favor, insira o Email vinculado à sua conta\nque enviaremos as instruções para a\nrestauração da sua senha");
        instruction.setFont(Font.font("Arial", 14));
        instruction.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        instruction.setPadding(new Insets(5,0,0,0));

        fldEmail= new TextField();
        fldEmail.setPromptText("E-mail");
        fldEmail.setMaxWidth(350);
        fldEmail.getStyleClass().add("login-field");

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        btnEnviar = new Button("Enviar");
        btnEnviar.setPrefHeight(40);
        btnEnviar.setPrefWidth(120);
        btnEnviar.setStyle("-fx-background-color: #D64BCD; -fx-text-fill: white; -fx-background-radius: 20;");

        btnFechar = new Button("Fechar");
        btnFechar.setPrefHeight(40);
        btnFechar.setPrefWidth(120);
        btnFechar.setStyle("-fx-background-color: #D64BCD; -fx-text-fill: white; -fx-background-radius: 20;");

        buttons.getChildren().addAll(btnEnviar, btnFechar);

        layout.getChildren().addAll(cadeadoView, title, instruction, fldEmail, buttons);

        modalScene = new Scene(layout, MODAL_WIDTH, MODAL_HEIGHT, Color.TRANSPARENT);
        modalScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
        modalStage.setScene(modalScene);

        modalStage.showAndWait();
    }

    /**
     * Métodos de encapsulamento getters
     */
    public Button getBtnEnviar() {return btnEnviar;}
    public Button getBtnFechar() {return btnFechar;}
    public TextField getFldEmail() {return fldEmail;}
    public Scene getModalScene() {return modalScene;}

    public void close() {
        if (modalStage != null) {
            System.out.println("ForgotPasswordModal: Fechando modalStage: " + modalStage);
            modalStage.close();
        } else {
            System.out.println("ForgotPasswordModal: modalStage é null. Não é possível fechar.");
        }
    }
}