package com.eventually.controller;

import com.eventually.model.TemaPreferencia;
import com.eventually.service.UsuarioAtualizacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.ModalEditaTemas;
import javafx.scene.control.CheckBox;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @version 1.01
 * @author Gabriella Tavares Costa Corrêa (Criação, Documentação, correção e revisão da parte lógica da estrutura da classe)
 * @since 2025-07-01
 */
public class EditaTemasController {
    private final ModalEditaTemas view;
    private UsuarioAtualizacaoService atualizacaoService;
    private UsuarioSessaoService sessaoService;
    private final String emailRecebido;

    public EditaTemasController(ModalEditaTemas view, String emailRecebido) {
        this.atualizacaoService = UsuarioAtualizacaoService.getInstancia();
        this.sessaoService = UsuarioSessaoService.getInstancia();

        this.view = view;
        this.view.setEditaTemasController(this);

        this.emailRecebido = emailRecebido;

        initialize();
    }

    private void initialize() {
        carregarPreferenciasAtuais();

        view.getBtnSalvar().setOnAction(e -> salvarAlteracoes());
        view.getBtnCancelar().setOnAction(e -> view.close());
    }

    private void carregarPreferenciasAtuais() {
        Set<TemaPreferencia> temasAtuais = sessaoService.procurarPreferencias(emailRecebido);
        Map<TemaPreferencia, CheckBox> mapaCheckBoxes = view.getMapaDeCheckBoxes();

        for (TemaPreferencia tema : temasAtuais) {
            if (mapaCheckBoxes.containsKey(tema)) {
                mapaCheckBoxes.get(tema).setSelected(true);
            }
        }
    }

    private void salvarAlteracoes() {
        int id = sessaoService.procurarID(emailRecebido);
        Set<TemaPreferencia> novosTemas = new HashSet<>();
        Map<TemaPreferencia, CheckBox> mapaCheckBoxes = view.getMapaDeCheckBoxes();

        for (Map.Entry<TemaPreferencia, CheckBox> entry : mapaCheckBoxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                novosTemas.add(entry.getKey());
            }
        }

        boolean sucesso = atualizacaoService.atualizarTemas(id, novosTemas);

        if (sucesso) {
            view.close();
        }
    }
}