package com.eventually.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/** PASSÍVEL DE ALTERAÇÃO
 * A classe {@code EventoUSCartao} representa um cartão visual para exibir informações resumidas
 * de um evento na interface do usuário.
 * Este componente, construído como um {@code HBox}, organiza o título, a localização e a
 * data/hora do evento de forma visualmente atraente.
 *
 * @author Yuri Garcia Maia
 * @version 1.01
 * @since 2025-04-06
 * @author Gabriella Tavares Costa Corrêa
 * @since 2025-04-22 (Revisão e documentação da classe)
 */
public class EventoUSCartao extends HBox {
     /**
     * Construtor da classe {@code EventoUSCartao}.
     * Inicializa e configura os elementos visuais do cartão de evento, incluindo: padding e
     * espaçamento interno do cartão, estilo de fundo com um gradiente linear e bordas arredondadas,
     * altura preferencial do cartão, {@code VBox} para o título e a localização do evento e um
     * para exibir a data (dia, mês/ano) e a hora do evento, alinhado à direita.
     * Os rótulos de texto são estilizados com cores brancas e, no caso da data e hora, com negrito.
     */
    public EventoUSCartao() {
        setPadding(new Insets(15));
        setSpacing(20);
        setStyle("-fx-background-color: linear-gradient(to right, #8e2de2, #c84cf4); -fx-background-radius: 20;");
        setPrefHeight(100);

        VBox textBox = new VBox(5);
        Label title = new Label("Título do evento");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: white;");

        Label location = new Label("Local do evento");
        location.setStyle("-fx-text-fill: white;");

        textBox.getChildren().addAll(title, location);

        VBox timeBox = new VBox();
        timeBox.setStyle("-fx-text-fill: white;");
        timeBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        Label day = new Label("SEX 14,");
        Label monthYear = new Label("MAR 2025");
        Label hour = new Label("18:20");

        day.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        monthYear.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        hour.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        timeBox.getChildren().addAll(day, monthYear, hour);

        HBox.setHgrow(textBox, Priority.ALWAYS);
        this.getChildren().addAll(textBox, timeBox);
    }
}
