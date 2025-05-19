package com.eventually.view;

import com.eventually.controller.SettingsController;
import com.eventually.controller.UserController; // Para navegação de volta ao login
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
 */
public class SettingsView extends BorderPane {

    private SettingsController controller;

    // Componentes da estrutura principal (similares a UserScheduleView)
    private Button btnInicio, btnMeusEventos, btnConfiguracoesSidebar;
    private Button btnProgramacao, btnAgenda, btnNovoEventoHeader;
    private Label lbNomeUsuarioHeader;
    private Circle avatarHeader;

    // Componentes específicos de Configurações
    // Preferências de Conteúdo
    private List<CheckBox> themeCheckBoxes = new ArrayList<>();
    private Button btnSaveChangesContentPrefs;

    // Preferências de Usuário
    private Label nameDisplay, emailDisplay, phoneDisplay, passwordDisplay, cityDisplay, dobDisplay;
    private ImageView profilePhotoView;
    private Label profilePhotoErrorLabel;

    private Button btnDeleteAccount;
    private final DateTimeFormatter appDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor da SettingsView.
     */
    public SettingsView() {
        this.getStyleClass().add("settings-view"); // Para CSS específico se necessário
        // O controller será setado externamente
    }

    /**
     * Define o controller para esta view.
     * @param controller O controller a ser usado.
     */
    public void setController(SettingsController controller) {
        this.controller = controller;
        setupUI(); // Configura a UI após o controller estar disponível
        setupActionHandlers(); // Configura os handlers de evento
    }

