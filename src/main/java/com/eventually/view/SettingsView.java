package com.eventually.view;

import com.eventually.controller.SettingsController;
import com.eventually.model.Comunidade;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Classe para a tela de Configurações.
 * Exibe e permite a alteração das preferências do usuário e de conteúdo.
 * @author Yuri Garcia Maia (Criação)
 * @since 22-05-2025
 * @version 1.09
 * @author Gabriella Tavares Costa Corrêa (Revisão de documentação, lógica e da estrutura da classe)
 * @since 22-05-2025
 */
public class SettingsView extends BorderPane {
    private SettingsController sController;
    private BarraBuilder barraBuilder;

    private List<CheckBox> themeCheckBoxes = new ArrayList<>();

    private Label lbNomeUsuario, lbEmailUsuario, lbSenhaUsuario, lbCidadeUsuario, lbDataNascUsuario;
    private Hyperlink hlAlterarNome, hlAlterarSenha, hlAlterarCidade, hlAlterarDataNasc,hlAlterarFoto;
    private Button btnDeleteAccount, btnAlterarPreferencias;

    private CheckBox cbCorporativo, cbBeneficente, cbEducacional, cbCultural, cbEsportivo, cbReligioso, cbSocial;

    private ImageView avatarView;

    private final Map<Comunidade, CheckBox> mapaDeCheckBoxesDeComunidades = new EnumMap<>(Comunidade.class);

    public SettingsView() {
        setupUI();
    }

    public void setSettingsController(SettingsController sController) {
        this.sController = sController;
    }

