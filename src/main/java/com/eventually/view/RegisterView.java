package com.eventually.view;

import com.eventually.controller.RegisterController;
import com.eventually.controller.UserController; // Adicionado
import com.eventually.view.LoginView;           // Adicionado
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene; // Adicionado
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Classe da tela de registro.
 * Esta classe é responsável
 * por cadastrar o usuário na memória.
 *
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-05-13
 */
public class RegisterView extends BorderPane {

    private RegisterController registerController;

    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private DatePicker dobPicker;
    private TextField cityField;

    private Label nameErrorLabel;
    private Label emailErrorLabel;
    private Label passwordErrorLabel;
    private Label dobErrorLabel;
    private Label cityErrorLabel;
    private Label themesErrorLabel;
    private Label generalErrorLabel;

    private Label nameRuleLabel;
    private Label passwordIntroRuleLabel;
    private Label passwordRuleSpecialChar;
    private Label passwordRuleDigit;
    private Label passwordRuleLetter;

    private CheckBox corporateCb;
    private CheckBox charitableCb;
    private CheckBox educationalCb;
    private CheckBox culturalCb;
    private CheckBox sportsCb;
    private CheckBox religiousCb;
    private CheckBox socialCb;

    private Button registerButton;
    private Hyperlink backToLoginLink;


    public RegisterView(RegisterController registerController) {
        if (registerController == null) {
            System.err.println("RegisterController não pode ser nulo na RegisterView!");
        }
        this.registerController = registerController;
        setupUI();
        setupDynamicValidation();
    }

