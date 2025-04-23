package com.eventually.service;

import com.eventually.model.EventoModel;
import com.eventually.repository.EventoRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Serviço que fornece operações de CRUD (Criar, Ler, Atualizar, Deletar) para eventos.
 * Esta classe utiliza o {@link EventoRepository} para acessar e manipular os dados dos eventos.
 *  @author Gabriella Tavares Costa Corrêa
 *  @version 1.01
 *  @since 2025-04-22
 */
public class EventoService {
    private final EventoRepository eventoRepository;

    /**
     * Construtor da classe {@code EventoService}.
     * Inicializa o {@code EventoRepository} a ser utilizado.
     */
    public EventoService() {
        this.eventoRepository = new EventoRepository();
    }

    /**
     * Retorna a lista de todos os eventos cadastrados.
     *
     * @return Lista de objetos {@code EventoModel} contendo todos os eventos registrados.
     */
    public List<EventoModel> listarEventos() {
        return eventoRepository.getAllEventos();
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
            System.out.println("Formato: " + evento.getFormato());
            System.out.println("Custo: " + evento.getCusto());
            System.out.println("Localização: " + evento.getLocalizacao());
            System.out.println("Data inicial: " + evento.getDataInicial());
            System.out.println("Data final: " + evento.getDataFinal());
            System.out.println("Quantidade de pessoas: " + evento.getQntDePessoas());
            System.out.println("Classificação: " + evento.getClassificacao());
            System.out.println("Certificação: " + (evento.isCertificacao() ? "Sim" : "Não"));
            System.out.println("Local na memória: " + evento);
            System.out.println("----------------\n");
        }
    }

    /**
     * Edita um evento existente com base no id informado
     * Este método representa a operação de atualização (Update) do CRUD.
     *
     * @param idEvento O ID do evento a ser editado.
     * @param novoOrganizador O novo organizador do evento.
     * @param novoNome O novo nome para o evento.
     * @param novaFotoEvento A nova foto do evento.
     * @param novaDescricao A nova descrição do evento.
     * @param novoFormato O novo formato do evento.
     * @param novoCusto O novo custo do evento.
     * @param novaLocalizacao A nova localização do evento.
     * @param novaDataInicial A nova data inicial do evento.
     * @param novaDataFinal A nova data final do evento.
     * @param novaQntDePessoas A nova quantidade de pessoas esperadas para o evento.
     * @param novaClassificacao A nova classificação do evento.
     * @param novaCertificacao Indica se o evento oferece certificação.
     */
    public void editarEvento(int idEvento, String novoOrganizador, String novoNome, String novaFotoEvento,
                             String novaDescricao, String novoFormato, float novoCusto, String novaLocalizacao,
                             Date novaDataInicial, Date novaDataFinal, int novaQntDePessoas,
                             int novaClassificacao, boolean novaCertificacao) {
        Optional<EventoModel> eventoParaEditarOptional = eventoRepository.buscarEventoPorId(idEvento);
        eventoParaEditarOptional.ifPresentOrElse(evento -> {
            evento.setOrganizador(novoOrganizador);
            evento.setNomeEvento(novoNome);
            evento.setFotoEvento(novaFotoEvento);
            evento.setDescricao(novaDescricao);
            evento.setFormato(novoFormato);
            evento.setCusto(novoCusto);
            evento.setLocalizacao(novaLocalizacao);
            evento.setDataInicial(novaDataInicial);
            evento.setDataFinal(novaDataFinal);
            evento.setQntDePessoas(novaQntDePessoas);
            evento.setClassificacao(novaClassificacao);
            evento.setCertificacao(novaCertificacao);
            System.out.println("Evento com ID " + idEvento + " ('" + evento.getNomeEvento() + "') editado com sucesso.");
        }, () -> System.out.println("Evento com ID " + idEvento + " não encontrado."));
    }

    /**
     * Deleta um evento do repositório com base no ID do {@code EventoModel} fornecido.
     * Este método representa a operação de exclusão (Delete) do CRUD.
     *
     * @param eventoParaExcluir O objeto {@code EventoModel} a ser deletado. O ID deste objeto será utilizado para a exclusão.
     */
    public void deletarEvento(EventoModel eventoParaExcluir) {
        if (eventoParaExcluir == null || eventoParaExcluir.getId() == 0) {
            System.out.println("Evento inválido para exclusão.");
            return;
        }

        int idEventoParaExcluir = eventoParaExcluir.getId();
        Optional<EventoModel> eventoEncontradoOptional = eventoRepository.buscarEventoPorId(idEventoParaExcluir);

        eventoEncontradoOptional.ifPresentOrElse(evento -> {
            if (eventoRepository.removerEvento(evento)) {
                System.out.println("Evento com ID " + idEventoParaExcluir + " ('" + evento.getNomeEvento() + "') deletado com sucesso.");
            } else {
                System.out.println("Erro ao deletar o evento com ID " + idEventoParaExcluir + ".");
            }
        }, () -> System.out.println("Evento com ID " + idEventoParaExcluir + " não encontrado."));
    }
}