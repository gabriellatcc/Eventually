package com.eventually.view;

import com.eventually.controller.SettingsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Classe para a tela de Configurações.
 * Exibe e permite a alteração das preferências do usuário e de conteúdo.
 * @author Yuri Garcia Maia (Criação)
 * @since 22-05-2025
 * @version 1.0
 * @author Gabriella Tavares Costa Corrêa (Revisão de documentação, lógica e da estrutura da classe)
 * @since 22-05-2025
 */
public class SettingsView extends BorderPane {

    private SettingsController sController;

    private Button btnInicio;
    private Button btnMeusEventos;
    private Button btnConfiguracoes;
    private Button btnSair;
    private Button btnProgramacao;
    private Button btnDeleteAccount;

    private Label lbNomeUsuarioHeader;
    private Label nameDisplay, emailDisplay, phoneDisplay, passwordDisplay, cityDisplay, dobDisplay;
    private ImageView profilePhotoView;
    private Label profilePhotoErrorLabel;
    private Circle avatarHeader;

    private List<CheckBox> themeCheckBoxes = new ArrayList<>();

    private final DateTimeFormatter appDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor da SettingsView.
     */
    public SettingsView() {
        setupUI();
    }

    /**
     * Define o controller para esta view.
     * @param sController O controller a ser usado.
     */
    public void setSettingsController(SettingsController sController) {
        this.sController = sController;
    }

