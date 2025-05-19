package com.eventually.service;

import com.eventually.dto.CadastrarUsuarioDto;
import com.eventually.dto.PreferenciasUsuarioDto;
import com.eventually.model.TemaPreferencia;
import com.eventually.model.UsuarioModel;
import com.eventually.repository.UsuarioRepository;

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
    private static final Pattern EMAIL_DOMAIN_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
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
     * Este método realiza a validação completa de um novo usuário e, se aprovado, realiza o cadastro.
     * @param usuarioDto DTO contendo as informações do usuário a ser cadastrado.
     * @throws RuntimeException se qualquer campo for inválido.
     */
    public void cadastrarNovoUsuario(CadastrarUsuarioDto usuarioDto)
    {
        String erro;
        erro = validarNomeParaEnvio(usuarioDto.nomePessoa());
        if (erro != null) throw new RuntimeException("Nome inválido: " + erro);
        erro = validarEmailParaEnvio(usuarioDto.email());
        if (erro != null) throw new RuntimeException("Email inválido: " + erro);
        erro = validarSenhaParaEnvio(usuarioDto.senha());
        if (erro != null) throw new RuntimeException("Senha inválida: " + erro);
        erro = validarDataParaEnvio(usuarioDto.data());
        if (erro != null) throw new RuntimeException("Data de nascimento inválida: " + erro);
        erro = validarCidadeParaEnvio(usuarioDto.localizacaoUsuario());
        if (erro != null) throw new RuntimeException("Cidade inválida: " + erro);
        erro = validarTemasParaEnvio(usuarioDto.preferencias());
        if (erro != null) throw new RuntimeException("Tema(s) inválido(s): " + erro);
        criarUsuario(usuarioDto);
    }

    /**
     * Valida o campo de nome do usuário.
     * @param name o nome completo do usuário
     * @return mensagem de erro se inválido, ou {@code null} se válido.
     */
    public String validarNomeParaEnvio(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "UsuarioCadastroService: erro: O campo nome é obrigatório.";
        }
        if (!isRegraNomeCumprida(name)) {
            return "UsuarioCadastroService: erro: O nome não segue os padrões (deve ter nome e sobrenome).";
        }
        return null;
    }

    /**
     * Verifica se o nome informado possui pelo menos duas partes (nome e sobrenome).
     * @param name nome de usuário,
     * @return {@code true} se seguir o padrão, caso contrário {@code false}.
     */
    public boolean isRegraNomeCumprida(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String[] parts = name.trim().split("\\s+");
        return parts.length >= 2;
    }

    /**
     * Valida o formato do e-mail.
     * @param email email do usuario.
     * @return Mensagem de erro se inválido, ou {@code null} se válido.
     */
    public String validarEmailParaEnvio(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "UsuarioCadastroService: erro: O campo email é obrigatório.";
        }
        if (!EMAIL_DOMAIN_PATTERN.matcher(email).matches()) {
            return "UsuarioCadastroService: erro: O email não tem domínio válido.";
        }
        return null;
    }

    /**
     * Valida a senha do usuário com base em regras de segurança.
     * @param password a senha do usuário
     * @return Mensagem de erro se inválida, ou {@code null} se válida.
     */
    public String validarSenhaParaEnvio(String password) {
        if (password == null || password.isEmpty()) {
            return "UsuarioCadastroService: erro: O campo senha é obrigatório.";
        }
        Map<String, Boolean> rules = validarRegrasSenha(password);
        if (!rules.get("hasSpecial") || !rules.get("hasDigit") || !rules.get("hasLetter")) {
            return "UsuarioCadastroService: erro: A senha não segue os padrões.";
        }
        return null;
    }

    /**
     * Aplica as regras de verificação da senha e retorna o status de cada uma.
     * @param password a senha a ser validada
     * @return Mapa com os resultados das verificações: caracteres especiais, dígitos e letras.
     */
    public Map<String, Boolean> validarRegrasSenha(String password) {
        Map<String, Boolean> ruleStatus = new HashMap<>();
        if (password == null) password = "";

        ruleStatus.put("hasSpecial", SPECIAL_CHAR_PATTERN.matcher(password).find());
        ruleStatus.put("hasDigit", DIGIT_PATTERN.matcher(password).find());
        ruleStatus.put("hasLetter", LETTER_PATTERN.matcher(password).find());

        return ruleStatus;
    }

    /**
     * Valida a presença da data de nascimento.
     * @param data a data de nascimento.
     * @return Mensagem de erro se ausente, ou {@code null} se válida.
     */
    public String validarDataParaEnvio(String data) {
        if (data == null) {
            //falta: enviar resposta ao controller e chamar método para exibir mensagem na interface
            return "UsuarioCadastroService: erro: Data de nascimento é obrigatória.";
        }
        return null;
    }

    /**
     * Valida a cidade informada pelo usuário
     * @param cidade o nome da cidade
     * @return Mensagem de erro se ausente, ou {@code null} se válida.
     */
    public String validarCidadeParaEnvio(String cidade) {
        if (cidade == null || cidade.trim().isEmpty()) {
            //falta: enviar resposta ao controller e chamar método para exibir mensagem na interface
            return "UsuarioCadastroService: erro: Cidade é obrigatória.";
        }
        return null;
    }

    /**
     * Valida se ao menos um tema de interesse foi selecionado.
     * @param temasSelecionados DTO com os temas preferidos selecionados.
     * @return Mensagem de erro se nenhum tema for selecionado, ou {@code null} se válido.
     */
    public String validarTemasParaEnvio(PreferenciasUsuarioDto temasSelecionados) {
        boolean algumSelecionado =
                temasSelecionados.corporativo() || temasSelecionados.beneficente() ||
                        temasSelecionados.educacional() || temasSelecionados.cultural() ||
                        temasSelecionados.esportivo() || temasSelecionados.religioso() ||
                        temasSelecionados.social();
        if (!algumSelecionado) {
            //falta enviar resposta ao controller e chamar método para exibir mensagem na interface
            return "UsuarioCadastroService: Selecione pelo menos um tema.";
        }
        return null;
    }

    /**
     * Valida as informações recebidas no controller e chama o método do repositório para adicionar a lista.
     * @param usuarioDto Objeto {@code UsuarioModel} contendo as informações do usuario a ser criado.
     */
    public void criarUsuario(CadastrarUsuarioDto usuarioDto){
        System.out.println("UsuarioCadastroService: método de criar usuario e adicionar na lista do repository chamado");

        Set<TemaPreferencia> temasPreferidos = MapeamentoPreferenciasService.mapearPreferencias(usuarioDto.preferencias());

        UsuarioModel novoUsuario = new UsuarioModel(
                usuarioDto.nomePessoa(),
                usuarioDto.email(),
                usuarioDto.senha(),
                usuarioDto.localizacaoUsuario(),
                usuarioDto.data(),
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                temasPreferidos
        );
        usuarioRepository.adicionarUsuario(novoUsuario);
    }
}