package com.eventually.service;
import com.eventually.dto.AutenticarUsuarioDto;
import com.eventually.model.UsuarioModel;
import com.eventually.repository.UsuarioRepository;
import java.util.Optional;

/**
 * Serviço que autentica usuários ao inserir valores na janela de login.
 * Esta classe usa o {@link UsuarioRepository} para acessar e manipular os dados dos usuários.
 *  @author Gabriella Tavares Costa Corrêa
 *  @version 1.01
 *  @since 2025-04-22
 */
public class AutenticacaoUsuarioService
{
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor da classe {@code AutenticacaoUsuarioService}.
     * Inicializa o {@code UsuarioRepository} a ser utilizado.
     */
    public AutenticacaoUsuarioService(AutenticarUsuarioDto usuarioASerAutenticado)
    {
        this.usuarioRepository = new UsuarioRepository();
    }

    /**
     * Este método valida um usuário, criando um objeto para analisar o atributo email e senha fornecidos na interface,
     * busca na lista de todos os usuários registrados aquele que possui o mesmo atributo e senha do objeto criado
     *
     * @return Um {@code Optional} contendo o {@code UsuarioModel} se um usuário com o email
     * e senha fornecidos for encontrado. Se nenhum usuário corresponder aos critérios,
     * retorna um {@code Optional} vazio.
     */
    public Optional<UsuarioModel> validarUsuario(String email, String senha)
    {
        AutenticarUsuarioDto validacaoUsuario=new AutenticarUsuarioDto(email, senha);

        return usuarioRepository.getAllUsuarios().stream()
                .filter(usuario -> usuario.getEmail().equals(email) && usuario.getSenha().equals(senha))
                .findFirst();
    }
}