    /**
     * Configura a interface gráfica principal da tela de configurações.
     */
    private void setupUI() {
        VBox barraLateral = criarBarraLateral();
        HBox barraSuperior = createTopbar();
        VBox conteudoCentral = createCenterContent();

        HBox subHeader = createSubHeaderControls();
        VBox mainContentWrapper = new VBox(conteudoCentral);
        VBox.setVgrow(mainContentWrapper, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(conteudoCentral);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(0));
        scrollPane.setStyle("-fx-background-color:transparent;");

        VBox centerAreaWithSubHeader = new VBox(subHeader, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(centerAreaWithSubHeader);
    }

    /**
     * Este método criarBarraLateral() cria uma barra lateral de navegação vertical da interface.
     * Essa barra aparece na lateral esquerda da tela e contém botões para acessar
     * diferentes seções do aplicativo:
     * Página Inicial, Meus eventos Programação, Configurações e saída
     * @return a barra lateral.
     */
    private VBox criarBarraLateral() {
        VBox barraLateral = new VBox(20);
        barraLateral.setPadding(new Insets(20));
        barraLateral.getStyleClass().add("sidebar");
        barraLateral.setPrefWidth(200);
        barraLateral.setAlignment(Pos.TOP_CENTER);

        btnInicio = new Button("Página inicial");
        btnInicio.getStyleClass().add("menu-button");
        btnInicio.setMaxWidth(Double.MAX_VALUE);
        btnInicio.setPadding(new Insets(0,0,15,0));

        btnMeusEventos = new Button("Meus eventos");
        btnMeusEventos.getStyleClass().add("menu-button");
        btnMeusEventos.setMaxWidth(Double.MAX_VALUE);
        btnMeusEventos.setPadding(new Insets(0,0,15,0));

        btnProgramacao = new Button("Programação");
        btnProgramacao.getStyleClass().add("menu-button");
        btnProgramacao.setMaxWidth(Double.MAX_VALUE);
        btnProgramacao.setPadding(new Insets(0,0,15,0));

        VBox parteSuperior = new VBox(15, btnInicio, btnMeusEventos, btnProgramacao);
        parteSuperior.setPadding(new Insets(20,15,15,15));

        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        btnConfiguracoes = new Button("Configurações");
        btnConfiguracoes.getStyleClass().add("menu-button");
        btnConfiguracoes.setMaxWidth(Double.MAX_VALUE);
        btnConfiguracoes.setPadding(new Insets(0,0,15,0));

        btnSair = new Button("Sair");
        btnSair.getStyleClass().add("menu-button");
        btnSair.setMaxWidth(Double.MAX_VALUE);
        btnSair.setPadding(new Insets(0,0,15,0));

        VBox parteInferior = new VBox(15, btnConfiguracoes, btnSair);
        parteInferior.setPadding(new Insets(0,15,40,15));

        barraLateral.getChildren().addAll(parteSuperior, espacador, parteInferior);
        barraLateral.setPadding(new Insets(0));
        return barraLateral;
    }

    /**
     * Este método cria a barra superior com o logo.
     * @return A HBox da barra superior.
     */
    private HBox createTopbar() {
        HBox topbar = new HBox();
        topbar.setPadding(new Insets(20));
        topbar.setAlignment(Pos.CENTER);
        topbar.getStyleClass().add("topbar");
        Label logo = new Label("Eventually");
        logo.getStyleClass().add("logo");
        topbar.getChildren().add(logo);
        return topbar;
    }

    /**
     * Este método cria o sub-cabeçalho com controles de navegação e display do usuário.
     * @return A HBox do sub-cabeçalho.
     */
    private HBox createSubHeaderControls() {
        HBox subHeader = new HBox(15);
        subHeader.setPadding(new Insets(10, 20, 10, 20));
        subHeader.setAlignment(Pos.CENTER_LEFT);
        subHeader.getStyleClass().add("sub-header-controls");

        lbNomeUsuarioHeader = new Label("Usuário Exemplo");
        lbNomeUsuarioHeader.getStyleClass().add("user-display-label");

        avatarHeader = new Circle(18);
        avatarHeader.getStyleClass().add("avatar-circle");
        avatarHeader.setFill(Color.LIGHTGRAY);

        HBox userDisplayBox = new HBox(8, lbNomeUsuarioHeader, avatarHeader);
        userDisplayBox.setAlignment(Pos.CENTER);
        userDisplayBox.getStyleClass().add("user-display-box");

        btnProgramacao.getStyleClass().add("top-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        subHeader.getChildren().addAll(spacer, userDisplayBox);
        return subHeader;
    }

    /**
     * Este método cria o container central da tela de configurações.
     * @return A VBox com o conteúdo de configurações.
     */
    private VBox createCenterContent() {
        VBox centerContent = new VBox(30);
        centerContent.setPadding(new Insets(20));
        centerContent.getStyleClass().add("center-content-area");
        centerContent.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Configurações");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.getStyleClass().add("settings-title");

        VBox contentPrefsSection = createContentPreferencesSection();
        VBox userPrefsSection = createUserPreferencesSection();

        btnDeleteAccount = new Button("Excluir conta");
        btnDeleteAccount.getStyleClass().add("delete-account-button");
        btnDeleteAccount.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-weight: bold;");
        btnDeleteAccount.setPrefWidth(150);

        HBox deleteButtonContainer = new HBox(btnDeleteAccount);
        deleteButtonContainer.setAlignment(Pos.CENTER_RIGHT);
        deleteButtonContainer.setPadding(new Insets(20,0,0,0));

        centerContent.getChildren().addAll(title, contentPrefsSection, userPrefsSection, deleteButtonContainer);
        return centerContent;
    }

    /**
     * Este método cria a seção de preferências de conteúdo.
     * @return A VBox da seção de preferências de conteúdo.
     */
    private VBox createContentPreferencesSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(10));

        Label title = new Label("Preferências de conteúdo");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label description = new Label("(Remover eventos visualizados na página inicial - descrição exemplo)");
        description.setFont(Font.font("Arial", 12));
        description.setTextFill(Color.GRAY);

        // Cria os dois grupos (colunas)
        VBox leftColumn = new VBox(10);
        VBox rightColumn = new VBox(10);

        // Dados das categorias
        String[] leftNames = {"Corporativos", "Educacionais", "Culturais", "Esportivos"};
        String[] leftDescriptions = {
                "(palestras, workshops, feiras de negócios)",
                "(palestras, seminários, cursos)",
                "(shows, exposições, festivais)",
                "(competições, maratonas, torneios)"
        };

        String[] rightNames = {"Beneficentes", "Religiosos", "Sociais"};
        String[] rightDescriptions = {
                "(arrecadação de fundos, campanhas sociais)",
                "(cultos, retiros, encontros espirituais)",
                "(aniversários, casamentos, confraternizações)"
        };

        themeCheckBoxes.clear();

        for (int i = 0; i < leftNames.length; i++) {
            CheckBox cb = new CheckBox(leftNames[i]);
            Label desc = new Label(leftDescriptions[i]);
            desc.setFont(Font.font("Arial", 11));
            desc.setTextFill(Color.DARKSLATEGRAY);
            VBox entry = new VBox(2, cb, desc);
            leftColumn.getChildren().add(entry);
            themeCheckBoxes.add(cb);
        }

        for (int i = 0; i < rightNames.length; i++) {
            CheckBox cb = new CheckBox(rightNames[i]);
            Label desc = new Label(rightDescriptions[i]);
            desc.setFont(Font.font("Arial", 11));
            desc.setTextFill(Color.DARKSLATEGRAY);
            VBox entry = new VBox(2, cb, desc);
            rightColumn.getChildren().add(entry);
            themeCheckBoxes.add(cb);
        }

        // Organiza as colunas em um HBox
        HBox columns = new HBox(60, leftColumn, rightColumn);

        section.getChildren().addAll(title, description, columns);
        return section;
    }

