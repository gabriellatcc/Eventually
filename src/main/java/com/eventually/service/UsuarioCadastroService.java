package com.eventually.service;

import com.eventually.dto.CadastrarUsuarioDto;
import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Esta classe é um Singleton, garantindo que apenas uma instância de {@code UsuarioCadastroService} exista em toda a aplicação.
 * É responsável por realizar a lógica de registro de novos usuários, utiliza padrões de validação para nome,
 * e-mail, senha, data de nascimento, localização e temas preferidos.
 * Além disso, possui o método CREATE do CRUD para usuário.
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.10
 * @since 2025-05-15
 */
public final class UsuarioCadastroService {
    private static UsuarioCadastroService instancia;
    private EventoCriacaoService eventoCriacaoService;
    private Set<UsuarioModel> listaUsuarios;

    private static final Pattern EMAIL_DOMAIN_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]{2,}@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern LETTER_PATTERN = Pattern.compile("[a-zA-Z]");

    private static int proximoId = 1;

    private AlertaService alertaService = new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(UsuarioCadastroService.class);

    /**
     * Construtor que inicializa a lista com um objeto teste do tipo {@link UsuarioModel}.
     */
    private UsuarioCadastroService() {
        listaUsuarios = new HashSet<>();
        sistemaDeLogger.info("ServicoCadastroUsuario inicializado e lista de usuários criada. HashSet size: " + listaUsuarios.size());
    }

    /**
     * Inicializa a aplicação com dados de teste (um usuário e três eventos).
     * Este método deve ser chamado apenas uma vez no início da aplicação para popular o sistema.
     */
    public void inicializarDadosDeTeste() {
        if (!listaUsuarios.isEmpty()) {
            sistemaDeLogger.info("Dados de teste já inicializados. Nenhuma ação foi tomada.");
            return;
        }

        sistemaDeLogger.info("Inicializando dados de teste...");

        EventoCriacaoService eventoCriacaoService = EventoCriacaoService.getInstancia();

        Set<Comunidade> preferenciasDoUsuario = new HashSet<>();
        preferenciasDoUsuario.add(Comunidade.CORPORATIVO);
        preferenciasDoUsuario.add(Comunidade.CULTURAL);
        preferenciasDoUsuario.add(Comunidade.SOCIAL);
        preferenciasDoUsuario.add(Comunidade.ESPORTIVO);
        preferenciasDoUsuario.add(Comunidade.BENEFICENTE);
        preferenciasDoUsuario.add(Comunidade.RELIGIOSO);
        preferenciasDoUsuario.add(Comunidade.EDUCACIONAL);


        UsuarioModel usuarioTesteModel = new UsuarioModel(
                "gab tav", "gab@gmail.com", "a1234$", "crz",
                LocalDate.of(2003, 2, 1), null, new ArrayList<>(), new ArrayList<>(),
                preferenciasDoUsuario, true
        );
        this.adicionarUsuario(usuarioTesteModel);

        LocalDate dataDeHoje = LocalDate.now();
        LocalDate antesOntem = dataDeHoje.plusDays(-2);
        LocalDate amanha = dataDeHoje.plusDays(1);
        LocalDate depoisAmanha = dataDeHoje.plusDays(2);
        LocalTime horaEspecificaTeste1 = LocalTime.of(10, 30);
        LocalTime horaEspecificaTeste2 = LocalTime.of(12, 30);
        Set<Comunidade> preferenciasEvento = new HashSet<>();
        preferenciasEvento.add(Comunidade.SOCIAL);
        preferenciasEvento.add(Comunidade.EDUCACIONAL);
        Set<Comunidade> preferenciasEvento1 = new HashSet<>();
        preferenciasEvento1.add(Comunidade.EDUCACIONAL);
        Set<Comunidade> preferenciasEvento2 = new HashSet<>();
        preferenciasEvento2.add(Comunidade.EDUCACIONAL);
        preferenciasEvento2.add(Comunidade.CULTURAL);

        EventoModel evento1 = new EventoModel(
                usuarioTesteModel, "Conferência Tech Inovação", "Discussão sobre o futuro da tecnologia.",
                FormatoSelecionado.PRESENCIAL, null, "Centro de Convenções, SP", null, 200,
                dataDeHoje, horaEspecificaTeste1, amanha, horaEspecificaTeste2,
                preferenciasEvento, new ArrayList<>(), true, false,  new ArrayList<>()
        );

        EventoModel evento2 = new EventoModel(
                usuarioTesteModel, "Workshop de Design UX/UI", "Aprenda na prática os fundamentos de UX.",
                FormatoSelecionado.ONLINE, "https://zoom.us/j/123456", "Online", null, 50,
                amanha, LocalTime.of(17, 30), depoisAmanha, LocalTime.of(18, 30),
                preferenciasEvento1, new ArrayList<>(), true, false,  new ArrayList<>()
        );

        EventoModel evento3 = new EventoModel(
                usuarioTesteModel, "Festival de Música Indie", "Bandas independentes em um evento único.",
                FormatoSelecionado.HIBRIDO, null, "Parque Ibirapuera, SP", null, 1000,
                antesOntem, horaEspecificaTeste1, antesOntem, horaEspecificaTeste2,
                preferenciasEvento2, new ArrayList<>(), true, false,  new ArrayList<>()
        );

        LocalTime horaEspecificaTeste3 = LocalTime.of(19, 0);
        LocalTime horaEspecificaTeste4 = LocalTime.of(21, 0);

// 1. Evento CORPORATIVO
        Set<Comunidade> preferenciasEventoCorporativo = new HashSet<>();
        preferenciasEventoCorporativo.add(Comunidade.CORPORATIVO);
        preferenciasEventoCorporativo.add(Comunidade.EDUCACIONAL); // Um evento pode ter mais de uma comunidade

        EventoModel eventoCorporativo = new EventoModel(
                usuarioTesteModel,
                "Workshop de Liderança Executiva",
                "Desenvolva suas habilidades de liderança e gestão de equipes.",
                FormatoSelecionado.HIBRIDO, // Formato Híbrido
                "https://meet.google.com/seu-link-aqui", // Link para participantes online
                "Centro de Convenções, Sala 5B", // Local para participantes presenciais
                null, // foto
                50, // nParticipantes
                amanha, // dataInicial
                horaEspecificaTeste1, // horaInicial
                amanha, // dataFinal
                horaEspecificaTeste2, // horaFinal
                preferenciasEventoCorporativo, // comunidades
                new ArrayList<>(), // participantes (lista vazia)
                true, // estado (ativo)
                false, // isFinalizado (ainda não ocorreu)
                new ArrayList<>() // comentarios (lista vazia)
        );

// 2. Evento RELIGIOSO
        Set<Comunidade> preferenciasEventoReligioso = new HashSet<>();
        preferenciasEventoReligioso.add(Comunidade.RELIGIOSO);

        EventoModel eventoReligioso = new EventoModel(
                usuarioTesteModel,
                "Celebração Ecumênica da Paz",
                "Um momento de reflexão e oração pela paz entre os povos.",
                FormatoSelecionado.ONLINE, // Formato Online
                "https://youtube.com/live/celebracao-paz", // Link de acesso
                null, // localizacao (não se aplica a eventos puramente online)
                null, // foto
                500, // nParticipantes
                depoisAmanha, // dataInicial
                horaEspecificaTeste3, // horaInicial
                depoisAmanha, // dataFinal
                horaEspecificaTeste4, // horaFinal
                preferenciasEventoReligioso, // comunidades
                new ArrayList<>(), // participantes
                true, // estado
                false, // isFinalizado
                new ArrayList<>() // comentarios
        );

// 3. Evento BENEFICENTE
        Set<Comunidade> preferenciasEventoBeneficente = new HashSet<>();
        preferenciasEventoBeneficente.add(Comunidade.BENEFICENTE);

        EventoModel eventoBeneficente = new EventoModel(
                usuarioTesteModel,
                "Feijoada Beneficente da Comunidade",
                "Arrecadação de fundos para a caridade local. Toda a renda será revertida.",
                FormatoSelecionado.PRESENCIAL, // Formato Presencial
                null, // linkAcesso (não se aplica)
                "Salão de Festas da Paróquia Central", // localizacao
                null, // foto
                300, // nParticipantes
                antesOntem, // dataInicial (um evento que já passou)
                horaEspecificaTeste2, // horaInicial
                antesOntem, // dataFinal
                horaEspecificaTeste3, // horaFinal
                preferenciasEventoBeneficente, // comunidades
                new ArrayList<>(), // participantes
                true, // estado
                true, // isFinalizado (marcado como finalizado pois a data já passou)
                new ArrayList<>() // comentarios
        );

        usuarioTesteModel.getEventosOrganizados().add(evento1);
        eventoCriacaoService.adicionarEvento(evento1);

        usuarioTesteModel.getEventosOrganizados().add(evento2);
        eventoCriacaoService.adicionarEvento(evento2);

        usuarioTesteModel.getEventosOrganizados().add(evento3);
        eventoCriacaoService.adicionarEvento(evento3);

        usuarioTesteModel.getEventosOrganizados().add(eventoCorporativo );
        eventoCriacaoService.adicionarEvento(eventoCorporativo );

        usuarioTesteModel.getEventosOrganizados().add(eventoReligioso  );
        eventoCriacaoService.adicionarEvento(eventoReligioso  );

        usuarioTesteModel.getEventosOrganizados().add(eventoBeneficente   );
        eventoCriacaoService.adicionarEvento(eventoBeneficente   );

        sistemaDeLogger.info("Dados de teste criados com sucesso.");
    }

    /**
     * Retorna a instância única de {@code UsuarioCadastroService}, se ainda não existe, ela é criada e, em caso de
     * falha, é exibida uma mensagem no console.
     * @return a instância única de {@code UsuarioCadastroService}.
     */
    public static synchronized UsuarioCadastroService getInstancia() {
        sistemaDeLogger.info("Método getInstancia() chamado.");
        try {
            if (instancia == null) {
                instancia = new UsuarioCadastroService();
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
            if (listaUsuarios == null) {
                listaUsuarios = new HashSet<>();
                sistemaDeLogger.info("Método criarLista() inicializou a lista de usuários.");
            } else {
                sistemaDeLogger.info("Método criarLista() chamado, lista de usuários já está pronta. Tamanho atual: " + listaUsuarios.size());
            }
        } catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao inicializar a lista: "+e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao inicializar a lista");
        }
    }

    /**
     * Verifica se o nome informado possui pelo menos duas partes (nome e sobrenome) e, em caso de falha, exibe uma
     * mensagem no console.
     * @param novoValorNome nome de usuário,
     * @return {@code true} se seguir o padrão, caso contrário {@code false}.
     */
    public boolean isRegraNomeCumprida(String novoValorNome) {
        sistemaDeLogger.info("Método isRegraNomeCumprida() chamado.");
        try {
            if (novoValorNome == null || novoValorNome.trim().isEmpty()) {
                alertaService.alertarCampoVazio("NOME");
                return false;
            }
            String[] separaNomeESobrenome = novoValorNome.trim().split("\\s+");
            if (separaNomeESobrenome.length < 2) {
                return false;
            }
            return true;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro ao validar o nome: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se o email informado é válido e, em caso de falha, exibe uma mensagem no console.
     * @param novoValorEmail email de usuário,
     * @return {@code true} se seguir o padrão, caso contrário {@code false}.
     */
    public boolean isRegraEmailCumprida(String novoValorEmail) {
        sistemaDeLogger.info("Método isRegraEmailCumprida() chamado.");
        try {
            if (novoValorEmail == null || novoValorEmail.trim().isEmpty()) {
                alertaService.alertarCampoVazio("EMAIL");
                return false;
            }

            int atIndex = novoValorEmail.indexOf("@");
            int dotIndex = novoValorEmail.lastIndexOf(".");

            boolean estruturaMinima = atIndex > 1
                    && dotIndex > atIndex + 1
                    && novoValorEmail.length() - dotIndex - 1 >= 2;

            if (!estruturaMinima) {
                return false;
            }

            boolean emailValido = EMAIL_DOMAIN_PATTERN.matcher(novoValorEmail).matches();
            if (!emailValido) {
                alertaService.alertarWarn("Email inválido", "Informe um email válido, como exemplo@dominio.com");
            }

            return emailValido;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro ao validar o email: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Valida se a senha informada é válida e, em caso de falha, é exibida uma mensagem no console.
     * @param novoValorSenha a senha do usuário.
     * @return {@code true} se seguir o padrão, caso contrário {@code false}.
     */
    public Map<String, Boolean> isRegraSenhaCumprida(String novoValorSenha) {
        sistemaDeLogger.info("Método isRegraSenhaCumprida() chamado.");
        try {
            Map<String, Boolean> ruleStatus = new HashMap<>();

            if (novoValorSenha == null || novoValorSenha.trim().isEmpty()) {
                alertaService.alertarCampoVazio("SENHA");
                return null;
            }
            ruleStatus.put("hasSpecial", SPECIAL_CHAR_PATTERN.matcher(novoValorSenha).find());
            ruleStatus.put("hasDigit", DIGIT_PATTERN.matcher(novoValorSenha).find());
            ruleStatus.put("hasLetter", LETTER_PATTERN.matcher(novoValorSenha).find());
            ruleStatus.put("hasSixChar", novoValorSenha.length() >= 6);

            return ruleStatus;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao validar regras da senha: "+e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao validar senha.");
            return null;
        }
    }

    /**
     * Valida se a data informada é válida e, em caso de falha, é exibida uma mensagem no console.
     * @param novoValorData a data de nascimento do usuário.
     * @return {@code true} se seguir o padrão, caso contrário {@code false}.
     */
    public boolean isRegraDataCumprida(LocalDate novoValorData) {
        sistemaDeLogger.info("Método isRegraDataCumprida() chamado.");
        try {
            if (novoValorData == null) {
                alertaService.alertarCampoVazio("DATA DE NASCIMENTO");
                return false;
            }
            LocalDate hoje = LocalDate.now();
            return novoValorData.isBefore(hoje.minusYears(12));
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao validar data: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Valida se a cidade informada é válida e, em caso de falha, é exibida uma mensagem no console.
     * @param novoValorCidade a cidade do usuário.
     * @return {@code true} se seguir o padrão, caso contrário {@code false}.
     */
    public boolean isRegraCidadeCumprida(String novoValorCidade) {
        sistemaDeLogger.info("Método isRegraCidadeCumprida() chamado.");
        try {
            if (novoValorCidade == null || novoValorCidade.trim().isEmpty()) {
                alertaService.alertarCampoVazio("CIDADE");
                return false;
            }
            return true;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro ao validar a cidade: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Valida se ao menos um tema de interesse foi selecionado e, em caso de falha, é exibida uma mensagem no console.
     * @param temasSelecionados DTO com os temas preferidos selecionados.
     * @return Mensagem de erro se nenhum tema for selecionado, ou {@code null} se válido.
     */
    public boolean isRegraTemasCumprida(PreferenciasUsuarioDto temasSelecionados) {
        sistemaDeLogger.info("Método isRegraTemasCumprida() chamado.");
        try{
            boolean algumSelecionado =
                    temasSelecionados.corporativo() || temasSelecionados.beneficente() ||
                            temasSelecionados.educacional() || temasSelecionados.cultural() ||
                            temasSelecionados.esportivo() || temasSelecionados.religioso() ||
                            temasSelecionados.social();

            if (!algumSelecionado) {
                alertaService.alertarWarn("Tema obrigatório", "Selecione pelo menos um tema de interesse.");
                return false;
            }
            return true;
        } catch (Exception e) {
            sistemaDeLogger.error("Ocorreu um erro ao validar os temas: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método realiza a validação completa de um novo usuário e, se aprovado, realiza o cadastro e, em caso de
     * falha, é exibida uma mensagem no console.
     * @param dto DTO contendo as informações do usuário a ser cadastrado.
     * @throws RuntimeException se qualquer campo for inválido.
     */
    public boolean cadastrarUsuarioSeValido(CadastrarUsuarioDto dto) {
        sistemaDeLogger.info("Método cadastrarUsuarioSeValido() chamado.");
        try {
            boolean nomeOk = isRegraNomeCumprida(dto.nomePessoa());

            boolean emailOk = isRegraEmailCumprida(dto.email());

            boolean senhaOk = false;
            Map<String, Boolean> regrasSenha = isRegraSenhaCumprida(dto.senha());
            if (regrasSenha != null) {
                senhaOk = regrasSenha.getOrDefault("hasSpecial", false)
                        && regrasSenha.getOrDefault("hasDigit", false)
                        && regrasSenha.getOrDefault("hasLetter", false)
                        && regrasSenha.getOrDefault("hasSixChar", false);
            }

            boolean dataOk;

            LocalDate dataNascimento = dto.data();
            dataOk = isRegraDataCumprida(dataNascimento);
            boolean cidadeOk = isRegraCidadeCumprida(dto.localizacaoUsuario());

            boolean temasOk = isRegraTemasCumprida(dto.preferencias());


            boolean regrasVisuaisOk = nomeOk && emailOk && senhaOk && dataOk && cidadeOk && temasOk;

            if (!regrasVisuaisOk) {
                sistemaDeLogger.info("Falha no cadastro: dados de entrada não passaram na validação visual.");
                return false;
            }

            boolean emailJaExiste = validaSeEmailJaExiste(dto.email());
            if (emailJaExiste) {
                alertaService.alertarErro("Este e-mail já está cadastrado.");
                sistemaDeLogger.warn("Tentativa de cadastro com e-mail duplicado: " + dto.email());
                return false;
            }
            criarUsuario(dto);
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
     * @param dto o objeto {@code UsuarioModel} contendo as informações do usuario a ser criado.
     */
    public void criarUsuario(CadastrarUsuarioDto dto){
        sistemaDeLogger.info("Método criarUsuario() chamado.");
        try {

            Set<Comunidade> temasPreferidos = MapeamentoPreferenciasService.mapearPreferencias(dto.preferencias());

            UsuarioModel novoUsuario = new UsuarioModel(
                    dto.nomePessoa(),
                    dto.email(),
                    dto.senha(),
                    dto.localizacaoUsuario(),
                    dto.data(),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    temasPreferidos,
                    true
            );
            adicionarUsuario(novoUsuario);
        }
        catch (RuntimeException e) {
            sistemaDeLogger.error("Erro ao criar usuario: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao criar usuario.");
        }
    }

    /**
     * Adiciona um novo usuário à lista de usuários após validações e, em caso de falha, exibe uma mensagem no console.
     * @param usuario o objeto {@code UsuarioModel} a ser adicionado.
     * @return {@code true} se o usuário foi adicionado com sucesso, {@code false} caso contrário.
     */
    public boolean adicionarUsuario(UsuarioModel usuario) {
        sistemaDeLogger.info("Método adicionarUsuario() na lista chamado.");
        try {
            usuario.setId(proximoId++);

            int id = System.identityHashCode(usuario);
            usuario.setId(id);

            boolean adicionado = listaUsuarios.add(usuario);
            if (adicionado) {
                sistemaDeLogger.info("Usuário adicionado com ID: " + id + " | HashSet size: " + listaUsuarios.size());
            } else {
                sistemaDeLogger.info("Usuário não adicionado (possivelmente já existe ou houve um problema).");
            }
            return adicionado;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro inesperado ao adicionar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Busca um usuário na lista pelo seu ID e, em caso de falha, exibe uma mensagem no console.
     * @param id o ID do usuário a ser buscado.
     * @return um {@code Optional} contendo o {@code UsuarioModel} correspondente ao ID,
     * ou um {@code Optional} vazio se não encontrado.
     */
    public Optional<UsuarioModel> buscarUsuarioPorId(int id) {
        sistemaDeLogger.info("Método buscarUsuarioPorId() chamado.");
        try {
            return listaUsuarios.stream().
                    filter(u -> u.getId() == id).
                    findFirst();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao retornar a busca de usuario por ID: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retorna a lista de todos os usuários armazenados e, em caso de falha, exibe uma mensagem no console.
     * @return Um {@code Set} de objetos {@code UsuarioModel}.
     */
    public Set<UsuarioModel> getAllUsuarios() {
        sistemaDeLogger.info("Método getAllEventos() chamado.");
        try {
            Set<UsuarioModel> usuarios = Collections.unmodifiableSet(listaUsuarios);
            System.out.println("Usuários encontrados:");
            for (UsuarioModel usuario : usuarios) {
                System.out.println(usuario);
            }
            return Collections.unmodifiableSet(listaUsuarios);
        } catch (Exception e) {
            sistemaDeLogger.info("Erro retornar lista: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida a regra de negócio que impede e-mails duplicados.
     * Consulta o banco de dados para verificar a existência do e-mail.
     * @param email O e-mail a ser consultado.
     * @return {@code true} se o e-mail JÁ EXISTE no banco, {@code false} caso contrário.
     */
    private boolean validaSeEmailJaExiste(String email) {
        sistemaDeLogger.info("Validando unicidade do e-mail: " + email);

        Optional<UsuarioModel> usuarioOptional = getAllUsuarios()
                .stream()
                .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                .findFirst();
        return usuarioOptional.isPresent();
    }
}