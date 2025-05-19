package com.eventually.service;
import com.eventually.dto.UsuarioEdicaoDto;
import com.eventually.model.UsuarioModel;
import com.eventually.repository.UsuarioRepository;

import java.util.Optional;

/**
 * Serviço que fornece a operação de editar (UPDATE do CRUD) para usuários.
 * Esta classe utiliza o {@link UsuarioRepository} para acessar e manipular os dados dos usuários.
 *
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.00
 * @since 2025-05-18
 */
public class UsuarioEdicaoService {
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor da classe {@code UsuarioEdicaoService}.
     * Inicializa o {@code UsuarioRepository} a ser utilizado.
     */
    public UsuarioEdicaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Edita um usuário existente com base no ID e nos novos atributos informados.
     * Apenas os atributos não nulos no {@link UsuarioEdicaoDto} serão atualizados no usuário.
     * Caso o usuário não seja encontrado pelo ID, será exibida uma mensagem informativa.
     *
     * @param idUsuario ID do usuário que será editado.
     * @param atributosEditados Objeto contendo os novos valores para os campos do usuário.
     */
    public void editarUsuario(int idUsuario, UsuarioEdicaoDto atributosEditados) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.buscarUsuarioPorId(idUsuario);

        usuarioOptional.ifPresentOrElse(usuario -> {
            if (atributosEditados.nomePessoa() != null) usuario.setNomePessoa(atributosEditados.nomePessoa());
            if (atributosEditados.email() != null) usuario.setEmail(atributosEditados.email());
            if (atributosEditados.senha() != null) usuario.setSenha(atributosEditados.senha());
            if (atributosEditados.localizacaoUsuario() != null) usuario.setLocalizacaoUsuario(atributosEditados.localizacaoUsuario());
            if (atributosEditados.dataNascimento() != null) usuario.setDataNascimento(atributosEditados.dataNascimento());
            if (atributosEditados.fotoUsuario() != null) usuario.setFotoUsuario(atributosEditados.fotoUsuario());
            if (atributosEditados.eventosParticipa() != null) usuario.setEventosParticipa(atributosEditados.eventosParticipa());
            if (atributosEditados.eventosOrganizados() != null) usuario.setEventosOrganizados(atributosEditados.eventosOrganizados());
            if (atributosEditados.temasPreferidos() != null) usuario.setTemasPreferidos(atributosEditados.temasPreferidos());

            System.out.println("Usuário com ID " + idUsuario + " editado com sucesso.");
        }, () -> System.out.println("Usuário com ID " + idUsuario + " não encontrado."));
    }
}
