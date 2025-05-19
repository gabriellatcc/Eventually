package com.eventually.service;

import com.eventually.model.UsuarioModel;
import com.eventually.repository.UsuarioRepository;

import java.util.Optional;

/**
 * Serviço que fornece a operação de deletar (DELETE do CRUD) para usuários.
 * Esta classe utiliza o {@link UsuarioRepository} para acessar e manipular os dados dos usuários.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.00
 * @since 2025-05-18
 */
public class UsuarioExclusaoService {
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor da classe {@code UsuarioExclucaoService}.
     * Inicializa o {@code UsuarioRepository} a ser utilizado.
     */
    public UsuarioExclusaoService() {
        this.usuarioRepository = new UsuarioRepository();
    }


    /**
     * Deleta um usupario do repositório com base no ID do {@code UsuarioModel} fornecido.
     * Este método representa a operação de exclusão (Delete) do CRUD.
     *
     * @param usuarioParaExcluir O objeto {@code UsuarioModel} a ser deletado.
     */
    public void deletarUsuario(UsuarioModel usuarioParaExcluir)
    {
        if (usuarioParaExcluir == null || usuarioParaExcluir.getId() == 0) {
            System.out.println("UDelservice: Usuário inválido para exclusão.");
            return;
        }

        int idUsuarioParaExcluir = usuarioParaExcluir.getId();
        Optional<UsuarioModel> usuarioEncontradoOptional = usuarioRepository.buscarUsuarioPorId(idUsuarioParaExcluir);

        usuarioEncontradoOptional.ifPresentOrElse(usuario -> {
            if (usuarioRepository.removerUsuario(usuario)) {
                System.out.println("UDelservice: Usuário com ID " + idUsuarioParaExcluir + " ('" + usuario.getNomePessoa() + "') deletado com sucesso.");
            } else {
                System.out.println("UDelservice: Erro ao deletar o usuário com ID " + idUsuarioParaExcluir + ".");
            }
        }, () -> System.out.println("UDelservice: Usuário com ID " + idUsuarioParaExcluir + " não encontrado."));
    }
}
