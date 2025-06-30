package com.eventually.model;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.util.*;

/** PASSÍVEL DE ALTERAÇÃO
 * A classe {@code UsuarioModel} contém atributos como nome da pessoa que criou a conta,
 * email, senha, localização, data de nascimento, foto de usuário, eventos que participa,
 * eventos organizados, temas preferidos.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.06
 * @since 2025-04-08
 */
public class UsuarioModel {
    private int idUsuario;
    private String nomePessoa;
    private String email;
    private String senha;
    private String localizacaoUsuario;
    private LocalDate dataNascimento;
    private Image fotoUsuario;
    private List<EventoModel> eventosParticipa = new ArrayList<>();
    private List<EventoModel> eventosOrganizados = new ArrayList<>();
    private Set<TemaPreferencia> temasPreferidos = new HashSet<>();
    private boolean estadoDoUsuario=true;

    public UsuarioModel(String nomePessoa, String email, String senha, String localizacaoUsuario,
                        LocalDate dataNascimento, Image fotoUsuario, List<EventoModel> eventosCriados, List<EventoModel> eventosInscritos,
                        Set<TemaPreferencia> temasPreferidos, boolean estadoDoUsuario)
    {
        this.nomePessoa = nomePessoa;
        this.email = email;
        this.senha = senha;
        this.localizacaoUsuario = localizacaoUsuario;
        this.dataNascimento = dataNascimento;
        this.fotoUsuario = new Image(getClass().getResourceAsStream("/images/icone-padrao-usuario.png"));
        this.eventosOrganizados = eventosCriados;
        this.eventosParticipa = eventosInscritos;
        this.temasPreferidos = temasPreferidos;
        this.estadoDoUsuario = true; //comeca ativa
    }

    /**
     * Métodos de encapsulamento getters e setters
     */
    public int getId() {return idUsuario;}
    public void setId(int id) {this.idUsuario = id;}

    public String getNomePessoa() {return nomePessoa;}
    public void setNomePessoa(String nomePessoa) {this.nomePessoa = nomePessoa;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getSenha() {return senha;}
    public void setSenha(String senha) {this.senha = senha;}

    public LocalDate getDataNascimento() {return dataNascimento;}
    public void setDataNascimento(LocalDate dataNascimento) {this.dataNascimento = dataNascimento;}

    public String getLocalizacaoUsuario() {return localizacaoUsuario;}
    public void setLocalizacaoUsuario(String localizacaoUsuario) {this.localizacaoUsuario = localizacaoUsuario;}

    public Image getFotoUsuario() {return fotoUsuario;}
    public void setFotoUsuario(Image fotoUsuario) {this.fotoUsuario = fotoUsuario;}

    public List<EventoModel> getEventosOrganizados() {return eventosOrganizados;}
    public void setEventosOrganizados(List<EventoModel> eventosOrganizados) {this.eventosOrganizados = eventosOrganizados;}

    public List<EventoModel> getEventosParticipa() {return eventosParticipa;}
    public void setEventosParticipa(List<EventoModel> eventosParticipa) {this.eventosParticipa = eventosParticipa;}

    public Set<TemaPreferencia> getTemasPreferidos() {return temasPreferidos;}
    public void setTemasPreferidos(Set<TemaPreferencia> temasPreferidos) {this.temasPreferidos = temasPreferidos;}

    public boolean isEstadoDoUsuario() {return estadoDoUsuario;}
    public void setEstadoDoUsuario(boolean estadoDoUsuario) {this.estadoDoUsuario = estadoDoUsuario;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioModel that = (UsuarioModel) o;
        return idUsuario == that.idUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }


}