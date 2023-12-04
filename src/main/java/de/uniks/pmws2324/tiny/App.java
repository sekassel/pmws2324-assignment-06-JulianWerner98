package de.uniks.pmws2324.tiny;

import de.uniks.pmws2324.tiny.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {



    @Override
    public void start(Stage stage) throws Exception {
        new Controller().init(stage);
    }
}
