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
 * @author Gabriella Tavares Costa Corrêa (Documentação e revisão da classe)
 * @since 2025-05-14
 */
public class RegisterView extends BorderPane {
    private TextField fldNome;
    private TextField fldEmail;
    private PasswordField fldSenha;
    private DatePicker fldDataNascimento;
    private TextField fldCidade;

    private Label lbNameErro;
    private Label lbEmailErro;
    private Label lbSenhaErro;
    private Label dobErrorLabel;
    private Label lbCidadeErro;
    private Label lbTemasErros;
    private Label lbErroGeral;

    private Label lbRegraNome;
    private Label lbIntroRegra;
    private Label lbRegraEspecial;
    private Label lbRegraDigito;
    private Label lbRegraLetra;

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
        setupUIRegisterView();
    }

    public void setRegisterController(RegisterController registerController) {this.registerController = registerController;}

    /**
     * Configura a interface da tela de registro, define o
     * plano de fundo, chama o método de criação do painel direito com seleção de temas,
     * o do formulário central de registro e os de validações de interface dinâmicas.
     */
    private void setupUIRegisterView() {
        setupBackgroundRegister();

        StackPane paneDireitoRegister = criarBasePainelDireito();
        VBox conteudoPainelDireito = criarConteudoPainelDireito();
        paneDireitoRegister.getChildren().add(conteudoPainelDireito);

        VBox formularioRegistro = criarFormularioRegistro();

        setRight(paneDireitoRegister);
        setCenter(formularioRegistro);

        configurarValidacaoDinamica();
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
        boxSelecaoTema.setMaxHeight(580);
        boxSelecaoTema.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        Text tituloTemas = new Text("Selecione temas de eventos\nque te interessam:");
        tituloTemas.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tituloTemas.setFill(Color.WHITE);

        HBox titleBox = new HBox(tituloTemas);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0,0,30,0));

        cbCorporativo = new CheckBox("Corporativo");
        cbCorporativo.setPrefHeight(25);

        cbBeneficente = new CheckBox("Beneficente");
        cbBeneficente.setPrefHeight(25);

        cbEducacional = new CheckBox("Educacional");
        cbEducacional.setPrefHeight(25);

        cbCultural = new CheckBox("Cultural");
        cbCultural.setPrefHeight(25);

        cbEsportivo = new CheckBox("Esportivo");
        cbEsportivo.setPrefHeight(25);

        cbReligioso = new CheckBox("Religioso");
        cbReligioso.setPrefHeight(25);

        cbSocial = new CheckBox("Social");
        cbSocial.setPrefHeight(25);

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

        lbTemasErros = new Label();
        lbTemasErros.setTextFill(Color.SALMON);
        lbTemasErros.setVisible(false);

        boxSelecaoTema.getChildren().addAll(
                tituloTemas,
                cbCorporativo, cbBeneficente, cbEducacional,
                cbCultural, cbEsportivo, cbReligioso, cbSocial,
                lbTemasErros
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
        formBox.alignmentProperty().set(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(0, 0, 0, 0));
        formBox.setMaxWidth(580);
        formBox.setMaxHeight(580);
        formBox.setStyle("-fx-border-color: green; -fx-border-width: 2px;");

        Text titulo = new Text("Criar Nova Conta");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titulo.setFill(Color.WHITE);
        HBox titleBox = new HBox(titulo);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0,0,20,0));

        fldNome = new TextField();
        fldNome.setPromptText("Nome Completo");
        fldNome.setPrefHeight(40);
        fldNome.getStyleClass().add("form-field");

        lbRegraNome = new Label("* O nome deve conter pelo menos nome e sobrenome.");
        lbRegraNome.setTextFill(Color.LIGHTGRAY);

        lbNameErro = new Label();
        lbNameErro.getStyleClass().add("error-label");
        lbNameErro.setTextFill(Color.SALMON);
        lbNameErro.setVisible(false);
        VBox nameBox = new VBox(3, fldNome, lbRegraNome, lbNameErro);

        fldEmail = new TextField();
        fldEmail.setPromptText("E-mail");
        fldEmail.setPrefHeight(40);
        fldEmail.getStyleClass().add("form-field");

        lbEmailErro = new Label();
        lbEmailErro.getStyleClass().add("error-label");
        lbEmailErro.setTextFill(Color.SALMON);
        lbEmailErro.setVisible(false);
        VBox emailBox = new VBox(3, fldEmail, lbEmailErro);

        fldSenha = new PasswordField();
        fldSenha.setPromptText("Senha");
        fldSenha.setPrefHeight(40);
        fldSenha.getStyleClass().add("form-field");

        lbIntroRegra = new Label("* A senha deve conter, no mínimo:");
        lbIntroRegra.setTextFill(Color.LIGHTGRAY);
        lbRegraEspecial = new Label("- 1 caractere especial");
        lbRegraEspecial.setTextFill(Color.LIGHTGRAY);
        lbRegraDigito = new Label("- 1 dígito");
        lbRegraDigito.setTextFill(Color.LIGHTGRAY);
        lbRegraLetra = new Label("- 1 letra");
        lbRegraLetra.setTextFill(Color.LIGHTGRAY);

        VBox passwordRulesBox = new VBox(1, lbIntroRegra, lbRegraEspecial, lbRegraDigito, lbRegraLetra);
        passwordRulesBox.setPadding(new Insets(2,0,2,0));

        lbSenhaErro = new Label();
        lbSenhaErro.getStyleClass().add("error-label");
        lbSenhaErro.setTextFill(Color.SALMON);
        lbSenhaErro.setVisible(false);
        VBox passwordBox = new VBox(3, fldSenha, passwordRulesBox, lbSenhaErro);

        fldDataNascimento = new DatePicker();
        fldDataNascimento.setPromptText("Data de Nascimento");
        fldDataNascimento.setPrefHeight(40);
        fldDataNascimento.getStyleClass().add("form-field");

        dobErrorLabel = new Label();
        dobErrorLabel.getStyleClass().add("error-label");
        dobErrorLabel.setTextFill(Color.SALMON);
        dobErrorLabel.setVisible(false);
        VBox dobBox = new VBox(3, fldDataNascimento, dobErrorLabel);

        fldCidade = new TextField();
        fldCidade.setPromptText("Cidade");
        fldCidade.setPrefHeight(40);
        fldCidade.getStyleClass().add("form-field");

        lbCidadeErro = new Label();
        lbCidadeErro.getStyleClass().add("error-label");
        lbCidadeErro.setTextFill(Color.SALMON);
        lbCidadeErro.setVisible(false);
        VBox cityBox = new VBox(3, fldCidade, lbCidadeErro);

        btnRegistrar = new Button("Registrar");
        btnRegistrar.setPrefHeight(40);
        btnRegistrar.setPrefWidth(400);
        btnRegistrar.getStyleClass().add("register-button");

        voltarLoginLink = new Hyperlink("Já tem uma conta? Faça login");
        voltarLoginLink.setTextFill(Color.rgb(221,1,245));
        HBox backLinkBox = new HBox(voltarLoginLink);
        backLinkBox.setAlignment(Pos.CENTER);
        backLinkBox.setPadding(new Insets(10,0,0,0));

        lbErroGeral = new Label();
        lbErroGeral.setTextFill(Color.SALMON);
        lbErroGeral.getStyleClass().add("error-label");
        lbErroGeral.setVisible(false);
        HBox generalErrorBox = new HBox(lbErroGeral);
        generalErrorBox.setAlignment(Pos.CENTER);

        formBox.getChildren().addAll(
                titleBox, nameBox, emailBox, passwordBox, dobBox, cityBox,
                btnRegistrar, generalErrorBox, backLinkBox
        );
        return formBox;
    }

    /**
     * Neste método os campos do formulário, passam por validações que atualizam cores dos rótulos
     * visuais, chamando o método {@code atualizarRegraSenhaUI()} e esconde mensagens de erro
     * conforme o usuário digita ou altera valores na interface.
     */
    private void configurarValidacaoDinamica() {
        fldNome.textProperty().addListener((obs, oldVal, newVal) -> {
            if (registerController.conferirNome(newVal)) {
                lbRegraNome.setTextFill(Color.LIGHTGREEN);
            } else {
                lbRegraNome.setTextFill(Color.LIGHTGRAY);
            }
            lbNameErro.setVisible(false);
            lbErroGeral.setVisible(false);
        });

        fldSenha.textProperty().addListener((obs, oldVal, newVal) -> {
            Map<String, Boolean> rulesStatus = registerController.conferirSenha(newVal);
            atualizarRegraSenhaUI(lbRegraEspecial, rulesStatus.getOrDefault("hasSpecial", false));
            atualizarRegraSenhaUI(lbRegraDigito, rulesStatus.getOrDefault("hasDigit", false));
            atualizarRegraSenhaUI(lbRegraLetra, rulesStatus.getOrDefault("hasLetter", false));
            lbSenhaErro.setVisible(false);
            lbErroGeral.setVisible(false);
        });

        fldEmail.textProperty().addListener( (obs,ov,nv) -> { lbEmailErro.setVisible(false); lbErroGeral.setVisible(false); });
        fldDataNascimento.valueProperty().addListener( (obs, ov, nv) -> { dobErrorLabel.setVisible(false); lbErroGeral.setVisible(false); });
        fldCidade.textProperty().addListener( (obs,ov,nv) -> { lbCidadeErro.setVisible(false); lbErroGeral.setVisible(false); });

        List.of(cbCorporativo, cbBeneficente, cbEducacional, cbCultural, cbEsportivo, cbReligioso, cbSocial).forEach(cb ->
                cb.selectedProperty().addListener((obs, ov, nv) -> {
                    lbTemasErros.setVisible(false);
                    lbErroGeral.setVisible(false);
                })
        );
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

    public Label getLbErroGeral() {return lbErroGeral;}
    public void setLbErroGeral(Label lbErroGeral) {this.lbErroGeral = lbErroGeral;}

    public Hyperlink getVoltarLoginLink() {return voltarLoginLink;}
    public Button getBtnRegistrar() {return btnRegistrar;}
}