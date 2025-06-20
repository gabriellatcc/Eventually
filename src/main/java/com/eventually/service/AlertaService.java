package com.eventually.service;

import com.eventually.controller.LoginController;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serviço responsável por exibir mensagens de alerta ao usuário,
 * utilizando os tipos padrão da interface JavaFX.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-05-15
 */
public class AlertaService {

    private static final Logger sistemaDeLog = LoggerFactory.getLogger(LoginController.class);

    /**
     * Exibe um alerta de campo obrigatório vazio e, em caso de falha ao exibir o alerta, uma mensagem é impressa
     * no console.
     * @param nomeCampo o nome do campo que está vazio, exibido na mensagem do alerta.
     */
    public static void alertarCampoVazio(String nomeCampo) {
        try {
            alertarWarn("Campo obrigatório", "O campo " + nomeCampo + " está vazio!");
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao exibir alerta de campo vazio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exibe um alerta do tipo {@link Alert.AlertType#WARNING} com título e mensagem personalizados e, em caso de falha
     * ao exibir o alerta, uma mensagem é impressa no console.
     * @param titulo o título da janela de alerta.
     * @param mensagem a mensagem a ser exibida no corpo do alerta.
     */
    public static void alertarWarn(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        try {
            Image icon = new Image(AlertaService.class.getResourceAsStream("/images/aviso-icone.png"));

            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(48);
            imageView.setFitHeight(48);

            alert.setGraphic(imageView);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(icon);
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao carregar o ícone para o alerta de aviso: " + e.getMessage());
            e.printStackTrace();
        }
        alert.showAndWait();
    }

    /**
     * Exibe um alerta do tipo {@link Alert.AlertType#ERROR} com uma mensagem de erro e, em caso de falha ao exibir o
     * alerta, uma mensagem é impressa no console.
     * @param mensagem a mensagem de erro a ser exibida.
     */
    public static void alertarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        try {
            Image icon = new Image(AlertaService.class.getResourceAsStream("/images/alerta-icone.png"));

            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(48);
            imageView.setFitHeight(48);

            alert.setGraphic(imageView);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(icon);
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao carregar o ícone para o alerta de aviso: " + e.getMessage());
            e.printStackTrace();
        }
        alert.showAndWait();
    }

    /**
     * Exibe um alerta do tipo {@link Alert.AlertType#INFORMATION} com uma mensagem informativa e, em caso de falha ao
     * exibir o alerta, uma mensagem é impressa no console.
     * @param mensagem a mensagem a ser exibida ao usuário.
     */
    public static void alertarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
        try {
            Image icon = new Image(AlertaService.class.getResourceAsStream("/images/info-icone.png"));

            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(48);
            imageView.setFitHeight(48);

            alert.setGraphic(imageView);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(icon);
        } catch (Exception e) {
            sistemaDeLog.error("Erro ao carregar o ícone para o alerta de aviso: " + e.getMessage());
            e.printStackTrace();
        }
    }
}