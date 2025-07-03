package com.eventually.service;

import com.eventually.dto.CriarEventoDto;
import com.eventually.dto.PreferenciaFormatoDto;
import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.model.*;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Esta classe é um Singleton, garantindo que apenas uma instância de {@code EventoCriacaoService} exista em toda a aplicação.
 * É responsável por realizar a lógica de registro de novos usuários, utiliza padrões de validação para nome,
 * e-mail, senha, data de nascimento, localização e temas preferidos.
 * Além disso, possui o método CREATE do CRUD para usuário.
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.07
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
        this.usuarioSessaoService=UsuarioSessaoService.getInstancia();
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
     * Este método é responsável por garantir que a lista de eventos seja inicializada, carrega dados iniciais e/ou
     * confirma a criação e, em caso de falha, exibe uma mensagem no console.
     */
    public void criarLista() {
        sistemaDeLogger.info("Método criarLista() chamado.");
        try{
            if (listaEventos == null) {
                listaEventos = new HashSet<>();
                sistemaDeLogger.info("Método criarLista() inicializou a lista de eventos.");
            } else {
                sistemaDeLogger.info("Método criarLista() chamado, lista de eventos já está pronta. Tamanho atual: " + listaEventos.size());
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

    private void criarEvento(CriarEventoDto dto, String link, String localizacao, Image foto) {
        UsuarioModel organizador = usuarioSessaoService.procurarUsuario(dto.emailOrganizador());
        if (organizador == null) {
            sistemaDeLogger.error("CRÍTICO: Não foi possível criar o evento porque o organizador com email '{}' não foi encontrado.", dto.emailOrganizador());
            return;
        }

        FormatoSelecionado formato = converterFormato(dto.preferenciaFormato());
        Set<Comunidade> comunidades = converterComunidades(dto.preferenciasEvento());

        EventoModel novoEvento = new EventoModel(
                organizador,
                dto.tituloEvento(),
                dto.descricaoEvento(),
                formato,
                link,
                localizacao,
                foto,
                dto.nParticipantes(),
                dto.diaInicial(),
                dto.horaInicial(),
                dto.diaFinal(),
                dto.horaFinal(),
                comunidades,
                new ArrayList<>(),
                true,
                false,
                new ArrayList<>()
        );
        novoEvento.setId(proximoId++);

        boolean adicionado = this.listaEventos.add(novoEvento);

        if (adicionado) {
            sistemaDeLogger.info("Evento '{}' criado com ID {} e adicionado à lista geral.", novoEvento.getNome(), novoEvento.getId());

            organizador.getEventosOrganizados().add(novoEvento);
            sistemaDeLogger.info("CONEXÃO FEITA: Evento ID {} associado ao organizador '{}'.", novoEvento.getId(), organizador.getEmail());
        } else {
            sistemaDeLogger.warn("Evento '{}' não foi adicionado (possivelmente um duplicado).", novoEvento.getNome());
        }
    }

    /**
     * Converte o DTO de formato em um Enum FormatoSelecionado.
     */
    private FormatoSelecionado converterFormato(PreferenciaFormatoDto dto) {
        if (dto.isPresencial()) return FormatoSelecionado.PRESENCIAL;
        if (dto.isOnline()) return FormatoSelecionado.ONLINE;
        if (dto.isHibrido()) return FormatoSelecionado.HIBRIDO;
        return FormatoSelecionado.PRESENCIAL;
    }

    /**
     * Converte o DTO de preferências em um Set de Enums Comunidade.
     */
    private Set<Comunidade> converterComunidades(PreferenciasUsuarioDto dto) {
        Set<Comunidade> comunidadesSelecionadas = new HashSet<>();
        if (dto.corporativo()) comunidadesSelecionadas.add(Comunidade.CORPORATIVO);
        if (dto.beneficente()) comunidadesSelecionadas.add(Comunidade.BENEFICENTE);
        if (dto.educacional()) comunidadesSelecionadas.add(Comunidade.EDUCACIONAL);
        if (dto.cultural()) comunidadesSelecionadas.add(Comunidade.CULTURAL);
        if (dto.esportivo()) comunidadesSelecionadas.add(Comunidade.ESPORTIVO);
        if (dto.religioso()) comunidadesSelecionadas.add(Comunidade.RELIGIOSO);
        if (dto.social()) comunidadesSelecionadas.add(Comunidade.SOCIAL);

        return comunidadesSelecionadas;
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
                sistemaDeLogger.info("EventoH adicionado com ID: " + id + " | HashSet size: " + listaEventos.size());
            } else {
                sistemaDeLogger.info("EventoH não adicionado (possivelmente já existe ou houve um problema).");
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
                .filter(evento -> evento.getNome().equalsIgnoreCase(nome))
                .findFirst();
        return eventoModelOptional.isPresent();
    }

    public boolean isRegraTituloValido(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            alertaService.alertarCampoVazio("TÍTULO DO EVENTO");
            return false;
        }
        return true;
    }

    public boolean isRegraDatasValidas(LocalDate diaInicial, String horaInicialStr, LocalDate diaFinal, String horaFinalStr) {
        if (diaInicial == null || diaFinal == null || horaInicialStr == null || horaFinalStr.trim().isEmpty() || horaInicialStr.trim().isEmpty()) {
            alertaService.alertarErro("Todas as datas e horas devem ser preenchidas.");
            return false;
        }
        try {
            LocalTime horaInicial = LocalTime.parse(horaInicialStr);
            LocalTime horaFinal = LocalTime.parse(horaFinalStr);

            LocalDateTime inicioEvento = diaInicial.atTime(horaInicial);
            LocalDateTime fimEvento = diaFinal.atTime(horaFinal);

            if (inicioEvento.isBefore(LocalDateTime.now())) {
                alertaService.alertarWarn("Data inválida", "A data de início do evento não pode ser no passado.");
                return false;
            }
            if (fimEvento.isBefore(inicioEvento)) {
                alertaService.alertarWarn("Data inválida", "A data final do evento deve ser após a data de início.");
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            alertaService.alertarErro("Use o formato HH:MM para as horas.");
            return false;
        }
    }

    public boolean isRegraFormatoValido(String formato) {
        if (formato == null || formato.trim().isEmpty()){
            alertaService.alertarWarn("Formato obrigatório", "Selecione um formato para o evento (Presencial, Online ou Híbrido).");
            return false;
        }
        return true;
    }

    public boolean isRegraLocalizacaoValida(FormatoSelecionado formato, String localizacao) {
        if (formato == FormatoSelecionado.PRESENCIAL || formato == FormatoSelecionado.HIBRIDO) {
            if (localizacao == null || localizacao.trim().isEmpty()) {
                alertaService.alertarCampoVazio("LOCALIZAÇÃO (obrigatório para eventos presenciais/híbridos)");
                return false;
            }
        }
        return true;
    }

    public boolean isRegraLinkValido(FormatoSelecionado formato, String link) {
        if (formato == FormatoSelecionado.ONLINE || formato == FormatoSelecionado.HIBRIDO) {
            if (link == null || link.trim().isEmpty()) {
                alertaService.alertarCampoVazio("LINK (obrigatório para eventos online/híbridos)");
                return false;
            }
        }
        return true;
    }

    public boolean isRegraTemasValido(PreferenciasUsuarioDto temasSelecionados) {
        boolean algumSelecionado = temasSelecionados.corporativo() || temasSelecionados.beneficente() ||
                temasSelecionados.educacional() || temasSelecionados.cultural() ||
                temasSelecionados.esportivo() || temasSelecionados.religioso() ||
                temasSelecionados.social();

        if (!algumSelecionado) {
            alertaService.alertarWarn("Tema obrigatório", "Selecione pelo menos um tema para o evento.");
            return false;
        }
        return true;
    }

    public boolean isRegraFotoValida(Image fotoEvento) {
        if (fotoEvento == null) {
            alertaService.alertarCampoVazio("IMAGEM DO EVENTO");
            return false;
        }
        return true;
    }
}