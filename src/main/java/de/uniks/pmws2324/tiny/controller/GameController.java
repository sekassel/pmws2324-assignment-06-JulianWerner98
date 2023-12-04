package de.uniks.pmws2324.tiny.controller;

import de.uniks.pmws2324.tiny.model.*;
import de.uniks.pmws2324.tiny.service.GameService;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.beans.PropertyChangeEvent;

import static de.uniks.pmws2324.tiny.Constants.FIELD_DIM;

public class GameController {
    @FXML
    private Button orderAcceptButton;
    @FXML
    private Label orderRewardLabel;
    @FXML
    private Label orderTimeLabel;
    @FXML
    private Label orderTownLabel;
    @FXML
    private Label carDestinationLabel;
    @FXML
    private Label carDriverLabel;
    @FXML
    private VBox carBox;
    @FXML
    private Button shopButton;
    @FXML
    private Label carCostLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label hqNameLabel;
    @FXML
    private AnchorPane gameViewPane;
    @FXML
    private Canvas mapCanvas;
    private Order currentOrder = new Order();
    private GraphicsContext context;
    GameService gameService = new GameService();

    public void render() {
        gameService.initGame();
        // Canvas
        this.context = mapCanvas.getGraphicsContext2D();

        mapCanvas.widthProperty().bind(gameViewPane.widthProperty());
        mapCanvas.heightProperty().bind(gameViewPane.widthProperty());
        mapCanvas.setOnMouseMoved(event -> handleMouseHover(event.getX(), event.getY()));
        mapCanvas.setOnMouseClicked(event -> handleMouseClick(event.getX(), event.getY()));

        addPropertyChangeListener();
        setInitialValues();
        drawMap();
    }

    private void drawMap() {
        //  Städte sollen als gelbe Quadrate angezeigt werden. An geeigneter Stelle soll zusätzlich der Name der Stadt angezeigt werden. Der HeadQuarter soll besonders hervorgehoben werden.
        //  Straßen sollen als schwarze Linien angezeigt werden. An geeigneter Stelle soll zusätzlich die Länge der Straße angezeigt werden.
        // Autos sollen als rote Kreise an ihrer aktuellen Location eingezeichnet werden.
        // Orders sollen als kleine blaue Kreise an geeigneter Stelle an ihrer jeweiligen Stadt eingezeichnet werden.

        // Clean
        final double sw = mapCanvas.getWidth();
        final double sh = mapCanvas.getHeight();


        // Clean canvas
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, sw, sh);

        // Draw Streets
        City cityOne;
        City cityTwo;
        context.setStroke(Color.GREY);
        context.setLineWidth(5);
        for (Street street : this.gameService.getStreets()) {
            cityOne = street.getConnects().get(0);
            cityTwo = street.getConnects().get(1);
            context.strokeLine(
                    cityOne.getX() + FIELD_DIM / 2,
                    cityOne.getY() + FIELD_DIM / 2,
                    cityTwo.getX() + FIELD_DIM / 2,
                    cityTwo.getY() + FIELD_DIM / 2
            );
        }

        // Draw Cities
        context.setFill(Color.YELLOW);
        for (City city : this.gameService.getCities()) {
            context.fillRect(city.getX(), city.getY(), FIELD_DIM, FIELD_DIM);
            context.setFill(Color.BLACK);
            context.fillText(city.getName(), city.getX(), city.getY());
        }
    }

    private void setInitialValues() {
        //Fire PCL to set initial values
        this.gameService.getHeadquarter().firePropertyChange(HeadQuarter.PROPERTY_MONEY, null, this.gameService.getHeadquarter().getMoney());
        this.gameService.getHeadquarter().firePropertyChange(HeadQuarter.PROPERTY_NAME, null, this.gameService.getHeadquarter().getName());
        currentOrder.firePropertyChange(HeadQuarter.PROPERTY_ORDERS, null, this.gameService.getHeadquarter().getOrders());
        this.gameService.getHeadquarter().firePropertyChange(HeadQuarter.PROPERTY_CARS, null, this.gameService.getHeadquarter().getCars());

        this.carCostLabel.setText(this.gameService.generateNewCarPrice() + " €");
        //Todo remove if shop exists
        this.shopButton.setVisible(false);
    }

    private void addPropertyChangeListener() {
        this.gameService.getHeadquarter().listeners().addPropertyChangeListener(HeadQuarter.PROPERTY_MONEY, this::setBalance);
        this.gameService.getHeadquarter().listeners().addPropertyChangeListener(HeadQuarter.PROPERTY_NAME, this::setHqName);
        currentOrder.listeners().addPropertyChangeListener(HeadQuarter.PROPERTY_ORDERS, this::setOrder);
        this.gameService.getHeadquarter().listeners().addPropertyChangeListener(HeadQuarter.PROPERTY_CARS, this::setCar);
    }

    private void setCar(PropertyChangeEvent propertyChangeEvent) {
        // Todo: Change to list
        if (this.gameService.getHeadquarter().getCars().size() == 0) {
            this.carBox.setVisible(false);
        } else {
            this.carBox.setVisible(true);
            Car car = this.gameService.getHeadquarter().getCars().get(0);
            this.carDriverLabel.setText(car.getDriver());
            if (car.getOrder() != null) {
                this.carDestinationLabel.setText(car.getOrder().getLocation().getName());
            } else {
                this.carDestinationLabel.setText("");
            }
        }
    }

    private void setOrder(PropertyChangeEvent propertyChangeEvent) {
        if (currentOrder.getReward() == 0) {
            this.orderAcceptButton.setDisable(true);
            this.orderRewardLabel.setText("");
            this.orderTimeLabel.setText("");
            this.orderTownLabel.setText("");
        } else {
            this.orderAcceptButton.setDisable(false);
            this.orderRewardLabel.setText(currentOrder.getReward() + " €");
            this.orderTimeLabel.setText(((int) (currentOrder.getExpires() / 1000 / 60)) + ":" + ((int) ((currentOrder.getExpires() / 1000) % 60)));
            this.orderTownLabel.setText(currentOrder.getLocation().getName());
        }
    }

    private void setHqName(PropertyChangeEvent propertyChangeEvent) {
        this.hqNameLabel.setText(this.gameService.getHeadquarter().getName());
    }

    private void setBalance(PropertyChangeEvent propertyChangeEvent) {
        this.balanceLabel.setText(this.gameService.getHeadquarter().getMoney() + " €");
    }


    private void handleMouseHover(double mouseX, double mouseY) {
        //System.out.println("Mouse Hover: " + mouseX + " " + mouseY);
    }

    private void handleMouseClick(double mouseX, double mouseY) {
        //System.out.println("Mouse Click: " + mouseX + " " + mouseY);
    }
}
