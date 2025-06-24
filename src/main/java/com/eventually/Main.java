package com.eventually;
import com.eventually.service.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** PASSIVEL DE ALTERAÇÃO
 * Classe principal da aplicação Eventually.
 * Esta classe estende {@code javafx.application.Application} e é responsável
 * por inicializar e exibir a interface gráfica do usuário.
 * @version 1.03
 * @author Yuri Garcia Maia
 * @since 2025-04-06
 * @author Gabriella Tavares Costa Corrêa (Documentação, revisão da estrutura e lógica da classe)
 * @since 2025-04-22
 */
public class Main extends Application {
    private Stage primaryStage;

    private UsuarioCadastroService usuarioCadastroService;
    private NavegacaoService navegacaoService;

    private static final Logger sistemaDeLogger = LoggerFactory.getLogger(Main.class);

    /**
     * O método principal para iniciar a aplicação JavaFX.
     * Este método é chamado pelo ambiente JavaFX quando a aplicação é lançada.
     * Ele cria a view de login, define a cena, carrega os estilos CSS,
     * define o ícone da aplicação e, finalmente, exibe a janela principal
     * @param primaryStage o palco principal para esta aplicação, onde a cena será construída.
     * Este objeto é criado e passado pelo ambiente JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            this.usuarioCadastroService = UsuarioCadastroService.getInstancia();
            usuarioCadastroService.criarLista();
            this.navegacaoService = new NavegacaoService(primaryStage);
            navegacaoService.navegarParaLogin();

            primaryStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();
            primaryStage.show();
        }catch (Exception e) {
            sistemaDeLogger.error("O programa não foi iniciado deviado algum erro: "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * O método principal que serve como ponto de entrada para a aplicação Java.
     * Ele chama o método {@code launch()} da classe {@code Application},
     * que é responsável por iniciar a aplicação JavaFX e chamar o método {@code start()}.
     * @param args os argumentos da linha de comando passados para a aplicação.
     */
    public static void main(String[] args) {
        launch(args);
    }
}