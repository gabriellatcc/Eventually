package com.eventually.view;

import com.eventually.controller.SettingsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para a tela de Configurações.
 * Exibe e permite a alteração das preferências do usuário e de conteúdo.
 * @author Yuri Garcia Maia (Criação)
 * @since 22-05-2025
 * @version 1.05
 * @author Gabriella Tavares Costa Corrêa (Revisão de documentação, lógica e da estrutura da classe)
 * @since 22-05-2025
 */
public class SettingsView extends BorderPane {
    private SettingsController sController;
    private BarraBuilder barraBuilder;

    private List<CheckBox> themeCheckBoxes = new ArrayList<>();

    private Label lbNomeUsuario, lbEmailUsuario, lbSenhaUsuario, lbCidadeUsuario, lbDataNascUsuario;
    private Hyperlink HlAlterarPreferencias, hlAlterarNome, hlAlterarEmail, hlAlterarSenha, hlAlterarCidade, hlAlterarDataNasc,hlAlterarFoto;
    private Button btnDeleteAccount;

    private CheckBox cbCorporativo, cbBeneficente, cbEducacional, cbCultural, cbEsportivo, cbReligioso, cbSocial;

    private ImageView avatarView;

    /**
     * Construtor da SettingsView.
     */
    public SettingsView() {
        setupUI();
    }

    /**
     * Define o controller para esta view.
     * @param sController o controller a ser usado.
     */
    public void setSettingsController(SettingsController sController) {
        this.sController = sController;
    }

