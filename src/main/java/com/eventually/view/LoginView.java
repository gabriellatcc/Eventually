package com.eventually.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;

import com.eventually.controller.UserController;
import com.eventually.controller.RegisterController;
import com.eventually.view.RegisterView;

/**
 * Classe principal da aplicação Eventually.
 * Esta classe estende {@code javafx.application.Application} e é responsável
 * por inicializar e exibir a interface gráfica do usuário.
 *
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-04-06
 * @author Gabriella Tavares
 * @since 2025-04-22 (Documentação da classe)
 */
public class LoginView extends BorderPane {

    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private UserController userController;
    private Hyperlink forgotPasswordLink;

    /**
     * Construtor que inicializa a interface de login.
     * Recebe o UserController via injeção de dependência.
     */
    public LoginView(UserController userController) {
        if (userController == null) {
            System.err.println("UserController não pode ser nulo na LoginView!");
        }
        this.userController = userController;
        setupUI();
    }

    /**
     * Configura todos os elementos da interface de usuário para a tela de login.
     */
    private void setupUI() {
        setupBackground();
        StackPane rightPanel = createRightPanel();
        VBox loginContent = createLoginContent();
        setRight(rightPanel);
        setCenter(loginContent);
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
            System.err.println("Erro ao carregar imagem de fundo: " + e.getMessage());
        }
    }

    /**
     * Cria o painel lateral direito com formato de trapézio e degradê roxo com opacidade de 80%.
     * @return O painel configurado
     */
    private StackPane createRightPanel() {
        StackPane rightPanel = new StackPane();
        rightPanel.setPrefWidth(300);
        rightPanel.setPrefHeight(900);
        javafx.scene.shape.Polygon trapezoid = new javafx.scene.shape.Polygon();
        trapezoid.getPoints().addAll(new Double[]{
                300.0, 0.0,
                300.0, 720.0,
                0.0, 720.0,
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
        rightPanel.getChildren().add(trapezoid);
        return rightPanel;
    }

    /**
     * Cria o conteúdo principal do formulário de login.
     * @return VBox contendo todos os elementos do formulário
     */
    private VBox createLoginContent() {
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
        forgotPasswordLink = new Hyperlink("Esqueceu sua senha? Clique aqui");
        forgotPasswordLink.setTextFill(Color.WHITE);
        forgotPasswordLink.setOnAction(e -> handleForgotPassword());
        HBox forgotPasswordBox = new HBox(forgotPasswordLink);
        forgotPasswordBox.setAlignment(Pos.CENTER_LEFT);
        forgotPasswordBox.setPadding(new Insets(0, 0, 0, 2));
        loginButton = new Button("Login");
        loginButton.setPrefHeight(40);
        loginButton.setPrefWidth(400);
        loginButton.getStyleClass().add("login-button");
        loginButton.setOnAction(e -> handleLogin());
        registerButton = new Button("Register");
        registerButton.setPrefHeight(30);
        registerButton.setPrefWidth(120);
        registerButton.getStyleClass().add("register-button");
        registerButton.setOnAction(e -> handleRegister());
        HBox registerBox = new HBox();
        registerBox.setAlignment(Pos.CENTER);
        registerBox.getChildren().add(registerButton);
        registerBox.setPadding(new Insets(10, 0, 0, 0));
        loginBox.getChildren().addAll(logoContainer, emailField, passwordField, forgotPasswordBox, loginButton, registerBox);
        return loginBox;
    }

    /**
     * Manipula o evento de clique no botão de login.
     * Delega a lógica para o UserController e lida com o resultado.
     */
    private void handleLogin() {
        if (userController == null) {
            showError("Erro interno: UserController não inicializado.");
            return;
        }
        String email = emailField.getText();
        String password = passwordField.getText();
        String result = userController.handleLoginRequest(email, password);
        if (result == null) {
            try {
                openUserScheduleView();
            } catch (Exception e) {
                showError("Erro ao abrir a tela principal: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showError(result);
        }
    }

    /**
     * Manipula o evento de clique no botão de registro.
     * Notifica o UserController e inicia a navegação.
     */
    private void handleRegister() {
        if (userController == null) {
            showError("Erro interno: UserController não inicializado.");
            return;
        }
        userController.handleRegistrationRequest();
        try {
            openRegisterView();
        } catch (Exception e) {
            showError("Erro ao abrir a tela de registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Manipula o evento de clique no link "Esqueceu sua senha".
     * Delega a ação para o UserController.
     */
    private void handleForgotPassword() {
        if (userController == null) {
            showError("Erro interno: UserController não inicializado.");
            return;
        }
        userController.handleForgotPasswordRequest();
    }

    /**
     * Abre a tela de agendamento do usuário
     */
    private void openUserScheduleView() {
        Stage currentStage = (Stage) this.getScene().getWindow();
        UserScheduleView userScheduleView = new UserScheduleView();
        Scene scene = new Scene(userScheduleView, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Erro ao carregar styles.css: Arquivo não encontrado. " + e.getMessage());
        }
        currentStage.setScene(scene);
        currentStage.setTitle("Eventually - Agenda do Usuário");
    }

    /**
     * Abre a tela de registro.
     */
    private void openRegisterView() {
        try {
            Stage currentStage = (Stage) this.getScene().getWindow();
            RegisterController registerController = new RegisterController();
            RegisterView registerView = new RegisterView(registerController);
            Scene registerScene = new Scene(registerView, 1280, 900);
            try {
                registerScene.getStylesheets().add(getClass().getResource("/styles/register-styles.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("Erro ao carregar CSS para RegisterView: " + e.getMessage());
            }
            currentStage.setScene(registerScene);
            currentStage.setTitle("Eventually - Novo Cadastro");
            currentStage.setMaximized(true);
        } catch (Exception e) {
            showError("Erro crítico ao tentar abrir a tela de registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exibe uma mensagem de erro na interface.
     * @param message A mensagem de erro a ser exibida
     */
    private void showError(String message) {
        System.err.println("ERRO: " + message);
    }
}