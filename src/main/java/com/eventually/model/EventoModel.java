package com.eventually.model;
import com.eventually.interfaces.Identidade;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 * A classe {@code EventoModel} representa um evento com todas as informações necessárias
 * para sua criação, exibição, edição e exclusão dentro de um sistema (CRUD de evento).
 * Ela contém atributos como nome, descricão, datas de início e término, localização, entre outros.
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.02
 * @since 2025-04-04
 */
public class EventoModel implements Identidade {
    private int idEvento;
    private String organizador;
    private String nomeEvento;
    private String fotoEvento;
    private String descricao;
    private String formato;
    private float custo;
    private String localizacao;
    private Date dataInicial;
    private Date dataFinal;
    private int qntDePessoas;
    private int classificacao;
    private boolean certificacao;

    public EventoModel(String organizador, String nomeEvento, String fotoEvento, String descricao,
                       String formato, float custo, String localizacao,
                       Date dataInicial, Date dataFinal, int qntDePessoas,
                       int classificacao, boolean certificacao) {
        this.organizador = organizador;
        this.nomeEvento = nomeEvento;
        this.fotoEvento = fotoEvento;
        this.descricao = descricao;
        this.formato = formato;
        this.custo = custo;
        this.localizacao = localizacao;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.qntDePessoas = qntDePessoas;
        this.classificacao = classificacao;
        this.certificacao = certificacao;
    }

    @Override
    public int getId() {return idEvento;}
    @Override
    public void setId(int id){this.idEvento=id;}

    public String getOrganizador() {return organizador;}
    public void setOrganizador(String organizador) {this.organizador = organizador;}
    public String getNomeEvento() {return nomeEvento;}
    public void setNomeEvento(String nomeEvento) {this.nomeEvento = nomeEvento;}
    public String getFotoEvento() {return fotoEvento;}
    public void setFotoEvento(String fotoEvento) {this.fotoEvento = fotoEvento;}
    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    public String getFormato() {return formato;}
    public void setFormato(String formato) {this.formato = formato;}
    public float getCusto() {return custo;}
    public void setCusto(float custo) {this.custo = custo;}
    public String getLocalizacao() {return localizacao;}
    public void setLocalizacao(String localizacao) {this.localizacao = localizacao;}
    public Date getDataInicial() {return dataInicial;}
    public void setDataInicial(Date dataInicial) {this.dataInicial = dataInicial;}
    public Date getDataFinal() {return dataFinal;}
    public void setDataFinal(Date dataFinal) {this.dataFinal = dataFinal;}
    public int getQntDePessoas() {return qntDePessoas;}
    public void setQntDePessoas(int qntDePessoas) {this.qntDePessoas = qntDePessoas;}
    public int getClassificacao() {return classificacao;}
    public void setClassificacao(int classificacao) {this.classificacao = classificacao;}
    public boolean isCertificacao() {return certificacao;}
    public void setCertificacao(boolean certificacao) {this.certificacao = certificacao;}

    @Override
    public String toString() {
        return "EventoModel{" +
                "Evento: '" + nomeEvento + '\'' +
                ", Organizador: '" + organizador +
                ", Data inicial: '" + dataInicial +
                ", Data final: '" + dataFinal +
                ", Localização: '" + localizacao + '\'' +
                ", Custo: R$ " + custo +
                ", Formato: '" + formato + '\'' +
                ", Descrição: '" + descricao + '\'' +
                '}';
    }
}