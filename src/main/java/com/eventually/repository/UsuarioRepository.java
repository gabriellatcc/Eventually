package com.eventually.repository;
import com.eventually.model.UsuarioModel;

import java.util.*;

/**
 * Esta classe {@code UsuarioRepository} é para gerenciar a coleção de usuários em memória,
 * fornece métodos para acessar e modificar a lista de usuários.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.02
 * @since 2025-04-22
 */
public class UsuarioRepository {
    /**
     * Retorna a lista de todos os usuários armazenados.
     * @return Uma lista de objetos {@code UsuarioModel}.
     */
    private Set<UsuarioModel> listaUsuarios = new HashSet<>(50);

    /**
     * Adiciona um novo usuário à lista de usuário.
     *
     * @param usuario O objeto {@code UsuarioModel} a ser adicionado.
     */
    public void adicionarUsuario(UsuarioModel usuario) {
        int id = System.identityHashCode(usuario);
        usuario.setId(id);
        listaUsuarios.add(usuario);
        System.out.println("UR: Usuário adicionado com ID: " + id + " | HashSet size: " + listaUsuarios.size());
        System.out.println("UR: Endereço (hash) do objeto na memória: " + Integer.toHexString(id));
    }

    /**
     * Remove um usuário da lista de usuário.
     *
     * @param usuario O objeto {@code UsuarioModel} a ser removido.
     * @return {@code true} se o usuário foi removido com sucesso, {@code false} caso contrário.
     */
    public boolean removerUsuario(UsuarioModel usuario) {
        return listaUsuarios.remove(usuario);
    }

    /**
     * Busca um usuário na lista pelo seu ID.
     *
     * @param id O ID do usuário a ser buscado.
     * @return Um {@code Optional} contendo o {@code UsuarioModel} correspondente ao ID,
     * ou um {@code Optional} vazio se não encontrado.
     */
    public Optional<UsuarioModel> buscarUsuarioPorId(int id) {
        return listaUsuarios.stream()
                .filter(usuario -> usuario.getId() == id)
                .findFirst();
    }

    /**
     * Método de encapsulamento getter para a lista de usuários.
     * @return Uma lista de objetos {@code UsuarioModel}.
     */
    public Set<UsuarioModel> getAllUsuarios() {
        return listaUsuarios;
    }
}