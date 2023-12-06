package de.uniks.pmws2324.tiny.gui;

import de.uniks.pmws2324.tiny.App;
import de.uniks.pmws2324.tiny.controller.GameController;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShopTest extends ApplicationTest {
    private Stage stage;
    private GameController gameController;

    @Override
    public void start(Stage stage){
        this.stage = stage;
        new App().start(stage);
    }

    @Test
    public void checkShop() {
        clickOn("#shopButton");
        Label shopBalanceLabel = lookup("#shopBalanceLabel").query();
        assertEquals("0 €", shopBalanceLabel.getText());
        Label ShopCarCostLabel = lookup("#shopCarCostLabel").query();
        assertEquals("4242 €", ShopCarCostLabel.getText());
    }
}