    private void setupUI() {
        try {
            setBackground(new LoginView(null).getBackground());
        } catch(Exception e) {
            setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            System.err.println("Fallback de fundo para RegisterView: " + e.getMessage());
        }

        StackPane rightPanelContainer = createRightPanelBase();
        VBox rightPanelContent = createRightPanelContent();
        rightPanelContainer.getChildren().add(rightPanelContent);

        VBox registrationForm = createRegistrationForm();

        setRight(rightPanelContainer);
        setCenter(registrationForm);

        try {
            this.getStylesheets().add(getClass().getResource("/styles/register-styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Erro ao carregar /styles/styles.css para RegisterView: " + e.getMessage());
        }
    }

    private StackPane createRightPanelBase() {
        StackPane rightPanel = new StackPane();
        rightPanel.setPrefWidth(300);
        rightPanel.setPrefHeight(900);

        Polygon trapezoid = new Polygon();
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
        rightPanel.getChildren().add(trapezoid);
        return rightPanel;
    }


    private VBox createRightPanelContent() {
        VBox themesSelectionBox = new VBox(15);
        themesSelectionBox.setAlignment(Pos.CENTER_LEFT);
        themesSelectionBox.setPadding(new Insets(40, 20, 20, 40));

        Text themesTitle = new Text("Selecione temas desejados:");
        themesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        themesTitle.setFill(Color.WHITE);

        corporateCb = new CheckBox("Corporativo");
        charitableCb = new CheckBox("Beneficente");
        educationalCb = new CheckBox("Educacional");
        culturalCb = new CheckBox("Cultural");
        sportsCb = new CheckBox("Esportivo");
        religiousCb = new CheckBox("Religioso");
        socialCb = new CheckBox("Social");

        for (CheckBox cb : List.of(corporateCb, charitableCb, educationalCb, culturalCb, sportsCb, religiousCb, socialCb)) {
            cb.setTextFill(Color.WHITE);
        }

        themesErrorLabel = new Label();
        themesErrorLabel.setTextFill(Color.SALMON);
        themesErrorLabel.setVisible(false);

        themesSelectionBox.getChildren().addAll(
                themesTitle,
                corporateCb, charitableCb, educationalCb,
                culturalCb, sportsCb, religiousCb, socialCb,
                themesErrorLabel
        );
        return themesSelectionBox;
    }


    private VBox createRegistrationForm() {
        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(40, 50, 40, 100));
        formBox.setMaxWidth(600);

        Text title = new Text("Criar Nova Conta");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setFill(Color.WHITE);
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setPadding(new Insets(0,0,20,0));

        nameField = new TextField();
        nameField.setPromptText("Nome Completo");
        nameField.getStyleClass().add("form-field");
        nameRuleLabel = new Label("* O nome deve conter pelo menos nome e sobrenome.");
        nameRuleLabel.setTextFill(Color.LIGHTGRAY);
        nameErrorLabel = new Label();
        nameErrorLabel.getStyleClass().add("error-label");
        nameErrorLabel.setTextFill(Color.SALMON);
        nameErrorLabel.setVisible(false);
        VBox nameBox = new VBox(3, nameField, nameRuleLabel, nameErrorLabel);

        emailField = new TextField();
        emailField.setPromptText("E-mail");
        emailField.getStyleClass().add("form-field");
        emailErrorLabel = new Label();
        emailErrorLabel.getStyleClass().add("error-label");
        emailErrorLabel.setTextFill(Color.SALMON);
        emailErrorLabel.setVisible(false);
        VBox emailBox = new VBox(3, emailField, emailErrorLabel);

        passwordField = new PasswordField();
        passwordField.setPromptText("Senha");
        passwordField.getStyleClass().add("form-field");

        passwordIntroRuleLabel = new Label("* A senha deve conter, no mínimo:");
        passwordIntroRuleLabel.setTextFill(Color.LIGHTGRAY);
        passwordRuleSpecialChar = new Label("- 1 caractere especial");
        passwordRuleSpecialChar.setTextFill(Color.LIGHTGRAY);
        passwordRuleDigit = new Label("- 1 dígito");
        passwordRuleDigit.setTextFill(Color.LIGHTGRAY);
        passwordRuleLetter = new Label("- 1 letra");
        passwordRuleLetter.setTextFill(Color.LIGHTGRAY);

        VBox passwordRulesBox = new VBox(1, passwordIntroRuleLabel, passwordRuleSpecialChar, passwordRuleDigit, passwordRuleLetter);
        passwordRulesBox.setPadding(new Insets(2,0,2,0));

        passwordErrorLabel = new Label();
        passwordErrorLabel.getStyleClass().add("error-label");
        passwordErrorLabel.setTextFill(Color.SALMON);
        passwordErrorLabel.setVisible(false);
        VBox passwordBox = new VBox(3, passwordField, passwordRulesBox, passwordErrorLabel);

        dobPicker = new DatePicker();
        dobPicker.setPromptText("Data de Nascimento");
        dobPicker.getStyleClass().add("form-field");
        dobPicker.setPrefWidth(Double.MAX_VALUE);
        dobErrorLabel = new Label();
        dobErrorLabel.getStyleClass().add("error-label");
        dobErrorLabel.setTextFill(Color.SALMON);
        dobErrorLabel.setVisible(false);
        VBox dobBox = new VBox(3, dobPicker, dobErrorLabel);

        cityField = new TextField();
        cityField.setPromptText("Cidade");
        cityField.getStyleClass().add("form-field");
        cityErrorLabel = new Label();
        cityErrorLabel.getStyleClass().add("error-label");
        cityErrorLabel.setTextFill(Color.SALMON);
        cityErrorLabel.setVisible(false);
        VBox cityBox = new VBox(3, cityField, cityErrorLabel);

        registerButton = new Button("Registrar");
        registerButton.getStyleClass().add("submit-button");
        registerButton.setPrefHeight(40);
        registerButton.setPrefWidth(Double.MAX_VALUE);
        registerButton.setOnAction(e -> handleRegisterSubmit());

        backToLoginLink = new Hyperlink("Já tem uma conta? Faça login");
        backToLoginLink.setTextFill(Color.LIGHTBLUE);
        backToLoginLink.setOnAction(e -> {
            if(registerController != null) {
                registerController.handleNavigateToLogin();
            }
            navigateToLoginScreen();
        });
        HBox backLinkBox = new HBox(backToLoginLink);
        backLinkBox.setAlignment(Pos.CENTER);
        backLinkBox.setPadding(new Insets(10,0,0,0));

        generalErrorLabel = new Label();
        generalErrorLabel.setTextFill(Color.SALMON);
        generalErrorLabel.getStyleClass().add("error-label");
        generalErrorLabel.setVisible(false);
        HBox generalErrorBox = new HBox(generalErrorLabel);
        generalErrorBox.setAlignment(Pos.CENTER);

        formBox.getChildren().addAll(
                titleBox, nameBox, emailBox, passwordBox, dobBox, cityBox,
                registerButton, generalErrorBox, backLinkBox
        );

        return formBox;
    }

