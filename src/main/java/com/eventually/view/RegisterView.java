package com.eventually.view;

import com.eventually.controller.RegisterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Classe da tela de registro.
 * Esta classe é responsável
 * por cadastrar o usuário na memória.
 *
 * @author Yuri Garcia Maia
 * @version 1.01
 * @since 2025-05-13
 * @author Gabriella Tavares Costa Corrêa (Documentação e revisão da classe)
 * @since 2025-05-14
 */
public class RegisterView extends BorderPane {
    private TextField fldNome;
    private TextField fldEmail;
    private PasswordField fldSenha;
    private DatePicker fldDataNascimento;
    private TextField fldCidade;

    private Label lbRegraNome;
    private Label lbRegraEmail;
    private Label lbIntroRegra;
    private Label lbRegraEspecial;
    private Label lbRegraDigito;
    private Label lbRegraLetra;
    private Label lbRegraTamanho;
    private Label lbRegraData;
    private Label lbRegraCidade;

    private Label lbRegraTema;
    private CheckBox cbCorporativo;
    private CheckBox cbBeneficente;
    private CheckBox cbEducacional;
    private CheckBox cbCultural;
    private CheckBox cbEsportivo;
    private CheckBox cbReligioso;
    private CheckBox cbSocial;

    private Button btnRegistrar;
    private Hyperlink voltarLoginLink;

    private RegisterController registerController;

    public RegisterView() {
        setupUI();
    }

    public void setRegisterController(RegisterController registerController) {this.registerController = registerController;}

    /**
     * Configura a interface da tela de registro dividida em duas áreas, define o
     * plano de fundo e chama os métodos de validações de interface dinâmicas.
     */
    private void setupUI() {
        setupBackgroundRegister();

        VBox formularioRegistro = criarFormularioRegistro();
        StackPane stackFormulario = new StackPane(formularioRegistro);
        stackFormulario.setAlignment(Pos.CENTER);
        stackFormulario.setMaxWidth(Double.MAX_VALUE);
        StackPane.setMargin(formularioRegistro, new Insets(-80, 20, 0, 20));

        StackPane paneDireitoRegister = criarBasePainelDireito();

        VBox conteudoPainelDireito = criarConteudoPainelDireito();
        paneDireitoRegister.getChildren().add(conteudoPainelDireito);
        paneDireitoRegister.setAlignment(Pos.CENTER_RIGHT);
        StackPane.setMargin(conteudoPainelDireito, new Insets(-60, 20, 0, 20));
        HBox layoutPrincipal = new HBox();
        layoutPrincipal.setPrefSize(1200, 900);
        layoutPrincipal.getChildren().addAll(stackFormulario, paneDireitoRegister);

        HBox.setHgrow(stackFormulario, Priority.ALWAYS);
        HBox.setHgrow(paneDireitoRegister, Priority.ALWAYS);

        setCenter(layoutPrincipal);
    }