    /**
     * Este método cria a seção de preferências de usuário.
     * @return A VBox da seção de preferências de usuário.
     */
    private VBox createUserPreferencesSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(10));

        Label title = new Label("Preferências de usuário");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label description = new Label("(Edição de informações do usuário - descrição exemplo)");
        description.setFont(Font.font("Arial", 12));
        description.setTextFill(Color.GRAY);

        GridPane userDetailsGrid = new GridPane();
        userDetailsGrid.setHgap(10);
        userDetailsGrid.setVgap(10);
        userDetailsGrid.setPadding(new Insets(10, 0, 10, 0));

        nameDisplay = new Label();
        StackPane nameFieldContainer = createEditableField("name", "Nome:", nameDisplay, "Exemplo Nome");
        userDetailsGrid.add(new Label("Nome:"), 0, 0);
        userDetailsGrid.add(nameFieldContainer, 1, 0);

        emailDisplay = new Label();
        Label emailLabelForDisplay = new Label("Email:");
        userDetailsGrid.add(emailLabelForDisplay, 0, 1);
        userDetailsGrid.add(emailDisplay, 1, 1);

        phoneDisplay = new Label();
        StackPane phoneFieldContainer = createEditableField("phone", "Telefone:", phoneDisplay, "(xx) xxxxx-xxxx");
        userDetailsGrid.add(new Label("Telefone:"), 0, 2);
        userDetailsGrid.add(phoneFieldContainer, 1, 2);

        passwordDisplay = new Label("********");
        StackPane passwordFieldContainer = createEditableField("password", "Senha:", passwordDisplay, "", true); // isPassword = true
        userDetailsGrid.add(new Label("Senha:"), 0, 3);
        userDetailsGrid.add(passwordFieldContainer, 1, 3);

        cityDisplay = new Label();
        StackPane cityFieldContainer = createEditableField("city", "Cidade:", cityDisplay, "Cidade Exemplo");
        userDetailsGrid.add(new Label("Cidade:"), 0, 4);
        userDetailsGrid.add(cityFieldContainer, 1, 4);

        dobDisplay = new Label();
        StackPane dobFieldContainer = createEditableField("dateOfBirth", "Data de Nasc.:", dobDisplay, "dd/mm/yyyy");
        userDetailsGrid.add(new Label("Data de Nascimento:"), 0, 5);
        userDetailsGrid.add(dobFieldContainer, 1, 5);

        VBox photoSection = createProfilePhotoSection();

        HBox userPrefsMainLayout = new HBox(30, userDetailsGrid, photoSection);
        HBox.setHgrow(userDetailsGrid, Priority.ALWAYS);

        section.getChildren().addAll(title, description, userPrefsMainLayout);
        return section;
    }

    /**
     * Este método cria a seção de foto de perfil.
     * @return A VBox da seção de foto de perfil.
     */
    private VBox createProfilePhotoSection() {
        VBox photoBox = new VBox(10);
        photoBox.setAlignment(Pos.CENTER);
        photoBox.setPadding(new Insets(10));

        Label photoTitle = new Label("Foto de perfil");
        photoTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        profilePhotoView = new ImageView();
        profilePhotoView.setFitHeight(100);
        profilePhotoView.setFitWidth(100);
        profilePhotoView.setPreserveRatio(true);

        StackPane photoPlaceholder = new StackPane(profilePhotoView);
        photoPlaceholder.setPrefSize(100,100);
        photoPlaceholder.setMinSize(100,100);
        photoPlaceholder.setMaxSize(100,100);

        photoPlaceholder.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-background-color: #f0f0f0;");


        // try {
        //     Image defaultImg = new Image(getClass().getResourceAsStream("/images/default-avatar.png"));
        //     profilePhotoView.setImage(defaultImg);
        // } catch (Exception e) {
        //     System.err.println("Erro ao carregar avatar padrão: " + e.getMessage());
        // }


        Label photoInfo = new Label("A imagem anexada deve ter\nXX x XX pixels (YY MB)");
        photoInfo.setFont(Font.font("Arial", 10));
        photoInfo.setTextFill(Color.GRAY);
        photoInfo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);


        Hyperlink changePhotoLink = new Hyperlink("(alterar)");
