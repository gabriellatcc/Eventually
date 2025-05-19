package com.eventually.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<UsuarioModel> participantes = new ArrayList<>();
    private String nomeEvento;
    private String fotoEvento;
    private String descricao;
    private String formato;
    private String localizacao;
    private Date dataInicial;
    private Date dataFinal;
    private int qntDePessoasPermitidas;
    private int classificacao;
    private boolean certificacao;

    public EventoModel(UsuarioModel organizador, String nomeEvento, String fotoEvento, String descricao,
                       String formato, String localizacao, Date dataInicial, Date dataFinal,
                       int qntDePessoasPermitidas, int classificacao, boolean certificacao, List<UsuarioModel> participantes) {
        this.organizador = organizador;
        this.nomeEvento = nomeEvento;
        this.fotoEvento = fotoEvento;
        this.descricao = descricao;
        this.formato = formato;
        this.localizacao = localizacao;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.qntDePessoasPermitidas = qntDePessoasPermitidas;
        this.classificacao = classificacao;
        this.certificacao = certificacao;
        this.participantes = new ArrayList<>();
    }

    public int getId() {return idEvento;}
    public void setId(int id){this.idEvento=id;}

    public UsuarioModel getOrganizador() {return organizador;}
    public void setOrganizador(UsuarioModel organizador) {this.organizador = organizador;}

    public String getNomeEvento() {return nomeEvento;}
    public void setNomeEvento(String nomeEvento) {this.nomeEvento = nomeEvento;}

    public String getFotoEvento() {return fotoEvento;}
    public void setFotoEvento(String fotoEvento) {this.fotoEvento = fotoEvento;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public String getFormato() {return formato;}
    public void setFormato(String formato) {this.formato = formato;}

    public String getLocalizacao() {return localizacao;}
    public void setLocalizacao(String localizacao) {this.localizacao = localizacao;}

    public Date getDataInicial() {return dataInicial;}
    public void setDataInicial(Date dataInicial) {this.dataInicial = dataInicial;}

    public Date getDataFinal() {return dataFinal;}
    public void setDataFinal(Date dataFinal) {this.dataFinal = dataFinal;}

    public int getQntDePessoasPermitidas() {return qntDePessoasPermitidas;}
    public void setQntDePessoasPermitidas(int qntDePessoasPermitidas) {this.qntDePessoasPermitidas = qntDePessoasPermitidas;}

    public int getClassificacao() {return classificacao;}
    public void setClassificacao(int classificacao) {this.classificacao = classificacao;}

    public boolean isCertificacao() {return certificacao;}
    public void setCertificacao(boolean certificacao) {this.certificacao = certificacao;}

    public List<UsuarioModel> getParticipantes() {return participantes;}
    public void setParticipantes(List<UsuarioModel> participantes) {this.participantes = participantes;}
}