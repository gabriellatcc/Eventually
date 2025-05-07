package com.eventually;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.eventually.view.UserScheduleView;

/**
 * Classe principal da aplicação Eventually.
 * Esta classe estende {@code javafx.application.Application} e é responsável
 * por inicializar e exibir a interface gráfica do usuário.
 *
 * @author Yuri Garcia Maia
 * @version 1.00
 * @since 2025-04-06
 * @author Gabriella Tavares
 * @since 2025-04-22 (Documentação da classe)
 */
public class Main extends Application {

    /**
     * O método principal para iniciar a aplicação JavaFX.
     * Este método é chamado pelo ambiente JavaFX quando a aplicação é lançada.
     * Ele cria a view principal, define a cena, carrega os estilos CSS,
     * define o ícone da aplicação e, finalmente, exibe a janela principal
     * @param primaryStage O palco principal para esta aplicação, onde a cena será construída.
     * Este objeto é criado e passado pelo ambiente JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        UserScheduleView userScheduleView = new UserScheduleView();
        Scene scene = new Scene(userScheduleView, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/user-schedule-styles.css").toExternalForm());

        // Adiciona ícone
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/app-icon.png").toExternalForm()));

        primaryStage.setTitle("Eventually");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * O método principal que serve como ponto de entrada para a aplicação Java.
     * Ele chama o método {@code launch()} da classe {@code Application},
     * que é responsável por iniciar a aplicação JavaFX e chamar o método {@code start()}.
     *
     * @param args Os argumentos da linha de comando passados para a aplicação.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
