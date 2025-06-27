package com.eventually.service;

import com.eventually.dto.CriarEventoDto;
import com.eventually.model.FormatoSelecionado;
import com.eventually.model.TemaPreferencia;
import com.eventually.model.EventoModel;
import com.eventually.model.UsuarioModel;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Esta classe é um Singleton, garantindo que apenas uma instância de {@code EventoCriacaoService} exista em toda a aplicação.
 * É responsável por realizar a lógica de registro de novos usuários, utiliza padrões de validação para nome,
 * e-mail, senha, data de nascimento, localização e temas preferidos.
 * Além disso, possui o método CREATE do CRUD para usuário.
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.02
 * @since 2025-05-15
 */
public final class EventoCriacaoService {
    private static EventoCriacaoService instancia;
    private Set<EventoModel> listaEventos;

    private static int proximoId = 1;

    private UsuarioSessaoService usuarioSessaoService;
    private AlertaService alertaService = new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(EventoCriacaoService.class);

    /**
     * Construtor que inicializa a lista com um objeto teste do tipo {@link EventoModel}.
     */
    private EventoCriacaoService() {
        listaEventos = new HashSet<>();
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        //evento teste abaixo:

        //evento teste acima

        sistemaDeLogger.info("Inicializado e lista de eventos criada. HashSet size: " + listaEventos.size());
    }

    /**
     * Retorna a instância única de {@code EventoCriacaoService}, se ainda não existe, ela é criada e, em caso de
     * falha, é exibida uma mensagem no console.
     * @return a instância única de {@code EventoCriacaoService}.
     */
    public static synchronized EventoCriacaoService getInstancia() {
        sistemaDeLogger.info("Método getInstancia() chamado.");
        try {
            if (instancia == null) {
                instancia = new EventoCriacaoService();
            }
            return instancia;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao retornar a instância."+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método é responsável por garantir que a lista de usuários seja inicializada, carrega dados iniciais e/ou
     * confirma a criação e, em caso de falha, exibe uma mensagem no console.
     */
    public void criarLista() {
        sistemaDeLogger.info("Método criarLista() chamado.");
        try{
            if (listaEventos == null) {
                listaEventos = new HashSet<>();
                sistemaDeLogger.info("Método criarLista() inicializou a lista de usuários.");
            } else {
                sistemaDeLogger.info("Método criarLista() chamado, lista de usuários já está pronta. Tamanho atual: " + listaEventos.size());
            }
        } catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao inicializar a lista: "+e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao inicializar a lista");
        }
    }

    /**
     * Este método realiza a validação completa de um novo usuário e, se aprovado, realiza o cadastro e, em caso de
     * falha, é exibida uma mensagem no console.
     * @param dto DTO contendo as informações do usuário a ser cadastrado.
     * @throws RuntimeException se qualquer campo for inválido.
     */
    public boolean criarEventoSeValido(CriarEventoDto dto, String link, String localizacaoEvento, Image fotoEvento) {
        sistemaDeLogger.info("Método criarEventoSeValido() chamado.");
        try {

            criarEvento(dto,link,localizacaoEvento,fotoEvento);
            return true;
        }catch (Exception e) {
            sistemaDeLogger.error("Erro ao validar DTO: "+e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao validar se o DTO é valido.");
            return false;
        }
    }


    /**
     * Valida as informações recebidas no controller e chama o método do repositório para adicionar a lista e, em caso
     * de falha, é exibida uma mensagem no console.
     * @param dto o objeto {@code EventoModel} contendo as informações do evento a ser criado.
     */
    public void criarEvento(CriarEventoDto dto, String link, String localizacaoEvento, Image fotoEvento) {
        sistemaDeLogger.info("Método criarEvento() chamado.");
        try {
            Set<TemaPreferencia> temasPreferidos = MapeamentoPreferenciasService.mapearPreferencias(dto.preferenciasEvento());

            FormatoSelecionado formatoEnum = FormatoSelecionadoService.mapearFormato(dto.preferenciaFormato());

            UsuarioModel usuario = usuarioSessaoService.procurarUsuario(dto.emailOrganizador());
            EventoModel novoEvento = new EventoModel(
                    usuario,
                    dto.tituloEvento(),
                    dto.descricaoEvento(),
                    formatoEnum,
                    link,
                    localizacaoEvento,
                    fotoEvento,
                    dto.nParticipantes(),
                    dto.diaInicial(),
                    dto.horaInicial(),
                    dto.diaFinal(),
                    dto.horaFinal(),
                    temasPreferidos,
                    new ArrayList<>(),
                    true,
                    false
            );

            adicionarEvento(novoEvento);
        }
        catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao criar evento: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao criar evento.");
        }
    }

    /**
     * Adiciona um novo evento à lista de eventos após validações e, em caso de falha, exibe uma mensagem no console.
     * @param evento o objeto {@code EventoModel} a ser adicionado.
     * @return {@code true} se o usuário foi adicionado com sucesso, {@code false} caso contrário.
     */
    public boolean adicionarEvento(EventoModel evento) {
        sistemaDeLogger.info("Método adicionarEvento() na lista chamado.");
        try {
            evento.setId(proximoId++);

            int id = System.identityHashCode(evento);
            evento.setId(id);

            boolean adicionado = listaEventos.add(evento);
            if (adicionado) {
                sistemaDeLogger.info("Evento adicionado com ID: " + id + " | HashSet size: " + listaEventos.size());
            } else {
                sistemaDeLogger.info("Evento não adicionado (possivelmente já existe ou houve um problema).");
            }
            return adicionado;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro inesperado ao adicionar evento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Busca um usuário na lista pelo seu ID e, em caso de falha, exibe uma mensagem no console.
     * @param id o ID do usuário a ser buscado.
     * @return um {@code Optional} contendo o {@code EventoModel} correspondente ao ID,
     * ou um {@code Optional} vazio se não encontrado.
     */
    public Optional<EventoModel> buscarEventoPorId(int id) {
        sistemaDeLogger.info("Método buscarEventoPorId() chamado.");
        try {
            return listaEventos.stream().
                    filter(u -> u.getId() == id).
                    findFirst();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao retornar a busca de evento por ID: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retorna a lista de todos os usuários armazenados e, em caso de falha, exibe uma mensagem no console.
     * @return Um {@code Set} de objetos {@code EventoModel}.
     */
    public Set<EventoModel> getAllEventos() {
        sistemaDeLogger.info("Método getAllEventos() chamado.");
        try {
            Set<EventoModel> eventos = Collections.unmodifiableSet(listaEventos);
            System.out.println("Eventos encontrados:");
            for (EventoModel evento : eventos) {
                System.out.println(evento);
            }
            return Collections.unmodifiableSet(listaEventos);
        } catch (Exception e) {
            sistemaDeLogger.info("Erro retornar lista: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida a regra de negócio que impede e-mails duplicados.
     * Consulta o banco de dados para verificar a existência do e-mail.
     * @param nome o nome a ser consultado.
     * @return {@code true} se o nomeJÁ EXISTE no banco, {@code false} caso contrário.
     */
    private boolean validarSeNomeJaExiste(String nome) {
        sistemaDeLogger.info("Validando unicidade do e-mail: " + nome);

        Optional<EventoModel> eventoModelOptional = getAllEventos()
                .stream()
                .filter(evento -> evento.getNomeEvento().equalsIgnoreCase(nome))
                .findFirst();
        return eventoModelOptional.isPresent();
    }
}