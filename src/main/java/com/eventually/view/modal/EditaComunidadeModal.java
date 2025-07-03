package com.eventually.view.modal;

import com.eventually.controller.EditaComunidadeController;
import com.eventually.model.Comunidade;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;

/**
 * @version 1.02
 * @author Gabriella Tavares Costa Corrêa (Criação, Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-07-01
 */
public class EditaComunidadeModal extends Parent {
    private EditaComunidadeController controller;

    private final Map<Comunidade, CheckBox> mapaDeCheckBoxes = new EnumMap<>(Comunidade.class);
    private Button btnSalvar;
    private Button btnCancelar;

    public EditaComunidadeModal() {
        setupUI();
    }

    private void setupUI() {
        VBox layout = criarLayoutPrincipal();
        this.getChildren().add(layout);
    }

    public void setEditaComunidadesController(EditaComunidadeController controller) {
        this.controller = controller;
    }

    private VBox criarLayoutPrincipal() {
        final double MODAL_WIDTH = 400;
        final double MODAL_HEIGHT = 420;

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPrefSize(MODAL_WIDTH, MODAL_HEIGHT);
        layout.setPadding(new Insets(5, 20, 10, 20));
        layout.getStyleClass().add("layout-pane");

        Rectangle rect = new Rectangle(MODAL_WIDTH, MODAL_HEIGHT);
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        layout.setClip(rect);

        Label lblTitulo = new Label("Selecione as comunidades dos eventos que deseja ver");
        lblTitulo.setWrapText(true);
        lblTitulo.setStyle(
                "-fx-font-family: 'Poppins Bold';" +
                        "-fx-font-size: 24px;" +
                        "-fx-pref-height: 80;"+
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #7A2074;"
        );

        VBox containerCheckBoxes = new VBox(10);
        containerCheckBoxes.setAlignment(Pos.CENTER_LEFT);
        containerCheckBoxes.setPadding(new Insets(0, 0, 5, 50));

        for (Comunidade comum : Comunidade.values()) {
            CheckBox cb = new CheckBox(formatarNomeComum(comum.name()));
            cb.getStyleClass().add("purple-checkbox");
            mapaDeCheckBoxes.put(comum, cb);
            containerCheckBoxes.getChildren().add(cb);
        }

        btnSalvar = new Button("Salvar Alterações");
        btnSalvar.getStyleClass().add("modal-interact-button");

        btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("modal-interact-button");

        HBox containerBotoes = new HBox(20, btnSalvar, btnCancelar);
        containerBotoes.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(lblTitulo, containerCheckBoxes, containerBotoes);
        return layout;
    }

    private String formatarNomeComum(String nomeEnum) {
        if (nomeEnum == null || nomeEnum.isEmpty()) return "";
        return nomeEnum.charAt(0) + nomeEnum.substring(1).toLowerCase();
    }

    public void close() {
        Stage stage = (Stage) this.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    public Map<Comunidade, CheckBox> getMapaDeCheckBoxes() {return mapaDeCheckBoxes;}
    public Button getBtnSalvar() {return btnSalvar;}
    public Button getBtnCancelar() {return btnCancelar;}
}