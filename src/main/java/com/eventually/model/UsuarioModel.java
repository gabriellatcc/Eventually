package com.eventually.model;
import java.time.LocalDate;
import java.util.*;

/**
 * A classe {@code UsuarioModel} contém atributos como nome da pessoa que criou a conta,
 * email, senha, localização, data de nascimento, foto de usuário, eventos que participa,
 * eventos organizados, temas preferidos.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.04
 * @since 2025-04-08
 */
public class UsuarioModel {
    private int idUsuario;
    private String nomePessoa;
    private String email;
    private String senha;
    private String localizacaoUsuario;
    private LocalDate dataNascimento;
    private String fotoUsuario;
    private List<EventoModel> eventosParticipa = new ArrayList<>();
    private List<EventoModel> eventosOrganizados = new ArrayList<>();
    private Set<TemaPreferencia> temasPreferidos = new HashSet<>();

    public UsuarioModel(String nomePessoa, String email, String senha, String localizacaoUsuario,
                        LocalDate dataNascimento, String fotoUsuario, List<EventoModel> eventosOrganizados, List<EventoModel> eventosParticipa,
                        Set<TemaPreferencia> temasPreferidos)
    {
        this.nomePessoa = nomePessoa;
        this.email = email;
        this.senha = senha;
        this.localizacaoUsuario = localizacaoUsuario;
        this.dataNascimento = dataNascimento;
        this.fotoUsuario = fotoUsuario;
        this.eventosOrganizados = new ArrayList<EventoModel>();
        this.eventosParticipa = new ArrayList<EventoModel>();
        this.temasPreferidos = temasPreferidos;
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

    public String getFotoUsuario() {return fotoUsuario;}
    public void setFotoUsuario(String fotoUsuario) {this.fotoUsuario = fotoUsuario;}

    public List<EventoModel> getEventosOrganizados() {return eventosOrganizados;}
    public void setEventosOrganizados(List<EventoModel> eventosOrganizados) {this.eventosOrganizados = eventosOrganizados;}

    public List<EventoModel> getEventosParticipa() {return eventosParticipa;}
    public void setEventosParticipa(List<EventoModel> eventosParticipa) {this.eventosParticipa = eventosParticipa;}

    public Set<TemaPreferencia> getTemasPreferidos() {return temasPreferidos;}
    public void setTemasPreferidos(Set<TemaPreferencia> temasPreferidos) {this.temasPreferidos = temasPreferidos;}
}