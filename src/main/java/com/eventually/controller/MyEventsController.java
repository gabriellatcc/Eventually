package com.eventually.controller;

import com.eventually.service.AlertaService;
import com.eventually.service.NavegacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** PASSÍVEL DE ALTERAÇÕES
 * Classe controladora da tela de eventos participados e criados pelo usuário, é responsável pela comunicação
 * com o backend.
 * Contém métodos privados para que os acesso sejam somente por esta classe.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.00
 * @since 2025-06-18
 */
public class MyEventsController {
    private final MyEventsView myEventsView;
    private final Stage primaryStage;

    private NavegacaoService navegacaoService;
    private UsuarioSessaoService usuarioSessaoService;

    private String emailRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(LoginController.class);

    public record Evento(int id, String titulo, String local, LocalDate data, String hora) {}

    /**
     * Construtor do {@code MyEventsController}, inicializa a view de eventos do usuário.
     * @param email o email do usuário logado.
     * @param myEventsView a interface de visualização de eventos associada.
     * @param primaryStage o palco principal da aplicação.
     */
    public MyEventsController(String email ,MyEventsView myEventsView, Stage primaryStage) {
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService.");

        this.emailRecebido = email;

        this.myEventsView = myEventsView;
        this.myEventsView.setMyEventsViewController(this);

        this.primaryStage = primaryStage;
        this.navegacaoService = new NavegacaoService(primaryStage);

        configManipuladoresDeEventoMeusEventos();
        carregarDadosIniciais();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de "Meus Eventos" e, em caso de falha,
     * exibe uma mensagem no console.
     */
    private void configManipuladoresDeEventoMeusEventos() {
        sistemaDeLogger.info("Método configManipuladoresDeEventoMeusEventos() chamado.");
        try {
            myEventsView.getBtnMeusEventos().setOnAction(e -> processarNavegacaoMeusEventos());
            myEventsView.getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
            myEventsView.getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));
            myEventsView.getBtnProgramacao().setOnAction(e -> navegacaoService.navegarParaProgramacao(emailRecebido));
            myEventsView.getBtnSair().setOnAction(e -> navegacaoService.abrirModalEncerrarSessao());
            myEventsView.getBtnNovoEvento().setOnAction(e -> navegacaoService.processarCriacaoEvento());

            myEventsView.getBtnEventosCriados().setOnAction(e -> carregarEventosCriados());
            myEventsView.getBtnEventosFinalizados().setOnAction(e -> carregarEventosFinalizados());

            if (myEventsView.getGrupoDatas() != null) {
                myEventsView.getGrupoDatas().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                    if (newToggle != null) {
                        processarSelecaoData(newToggle);
                    }
                });
            }

        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores da tela de Meus Eventos: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega os dados iniciais do usuário e a lista de eventos na view.
     */
    private void carregarDadosIniciais() {
        myEventsView.setNomeUsuario(definirNome(emailRecebido));
        myEventsView.setEmailUsuario(emailRecebido);
        myEventsView.setAvatar(definirImagem(emailRecebido));

        configurarSeletorDeDatas();
        processarSelecaoData(myEventsView.getGrupoDatas().getSelectedToggle());
    }

    /**
     * Configura o seletor de datas na view com os próximos 7 dias.
     */
    private void configurarSeletorDeDatas() {
        if (myEventsView.getSeletorDataContainer() == null) {
            sistemaDeLogger.warn("O container do seletor de datas (seletorDataContainer) não foi encontrado na MyEventsView.");
            return;
        }

        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatoDiaSemana = DateTimeFormatter.ofPattern("EEE", new Locale("pt", "BR"));
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd", new Locale("pt", "BR"));

        for (int i = 0; i < 7; i++) {
            LocalDate dataIteracao = hoje.plusDays(i);
            String diaSemana = dataIteracao.format(formatoDiaSemana).toUpperCase();
            String dia = dataIteracao.format(formatoDia);

            ToggleButton btnData = new ToggleButton(diaSemana + "\n" + dia);
            btnData.setUserData(dataIteracao);
            btnData.setToggleGroup(myEventsView.getGrupoDatas());
            btnData.getStyleClass().add("date-button");

            myEventsView.getSeletorDataContainer().getChildren().add(btnData);

            if (i == 0) {
                btnData.setSelected(true);
            }
        }
    }

    /**
     * Processa a seleção de uma nova data, buscando e carregando os eventos correspondentes.
     * @param toggle O ToggleButton que foi selecionado.
     */
    private void processarSelecaoData(Toggle toggle) {
        if (toggle == null) return;

        LocalDate dataSelecionada = (LocalDate) toggle.getUserData();
        sistemaDeLogger.info("Data selecionada: " + dataSelecionada);

        if (myEventsView.getBtnEventosCriados().isSelected()) {
            carregarEventosCriados(dataSelecionada);
        } else {
            carregarEventosFinalizados(dataSelecionada);
        }
    }

    private void carregarEventosCriados() { processarSelecaoData(myEventsView.getGrupoDatas().getSelectedToggle());}
    private void carregarEventosFinalizados() {processarSelecaoData(myEventsView.getGrupoDatas().getSelectedToggle());}
    private void carregarEventosCriados(LocalDate data) {
        sistemaDeLogger.info("Buscando eventos criados para a data: " + data);
        List<Evento> eventos = buscarEventos(true, data);

        myEventsView.carregarEventos(eventos, (ActionEvent e) -> {
            Button btn = (Button) e.getSource();
            int eventoId = (int) btn.getUserData();
            processarEdicaoEvento(eventoId);
        });
    }

    private void carregarEventosFinalizados(LocalDate data) {
        sistemaDeLogger.info("Buscando eventos finalizados para a data: " + data);
        List<Evento> eventos = buscarEventos(false, data);

        myEventsView.carregarEventos(eventos, (ActionEvent e) -> {
            Button btn = (Button) e.getSource();
            int eventoId = (int) btn.getUserData();
            processarEdicaoEvento(eventoId);
        });
    }

    private void processarEdicaoEvento(int eventoId) {
        sistemaDeLogger.info("Botão 'Editar Evento' clicado para o evento ID: " + eventoId);
        alertaService.alertarInfo("Funcionalidade de edição para o evento " + eventoId + " ainda não implementada.");
    }

    /**
     * Busca os eventos do usuário a partir do serviço de backend.
     * @param criados Se true, busca eventos criados pelo usuário, senão, busca eventos finalizados/participados.
     * @param data A data para a qual os eventos devem ser buscados.
     * @return Uma lista de eventos.
     */
    private List<Evento> buscarEventos(boolean criados, LocalDate data) {
        // IMPLEMENTAR: lógica para buscar eventos
        return new ArrayList<>();
    }

    /**
     * Este método retorna a imagem de perfil do usuário.
     * @param email informado no cadastro.
     * @return retorna a imagem do usuário relativo ao email cadastrado.
     */
    private Image definirImagem(String email) {
        sistemaDeLogger.info("Método definirImagem() chamado.");
        try {
            return usuarioSessaoService.procurarImagem(email);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter imagem do usuário."+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método retorna o nome do usuário.
     * @param email informado no cadastro.
     * @return retorna o nome do usuário relativo ao email cadastrado.
     */
    public String definirNome(String email) {
        sistemaDeLogger.info("Método definirNome() chamado.");
        try {
            return usuarioSessaoService.procurarNome(email);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter nome do usuário: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método exibe mensagem informando que o usuário já está na tela de Meus Eventos
     * e, em caso de falha na exibição do alerta, é exibida uma mensagem de erro no console.
     */
    private void processarNavegacaoMeusEventos() {
        sistemaDeLogger.info("Método processarNavegacaoMeusEventos() chamado.");
        try{
            sistemaDeLogger.info("Botão de Meus Eventos clicado!");
            alertaService.alertarInfo("Você já está na tela de Meus Eventos!");
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao ir para tela de início: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
}
