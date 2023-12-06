package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.Main;
import de.uniks.pmws2324.tiny.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class ShopController {

    private final StackPane rootPane;
    private final GameService gameService;
    @FXML
    Label shopBalanceLabel;
    @FXML
    Label ShopCarCostLabel;
    @FXML
    TextField nameInput;
    @FXML
    Button cancelButton;
    @FXML
    Button buyButton;

    public ShopController(GameService gameService, StackPane rootPane) {
        this.rootPane = rootPane;
        this.gameService = gameService;
    }

    public void load() {
        // Load the shop view
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Shop.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            Node underground = this.rootPane.getChildren().get(0);
            underground.setEffect(new javafx.scene.effect.BoxBlur(3, 3, 3));
            underground.setDisable(true);
            Node shop = loader.load();
            this.rootPane.getChildren().add(shop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Init the shop view
        this.shopBalanceLabel.setText(gameService.getHeadquarter().getMoney() + " €");
        this.ShopCarCostLabel.setText(gameService.getHeadquarter().getNewCarPrice() + " €");
        this.cancelButton.setOnMouseClicked(this::closeShop);
        this.buyButton.setOnMouseClicked(this::buyCar);
        this.buyButton.setDisable(true);
        this.nameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0
                            && newValue.length() < 20
                            && newValue.matches("[a-zA-Z0-9]+")
                    //todo uncomment
                            //&& gameService.getHeadquarter().getMoney() >= gameService.getHeadquarter().getNewCarPrice()
            ) {
                this.buyButton.setDisable(false);
            } else {
                this.buyButton.setDisable(true);
            }
        });
    }

    private void buyCar(MouseEvent mouseEvent) {
        String name = this.nameInput.getText();
        this.gameService.buyCar(name);
        this.closeShop(mouseEvent);
    }

    private void closeShop(MouseEvent mouseEvent) {
        Node shop = this.rootPane.getChildren().get(1);
        this.rootPane.getChildren().remove(shop);
        Node underground = this.rootPane.getChildren().get(0);
        underground.setEffect(null);
        underground.setDisable(false);
    }
}
