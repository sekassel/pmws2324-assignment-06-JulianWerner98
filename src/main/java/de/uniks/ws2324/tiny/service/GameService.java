package de.uniks.ws2324.tiny.service;

import de.uniks.ws2324.tiny.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.uniks.ws2324.tiny.Constants.*;

public class GameService {
    private final List<Street> streets = new ArrayList<>();
    private final List<City> cities = new ArrayList<>();
    private HeadQuarter headQuarter;
    private final Random rnGenerator = new Random();

    public void initGame() {

        // HQ Kassel
        this.headQuarter = new HeadQuarter();
        this.headQuarter.setName(CITY_NAME_KASSEL).setX(CITY_X_KASSEL).setY(CITY_Y_KASSEL);
        this.cities.add(this.headQuarter);

        // Paderborn
        City paderborn = new City();
        paderborn.setName(CITY_NAME_PADERBORN).setX(CITY_X_PADERBORN).setY(CITY_Y_PADERBORN);
        this.cities.add(paderborn);

        // Bad Arolsen
        City badArolsen = new City();
        badArolsen.setName(CITY_NAME_BAD_AROLSEN).setX(CITY_X_BAD_AROLSEN).setY(CITY_Y_BAD_AROLSEN);
        this.cities.add(badArolsen);

        // Marburg
        City marburg = new City();
        marburg.setName(CITY_NAME_MARBURG).setX(CITY_X_MARBURG).setY(CITY_Y_MARBURG);
        this.cities.add(marburg);

        // Eschwege
        City eschwege = new City();
        eschwege.setName(CITY_NAME_ESCHWEGE).setX(CITY_X_ESCHWEGE).setY(CITY_Y_ESCHWEGE);
        this.cities.add(eschwege);

        // Göttingen
        City goettingen = new City();
        goettingen.setName(CITY_NAME_GOETTINGEN).setX(CITY_X_GOETTINGEN).setY(CITY_Y_GOETTINGEN);
        this.cities.add(goettingen);

        // connect cities
        connectCities(this.headQuarter, eschwege);
        connectCities(this.headQuarter, marburg);
        connectCities(this.headQuarter, badArolsen);
        connectCities(this.headQuarter, goettingen);
        connectCities(badArolsen, paderborn);
        connectCities(goettingen, paderborn);
        connectCities(marburg, eschwege);

        // generate Car
        new Car().setDriver("Alice").setPosition(this.headQuarter);

        // generate orders
        generateOrder();
        generateOrder();
    }

    private void generateOrder() {
        new Order()
                .setLocation(this.cities.get(rnGenerator.nextInt(this.cities.size())))
                .setReward(rnGenerator.nextInt(ORDER_REWARD_MIN, ORDER_REWARD_MAX))
                .setExpires(rnGenerator.nextInt(ORDER_LIFE_MS_MIN, ORDER_LIFE_MS_MAX));
    }

    public Street connectCities(City c1, City c2) {
        Street street = new Street().withConnects(c1).withConnects(c2);
        streets.add(street);
        return street;
    }
}