/*        changePhotoLink.setOnAction(e -> sController.handleChangeProfilePhoto()); */

        profilePhotoErrorLabel = new Label();
        profilePhotoErrorLabel.setTextFill(Color.SALMON);
        profilePhotoErrorLabel.setVisible(false);

        photoBox.getChildren().addAll(photoTitle, photoPlaceholder, photoInfo, changePhotoLink, profilePhotoErrorLabel);
        return photoBox;
    }

    /**
     * Este método cria um campo editável genérico com Label, TextField/DatePicker e botões Salvar/Cancelar.
     * @param fieldKey A chave identificadora do campo.
     * @param labelText O texto do rótulo do campo.
     * @param displayLabel O Label usado para exibir o valor atual.
     * @param promptText O texto de prompt para o campo de edição.
     * @return Um StackPane contendo os modos de visualização e edição.
     */
    private StackPane createEditableField(String fieldKey, String labelText, Label displayLabel, String promptText) {
        return createEditableField(fieldKey, labelText, displayLabel, promptText, false);
    }

    /**
     * Este método cria um campo editável genérico com Label, TextField/PasswordField/DatePicker e botões Salvar/Cancelar.
     * @param fieldKey A chave identificadora do campo.
     * @param displayLabel O Label usado para exibir o valor atual.
     * @param promptText O texto de prompt para o campo de edição.
     * @param isPassword Indica se o campo é uma senha.
     * @return Um StackPane contendo os modos de visualização e edição.
     */
    private StackPane createEditableField(String fieldKey, String labelTextUnused, Label displayLabel, String promptText, boolean isPassword) {
        StackPane stack = new StackPane();

        HBox viewMode = new HBox(10);
        viewMode.setAlignment(Pos.CENTER_LEFT);
        Hyperlink alterLink = new Hyperlink("(alterar)");
        viewMode.getChildren().addAll(displayLabel, alterLink);

        HBox editMode = new HBox(5);
        editMode.setAlignment(Pos.CENTER_LEFT);
        Node editField;

        if ("dateOfBirth".equals(fieldKey)) {
            DatePicker picker = new DatePicker();
            picker.setPromptText(promptText);
            picker.setPrefWidth(150);
            editField = picker;
        } else if (isPassword) {
            PasswordField passField = new PasswordField();
            passField.setPromptText("Nova Senha");
            passField.setPrefWidth(150);
            editField = passField;
        } else {
            TextField textField = new TextField();
            textField.setPromptText(promptText);
            textField.setPrefWidth(150);
            editField = textField;
        }

        Button saveButton = new Button("Salvar");
        Button cancelButton = new Button("Cancelar");
        editMode.getChildren().addAll(editField, saveButton, cancelButton);
        editMode.setVisible(false);

        Label fieldErrorLabel = new Label();
        fieldErrorLabel.setTextFill(Color.SALMON);
        fieldErrorLabel.setFont(Font.font("Arial", 10));
        fieldErrorLabel.setVisible(false);

        VBox fieldContainer = new VBox(3, editMode, fieldErrorLabel);


        alterLink.setOnAction(e -> {
            viewMode.setVisible(false);
            fieldContainer.setVisible(true);
            editMode.setVisible(true);
            fieldErrorLabel.setVisible(false);
            if (editField instanceof TextField && !isPassword) {
                ((TextField) editField).setText(displayLabel.getText());
            } else if (editField instanceof DatePicker) {
                try {
                    if (!displayLabel.getText().equals(promptText) && !displayLabel.getText().isEmpty()) {
                        ((DatePicker) editField).setValue(LocalDate.parse(displayLabel.getText(), appDateFormatter));
                    } else {
                        ((DatePicker) editField).setValue(null);
                    }
                } catch (DateTimeParseException ex) {
                    ((DatePicker) editField).setValue(null);
                }
            }
        });

        cancelButton.setOnAction(e -> {
            viewMode.setVisible(true);
            fieldContainer.setVisible(false);
            editMode.setVisible(false);
        });

        saveButton.setOnAction(e -> {
            fieldErrorLabel.setVisible(false);
            if (sController != null) {
                String newValue = "";
                LocalDate newDate = null;

                if (editField instanceof PasswordField) {
                    newValue = ((PasswordField) editField).getText();
                } else if (editField instanceof TextField) {
                    newValue = ((TextField) editField).getText();
                } else if (editField instanceof DatePicker) {
                    newDate = ((DatePicker) editField).getValue();
                }
/*
                // Chamar o método apropriado do sController
                switch (fieldKey) {
                    case "name": sController.handleUpdateName(newValue); break;
                    case "email": sController.handleUpdateEmail(newValue); break; // Se email se tornar editável
                    case "phone": sController.handleUpdatePhone(newValue); break;
                    case "password": sController.handleUpdatePassword(newValue); break;
                    case "city": sController.handleUpdateCity(newValue); break;
                    case "dateOfBirth": sController.handleUpdateDateOfBirth(newDate); break;
                }

 */
            }
        });

        stack.getChildren().addAll(viewMode, fieldContainer);
        GridPane.setHgrow(stack, Priority.ALWAYS);
        return stack;
    }

    /**
     *
     * Métodos getters e setters
     */
    public Button getBtnInicio() {return btnInicio;}
    public Button getBtnMeusEventos() {return btnMeusEventos;}
    public Button getBtnConfiguracoes() {return btnConfiguracoes;}
    public Button getBtnSair() {return btnSair;}
    public Button getBtnProgramacao() {return btnProgramacao;}
    public Button getBtnDeleteAccount() {return btnDeleteAccount;}

    public Label getLbNomeUsuarioHeader() {return lbNomeUsuarioHeader;}
    public void setLbNomeUsuarioHeader(Label lbNomeUsuarioHeader) {this.lbNomeUsuarioHeader = lbNomeUsuarioHeader;}

    public Circle getAvatarHeader() {return avatarHeader;}
    public void setAvatarHeader(Circle avatarHeader) {this.avatarHeader = avatarHeader;}

    public List<CheckBox> getThemeCheckBoxes() {return themeCheckBoxes;}
    public void setThemeCheckBoxes(List<CheckBox> themeCheckBoxes) {this.themeCheckBoxes = themeCheckBoxes;}
}