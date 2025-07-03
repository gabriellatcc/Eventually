package com.eventually.service;

import com.eventually.model.EventoModel;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.UsuarioModel;
import com.eventually.view.HomeView;
import com.eventually.view.modal.EditaEventoModal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Serviço Singleton responsável pela lógica de negócio da edição de eventos.
 * Ele pega os dados da view e os aplica ao objeto de modelo do evento.
 * @version 1.03
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação e revisão da parte lógica da estrutura)
 * @since 2025-07-01
 */
public class EditaEventoService {
    private static EditaEventoService instance;

    private EventoLeituraService eventoLeituraService;
    private UsuarioSessaoService usuarioSessaoService;

    private EditaEventoService() {
        this.usuarioSessaoService=UsuarioSessaoService.getInstancia();
        this.eventoLeituraService=EventoLeituraService.getInstancia();
    }

    private static final Logger logger = LoggerFactory.getLogger(EditaEventoService.class);

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
        System.out.println("Iniciando atualização para o evento real: " + eventoParaAtualizar.getNome());

        String novoNome = modal.getFldNomeEvento().getText();
        if (novoNome != null && !novoNome.isBlank()) {
            eventoParaAtualizar.setNome(novoNome);
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

        System.out.println("Evento atualizado para: " + eventoParaAtualizar.getNome());
    }

    public boolean adicionarParticipante(HomeView.EventoH eventoH, String email) {
        logger.info("Tentando adicionar usuário '{}' ao evento ID '{}'", email, eventoH.id());

        Optional<EventoModel> optionalEvento = eventoLeituraService.procurarEventoPorId(eventoH.id());

        Optional<UsuarioModel> optionalUsuario = Optional.ofNullable(usuarioSessaoService.procurarUsuario(email));

        if (optionalEvento.isPresent() && optionalUsuario.isPresent()) {
            EventoModel evento = optionalEvento.get();
            UsuarioModel usuario = optionalUsuario.get();

            List<UsuarioModel> participantes = evento.getParticipantes();

            if (participantes.contains(usuario)) {
                logger.warn("Usuário '{}' já está inscrito no evento '{}'. Operação cancelada.", email, evento.getNome());
                return false;
            }

            participantes.add(usuario);
            logger.info("Usuário '{}' adicionado com sucesso à lista de participantes do evento '{}'.", usuario.getNome(), evento.getNome());

            return true;
        }

        return false;
    }

    /**
     * Remove um participante da lista de um evento específico.
     * Este método atualiza a lista de participantes do próprio evento.
     * @param eventoH o evento (no formato record/DTO) que perderá um participante.
     * @param email o email do participante a ser removido.
     */
    public void removerParticipante(HomeView.EventoH eventoH, String email) {
        Optional<EventoModel> eventoOptional = eventoLeituraService.procurarEventoPorId(eventoH.id());

        if (eventoOptional.isPresent()) {
            EventoModel eventoEncontrado = eventoOptional.get();

            List<UsuarioModel> participantes = eventoEncontrado.getParticipantes();

            boolean removido = participantes.removeIf(participante -> participante.getEmail().equals(email));

            if (removido) {
                System.out.println("Participante " + email + " removido do evento " + eventoEncontrado.getNome());
            }
        } else {
            System.out.println("Erro: Evento ID " + eventoH.id() + " não encontrado para remover participante.");
        }
    }
}