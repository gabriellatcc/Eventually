package com.eventually.service;

import javafx.scene.control.Alert;

/**
 * Serviço responsável por exibir mensagens de alerta ao usuário,
 * utilizando os tipos padrão da interface JavaFX.
 *
 * @author Gabriella Tavares
 * @version 1.0
 * @since 2025-05-15
 */
public class AlertService {

    /**
     * Exibe um alerta de campo obrigatório vazio.
     *
     * @param nomeCampo o nome do campo que está vazio, exibido na mensagem do alerta.
     */
    public static void alertarCampoVazio(String nomeCampo) {
        alertarWarn("Campo obrigatório", "O campo " + nomeCampo + " está vazio!");
    }

    /**
     * Exibe um alerta do tipo {@link Alert.AlertType#WARNING} com título e mensagem personalizados.
     *
     * @param titulo   o título da janela de alerta.
     * @param mensagem a mensagem a ser exibida no corpo do alerta.
     */
    public static void alertarWarn(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta do tipo {@link Alert.AlertType#ERROR} com uma mensagem de erro.
     *
     * @param mensagem a mensagem de erro a ser exibida.
     */
    public static void alertarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta do tipo {@link Alert.AlertType#INFORMATION} com uma mensagem informativa.
     *
     * @param mensagem a mensagem a ser exibida ao usuário.
     */
    public static void alertarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}