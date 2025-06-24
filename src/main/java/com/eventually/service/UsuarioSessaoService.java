package com.eventually.service;
import com.eventually.model.TemaPreferencia;
import com.eventually.model.UsuarioModel;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;

/**
 * Esta classe é um Singleton, garantindo que apenas uma instância de {@code UsuarioSessaoService} exista em toda a aplicação.
 * Serviço que autentica usuários e gerencia a sessão de usuário. Atua principalmente como um serviço de leitura (READ)
 * fornecendo acesso a informações como nome, ID, email, senha e outros atributos do {@link UsuarioModel} (por enquanto, em memória).
 * A classe acessa a coleção de usuários diretamente através do {@link UsuarioCadastroService}.
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.02
 * @since 2025-04-22
 */
public final class UsuarioSessaoService {
    private static UsuarioSessaoService instancia;

    private AlertaService alertaService =new AlertaService();

    private UsuarioCadastroService usuarioCadastroService;

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(UsuarioSessaoService.class);

    /**
     * Construtor privado que obtém a instância única de UsuarioCadastroService para acessar a lista de usuários.
     * Impede a criação de múltiplas instâncias externamente.
     */
    private UsuarioSessaoService() {
        this.usuarioCadastroService = UsuarioCadastroService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioCadastroService.");
    }