    /**
     * Configura o plano de fundo da tela de registro.
     */
    private void setupBackgroundRegister() {
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
            System.err.println("RegisterView: Erro ao carregar imagem de fundo: " + e.getMessage());
        }
    }

    /**
     * Cria o PAINEL direito com formato trapezoidal e gradiente de cor,
     * é a base visual do lado direito da tela.
     * @return o painel configurado.
     */
    private StackPane criarBasePainelDireito() {
        StackPane paneDireitoRegistro = new StackPane();
        paneDireitoRegistro.setPrefWidth(300);
        paneDireitoRegistro.setPrefHeight(900);
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
        paneDireitoRegistro.getChildren().add(trapezoid);
        return paneDireitoRegistro;
    }

    /**
     * Cria o CONTEÚDO do painel direito com os checkboxes de seleção de temas.
     * @return o título e a lista de seleção de temas dentro de uma VBox.
     */
    private VBox criarConteudoPainelDireito() {
        VBox boxSelecaoTema = new VBox(15);
        boxSelecaoTema.alignmentProperty().set(Pos.CENTER_LEFT);
        boxSelecaoTema.setAlignment(Pos.CENTER_LEFT);
        boxSelecaoTema.setPadding(new Insets(0, 0, 0, 0));
        boxSelecaoTema.setMaxWidth(580);
        boxSelecaoTema.setMaxHeight(310);

        Text tituloTemas = new Text("Selecione temas de eventos\nque te interessam:");
        tituloTemas.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tituloTemas.setFill(Color.WHITE);

        HBox titleBox = new HBox(tituloTemas);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0,0,30,0));

        cbCorporativo = new CheckBox("Corporativo");
        cbCorporativo.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        cbCorporativo.setPrefHeight(25);
        cbCorporativo.getStyleClass().add("purple-checkbox");

        cbBeneficente = new CheckBox("Beneficente");
        cbBeneficente.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        cbBeneficente.setPrefHeight(25);
        cbBeneficente.getStyleClass().add("purple-checkbox");

        cbEducacional = new CheckBox("Educacional");
        cbEducacional.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        cbEducacional.setPrefHeight(25);
        cbEducacional.getStyleClass().add("purple-checkbox");

        cbCultural = new CheckBox("Cultural");
        cbCultural.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        cbCultural.setPrefHeight(25);
        cbCultural.getStyleClass().add("purple-checkbox");

        cbEsportivo = new CheckBox("Esportivo");
        cbEsportivo.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        cbEsportivo.setPrefHeight(25);
        cbEsportivo.getStyleClass().add("purple-checkbox");

        cbReligioso = new CheckBox("Religioso");
        cbReligioso.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        cbReligioso.setPrefHeight(25);
        cbReligioso.getStyleClass().add("purple-checkbox");

        cbSocial = new CheckBox("Social");
        cbSocial.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        cbSocial.setPrefHeight(25);
        cbSocial.getStyleClass().add("purple-checkbox");

        for (CheckBox cb : List.of(
                cbCorporativo,
                cbBeneficente,
                cbEducacional,
                cbCultural,
                cbEsportivo,
                cbReligioso,
                cbSocial)) {
            cb.setTextFill(Color.WHITE);
        }

        lbRegraTema = new Label("* Isso pode ser alterado depois.");
        lbRegraTema.getStyleClass().add("form-field");
        lbRegraTema.setWrapText(true);
        lbRegraTema.setPadding(new Insets(1, 0, 0, 0));

        boxSelecaoTema.getChildren().addAll(
                tituloTemas,
                cbCorporativo, cbBeneficente, cbEducacional,
                cbCultural, cbEsportivo, cbReligioso, cbSocial,lbRegraTema
        );
        return boxSelecaoTema;
    }

    /**
     * Cria o FORMULÁRIO de registro com campos de nome, email, senha,
     * data de nascimento, cidade e botão de registro.
     * @return os campos a serem preenchidos no registro dentro de uma VBox.
     */
    private VBox criarFormularioRegistro() {
        VBox formBox = new VBox(10);
        formBox.alignmentProperty().set(Pos.CENTER);
        formBox.setPadding(new Insets(0, 0, 0, 100));
        formBox.setMaxWidth(550);

        Text titulo = new Text("Criar Nova Conta");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        titulo.setFill(Color.WHITE);
        HBox titleBox = new HBox(titulo);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(4,0,4,0));

        fldNome = new TextField();
        fldNome.setPromptText("Nome Completo");
        fldNome.setPrefHeight(40);
        fldNome.getStyleClass().add("register-field");

        lbRegraNome = new Label("* O nome deve conter pelo menos nome e sobrenome.");
        lbRegraNome.getStyleClass().add("form-field");
        lbRegraNome.setWrapText(true);
        lbRegraNome.setPadding(new Insets(1, 0, 0, 0));

        fldNome.textProperty().addListener((obs, oldVal, novoValorNome) -> {
            if (registerController.conferirNome(novoValorNome)) {
                lbRegraNome.setTextFill(Color.LIGHTGREEN);
            } else {
                lbRegraNome.setTextFill(Color.LIGHTGRAY);
            }
        });

        VBox nameBox = new VBox(3, fldNome, lbRegraNome);

        fldEmail = new TextField();
        fldEmail.setPromptText("E-mail");
        fldEmail.setPrefHeight(40);
        fldEmail.getStyleClass().add("register-field");

        lbRegraEmail = new Label("* O email deve conter um domínio válido.");
        lbRegraEmail.getStyleClass().add("form-field");
        lbRegraEmail.setWrapText(true);
        lbRegraEmail.setPadding(new Insets(1, 0, 0, 0));

        fldEmail.textProperty().addListener((obs, oldVal, novoValorEmail) -> {
            if (registerController.conferirEmail(novoValorEmail)) {
                lbRegraEmail.setTextFill(Color.LIGHTGREEN);
            } else {
                lbRegraEmail.setTextFill(Color.LIGHTGRAY);
            }
        });

        VBox emailBox = new VBox(3, fldEmail, lbRegraEmail);

        fldSenha = new PasswordField();
        fldSenha.setPromptText("Senha");
        fldSenha.setPrefHeight(40);
        fldSenha.getStyleClass().add("register-field");

        lbIntroRegra = new Label("* A senha deve conter, no mínimo:");
        lbIntroRegra.getStyleClass().add("form-field");
        lbRegraEspecial = new Label("- 1 caractere especial");
        lbRegraEspecial.getStyleClass().add("form-field");
        lbRegraDigito = new Label("- 1 dígito");
        lbRegraDigito.getStyleClass().add("form-field");
        lbRegraLetra = new Label("- 1 letra");
        lbRegraLetra.getStyleClass().add("form-field");
        lbRegraTamanho = new Label("- 6 caracteres");
        lbRegraTamanho.getStyleClass().add("form-field");

        VBox passwordRulesBox = new VBox(1, lbIntroRegra, lbRegraEspecial, lbRegraDigito, lbRegraLetra, lbRegraTamanho);
        passwordRulesBox.setPadding(new Insets(1,0,1,0));

        fldSenha.textProperty().addListener((obs, oldVal, novoValorSenha) -> {
            Map<String, Boolean> rulesStatus = registerController.conferirSenha(novoValorSenha);
            atualizarRegraSenhaUI(lbRegraEspecial, rulesStatus.getOrDefault("hasSpecial", false));
            atualizarRegraSenhaUI(lbRegraDigito, rulesStatus.getOrDefault("hasDigit", false));
            atualizarRegraSenhaUI(lbRegraLetra, rulesStatus.getOrDefault("hasLetter", false));
            atualizarRegraSenhaUI(lbRegraTamanho, rulesStatus.getOrDefault("hasSixChar", false));
        });

        VBox passwordBox = new VBox(3, fldSenha, passwordRulesBox);

        fldDataNascimento = new DatePicker();
        fldDataNascimento.setPromptText("Data de Nascimento");
        fldDataNascimento.setPrefHeight(40);
        fldDataNascimento.getStyleClass().add("register-field");
        bloquearIntervaloData(fldDataNascimento,
                LocalDate.of(1960, 1, 1),
                LocalDate.of(2013, 12, 31)
        );

        lbRegraData = new Label("Informe sua data de nascimento no formato dia/mês/ano.");
        lbRegraData.getStyleClass().add("form-field");
        lbRegraData.setWrapText(true);
        lbRegraData.setPadding(new Insets(1, 0, 0, 0));

        fldDataNascimento.getEditor().setOnAction(event -> {
            try {
                String input = fldDataNascimento.getEditor().getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate parsedDate = LocalDate.parse(input, formatter);
                fldDataNascimento.setValue(parsedDate);
            } catch (DateTimeParseException e) {
                System.out.println("Erro ao interpretar data digitada manualmente: " + e.getMessage());
            }
        });

        fldDataNascimento.valueProperty().addListener((obs, oldVal,novoValorData) -> {
            if (registerController.conferirDataNasc(novoValorData)) {
                lbRegraData.setTextFill(Color.LIGHTGREEN);
            } else {
                lbRegraData.setTextFill(Color.LIGHTGRAY);
            }
        });
        fldDataNascimento.setConverter(new StringConverter<LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                return date != null ? date.format(formatter) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, formatter) : null;
            }
        });

        fldDataNascimento.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                try {
                    String input = fldDataNascimento.getEditor().getText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate parsedDate = LocalDate.parse(input, formatter);
                    fldDataNascimento.setValue(parsedDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Erro ao interpretar data ao perder foco: " + e.getMessage());
                }
            }
        });

        VBox dobBox = new VBox(2, fldDataNascimento, lbRegraData);

        fldCidade = new TextField();
        fldCidade.setPromptText("Cidade");
        fldCidade.setPrefHeight(40);
        fldCidade.getStyleClass().add("register-field");

        lbRegraCidade = new Label("Informe sua cidade para ver primeiro os eventos mais próximos de você.");
        lbRegraCidade.getStyleClass().add("form-field");
        lbRegraCidade.setWrapText(true);
        lbRegraCidade.setPadding(new Insets(1, 0, 0, 0));

        fldCidade.textProperty().addListener((obs, oldVal, novoValorCidade) -> {
            if (registerController.conferirCidade(novoValorCidade)) {
                lbRegraCidade.setTextFill(Color.LIGHTGREEN);
            } else {
                lbRegraCidade.setTextFill(Color.LIGHTGRAY);
            }
        });

        VBox cityBox = new VBox(2, fldCidade, lbRegraCidade);

        btnRegistrar = new Button("Registrar");
        btnRegistrar.setPrefHeight(40);
        btnRegistrar.setPrefWidth(400);
        btnRegistrar.getStyleClass().add("register-button");

        voltarLoginLink = new Hyperlink("Já tem uma conta? Faça login");
        voltarLoginLink.setTextFill(Color.rgb(221,1,245));
        HBox backLinkBox = new HBox(voltarLoginLink);
        backLinkBox.setAlignment(Pos.CENTER);
        backLinkBox.setPadding(new Insets(10,0,0,0));

        formBox.getChildren().addAll(
                titleBox, nameBox, emailBox, passwordBox, dobBox, cityBox,
                btnRegistrar, backLinkBox
        );

        return formBox;
    }

    /**
     * Este método bloqueia a seleção de datas do datapicker na interface
     * @param fldDataNascimento o campo de data
     * @param dataMinima data minima de nascimento (2013)
     * @param dataMaxima data maxima de nascimento (1960)
     */
    private void bloquearIntervaloData(DatePicker fldDataNascimento, LocalDate dataMinima, LocalDate dataMaxima) {
        fldDataNascimento.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(dataMinima) || item.isAfter(dataMaxima)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                } else {
                    setDisable(false);
                    setStyle("-fx-background-color: white;");
                }
            }
        });
    }

    /**
     * Este método faz a mudança de cor do texto da regra de senha com base se ela foi cumprida ou não.
     *
     * @param lbRegra o label da regra.
     * @param isCumprida true se a regra foi cumprida, false caso contrário.
     */
    private void atualizarRegraSenhaUI(Label lbRegra, boolean isCumprida) {
        if (isCumprida) {
            lbRegra.setTextFill(Color.LIGHTGREEN);
        } else {
            lbRegra.setTextFill(Color.LIGHTGRAY);
        }
    }

    /**
     * Métodos de encapsulamento getters e setter
     */
    public TextField getFldNome() {return fldNome;}
    public TextField getFldEmail() {return fldEmail;}
    public PasswordField getFldSenha() {return fldSenha;}
    public DatePicker getFldDataNascimento() {return fldDataNascimento;}
    public TextField getFldCidade() {return fldCidade;}

    public CheckBox getCbCorporativo() {return cbCorporativo;}
    public CheckBox getCbBeneficente() {return cbBeneficente;}
    public CheckBox getCbEducacional() {return cbEducacional;}
    public CheckBox getCbCultural() {return cbCultural;}
    public CheckBox getCbEsportivo() {return cbEsportivo;}
    public CheckBox getCbReligioso() {return cbReligioso;}
    public CheckBox getCbSocial() {return cbSocial;}

    public Hyperlink getVoltarLoginLink() {return voltarLoginLink;}
    public Button getBtnRegistrar() {return btnRegistrar;}
}