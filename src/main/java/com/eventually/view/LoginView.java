package com.eventually.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import com.eventually.controller.LoginController;

/**
 * Classe principal da aplicação Eventually.
 * Esta classe estende {@code javafx.application.Application} e é responsável
 * por inicializar e exibir a interface gráfica do usuário.
 *
 * @author Yuri Garcia Maia
 * @version 1.01
 * @since 2025-05-12
 * @author Gabriella Tavares Costa Corrêa (Documentação e revisão da classe)
 * @since 2025-05-14
 */
public class LoginView extends BorderPane {

    private TextField emailField;
    private PasswordField passwordField;
    private Button btnLogin;
    private Button btnRegistrar;
    private Hyperlink esqueceuSenhaLink;

    private LoginController loginController;

    /**
     * Construtor que inicializa a interface de login.
     * Recebe o LoginController via injeção de dependência.
     */
    public LoginView() {
        setupUILoginView();
    }

    public void setLoginController(LoginController loginController) {this.loginController = loginController;}

    /**
     * Configura todos os elementos da interface de usuário para a tela de login.
     */
    private void setupUILoginView() {
        setupBackground();
        StackPane paneDireitaLogin = criarPainelDireito();
        VBox conteudoLogin = criarConteudoLogin();

        //se minimizar fica nessa proporcao
        setPrefWidth(1080);
        setPrefHeight(600);

        paneDireitaLogin.setAlignment(Pos.TOP_RIGHT);
        setRight(paneDireitaLogin);
        setCenter(conteudoLogin);
    }

    /**
     * Configura o plano de fundo da tela de login.
     */
    private void setupBackground() {
        try {
            Image backgroundImage = new Image(getClass().getResource("/images/crowd-background.jpg").toExternalForm());
            BackgroundImage background = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            setBackground(new Background(background));
        } catch (Exception e) {
            setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            System.err.println("LoginView: Erro ao carregar imagem de fundo: " + e.getMessage());
        }
    }

    /**
     * Cria o painel lateral direito com formato de trapézio e degradê roxo com opacidade de 80%.
     * @return o painel configurado
     */
    private StackPane criarPainelDireito() {
        StackPane paneDireitoLogin = new StackPane();
        paneDireitoLogin.setPrefWidth(300);
        paneDireitoLogin.setPrefHeight(900);
        javafx.scene.shape.Polygon trapezoid = new javafx.scene.shape.Polygon();
        trapezoid.getPoints().addAll(new Double[]{
                300.0, 0.0,
                300.0, 900.0,
                0.0, 900.0,
                80.0, 0.0
        });
        javafx.scene.paint.LinearGradient gradient = new javafx.scene.paint.LinearGradient(
                0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop[]{
                        new javafx.scene.paint.Stop(0, Color.rgb(128, 0, 128, 0.8)),
                        new javafx.scene.paint.Stop(1, Color.rgb(180, 0, 180, 0.8))
                }
        );
        trapezoid.setFill(gradient);
        paneDireitoLogin.getChildren().add(trapezoid);
        return paneDireitoLogin;
    }

    /**
     * Cria o conteúdo principal do formulário de login.
     * @return VBox contendo todos os elementos do formulário
     */
    private VBox criarConteudoLogin() {
        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setPadding(new Insets(0, 0, 0, 100));
        loginBox.setMaxWidth(600);

        ImageView logoImageView = new ImageView();
        try {
            Image logoImage = new Image(getClass().getResource("/images/eventually-logo.png").toExternalForm());
            logoImageView.setImage(logoImage);
            logoImageView.setFitWidth(350);
            logoImageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Erro ao carregar o logo: " + e.getMessage());
            HBox fallbackLogoBox = new HBox();
            Text eventText = new Text("Even");
            eventText.setFill(Color.web("#800080"));
            eventText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            Text tuallyText = new Text("tually");
            tuallyText.setFill(Color.WHITE);
            tuallyText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            fallbackLogoBox.getChildren().addAll(eventText, tuallyText);
            logoImageView = null;
        }

        VBox logoContainer = new VBox(10);
        if (logoImageView != null && logoImageView.getImage() != null) {
            logoContainer.getChildren().add(logoImageView);
        } else {
            HBox fallbackLogoBox = new HBox();
            Text eventText = new Text("Even");
            eventText.setFill(Color.web("#800080"));
            eventText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            Text tuallyText = new Text("tually");
            tuallyText.setFill(Color.WHITE);
            tuallyText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            fallbackLogoBox.getChildren().addAll(eventText, tuallyText);
            Text tagline = new Text("join the moment");
            tagline.setFill(Color.WHITE);
            tagline.setFont(Font.font("Arial", 18));
            HBox taglineBox = new HBox();
            taglineBox.getChildren().add(tagline);
            taglineBox.setPadding(new Insets(-20, 0, 20, 5));
            logoContainer.getChildren().addAll(fallbackLogoBox, taglineBox);
        }
        logoContainer.setPadding(new Insets(0, 0, 30, 0));

        emailField = new TextField();
        emailField.setPromptText("E-mail");
        emailField.setPrefHeight(40);
        emailField.getStyleClass().add("login-field");

        passwordField = new PasswordField();
        passwordField.setPromptText("Senha");
        passwordField.setPrefHeight(40);
        passwordField.getStyleClass().add("login-field");

        esqueceuSenhaLink = new Hyperlink("Esqueceu sua senha? Clique aqui");
        esqueceuSenhaLink.setTextFill(Color.WHITE);

        HBox forgotPasswordBox = new HBox(esqueceuSenhaLink);
        forgotPasswordBox.setAlignment(Pos.CENTER_LEFT);
        forgotPasswordBox.setPadding(new Insets(0, 0, 0, 2));

        btnLogin = new Button("Login");
        btnLogin.setPrefHeight(40);
        btnLogin.setPrefWidth(400);
        btnLogin.getStyleClass().add("login-button");

        btnRegistrar = new Button("Registrar");
        btnRegistrar.setPrefHeight(30);
        btnRegistrar.setPrefWidth(120);
        btnRegistrar.getStyleClass().add("register-button");

        HBox boxRegistrar = new HBox();
        boxRegistrar.setAlignment(Pos.CENTER);
        boxRegistrar.getChildren().add(btnRegistrar);
        boxRegistrar.setPadding(new Insets(10, 0, 0, 0));

        loginBox.getChildren().addAll(logoContainer, emailField, passwordField, forgotPasswordBox, btnLogin, boxRegistrar);

        return loginBox;
    }

    /**
     * Métodos de encapsulamento getters
     */
    public TextField getEmailField() {return emailField;}
    public PasswordField getPasswordField() {return passwordField;}
    public Button getBtnLogin() {return btnLogin;}
    public Button getBtnRegistrar() {return btnRegistrar;}
    public Hyperlink getEsqueceuSenhaLink() {return esqueceuSenhaLink;}
}