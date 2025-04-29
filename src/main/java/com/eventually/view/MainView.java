package com.eventually.view; // Pacote original restaurado

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

// Nome da classe restaurado
public class MainView extends BorderPane {

    // Construtor com nome restaurado
    public MainView() {
        VBox barraLateral = criarBarraLateral();
        HBox barraSuperior = criarBarraSuperior();
        VBox conteudoCentral = criarConteudoCentral();

        setLeft(barraLateral);
        setTop(barraSuperior);
        setCenter(conteudoCentral);
    }

    private VBox criarBarraLateral() {
        VBox barraLateral = new VBox(20);
        barraLateral.setPadding(new Insets(20));
        barraLateral.setStyle("-fx-background-color: #7A2074;");
        barraLateral.setPrefWidth(200);
        barraLateral.setAlignment(Pos.TOP_CENTER);

        Button botaoInicio = new Button("Página inicial");
        botaoInicio.setOnAction(evento -> {
            System.out.println("Botão JavaFX 'Página inicial' clicado! (Lambda)");
        });
        Button botaoMeusEventos = new Button("Meus eventos");
        Button botaoConfiguracoes = new Button("Configurações");

        botaoInicio.getStyleClass().add("menu-button");
        botaoMeusEventos.getStyleClass().add("menu-button");
        botaoConfiguracoes.getStyleClass().add("menu-button");

        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        barraLateral.getChildren().addAll(botaoInicio, botaoMeusEventos, espacador, botaoConfiguracoes);
        return barraLateral;
    }

    private HBox criarBarraSuperior() {
        HBox barraSuperior = new HBox();
        barraSuperior.setPadding(new Insets(20));
        barraSuperior.setAlignment(Pos.CENTER_LEFT);
        String corDeFundoDesejada = "#5F115A";
        barraSuperior.setStyle(
                "-fx-background-color: " + corDeFundoDesejada + "; " +
                        "-fx-border-color: transparent; " +
                        "-fx-border-width: 0 0 1 0;"
        );

        Label rotuloLogo = new Label("Eventualmente");
        rotuloLogo.getStyleClass().add("logo");

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        barraSuperior.getChildren().addAll(rotuloLogo, espacador);
        return barraSuperior;
    }

    private HBox criarBarraDeControles() {
        HBox barraDeControles = new HBox(10);
        barraDeControles.setAlignment(Pos.CENTER_LEFT);
        barraDeControles.setPadding(new Insets(10, 20, 10, 20));
        barraDeControles.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");

        Button botaoProgramacao = new Button("Programação");
        Button botaoMinhaAgenda = new Button("Minha agenda");
        Label rotuloUsuario = new Label("Usuário");
        Circle avatar = new Circle(20, Color.LIGHTGRAY);

        botaoProgramacao.getStyleClass().add("top-button");
        botaoMinhaAgenda.getStyleClass().add("top-button");

        HBox caixaUsuario = new HBox(10, rotuloUsuario, avatar);
        caixaUsuario.setAlignment(Pos.CENTER);

        caixaUsuario.setStyle(
                "-fx-background-color: #EFEFEF; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15;"
        );
        caixaUsuario.setMinWidth(200);
        caixaUsuario.setMinHeight(75);
        caixaUsuario.setMaxWidth(200);
        caixaUsuario.setMaxHeight(75);

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        barraDeControles.getChildren().addAll(espacador, botaoProgramacao, botaoMinhaAgenda, caixaUsuario);

        return barraDeControles;
    }

    private VBox criarAreaDeConteudoPrincipal() {
        VBox areaPrincipal = new VBox(15);
        areaPrincipal.setPadding(new Insets(0, 20, 20, 20));

        HBox caixaBotoesData = new HBox(5);
        caixaBotoesData.setAlignment(Pos.CENTER);
        caixaBotoesData.setPadding(new Insets(10, 0, 15, 0));

        ToggleGroup grupoData = new ToggleGroup();
        String[] diasTexto = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"};

        for (int i = 0; i < diasTexto.length; i++) {
            ToggleButton botaoDia = new ToggleButton(diasTexto[i]);
            botaoDia.getStyleClass().add("date-button");
            botaoDia.setToggleGroup(grupoData);
            if (i == 0) {
                botaoDia.setSelected(true);
            }
            caixaBotoesData.getChildren().add(botaoDia);
        }

        VBox listaEventos = new VBox(15);
        VBox.setVgrow(listaEventos, Priority.ALWAYS);

        areaPrincipal.getChildren().addAll(caixaBotoesData, listaEventos);

        return areaPrincipal;
    }

    private VBox criarConteudoCentral() {
        VBox conteudoCentral = new VBox();

        HBox barraDeControles = criarBarraDeControles();
        VBox areaDeConteudoPrincipal = criarAreaDeConteudoPrincipal();

        conteudoCentral.getChildren().addAll(barraDeControles, areaDeConteudoPrincipal);

        return conteudoCentral;
    }
}