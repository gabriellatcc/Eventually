package com.eventually.model;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.*;

/** PASSÍVEL DE ALTERAÇÃO
 * A classe {@code UsuarioModel} contém atributos como nome da pessoa que criou a conta,
 * email, senha, localização, data de nascimento, foto de usuário, eventos que participa,
 * eventos organizados, temas preferidos.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.07
 * @since 2025-04-08
 */
public class UsuarioModel {
    private int idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String cidade;
    private LocalDate dataNascimento;
    private Image foto;
    private List<EventoModel> eventosInscrito = new ArrayList<>();
    private List<EventoModel> eventosOrganizados = new ArrayList<>();
    private Set<TemaPreferencia> temasPreferidos = new HashSet<>();
    private boolean estado =true;

    public UsuarioModel(String nome, String email, String senha, String cidade,
                        LocalDate dataNascimento, Image foto, List<EventoModel> eventosCriados, List<EventoModel> eventosInscritos,
                        Set<TemaPreferencia> temasPreferidos, boolean estado)
    {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cidade = cidade;
        this.dataNascimento = dataNascimento;
        if (foto != null) {
            this.foto = foto;
        } else {
            this.foto = new Image(getClass().getResourceAsStream("/images/icone-padrao-usuario.png"));
        }
        this.eventosOrganizados = eventosCriados;
        this.eventosInscrito = eventosInscritos;
        this.temasPreferidos = temasPreferidos;
        this.estado = true; //comeca ativa
    }

    /**
     * Métodos de encapsulamento getters e setters
     */
    public int getId() {return idUsuario;}
    public void setId(int id) {this.idUsuario = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getSenha() {return senha;}
    public void setSenha(String senha) {this.senha = senha;}

    public LocalDate getDataNascimento() {return dataNascimento;}
    public void setDataNascimento(LocalDate dataNascimento) {this.dataNascimento = dataNascimento;}

    public String getCidade() {return cidade;}
    public void setCidade(String cidade) {this.cidade = cidade;}

    public Image getFoto() {return foto;}
    public void setFoto(Image foto) {this.foto = foto;}

    public List<EventoModel> getEventosOrganizados() {return eventosOrganizados;}
    public void setEventosOrganizados(List<EventoModel> eventosOrganizados) {this.eventosOrganizados = eventosOrganizados;}

    public List<EventoModel> getEventosInscrito() {return eventosInscrito;}
    public void setEventosInscrito(List<EventoModel> eventosInscrito) {this.eventosInscrito = eventosInscrito;}

    public Set<TemaPreferencia> getTemasPreferidos() {return temasPreferidos;}
    public void setTemasPreferidos(Set<TemaPreferencia> temasPreferidos) {this.temasPreferidos = temasPreferidos;}

    public boolean isEstado() {return estado;}
    public void setEstado(boolean estado) {this.estado = estado;}

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