    private void setupUI() {
        this.barraBuilder = new BarraBuilder();
        VBox barraLateral = this.barraBuilder.construirBarraLateral();
        HBox barraSuperior = this.barraBuilder.construirBarraSuperior();
        VBox conteudoCentral = criarConteudoCentral();

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    private VBox criarConteudoCentral() {
        VBox conteudoCentral = new VBox();
        conteudoCentral.getStyleClass().add("conteudo-central");

        Label lbTitulo1 = new Label("Configurações");
        lbTitulo1.getStyleClass().add("settings-titulo1");

        VBox vbSessaoPreferenciasConteudo = criarConteudoSessaoPreferencias();
        VBox vbSessaoPreferenciasUsuario = criarSessaoPreferenciasUsuario();

        conteudoCentral.getChildren().addAll(lbTitulo1, vbSessaoPreferenciasConteudo, vbSessaoPreferenciasUsuario);
        return conteudoCentral;
    }

    private VBox criarConteudoSessaoPreferencias() {
        VBox vbSessao = new VBox();
        vbSessao.getStyleClass().add("settings-section");

        Label title = new Label("Preferências de conteúdo de comunidades");
        title.getStyleClass().add("section-title");
        btnAlterarPreferencias = new Button("Alterar");
        btnAlterarPreferencias.getStyleClass().add("interact-button");
        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);
        HBox hbTitulo = new HBox(title, espacador, btnAlterarPreferencias);
        hbTitulo.getStyleClass().add("section-header");

        Label description = new Label("(Remover eventos visualizados na página inicial)");
        description.getStyleClass().add("description-label");

        VBox leftColumn = new VBox();
        leftColumn.getStyleClass().add("checkbox-column");
        VBox rightColumn = new VBox();
        rightColumn.getStyleClass().add("checkbox-column");

        cbCorporativo = new CheckBox("Corporativos");
        cbCorporativo.getStyleClass().add("purple-checkbox");
        mapaDeCheckBoxesDeComunidades.put(Comunidade.CORPORATIVO, cbCorporativo);
        Label descCorporativo = new Label("(palestras, workshops, feiras de negócios)");
        leftColumn.getChildren().add(new VBox(cbCorporativo, descCorporativo));

        cbEducacional = new CheckBox("Educacionais");
        cbEducacional.getStyleClass().add("purple-checkbox");
        mapaDeCheckBoxesDeComunidades.put(Comunidade.EDUCACIONAL, cbEducacional);
        Label descEducacional = new Label("(palestras, seminários, cursos)");
        leftColumn.getChildren().add(new VBox(cbEducacional, descEducacional));

        cbCultural = new CheckBox("Culturais");
        cbCultural.getStyleClass().add("purple-checkbox");
        mapaDeCheckBoxesDeComunidades.put(Comunidade.CULTURAL, cbCultural);
        Label descCultural = new Label("(shows, exposições, festivais)");
        leftColumn.getChildren().add(new VBox(cbCultural, descCultural));

        cbEsportivo = new CheckBox("Esportivos");
        cbEsportivo.getStyleClass().add("purple-checkbox");
        mapaDeCheckBoxesDeComunidades.put(Comunidade.ESPORTIVO, cbEsportivo);
        Label descEsportivo = new Label("(competições, maratonas, torneios)");
        leftColumn.getChildren().add(new VBox(cbEsportivo, descEsportivo));

        cbBeneficente = new CheckBox("Beneficentes");
        cbBeneficente.getStyleClass().add("purple-checkbox");
        mapaDeCheckBoxesDeComunidades.put(Comunidade.BENEFICENTE, cbBeneficente);
        Label descBeneficente = new Label("(arrecadação de fundos, campanhas sociais)");
        rightColumn.getChildren().add(new VBox(cbBeneficente, descBeneficente));

        cbReligioso = new CheckBox("Religiosos");
        cbReligioso.getStyleClass().add("purple-checkbox");
        mapaDeCheckBoxesDeComunidades.put(Comunidade.RELIGIOSO, cbReligioso);
        Label descReligioso = new Label("(cultos, retiros, encontros espirituais)");
        rightColumn.getChildren().add(new VBox(cbReligioso, descReligioso));

        cbSocial = new CheckBox("Sociais");
        cbSocial.getStyleClass().add("purple-checkbox");
        mapaDeCheckBoxesDeComunidades.put(Comunidade.SOCIAL, cbSocial);
        Label descSocial = new Label("(aniversários, casamentos, confraternizações)");
        rightColumn.getChildren().add(new VBox(cbSocial, descSocial));

        rightColumn.getChildren().add(btnAlterarPreferencias);

        for (Label l : List.of(descCorporativo, descEducacional, descCultural, descEsportivo, descBeneficente, descReligioso, descSocial)) {
            l.getStyleClass().add("description-label-list");
        }

        for (CheckBox cb : mapaDeCheckBoxesDeComunidades.values()) {
            cb.setDisable(true);
        }

        HBox columns = new HBox(leftColumn, rightColumn);
        columns.getStyleClass().add("checkbox-columns-container");

        vbSessao.getChildren().addAll(hbTitulo, description, columns);
        return vbSessao;
    }

