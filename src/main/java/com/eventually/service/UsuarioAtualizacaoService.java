package com.eventually.service;

import com.eventually.model.EventoModel;
import com.eventually.model.Comunidade;
import com.eventually.model.UsuarioModel;
import com.eventually.view.HomeView;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Esta classe é um Singleton, garantindo que apenas uma instância de {@code UsuarioAtualizacaoService} exista em
 * toda a aplicação, é responsável por fornecer a operação de editar (UPDATE do CRUD) para usuários.
 * Esta classe utiliza a instância única de {@link UsuarioCadastroService} para acessar e manipular os dados
 * dos usuários em memória.
 * @author Gabriella Tavares Costa Corrêa (Criação,documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.07
 * @since 2025-05-18
 */
public final class UsuarioAtualizacaoService {
    private static UsuarioAtualizacaoService instancia;

    private UsuarioCadastroService usuarioCadastroService;
    private UsuarioSessaoService usuarioSessaoService;
    private EventoLeituraService eventoLeituraService;
    private AlertaService alertaService = new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(UsuarioAtualizacaoService.class);

    private UsuarioAtualizacaoService() {
        this.usuarioCadastroService = UsuarioCadastroService.getInstancia();
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        this.eventoLeituraService = EventoLeituraService.getInstancia();

        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService e UsuarioCadastroService.");
    }

    /**
     * Retorna a instância única de {@code UsuarioAtualizacaoService}, se ainda não existe, ela é criada e, em caso de
     * falha, é exibida uma mensagem no console.
     * @return a instância única de {@code UsuarioAtualizacaoService}.
     */
    public static synchronized UsuarioAtualizacaoService getInstancia() {
       try{
            if (instancia == null) {
                instancia = new UsuarioAtualizacaoService();
            }
            return instancia;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao retornar a instância."+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Atualiza o nome de um usuário específico.
     * @param idUsuario o ID do usuário.
     * @param novoNome o novo nome para o usuário.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarNome(int idUsuario, String novoNome) {
        Optional<UsuarioModel> usuarioOpt = buscarUsuarioParaAtualizacao(idUsuario);
        if (usuarioOpt.isEmpty()) return false;

        if (usuarioCadastroService.isRegraNomeCumprida(novoNome)) {
            usuarioOpt.get().setNome(novoNome);
            notificarSucesso("Nome", idUsuario);
            return true;
        } else {
            alertaService.alertarWarn("Edição Inválida", "Nome da pessoa inválido.");
            return false;
        }
    }

    /**
     * Atualiza o email de um usuário específico, garantindo que o novo email não esteja em uso.
     * @param idUsuario o ID do usuário.
     * @param novoEmail o novo email para o usuário.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarEmail(int idUsuario, String novoEmail) {
        Optional<UsuarioModel> usuarioOpt = buscarUsuarioParaAtualizacao(idUsuario);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Optional<UsuarioModel> emailExistenteOpt = usuarioSessaoService.buscarUsuarioPorEmail(novoEmail);

        if (emailExistenteOpt.isPresent() && emailExistenteOpt.get().getId() != idUsuario) {
            alertaService.alertarWarn("Email Indisponível", "Este email já está cadastrado para outro usuário.");
            sistemaDeLogger.warn("Tentativa de alterar email para um já existente ('{}') pelo usuário ID {}.", novoEmail, idUsuario);
            return false;
        }

        if (usuarioCadastroService.isRegraEmailCumprida(novoEmail)) {
            usuarioOpt.get().setEmail(novoEmail);
            notificarSucesso("Email", idUsuario);
            return true;
        } else {
            alertaService.alertarWarn("Edição Inválida", "O formato do email fornecido é inválido.");
            return false;
        }
    }

    /**
     * Atualiza a senha de um usuário específico.
     * @param idUsuario o ID do usuário.
     * @param novaSenha a nova senha para o usuário.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarSenha(int idUsuario, String novaSenha) {
        Optional<UsuarioModel> usuarioOpt = buscarUsuarioParaAtualizacao(idUsuario);
        if (usuarioOpt.isEmpty()) return false;

        if (usuarioCadastroService.isRegraSenhaCumprida(novaSenha) != null &&
                usuarioCadastroService.isRegraSenhaCumprida(novaSenha).values().stream().allMatch(Boolean::booleanValue)) {
            usuarioOpt.get().setSenha(novaSenha);
            notificarSucesso("Senha", idUsuario);
            return true;
        } else {
            alertaService.alertarWarn("Edição Inválida", "Senha não cumpre os requisitos.");
            return false;
        }
    }

    /**
     * Atualiza a localização (cidade) de um usuário específico.
     * @param idUsuario o ID do usuário.
     * @param novaCidade a nova cidade para o usuário.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarCidade(int idUsuario, String novaCidade) {
        Optional<UsuarioModel> usuarioOpt = buscarUsuarioParaAtualizacao(idUsuario);
        if (usuarioOpt.isEmpty()) return false;

        if (usuarioCadastroService.isRegraCidadeCumprida(novaCidade)) {
            usuarioOpt.get().setCidade(novaCidade);
            notificarSucesso("Cidade", idUsuario);
            return true;
        } else {
            alertaService.alertarWarn("Edição Inválida", "Localização inválida.");
            return false;
        }
    }

    /**
     * Atualiza a data de nascimento de um usuário específico.
     * @param idUsuario o ID do usuário.
     * @param novaData a nova data de nascimento para o usuário.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarDataNascimento(int idUsuario, LocalDate novaData) {
        Optional<UsuarioModel> usuarioOpt = buscarUsuarioParaAtualizacao(idUsuario);
        if (usuarioOpt.isEmpty()) return false;

        if (usuarioCadastroService.isRegraDataCumprida(novaData)) {
            usuarioOpt.get().setDataNascimento(novaData);
            notificarSucesso("Data de Nascimento", idUsuario);
            return true;
        } else {
            alertaService.alertarWarn("Edição Inválida", "Data de nascimento inválida (mínimo 12 anos).");
            return false;
        }
    }

    /**
     * Atualiza a foto de um usuário específico.
     * @param idUsuario o ID do usuário.
     * @param novaFoto a nova foto para o usuário.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarFoto(int idUsuario, Image novaFoto) {
        Optional<UsuarioModel> usuarioOpt = buscarUsuarioParaAtualizacao(idUsuario);
        if (usuarioOpt.isEmpty()) return false;
        else{
            usuarioOpt.get().setFoto(novaFoto);
            notificarSucesso("Foto", idUsuario);
            return true;
        }
    }

    /**
     * Busca um usuário pelo ID e lida com o caso de não ser encontrado.
     * Evita repetição de código nos métodos públicos.
     * @param idUsuario o ID do usuário a ser buscado.
     * @return um Optional contendo o usuário, ou um Optional vazio se não encontrado.
     */
    private Optional<UsuarioModel> buscarUsuarioParaAtualizacao(int idUsuario) {
        Optional<UsuarioModel> usuarioOpt = usuarioCadastroService.buscarUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty()) {
            alertaService.alertarWarn("Edição Inválida", "Usuário com ID " + idUsuario + " não encontrado.");
            sistemaDeLogger.warn("Tentativa de edição para usuário não existente com ID: {}", idUsuario);
        }
        return usuarioOpt;
    }

    /**
     * Exibe um alerta de sucesso e registra o log.
     */
    private void notificarSucesso(String campo, int idUsuario) {
        sistemaDeLogger.info("{} do usuário com ID {} alterado(a) com sucesso.", campo, idUsuario);
        alertaService.alertarInfo(campo + " alterado(a) com sucesso!");
    }

    /**
     * Atualiza as preferências de tema de um usuário específico.
     * @param idUsuario o ID do usuário a ser atualizado.
     * @param novosTemas o novo conjunto de temas de preferência.
     * @return true se a atualização for bem-sucedida, false caso contrário.
     */
    public boolean atualizarTemas(int idUsuario, Set<Comunidade> novosTemas) {
        Optional<UsuarioModel> usuarioOpt = buscarUsuarioParaAtualizacao(idUsuario);

        if (usuarioOpt.isEmpty()) {
            sistemaDeLogger.warn("Tentativa de atualizar temas para usuário inexistente com ID: {}", idUsuario);
            return false;
        }

        try {
            UsuarioModel usuario = usuarioOpt.get();
            usuario.setComunidades(novosTemas);

            sistemaDeLogger.info("Temas do usuário com ID {} atualizados com sucesso.", idUsuario);
            notificarSucesso("Temas", idUsuario);
            return true;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao atualizar temas para o usuário com ID {}: {}", idUsuario, e.getMessage());
            alertaService.alertarErro("Ocorreu um erro inesperado ao salvar suas preferências.");
            return false;
        }
    }

    public void atualizarEventoParticipado(String email, HomeView.EventoH eventoH) {
        UsuarioModel usuario = usuarioSessaoService.procurarUsuario(email);

        Optional<EventoModel> eventoOptional = eventoLeituraService.procurarEventoPorId(eventoH.id());

        if (usuario != null && eventoOptional.isPresent()) {
            EventoModel eventoReal = eventoOptional.get();
            List<EventoModel> eventosInscritos = usuario.getEventosInscrito();

            if (!eventosInscritos.contains(eventoReal)) {
                eventosInscritos.add(eventoReal);
                sistemaDeLogger.info("Evento '{}' adicionado à lista de inscrições do usuário '{}'.", eventoReal.getNome(), email);
            } else {
                sistemaDeLogger.warn("Usuário '{}' já estava inscrito no evento '{}'. Nenhuma ação necessária.", email, eventoReal.getNome());
            }

        } else {
            if (usuario == null) {
                sistemaDeLogger.error("Falha ao atualizar evento participado: usuário '{}' não encontrado.", email);
            }
            if (eventoOptional.isEmpty()) {
                sistemaDeLogger.error("Falha ao atualizar evento participado: evento com ID '{}' não encontrado.", eventoH.id());
            }
        }
    }

    /**
     * Remove a inscrição de um usuário em um evento específico.
     * Este método atualiza a lista de eventos inscritos do próprio usuário.
     * @param email o email do usuário cuja inscrição será removida.
     * @param eventoH o evento (no formato record/DTO) do qual o usuário está se desinscrevendo.
     */
    public void removerInscricao(String email, HomeView.EventoH eventoH) {
        UsuarioSessaoService sessaoService = UsuarioSessaoService.getInstancia();
        UsuarioModel usuario = sessaoService.procurarUsuario(email);

        if (usuario != null) {
            List<EventoModel> eventosInscritos = usuario.getEventosInscrito();

            boolean removido = eventosInscritos.removeIf(evento -> evento.getId() == eventoH.id());

            if (removido) {
                System.out.println("Evento ID " + eventoH.id() + " removido da lista de inscrições do usuário " + email);
            }
        } else {
            System.out.println("Erro: Usuário com email " + email + " não encontrado para remover inscrição.");
        }
    }
}