package com.eventually.service;

import com.eventually.dto.CadastrarUsuarioDto;
import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.model.TemaPreferencia;
import com.eventually.model.UsuarioModel;
import com.eventually.repository.UsuarioRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Classe responsável por realizar a lógica de registro de novos usuários,
 * incluindo validações de dados e persistência no repositório.
 * Utiliza padrões de validação para nome, e-mail, senha, data de nascimento,
 * localização e temas preferidos.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-05-15
 */
public class UsuarioCadastroService {

    private final UsuarioRepository usuarioRepository;

    private AlertService alertService = new AlertService();

    private static final Pattern EMAIL_DOMAIN_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]{2,}@[a-zA-Z0-9.-]{2,}\\.[a-zA-Z]{2,6}$");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern LETTER_PATTERN = Pattern.compile("[a-zA-Z]");

    /**
     * Construtor padrão que inicializa o repositório de usuários.
     */
    public UsuarioCadastroService() {
        this.usuarioRepository = new UsuarioRepository();
    }

    /**
     * Verifica se o nome informado possui pelo menos duas partes (nome e sobrenome).
     *
     * @param novoValorNome nome de usuário,
     * @return {@code true} se seguir o padrão, caso contrário {@code false}.
     */
    public boolean isRegraNomeCumprida(String novoValorNome) {
        try {
            if (novoValorNome == null || novoValorNome.trim().isEmpty()) {
                alertService.alertarCampoVazio("NOME");
                return false;
            }
            String[] separaNomeESobrenome = novoValorNome.trim().split("\\s+");
            if (separaNomeESobrenome.length < 2) {
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("UCS: ocorreu um erro");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se o email informado é válido.
     *
     * @param novoValorEmail email de usuário,
     * @return {@code true} se seguir o padrão, caso contrário {@code false}.
     */
    public boolean isRegraEmailCumprida(String novoValorEmail, boolean exibirAlertas) {
        try {
            if (novoValorEmail == null || novoValorEmail.trim().isEmpty()) {
                if (exibirAlertas) alertService.alertarCampoVazio("EMAIL");
                return false;
            }
            boolean emailValido = EMAIL_DOMAIN_PATTERN.matcher(novoValorEmail).matches();
            if (!emailValido && exibirAlertas) {
                alertService.alertarWarn("Email inválido", "Informe um email válido, como exemplo@dominio.com");
            }
            return emailValido;
        } catch (Exception e) {
            System.out.println("UCS: ocorreu um erro ao validar o email.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Valida se ao menos um tema de interesse foi selecionado.
     *
     * @param temasSelecionados DTO com os temas preferidos selecionados.
     * @return Mensagem de erro se nenhum tema for selecionado, ou {@code null} se válido.
     */
    public boolean isRegraTemasCumprida(PreferenciasUsuarioDto temasSelecionados) {
        boolean algumSelecionado =
                temasSelecionados.corporativo() || temasSelecionados.beneficente() ||
                        temasSelecionados.educacional() || temasSelecionados.cultural() ||
                        temasSelecionados.esportivo() || temasSelecionados.religioso() ||
                        temasSelecionados.social();

        if (!algumSelecionado) {
            alertService.alertarWarn("Tema obrigatório", "Selecione pelo menos um tema de interesse.");
            return false;
        }
        return true;
    }

    public Map<String, Boolean> isRegraSenhaCumprida(String novoValorSenha) {
        try {
            Map<String, Boolean> ruleStatus = new HashMap<>();

            if (novoValorSenha == null || novoValorSenha.trim().isEmpty()) {
                alertService.alertarCampoVazio("SENHA");
                return null;
            }
            ruleStatus.put("hasSpecial", SPECIAL_CHAR_PATTERN.matcher(novoValorSenha).find());
            ruleStatus.put("hasDigit", DIGIT_PATTERN.matcher(novoValorSenha).find());
            ruleStatus.put("hasLetter", LETTER_PATTERN.matcher(novoValorSenha).find());
            ruleStatus.put("hasSixChar", novoValorSenha.length() >= 6);

            return ruleStatus;
        } catch (Exception e) {
            System.out.println("UCS: Erro ao validar regras da senha.");
            e.printStackTrace();
            alertService.alertarErro("Erro ao validar senha.");
            return null;
        }
    }

    public boolean isRegraDataCumprida(LocalDate novoValorData) {
        try {
            if (novoValorData == null) {
                alertService.alertarCampoVazio("DATA DE NASCIMENTO");
                return false;
            }
            LocalDate hoje = LocalDate.now();
            return novoValorData.isBefore(hoje.minusYears(12));//se for antes de 12 anos de hoje é true
        } catch (Exception e) {
            System.out.println("UCS: erro ao validar data.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean isRegraCidadeCumprida(String novoValorCidade) {
        try {
            if (novoValorCidade == null || novoValorCidade.trim().isEmpty()) {
                alertService.alertarCampoVazio("CIDADE");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("UCS: ocorreu um erro ao validar a cidade.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Este método realiza a validação completa de um novo usuário e, se aprovado, realiza o cadastro.
     *
     * @param dto DTO contendo as informações do usuário a ser cadastrado.
     * @throws RuntimeException se qualquer campo for inválido.
     */
    public boolean cadastrarUsuarioSeValido(CadastrarUsuarioDto dto) {
        boolean nomeOk = isRegraNomeCumprida(dto.nomePessoa());
        boolean emailOk = isRegraEmailCumprida(dto.email(), false);
        boolean cidadeOk = isRegraCidadeCumprida(dto.localizacaoUsuario());

        boolean senhaOk = false;
        Map<String, Boolean> regrasSenha = isRegraSenhaCumprida(dto.senha());
        if (regrasSenha != null) {
            senhaOk = regrasSenha.getOrDefault("hasSpecial", false)
                    && regrasSenha.getOrDefault("hasDigit", false)
                    && regrasSenha.getOrDefault("hasLetter", false)
                    && regrasSenha.getOrDefault("hasSixChar", false);
        }

        boolean dataOk;
        try {
            LocalDate dataNascimento = dto.data();
            dataOk = isRegraDataCumprida(dataNascimento);
        } catch (DateTimeParseException e) {
            System.out.println("UCS: Erro ao converter data: " + dto.data());
            alertService.alertarWarn("Data inválida", "Informe a data no formato correto (dd/MM/yyyy).");
            dataOk = false;
        }

        boolean temasOk = isRegraTemasCumprida(dto.preferencias());

        if (nomeOk && emailOk && cidadeOk && senhaOk && dataOk && temasOk) {
            criarUsuario(dto);
            return true;
        } else {
            alertService.alertarWarn("Cadastro inválido", "Preencha corretamente todos os campos.");
            System.out.println("UCS: Falha no cadastro: algum dado não passou na validação.");
            return false;
        }
    }

    /**
     * Valida as informações recebidas no controller e chama o método do repositório para adicionar a lista.
     * @param dto Objeto {@code UsuarioModel} contendo as informações do usuario a ser criado.
     */
    public void criarUsuario(CadastrarUsuarioDto dto){
        System.out.println("UCS: método de criar usuario e adicionar na lista do repository chamado");
        try {
            Set<TemaPreferencia> temasPreferidos = MapeamentoPreferenciasService.mapearPreferencias(dto.preferencias());

            UsuarioModel novoUsuario = new UsuarioModel(
                    dto.nomePessoa(),
                    dto.email(),
                    dto.senha(),
                    dto.localizacaoUsuario(),
                    dto.data(),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    temasPreferidos
            );
            usuarioRepository.adicionarUsuario(novoUsuario);
        }
        catch (RuntimeException e) {
            System.out.println("UCS: Erro ao criar usuario.");
            e.printStackTrace();
            alertService.alertarErro("Erro ao criar usuario.");
        }
        System.out.println("UCS: Usuário realmente foi criado:");
        System.out.println(" - Nome: " + dto.nomePessoa());
        System.out.println(" - Email: " + dto.email());
        System.out.println(" - Preferências: " + dto.preferencias());
    }
}