    /**
     * Configura a interface gráfica principal da tela de configurações.
     */
    private void setupUI() {
        this.barraBuilder = new BarraBuilder();
        VBox barraLateral = this.barraBuilder.construirBarraLateral();

        HBox barraSuperior = this.barraBuilder.construirBarraSuperior();

        VBox conteudoCentral = criarConteudoCentral();

        VBox envoltorioConteudoPrincipal = new VBox(conteudoCentral);
        VBox.setVgrow(envoltorioConteudoPrincipal, Priority.ALWAYS);

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    /**
     * Este método cria o container central da tela de configurações.
     * @return a VBox com o conteúdo de configurações.
     */
    private VBox criarConteudoCentral() {
        VBox conteudoCentral = new VBox(10);
        conteudoCentral.setPadding(new Insets(15));
        conteudoCentral.getStyleClass().add("center-content-area");
        conteudoCentral.setAlignment(Pos.TOP_CENTER);

        Label lbTitulo1 = new Label("Configurações");
        lbTitulo1.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lbTitulo1.getStyleClass().add("settings-titulo1");
        lbTitulo1.setBorder(new Border(new BorderStroke(Color.PINK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        VBox vbSessaoPreferenciasConteudo = criarConteudoSessaoPreferencias();
        VBox vbSessaoPreferenciasUsuario = criarSessaoPreferenciasUsuario();

        btnDeleteAccount = new Button("Excluir conta");
        btnDeleteAccount.getStyleClass().add("delete-account-button");
        btnDeleteAccount.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-color: #f44336;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 5px;"
        );
        btnDeleteAccount.setPrefWidth(150);

        HBox hbBtnExclusao = new HBox(btnDeleteAccount);
        hbBtnExclusao.setAlignment(Pos.CENTER_RIGHT);
        hbBtnExclusao.setPadding(new Insets(20,0,0,0));
        hbBtnExclusao.setBorder(new Border(new BorderStroke(Color.BLUEVIOLET,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        conteudoCentral.getChildren().addAll(lbTitulo1, vbSessaoPreferenciasConteudo, vbSessaoPreferenciasUsuario, hbBtnExclusao);
        conteudoCentral.setBorder(new Border(new BorderStroke(Color.ORANGE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return conteudoCentral;
    }

    /**
     * Este método cria a seção de preferências de conteúdo.
     * @return a VBox da seção de preferências de conteúdo.
     */
    private VBox criarConteudoSessaoPreferencias() {
        VBox vbSessao = new VBox(15);
        vbSessao.setPadding(new Insets(10));

        Label title = new Label("Preferências de conteúdo");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        HlAlterarPreferencias = new Hyperlink("(Alterar)");
        HBox hbTitulo = new HBox();
        hbTitulo.setAlignment(Pos.BASELINE_LEFT);
        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);
        hbTitulo.getChildren().addAll(title, espacador, HlAlterarPreferencias);
        hbTitulo.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


        Label description = new Label("(Remover eventos visualizados na página inicial)");
        description.setFont(Font.font("Arial", 12));
        description.setTextFill(Color.GRAY);

        VBox leftColumn = new VBox(10);
        leftColumn.setBorder(new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        VBox rightColumn = new VBox(10);
        rightColumn.setBorder(new Border(new BorderStroke(Color.GREENYELLOW,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        cbCorporativo = new CheckBox("Corporativos");
        Label descCorporativo = new Label("(palestras, workshops, feiras de negócios)");
        descCorporativo.setFont(Font.font("Arial", 11));
        descCorporativo.setTextFill(Color.DARKSLATEGRAY);
        leftColumn.getChildren().add(new VBox(2, cbCorporativo, descCorporativo));

        cbEducacional = new CheckBox("Educacionais");
        Label descEducacional = new Label("(palestras, seminários, cursos)");
        descEducacional.setFont(Font.font("Arial", 11));
        descEducacional.setTextFill(Color.DARKSLATEGRAY);
        leftColumn.getChildren().add(new VBox(2, cbEducacional, descEducacional));

        cbCultural = new CheckBox("Culturais");
        Label descCultural = new Label("(shows, exposições, festivais)");
        descCultural.setFont(Font.font("Arial", 11));
        descCultural.setTextFill(Color.DARKSLATEGRAY);
        leftColumn.getChildren().add(new VBox(2, cbCultural, descCultural));

        cbEsportivo = new CheckBox("Esportivos");
        Label descEsportivo = new Label("(competições, maratonas, torneios)");
        descEsportivo.setFont(Font.font("Arial", 11));
        descEsportivo.setTextFill(Color.DARKSLATEGRAY);
        leftColumn.getChildren().add(new VBox(2, cbEsportivo, descEsportivo));

        cbBeneficente = new CheckBox("Beneficentes");
        Label descBeneficente = new Label("(arrecadação de fundos, campanhas sociais)");
        descBeneficente.setFont(Font.font("Arial", 11));
        descBeneficente.setTextFill(Color.DARKSLATEGRAY);
        rightColumn.getChildren().add(new VBox(2, cbBeneficente, descBeneficente));

        cbReligioso = new CheckBox("Religiosos");
        Label descReligioso = new Label("(cultos, retiros, encontros espirituais)");
        descReligioso.setFont(Font.font("Arial", 11));
        descReligioso.setTextFill(Color.DARKSLATEGRAY);
        rightColumn.getChildren().add(new VBox(2, cbReligioso, descReligioso));

        cbSocial = new CheckBox("Sociais");
        Label descSocial = new Label("(aniversários, casamentos, confraternizações)");
        descSocial.setFont(Font.font("Arial", 11));
        descSocial.setTextFill(Color.DARKSLATEGRAY);
        rightColumn.getChildren().add(new VBox(2, cbSocial, descSocial));

        for (CheckBox cb : List.of(cbCorporativo, cbBeneficente, cbEducacional, cbCultural, cbEsportivo, cbReligioso, cbSocial)) {
            cb.setDisable(true);
        }

        HBox columns = new HBox(60, leftColumn, rightColumn);
        vbSessao.getChildren().addAll(hbTitulo, description, columns);
        vbSessao.setBorder(new Border(new BorderStroke(Color.DARKBLUE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return vbSessao;
    }

    /**
     * Este método cria a seção de preferências de usuário.
     * @return a VBox da seção de preferências de usuário.
     */
    private VBox criarSessaoPreferenciasUsuario() {
        VBox sessao = new VBox(15);
        sessao.setPadding(new Insets(10));

        Label lbTitulo = new Label("Preferências de usuário");
        lbTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label lbDescricaoTitulo = new Label("(Editar informações do usuário)");
        lbDescricaoTitulo.setFont(Font.font("Arial", 12));
        lbDescricaoTitulo.setTextFill(Color.GRAY);

        GridPane gpDetalhesUsuario = new GridPane();
        gpDetalhesUsuario.setHgap(10);
        gpDetalhesUsuario.setVgap(15);
        gpDetalhesUsuario.setPadding(new Insets(10, 0, 10, 0));
        gpDetalhesUsuario.setBorder(new Border(new BorderStroke(Color.RED,
               BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        Label lbNome = new Label("Nome:");
        lbNome.getStyleClass().add("greeting-label");
        lbNomeUsuario = new Label();
        lbNomeUsuario.getStyleClass().add("greeting-label");
        hlAlterarNome = new Hyperlink("(Alterar)");
        Region espacadorNome = new Region();
        HBox.setHgrow(espacadorNome, Priority.ALWAYS);
        HBox hbNome = new HBox(8, lbNome, lbNomeUsuario, espacadorNome, hlAlterarNome);
        hbNome.setAlignment(Pos.BASELINE_LEFT);
        gpDetalhesUsuario.add(hbNome, 0, 0);

        Label lbEmail = new Label("Email:");
        lbEmail.getStyleClass().add("greeting-label");
        lbEmailUsuario = new Label();
        lbEmailUsuario.getStyleClass().add("greeting-label");
        hlAlterarEmail = new Hyperlink("(Alterar)");
        Region espacadorEmail= new Region();
        HBox.setHgrow(espacadorEmail, Priority.ALWAYS);
        HBox hbEmail = new HBox(8, lbEmail, lbEmailUsuario, espacadorEmail, hlAlterarEmail);
        hbEmail.setAlignment(Pos.BASELINE_LEFT);
        gpDetalhesUsuario.add(hbEmail, 0, 1);

        Label lbSenha = new Label("Senha:");
        lbSenha.getStyleClass().add("greeting-label");
        lbSenhaUsuario = new Label();
        lbSenhaUsuario.getStyleClass().add("greeting-label");
        hlAlterarSenha = new Hyperlink("(Alterar)");
        Region espacadorSenha= new Region();
        HBox.setHgrow(espacadorSenha, Priority.ALWAYS);
        HBox hbSenha = new HBox(8, lbSenha, lbSenhaUsuario, espacadorSenha, hlAlterarSenha);
        hbSenha.setAlignment(Pos.BASELINE_LEFT);
        gpDetalhesUsuario.add(hbSenha, 0, 2);

        Label lbCidade = new Label("Cidade:");
        lbCidade.getStyleClass().add("greeting-label");
        lbCidadeUsuario = new Label();
        lbCidadeUsuario.getStyleClass().add("greeting-label");
        hlAlterarCidade = new Hyperlink("(Alterar)");
        Region espacadorCidade= new Region();
        HBox.setHgrow(espacadorCidade, Priority.ALWAYS);
        HBox hbCidade = new HBox(8, lbCidade,lbCidadeUsuario, espacadorCidade, hlAlterarCidade);
        hbCidade.setAlignment(Pos.BASELINE_LEFT);
        gpDetalhesUsuario.add(hbCidade, 0, 3);

        Label lbDataNascimento = new Label("Data de Nascimento:");
        lbDataNascimento.getStyleClass().add("greeting-label");
        lbDataNascUsuario = new Label();
        lbDataNascUsuario.getStyleClass().add("greeting-label");
        hlAlterarDataNasc = new Hyperlink("(Alterar)");
        Region espacadorData= new Region();
        HBox.setHgrow(espacadorData, Priority.ALWAYS);
        HBox hbDataNasc = new HBox(8, lbDataNascimento,lbDataNascUsuario, espacadorData, hlAlterarDataNasc);
        hbDataNasc.setAlignment(Pos.BASELINE_LEFT);
        gpDetalhesUsuario.add(hbDataNasc, 0, 4);

        VBox photoSection = criarSessaoFotoUsuario();

        HBox hbPreferenciasUsuario = new HBox(30, gpDetalhesUsuario, photoSection);
        HBox.setHgrow(gpDetalhesUsuario, Priority.ALWAYS);

        sessao.getChildren().addAll(lbTitulo, lbDescricaoTitulo, hbPreferenciasUsuario);

        sessao.setBorder(new Border(new BorderStroke(Color.DARKBLUE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return sessao;
    }

    /**
     * Este método cria a seção de foto de perfil.
     * @return A VBox da seção de foto de perfil.
     */
    private VBox criarSessaoFotoUsuario() {
        VBox photoBox = new VBox(10);
        photoBox.setAlignment(Pos.CENTER);
        photoBox.setPadding(new Insets(10));

        Label photoTitle = new Label("Foto de perfil");
        photoTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        avatarView = new ImageView();
        avatarView.setFitHeight(100);
        avatarView.setFitWidth(100);
        avatarView.setPreserveRatio(true);

        StackPane photoPlaceholder = new StackPane();
        photoPlaceholder.getChildren().add(avatarView);
        photoPlaceholder.setPrefSize(100,100);
        photoPlaceholder.setMinSize(100,100);
        photoPlaceholder.setMaxSize(100,100);
        photoPlaceholder.setStyle(
                "-fx-background-color: C7C7C7;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-width: 4px;" +
                        "-fx-border-radius: 20;"
        );

        Rectangle rect = new Rectangle(100, 100);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        photoPlaceholder.setClip(rect);

        Label photoInfo = new Label("A imagem anexada deve ter\n200 x 200 pixels ");
        photoInfo.setFont(Font.font("Arial", 10));
        photoInfo.setTextFill(Color.GRAY);
        photoInfo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        hlAlterarFoto = new Hyperlink("(Alterar)");

        photoBox.getChildren().addAll(photoTitle, photoPlaceholder, photoInfo, hlAlterarFoto);
        return photoBox;
    }

    /**
     * Métodos de encapsulamento getters e setters
     */
    public BarraBuilder getBarraBuilder() {return barraBuilder;}

    public CheckBox getCbCorporativo() {return cbCorporativo;}
    public CheckBox getCbBeneficente() {return cbBeneficente;}
    public CheckBox getCbEducacional() {return cbEducacional;}
    public CheckBox getCbCultural() {return cbCultural;}
    public CheckBox getCbEsportivo() {return cbEsportivo;}
    public CheckBox getCbReligioso() {return cbReligioso;}
    public CheckBox getCbSocial() {return cbSocial;}

    public Label getLbNomeUsuario() {return lbNomeUsuario;}
    public Label getLbEmailUsuario() {return lbEmailUsuario;}
    public Label getLbSenhaUsuario() {return lbSenhaUsuario;}
    public Label getLbCidadeUsuario() {return lbCidadeUsuario;}
    public Label getLbDataNascUsuario() {return lbDataNascUsuario;}

    public Button getBtnDeleteAccount() {return btnDeleteAccount;}

    public Hyperlink getHlAlterarPreferencias() {return HlAlterarPreferencias;}
    public Hyperlink getHlAlterarNome() {return hlAlterarNome;}
    public Hyperlink getHlAlterarEmail() {return hlAlterarEmail;}
    public Hyperlink getHlAlterarSenha() {return hlAlterarSenha;}
    public Hyperlink getHlAlterarCidade() {return hlAlterarCidade;}
    public Hyperlink getHlAlterarDataNasc() {return hlAlterarDataNasc;}
    public Hyperlink getHlAlterarFoto() {return hlAlterarFoto;}

    public List<CheckBox> getThemeCheckBoxes() {return themeCheckBoxes;}
    public void setThemeCheckBoxes(List<CheckBox> themeCheckBoxes) {this.themeCheckBoxes = themeCheckBoxes;}

    public void setAvatarImagem(Image avatarImagem) {
        if(this.avatarView != null && avatarImagem != null) {
            this.avatarView.setImage(avatarImagem);
        }
    }
}