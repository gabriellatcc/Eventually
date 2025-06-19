package com.eventually.service;

import com.eventually.controller.LoginController;
import com.eventually.model.UsuarioModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Esta classe é um Singleton, garantindo que apenas uma instância de {@code UsuarioExclusaoService} exista em toda a aplicação.
 * Serviço que fornece a operação de exclusão (DELETE do CRUD) para usuários, ele altera o estado do usuário para false.
 * Esta classe utiliza a instância única de {@link UsuarioCadastroService} para acessar e manipular os dados
 * dos usuários em memória.
 * @author Gabriella Tavares Costa Corrêa (Criação, documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.00
 * @since 2025-05-18
 */
public final class UsuarioExclusaoService {
    private static UsuarioExclusaoService instancia;
    private UsuarioCadastroService usuarioCadastroService;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Construtor privado que obtém a instância única de UsuarioCadastroService para acessar a lista de usuários e
     * immpede a criação de múltiplas instâncias externamente.
     */
    private UsuarioExclusaoService() {
        this.usuarioCadastroService = UsuarioCadastroService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioCadastroService.");
    }

    /**
     * Retorna a instância única de {@code UsuarioExclusaoService}, se não existe, ela é criada e, em caso de falha, é
     * exibida uma mensagem no console.
     * @return a instância única de {@code UsuarioExclusaoService}.
     */
    public static synchronized UsuarioExclusaoService getInstancia() {
        sistemaDeLogger.info("Método getInstancia() chamado.");
        try{
            if (instancia == null) {
                instancia = new UsuarioExclusaoService();
            }
            return instancia;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao retornar a instância: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Altera o estado de um usuário (ativo/inativo) e, em caso de falha na atualização de estado do usuário, é exibida
     * uma mensagem apontando o erro no console.
     * @param idUsuario o ID do usuário cujo estado será alterado.
     * @param novoEstado o novo estado (true para ativo, false para inativo).
     * @return {@code true} se o estado foi alterado com sucesso, {@code false} caso contrário.
     */
    public boolean alterarEstadoDoUsuario(int idUsuario, boolean novoEstado) {
        sistemaDeLogger.info("Método alterarEstadoDoUsuario() chamado.");
        try {
            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.buscarUsuarioPorId(idUsuario);

            if (usuarioOptional.isPresent()) {
                UsuarioModel usuario = usuarioOptional.get();
                usuario.setEstadoDoUsuario(novoEstado);
                sistemaDeLogger.info("Estado do usuário com ID " + idUsuario + " alterado para " + (novoEstado ? "ATIVO" : "INATIVO") + ".");
                alertaService.alertarInfo("Sucesso: Estado do usuário alterado!");
                return true;
            } else {
                alertaService.alertarWarn("Alteração de Estado Inválida", "Usuário com ID " + idUsuario + " não encontrado.");
                sistemaDeLogger.info("Usuário com ID " + idUsuario + " não encontrado para alterar estado.");
                return false;
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Erro inesperado ao alterar estado do usuário: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao alterar estado do usuário.");
            return false;
        }
    }
}
