package com.eventually.backend.model;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * A classe {@code EventoModel} representa um evento com todas as informações necessárias
 * para sua criação, exibição, edição e exclusão dentro de um sistema (CRUD de evento).
 * <p>Ela contém atributos como nome, descricão, datas de início e término, localização, entre outros.</p>
 * @author Gabriella Tavares Costa Corrêa
 * @version 1.0
 * @since 2025-04-04
 */
public class EventoModel {
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

    public static class EventoManager {
        private List<EventoModel> listaEventos = new ArrayList<>();

        /**
         * Retorna a lista de eventos cadastrados no sistema.
         * <p>Esse método pode ser utilizado por outras classes, como interfaces gráficas
         * ou serviços,para acessar os eventos armazenados em memória.</p>
         * @return Lista de objetos {@code EventoModel} contendo todos os eventos registrados.
         */
        public List<EventoModel> getListEventos() {return listaEventos;}

        /**
         * Adiciona um novo evento à lista de eventos.
         * Este método representa a operação de criação (Create) do CRUD.
         * @param evento Objeto {@code Model} contendo as informações do evento a ser criado.
         */
        public void criarEvento(EventoModel evento)
        {
            listaEventos.add(evento);
            System.out.println("\n Evento criado com sucesso!");
            System.out.println("Endereço de memória: "+System.identityHashCode(evento));
            System.out.println(evento);
        }

        /**
         * Lê e exibe todos os eventos cadastrados no sistema.
         * Este método representa a operação de leitura (Read) do CRUD.
         */
        public void lerEvento()
        {
            if(listaEventos.isEmpty())
            {
                System.out.println("Nenhum evento cadastrado");
                return;
            }
            for(EventoModel evento : listaEventos)
            {
                System.out.println("---- Evento ----");
                System.out.println("Nome: " + evento.getNomeEvento());
                System.out.println("Organizador: " + evento.getOrganizador());
                System.out.println("Descrição: " + evento.getDescricao());
                System.out.println("Formato: " + evento.getFormato());
                System.out.println("Custo: " + evento.getCusto());
                System.out.println("Localização: " + evento.getLocalizacao());
                System.out.println("Data inicial: " + evento.getDataInicial());
                System.out.println("Data final: " + evento.getDataFinal());
                System.out.println("Quantidade de pessoas: " + evento.getQntDePessoas());
                System.out.println("Classificação: " + evento.getClassificacao());
                System.out.println("Certificação: " + (evento.isCertificacao() ? "Sim" : "Não"));
                System.out.println("Local na memória: " + evento);
                System.out.println("----------------\n");
            }
        }

        /**
         * Edita um evento existente com base no nome buscado.
         * Este método representa a operação de atualização (Update) do CRUD.
         *
         * @param nomeBusca
         * @param novoNome
         * @param novaDataInicial
         * @param novaDataFinal
         */
        public void editarEvento(String nomeBusca, String novoNome, Date novaDataInicial, Date novaDataFinal) {
            for (EventoModel evento : listaEventos) {
                if (evento.getNomeEvento().equalsIgnoreCase(nomeBusca)) {
                    evento.setNomeEvento(novoNome);
                    evento.setDataInicial(novaDataInicial);
                    evento.setDataFinal(novaDataFinal);

                    System.out.println("Evento " + evento.getNomeEvento() + " de " + evento.getDataInicial() + " a " + evento.getDataFinal() + " editado.");
                    return;
                }
            }
            System.out.println("Evento não encontrado.");
        }

        /**
         * Remove um evento da lista com base no nome informado.
         * Este método representa a operação de exclusão (Delete) do CRUD.
         *
         * @param nomeBusca
         */
        public void deletarEvento(String nomeBusca)
        {
            Iterator<EventoModel> iterator = listaEventos.iterator();
            while (iterator.hasNext())
            {
                EventoModel evento = iterator.next();
                if (evento.getNomeEvento().equalsIgnoreCase(nomeBusca))
                {
                    iterator.remove();
                    System.out.println("Evento "+evento.getNomeEvento()+" de "+evento.getDataInicial()+" a "+evento.getDataFinal()+" deletado.");
                    return;
                }
            }
            System.out.println("Evento não encontrado.");
        }
    }
}