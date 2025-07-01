package com.eventually.service;

import com.eventually.controller.*;
import com.eventually.model.EventoModel;
import com.eventually.model.UsuarioModel;
import com.eventually.view.*;
import com.eventually.view.modal.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/** PASSÍVEL DE ALTERÇÕES
 * Serviço responsável por gerenciar a navegação entre as diferentes telas da aplicação, centraliza a lógica de
 * inicialização de telas e controladores de telas para evitar a duplicação de código em diferentes classes
 * controladores.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.08
 * @since 2025-06-19
 */
public class NavegacaoService {
    private final Stage primaryStage;

    private UsuarioSessaoService usuarioSessaoService;
    private EventoExclusaoService eventoExclusaoService;

    private AlertaService alertaService =new AlertaService();

    private final TelaService telaService;

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(NavegacaoService.class);

    /**
     * Construtor para o NavegacaoService.
     * @param primaryStage o palco principal da aplicação, onde as cenas serão definidas.
     */
    public NavegacaoService(Stage primaryStage) {
        this.usuarioSessaoService = UsuarioSessaoService.getInstancia();
        this.eventoExclusaoService = EventoExclusaoService.getInstancia();
        sistemaDeLogger.info("Inicializado e conectado ao UsuarioSessaoService e EventoExclusaoService.");

        this.primaryStage = primaryStage;
        this.telaService = new TelaService();
    }

