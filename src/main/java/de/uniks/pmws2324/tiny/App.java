package de.uniks.pmws2324.tiny;

import de.uniks.pmws2324.tiny.controller.GameController;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class App extends Application {

    private GameController gameController;


    @Override
    public void start(Stage stage) throws Exception {
        gameController = new GameController();

        Scene scene = new Scene(gameController.render());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        CSSFX.start();

        stage.setTitle("Tiny Hero Adventure");
        stage.setScene(scene);
        stage.show();
    }
}