    /**
     * Retorna a instância única de {@code UsuarioSessaoService}, se não existe, ela é criada e, em caso de falha, é
     * exibida uma mensagem no console.
     * @return a instância única de {@code UsuarioSessaoService}.
     */
    public static synchronized UsuarioSessaoService getInstancia() {
        sistemaDeLogger.info("Método getInstancia() chamado.");
        try{
            if (instancia == null) {
                instancia = new UsuarioSessaoService();
            }
            return instancia;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao retornar a instância: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método valida um usuário, criando um objeto para analisar o atributo email e senha fornecidos na interface,
     * busca na lista de todos os usuários registrados aquele que possui o mesmo atributo e senha do objeto criado e, em
     * caso de falha, é exibida uma mensagem no console.
     * @return Um {@code Optional} contendo o {@code UsuarioModel} se um usuário com o email e senha fornecidos for
     * encontrado. Se nenhum usuário corresponder aos critérios, retorna um {@code Optional} vazio.
     */
    public Optional<UsuarioModel> validarUsuario(String email, String senha) {
        sistemaDeLogger.info("Método validarUsuario() chamado.");
        try {
            if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
                alertaService.alertarWarn("Login Inválido", "Email e senha não podem ser vazios.");
                return Optional.empty();
            }
            Optional<UsuarioModel> usuarioAValidar = usuarioCadastroService.getAllUsuarios().stream()
                    .filter(usuario -> usuario.getEmail().equals(email) && usuario.getSenha().equals(senha))
                    .findFirst();
            if(procurarEstado(email)) {
                return usuarioAValidar;
            } else{
                alertaService.alertarErro("A conta está inativa e não pode realizar o login.\nCrie outra conta.");
                return Optional.empty();
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao validar usuário: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao validar credenciais do usuário.");
            return Optional.empty();
        }
    }

    /**
     * Procura o objeto usuário o email do usuário e, em caso de falha, é exibida uma mensagem no console.
     * @param email o email do usuário a ser procurado.
     * @return o valor da senha da pessoa ser encontrado, ou {@code null} se não for encontrado.
     */
    public UsuarioModel procurarUsuario(String email) {
        try{
            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                    .findFirst();
            if (usuarioOptional.isPresent()) {
                return usuarioOptional.get();
            }
            else {return null;}
        } catch (Exception e) {
            sistemaDeLogger.info("Erro ao procurar o usuário pelo email: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Procura o ID de uma pessoa dado o seu email e, em caso de falha, é xibida uma mensagem no console.
     * @param email o email do usuário a ter o ID procurado.
     * @return o valor do ID da pessoa ser encontrado, ou {@code null} se não for encontrado.
     */
    public int procurarID(String email) {
        sistemaDeLogger.info("Método procurarID() chamado.");
        try {
            if (email == null || email.trim().isEmpty()) {
                alertaService.alertarWarn("Busca Inválida", "Email não pode ser vazio para procurar um ID.");
                throw new IllegalArgumentException("ID não obtido");
            }

            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                    .findFirst();

            if (usuarioOptional.isPresent()) {
                return usuarioOptional.get().getId();
            } else {
                alertaService.alertarErro("Usuário com o email informado não foi encontrado.");
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            sistemaDeLogger.info("Erro ao procurar o ID pelo email: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao procurar o ID do usuário.");
            throw new IllegalArgumentException("ID não obtido");
        }
    }

    /**
     * Procura o estado da conta de uma pessoa dado o seu email e, em caso de falha, é exibida uma mensagem no console.
     * @param email o email do usuário a ter o estado da conta procurado.
     * @return o estado da conta da pessoa ser encontrado, ou {@code null} se não for encontrado.
     */
    public Boolean procurarEstado(String email) {
        sistemaDeLogger.info("Método procurarEstado() chamado.");
        try {
            if (email == null || email.trim().isEmpty()) {
                alertaService.alertarWarn("Busca Inválida", "Email não pode ser vazio para procurar o estado da conta.");
                return null;
            }

            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                    .findFirst();

            if (usuarioOptional.isPresent()) {return usuarioOptional.get().isEstadoDoUsuario();}
            else {
                alertaService.alertarErro("Usuário com o email informado não foi encontrado.");
                return null;
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao procurar o estado da conta pelo email: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao procurar o estado da conta do usuário.");
            return null;
        }
    }

    /**
     * Procura o nome de uma pessoa dado o seu email e, em caso de falha, é xibida uma mensagem no console.
     * @param email o email do usuário a ter o nome procurado.
     * @return o nome da pessoa ser encontrado, ou {@code null} se não for encontrado.
     */
    public String procurarNome(String email) {
        sistemaDeLogger.info("Método procurarNome() chamado.");
        try {
            if (email == null || email.trim().isEmpty()) {
                alertaService.alertarWarn("Busca Inválida", "Email não pode ser vazio para procurar nome.");
                return null;
            }

            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                    .findFirst();

            if (usuarioOptional.isPresent()) {return usuarioOptional.get().getNomePessoa();}
            else {
                alertaService.alertarErro("Usuário com o email informado não foi encontrado.");
                return null;
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao procurar nome pelo email: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao procurar nome do usuário.");
            return null;
        }
    }

    /**
     * Procura a senha de uma pessoa dado o seu email e, em caso de falha, é xibida uma mensagem no console.
     * @param email o email do usuário a ter a senha procurada.
     * @return o valor da senha da pessoa ser encontrado, ou {@code null} se não for encontrado.
     */
    public String procurarSenha(String email) {
        sistemaDeLogger.info("Método procurarSenha() chamado.");
        try {
            if (email == null || email.trim().isEmpty()) {
                alertaService.alertarWarn("Busca Inválida", "Email não pode ser vazio para procurar uma senha.");
                return null;
            }

            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                    .findFirst();

            if (usuarioOptional.isPresent()) {
                return usuarioOptional.get().getSenha();
            } else {
                alertaService.alertarErro("Usuário com o email informado não foi encontrado.");
                return null;
            }
        } catch (Exception e) {
            sistemaDeLogger.info("Erro ao procurar a senha pelo email: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao procurar a senha do usuário.");
            return null;
        }
    }

    /**
     * Procura a imagem de uma pessoa dado o seu email e, em caso de falha, é exibida uma mensagem no console.
     * @param email o email do usuário a ser procurado.
     * @return a imagem da pessoa ser encontrada, ou {@code null} se não for encontrada.
     */
    public Image procurarImagem(String email) {
        sistemaDeLogger.info("Método procurarImagem() chamado.");
        try{
            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                    .findFirst();
            if (usuarioOptional.isPresent()) {return usuarioOptional.get().getFotoUsuario();
            } else {
                alertaService.alertarErro("Usuário com o email informado não foi encontrado.");
                return null;
            }
        } catch (Exception e) {
            sistemaDeLogger.info("Erro ao procurar a Imagem do usuário pelo email: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao procurar a imagem do usuário.");
            throw new IllegalArgumentException("Imagem não obtida");
        }
    }

    /**
     * Procura a cidade da conta de uma pessoa dado o seu email e, em caso de falha, é exibida uma mensagem no console.
     * @param email o email do usuário a ter a cidade procurada.
     * @return a cidade da conta da pessoa ser encontrada, ou {@code null} se não for encontrada.
     */
    public String procurarCidade(String email) {
        sistemaDeLogger.info("Método procurarCidade() chamado.");
        try {
            if (email == null || email.trim().isEmpty()) {
                alertaService.alertarWarn("Busca Inválida", "Email não pode ser vazio para procurar cidade do usuário.");
                return null;
            }

            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                    .findFirst();

            if (usuarioOptional.isPresent()) {
                return usuarioOptional.get().getLocalizacaoUsuario();
            } else {
                alertaService.alertarErro("Usuário com o email informado não foi encontrado.");
                return null;
            }
        } catch (Exception e) {
            sistemaDeLogger.info("Erro ao procurar a cidade pelo email: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao procurar a cidade do usuário.");
            return null;
        }
    }

    /**
     * Procura a data de nascimento da conta de uma pessoa dado o seu email e, em caso de falha, é exibida uma mensagem no console.
     * @param email o email do usuário a ter a cidade procurada.
     * @return a data de nascimento da conta da pessoa ser encontrada, ou {@code null} se não for encontrada.
     */
    public String procurarDataNasc(String email) {
        sistemaDeLogger.info("Método procurarDataNasc() chamado.");
        try {
            if (email == null || email.trim().isEmpty()) {
                alertaService.alertarWarn("Busca Inválida", "Email não pode ser vazio para procurar data de nascimento do usuário.");
                return null;
            }

            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                    .findFirst();

            if (usuarioOptional.isPresent()) {
                return usuarioOptional.get().getDataNascimento().toString();
            } else {
                alertaService.alertarErro("Usuário com o email informado não foi encontrado.");
                return null;
            }
        } catch (Exception e) {
            sistemaDeLogger.info("Erro ao procurar a data de nascimento pelo email: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao procurar a data de nascimento do usuário.");
            return null;
        }
    }

    /**
     * Procura os temas selecionados por uma pessoa dado o seu email e, em caso de falha, é exibida uma mensagem no console.
     *
     * @param email o email do usuário a ter a cidade procurada.
     * @return os temas selecionados por uma pessoa serem encontrados, ou {@code null} se não forem encontrados.
     */
    public Set<TemaPreferencia> procurarPreferencias(String email) {
        sistemaDeLogger.info("Método procurarDataNasc() chamado.");
        try {
            if (email == null || email.trim().isEmpty()) {
                alertaService.alertarWarn("Busca Inválida", "Email não pode ser vazio para procurar os temas selecionados pelo usuário.");
                return null;
            }

            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.getAllUsuarios()
                    .stream()
                    .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                    .findFirst();

            if (usuarioOptional.isPresent()) {
                return usuarioOptional.get().getTemasPreferidos();
            } else {
                alertaService.alertarErro("Usuário com o email informado não foi encontrado.");
                return null;
            }
        } catch (Exception e) {
            sistemaDeLogger.info("Erro ao procurar a data de nascimento pelo email: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao procurar os temas selecionados pelo usuário.");
            return null;
        }
    }
}