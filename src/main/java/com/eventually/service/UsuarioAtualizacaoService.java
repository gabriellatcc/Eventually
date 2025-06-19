package com.eventually.service;
import com.eventually.controller.LoginController;
import com.eventually.dto.UsuarioEdicaoDto;
import com.eventually.model.TemaPreferencia;
import com.eventually.model.UsuarioModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;

/**
 * Esta classe é um Singleton, garantindo que apenas uma instância de {@code UsuarioAtualizacaoService} exista em
 * toda a aplicação, é responsável por fornecer a operação de editar (UPDATE do CRUD) para usuários.
 * Esta classe utiliza a instância única de {@link UsuarioCadastroService} para acessar e manipular os dados
 * dos usuários em memória.
 * @author Gabriella Tavares Costa Corrêa (Criação,documentação, correção e revisão da parte lógica da estrutura da classe)
 * @version 1.00
 * @since 2025-05-18
 */
public final class UsuarioAtualizacaoService {
    private static UsuarioAtualizacaoService instancia;
    private final UsuarioCadastroService usuarioCadastroService;

    private AlertaService alertaService;

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Construtor privado que inicializa o {@code UsuarioCadastroService} e mpede a criação de múltiplas
     * instâncias externamente.
     */
    private UsuarioAtualizacaoService() {
        this.usuarioCadastroService = UsuarioCadastroService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioCadastroService..");
    }

    /**
     * Retorna a instância única de {@code UsuarioAtualizacaoService}, se e a instância ainda não existe, ela é criada e,
     * em caso de falha, é exibida uma mensagem no console.
     * @return a instância única de {@code UsuarioAtualizacaoService}.
     */
    public static synchronized UsuarioAtualizacaoService getInstancia() {
        sistemaDeLogger.info("Método getInstancia() chamado().");
        try {
            if (instancia == null) {
                instancia = new UsuarioAtualizacaoService();
            }
            return instancia;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao procurar nome pelo email: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Edita um usuário existente com base no ID e nos novos atributos informados, apenas os atributos não nulos no
     * {@link UsuarioEdicaoDto} serão atualizados no usuário. Caso o usuário não seja encontrado pelo ID, será exibida
     * uma mensagem informativa e, em caso de falha, é exibida uma mensagem no console.
     * @param idUsuario o ID do usuário que será editado.
     * @param atributosEditados o objeto contendo os novos valores para os campos do usuário.
     * @return {@code true} se o usuário foi editado com sucesso, {@code false} caso contrário.
     */
    public boolean editarUsuario(int idUsuario, UsuarioEdicaoDto atributosEditados) {
        sistemaDeLogger.info("Método buscarUsuarioPorId() chamado.");
        try {
            if (atributosEditados == null) {
                alertaService.alertarWarn("Edição Inválida", "Nenhum dado para edição foi fornecido.");
                return false;
            }

            Optional<UsuarioModel> usuarioOptional = usuarioCadastroService.buscarUsuarioPorId(idUsuario);

            if (usuarioOptional.isPresent()) {
                UsuarioModel usuario = usuarioOptional.get();

                if (atributosEditados.nomePessoa() != null) {
                    if (usuarioCadastroService.isRegraNomeCumprida(atributosEditados.nomePessoa())) {
                        usuario.setNomePessoa(atributosEditados.nomePessoa());
                    } else {
                        alertaService.alertarWarn("Edição Inválida", "Nome da pessoa inválido.");
                        return false;
                    }
                }
                if (atributosEditados.email() != null) {
                    if (usuarioCadastroService.isRegraEmailCumprida(atributosEditados.email())) {
                        usuario.setEmail(atributosEditados.email());
                    } else {
                        alertaService.alertarWarn("Edição Inválida", "Email inválido.");
                        return false;
                    }
                }
                if (atributosEditados.senha() != null) {
                    if (usuarioCadastroService.isRegraSenhaCumprida(atributosEditados.senha()) != null &&
                            usuarioCadastroService.isRegraSenhaCumprida(atributosEditados.senha()).values().stream().allMatch(Boolean::booleanValue)) {
                        usuario.setSenha(atributosEditados.senha());
                    } else {
                        alertaService.alertarWarn("Edição Inválida", "Senha não cumpre os requisitos.");
                        return false;
                    }
                }
                if (atributosEditados.localizacaoUsuario() != null) {
                    if (usuarioCadastroService.isRegraCidadeCumprida(atributosEditados.localizacaoUsuario())) {
                        usuario.setLocalizacaoUsuario(atributosEditados.localizacaoUsuario());
                    } else {
                        alertaService.alertarWarn("Edição Inválida", "Localização inválida.");
                        return false;
                    }
                }
                if (atributosEditados.dataNascimento() != null) {
                    if (usuarioCadastroService.isRegraDataCumprida(atributosEditados.dataNascimento())) {
                        usuario.setDataNascimento(atributosEditados.dataNascimento());
                    } else {
                        alertaService.alertarWarn("Edição Inválida", "Data de nascimento inválida (mínimo 12 anos).");
                        return false;
                    }
                }

                if (atributosEditados.temasPreferidos() != null) {
                    if (usuarioCadastroService.isRegraTemasCumprida(atributosEditados.temasPreferidos())) {
                        Set<TemaPreferencia> novosTemas = MapeamentoPreferenciasService.mapearPreferencias(atributosEditados.temasPreferidos());

                        usuario.setTemasPreferidos(novosTemas);
                    } else {
                        alertaService.alertarWarn("Edição Inválida", "Selecione pelo menos um tema de interesse.");
                        return false;
                    }
                }

                if (atributosEditados.fotoUsuario() != null) {
                    usuario.setFotoUsuario(atributosEditados.fotoUsuario());
                }
                if (atributosEditados.eventosParticipa() != null) {
                    usuario.setEventosParticipa(atributosEditados.eventosParticipa());
                }
                if (atributosEditados.eventosOrganizados() != null) {
                    usuario.setEventosOrganizados(atributosEditados.eventosOrganizados());
                }

                sistemaDeLogger.info("Usuário com ID " + idUsuario + " editado com sucesso.");
                alertaService.alertarInfo("Sucesso: Usuário editado com sucesso!");
                return true;
            } else {
                alertaService.alertarWarn("Edição Inválida", "Usuário com ID " + idUsuario + " não encontrado para edição.");
                sistemaDeLogger.info("Usuário com ID " + idUsuario + " não encontrado.");
                return false;
            }
        } catch (Exception e) {
            sistemaDeLogger.error("Erro inesperado ao editar usuário: " + e.getMessage());
            e.printStackTrace();
            alertaService.alertarErro("Erro ao editar usuário.");
            return false;
        }
    }
}