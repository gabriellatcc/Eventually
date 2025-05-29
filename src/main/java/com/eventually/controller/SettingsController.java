package com.eventually.controller;

import com.eventually.service.AlertService;
import com.eventually.service.TelaService;
import com.eventually.service.UsuarioExclusaoService;
import com.eventually.view.LoginView;
import com.eventually.view.SettingsView;
import com.eventually.view.UserScheduleView;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe controller da tela de Configurações do usuário.
 * Esta classe é responsável pela comunicação
 * da tela de de configurações com o backend.
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-05-22
 * @author Gabriella Tavares Costa Corrêa (Revisão de documentação, estrutura e lógica da classe)
 * @since 2025-05-22
 */
public class SettingsController {
    private SettingsView settingsView;
    private final Stage primaryStage;

    private AlertService alertService=new AlertService();

    UsuarioExclusaoService usuarioExclusaoService = new UsuarioExclusaoService();


    /**
     * Construtor do {@code SettingsController}, inicializa a view de configurações.
     * @param settingsView a interface de configurações associada
     * @param primaryStage o palco principal da aplicação
     */
    public SettingsController(SettingsView settingsView, Stage primaryStage) {
        this.settingsView = settingsView;
        this.primaryStage = primaryStage;
        this.settingsView.setSettingsController(this);
        setupEventHandlersSettings();
    }

    private void setupEventHandlersSettings() {
        settingsView.getBtnInicio().setOnAction(e -> irParaInicio());
        settingsView.getBtnMeusEventos().setOnAction(e -> irParaMeusEventos());

        settingsView.getBtnConfiguracoes().setOnAction(e -> continuar());

        settingsView.getBtnProgramacao().setOnAction(e ->irParaProgramacao());
        settingsView.getBtnSair().setOnAction(e -> sair());
    }

    /**
     * Este método navega para a tela de Início.
     */
    public void irParaInicio() {
        System.out.println("SC: botão de início clicado");
        try{
            //falta: instanciar tela
        } catch (Exception ex) {
            System.err.println("SC: erro ao ir para tela de programação");
            ex.printStackTrace();
        }
    }

    /**
     * Este método navega para a tela de Início.
     */
    public void irParaMeusEventos() {
        System.out.println("SC: botão de meus eventos clicado");
        try{
            //falta: instanciar tela
        } catch (Exception ex) {
            System.err.println("SC: erro ao ir para tela de meus eventos");
            ex.printStackTrace();
        }
    }

    /**
     * Este método exibe mensagem informando que o usuário já está na tela de configurações
     */
    public void continuar() {
        System.out.println("SC: botão de configurções clicado");
        try{
            alertService.alertarInfo("Já está na tela de configurações");
        } catch (Exception ex) {
            System.err.println("SC: erro ao ir para tela de configurações");
            ex.printStackTrace();
        }
    }

    /**
     * Este método navega para a tela UserScheduleView.
     */
    public void irParaProgramacao() {
        System.out.println("SC: botão de programação clicado");
        try{
            UserScheduleView userScheduleView = new UserScheduleView();
            UserScheduleController userScheduleController = new UserScheduleController(userScheduleView, primaryStage);
            userScheduleView.setUserScheduleController(userScheduleController);

            TelaService service = new TelaService();
            Scene sceneUserSchedule = new Scene(userScheduleView,service.medirWidth(),service.medirHeight());

            sceneUserSchedule.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());
            primaryStage.setTitle("Eventually - Progamação do Usuário");
            primaryStage.setScene(sceneUserSchedule);
        } catch (Exception ex) {
            System.err.println("SC: erro ao ir para tela de programação");
            ex.printStackTrace();
        }
    }

    /**
     * Este método navega para a tela de login.
     */
    public void sair() {
        System.out.println("Botçao de sair clicado!");
        try{
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView, primaryStage);
            loginView.setLoginController(loginController);

            TelaService settingsService = new TelaService();
            Scene loginScene = new Scene(loginView,settingsService.medirWidth(),settingsService.medirHeight());

            loginScene.getStylesheets().add(getClass().getResource("/styles/login-styles.css").toExternalForm());
            primaryStage.setTitle("Eventually - Login");
            primaryStage.setScene(loginScene);
        } catch (Exception ex) {
            System.err.println("SC: erro ao voltar para tela de programação");
            ex.printStackTrace();
        }
    }

    public void deletarConta() {
        try{
            //falta:
            //modal: Você está prestes a deletar sua conta, deseja prosseguir?"
            //modal: inserir senha e confirmar senha para exclusão
            //chamar usuarioExclusaoService.deletarUsuario(valor);
        }
        catch (Exception ex) {
            System.err.println("SC: erro ao deletar conta");
            ex.printStackTrace();
        }
    }
}