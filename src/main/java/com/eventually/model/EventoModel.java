package com.eventually.model;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * A classe {@code EventoModel} representa um objeto evento com todos os atributos necessárias
 * para sua criação como nome do organizador, nome do evento, foto do evento, descricão, localização,
 * datas de início e término, quantidade de participantes, classificação etária, presença de
 * certificado e participantes do evento.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.06
 * @since 2025-04-04
 */
public class EventoModel {
    private int idEvento;
    private UsuarioModel organizador;

    private String nome;
    private String descricao;
    private FormatoSelecionado formato;
    private String linkAcesso;  
    private String localizacao;
    private Image foto;
    private int nParticipantes;
    private LocalTime horaInicial, horaFinal;
    private LocalDate dataInicial, dataFinal;

    private Set<TemaPreferencia> temas = new HashSet<>();
    private List<UsuarioModel> participantes = new ArrayList<>();
    private List<ComentarioModel> comentarios = new ArrayList<>();

    private boolean estado =true;
    private boolean isFinalizado=false;
    public EventoModel(UsuarioModel organizador, String nomeEvento, String descricao, FormatoSelecionado formato, String linkAcesso,
                       String localizacao, Image foto, int nParticipantes, LocalDate dataInicial, LocalTime horaInicial,
                       LocalDate dataFinal, LocalTime horaFinal, Set<TemaPreferencia> temas, List<UsuarioModel> participantes, boolean estado, boolean isFinalizado, List<ComentarioModel> comentarios) {
        this.organizador = organizador;
        this.nome = nomeEvento;
        this.descricao = descricao;
        this.formato = formato;
        this.linkAcesso = linkAcesso;
        this.localizacao = localizacao;
        this.nParticipantes = nParticipantes;
        this.dataInicial = dataInicial;
        this.horaInicial = horaInicial;
        this.dataFinal = dataFinal;
        this.horaFinal = horaFinal;
        this.temas = temas;
        if (participantes != null) {
            this.participantes = participantes;
        } else {
            this.participantes = new ArrayList<>();
        }
        this.comentarios =comentarios;
        this.estado = true;//comeca ativo
        this.isFinalizado = false;//comeca não finalizado

        if (foto != null) {
            this.foto = foto;
        } else {
            this.foto = new Image(getClass().getResourceAsStream("/images/evento-padrao.jpg"));
        }
    }

    /**
     * Métodos de encapsulamento gettes e setters.
     */
    public int getId() {return idEvento;}
    public void setId(int id){this.idEvento=id;}

    public UsuarioModel getOrganizador() {return organizador;}
    public void setOrganizador(UsuarioModel organizador) {this.organizador = organizador;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public FormatoSelecionado getFormato() {return formato;}
    public void setFormato(FormatoSelecionado formato) {this.formato = formato;}

    public String getLinkAcesso() {return linkAcesso;}
    public void setLinkAcesso(String linkAcesso) {this.linkAcesso = linkAcesso;}

    public String getLocalizacao() {return localizacao;}
    public void setLocalizacao(String localizacao) {this.localizacao = localizacao;}

    public Image getFoto() {return foto;}
    public void setFoto(Image foto) {this.foto = foto;}

    public int getnParticipantes() {return nParticipantes;}
    public void setnParticipantes(int nParticipantes) {this.nParticipantes = nParticipantes;}

    public LocalDate getDataInicial() {return dataInicial;}
    public void setDataInicial(LocalDate dataInicial) {this.dataInicial = dataInicial;}

    public LocalTime getHoraInicial() {return horaInicial;}
    public void setHoraInicial(LocalTime horaInicial) {this.horaInicial = horaInicial;}

    public LocalDate getDataFinal() {return dataFinal;}
    public void setDataFinal(LocalDate dataFinal) {this.dataFinal = dataFinal;}

    public LocalTime getHoraFinal() {return horaFinal;}
    public void setHoraFinal(LocalTime horaFinal) {this.horaFinal = horaFinal;}

    public Set<TemaPreferencia> getTemas() {return temas;}
    public void setTemas(Set<TemaPreferencia> temas) {this.temas = temas;}

    public boolean isEstado() {return estado;}
    public void setEstado(boolean estado) {this.estado = estado;}

    public boolean isFinalizado() {return isFinalizado;}
    public void setFinalizado(boolean finalizado) {isFinalizado = finalizado;}

    public List<UsuarioModel> getParticipantes() {return participantes;}
    public void setParticipantes(List<UsuarioModel> participantes) {
        if (participantes != null) {
        this.participantes = participantes;
    } else {
        this.participantes = new ArrayList<>();
    }}


    public List<ComentarioModel> getComentarios() {return comentarios;}

    public void adicionarComentario(ComentarioModel comentario) {
        this.comentarios.add(comentario);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventoModel that = (EventoModel) o;
        return idEvento == that.idEvento;
    }

}