    /**
     * Navega para a tela de Login e limpa a sessão atual (se houver) e redireciona para a tela inicial de login.
     */
    public void navegarParaLogin() {
        sistemaDeLogger.info("Navegando para a tela de Login.");
        try {
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView, primaryStage);
            loginView.setLoginController(loginController);

            Scene loginScene = new Scene(loginView, telaService.medirWidth(), telaService.medirHeight());
            loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Login");
            primaryStage.setScene(loginScene);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela de Registro de Usuário.
     */
    public void navegarParaRegistro() {
        sistemaDeLogger.info("Navegando para a tela de Registro.");
        try {
            RegisterView registerView = new RegisterView();
            RegisterController registerController = new RegisterController(registerView, primaryStage);
            registerView.setRegisterController(registerController);

            Scene sceneRegister = new Scene(registerView, telaService.medirWidth(), telaService.medirHeight());
            sceneRegister.getStylesheets().add(getClass().getResource("/styles/register-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Registro do Usuário");
            primaryStage.setScene(sceneRegister);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela inicial (HomeView) após um login bem-sucedido.
     * @param usuarioAutenticado O modelo de usuário autenticado, necessário para inicializar a HomeView.
     */
    public void navegarParaHome(UsuarioModel usuarioAutenticado) {
        sistemaDeLogger.info("Navegando para a tela inicial (HomeView) para o usuário: " + usuarioAutenticado.getNomePessoa());
        try {
            HomeView homeView = new HomeView();
            HomeController homeController = new HomeController(usuarioAutenticado.getEmail(), homeView, primaryStage);
            homeView.setHomeController(homeController);

            Scene sceneHomeView = new Scene(homeView, telaService.medirWidth(), telaService.medirHeight());
            sceneHomeView.getStylesheets().add(getClass().getResource("/styles/home-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Início");
            primaryStage.setScene(sceneHomeView);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela inicial (HomeView): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela de Meus Eventos.
     * @param emailUsuario o e-mail do usuário logado, necessário para carregar os eventos.
     */
    public void navegarParaMeusEventos(String emailUsuario) {
        sistemaDeLogger.info("Navegando para a tela de Meus Eventos para o usuário: " + emailUsuario);
        try {
            MyEventsView myEventsView = new MyEventsView();
            MyEventsController myEventsController = new MyEventsController(emailUsuario, myEventsView, primaryStage);
            myEventsView.setMyEventsViewController(myEventsController);

            Scene myEventsScene = new Scene(myEventsView, telaService.medirWidth(), telaService.medirHeight());
            myEventsScene.getStylesheets().add(getClass().getResource("/styles/my-events-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Meus Eventos");
            primaryStage.setScene(myEventsScene);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Meus Eventos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela de Configurações do Usuário.
     * @param emailUsuario o e-mail do usuário logado, necessário para carregar as configurações.
     */
    public void navegarParaConfiguracoes(String emailUsuario) {
        sistemaDeLogger.info("Navegando para a tela de Configurações para o usuário: " + emailUsuario);
        try {
            SettingsView settingsView = new SettingsView();
            SettingsController settingsController = new SettingsController(emailUsuario, settingsView, primaryStage);
            settingsView.setSettingsController(settingsController);

            Scene settingsScene = new Scene(settingsView, telaService.medirWidth(), telaService.medirHeight());
            settingsScene.getStylesheets().add(getClass().getResource("/styles/settings-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Configurações");
            primaryStage.setScene(settingsScene);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Configurações: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navega para a tela de Programação do Usuário.
     * @param emailUsuario o e-mail do usuário logado.
     */
    public void navegarParaProgramacao(String emailUsuario) {
        sistemaDeLogger.info("Navegando para a tela de Programação para o usuário: " + emailUsuario);
        try {
            UserScheduleView userScheduleView = new UserScheduleView();
            UserScheduleController userScheduleController = new UserScheduleController(emailUsuario, userScheduleView, primaryStage);
            userScheduleView.setUserScheduleController(userScheduleController);

            Scene sceneUserSchedule = new Scene(userScheduleView, telaService.medirWidth(), telaService.medirHeight());
            sceneUserSchedule.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());

            primaryStage.setTitle("Eventually - Programação do Usuário");
            primaryStage.setScene(sceneUserSchedule);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Programação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Sair", abre um modal que aguarda confirmação para
     * saída da sessão e, em caso de falha na abertura do modal, é exibida uma mensagem no console.
     */
    public void abrirModalEncerrarSessao() {
        LogoutConfirmModal confirmModal = new LogoutConfirmModal();

        boolean usuarioConfirmou = confirmModal.showAndWait(primaryStage);
        if (usuarioConfirmou) {
            processarSaida();
        } else {
            System.out.println("O usuário cancelou o encerramento da sessão.");
        }
    }

    /**
     * Retornando para a tela de login e, em caso de erro, é exibida
     * uma mensagem no console.
     */
    private void processarSaida() {
        try {
            navegarParaLogin();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao navegar para a tela de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Criar evento", navegando para a tela de criação de eventos e, em
     * caso de erro, é exibida uma mensagem no console.
     */
    public void abrirModalCriarEvento(String emailUsuario, Runnable onSucesso) {
        sistemaDeLogger.info("Método abrirModalCriarEvento() chamado.");
        try {
            CriaEventoModal modal=new CriaEventoModal();
            CriaEventoController modalController= new CriaEventoController(emailUsuario,modal);
            modalController.setOnSucesso(onSucesso);
            modal.setCriaEventoController(modalController);
            Stage modalStage = new Stage();

            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);

            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

            Scene modalScene = new Scene(modal, modalStage.getWidth()/2,  modalStage.getHeight()/2);

            modalStage.setOnShown(event -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                modalStage.setX((screenBounds.getWidth() - modalStage.getWidth()) / 2);
                modalStage.setY((screenBounds.getHeight() - modalStage.getHeight()) / 2);
            });

            modalScene.setFill(Color.TRANSPARENT);
            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
            modalStage.setScene(modalScene);

            modalStage.showAndWait();
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao abrir modal para Criar EventoH: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Alterar", abre um modal que aguarda alteração para alguma
     * informação do usuário e, em caso de falha na abertura do modal, é exibida uma mensagem no console.
     */
    public void abrirModalMudanca(SettingsView settingsView, String email, String valor) {
        sistemaDeLogger.info("Método abrirModalMudanca() chamado.");
        try{
            MudancaModal modal=new MudancaModal();
            MudancaController modalController= new MudancaController(settingsView,email,valor,modal);
            modal.setMudancaControlador(modalController);
            Stage modalStage = new Stage();

            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);

            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

            Scene modalScene = new Scene(modal, modalStage.getWidth()/2,  modalStage.getHeight()/2);

            modalStage.setOnShown(event -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                modalStage.setX((screenBounds.getWidth() - modalStage.getWidth()) / 2);
                modalStage.setY((screenBounds.getHeight() - modalStage.getHeight()) / 2);
            });

            modalScene.setFill(Color.TRANSPARENT);
            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
            modalStage.setScene(modalScene);

            modalStage.showAndWait();
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao abrir modal para mudar informação do usuário: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no botão "Alterar" para imagem, abre um modal que aguarda alteração para icone
     * do usuário e, em caso de falha na abertura do modal, é exibida uma mensagem no console.
     */
    public void abrirMudancaImagemModal(SettingsView settingsView, String email, String valor) {
        sistemaDeLogger.info("Método abrirModalMudanca() chamado.");
        try{
            MudancaImagemModal modalImagem = new MudancaImagemModal();
            MudancaImagemController modalController = new MudancaImagemController(settingsView,email,valor,modalImagem);
            modalImagem.setMudancaImagemController(modalController);
            Stage modalStage = new Stage();

            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);

            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

            Scene modalScene = new Scene(modalImagem, modalStage.getWidth()/2,  modalStage.getHeight()/2);

            modalStage.setOnShown(event -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                modalStage.setX((screenBounds.getWidth() - modalStage.getWidth()) / 2);
                modalStage.setY((screenBounds.getHeight() - modalStage.getHeight()) / 2);
            });

            modalScene.setFill(Color.TRANSPARENT);
            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
            modalStage.setScene(modalScene);

            modalStage.showAndWait();

            File imagemSelecionada = modalController.getArquivoFinal();

            if (imagemSelecionada != null) {
                System.out.println("O usuário selecionou e salvou o arquivo: " + imagemSelecionada.getAbsolutePath());
            } else {
                System.out.println("O usuário fechou o modal sem salvar uma imagem.");
            }
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao abrir modal para mudar imagem do usuário: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Neste método é manipulado o clique no cartão de evento da tela de início e, em
     * caso de erro, é exibida uma mensagem no console.
     */
    public void abrirModalVerEvento(String emailRecebido, HomeView.EventoH eventoH) {
        sistemaDeLogger.info("Método abrirModalVerEvento() chamado.");
        try {
            List<EventoModel> listaDeEventosCriados = usuarioSessaoService.procurarEventosCriados(emailRecebido);
            List<EventoModel> listaDeEventosInscritos = usuarioSessaoService.procurarEventosInscritos(emailRecebido);

            EventoModal modal = new EventoModal();

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);
            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

            EventoController modalController = new EventoController(
                    emailRecebido,
                    modal,
                    eventoH,
                    listaDeEventosCriados,
                    listaDeEventosInscritos,
                    modalStage
            );
            modal.setInscricaoController(modalController);


            Scene modalScene = new Scene(modal);

            modalStage.setOnShown(event -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                modalStage.setX((screenBounds.getWidth() - modalStage.getWidth()) / 2);
                modalStage.setY((screenBounds.getHeight() - modalStage.getHeight()) / 2);
            });

            modalScene.setFill(Color.TRANSPARENT);
            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
            modalStage.setScene(modalScene);

            modalStage.showAndWait();
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao abrir modal para exibir evento: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void abrirEsqueceuSenhaModal() {
        sistemaDeLogger.info("Método abrirModalEsqueceuSenha() chamado.");
        try{
            EsqueceuSenhaModal modal=new EsqueceuSenhaModal();
            EsqueceuSenhaController modalController= new EsqueceuSenhaController(modal);
            modal.setForgotPasswordController(modalController);
            Stage modalStage = new Stage();

            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);

            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

            Scene modalScene = new Scene(modal, modalStage.getWidth()/2,  modalStage.getHeight()/2);

            modalStage.setOnShown(event -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                modalStage.setX((screenBounds.getWidth() - modalStage.getWidth()) / 2);
                modalStage.setY((screenBounds.getHeight() - modalStage.getHeight()) / 2);
            });

            modalScene.setFill(Color.TRANSPARENT);
            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
            modalStage.setScene(modalScene);

            modalStage.showAndWait();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao abrir o modal: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public void abrirModalEditarFiltros(String emailRecebido) {
        sistemaDeLogger.info("Método abrirModalEditarFiltros() chamado.");
        try{
            ModalEditaTemas modal=new ModalEditaTemas();
            EditaTemasController modalController= new EditaTemasController(modal,emailRecebido);
            modal.setEditaTemasController(modalController);
            Stage modalStage = new Stage();

            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);

            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

            Scene modalScene = new Scene(modal, modalStage.getWidth()/2,  modalStage.getHeight()/2);

            modalStage.setOnShown(event -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                modalStage.setX((screenBounds.getWidth() - modalStage.getWidth()) / 2);
                modalStage.setY((screenBounds.getHeight() - modalStage.getHeight()) / 2);
            });

            modalScene.setFill(Color.TRANSPARENT);
            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
            modalStage.setScene(modalScene);

            modalStage.showAndWait();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao abrir o modal: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public void abrirModalEdicao(HomeView.EventoH eventoH) {
        sistemaDeLogger.info("Método abrirModalEdicao() chamado.");
        try {
            EditaEventoModal modal=new EditaEventoModal();
            EditaEventoController modalController= new EditaEventoController(modal,eventoH);
            modal.setEditaEventoController(modalController);
            Stage modalStage = new Stage();

            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);

            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

            Scene modalScene = new Scene(modal, modalStage.getWidth()/2,  modalStage.getHeight()/2);

            modalStage.setOnShown(event -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                modalStage.setX((screenBounds.getWidth() - modalStage.getWidth()) / 2);
                modalStage.setY((screenBounds.getHeight() - modalStage.getHeight()) / 2);
            });

            modalScene.setFill(Color.TRANSPARENT);
            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
            modalStage.setScene(modalScene);

            modalStage.showAndWait();
        } catch (Exception ex) {
            sistemaDeLogger.error("Erro ao abrir modal para editar Evento: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void abrirModalConfimarExclusao(HomeView.EventoH eventoH) {
        ModalConfirmarExclusao confirmModal = new ModalConfirmarExclusao();
        int id = eventoH.id();

        boolean usuarioConfirmou = confirmModal.showAndWait(primaryStage);
        if (usuarioConfirmou) {

            eventoExclusaoService.alterarEstadoDoEvento(id,false);
            alertaService.alertarInfo("Evento excluído com sucesso!");
        } else {
            System.out.println("O usuário excluiu o evento.");
        }
    }

    public void abrirModalCancInscricao(HomeView.EventoH eventoH) {
        CancelaInscricaoModal confirmModal = new CancelaInscricaoModal();
        int id = eventoH.id();

        boolean usuarioConfirmou = confirmModal.showAndWait(primaryStage);
        if (usuarioConfirmou) {

            eventoExclusaoService.alterarEstadoDoEvento(id,false);
            alertaService.alertarInfo("Evento excluído com sucesso!");
        } else {
            System.out.println("O usuário cancelou a inscricao do evento.");
        }
    }

    public void abrirModalParticipantes(HomeView.EventoH eventoH) {

    }
}