    private void setupDynamicValidation() {
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (registerController.isNameRuleMet(newVal)) {
                nameRuleLabel.setTextFill(Color.LIGHTGREEN);
            } else {
                nameRuleLabel.setTextFill(Color.LIGHTGRAY);
            }
            nameErrorLabel.setVisible(false);
            generalErrorLabel.setVisible(false);
        });

        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            Map<String, Boolean> rulesStatus = registerController.checkPasswordRules(newVal);
            updatePasswordRuleUI(passwordRuleSpecialChar, rulesStatus.getOrDefault("hasSpecial", false));
            updatePasswordRuleUI(passwordRuleDigit, rulesStatus.getOrDefault("hasDigit", false));
            updatePasswordRuleUI(passwordRuleLetter, rulesStatus.getOrDefault("hasLetter", false));
            passwordErrorLabel.setVisible(false);
            generalErrorLabel.setVisible(false);
        });

        emailField.textProperty().addListener( (obs,ov,nv) -> { emailErrorLabel.setVisible(false); generalErrorLabel.setVisible(false); });
        dobPicker.valueProperty().addListener( (obs,ov,nv) -> { dobErrorLabel.setVisible(false); generalErrorLabel.setVisible(false); });
        cityField.textProperty().addListener( (obs,ov,nv) -> { cityErrorLabel.setVisible(false); generalErrorLabel.setVisible(false); });

        List.of(corporateCb, charitableCb, educationalCb, culturalCb, sportsCb, religiousCb, socialCb).forEach(cb ->
                cb.selectedProperty().addListener((obs, ov, nv) -> {
                    themesErrorLabel.setVisible(false);
                    generalErrorLabel.setVisible(false);
                })
        );
    }

    private void updatePasswordRuleUI(Label ruleLabel, boolean isMet) {
        if (isMet) {
            ruleLabel.setTextFill(Color.LIGHTGREEN);
        } else {
            ruleLabel.setTextFill(Color.LIGHTGRAY);
        }
    }

    private void handleRegisterSubmit() {
        if (registerController == null) {
            generalErrorLabel.setText("Erro interno: Controller não configurado.");
            generalErrorLabel.setVisible(true);
            return;
        }
        clearAllErrors();

        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        LocalDate dob = dobPicker.getValue();
        String city = cityField.getText();
        List<String> selectedThemes = getSelectedThemes();

        boolean allFieldsValid = true;

        String nameValidationError = registerController.validateNameForSubmit(name);
        if (nameValidationError != null) {
            nameErrorLabel.setText(nameValidationError);
            nameErrorLabel.setVisible(true);
            allFieldsValid = false;
        }

        String emailValidationError = registerController.validateEmailForSubmit(email);
        if (emailValidationError != null) {
            emailErrorLabel.setText(emailValidationError);
            emailErrorLabel.setVisible(true);
            allFieldsValid = false;
        }

        String passwordValidationError = registerController.validatePasswordForSubmit(password);
        if (passwordValidationError != null) {
            passwordErrorLabel.setText(passwordValidationError);
            passwordErrorLabel.setVisible(true);
            allFieldsValid = false;
        }

        String dobValidationError = registerController.validateDobForSubmit(dob);
        if (dobValidationError != null) {
            dobErrorLabel.setText(dobValidationError);
            dobErrorLabel.setVisible(true);
            allFieldsValid = false;
        }

        String cityValidationError = registerController.validateCityForSubmit(city);
        if (cityValidationError != null) {
            cityErrorLabel.setText(cityValidationError);
            cityErrorLabel.setVisible(true);
            allFieldsValid = false;
        }

        String themesValidationError = registerController.validateThemesForSubmit(selectedThemes);
        if (themesValidationError != null) {
            themesErrorLabel.setText(themesValidationError);
            themesErrorLabel.setVisible(true);
            allFieldsValid = false;
        }

        if (allFieldsValid) {
            boolean success = registerController.processRegistration(name, email, password, dob, city, selectedThemes);
            if (success) {
                generalErrorLabel.setText("Registro realizado com sucesso!");
                generalErrorLabel.setTextFill(Color.LIGHTGREEN);
                generalErrorLabel.setVisible(true);
                registerButton.setDisable(true);
            } else {
                generalErrorLabel.setText("Falha no registro. Verifique os dados.");
                generalErrorLabel.setVisible(true);
            }
        } else {
            generalErrorLabel.setText("Alguns campos contêm erros. Verifique.");
            generalErrorLabel.setVisible(true);
        }
    }

    private void clearAllErrors() {
        nameErrorLabel.setVisible(false);
        emailErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        dobErrorLabel.setVisible(false);
        cityErrorLabel.setVisible(false);
        themesErrorLabel.setVisible(false);
        generalErrorLabel.setVisible(false);
    }

    private List<String> getSelectedThemes() {
        List<String> themes = new ArrayList<>();
        if (corporateCb.isSelected()) themes.add(corporateCb.getText());
        if (charitableCb.isSelected()) themes.add(charitableCb.getText());
        if (educationalCb.isSelected()) themes.add(educationalCb.getText());
        if (culturalCb.isSelected()) themes.add(culturalCb.getText());
        if (sportsCb.isSelected()) themes.add(sportsCb.getText());
        if (religiousCb.isSelected()) themes.add(religiousCb.getText());
        if (socialCb.isSelected()) themes.add(socialCb.getText());
        return themes;
    }

    private void navigateToLoginScreen() {
        try {
            Stage currentStage = (Stage) this.getScene().getWindow();
            UserController userController = new UserController();
            LoginView loginView = new LoginView(userController);
            Scene loginScene = new Scene(loginView, 1280, 720);
            try {
                loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
            } catch (Exception cssEx) {
                System.err.println("Erro ao carregar CSS para LoginView: " + cssEx.getMessage());
            }
            currentStage.setScene(loginScene);
            currentStage.setTitle("Eventually - Login");
            currentStage.setMaximized(true);
        } catch (Exception ex) {
            System.err.println("Erro ao navegar para a tela de Login: " + ex.getMessage());
            ex.printStackTrace();
            if (generalErrorLabel != null) {
                generalErrorLabel.setText("Erro ao tentar voltar para tela de login.");
                generalErrorLabel.setVisible(true);
            }
        }
    }
}