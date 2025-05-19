package com.eventually.service;
//verificada e com dependencia
import com.eventually.model.EventoModel;
import com.eventually.repository.EventoRepository;

import java.util.List;

/**
 * Serviço que fornece a operação de criar (CREATE do CRUD) para eventos.
 * Esta classe utiliza o {@link EventoRepository} para acessar e manipular os dados dos eventos.
 *  @author Gabriella Tavares Costa Corrêa
 *  @version 1.02
 *  @since 2025-04-22
 */
public class EventoCriacaoService {
    private final EventoRepository eventoRepository;

    /**
     * Construtor da classe {@code EventoCriacaoService}.
     * Inicializa o {@code EventoRepository} a ser utilizado.
     */
    public EventoCriacaoService() {
        this.eventoRepository = new EventoRepository();
    }

    /**
     * Cria um novo evento e o adiciona ao repositório.
     * Este método representa a operação de criação (Create) do CRUD.
     * @param evento O objeto {@code EventoModel} contendo as informações do evento a ser criado.
     */
    public void criarEvento(EventoModel evento)
    {
        eventoRepository.adicionarEvento(evento);
        System.out.println("\n Evento criado com sucesso!");
        System.out.println("Endereço de memória: "+System.identityHashCode(evento));
        System.out.println(evento);
    }

    //será chamado pelos objetos de postagem da pagina inicial
    /**
     * Lê e exibe todos os eventos cadastrados no sistema.
     * Este método representa a operação de leitura (Read) do CRUD.
     */
    public void exibitEventos() {
        List<EventoModel> eventos = eventoRepository.getAllEventos();
        if(eventos.isEmpty())
        {
            System.out.println("Nenhum evento encontrado.");
            return;
        }
        for(EventoModel evento : eventos)
        {
            System.out.println("---- Evento ----");
            System.out.println("Nome: " + evento.getNomeEvento());
            System.out.println("Organizador: " + evento.getOrganizador());
            System.out.println("Descrição: " + evento.getDescricao());
            System.out.println("Custo: " + evento.getFormato());
            System.out.println("Localização: " + evento.getLocalizacao());
            System.out.println("Data inicial: " + evento.getDataInicial());
            System.out.println("Data final: " + evento.getDataFinal());
            System.out.println("Quantidade de pessoas: " + evento.getQntDePessoasPermitidas());
            System.out.println("Classificação: " + evento.getClassificacao());
            System.out.println("Certificação: " + (evento.isCertificacao() ? "Sim" : "Não"));
            System.out.println("Local na memória: " + evento);
            System.out.println("----------------\n");
        }
    }
}