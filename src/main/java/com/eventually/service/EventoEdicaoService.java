package com.eventually.service;

import com.eventually.dto.EventoEdicaoDto;
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
 * @version 1.05
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação e revisão da parte lógica da estrutura)
 * @since 2025-07-01
 */
public class EventoEdicaoService {
    private static EventoEdicaoService instance;

    private EventoLeituraService eventoLeituraService;
    private UsuarioSessaoService usuarioSessaoService;

    private EventoEdicaoService() {
        this.usuarioSessaoService=UsuarioSessaoService.getInstancia();
        this.eventoLeituraService=EventoLeituraService.getInstancia();
    }

    private static final Logger logger = LoggerFactory.getLogger(EventoEdicaoService.class);

    public static EventoEdicaoService getInstance() {
        if (instance == null) {
            instance = new EventoEdicaoService();
        }
        return instance;
    }

    /**
     * Atualiza um objeto EventoModel com os dados preenchidos no modal de edição.
     * A lógica "Optional" é aplicada aqui: se um campo estiver vazio na view,
     * o dado correspondente no objeto evento não é alterado.
     */
    public void atualizarEvento(int idDoEvento, EventoEdicaoDto dto) {
        Optional<EventoModel> optionalEvento = eventoLeituraService.procurarEventoPorId(idDoEvento);

        if (optionalEvento.isEmpty()) {
            System.err.println("ERRO CRÍTICO: Tentativa de editar um evento que não existe. ID: " + idDoEvento);
            return;
        }

        EventoModel eventoParaAtualizar = optionalEvento.get();
        System.out.println("Atualizando o evento: " + eventoParaAtualizar.getNome());

        if (dto.nome() != null && !dto.nome().isBlank()) {
            eventoParaAtualizar.setNome(dto.nome());
        }
        if (dto.descricao() != null && !dto.descricao().isBlank()) {
            eventoParaAtualizar.setDescricao(dto.descricao());
        }
        if (dto.localizacao() != null && !dto.localizacao().isBlank()) {
            eventoParaAtualizar.setLocalizacao(dto.localizacao());
        }
        if (dto.link() != null && !dto.link().isBlank()) {
            eventoParaAtualizar.setLinkAcesso(dto.link());
        }

        if (dto.novaImagem() != null) {
            eventoParaAtualizar.setFoto(dto.novaImagem());
        }

        if (dto.comunidades() != null && !dto.comunidades().isEmpty()) {
            eventoParaAtualizar.setComunidades(dto.comunidades());
        }
        eventoParaAtualizar.setnParticipantes(dto.capacidade());
        eventoParaAtualizar.setFormato(dto.formato());
        eventoParaAtualizar.setDataInicial(dto.dataInicio());
        eventoParaAtualizar.setHoraInicial(dto.horaInicio());
        eventoParaAtualizar.setDataFinal(dto.dataFim());
        eventoParaAtualizar.setHoraFinal(dto.horaFim());

        System.out.println("Evento atualizado com sucesso para: " + eventoParaAtualizar.getNome());
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