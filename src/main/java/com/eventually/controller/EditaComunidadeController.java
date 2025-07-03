package com.eventually.controller;

import com.eventually.model.Comunidade;
import com.eventually.service.UsuarioAtualizacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.modal.EditaComunidadeModal;
import javafx.scene.control.CheckBox;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @version 1.02
 * @author Gabriella Tavares Costa Corrêa (Criação, Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-07-01
 */
public class EditaComunidadeController {
    private final EditaComunidadeModal view;
    private UsuarioAtualizacaoService atualizacaoService;
    private UsuarioSessaoService sessaoService;
    private final String emailRecebido;

    public EditaComunidadeController(EditaComunidadeModal view, String emailRecebido) {
        this.atualizacaoService = UsuarioAtualizacaoService.getInstancia();
        this.sessaoService = UsuarioSessaoService.getInstancia();

        this.view = view;
        this.view.setEditaComunidadesController(this);

        this.emailRecebido = emailRecebido;

        initialize();
    }

    private void initialize() {
        carregarPreferenciasAtuais();

        view.getBtnSalvar().setOnAction(e -> salvarAlteracoes());
        view.getBtnCancelar().setOnAction(e -> view.close());
    }

    private void carregarPreferenciasAtuais() {
        Set<Comunidade> comumAtuais = sessaoService.procurarPreferencias(emailRecebido);
        Map<Comunidade, CheckBox> mapaCheckBoxes = view.getMapaDeCheckBoxes();

        for (Comunidade comunidade : comumAtuais) {
            if (mapaCheckBoxes.containsKey(comunidade)) {
                mapaCheckBoxes.get(comunidade).setSelected(true);
            }
        }
    }

    private void salvarAlteracoes() {
        int id = sessaoService.procurarID(emailRecebido);
        Set<Comunidade> novasComunidades = new HashSet<>();
        Map<Comunidade, CheckBox> mapaCheckBoxes = view.getMapaDeCheckBoxes();

        for (Map.Entry<Comunidade, CheckBox> entry : mapaCheckBoxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                novasComunidades.add(entry.getKey());
            }
        }

        boolean sucesso = atualizacaoService.atualizarTemas(id, novasComunidades);

        if (sucesso) {
            view.close();
        }
    }
}