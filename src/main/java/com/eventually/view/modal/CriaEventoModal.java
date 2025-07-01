package com.eventually.view.modal;

import com.eventually.controller.CriaEventoController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe responsável pelo modal de "Criar evento".
 * @author Yuri Garcia Maia
 * @version 1.04
 * @since 2025-06-18
 * @author Gabriella Tavares Costa Corrêa (Revisão de documentação e parte lógica)
 * @since 2025-06-19
 */
public class CriaEventoModal extends Parent {
    private CriaEventoController criaEventoController;

    private TextField fldNomeEvento;
    private TextArea taDescricao, taLocalizacao;
    private RadioButton radioPresencial, radioOnline, radioHibrido;
    private ToggleGroup groupFormato;
    private TextField fldLink;

    private Image imageEvento;
    private ImageView imgPreview;
    private File arquivoSelecionado;

    private CheckBox cbCorporativo, cbBeneficente, cbEducacional, cbCultural, cbEsportivo, cbReligioso, cbSocial;

    private TextField fldNParticipantes;
    private TextField fldHoraInicio, fldHoraFinal;
    private DatePicker datePickerStart, datePickerEnd;

    private Button btnDecrement, btnIncrement, btnEscolherImagem, btnCriar, btnCancelar;

    private CriaEventoController eventController;

    static {
        try {
            Font.loadFont(CriaEventoModal.class.getResource("/fonts/Poppins-Regular.ttf").toExternalForm(), 10);
            Font.loadFont(CriaEventoModal.class.getResource("/fonts/Poppins-Bold.ttf").toExternalForm(), 10);
        } catch (Exception e) {
            System.err.println("Fonte Poppins não encontrada. Usando fontes padrão.");
            e.printStackTrace();
        }
    }

    /**
     * Construtor padrão da classe.
     */
    public CriaEventoModal() {
        setup();
    }

    /**
     * Define o controlador para este modal.
     * @param criaEventoController o controller a ser usado.
     */
    public void setCriaEventoController(CriaEventoController criaEventoController) {
        this.criaEventoController = criaEventoController;
    }

    /**
     * Exibe a janela modal configurada para criar evento.
     */
    private void setup() {
        VBox layout = criarLayoutPrincipal();
        this.getChildren().add(layout);
    }