    /**
     * Configura a interface gráfica principal da tela de configurações.
     */
    private void setupUI() {
        // Recria a estrutura visual similar a UserScheduleView
        VBox barraLateral = createSidebar();
        HBox barraSuperior = createTopbar();
        VBox conteudoCentral = createCenterContent(); // Conteúdo específico de configurações

        // Simula o sub-cabeçalho da UserScheduleView para consistência
        HBox subHeader = createSubHeaderControls();
        VBox mainContentWrapper = new VBox(conteudoCentral); // Envolve o conteúdo específico
        VBox.setVgrow(mainContentWrapper, Priority.ALWAYS);

        VBox centerAreaWithSubHeader = new VBox(subHeader, mainContentWrapper);
        VBox.setVgrow(centerAreaWithSubHeader, Priority.ALWAYS);

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(centerAreaWithSubHeader);

        // Carrega o CSS (assumindo /styles/styles.css)
        try {
            this.getStylesheets().add(getClass().getResource("/styles/settings-styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Erro ao carregar /styles/settings-styles.css para SettingsView: " + e.getMessage());
        }
    }

    /**
     * Este método obtém o formatador de data padrão da aplicação.
     * @return O DateTimeFormatter configurado.
     */
    public DateTimeFormatter getAppDateFormatter() {
        return appDateFormatter;
    }

    // --- Métodos para recriar a estrutura da UserScheduleView ---
    // Estes métodos são simplificações/cópias adaptadas de UserScheduleView
    // Idealmente, UserScheduleView seria refatorada para ter um MainLayoutView reutilizável.

    /**
     * Este método cria a barra lateral de navegação.
     * @return A VBox da barra lateral.
     */
    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(200);
        sidebar.setAlignment(Pos.TOP_CENTER);

        btnInicio = new Button("Página inicial");
        btnMeusEventos = new Button("Meus eventos");
        btnConfiguracoesSidebar = new Button("Configurações");
        Button btnSair = new Button("Sair"); // Adicionado para exemplo

        btnInicio.getStyleClass().add("menu-button");
        btnMeusEventos.getStyleClass().add("menu-button");
        btnConfiguracoesSidebar.getStyleClass().add("menu-button");
        btnConfiguracoesSidebar.setStyle("-fx-background-color: #D54BD9;"); // Destaca botão atual
        btnSair.getStyleClass().add("menu-button");


        btnInicio.setMaxWidth(Double.MAX_VALUE);
        btnMeusEventos.setMaxWidth(Double.MAX_VALUE);
        btnConfiguracoesSidebar.setMaxWidth(Double.MAX_VALUE);
        btnSair.setMaxWidth(Double.MAX_VALUE);


        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(btnInicio, btnMeusEventos, btnConfiguracoesSidebar, spacer, btnSair);
        return sidebar;
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

        btnProgramacao = new Button("Programação");
        btnAgenda = new Button("Minha agenda");
        btnNovoEventoHeader = new Button("+ Novo Evento");

        lbNomeUsuarioHeader = new Label("Usuário Exemplo"); // Dados viriam do controller
        lbNomeUsuarioHeader.getStyleClass().add("user-display-label");

        avatarHeader = new Circle(18);
        avatarHeader.getStyleClass().add("avatar-circle");
        avatarHeader.setFill(Color.LIGHTGRAY); // Placeholder

        HBox userDisplayBox = new HBox(8, lbNomeUsuarioHeader, avatarHeader);
        userDisplayBox.setAlignment(Pos.CENTER);
        userDisplayBox.getStyleClass().add("user-display-box");

        btnProgramacao.getStyleClass().add("top-button");
        btnAgenda.getStyleClass().add("top-button");
        btnNovoEventoHeader.getStyleClass().add("new-event-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        subHeader.getChildren().addAll(btnProgramacao, btnAgenda, btnNovoEventoHeader, spacer, userDisplayBox);
        return subHeader;
    }


    // --- Conteúdo Específico de Configurações ---
    /**
     * Este método cria o container central da tela de configurações.
     * @return A VBox com o conteúdo de configurações.
     */
    private VBox createCenterContent() {
        VBox centerContent = new VBox(30);
        centerContent.setPadding(new Insets(20));
        centerContent.getStyleClass().add("center-content-area"); // Reutiliza estilo se aplicável
        centerContent.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Configurações");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.getStyleClass().add("settings-title"); // Estilizar se necessário

        VBox contentPrefsSection = createContentPreferencesSection();
        VBox userPrefsSection = createUserPreferencesSection();

        btnDeleteAccount = new Button("Excluir conta");
        btnDeleteAccount.getStyleClass().add("delete-account-button"); // Estilo específico no CSS
        // Exemplo de estilo direto se não tiver no CSS (idealmente no CSS):
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
        // section.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5;"); // Estilo exemplo

        Label title = new Label("Preferências de conteúdo");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label description = new Label("(Remover eventos visualizados na página inicial - descrição exemplo)");
        description.setFont(Font.font("Arial", 12));
        description.setTextFill(Color.GRAY);

        GridPane themesGrid = new GridPane();
        themesGrid.setHgap(40);
        themesGrid.setVgap(10);

        String[] themeNames = {"Corporativos", "Educacionais", "Culturais", "Esportivos", "Beneficentes", "Religiosos", "Socials"};
        String[] themeDescriptions = {
                "(palestras, workshops, feiras de negócios)", "(palestras, seminários, cursos)",
                "(shows, exposições, festivais)", "(competições, maratonas, torneios)",
                "(arrecadação de fundos, campanhas sociais)", "(cultos, retiros, encontros espirituais)",
                "(aniversários, casamentos, confraternizações)"
        };

        themeCheckBoxes.clear();
        int col = 0;
        int row = 0;
        for (int i = 0; i < themeNames.length; i++) {
            CheckBox cb = new CheckBox(themeNames[i]);
            Label descLabel = new Label(themeDescriptions[i]);
            descLabel.setFont(Font.font("Arial", 11));
            descLabel.setTextFill(Color.DARKSLATEGRAY);
            VBox themeEntry = new VBox(2, cb, descLabel);
            themeCheckBoxes.add(cb);
            themesGrid.add(themeEntry, col, row);
            col++;
            if (col > 0 && (i+1) < themeNames.length) { // Ajuste para 2 colunas, exceto talvez o último
                col = 0;
                row++;
            } else if (col > 1) { // Garante no máximo 2 colunas se for ímpar
                col = 0;
                row++;
            }
        }
        // Ajuste manual se a grade não ficou como esperado para 7 itens em 2 colunas
        // (A lógica acima tenta colocar 2 por linha, o último pode ficar sozinho ou ajustar)


        btnSaveChangesContentPrefs = new Button("Salvar Preferências de Conteúdo");
        btnSaveChangesContentPrefs.getStyleClass().add("save-prefs-button"); // Estilizar no CSS
        HBox buttonBox = new HBox(btnSaveChangesContentPrefs);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(10,0,0,0));

        section.getChildren().addAll(title, description, themesGrid, buttonBox);
        return section;
    }

