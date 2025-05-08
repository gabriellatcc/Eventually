package com.eventually.model;

import java.util.Date;

/**
 * A classe {@code UsuarioModel} contém atributos como nome do usuário, nome da pessoa que criou a conta,
 * email, senha, localizaçãoUsuario, entre outros.</p>
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.01
 * @since 2025-04-08
 */
public class UsuarioModel {
    private int idUsuario;
    private String nomePessoa;
    private String nomeUsuario;
    private String email;
    private String senha;
    private String localizacaoUsuario;
    private Date dataNascimento;
    private String fotoUsuario;

    public UsuarioModel(String nomePessoa, String nomeUsuario,
                        String email, String senha, String localizacaoUsuario,
                        Date dataNascimento, String fotoUsuario)
    {
        this.nomePessoa = nomePessoa;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.localizacaoUsuario = localizacaoUsuario;
        this.dataNascimento = dataNascimento;
        this.fotoUsuario = fotoUsuario;
    }

    public int getId() {return idUsuario;}
    public void setId(int id) {this.idUsuario = id;}
    public String getNomePessoa() {return nomePessoa;}
    public void setNomePessoa(String nomePessoa) {this.nomePessoa = nomePessoa;}
    public String getNomeUsuario() {return nomeUsuario;}
    public void setNomeUsuario(String nomeUsuario) {this.nomeUsuario = nomeUsuario;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getSenha() {return senha;}
    public void setSenha(String senha) {this.senha = senha;}
    public String getLocalizacaoUsuario() {return localizacaoUsuario;}
    public void setLocalizacaoUsuario(String localizacaoUsuario) {this.localizacaoUsuario = localizacaoUsuario;}
    public Date getDataNascimento() {return dataNascimento;}
    public void setDataNascimento(Date dataNascimento) {this.dataNascimento = dataNascimento;}
    public String getFotoUsuario() {return fotoUsuario;}
    public void setFotoUsuario(String fotoUsuario) {this.fotoUsuario = fotoUsuario;}

    @Override
    public String toString() {
        return "UsuarioModel{" +
                " Nome da pessoa='" + nomePessoa + '\'' +
                " Nome de usuario: '" + nomeUsuario + '\'' +
                " Email: '" + email + '\'' +
                " Senha: '" + senha + '\'' +
                " Localização: '" + localizacaoUsuario + '\'' +
                " Data Nascimento: " + dataNascimento +
                " Foto do Usuario: '" + fotoUsuario + '\'' +
                '}';
    }


}