    /**
     * Exibe a janela modal configurada para a criação de um novo evento.
     */
    public VBox criarLayoutPrincipal() {
        final double MODAL_WIDTH = 1000;
        final double MODAL_HEIGHT = 730;

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10, 20, 10, 20));
        layout.getStyleClass().add("layout-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        layout.setClip(rect);

        Label titulo = new Label("Criar novo evento");
        titulo.getStyleClass().add("title-label-modal");

        HBox hbColunas = new HBox(50);
        hbColunas.setAlignment(Pos.TOP_CENTER);
        VBox vbColunaEsquerda = createLeftColumn();
        vbColunaEsquerda.setPrefWidth((MODAL_WIDTH / 2) - 60);
        VBox vbColunaDireita = createRightColumn();
        vbColunaDireita.setPrefWidth((MODAL_WIDTH / 2) - 60);
        hbColunas.getChildren().addAll(vbColunaEsquerda, vbColunaDireita);

        HBox hbBotoes = new HBox(20);
        hbBotoes.setAlignment(Pos.CENTER);
        btnCriar = new Button("Criar");
        btnCriar.getStyleClass().add("modal-interact-button");
        btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("modal-interact-button");
        hbBotoes.getChildren().addAll(btnCriar, btnCancelar);

        layout.getChildren().addAll(titulo, hbColunas, hbBotoes);
        return layout;
    }

    private VBox createLeftColumn() {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.TOP_LEFT);

        Label lbNomeEvento = new Label("Nome do evento");
        lbNomeEvento.getStyleClass().add("subtitle-label-modal");
        vbox.getChildren().add(lbNomeEvento);

        fldNomeEvento = new TextField();
        fldNomeEvento.setPromptText("Nome do evento");
        fldNomeEvento.getStyleClass().add("modal-field");
        vbox.getChildren().add(fldNomeEvento);

        Label lbDescricao = new Label ("Descrição");
        lbDescricao.getStyleClass().add("subtitle-label-modal");
        vbox.getChildren().add(lbDescricao);

        taDescricao = new TextArea();
        taDescricao.setPromptText("Um breve detalhamento sobre o evento");
        taDescricao.setWrapText(true);
        taDescricao.setPrefHeight(90);
        taDescricao.getStyleClass().add("modal-field");
        vbox.getChildren().add(taDescricao);

        Label lbFormato = new Label ("Formato");
        lbFormato.getStyleClass().add("subtitle-label-modal");
        vbox.getChildren().add(lbFormato);

        groupFormato = new ToggleGroup();
        radioPresencial = new RadioButton("Presencial");
        radioPresencial.getStyleClass().add("modal-field");
        radioPresencial.setToggleGroup(groupFormato);
        radioOnline = new RadioButton("Online");
        radioOnline.getStyleClass().add("modal-field");
        radioOnline.setToggleGroup(groupFormato);
        radioHibrido = new RadioButton("Híbrido");
        radioHibrido.getStyleClass().add("modal-field");
        radioHibrido.setToggleGroup(groupFormato);
        HBox hbFormato = new HBox(20, radioPresencial, radioOnline, radioHibrido);
        vbox.getChildren().add(hbFormato);

        Label lbLink = new Label("Link");
        lbLink.getStyleClass().add("subtitle-label-modal");
        vbox.getChildren().add(lbLink);

        fldLink = new TextField();
        fldLink.setPromptText("Link rede social do evento ou orgranizador principal");
        fldLink.getStyleClass().add("modal-field");
        vbox.getChildren().add(fldLink);

        Label lbLocalizacao=new Label("Localização (Se em ambiente físico ou virtual)");
        lbLocalizacao.getStyleClass().add("subtitle-label-modal");
        vbox.getChildren().add(lbLocalizacao);

        taLocalizacao = new TextArea();
        taLocalizacao.setPromptText("Exemplo: R. Rua Altos Bosques Claros, 12, Jardim das Flores, Bela Vista - SP, 12345-678 ou https://meet.google.com/kpw-iwnw-nir");
        taLocalizacao.setPrefHeight(60);
        taLocalizacao.setWrapText(true);
        taLocalizacao.getStyleClass().add("text-area");
        vbox.getChildren().add(taLocalizacao);
        Platform.runLater(() -> {
            Node promptNode = taLocalizacao.lookup(".prompt-text");

            if (promptNode != null) {
                promptNode.setStyle("-fx-font-size: 6px;");
            }
        });

        Label lbNParticipantes = new Label("Capacidade de participantes");
        lbNParticipantes.getStyleClass().add("subtitle-label-modal");
        vbox.getChildren().add(lbNParticipantes);

        HBox hbNParticipantes = new HBox(20);
        hbNParticipantes.setAlignment(Pos.CENTER_LEFT);

        btnDecrement =new Button("-");
        btnDecrement.setPrefHeight(30);
        btnDecrement.setPrefWidth(30);
        btnDecrement.getStyleClass().add("crement-button");
        btnIncrement = new Button("+");
        btnIncrement.setPrefHeight(30);
        btnIncrement.setPrefWidth(30);
        btnIncrement.getStyleClass().add("crement-button");

        fldNParticipantes = new TextField("0");
        fldNParticipantes.setPrefWidth(40);
        fldNParticipantes.setAlignment(Pos.CENTER);
        fldNParticipantes.getStyleClass().add("modal-field");

        hbNParticipantes.getChildren().addAll(btnDecrement, fldNParticipantes, btnIncrement);
        vbox.getChildren().add(hbNParticipantes);
        return vbox;
    }

    private VBox createRightColumn() {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.TOP_LEFT);

        HBox hbImagem = new HBox(15);
        hbImagem.setAlignment(Pos.CENTER);
        Label lbImagemEvento = new Label("Imagem do evento");
        lbImagemEvento.getStyleClass().add("subtitle-label-modal");
        vbox.getChildren().add(lbImagemEvento);

        VBox vbPreview = new VBox();
        vbPreview.setAlignment(Pos.CENTER_LEFT);
        Image provisoria = new Image(getClass().getResourceAsStream("/images/upload-icon.png"));
        imgPreview = new ImageView(provisoria);
        imgPreview.setFitWidth(160);
        imgPreview.setFitHeight(100);
        imgPreview.setPreserveRatio(true);
        imgPreview.getStyleClass().add("image-preview-container");
        vbPreview.getChildren().add(imgPreview);

        VBox vbImagem = new VBox(5);
        vbImagem.setAlignment(Pos.CENTER);
        btnEscolherImagem = new Button("Escolher Arquivo");
        btnEscolherImagem.getStyleClass().add("modal-interact-button");

        Label lbAvisoImagem = new Label("Anexe uma imagem na\n    (320x200 pixels)");
        lbAvisoImagem.getStyleClass().add("label-modal");
        vbImagem.getChildren().addAll(btnEscolherImagem, lbAvisoImagem);

        hbImagem.getChildren().addAll(vbPreview,vbImagem);

        vbox.getChildren().add(hbImagem);

        Label lbDataEvento = new Label("Data do evento");
        lbDataEvento.getStyleClass().add("subtitle-label-modal");
        vbox.getChildren().add(lbDataEvento);
        HBox hbData = new HBox(10);
        hbData.setAlignment(Pos.CENTER_LEFT);

        VBox vbLabel = new VBox(12);
        vbLabel.setAlignment(Pos.CENTER_LEFT);
        vbLabel.setPadding(new Insets(0, 0, 0, 5));
        Label lbInicio = new Label("Início:");
        lbInicio.getStyleClass().add("label-modal");
        Label lbFim = new Label("Fim:");
        lbFim.getStyleClass().add("label-modal");
        vbLabel.getChildren().addAll(lbInicio, lbFim);

        VBox vbFldHora = new VBox(5);
        vbFldHora.setAlignment(Pos.CENTER);
        fldHoraInicio = new TextField();
        fldHoraInicio.setPromptText("XX:XX");
        fldHoraInicio.setPrefHeight(28);
        fldHoraInicio.setPrefWidth(100);
        fldHoraInicio.getStyleClass().add("modal-field");
        fldHoraFinal = new TextField();
        fldHoraFinal.setPromptText("XX:XX");
        fldHoraFinal.setPrefHeight(28);
        fldHoraFinal.setPrefWidth(100);
        fldHoraFinal.getStyleClass().add("modal-field");
        vbFldHora.getChildren().addAll(fldHoraInicio, fldHoraFinal);

        VBox hbdataPicker = new VBox(5);
        hbdataPicker.setAlignment(Pos.CENTER);
        datePickerStart = new DatePicker();
        datePickerStart.setPromptText("dd/mm/yyyy");
        datePickerStart.setPrefHeight(28);
        datePickerStart.getStyleClass().add("modal-field");
        LocalDate hoje = LocalDate.now();
        hoje.minusDays(1);
        bloquearIntervaloData(datePickerStart,
                LocalDate.of(hoje.getYear(), hoje.getMonthValue(), hoje.getDayOfMonth()));
        datePickerEnd = new DatePicker();
        datePickerEnd.setPromptText("dd/mm/yyyy");
        datePickerEnd.setPrefHeight(28);
        datePickerEnd.getStyleClass().add("modal-field");
        bloquearIntervaloData(datePickerEnd,
                LocalDate.of(hoje.getYear(), hoje.getMonthValue(), hoje.getDayOfMonth()));
        hbdataPicker.getChildren().addAll(datePickerStart, datePickerEnd);
        hbData.getChildren().addAll(vbLabel, vbFldHora, hbdataPicker);
        vbox.getChildren().add(hbData);

        Label lbTags = new Label("Tags");
        lbTags.getStyleClass().add("subtitle-label-modal");
        vbox.getChildren().add(lbTags);
        Label lbTagsNota = new Label("Selecione os temas que mais se relacionam com o evento");

        lbTagsNota.setWrapText(true);
        lbTagsNota.setTextAlignment(TextAlignment.LEFT);

        lbTagsNota.getStyleClass().add("label-modal");
        vbox.getChildren().add(lbTagsNota);

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

        VBox colunaEsquerda = new VBox(10);
        colunaEsquerda.getChildren().addAll(cbCorporativo, cbBeneficente, cbEducacional,cbCultural);

        VBox colunaDireita = new VBox(10);
        colunaDireita.getChildren().addAll(cbEsportivo, cbReligioso, cbSocial);

        HBox colunas = new HBox(100);
        colunas.getChildren().addAll(colunaEsquerda, colunaDireita);

        vbox.getChildren().addAll(colunas);

        return vbox;
    }

    /**
     * Este método bloqueia a seleção de datas do datapicker na interface
     * @param datePickerStart o campo de data.
     * @param dataMinima data minima a partir de hoje.
     */
    private void bloquearIntervaloData(DatePicker datePickerStart, LocalDate dataMinima) {
        datePickerStart.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(dataMinima)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                } else {
                    setDisable(false);
                    setStyle("-fx-background-color: white;");
                }
            }
        });
    }

    public void close() {
        Stage stage = (Stage) this.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    public int getParticipantCount() {
        try {
            return Integer.parseInt(fldNParticipantes.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getFormato() {
        if (groupFormato.getSelectedToggle() != null) {
            RadioButton selectedRadioButton = (RadioButton) groupFormato.getSelectedToggle();
            return selectedRadioButton.getText();
        }
        return "";
    }

    /**
     * Métodos de encapsulamento getters e setters.
     */
    public File getArquivoSelecionado() {return arquivoSelecionado;}
    public void setArquivoSelecionado(File arquivoSelecionado) {
        this.arquivoSelecionado = arquivoSelecionado;
        btnCriar.setDisable(arquivoSelecionado == null);
    }

    public Button getBtnEscolherImagem() {return btnEscolherImagem;}
    public TextField getFldNomeEvento() {return fldNomeEvento;}
    public TextArea getTaDescricao() {return taDescricao;}

    public Button getBtnDecrement() {return btnDecrement;}
    public Button getBtnIncrement() {return btnIncrement;}

    public RadioButton getRadioPresencial() {return radioPresencial;}
    public RadioButton getRadioOnline() {return radioOnline;}
    public RadioButton getRadioHibrido() {return radioHibrido;}

    public TextField getFldLink() {return fldLink;}
    public TextArea getTaLocalizacao() {return taLocalizacao;}
    public Image getImageEvento() {return imageEvento;}
    public TextField getFldNParticipantes() {return fldNParticipantes;}

    public void setPreviewImage(Image image) {
        this.imageEvento = image;
        this.imgPreview.setImage(image);
    }

    public TextField getFldHoraInicio() {return fldHoraInicio;}
    public DatePicker getDatePickerStart() {return datePickerStart;}
    public TextField getFldHoraFinal() {return fldHoraFinal;}
    public DatePicker getDatePickerEnd() {return datePickerEnd;}

    public CheckBox getCbCorporativo() {return cbCorporativo;}
    public CheckBox getCbBeneficente() {return cbBeneficente;}
    public CheckBox getCbEducacional() {return cbEducacional;}
    public CheckBox getCbCultural() {return cbCultural;}
    public CheckBox getCbEsportivo() {return cbEsportivo;}
    public CheckBox getCbReligioso() {return cbReligioso;}
    public CheckBox getCbSocial() {return cbSocial;}

    public Button getBtnCriar() {return btnCriar;}
    public Button getBtnCancelar() {return btnCancelar;}
}