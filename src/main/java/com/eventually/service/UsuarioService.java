package com.eventually.service;

import com.eventually.model.UsuarioModel;
import com.eventually.repository.UsuarioRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Serviço que fornece operações de CRUD (Criar, Ler, Atualizar, Deletar) para usuários.
 * Esta classe utiliza o {@link UsuarioRepository} para acessar e manipular os dados dos usuários.
 *  @author Gabriella Tavares Costa Corrêa
 *  @version 1.00
 *  @since 2025-04-22
 */
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor da classe {@code UsuarioService}.
     * Inicializa o {@code UsuarioRepository} a ser utilizado.
     */
    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }

    /**
     * Retorna a lista de todos os usuários cadastrados.
     *
     * @return Lista de objetos {@code UsuarioModel} contendo todos os usuários registrados.
     */
    public List<UsuarioModel> listarUsuarios() {
        return usuarioRepository.getAllUsuarios();
    }

    /**
     * Cria um novo usuario e o adiciona ao repositório.
     * Este método representa a operação de criação (Create) do CRUD.
     * @param usuario Objeto {@code UsuarioModel} contendo as informações do usuario a ser criado.
     */
    public void criarUsuario(UsuarioModel usuario){
        usuarioRepository.adicionarUsuario(usuario);
        System.out.println("\n Usuario criado com sucesso!");
        System.out.println("Endereço de memória: "+System.identityHashCode(usuario));
        System.out.println(usuario);
    }

    /**
     * Lê e exibe todos os usuários cadastrados no sistema.
     * Este método representa a operação de leitura (Read) do CRUD.
     */
    public void exibirUsuarios() {
        List<UsuarioModel> usuarios = usuarioRepository.getAllUsuarios();
        if(usuarios.isEmpty())
        {
            System.out.println("Nenhum usuario cadastrado.");
            return;
        }
        for(UsuarioModel usuario: usuarios){
            System.out.println("---- Usuário ----");
            System.out.println( "Nome do utilizador do sistema: "+usuario.getNomePessoa());
            System.out.println( "Nome de usuário do utilizador: "+usuario.getNomeUsuario());
            System.out.println( "Email do utilizador: "+usuario.getEmail());
            System.out.println( "Senha do utilizador: "+usuario.getSenha());
            System.out.println( "Localização do utilizador: "+usuario.getLocalizacaoUsuario());
            System.out.println( "Data de nascimento: "+usuario.getDataNascimento());
            System.out.println( "Foto do usuário: "+usuario.getLocalizacaoUsuario());
            System.out.println( "Local da memória: "+usuario);
            System.out.println("----------------\n");
        }
    }

    /**
     * Edita um usuário existente com base no id informado.
     * Este método representa a operação de atualização (Update) do CRUD.
     *
     * @param idUsuario O ID do usuario a ser editado.
     * @param novoNomePessoa O nome da pessoa cadastrado a ser editado.
     * @param novoNomeUsuario O nome de usuário da pessoa cadastrada a ser editado.
     * @param novoEmail O email do usuário cadastrado a ser editado.
     * @param novaSenha A senha do usuário cadastrado a ser editado.
     * @param novaLocalizacaoUsuario A localização do usuário cadastrado a ser editado.
     * @param novaDataNascimento A data de nascimento do usuário cadastrado a ser editado.
     * @param novaFotoUsuario A foto de perfil do usuário cadastrado a ser editado.
     */
    public void editarUsuario(int idUsuario, String novoNomePessoa, String novoNomeUsuario,
                              String novoEmail, String novaSenha, String novaLocalizacaoUsuario,
                              Date novaDataNascimento, String novaFotoUsuario) {
        Optional<UsuarioModel> usuarioParaEditarOptional = usuarioRepository.buscarUsuarioPorId(idUsuario);
        usuarioParaEditarOptional.ifPresentOrElse(usuario -> {
            usuario.setNomePessoa(novoNomePessoa);
            usuario.setNomeUsuario(novoNomeUsuario);
            usuario.setEmail(novoEmail);
            usuario.setSenha(novaSenha);
            usuario.setLocalizacaoUsuario(novaLocalizacaoUsuario);
            usuario.setDataNascimento(novaDataNascimento);
            usuario.setFotoUsuario(novaFotoUsuario);
            System.out.println("Usuário com ID " + idUsuario + " ('" + usuario.getNomeUsuario() + "') editado com sucesso.");
        }, () -> System.out.println("Usuário com ID " + idUsuario + " não encontrado."));
    }

    /**
     *
     * @param usuarioParaExcluir
     */
    public void deletarUsuario(UsuarioModel usuarioParaExcluir) {
        if (usuarioParaExcluir == null || usuarioParaExcluir.getId() == 0) {
            System.out.println("Usuário inválido para exclusão.");
            return;
        }

        int idUsuarioParaExcluir = usuarioParaExcluir.getId();
        Optional<UsuarioModel> usuarioEncontradoOptional = usuarioRepository.buscarUsuarioPorId(idUsuarioParaExcluir);
        usuarioEncontradoOptional.ifPresentOrElse(usuario -> {
            if (usuarioRepository.removerUsuario(usuario)) {
                System.out.println("Usuario com ID " + idUsuarioParaExcluir + " ('" + usuario.getNomeUsuario() + "') deletado com sucesso.");
            } else {
                System.out.println("Erro ao deletar o usuário com ID " + idUsuarioParaExcluir + ".");
            }
        }, () -> System.out.println("Usuario com ID " + idUsuarioParaExcluir + " não encontrado."));
    }
}