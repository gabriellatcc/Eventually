package com.eventually.model;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.*;

/**
 * A classe {@code EventoModel} representa um objeto evento com todos os atributos necessárias
 * para sua criação como nome do organizador, nome do evento, foto do evento, descricão, localização,
 * datas de início e término, quantidade de participantes, classificação etária, presença de
 * certificado e participantes do evento.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.03
 * @since 2025-04-04
 */
public class EventoModel {
    private int idEvento;
    private UsuarioModel organizador;

    private String nomeEvento;
    private String descricao;
    private FormatoSelecionado formato;
    private String linkAcesso;
    private String localizacao;
    private Image fotoEvento;
    private int nParticipantes;
    private String horaInicial, horaFinal;
    private LocalDate dataInicial, dataFinal;
    private Set<TemaPreferencia> temasEvento = new HashSet<>();

    private List<UsuarioModel> participantes = new ArrayList<>();

    private boolean estadoDoEvento=true;
    private boolean isFinalizado=false;
    public EventoModel(UsuarioModel organizador, String nomeEvento, String descricao, FormatoSelecionado formato, String linkAcesso,
                       String localizacao, Image fotoEvento, int nParticipantes, LocalDate dataInicial, String horaInicial,
                       LocalDate dataFinal, String horaFinal, Set<TemaPreferencia> temasEvento, List<UsuarioModel> participantes, boolean estadoDoEvento, boolean isFinalizado) {
        this.organizador = organizador;
        this.nomeEvento = nomeEvento;
        this.descricao = descricao;
        this.formato = formato;
        this.linkAcesso = linkAcesso;
        this.localizacao = localizacao;
        this.fotoEvento = fotoEvento;
        this.nParticipantes = nParticipantes;
        this.dataInicial = dataInicial;
        this.horaInicial = horaInicial;
        this.dataFinal = dataFinal;
        this.horaFinal = horaFinal;
        this.temasEvento = temasEvento;
        this.participantes =  new ArrayList<UsuarioModel>();;
        this.estadoDoEvento = true;//comeca ativo
        this.isFinalizado = false;//comeca não finalizado
    }

    /**
     * Métodos de encapsulamento gettes e setters.
     */
    public int getId() {return idEvento;}
    public void setId(int id){this.idEvento=id;}

    public UsuarioModel getOrganizador() {return organizador;}
    public void setOrganizador(UsuarioModel organizador) {this.organizador = organizador;}

    public String getNomeEvento() {return nomeEvento;}
    public void setNomeEvento(String nomeEvento) {this.nomeEvento = nomeEvento;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public FormatoSelecionado getFormato() {return formato;}
    public void setFormato(FormatoSelecionado formato) {this.formato = formato;}

    public String getLinkAcesso() {return linkAcesso;}
    public void setLinkAcesso(String linkAcesso) {this.linkAcesso = linkAcesso;}

    public String getLocalizacao() {return localizacao;}
    public void setLocalizacao(String localizacao) {this.localizacao = localizacao;}

    public Image getFotoEvento() {return fotoEvento;}
    public void setFotoEvento(Image fotoEvento) {this.fotoEvento = fotoEvento;}

    public int getnParticipantes() {return nParticipantes;}
    public void setnParticipantes(int nParticipantes) {this.nParticipantes = nParticipantes;}

    public LocalDate getDataInicial() {return dataInicial;}
    public void setDataInicial(LocalDate dataInicial) {this.dataInicial = dataInicial;}

    public String getHoraInicial() {return horaInicial;}
    public void setHoraInicial(String horaInicial) {this.horaInicial = horaInicial;}

    public LocalDate getDataFinal() {return dataFinal;}
    public void setDataFinal(LocalDate dataFinal) {this.dataFinal = dataFinal;}

    public String getHoraFinal() {return horaFinal;}
    public void setHoraFinal(String horaFinal) {this.horaFinal = horaFinal;}

    public Set<TemaPreferencia> getTemasEvento() {return temasEvento;}
    public void setTemasEvento(Set<TemaPreferencia> temasEvento) {this.temasEvento = temasEvento;}

    public boolean isEstadoDoEvento() {return estadoDoEvento;}
    public void setEstadoDoEvento(boolean estadoDoEvento) {this.estadoDoEvento = estadoDoEvento;}

    public boolean isFinalizado() {return isFinalizado;}
    public void setFinalizado(boolean finalizado) {isFinalizado = finalizado;}

    public List<UsuarioModel> getParticipantes() {return participantes;}
    public void setParticipantes(List<UsuarioModel> participantes) {this.participantes = participantes;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventoModel that = (EventoModel) o;
        return idEvento == that.idEvento;
    }

    @Override
    public String toString() {
        return "EventoModel{" +
                "idEvento=" + idEvento +
                ", organizador=" + organizador +
                ", nomeEvento='" + nomeEvento + '\'' +
                ", descricao='" + descricao + '\'' +
                ", formato=" + formato +
                ", linkAcesso=" + linkAcesso +
                ", localizacao='" + localizacao + '\'' +
                ", fotoEvento=" + fotoEvento +
                ", nParticipantes=" + nParticipantes +
                ", dataInicial=" + dataInicial +
                ", horaInicial=" + horaInicial +
                ", dataFinal=" + dataFinal +
                ", horaFinal=" + horaFinal +
                ", temasEvento=" + temasEvento +
                ", estadoDoEvento=" + estadoDoEvento +
                ", isFinalizado=" + isFinalizado +
                ", participantes=" + participantes +
                '}';
    }
}