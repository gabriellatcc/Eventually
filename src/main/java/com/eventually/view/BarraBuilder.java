package com.eventually.view;

import com.eventually.service.TelaService;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Classe construtora de barra superior e lateral do sistema.
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.02
 * @since 2025-06-24
 */
public class BarraBuilder {
    private Button btnInicio;
    private Button btnMeusEventos;
    private Button btnConfiguracoes;
    private Button btnSair;
    private Button btnProgramacao;

    private final TelaService telaService;


    public BarraBuilder() {
        this.telaService = new TelaService();

    }
    /**
     * Este método cria a classe do container da barra superior com o nome
     * do programa.
     * @return a barra superior
     */
    public HBox construirBarraSuperior() {
        HBox barraSuperior = new HBox();
        barraSuperior.setPadding(new Insets(20));
        barraSuperior.setAlignment(Pos.CENTER);
        barraSuperior.getStyleClass().add("topbar");

        Image logoImagem = new Image(getClass().getResourceAsStream("/images/eventually-logo.png"));
        ImageView logoView = new ImageView(logoImagem);

        logoView.setFitHeight(50);

        logoView.setPreserveRatio(true);

        barraSuperior.getChildren().add(logoView);
        return barraSuperior;
    }

    /**
     * Este método construirBarraLateral() cria uma barra lateral de navegação vertical da interface.
     * Essa barra aparece na lateral esquerda da tela e contém botões para acessar
     * diferentes seções do aplicativo:
     * Página Inicial, Meus eventos, Programação, Configurações e saída.
     * @return a barra lateral.
     */
    public VBox construirBarraLateral() {
        VBox barraLateral = new VBox(20);
        barraLateral.setPadding(new Insets(20));
        barraLateral.getStyleClass().add("sidebar");
        barraLateral.setPrefWidth(200);
        barraLateral.setPrefHeight(telaService.medirHeight());
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
     * Métodos de encapsulamento getters
     */
    public Button getBtnInicio() {return btnInicio;}
    public Button getBtnMeusEventos() {return btnMeusEventos;}
    public Button getBtnConfiguracoes() {return btnConfiguracoes;}
    public Button getBtnSair() {return btnSair;}
    public Button getBtnProgramacao() {return btnProgramacao;}
}