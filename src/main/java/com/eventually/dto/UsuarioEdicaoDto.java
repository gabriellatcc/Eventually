package com.eventually.dto;
import com.eventually.model.EventoModel;
import javafx.scene.image.Image;
import java.time.LocalDate;
import java.util.List;


/**
 * DTO utilizado para representar os dados de edição de um usuário.
 * Apenas os campos não nulos serão utilizados para atualizar o {@code UsuarioModel}.
 * @author Gabriella Tavares Costa Corrêa (Criação, revisão de documentação, estrutura e revisão da parte lógica da classe)
 * @version 1.02
 * @since 2025-05-18
 */
public record UsuarioEdicaoDto(
        String nomePessoa,
        String email,
        String senha,
        String localizacaoUsuario,
        LocalDate dataNascimento,
        Image fotoUsuario,
        List<EventoModel> eventosParticipa,
        List<EventoModel> eventosOrganizados,
        PreferenciasUsuarioDto temasPreferidos,
        boolean estado
) {}