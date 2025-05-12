package com.eventually.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
// Se UserScheduleView estiver em um pacote diferente, você precisará importá-la.
// Exemplo: import com.eventually.view.UserScheduleView;

/**
 * Classe responsável pela tela de login da aplicação Eventually.
 * Esta classe cria a interface de login com campos para e-mail e senha,
 * além de botões para login e registro de novos usuários.
 *
 * @author [Seu Nome]
 * @version 1.01
 * @since 2025-05-07
 */
public class LoginView extends BorderPane {

    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private UserController userController;

    /**
     * Construtor que inicializa a interface de login.
     * Recebe o UserController via injeção de dependência.
     */
    public LoginView(UserController userController) {
        if (userController == null) {
            // Lançar uma exceção ou lidar com o erro é importante na prática
            System.err.println("UserController não pode ser nulo na LoginView!");
            // Poderia lançar: throw new IllegalArgumentException("UserController cannot be null");
            // Por simplicidade no exemplo, apenas logamos o erro.
            // Em uma aplicação real, a inicialização deveria falhar ou ter um fallback.
        }
        this.userController = userController;
        setupUI();
    }

    /**
     * Configura todos os elementos da interface de usuário para a tela de login.
     */
    private void setupUI() {
        // Configuração do plano de fundo com uma imagem escurecida
        setupBackground();

        // Criação do painel lateral direito em roxo
        StackPane rightPanel = createRightPanel();

        // Criação do conteúdo do login (logotipo, campos, botões)
        VBox loginContent = createLoginContent();

        // Adiciona os elementos ao layout principal
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
            // Fallback para um fundo preto caso a imagem não seja encontrada
            setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            System.err.println("Erro ao carregar imagem de fundo: " + e.getMessage());
        }
    }

    /**
     * Cria o painel lateral direito com formato de trapézio e degradê roxo com opacidade de 80%.
     * @return O painel configurado
     */
    private StackPane createRightPanel() {
        // Criamos um StackPane para conter o trapézio
        StackPane rightPanel = new StackPane();
        rightPanel.setPrefWidth(300);
        rightPanel.setPrefHeight(900);

        // Criamos o trapézio
        javafx.scene.shape.Polygon trapezoid = new javafx.scene.shape.Polygon();

        // Definimos as coordenadas do trapézio (x,y pontos no sentido horário)
        // Começando do topo direito e movendo-se no sentido horário
        trapezoid.getPoints().addAll(new Double[]{
                300.0, 0.0,    // topo direito
                300.0, 720.0,  // base direita
                0.0, 720.0,    // base esquerda
                80.0, 0.0      // topo esquerdo (para criar o efeito de trapézio)
        });

        // Criamos um gradiente roxo com opacidade de 80%
        javafx.scene.paint.LinearGradient gradient = new javafx.scene.paint.LinearGradient(
                0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop[]{
                        new javafx.scene.paint.Stop(0, Color.rgb(128, 0, 128, 0.8)),  // Roxo com 80% de opacidade
                        new javafx.scene.paint.Stop(1, Color.rgb(180, 0, 180, 0.8))   // Roxo mais claro com 80% de opacidade
                }
        );

        // Aplicamos o gradiente ao trapézio
        trapezoid.setFill(gradient);

        // Adiciona o trapézio ao painel
        rightPanel.getChildren().add(trapezoid);

        return rightPanel;
    }

    /**
     * Cria o conteúdo principal do formulário de login.
     * @return VBox contendo todos os elementos do formulário
     */
    private VBox createLoginContent() {
        VBox loginBox = new VBox(25);
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setPadding(new Insets(0, 0, 0, 100));
        loginBox.setMaxWidth(600);

        // Logo "Eventually join the moment" como imagem
        ImageView logoImageView = new ImageView();
        try {
            Image logoImage = new Image(getClass().getResource("/images/eventually-logo.png").toExternalForm());
            logoImageView.setImage(logoImage);
            logoImageView.setFitWidth(350); // Ajuste conforme o tamanho da sua imagem
            logoImageView.setPreserveRatio(true);
        } catch (Exception e) {
            // Fallback caso a imagem não seja encontrada
            System.err.println("Erro ao carregar o logo: " + e.getMessage());

            // Criação de texto como fallback
            HBox fallbackLogoBox = new HBox();
            Text eventText = new Text("Even");
            eventText.setFill(Color.web("#800080"));
            eventText.setFont(Font.font("Arial", FontWeight.BOLD, 48));

            Text tuallyText = new Text("tually");
            tuallyText.setFill(Color.WHITE);
            tuallyText.setFont(Font.font("Arial", FontWeight.BOLD, 48));

            fallbackLogoBox.getChildren().addAll(eventText, tuallyText);
            logoImageView = null; // Define como null para usar o fallback abaixo
        }

        // Cria um container para o logo
        VBox logoContainer = new VBox(10);
        if (logoImageView != null && logoImageView.getImage() != null) { // Verifica se a imagem foi carregada
            logoContainer.getChildren().add(logoImageView);
        } else {
            // Usa o fallback se a imagem não carregou
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

        // Campo de email
        emailField = new TextField();
        emailField.setPromptText("E-mail");
        emailField.setPrefHeight(40);
        emailField.getStyleClass().add("login-field");

        // Campo de senha
        passwordField = new PasswordField();
        passwordField.setPromptText("Senha");
        passwordField.setPrefHeight(40);
        passwordField.getStyleClass().add("login-field");

        // Botão de login
        loginButton = new Button("Login");
        loginButton.setPrefHeight(40);
        loginButton.setPrefWidth(400);
        loginButton.getStyleClass().add("login-button");
        // A ação agora chama o método handleLogin refatorado
        loginButton.setOnAction(e -> handleLogin());

        // Botão de registro
        registerButton = new Button("Register");
        registerButton.setPrefHeight(30);
        registerButton.setPrefWidth(120);
        registerButton.getStyleClass().add("register-button");
        // A ação agora chama o método handleRegister refatorado
        registerButton.setOnAction(e -> handleRegister());

        HBox registerBox = new HBox();
        registerBox.setAlignment(Pos.CENTER);
        registerBox.getChildren().add(registerButton);
        registerBox.setPadding(new Insets(10, 0, 0, 0));

        // Adiciona todos os elementos ao VBox principal
        loginBox.getChildren().addAll(logoContainer, emailField, passwordField, loginButton, registerBox);

        return loginBox;
    }

    /**
     * Manipula o evento de clique no botão de login.
     * Delega a lógica para o UserController e lida com o resultado.
     */
    private void handleLogin() {
        // Verifica se o controller foi injetado corretamente
        if (userController == null) {
            showError("Erro interno: UserController não inicializado.");
            return;
        }

        String email = emailField.getText();
        String password = passwordField.getText();

        // Chama o método do controller que contém a lógica
        String result = userController.handleLoginRequest(email, password);

        if (result == null) {
            // Se o resultado for null, o login foi bem-sucedido
            try {
                openUserScheduleView(); // Navega para a próxima tela
            } catch (Exception e) {
                showError("Erro ao abrir a tela principal: " + e.getMessage());
                e.printStackTrace(); // Adicionado para depuração
            }
        } else {
            // Se houver uma string de resultado, é uma mensagem de erro
            showError(result);
        }
    }

    /**
     * Manipula o evento de clique no botão de registro.
     * Notifica o UserController e inicia a navegação.
     */
    private void handleRegister() {
        // Verifica se o controller foi injetado corretamente
        if (userController == null) {
            showError("Erro interno: UserController não inicializado.");
            return;
        }
        // Notifica o controller sobre a intenção (pode fazer algo no futuro)
        userController.handleRegistrationRequest();

        // Inicia a navegação para a tela de registro (ainda acionado pela View)
        try {
            openRegisterView();
        } catch (Exception e) {
            showError("Erro ao abrir a tela de registro: " + e.getMessage());
            e.printStackTrace(); // Adicionado para depuração
        }
    }

    /**
     * Abre a tela de agendamento do usuário
     */
    private void openUserScheduleView() { // Nome do método alterado
        Stage currentStage = (Stage) this.getScene().getWindow();

        // Altere MainView para UserScheduleView
        UserScheduleView userScheduleView = new UserScheduleView(); // << ALTERAÇÃO AQUI
        Scene scene = new Scene(userScheduleView, 1280, 720); // << ALTERAÇÃO AQUI
        // Certifique-se de que UserScheduleView é um Pane ou um de seus subtipos.

        // Adiciona a folha de estilos, se aplicável à nova view
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Erro ao carregar styles.css: Arquivo não encontrado. " + e.getMessage());
            // Considere ter um estilo padrão ou lidar com isso de outra forma
        }


        currentStage.setScene(scene);
        currentStage.setTitle("Eventually - Agenda do Usuário"); // Título pode ser atualizado
    }

    /**
     * Abre a tela de registro.
     */
    private void openRegisterView() {
        // Implementar navegação para a tela de registro
        // Exemplo:
        // RegisterView registerView = new RegisterView();
        // Scene scene = new Scene(registerView, 1280, 720);
        // Stage currentStage = (Stage) this.getScene().getWindow();
        // try {
        //    scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        // } catch (NullPointerException e) {
        //    System.err.println("Erro ao carregar styles.css para RegisterView: " + e.getMessage());
        // }
        // currentStage.setScene(scene);
        // currentStage.setTitle("Eventually - Registro");
        showError("Funcionalidade de registro ainda não implementada."); // Placeholder
    }

    /**
     * Exibe uma mensagem de erro na interface.
     * @param message A mensagem de erro a ser exibida
     */
    private void showError(String message) {
        // Idealmente, isso seria um Alert ou um Label na UI.
        // Por enquanto, apenas imprimindo no console de erro.
        System.err.println("ERRO: " + message);

        // Exemplo de como exibir um Alert:
        /*
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        */
    }
}