    private VBox criarSessaoPreferenciasUsuario() {
        VBox sessao = new VBox();
        sessao.getStyleClass().add("settings-section");

        Label lbTitulo = new Label("Preferências de usuário");
        lbTitulo.getStyleClass().add("section-title");
        Label lbDescricaoTitulo = new Label("(Editar informações do usuário)");
        lbDescricaoTitulo.getStyleClass().add("description-label");

        GridPane gpDetalhesUsuario = new GridPane();
        gpDetalhesUsuario.getStyleClass().add("user-details-grid");

        Label lbEmail = new Label("Email:");
        lbEmail.getStyleClass().add("user-detail-row-email");
        lbEmailUsuario = new Label();
        HBox hbEmail = new HBox(lbEmail, lbEmailUsuario);
        hbEmail.getStyleClass().add("user-detail-row-email");
        gpDetalhesUsuario.add(hbEmail, 0, 1);

        Label lbNome = new Label("Nome:");
        lbNomeUsuario = new Label();
        hlAlterarNome = new Hyperlink("(Alterar)");
        Region espacadorNome = new Region();
        HBox.setHgrow(espacadorNome, Priority.ALWAYS);
        HBox hbNome = new HBox(lbNome, lbNomeUsuario, espacadorNome, hlAlterarNome);
        hbNome.getStyleClass().add("user-detail-row");
        gpDetalhesUsuario.add(hbNome, 0, 0);

        Label lbSenha = new Label("Senha:");
        lbSenhaUsuario = new Label();
        hlAlterarSenha = new Hyperlink("(Alterar)");
        Region espacadorSenha= new Region();
        HBox.setHgrow(espacadorSenha, Priority.ALWAYS);
        HBox hbSenha = new HBox(lbSenha, lbSenhaUsuario, espacadorSenha, hlAlterarSenha);
        hbSenha.getStyleClass().add("user-detail-row");
        gpDetalhesUsuario.add(hbSenha, 0, 2);

        Label lbCidade = new Label("Cidade:");
        lbCidadeUsuario = new Label();
        hlAlterarCidade = new Hyperlink("(Alterar)");
        Region espacadorCidade= new Region();
        HBox.setHgrow(espacadorCidade, Priority.ALWAYS);
        HBox hbCidade = new HBox(lbCidade,lbCidadeUsuario, espacadorCidade, hlAlterarCidade);
        hbCidade.getStyleClass().add("user-detail-row");
        gpDetalhesUsuario.add(hbCidade, 0, 3);

        Label lbDataNascimento = new Label("Data de Nascimento:");
        lbDataNascUsuario = new Label();
        hlAlterarDataNasc = new Hyperlink("(Alterar)");
        Region espacadorData= new Region();
        HBox.setHgrow(espacadorData, Priority.ALWAYS);
        HBox hbDataNasc = new HBox(lbDataNascimento,lbDataNascUsuario, espacadorData, hlAlterarDataNasc);
        hbDataNasc.getStyleClass().add("user-detail-row");
        gpDetalhesUsuario.add(hbDataNasc, 0, 4);

        for (Label l : List.of( lbEmail, lbEmailUsuario, lbNome, lbNomeUsuario, lbSenha, lbSenhaUsuario, lbCidade, lbCidadeUsuario, lbDataNascimento, lbDataNascUsuario)) {
            l.getStyleClass().add("greeting-label");
        }

        VBox photoSection = criarSessaoFotoUsuario();

        HBox hbPreferenciasUsuario = new HBox(gpDetalhesUsuario, photoSection);
        hbPreferenciasUsuario.getStyleClass().add("user-preferences-container");
        HBox.setHgrow(gpDetalhesUsuario, Priority.ALWAYS);

        sessao.getChildren().addAll(lbTitulo, lbDescricaoTitulo, hbPreferenciasUsuario);
        return sessao;
    }

    private VBox criarSessaoFotoUsuario() {
        VBox photoBox = new VBox();
        photoBox.getStyleClass().add("photo-section");

        Label photoTitle = new Label("Foto de perfil");
        photoTitle.getStyleClass().add("photo-title");

        avatarView = new ImageView();
        avatarView.setFitHeight(100);
        avatarView.setFitWidth(100);
        avatarView.setPreserveRatio(true);

        StackPane photoPlaceholder = new StackPane(avatarView);
        photoPlaceholder.getStyleClass().add("photo-placeholder");

        Label photoInfo = new Label("A imagem anexada deve ter 200 x 200 pixels");
        photoInfo.setWrapText(true);
        photoInfo.getStyleClass().add("photo-info");

        hlAlterarFoto = new Hyperlink("(Alterar)");

        btnDeleteAccount = new Button("Excluir conta");
        btnDeleteAccount.getStyleClass().add("delete-account-button");

        photoBox.getChildren().addAll(photoTitle, photoPlaceholder, photoInfo, hlAlterarFoto,btnDeleteAccount);
        return photoBox;
    }

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
    public Button getbtnAlterarPreferencias() {return btnAlterarPreferencias;}
    public Hyperlink getHlAlterarNome() {return hlAlterarNome;}
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
    public Map<Comunidade, CheckBox> getMapaDeCheckBoxesDeComunidades() {
        return mapaDeCheckBoxesDeComunidades;
    }
}