package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.Main;
import de.uniks.pmws2324.tiny.service.GameService;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class GameController{
    private final GameService gameService;
   @FXML
    private Canvas canvas;
    private GraphicsContext context;

    public GameController() {
        this.gameService = new GameService();
        this.gameService.initGame();
    }

    public Parent render() throws IOException {
        Parent parent = FXMLLoader.load(Main.class.getResource("view/Game.fxml"));

        // Canvas
        AnchorPane gameView = (AnchorPane) parent.lookup("#gameViewPane");
        this.context = canvas.getGraphicsContext2D();

        //canvas.widthProperty().bind(gameView.widthProperty());
        //canvas.heightProperty().bind(gameView.widthProperty());
        canvas.setOnMouseMoved(event -> handleMouseHover(event.getX(), event.getY()));
        canvas.setOnMouseClicked(event -> handleMouseClick(event.getX(), event.getY()));

        return parent;
    }

    private void handleMouseHover(double mouseX, double mouseY) {

    }

    private void handleMouseClick(double mouseX, double mouseY) {

    }
}
