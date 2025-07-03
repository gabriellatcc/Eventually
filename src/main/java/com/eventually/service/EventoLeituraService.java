package com.eventually.service;

import com.eventually.model.*;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Esta classe é um Singleton e atua como a camada de leitura (READ) para a entidade EventoH.
 * É responsável por buscar eventos e seus atributos específicos através de um ID.
 * A classe acessa a coleção de eventos diretamente através do {@link EventoCriacaoService}.
 * @author Gabriella Tavares Costa Corrêa (Adaptação baseada na UsuarioSessaoService)
 * @version 1.02
 * @since 2025-06-27
 */
public final class EventoLeituraService {
    private static EventoLeituraService instancia;
    private final EventoCriacaoService eventoCriacaoService;

    private final AlertaService alertaService = new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(EventoLeituraService.class);

    /**
     * Construtor privado que obtém a instância única de EventoCriacaoService para acessar a lista de eventos.
     */
    private EventoLeituraService() {
        this.eventoCriacaoService = EventoCriacaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao EventoCriacaoService.");
    }

    /**
     * Retorna a instância única de {@code EventoLeituraService}.
     * @return a instância única de {@code EventoLeituraService}.
     */
    public static synchronized EventoLeituraService getInstancia() {
        sistemaDeLogger.info("Método getInstancia() para EventoLeituraService chamado.");
        try {
            if (instancia == null) {
                instancia = new EventoLeituraService();
            }
            return instancia;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao retornar a instância de EventoLeituraService: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca um evento completo pelo seu ID. Este é o método base para os outros.
     * @param id o ID do evento a ser procurado.
     * @return um Optional contendo o EventoModel se encontrado, ou um Optional vazio caso contrário.
     */
    public Optional<EventoModel> procurarEventoPorId(int id) {
        try {
            return eventoCriacaoService.buscarEventoPorId(id);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao buscar evento por ID: " + id + " - " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Procura o nome de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return o nome do evento, ou {@code null} se não for encontrado ou em caso de erro.
     */
    public String procurarNomeEvento(int id) {
        try {
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getNome();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar o nome do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar o nome do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura a descrição de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return a descrição do evento, ou {@code null} se não for encontrado.
     */
    public String procurarDescricao(int id) {
        try {
            Optional<EventoModel> evento = procurarEventoPorId(id);
            if (evento.isPresent()) {
                return evento.get().getDescricao();
            }
            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
                sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar a descrição do evento com ID: " + id, e);
                alertaService.alertarErro("Ocorreu um erro ao buscar a descrição do evento.");
                e.printStackTrace();
                return null;
        }
    }

    /**
     * Procura o organizador (usuário) de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return o UsuarioModel do organizador, ou {@code null} se não for encontrado.
     */
    public UsuarioModel procurarOrganizador(int id) {
        try {
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getOrganizador();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar o organizador do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar o organizador do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura o formato de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return o FormatoSelecionado do evento, ou {@code null} se não for encontrado.
     */
    public FormatoSelecionado procurarFormato(int id) {
        try {
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getFormato();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar o formato do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar o formato do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura o link de acesso de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return o link de acesso do evento, ou {@code null} se não for encontrado.
     */
    public String procurarLinkAcesso(int id) {
        try {
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getLinkAcesso();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar o Link do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar o Link do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura a localização de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return a localização do evento, ou {@code null} se não for encontrado.
     */
    public String procurarLocalizacao(int id) {
        try {
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getLocalizacao();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar a Localização do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar a Localização do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura a foto de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return a Image do evento, ou {@code null} se não for encontrada.
     */
    public Image procurarFotoEvento(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getFoto();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar a foto do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar a foto do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura o número máximo de participantes de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return o número de participantes, ou {@code 0} se não for encontrado.
     */
    public int procurarNumeroMaxParticipantes(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getnParticipantes();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return 0;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar o número de participantes do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar o número de participantes do evento.");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Procura a data inicial de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return a data inicial do evento, ou {@code null} se não for encontrada.
     */
    public LocalDate procurarDataInicial(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getDataInicial();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar a data incial do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar a data inicial do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura a hora inicial de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return a hora inicial do evento, ou {@code null} se não for encontrada.
     */
    public LocalTime procurarHoraInicial(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getHoraInicial();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar a hora incial do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar a hora inicial do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura a data final de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return a data final do evento, ou {@code null} se não for encontrada.
     */
    public LocalDate procurarDataFinal(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getDataFinal();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar a data final do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar a data final do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura a hora final de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return a hora final do evento, ou {@code null} se não for encontrada.
     */
    public LocalTime procurarHoraFinal(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getHoraFinal();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar a hora final do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar a hora final do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura os temas de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return um Set com os temas do evento, ou {@code null} se não for encontrado.
     */
    public Set<Comunidade> procurarTemas(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getComunidades();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar temas do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar temas do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura a lista de participantes de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return uma List com os usuários participantes, ou {@code null} se não for encontrado.
     */
    public List<UsuarioModel> procurarParticipantes(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().getParticipantes();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return null;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar os participantes do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar os participantes do evento.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura o estado (ativo/inativo) de um evento dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return {@code true} se o evento está ativo, {@code false} se inativo ou não encontrado.
     */
    public boolean procurarEstado(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().isEstado();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return false;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar o estado do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar o estado do evento.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Procura se um evento está finalizado, dado o seu ID e, em caso de erro, é exibida uma mensagem no console.
     * @param id o ID do evento.
     * @return {@code true} se o evento está finalizado, {@code false} se não estiver ou não for encontrado.
     */
    public boolean isFinalizado(int id) {
        try{
            Optional<EventoModel> evento = procurarEventoPorId(id);

            if (evento.isPresent()) {return evento.get().isFinalizado();}

            alertaService.alertarErro("EventoH com o ID informado não foi encontrado.");
            return false;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro inesperado ao procurar estado de finalização do evento com ID: " + id, e);
            alertaService.alertarErro("Ocorreu um erro ao buscar estado de finalização do evento.");
            e.printStackTrace();
            return false;
        }
    }
}