    /**
     * Este método cria a seção de preferências de usuário.
     * @return A VBox da seção de preferências de usuário.
     */
    private VBox createUserPreferencesSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(10));
        // section.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5;"); // Estilo exemplo

        Label title = new Label("Preferências de usuário");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label description = new Label("(Edição de informações do usuário - descrição exemplo)");
        description.setFont(Font.font("Arial", 12));
        description.setTextFill(Color.GRAY);

        GridPane userDetailsGrid = new GridPane();
        userDetailsGrid.setHgap(10);
        userDetailsGrid.setVgap(10);
        userDetailsGrid.setPadding(new Insets(10, 0, 10, 0));

        // Nome
        nameDisplay = new Label();
        StackPane nameFieldContainer = createEditableField("name", "Nome:", nameDisplay, "Exemplo Nome");
        userDetailsGrid.add(new Label("Nome:"), 0, 0);
        userDetailsGrid.add(nameFieldContainer, 1, 0);

        // Email (geralmente não editável ou com processo de verificação)
        emailDisplay = new Label();
        Label emailLabelForDisplay = new Label("Email:");
        // StackPane emailFieldContainer = createEditableField("email", "Email:", emailDisplay, "exemplo@email.com", false); // Exemplo não editável
        // Para o exemplo da imagem, o email não tem "alterar" explícito, então exibimos como Label.
        // Se precisar ser editável, usar createEditableField.
        userDetailsGrid.add(emailLabelForDisplay, 0, 1);
        userDetailsGrid.add(emailDisplay, 1, 1);


        // Telefone
        phoneDisplay = new Label();
        StackPane phoneFieldContainer = createEditableField("phone", "Telefone:", phoneDisplay, "(xx) xxxxx-xxxx");
        userDetailsGrid.add(new Label("Telefone:"), 0, 2);
        userDetailsGrid.add(phoneFieldContainer, 1, 2);

        // Senha
        passwordDisplay = new Label("********");
        StackPane passwordFieldContainer = createEditableField("password", "Senha:", passwordDisplay, "", true); // isPassword = true
        userDetailsGrid.add(new Label("Senha:"), 0, 3);
        userDetailsGrid.add(passwordFieldContainer, 1, 3);

        // Cidade
        cityDisplay = new Label();
        StackPane cityFieldContainer = createEditableField("city", "Cidade:", cityDisplay, "Cidade Exemplo");
        userDetailsGrid.add(new Label("Cidade:"), 0, 4);
        userDetailsGrid.add(cityFieldContainer, 1, 4);

        // Data de Nascimento
        dobDisplay = new Label();
        StackPane dobFieldContainer = createEditableField("dateOfBirth", "Data de Nasc.:", dobDisplay, "dd/mm/yyyy");
        userDetailsGrid.add(new Label("Data de Nascimento:"), 0, 5);
        userDetailsGrid.add(dobFieldContainer, 1, 5);

        // Foto de Perfil
        VBox photoSection = createProfilePhotoSection();

        // Layout Principal de Preferências de Usuário
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
        // Placeholder visual para a foto
        StackPane photoPlaceholder = new StackPane(profilePhotoView);
        photoPlaceholder.setPrefSize(100,100);
        photoPlaceholder.setMinSize(100,100);
        photoPlaceholder.setMaxSize(100,100);

        photoPlaceholder.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-background-color: #f0f0f0;");
        // Carregar imagem default ou do usuário aqui se houver
        // try {
        //     Image defaultImg = new Image(getClass().getResourceAsStream("/images/default-avatar.png"));
        //     profilePhotoView.setImage(defaultImg);
        // } catch (Exception e) {
        //     System.err.println("Erro ao carregar avatar padrão: " + e.getMessage());
        //     // Se não houver imagem, o placeholder já tem um fundo
        // }


        Label photoInfo = new Label("A imagem anexada deve ter\nXX x XX pixels (YY MB)");
        photoInfo.setFont(Font.font("Arial", 10));
        photoInfo.setTextFill(Color.GRAY);
        photoInfo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);


        Hyperlink changePhotoLink = new Hyperlink("(alterar)");
        changePhotoLink.setOnAction(e -> controller.handleChangeProfilePhoto()); // A view notifica o controller

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

        // Modo de Visualização
        HBox viewMode = new HBox(10);
        viewMode.setAlignment(Pos.CENTER_LEFT);
        Hyperlink alterLink = new Hyperlink("(alterar)");
        viewMode.getChildren().addAll(displayLabel, alterLink);

        // Modo de Edição
        HBox editMode = new HBox(5);
        editMode.setAlignment(Pos.CENTER_LEFT);
        Node editField; // TextField, PasswordField ou DatePicker

        if ("dateOfBirth".equals(fieldKey)) {
            DatePicker picker = new DatePicker();
            picker.setPromptText(promptText);
            picker.setPrefWidth(150);
            editField = picker;
        } else if (isPassword) {
            PasswordField passField = new PasswordField();
            passField.setPromptText("Nova Senha"); // Prompt específico para senha
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
        editMode.setVisible(false); // Começa invisível

        Label fieldErrorLabel = new Label(); // Label de erro específico para este campo
        fieldErrorLabel.setTextFill(Color.SALMON);
        fieldErrorLabel.setFont(Font.font("Arial", 10));
        fieldErrorLabel.setVisible(false);

        VBox fieldContainer = new VBox(3, editMode, fieldErrorLabel);


        alterLink.setOnAction(e -> {
            viewMode.setVisible(false);
            fieldContainer.setVisible(true);
            editMode.setVisible(true); // Garante que o editMode dentro do VBox esteja visível
            fieldErrorLabel.setVisible(false); // Limpa erro ao abrir edição
            // Preenche o campo de edição com o valor atual do displayLabel (exceto senha)
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
                    ((DatePicker) editField).setValue(null); // Se parse falhar
                }
            }
        });

        cancelButton.setOnAction(e -> {
            viewMode.setVisible(true);
            fieldContainer.setVisible(false);
            editMode.setVisible(false); // Esconde modo de edição
        });

        saveButton.setOnAction(e -> {
            fieldErrorLabel.setVisible(false); // Limpa erro antes de salvar
            if (controller != null) {
                String newValue = "";
                LocalDate newDate = null;

                if (editField instanceof PasswordField) {
                    newValue = ((PasswordField) editField).getText();
                } else if (editField instanceof TextField) {
                    newValue = ((TextField) editField).getText();
                } else if (editField instanceof DatePicker) {
                    newDate = ((DatePicker) editField).getValue();
                }

                // Chamar o método apropriado do controller
                switch (fieldKey) {
                    case "name": controller.handleUpdateName(newValue); break;
                    case "email": controller.handleUpdateEmail(newValue); break; // Se email se tornar editável
                    case "phone": controller.handleUpdatePhone(newValue); break;
                    case "password": controller.handleUpdatePassword(newValue); break;
                    case "city": controller.handleUpdateCity(newValue); break;
                    case "dateOfBirth": controller.handleUpdateDateOfBirth(newDate); break;
                }
            }
            // A lógica de reverter para viewMode (ou manter editMode se houver erro)
            // será tratada pelo controller chamando updateDisplayField ou showFieldError
        });

        stack.getChildren().addAll(viewMode, fieldContainer);
        GridPane.setHgrow(stack, Priority.ALWAYS); // Para o StackPane preencher o espaço na grade
        return stack;
    }

    // --- Métodos para atualizar a UI a partir do Controller ---
    /**
     * Este método exibe os dados do usuário na interface.
     * @param userData Mapa contendo os dados do usuário.
     */
    public void displayUserData(Map<String, Object> userData) {
        nameDisplay.setText((String) userData.getOrDefault("name", "N/A"));
        emailDisplay.setText((String) userData.getOrDefault("email", "N/A"));
        phoneDisplay.setText((String) userData.getOrDefault("phone", "N/A"));
        passwordDisplay.setText("********"); // Sempre mostrar asteriscos
        cityDisplay.setText((String) userData.getOrDefault("city", "N/A"));
        LocalDate dob = (LocalDate) userData.get("dateOfBirth");
        dobDisplay.setText(dob != null ? dob.format(appDateFormatter) : "N/A");

        // Atualiza nome do usuário no header também (se existir)
        if (lbNomeUsuarioHeader != null) {
            lbNomeUsuarioHeader.setText((String) userData.getOrDefault("name", "Usuário"));
        }

        // Lógica para foto de perfil (exemplo)
        String photoPath = (String) userData.get("profilePhotoPath");
        if (photoPath != null && !photoPath.isEmpty()) {
            try {
                profilePhotoView.setImage(new Image(new File(photoPath).toURI().toString()));
            } catch (Exception e) {
                System.err.println("Erro ao carregar foto de perfil: " + photoPath + " - " + e.getMessage());
                // profilePhotoView.setImage(null); // ou imagem padrão
            }
        } else {
            // profilePhotoView.setImage(null); // ou imagem padrão
        }
    }

    /**
     * Este método exibe as preferências de conteúdo (temas selecionados).
     * @param preferences Lista de temas preferidos.
     */
    public void displayContentPreferences(List<String> preferences) {
        for (CheckBox cb : themeCheckBoxes) {
            cb.setSelected(preferences.contains(cb.getText()));
        }
    }

    /**
     * Este método atualiza um campo de exibição após uma edição bem-sucedida.
     * Também reverte o campo para o modo de visualização.
     * @param fieldKey A chave do campo (ex: "name", "phone").
     * @param newValue O novo valor a ser exibido.
     */
    public void updateDisplayField(String fieldKey, String newValue) {
        Label displayLabel = null;
        switch (fieldKey) {
            case "name": displayLabel = nameDisplay; break;
            case "email": displayLabel = emailDisplay; break;
            case "phone": displayLabel = phoneDisplay; break;
            case "password": displayLabel = passwordDisplay; newValue = "********"; break; // Sempre mostra asteriscos
            case "city": displayLabel = cityDisplay; break;
            case "dateOfBirth": displayLabel = dobDisplay; break; // newValue já formatado pelo controller
            case "profilePhotoPath":
                // A view precisa decidir como exibir a foto, aqui apenas um exemplo
                System.out.println("View: Foto de perfil atualizada para " + newValue);
                if (newValue != null && !newValue.isEmpty()) {
                    try { profilePhotoView.setImage(new Image(new File(newValue).toURI().toString())); }
                    catch (Exception e) { System.err.println("Erro ao recarregar foto: " + e.getMessage());}
                } else {
                    // profilePhotoView.setImage(null); // Limpa ou define imagem padrão
                }
                break;
        }

        if (displayLabel != null) {
            displayLabel.setText(newValue);
            // Reverte para modo de visualização
            Node fieldContainer = displayLabel.getParent().getParent(); // HBox -> StackPane
            if (fieldContainer instanceof StackPane) {
                Node viewMode = ((StackPane) fieldContainer).getChildren().get(0);
                Node editVBox = ((StackPane) fieldContainer).getChildren().get(1); // O VBox que contém editMode e fieldErrorLabel
                viewMode.setVisible(true);
                editVBox.setVisible(false);
            }
        }
        // Atualiza nome do usuário no header também se for o campo 'name'
        if ("name".equals(fieldKey) && lbNomeUsuarioHeader != null) {
            lbNomeUsuarioHeader.setText(newValue);
        }
    }

    /**
     * Este método exibe uma mensagem de erro específica para um campo.
     * Mantém o campo no modo de edição.
     * @param fieldKey A chave do campo (ex: "name", "phone").
     * @param message A mensagem de erro.
     */
    public void showFieldError(String fieldKey, String message) {
        // Encontra o StackPane do campo
        Node fieldUIElement = null;
        switch (fieldKey) {
            case "name": fieldUIElement = nameDisplay; break;
            case "email": fieldUIElement = emailDisplay; break;
            case "phone": fieldUIElement = phoneDisplay; break;
            case "password": fieldUIElement = passwordDisplay; break;
            case "city": fieldUIElement = cityDisplay; break;
            case "dateOfBirth": fieldUIElement = dobDisplay; break;
            case "profilePhotoPath": fieldUIElement = profilePhotoErrorLabel; break; // Usa o label de erro da foto
        }

        if (fieldUIElement == profilePhotoErrorLabel) {
            profilePhotoErrorLabel.setText(message);
            profilePhotoErrorLabel.setVisible(true);
        } else if (fieldUIElement instanceof Label) {
            Node container = ((Label)fieldUIElement).getParent().getParent(); // Label -> HBox(viewMode) -> StackPane
            if (container instanceof StackPane) {
                StackPane stack = (StackPane) container;
                if (stack.getChildren().size() > 1 && stack.getChildren().get(1) instanceof VBox) {
                    VBox editVBox = (VBox) stack.getChildren().get(1); // O VBox que contém editMode e fieldErrorLabel
                    if (editVBox.getChildren().size() > 1 && editVBox.getChildren().get(1) instanceof Label) {
                        Label errorLabel = (Label) editVBox.getChildren().get(1);
                        errorLabel.setText(message);
                        errorLabel.setVisible(true);
                        // Mantém no modo de edição
                        stack.getChildren().get(0).setVisible(false); // viewMode
                        editVBox.setVisible(true);    // editVBox (que contém editMode e errorLabel)
                        if (editVBox.getChildren().get(0) instanceof HBox) { // Garante que o editMode (HBox) esteja visível dentro do VBox
                            editVBox.getChildren().get(0).setVisible(true);
                        }
                    }
                }
            }
        }
        // Poderia mostrar um Alert genérico também ou um local para erros não associados a campos.
        showErrorMessage("Verifique o campo: " + message); // Reutiliza showErrorMessage para um alerta rápido
    }


    /**
     * Este método exibe uma mensagem de erro genérica (ex: Alert).
     * @param message A mensagem de erro.
     */
    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Configuração");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Este método exibe uma mensagem de sucesso (ex: Alert).
     * @param message A mensagem de sucesso.
     */
    public void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Este método exibe um diálogo de confirmação para exclusão de conta.
     * @return true se o usuário confirmar, false caso contrário.
     */
    public boolean confirmAccountDeletion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão de Conta");
        alert.setHeaderText("Tem certeza que deseja excluir sua conta?");
        alert.setContentText("Esta ação é irreversível e todos os seus dados serão perdidos.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Este método configura os manipuladores de ação para os botões da view.
     */
    private void setupActionHandlers() {
        if (controller == null) return;

        // Sidebar
        btnInicio.setOnAction(e -> controller.navigateToHome());
        btnMeusEventos.setOnAction(e -> controller.navigateToMyEvents());
        // btnConfiguracoesSidebar já é a tela atual, pode não ter ação ou recarregar
        btnConfiguracoesSidebar.setOnAction(e -> controller.loadInitialData()); // Ex: Recarrega dados

        // SubHeader (se forem diferentes da sidebar)
        btnProgramacao.setOnAction(e -> controller.navigateToProgramacao());
        btnAgenda.setOnAction(e -> controller.navigateToMinhaAgenda());
        btnNovoEventoHeader.setOnAction(e -> controller.navigateToNovoEvento());


        // Ações específicas da tela de Configurações
        btnSaveChangesContentPrefs.setOnAction(e -> {
            List<String> selected = new ArrayList<>();
            for (CheckBox cb : themeCheckBoxes) {
                if (cb.isSelected()) {
                    selected.add(cb.getText());
                }
            }
            controller.handleUpdateContentPreferences(selected);
        });

        btnDeleteAccount.setOnAction(e -> controller.handleDeleteAccount());
    }

    /**
     * Este método navega para a tela de login.
     * (Chamado pelo controller após exclusão de conta, por exemplo)
     */
    public void navigateToLoginScreen() {
        try {
            Stage currentStage = (Stage) this.getScene().getWindow();
            UserController userController = new UserController(); // Assume construtor padrão
            LoginView loginView = new LoginView(userController);
            Scene loginScene = new Scene(loginView, 1280, 720); // Dimensões da tela de login
            try {
                loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("Erro ao carregar CSS para LoginView ao retornar: " + e.getMessage());
            }
            currentStage.setScene(loginScene);
            currentStage.setTitle("Eventually - Login");
            Platform.runLater(() -> currentStage.setMaximized(true)); // Garante maximização
        } catch (Exception e) {
            System.err.println("Erro ao navegar para a tela de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Este método navega para a tela principal/Home (UserScheduleView).
     */
    public void navigateToHome() {
        try {
            Stage currentStage = (Stage) this.getScene().getWindow();
            UserScheduleView userScheduleView = new UserScheduleView();
            // O UserScheduleController é normalmente injetado ou criado aqui
            // com.eventually.controller.UserScheduleController scheduleController = new com.eventually.controller.UserScheduleController(userScheduleView);
            // userScheduleView.setController(scheduleController); // Se UserScheduleView precisar do controller imediatamente

            Scene homeScene = new Scene(userScheduleView, 1280, 720);
            try {
                homeScene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("Erro ao carregar CSS para Home: " + e.getMessage());
            }
            currentStage.setScene(homeScene);
            currentStage.setTitle("Eventually - Página Inicial");
            Platform.runLater(() -> currentStage.setMaximized(true));
        } catch (Exception e) {
            System.err.println("Erro ao navegar para Home: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Este método navega para a tela de "Meus Eventos".
     * (Pode ser a mesma UserScheduleView com um filtro ou uma view diferente)
     */
    public void navigateToMyEvents() {
        // Por enquanto, redireciona para a Home como exemplo
        System.out.println("View: Navegando para Meus Eventos (implementação pendente, usando Home).");
        navigateToHome();
    }


}