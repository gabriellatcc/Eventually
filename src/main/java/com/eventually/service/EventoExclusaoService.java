package com.eventually.service;

import com.eventually.model.EventoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Optional;

/**
 * Esta classe é um Singleton, garantindo que apenas uma instância de {@code EventoExclusaoService} exista em toda a aplicação.
 * Serviço que fornece a operação de exclusão (DELETE do CRUD) para eventos, ele altera o estado do evento para false.
 * Esta classe utiliza a instância única de {@link EventoCriacaoService} para acessar e manipular os dados
 * dos eventos em memória.
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.00
 * @since 2025-05-18
 */

public class EventoExclusaoService {
    private static EventoExclusaoService instancia;
    private EventoCriacaoService eventoCriacaoService;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(EventoExclusaoService.class);

    /**
     * Construtor privado que obtém a instância única de EventoCriacaoService para acessar a lista de eventos e
     * immpede a criação de múltiplas instâncias externamente.
     */
    private EventoExclusaoService() {
        this.eventoCriacaoService = EventoCriacaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao EventoCriacaoService.");
    }

    /**
     * Retorna a instância única de {@code EventoExclusaoService}, se não existe, ela é criada e, em caso de falha, é
     * exibida uma mensagem no console.
     * @return a instância única de {@code EventoExclusaoService}.
     */
    public static synchronized EventoExclusaoService getInstancia() {
        sistemaDeLogger.info("Método EventoExclusaoService() chamado.");
        try{
            if (instancia == null) {
                instancia = new EventoExclusaoService();
            }
            return instancia;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao retornar a instância: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Altera o estado de um evento (ativo/inativo) e, em caso de falha na atualização de estado do evento, é exibida
     * uma mensagem apontando o erro no console.
     * @param idEvento o ID do evento cujo estado será alterado.
     * @param novoEstado o novo estado (true para ativo, false para inativo).
     * @return {@code true} se o estado foi alterado com sucesso, {@code false} caso contrário.
     */
    public boolean alterarEstadoDoEvento(int idEvento, boolean novoEstado) {
        sistemaDeLogger.info("Método alterarEstadoDoEvento() chamado.");
        try {
            Optional<EventoModel> eventoModel = eventoCriacaoService.buscarEventoPorId(idEvento);

            if (eventoModel.isPresent()) {
                EventoModel evento = eventoModel.get();
                evento.setEstadoDoEvento(novoEstado);
                sistemaDeLogger.info("Estado do evento com ID " + idEvento + " alterado para " + (novoEstado ? "ATIVO" : "INATIVO") + ".");
                alertaService.alertarInfo("Sucesso: Estado do evento alterado!");
                return true;
            } else {
                alertaService.alertarWarn("Alteração de Estado Inválida", "Evento com ID " + idEvento + " não encontrado.");
                sistemaDeLogger.info("Evento com ID " + idEvento + " não encontrado para alterar estado.");
                return false;
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Erro inesperado ao alterar estado do evento: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao alterar estado do evento.");
            return false;
        }
    }
}