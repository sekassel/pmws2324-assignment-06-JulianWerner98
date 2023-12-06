package de.uniks.pmws2324.tiny.gui;

import de.uniks.pmws2324.tiny.App;
import de.uniks.pmws2324.tiny.controller.GameController;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest extends ApplicationTest {

    private Stage stage;
    private GameController gameController;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new App().start(stage);

    }

    @Test
    public void checkTitle() {
        assertEquals("Tiny Transport", stage.getTitle());
    }


}
