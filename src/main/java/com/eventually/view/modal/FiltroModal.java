package com.eventually.view.modal;

import com.eventually.controller.FiltroController;
import com.eventually.model.Comunidade;
import com.eventually.model.FormatoSelecionado;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * @version 1.00
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-07-02
 */
public class FiltroModal extends Parent {
    private FiltroController controller;

    private final Map<Comunidade, CheckBox> mapaDeComunidades = new EnumMap<>(Comunidade.class);
    private final ToggleGroup formatoGroup = new ToggleGroup();
    private RadioButton radioTodos, radioPresencial, radioOnline, radioHibrido;
    private Button btnAplicar, btnCancelar;

    public FiltroModal() {
        VBox layout = criarLayoutPrincipal();
        this.getChildren().add(layout);
    }

    public void setController(FiltroController controller) {
        this.controller = controller;
    }

    private VBox criarLayoutPrincipal() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPrefWidth(450);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("layout-pane");

        Label lblTitulo = new Label("Filtrar Eventos");
        lblTitulo.getStyleClass().add("title-label-modal");

        TitledPane paneComunidades = new TitledPane("Por Comunidade", criarAreaComunidades());
        paneComunidades.setCollapsible(false);

        TitledPane paneFormatos = new TitledPane("Por Formato", criarAreaFormatos());
        paneFormatos.getStyleClass().add("filtro-titled-pane");
        paneFormatos.setCollapsible(false);

        HBox areaBotoes = criarAreaDeBotoes();

        layout.getChildren().addAll(lblTitulo, paneComunidades, paneFormatos, areaBotoes);
        return layout;
    }

    private VBox criarAreaComunidades() {
        VBox containerCheckBoxes = new VBox(10);
        containerCheckBoxes.setAlignment(Pos.CENTER_LEFT);
        containerCheckBoxes.setPadding(new Insets(15, 15, 15, 25));

        for (Comunidade comum : Comunidade.values()) {
            CheckBox cb = new CheckBox(formatarNome(comum.name()));
            cb.getStyleClass().add("purple-checkbox");
            mapaDeComunidades.put(comum, cb);
            containerCheckBoxes.getChildren().add(cb);
        }
        return containerCheckBoxes;
    }

    private VBox criarAreaFormatos() {
        VBox containerRadios = new VBox(10);
        containerRadios.setAlignment(Pos.CENTER_LEFT);
        containerRadios.setPadding(new Insets(15, 15, 15, 25));

        radioTodos = new RadioButton("Todos");
        radioTodos.setToggleGroup(formatoGroup);
        radioTodos.getStyleClass().add("filter-radio-button");
        radioTodos.setUserData(null);

        radioPresencial = new RadioButton("Presencial");
        radioPresencial.setToggleGroup(formatoGroup);
        radioPresencial.getStyleClass().add("filter-radio-button");
        radioPresencial.setUserData(FormatoSelecionado.PRESENCIAL);

        radioOnline = new RadioButton("Online");
        radioOnline.setToggleGroup(formatoGroup);
        radioOnline.getStyleClass().add("filter-radio-button");
        radioOnline.setUserData(FormatoSelecionado.ONLINE);

        radioHibrido = new RadioButton("Híbrido");
        radioHibrido.setToggleGroup(formatoGroup);
        radioHibrido.getStyleClass().add("filter-radio-button");
        radioHibrido.setUserData(FormatoSelecionado.HIBRIDO);

        containerRadios.getChildren().addAll(radioTodos, radioPresencial, radioOnline, radioHibrido);
        return containerRadios;
    }

    private HBox criarAreaDeBotoes() {
        btnAplicar = new Button("Aplicar Filtros");
        btnAplicar.getStyleClass().add("modal-interact-button");

        btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("modal-interact-button");

        HBox hbBotoes = new HBox(20, btnAplicar, btnCancelar);
        hbBotoes.setAlignment(Pos.CENTER);
        hbBotoes.setPadding(new Insets(15, 0, 0, 0));
        return hbBotoes;
    }

    private String formatarNome(String nomeEnum) {
        return nomeEnum.charAt(0) + nomeEnum.substring(1).toLowerCase();
    }

    public Map<Comunidade, CheckBox> getMapaDeComunidades() { return mapaDeComunidades; }
    public ToggleGroup getFormatoGroup() { return formatoGroup; }
    public Button getBtnAplicar() { return btnAplicar; }
    public Button getBtnCancelar() { return btnCancelar; }
}