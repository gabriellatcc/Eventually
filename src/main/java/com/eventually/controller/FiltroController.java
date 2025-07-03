package com.eventually.controller;

import com.eventually.dto.FiltroDto;
import com.eventually.model.Comunidade;
import com.eventually.model.FormatoSelecionado;
import com.eventually.view.modal.FiltroModal;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @version 1.00
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-07-03
 */
public class FiltroController {
    private final FiltroModal view;
    private final Consumer<FiltroDto> onAplicarCallback;

    public FiltroController(FiltroModal view, FiltroDto filtroInicial, Consumer<FiltroDto> onAplicarCallback) {
        this.view = view;
        this.onAplicarCallback = onAplicarCallback;
        initialize(filtroInicial);
    }

    private void initialize(FiltroDto filtroInicial) {
        carregarFiltrosAtuais(filtroInicial);
        view.getBtnAplicar().setOnAction(e -> aplicarFiltros());
        view.getBtnCancelar().setOnAction(e -> ((Stage) view.getScene().getWindow()).close());
    }

    private void carregarFiltrosAtuais(FiltroDto filtroAtual) {
        for (Comunidade comunidade : filtroAtual.comunidades()) {
            if (view.getMapaDeComunidades().containsKey(comunidade)) {
                view.getMapaDeComunidades().get(comunidade).setSelected(true);
            }
        }

        Optional<FormatoSelecionado> formatoAtual = filtroAtual.formato();
        for (Toggle toggle : view.getFormatoGroup().getToggles()) {
            if (formatoAtual.isPresent() && toggle.getUserData() == formatoAtual.get()) {
                toggle.setSelected(true);
                return;
            }
        }
        view.getFormatoGroup().getToggles().get(0).setSelected(true);
    }

    private void aplicarFiltros() {
        Set<Comunidade> comunidadesSelecionadas = new HashSet<>();
        for (Map.Entry<Comunidade, CheckBox> entry : view.getMapaDeComunidades().entrySet()) {
            if (entry.getValue().isSelected()) {
                comunidadesSelecionadas.add(entry.getKey());
            }
        }

        Toggle formatoSelecionadoToggle = view.getFormatoGroup().getSelectedToggle();
        Optional<FormatoSelecionado> formatoSelecionado = Optional.ofNullable(
                (FormatoSelecionado) formatoSelecionadoToggle.getUserData()
        );

        FiltroDto novosFiltros = new FiltroDto(comunidadesSelecionadas, formatoSelecionado);

        onAplicarCallback.accept(novosFiltros);

        ((Stage) view.getScene().getWindow()).close();
    }
}