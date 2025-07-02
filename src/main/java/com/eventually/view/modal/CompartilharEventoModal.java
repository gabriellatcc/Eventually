package com.eventually.view.modal;

import com.eventually.model.EventoModel;
import com.eventually.view.HomeView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A View (janela) para o modal de "Compartilhar evento".
 * Esta versão é baseada na estrutura do CriaEventoModal para garantir consistência visual.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-07-02
 */
public class CompartilharEventoModal extends Parent {

    private HomeView.EventoH evento;

    private MenuItem twitterItem, facebookItem, copyLinkItem;
    private Button btnFechar;
    private MenuButton btnCompartilhar;

    /**
     * Construtor que recebe o evento a ser exibido e inicializa a UI.
     * @param evento O objeto de modelo com os dados do evento.
     */
    public CompartilharEventoModal(HomeView.EventoH evento) {
        this.evento = evento;
        setupUI();
    }

    /**
     * Configura o nó pai adicionando o layout principal.
     */
    private void setupUI() {
        VBox layout = criarLayoutPrincipal();
        this.getChildren().add(layout);
    }

    /**
     * Cria e retorna o layout principal do modal, seguindo a estrutura do CriaEventoModal.
     * @return Um VBox contendo toda a interface do modal.
     */
    public VBox criarLayoutPrincipal() {
        final double MODAL_WIDTH = 800;
        final double MODAL_HEIGHT = 550;

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10, 20, 10, 20));
        layout.getStyleClass().add("layout-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        layout.setClip(rect);

        Label titulo = new Label("Compartilhar Evento");
        titulo.getStyleClass().add("title-label-modal");

        HBox hbColunas = new HBox(40);
        hbColunas.setAlignment(Pos.TOP_CENTER);
        VBox vbColunaEsquerda = createLeftColumn(MODAL_WIDTH);
        VBox vbColunaDireita = createRightColumn(MODAL_WIDTH);
        hbColunas.getChildren().addAll(vbColunaEsquerda, vbColunaDireita);

        HBox hbBotoes = criarAreaDeBotoes();

        layout.getChildren().addAll(titulo, hbColunas, hbBotoes);
        return layout;
    }

    /**
     * Cria a coluna da esquerda com os detalhes do evento.
     * @param modalWidth A largura total do modal para cálculo de proporção.
     * @return Um VBox com as informações da esquerda.
     */
    private VBox createLeftColumn(double modalWidth) {
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setPrefWidth((modalWidth / 2) - 60);

        Label lbNomeTitulo = new Label("Evento");
        lbNomeTitulo.getStyleClass().add("subtitle-label-modal");
        Label lbNomeValor = new Label(evento.titulo());
        lbNomeValor.getStyleClass().add("info-label-modal");
        lbNomeValor.setWrapText(true);
        vbox.getChildren().addAll(lbNomeTitulo, lbNomeValor);

        Label lbDescricaoTitulo = new Label("Descrição");
        lbDescricaoTitulo.getStyleClass().add("subtitle-label-modal");
        Text txtDescricao = new Text(evento.descricao());
        txtDescricao.getStyleClass().add("info-text-modal");
        txtDescricao.setWrappingWidth(320);
        vbox.getChildren().addAll(lbDescricaoTitulo, txtDescricao);

        Label lbLocalizacaoTitulo = new Label("Localização");
        lbLocalizacaoTitulo.getStyleClass().add("subtitle-label-modal");
        Text txtLocalizacao = new Text(evento.local());
        txtLocalizacao.getStyleClass().add("info-text-modal");
        txtLocalizacao.setWrappingWidth(320);
        vbox.getChildren().addAll(lbLocalizacaoTitulo, txtLocalizacao);

        return vbox;
    }

    /**
     * Cria a coluna da direita com imagem e datas.
     * @param modalWidth A largura total do modal para cálculo de proporção.
     * @return Um VBox com as informações da direita.
     */
    private VBox createRightColumn(double modalWidth) {
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setPrefWidth((modalWidth / 2) - 60);

        Label lbImagemTitulo = new Label("Imagem do Evento");
        lbImagemTitulo.getStyleClass().add("subtitle-label-modal");
        ImageView imgPreview = new ImageView(evento.imagem());
        imgPreview.setFitWidth(320);
        imgPreview.setFitHeight(200);
        imgPreview.setPreserveRatio(true);
        imgPreview.getStyleClass().add("image-preview-container");
        vbox.getChildren().addAll(lbImagemTitulo, imgPreview);

        Label lbDataTitulo = new Label("Quando");
        lbDataTitulo.getStyleClass().add("subtitle-label-modal");

        Locale brasil = new Locale("pt", "BR");
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("EEE, dd 'de' MMMM 'de' yyyy", brasil);
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        String dataFormatada = String.format("De %s às %s\nAté %s às %s",
                evento.dataI().format(formatoData), evento.horaI().format(String.valueOf(formatoHora)),
                evento.dataF().format(formatoData), evento.horaF().format(String.valueOf(formatoHora))
        );
        Label lbDataValor = new Label(dataFormatada);
        lbDataValor.getStyleClass().add("info-label-modal");
        vbox.getChildren().addAll(lbDataTitulo, lbDataValor);

        return vbox;
    }

    /**
     * Cria a área de botões na parte inferior do modal.
     * @return Um HBox contendo os botões de ação.
     */
    private HBox criarAreaDeBotoes() {
        btnCompartilhar = new MenuButton("Compartilhar em...");
        btnCompartilhar.getStyleClass().add("modal-interact-button");

        twitterItem = new MenuItem("Compartilhar no Twitter");
        facebookItem = new MenuItem("Compartilhar no Facebook");
        copyLinkItem = new MenuItem("Copiar Link do Evento");

        btnCompartilhar.getItems().addAll(twitterItem, facebookItem, copyLinkItem);

        btnFechar = new Button("Fechar");
        btnFechar.getStyleClass().add("modal-interact-button");

        HBox hbBotoes = new HBox(20, btnCompartilhar, btnFechar);
        hbBotoes.setAlignment(Pos.CENTER);
        hbBotoes.setPadding(new Insets(15, 0, 0, 0));

        return hbBotoes;
    }

    /**
     * Fecha a janela (Stage) do modal.
     */
    public void close() {
        Stage stage = (Stage) this.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    public MenuItem getTwitterItem() { return twitterItem; }
    public MenuItem getFacebookItem() { return facebookItem; }
    public MenuItem getCopyLinkItem() { return copyLinkItem; }
    public Button getBtnFechar() { return btnFechar; }
}