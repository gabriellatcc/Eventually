package com.eventually.view;

import com.eventually.controller.CriaEventoController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Classe responsável pelo modal de "Criar evento".
 * @author Yuri Garcia Maia
 * @version 1.0
 * @since 2025-06-18
 * @author Gabriella Tavares Costa Corrêa (revisão de documentação e parte lógica)
 * @since 2025-06-19
 */
public class CriaEventoModal {
    private Stage modalStage;
    private Scene modalScene;

    private TextField fldEventName;
    private TextArea areaDescription;
    private RadioButton radioPresencial, radioOnline, radioHibrido;
    private ToggleGroup groupFormato;
    private TextField fldLink;
    private TextField fldLocalizacao;
    private VBox imageUploadBox;
    private ImageView eventImageView;
    private TextField fldParticipantCount;
    private TextField fldStartTime, fldEndTime;
    private DatePicker datePickerStart, datePickerEnd;
    private TextField fldTags, fldHashtags;
    private Button btnSalvar;
    private Button btnSair;

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
    }

    /**
     * Define o controller para este modal.
     * @param eventController O controller a ser usado.
     */
    public void setCreateEventController(CriaEventoController eventController) {
        this.eventController = eventController;
    }

    /**
     * Exibe a janela modal configurada para a criação de um novo evento.
     * @param parentStage Janela principal da aplicação que será usada como base para o modal.
     */
    public void showCreateEventModal(Stage parentStage) {
        modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(parentStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

        final double MODAL_WIDTH = 1000;
        final double MODAL_HEIGHT = 730;

        VBox rootLayout = new VBox(20);
        rootLayout.setAlignment(Pos.TOP_CENTER);
        rootLayout.setPadding(new Insets(30, 40, 30, 40));
        rootLayout.getStyleClass().add("root-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        rootLayout.setClip(rect);

        Label title = new Label("Criar novo evento");
        title.getStyleClass().add("title-label");

        HBox columnsContainer = new HBox(50);
        columnsContainer.setAlignment(Pos.TOP_CENTER);

        VBox leftColumn = createLeftColumn();
        leftColumn.setPrefWidth((MODAL_WIDTH / 2) - 60);

        VBox rightColumn = createRightColumn();
        rightColumn.setPrefWidth((MODAL_WIDTH / 2) - 60);

        columnsContainer.getChildren().addAll(leftColumn, rightColumn);

        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));

        btnSalvar = new Button("Salvar");
        btnSalvar.getStyleClass().add("save-button");
        btnSalvar.setOnAction(e -> {
            if (eventController != null) {
                System.out.println("Salvar Clicado");
            }
        });

        btnSair = new Button("Sair");
        btnSair.getStyleClass().add("exit-button");
        btnSair.setOnAction(e -> close());

        buttonsBox.getChildren().addAll(btnSalvar, btnSair);

        Label idLabel = new Label("id xxxx-x");
        idLabel.getStyleClass().add("id-label");
        VBox.setMargin(idLabel, new Insets(10, 0, 0, 0));
        StackPane bottomPane = new StackPane(buttonsBox);
        StackPane.setAlignment(idLabel, Pos.CENTER_RIGHT);
        bottomPane.getChildren().add(idLabel);


        rootLayout.getChildren().addAll(title, columnsContainer, bottomPane);

        modalScene = new Scene(rootLayout, MODAL_WIDTH, MODAL_HEIGHT, Color.TRANSPARENT);
        modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    private VBox createLeftColumn() {
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.TOP_LEFT);

        vbox.getChildren().add(createLabel("Nome do evento"));
        fldEventName = createTextField("Nome do evento");
        vbox.getChildren().add(fldEventName);

        vbox.getChildren().add(createLabel("Descrição"));
        areaDescription = new TextArea();
        areaDescription.setPromptText("uma breve descrição sobre o evento");
        areaDescription.setWrapText(true);
        areaDescription.setPrefHeight(100);
        vbox.getChildren().add(areaDescription);

        vbox.getChildren().add(createLabel("Formato"));
        groupFormato = new ToggleGroup();
        radioPresencial = new RadioButton("Presencial");
        radioPresencial.setToggleGroup(groupFormato);
        radioOnline = new RadioButton("Online");
        radioOnline.setToggleGroup(groupFormato);
        radioHibrido = new RadioButton("Híbrido");
        radioHibrido.setToggleGroup(groupFormato);
        HBox formatoBox = new HBox(20, radioPresencial, radioOnline, radioHibrido);
        vbox.getChildren().add(formatoBox);

        vbox.getChildren().add(createLabel("Link"));
        fldLink = createTextField("Link para o evento");
        vbox.getChildren().add(fldLink);

        vbox.getChildren().add(createLabel("Localização"));
        fldLocalizacao = createTextField("Link do google maps com a localização do evento");
        vbox.getChildren().add(fldLocalizacao);

        return vbox;
    }

    private VBox createRightColumn() {
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.TOP_LEFT);

        vbox.getChildren().add(createLabel("Imagem do evento"));
        imageUploadBox = new VBox(5);
        imageUploadBox.setAlignment(Pos.CENTER);
        imageUploadBox.getStyleClass().add("image-upload-box");
        imageUploadBox.setPrefHeight(120);
        try {
            Image placeholder = new Image(getClass().getResource("/images/upload-icon.png").toExternalForm(), 50, 50, true, true);
            eventImageView = new ImageView(placeholder);
        } catch (Exception e) {
            eventImageView = new ImageView();
            eventImageView.setFitWidth(50);
            eventImageView.setFitHeight(50);
        }
        Label uploadLabel = new Label("Nenhum arquivo selecionado\nFaça upload de uma imagem");
        uploadLabel.getStyleClass().add("upload-label");
        imageUploadBox.getChildren().addAll(eventImageView, uploadLabel);
        imageUploadBox.setOnMouseClicked(e -> System.out.println("Image upload box clicked!"));
        vbox.getChildren().add(imageUploadBox);

        vbox.getChildren().add(createLabel("Capacidade de participantes"));
        HBox capacityBox = new HBox(20);
        capacityBox.setAlignment(Pos.CENTER_LEFT);
        Button btnDecrement = createCapacityButton("-");
        Button btnIncrement = createCapacityButton("+");

        fldParticipantCount = new TextField("0");
        fldParticipantCount.setPrefWidth(100);
        fldParticipantCount.setAlignment(Pos.CENTER);
        fldParticipantCount.getStyleClass().add("capacity-field");

        fldParticipantCount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                fldParticipantCount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        btnDecrement.setOnAction(e -> updateCapacity(false));
        btnIncrement.setOnAction(e -> updateCapacity(true));
        capacityBox.getChildren().addAll(btnDecrement, fldParticipantCount, btnIncrement);
        vbox.getChildren().add(capacityBox);

        vbox.getChildren().add(createLabel("Data do evento"));
        HBox startBox = createDateTimeRow("Início:", "horário: XX:XX AM/PM");
        fldStartTime = (TextField) startBox.lookup("#timeField");
        datePickerStart = (DatePicker) startBox.lookup("#datePicker");
        vbox.getChildren().add(startBox);

        HBox endBox = createDateTimeRow("Fim:", "horário: XX:XX AM/PM");
        fldEndTime = (TextField) endBox.lookup("#timeField");
        datePickerEnd = (DatePicker) endBox.lookup("#datePicker");
        vbox.getChildren().add(endBox);

        vbox.getChildren().add(createLabel("Tags"));
        fldTags = createTextField("");
        vbox.getChildren().add(fldTags);

        vbox.getChildren().add(createLabel("Hashtags"));
        fldHashtags = createTextField("");
        vbox.getChildren().add(fldHashtags);

        return vbox;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("field-label");
        return label;
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setPrefHeight(40);
        return textField;
    }

    private Button createCapacityButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("capacity-button");
        return button;
    }

    private HBox createDateTimeRow(String labelText, String timePrompt) {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label label = createLabel(labelText);

        TextField timeField = createTextField(timePrompt);
        timeField.setId("timeField");
        timeField.setPrefWidth(180);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("data\nxx/xxx/xxxx");
        datePicker.setId("datePicker");
        datePicker.setPrefWidth(180);

        hbox.getChildren().addAll(label, timeField, datePicker);
        return hbox;
    }

    private void updateCapacity(boolean increment) {
        try {
            int currentValue = Integer.parseInt(fldParticipantCount.getText());
            if (increment) {
                currentValue++;
            } else {
                if (currentValue > 0) {
                    currentValue--;
                }
            }
            fldParticipantCount.setText(String.valueOf(currentValue));
        } catch (NumberFormatException e) {
            fldParticipantCount.setText("0");
        }
    }

    public void close() {
        if (modalStage != null) {
            modalStage.close();
        }
    }

    public int getParticipantCount() {
        try {
            return Integer.parseInt(fldParticipantCount.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public TextField getEventName() {return fldEventName;}
    public TextArea getDescription() {return areaDescription;}
    public String getFormato() {
        if (groupFormato.getSelectedToggle() != null) {
            RadioButton selectedRadioButton = (RadioButton) groupFormato.getSelectedToggle();
            return selectedRadioButton.getText();
        }
        return "";
    }
    public DatePicker getStartDate() {return datePickerStart;}
    public DatePicker getEndDate() {return datePickerEnd;}
    public TextField getStartTime() {return fldStartTime;}
    public TextField getEndTime() {return fldEndTime;}
    public Scene getModalScene() {return modalScene;}

    public TextField getLink() {return fldLink;}
    public TextField getLocalizacao() {return fldLocalizacao;}
    public VBox getImageUploadBox() {return imageUploadBox;}
    public ImageView getEventImageView() {return eventImageView;}
    public TextField getFldTags() {return fldTags;}
    public TextField getFldHashtags() {return fldHashtags;}
    public Button getBtnSalvar() {return btnSalvar;}
}
