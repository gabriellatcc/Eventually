package com.eventually.service;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;

/** Classe utilizada para obter dimensões da tela e aplicar comportamentos relacionados ao Stage
 * quando instaciada, nela são fornecidos métodos para medir largura e altura visível da tela maximizada
 * e restaurada.
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.01
 * @since 2025-05-16
 */
public class TelaService {

    /**
     * Retorna a largura da área visível da tela primária.
     * @return largura da tela (sem a barra de tarefas)
     */
    public double medirWidth() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        return bounds.getWidth();
    }

    /**
     * Retorna a altura da área visível da tela primária.
     * @return altura da tela (sem a barra de tarefas)
     */
    public double medirHeight() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        return bounds.getHeight();
    }

    /**
     * Aplica um comportamento ao Stage para que o restaurar (processarSaida do modo maximizado),
     * seja redimensionado automaticamente para os valores informados.
     *
     * @param stage o palco a ser monitorado
     * @param largura a largura a ser aplicada ao restaurar
     * @param altura a altura a ser aplicada ao restaurar
     */
    public void aplicarTamanhoRestaurar(Stage stage, double largura, double altura) {
        stage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (!isNowMaximized) {
                stage.setWidth(largura);
                stage.setHeight(altura);

                Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                double posX = bounds.getMinX() + (bounds.getWidth() - largura) / 2;
                double posY = bounds.getMinY() + (bounds.getHeight() - altura) / 2;

                stage.setX(posX);
                stage.setY(posY);
            }
        });
    }
}