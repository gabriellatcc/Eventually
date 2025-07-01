package com.eventually.service;

import com.eventually.model.EventoModel;
import com.eventually.model.FormatoSelecionado;
import com.eventually.view.modal.EditaEventoModal;

import java.util.Optional;

/**
 * Serviço Singleton responsável pela lógica de negócio da edição de eventos.
 * Ele pega os dados da view e os aplica ao objeto de modelo do evento.
 * @version 1.0
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação e revisão da parte lógica da estrutura)
 * @since 2025-07-01
 */
public class EditaEventoService {
    private static EditaEventoService instance;
    private EventoLeituraService eventoLeituraService;

    private EditaEventoService() {
        this.eventoLeituraService=EventoLeituraService.getInstancia();
    }

    public static EditaEventoService getInstance() {
        if (instance == null) {
            instance = new EditaEventoService();
        }
        return instance;
    }

    /**
     * Atualiza um objeto EventoModel com os dados preenchidos no modal de edição.
     * A lógica "Optional" é aplicada aqui: se um campo estiver vazio na view,
     * o dado correspondente no objeto evento não é alterado.
     *
     * @param modal A instância do modal de onde os novos dados serão lidos.
     */
    public void atualizarEvento(int idDoEvento, EditaEventoModal modal) {
        Optional<EventoModel> optionalEvento = eventoLeituraService.procurarEventoPorId(idDoEvento);

        if (optionalEvento.isEmpty()) {
            System.err.println("ERRO CRÍTICO: Tentativa de editar um evento que não existe. ID: " + idDoEvento);
            return;
        }

        EventoModel eventoParaAtualizar = optionalEvento.get();
        System.out.println("Iniciando atualização para o evento real: " + eventoParaAtualizar.getNomeEvento());

        String novoNome = modal.getFldNomeEvento().getText();
        if (novoNome != null && !novoNome.isBlank()) {
            eventoParaAtualizar.setNomeEvento(novoNome);
        }

        String novaDescricao = modal.getTaDescricao().getText();
        if (novaDescricao != null && !novaDescricao.isBlank()) {
            eventoParaAtualizar.setDescricao(novaDescricao);
        }

        String novoLink = modal.getFldLink().getText();
        if (novoLink != null && !novoLink.isBlank()) {
            eventoParaAtualizar.setLinkAcesso(novoLink);
        }

        String novaLocalizacao = modal.getTaLocalizacao().getText();
        if (novaLocalizacao != null && !novaLocalizacao.isBlank()) {
            eventoParaAtualizar.setLocalizacao(novaLocalizacao);
        }

        String novaCapacidadeStr = modal.getFldNParticipantes().getText();
        if (novaCapacidadeStr != null && !novaCapacidadeStr.isBlank()) {
            try {
                int novaCapacidade = Integer.parseInt(novaCapacidadeStr);
                eventoParaAtualizar.setnParticipantes(novaCapacidade);
            } catch (NumberFormatException e) {
                System.err.println("Valor de capacidade inválido: " + novaCapacidadeStr);
            }
        }

        String novoFormato = modal.getFormato();
        if (!novoFormato.isEmpty()) {
            eventoParaAtualizar.setFormato(FormatoSelecionado.valueOf(novoFormato));
        }

        System.out.println("Evento atualizado para: " + eventoParaAtualizar.getNomeEvento());
    }
}