package com.eventually.service;

import com.eventually.controller.*;
import com.eventually.dto.FiltroDto;
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
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Serviço responsável por gerenciar a navegação entre as diferentes telas da aplicação, centraliza a lógica de
 * inicialização de telas e controladores de telas para evitar a duplicação de código em diferentes classes
 * controladores.
 * @author Gabriella Tavares Costa Corrêa (Construção da documentação, da classe e revisão da parte lógica da estrutura)
 * @version 1.13
 * @since 2025-06-19
 */
public class NavegacaoService {
    private final Stage primaryStage;

    private UsuarioSessaoService usuarioSessaoService;
    private EventoExclusaoService eventoExclusaoService;

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

        this.telaService.aplicarTamanhoRestaurar(primaryStage, 1280, 720);
    }

    /**
     * Define uma nova cena no palco principal, garantindo que o tamanho da janela seja preservado.
     * @param scene A nova cena a ser exibida.
     * @param title O novo título para a janela.
     */
    private void setSceneAndPreserveSize(Scene scene, String title) {
        double currentWidth = primaryStage.getWidth();
        double currentHeight = primaryStage.getHeight();
        boolean isMaximized = primaryStage.isMaximized();

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);

        if (isMaximized) {
            primaryStage.setMaximized(true);
        } else {
            primaryStage.setWidth(currentWidth);
            primaryStage.setHeight(currentHeight);
        }
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

            Scene loginScene = new Scene(loginView);
            loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());

            setSceneAndPreserveSize(loginScene, "Eventually - Login");
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

            Scene sceneRegister = new Scene(registerView);
            sceneRegister.getStylesheets().add(getClass().getResource("/styles/register-styles.css").toExternalForm());

            setSceneAndPreserveSize(sceneRegister, "Eventually - Registro do Usuário");
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
        sistemaDeLogger.info("Navegando para a tela inicial (HomeView) para o usuário: " + usuarioAutenticado.getNome());
        try {
            HomeView homeView = new HomeView();
            HomeController homeController = new HomeController(usuarioAutenticado.getEmail(), homeView, primaryStage);
            homeView.setHomeController(homeController);

            Scene sceneHomeView = new Scene(homeView);
            sceneHomeView.getStylesheets().add(getClass().getResource("/styles/home-styles.css").toExternalForm());

            setSceneAndPreserveSize(sceneHomeView, "Eventually - Início");
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

            Scene myEventsScene = new Scene(myEventsView);
            myEventsScene.getStylesheets().add(getClass().getResource("/styles/my-events-styles.css").toExternalForm());

            setSceneAndPreserveSize(myEventsScene, "Eventually - Meus Eventos");
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

            Scene settingsScene = new Scene(settingsView);
            settingsScene.getStylesheets().add(getClass().getResource("/styles/settings-styles.css").toExternalForm());

            setSceneAndPreserveSize(settingsScene, "Eventually - Configurações");
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

            Scene sceneUserSchedule = new Scene(userScheduleView);
            sceneUserSchedule.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());

            setSceneAndPreserveSize(sceneUserSchedule, "Eventually - Programação do Usuário");
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
    public void abrirModalVerEvento(String emailRecebido, HomeView.EventoH eventoH, Runnable refreshCallback) {
        sistemaDeLogger.info("Método abrirModalVerEvento() chamado.");
        try {
            EventoModal modalView = new EventoModal();

            EventoController controller = new EventoController(emailRecebido, modalView, eventoH, primaryStage, refreshCallback);
            modalView.setInscricaoController(controller);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);
            modalStage.initStyle(StageStyle.TRANSPARENT);

            Scene modalScene = new Scene(modalView);

            modalStage.setOnShown(event -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                modalStage.setX((screenBounds.getWidth() - modalStage.getWidth()) / 2);
                modalStage.setY(((screenBounds.getHeight() - modalStage.getHeight()) / 2)+40);
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
            EditaComunidadeModal modal=new EditaComunidadeModal();
            EditaComunidadeController modalController= new EditaComunidadeController(modal,emailRecebido);
            modal.setEditaComunidadesController(modalController);
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

    public void abrirModalEdicao(HomeView.EventoH eventoH, Runnable aoSalvarCallback) {
        sistemaDeLogger.info("Método abrirModalEdicao() chamado.");
        try {
            EditaEventoModal modal=new EditaEventoModal();
            EditaEventoController modalController= new EditaEventoController(modal,eventoH, aoSalvarCallback);
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

    public boolean abrirModalConfimarExclusao() {
        ConfirmarExclusaoModal confirmModal = new ConfirmarExclusaoModal();

        boolean usuarioConfirmou = confirmModal.showAndWait(primaryStage);

        return usuarioConfirmou;
    }

    public boolean abrirModalCancInscricao() {
        CancelaInscricaoModal confirmModal = new CancelaInscricaoModal();

        return confirmModal.showAndWait(primaryStage);
    }

    /**
     * Este método cria e exibe o modal de compartilhamento para um evento específico.
     * @param eventoParaCompartilhar Os dados do evento que foi clicado.
     */
    public void abrirModalDeCompartilhamento(HomeView.EventoH eventoParaCompartilhar) {
        try {
            CompartilharEventoModal modalView = new CompartilharEventoModal(eventoParaCompartilhar);
            CompartilharEventoController controller = new CompartilharEventoController(modalView, eventoParaCompartilhar);

            Scene modalScene = new Scene(modalView);

            String cssPath = getClass().getResource("/styles/modal-styles.css").toExternalForm();
            modalScene.getStylesheets().add(cssPath);

            modalScene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            Stage modalStage = new Stage();
            modalStage.setTitle("Compartilhar Evento");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.TRANSPARENT);
            modalStage.setScene(modalScene);

            modalStage.initOwner(primaryStage);

            modalStage.showAndWait();
        } catch (Exception e) {
            sistemaDeLogger.error("Falha ao abrir o modal de compartilhamento: "+ e);
        }
    }

    public void abrirModalParticipantes(HomeView.EventoH eventoH) {
        sistemaDeLogger.info("Abrindo modal de participantes para o evento: " + eventoH.titulo());
        try {
            ParticipantesModal modal = new ParticipantesModal(eventoH);

            Stage modalStage = new Stage();

            modalStage.initModality(Modality.NONE);
            modalStage.initOwner(primaryStage);
            modalStage.initStyle(StageStyle.TRANSPARENT);

            Scene modalScene = new Scene(modal);
            modalScene.setFill(Color.TRANSPARENT);

            modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
            modalStage.setScene(modalScene);

            modalStage.setOnShown(event -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                modalStage.setX(((screenBounds.getWidth() - modalStage.getWidth()) / 2)+360);
                modalStage.setY(((screenBounds.getHeight() - modalStage.getHeight()) / 2)-60);
            });

            modalStage.showAndWait();
        } catch (Exception e) {
            sistemaDeLogger.error("Erro ao abrir o modal de participantes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void abrirModalComentarios(int eventoId,String email) {
        Optional<EventoModel> optionalEvento = EventoLeituraService.getInstancia().procurarEventoPorId(eventoId);

        if (optionalEvento.isPresent()) {
            EventoModel evento = optionalEvento.get();

            try {
                ComentarioModal modal = new ComentarioModal(evento,email);

                Stage modalStage = new Stage();

                modalStage.initModality(Modality.NONE);

                modalStage.initOwner(primaryStage);
                modalStage.initStyle(StageStyle.TRANSPARENT);

                Scene modalScene = new Scene(modal);
                modalScene.setFill(Color.TRANSPARENT);

                modalScene.getStylesheets().add(getClass().getResource("/styles/modal-styles.css").toExternalForm());
                modalStage.setScene(modalScene);

                modalStage.setOnShown(event -> {
                    javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                    modalStage.setX(((screenBounds.getWidth() - modalStage.getWidth()) / 2)+454);
                    modalStage.setY(((screenBounds.getHeight() - modalStage.getHeight()) / 2)+200);
                });

                modalStage.showAndWait();
            } catch (Exception e) {
                sistemaDeLogger.error("Falha ao abrir o modal de comentários: " + e.getMessage());
            }
        } else {
            sistemaDeLogger.error("Tentativa de abrir comentários para evento não encontrado. ID: " + eventoId);
        }
    }

    public void abrirModalFiltro(FiltroDto filtroAtual, Consumer<FiltroDto> onAplicarCallback) {
        sistemaDeLogger.info("Método abrirModalFiltro() chamado.");
        try{
            FiltroModal modal=new FiltroModal();
            FiltroController modalController= new FiltroController(modal,filtroAtual,onAplicarCallback);
            modal.setController(modalController);
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
}