package com.eventually.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

/**
 * Representa um card de evento em formato de lista horizontal.
 * Este componente exibe título, local, datas e informações de capacidade/vagas,
 * além de um botão de ação. É flexível para exibir eventos de um ou múltiplos dias.
 *
 * @version 1.0
 * @author Gabriella Tavares Costa Corrêa
 * @since 2025-06-28
 */
public class EventoMECartao extends HBox {
    private Label lblTitulo;
    private Label lblLocal;
    private Label lblDataLinha1;
    private Label lblDataLinha2;
    private Label lblCapacidadeValor;
    private Label lblCapacidadeDesc;
    private Button btnVer;
    private Circle dotCapacidade;

    /**
     * Construtor que inicializa a UI do card.
     */
    public EventoMECartao() {
         String cssPath = getClass().getResource("/styles/event-list-card.css").toExternalForm();
         this.getStylesheets().add(cssPath);
        setupUI();
    }

    private void setupUI() {
        this.setPrefHeight(120);
        this.setMaxWidth(1000);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("layout-pane");

        VBox leftPane = new VBox(5);
        leftPane.setPadding(new Insets(15));
        leftPane.setAlignment(Pos.CENTER_LEFT);
        leftPane.getStyleClass().add("evento-list-card-left");
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        lblTitulo = new Label("Título do EventoH");
        lblTitulo.getStyleClass().add("card-title");

        lblLocal = new Label("Local do EventoH");
        lblLocal.getStyleClass().add("card-local");

        VBox titleBox = new VBox(0, lblTitulo, lblLocal);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        lblDataLinha1 = new Label("SEX 14, MAR 2025");
        lblDataLinha1.getStyleClass().add("card-date");

        lblDataLinha2 = new Label("18:20 - 21:00");
        lblDataLinha2.getStyleClass().add("card-date");

        VBox dateBox = new VBox(0, lblDataLinha1, lblDataLinha2);
        dateBox.setAlignment(Pos.BOTTOM_LEFT);

        leftPane.getChildren().addAll(titleBox, spacer, dateBox);

        VBox rightPane = new VBox(10);
        rightPane.setPrefWidth(220);
        rightPane.setPadding(new Insets(15));
        rightPane.setAlignment(Pos.CENTER);
        rightPane.getStyleClass().add("evento-list-card-right");

        HBox capacityInfoBox = new HBox(8);
        capacityInfoBox.setAlignment(Pos.CENTER);

        dotCapacidade = new Circle(6);
        dotCapacidade.getStyleClass().add("card-dot");

        lblCapacidadeValor = new Label("30/30");
        lblCapacidadeValor.getStyleClass().add("card-capacity-value");

        lblCapacidadeDesc = new Label("participantes inscritos");
        lblCapacidadeDesc.getStyleClass().add("card-capacity-desc");

        VBox capacityTextBox = new VBox(-2, lblCapacidadeValor, lblCapacidadeDesc);
        capacityTextBox.setAlignment(Pos.CENTER_LEFT);

        capacityInfoBox.getChildren().addAll(dotCapacidade, capacityTextBox);

        btnVer = new Button("Ver");
        btnVer.getStyleClass().add("card-button");
        btnVer.setPrefWidth(120);

        rightPane.getChildren().addAll(capacityInfoBox, btnVer);

        this.getChildren().addAll(leftPane, rightPane);
    }

    /**
     * Configura o card para um evento de dia único.
     * @param data A linha de data/hora completa (ex: "SEX 14, MAR 2025 18:20 - 21:00")
     */
    public void setDataUnica(String data, String horario) {
        this.lblDataLinha1.setText(data);
        this.lblDataLinha2.setText(horario);
        this.lblDataLinha2.setVisible(true);
        this.lblDataLinha2.setManaged(true);
    }

    /**
     * Configura o card para um evento de múltiplos dias.
     * @param dataInicio A linha de data/hora de início
     * @param dataFim A linha de data/hora de fim
     */
    public void setDataMultipla(String dataInicio, String dataFim) {
        this.lblDataLinha1.setText(dataInicio);
        this.lblDataLinha2.setText(dataFim);
        this.lblDataLinha2.setVisible(true);
        this.lblDataLinha2.setManaged(true);
    }

    public void setLblTitulo(String titulo) {this.lblTitulo.setText(titulo);}
    public void setLblLocal(String local) {this.lblLocal.setText(local);}
    public void setLblCapacidadeValor(String ncapa) {this.lblCapacidadeValor.setText(ncapa);}
    public void setLblDataLinha1(String lblDataLinha1) {this.lblDataLinha1.setText(lblDataLinha1);}
    public void setLblDataLinha2(String lblDataLinha2) {this.lblDataLinha2.setText(lblDataLinha2);}

    public Button getBtnVer() {return btnVer;}
}