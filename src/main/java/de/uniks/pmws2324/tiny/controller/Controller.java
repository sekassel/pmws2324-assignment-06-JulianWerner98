package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.Main;
import de.uniks.pmws2324.tiny.service.GameService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    public void init(Stage stage) throws IOException {
        final FXMLLoader guiLoader = new FXMLLoader(Main.class.getResource("view/Game.fxml"));
        Parent parent = guiLoader.load();

        GameController controller = guiLoader.getController();
        controller.render();

        stage.setTitle("Tiny Transport");
        stage.setScene(new Scene(parent));
        stage.show();
    }
}
