package com.eventually.controller;

import com.eventually.service.AlertaService;
import com.eventually.service.NavegacaoService;
import com.eventually.service.UsuarioSessaoService;
import com.eventually.view.UserScheduleView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** PASSÍVEL A ALTERAÇÃO
 * Classe controladora da tela de programação do usuário, é responsável pela comunicação
 * da tela de programação com o backend.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.03
 * @since 2025-04-25
 */
public class UserScheduleController {
    private final UserScheduleView userScheduleView;
    private final Stage primaryStage;

    private UsuarioSessaoService usuarioSessaoService;
    private NavegacaoService navegacaoService;

    private String emailRecebido;

    private AlertaService alertaService =new AlertaService();

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(UserScheduleController.class);

    /**
     * Construtor do {@code UserScheduleController}, inicializa a view de programação de usuário.
     * @param userScheduleView a interface de programação associada
     * @param primaryStage o palco principal da aplicação
     */
    public UserScheduleController(String email, UserScheduleView userScheduleView, Stage primaryStage) {
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService.");

        this.emailRecebido = email;

        this.userScheduleView = userScheduleView;
        this.userScheduleView.setUserScheduleController(this);

        this.primaryStage = primaryStage;
        this.navegacaoService = new NavegacaoService(primaryStage);

        configManipuladoresDeEventoUS();
        carregarDadosIniciais();
    }

    /**
     * Este método configura os manipuladores de eventos para os botões da tela de programação e, em caso de falha, exibe uma
     * mensagem no console.
     */
    private void configManipuladoresDeEventoUS() {
        sistemaDeLogger.info("Método configManipuladoresEventoRegistro() chamado.");
        try {
            userScheduleView.getBarraBuilder().getBtnProgramacao().setOnAction(e -> processarNavegacaoProgramacao());

            userScheduleView.getBarraBuilder().getBtnInicio().setOnAction(e -> navegacaoService.navegarParaHome(usuarioSessaoService.procurarUsuario(emailRecebido)));
            userScheduleView.getBarraBuilder().getBtnMeusEventos().setOnAction(e -> navegacaoService.navegarParaMeusEventos(emailRecebido));
            userScheduleView.getBarraBuilder().getBtnConfiguracoes().setOnAction(e -> navegacaoService.navegarParaConfiguracoes(emailRecebido));

            userScheduleView.getBarraBuilder().getBtnSair().setOnAction(e -> navegacaoService.abrirModalEncerrarSessao());

          //  userScheduleView.getBtnNovoEvento().setOnAction(e -> navegacaoService.abrirModalCriarEvento(emailRecebido, this::processarCarregamentoEventos));

            userScheduleView.getLbEmailUsuario().setText(emailRecebido);
            userScheduleView.getLbNomeUsuario().setText(definirNome(emailRecebido));
            userScheduleView.setAvatarImagem(definirImagem(emailRecebido));

            if (userScheduleView.getGrupoDatas() != null) {
                userScheduleView.getGrupoDatas().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                    if (newToggle != null) {
                        processarSelecaoData(newToggle);
                    }
                });
            }

        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao configurar manipuladores de programação: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método retorna a imagem de perfil do usuário, se foi recém cadastrado no sistema, terá a imagem padrão.
     * @param email informado no cadastro.
     * @return retorna a imagem do usuário relativo ao email cadastrado.
     */
    private Image definirImagem(String email) {
        sistemaDeLogger.info("Método definirImagem() chamado.");
        try {
            Image imagemUsuario = usuarioSessaoService.procurarImagem(email);
            return imagemUsuario;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter imagem do usuário."+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Este método retorna o nome do usuário e, em caso de falha, é exibida uma mensagem de erro no console.
     * @param email informado no cadastro.
     * @return retorna o nome do usuário relativo ao email cadastrado.
     */
    private String definirNome(String email) {
        sistemaDeLogger.info("Método definirNome() chamado.");
        try {
            String nome = usuarioSessaoService.procurarNome(email);
            return nome;
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao obter nome do usuário: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Carrega os dados iniciais do usuário e a lista de eventos na view.
     */
    private void carregarDadosIniciais() {
        configurarSeletorDeDatas();
        processarSelecaoData(userScheduleView.getGrupoDatas().getSelectedToggle());
    }

    /**
     * Este método exibe mensagem informando que o usuário já está na tela de programação e, em caso de falha na exibição
     * do aviso, é exibida uma mensagem de erro no console.
     */
    private void processarNavegacaoProgramacao() {
        sistemaDeLogger.info("Método processarNavegacaoProgramacao() chamado.");
        try{
            sistemaDeLogger.info("Botão de Programação clicado!");
            alertaService.alertarInfo("Você já está na tela de programação!");
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao ir para tela de programação: "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Configura o seletor de datas na view com os próximos 7 dias.
     */
    private void configurarSeletorDeDatas() {
        if (userScheduleView.getSeletorDataContainer() == null) {
            sistemaDeLogger.warn("O container do seletor de datas (seletorDataContainer) não foi encontrado na UserScheduleView.");
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
            btnData.setToggleGroup(userScheduleView.getGrupoDatas());
            btnData.getStyleClass().add("date-button");

            userScheduleView.getSeletorDataContainer().getChildren().add(btnData);

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
    }
}