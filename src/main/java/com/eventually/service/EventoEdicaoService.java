package com.eventually.service;

import com.eventually.dto.EventoEdicaoDto;
import com.eventually.model.EventoModel;
import com.eventually.repository.EventoRepository;

import java.util.Optional;

/**
 * Serviço que fornece a operação de editar (UPDATE do CRUD) para eventos.
 * Esta classe utiliza o {@link EventoRepository} para acessar e manipular os dados dos eventos.
 *  @author Gabriella Tavares Costa Corrêa
 *  @version 1.00
 *  @since 2025-05-18
 */
public class EventoEdicaoService {
    private final EventoRepository eventoRepository;

    /**
     * Construtor da classe {@code EventoEdicaoService}.
     * Inicializa o {@code EventoRepository} a ser utilizado.
     */
    public EventoEdicaoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    /**
     * Edita um evento existente com base no ID e nos novos atributos informados.
     * Apenas os atributos não nulos no {@link EventoEdicaoDto} serão atualizados no evento.
     * Caso o evento não seja encontrado pelo ID, será exibida uma mensagem informativa.
     * @param idEvento ID do evento que será editado.
     * @param atributoEditado Objeto contendo os novos valores para os campos do evento.
     */
    public void editarEvento(int idEvento, EventoEdicaoDto atributoEditado) {
        Optional<EventoModel> eventoParaEditarOptional = eventoRepository.buscarEventoPorId(idEvento);
        eventoParaEditarOptional.ifPresentOrElse(evento -> {
            if (atributoEditado.novoOrganizador() != null) evento.setOrganizador(atributoEditado.novoOrganizador());
            if (atributoEditado.novoNome() != null) evento.setNomeEvento(atributoEditado.novoNome());
            if (atributoEditado.novaFotoEvento() != null) evento.setFotoEvento(atributoEditado.novaFotoEvento());
            if (atributoEditado.novaDescricao() != null) evento.setDescricao(atributoEditado.novaDescricao());
            if (atributoEditado.novoFormato() != null) evento.setFormato(atributoEditado.novoFormato());
            if (atributoEditado.novaLocalizacao() != null) evento.setLocalizacao(atributoEditado.novaLocalizacao());
            if (atributoEditado.novaDataInicial() != null) evento.setDataInicial(atributoEditado.novaDataInicial());
            if (atributoEditado.novaDataFinal() != null) evento.setDataFinal(atributoEditado.novaDataFinal());
            if (atributoEditado.novaQntDePessoas() != null) evento.setQntDePessoasPermitidas(atributoEditado.novaQntDePessoas());
            if (atributoEditado.novaClassificacao() != null) evento.setClassificacao(atributoEditado.novaClassificacao());
            if (atributoEditado.novaCertificacao() != null) evento.setCertificacao(atributoEditado.novaCertificacao());
            System.out.println("Evento com ID " + idEvento + " editado com sucesso.");
        }, () -> System.out.println("Evento com ID " + idEvento + " não encontrado."));
    }
}