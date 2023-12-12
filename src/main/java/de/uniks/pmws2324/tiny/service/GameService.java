package de.uniks.pmws2324.tiny.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import de.uniks.pmws2324.tiny.Constants;
import de.uniks.pmws2324.tiny.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.pmws2324.tiny.model.dto.SaveObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GameService {
    public static final String DATA_FOLDER = "data";
    public static final String GAME_DATA_JSON = DATA_FOLDER + "/gameData.json";
    private final List<Street> streets = new ArrayList<>();
    private final List<City> cities = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private HeadQuarter headQuarter;
    private final Random rnGenerator = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Street> getStreets() {
        return streets;
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Car> getCars() {
        cars.sort(Comparator.comparing(Car::getDriver));
        return cars;
    }


    public void initGame() {

        // HQ Kassel
        this.headQuarter = new HeadQuarter();
        this.headQuarter.setName(Constants.CITY_NAME_KASSEL).setX(Constants.CITY_X_KASSEL).setY(Constants.CITY_Y_KASSEL);

        // Paderborn
        City paderborn = new City();
        paderborn.setName(Constants.CITY_NAME_PADERBORN).setX(Constants.CITY_X_PADERBORN).setY(Constants.CITY_Y_PADERBORN);
        this.cities.add(paderborn);

        // Bad Arolsen
        City badArolsen = new City();
        badArolsen.setName(Constants.CITY_NAME_BAD_AROLSEN).setX(Constants.CITY_X_BAD_AROLSEN).setY(Constants.CITY_Y_BAD_AROLSEN);
        this.cities.add(badArolsen);

        // Marburg
        City marburg = new City();
        marburg.setName(Constants.CITY_NAME_MARBURG).setX(Constants.CITY_X_MARBURG).setY(Constants.CITY_Y_MARBURG);
        this.cities.add(marburg);

        // Eschwege
        City eschwege = new City();
        eschwege.setName(Constants.CITY_NAME_ESCHWEGE).setX(Constants.CITY_X_ESCHWEGE).setY(Constants.CITY_Y_ESCHWEGE);
        this.cities.add(eschwege);

        // Göttingen
        City goettingen = new City();
        goettingen.setName(Constants.CITY_NAME_GOETTINGEN).setX(Constants.CITY_X_GOETTINGEN).setY(Constants.CITY_Y_GOETTINGEN);
        this.cities.add(goettingen);

        // connect cities
        connectCities(this.headQuarter, eschwege);
        connectCities(this.headQuarter, marburg);
        connectCities(this.headQuarter, badArolsen);
        connectCities(this.headQuarter, goettingen);
        connectCities(badArolsen, paderborn);
        connectCities(goettingen, paderborn);
        connectCities(marburg, eschwege);

        SaveObject saveObject = loadSaveObject();

        if (saveObject != null) {
            this.headQuarter.setMoney(saveObject.getMoney());
            this.headQuarter.setNewCarPrice(saveObject.getNewCarPrice());
            for (String driver : saveObject.getCarDriver()) {
                cars.add(new Car().setDriver(driver).setPosition(this.headQuarter).setOwner(this.headQuarter));
            }
        } else {
            // generate Car
            cars.add(new Car().setDriver("Alice").setPosition(this.headQuarter).setOwner(this.headQuarter));
            //Set init Car Price
            this.headQuarter.setNewCarPrice(4242);
        }
        // generate orders
        generateOrder();
        generateOrder();
    }

    public void generateOrder() {
        new Order()
                .setLocation(this.cities.get(rnGenerator.nextInt(this.cities.size())))
                .setReward(rnGenerator.nextInt(Constants.ORDER_REWARD_MIN, Constants.ORDER_REWARD_MAX))
                .setExpires(rnGenerator.nextInt(Constants.ORDER_EXPIRES_MS_MIN, Constants.ORDER_EXPIRES_MS_MAX))
                .setGeneratedTime(System.currentTimeMillis());
    }

    public Street connectCities(City c1, City c2) {
        Street street = new Street().withConnects(c1).withConnects(c2);
        street.setSpeedLimit(rnGenerator.nextInt(5, 15) * 10);
        streets.add(street);
        return street;
    }

    public HeadQuarter getHeadquarter() {
        return headQuarter;
    }

    public HeadQuarter getHeadQuarter() {
        return headQuarter;
    }

    public ArrayList<Location> getPath(City start, City goal) {
        if (cities.isEmpty() || streets.isEmpty()) return null;

        Map<Location, Location> previous = new HashMap<>();
        Set<City> visited = new HashSet<>();
        Queue<Location> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            if (current.equals(goal)) {
                return buildPath(start, goal, previous);
            }

            if (current instanceof City) {
                City currentCity = (City) current;
                for (Street street : streets) {
                    if (street.isBlocked() || !street.getConnects().contains(currentCity)) continue;

                    for (City nextCity : street.getConnects()) {
                        if (!visited.contains(nextCity)) {
                            visited.add(nextCity);
                            previous.put(nextCity, street); // Speichert die Straße, die zur Stadt führt
                            previous.put(street, currentCity); // Speichert die Stadt, von der aus die Straße kommt
                            queue.add(nextCity);
                        }
                    }
                }
            }
        }

        return null; // Kein Pfad gefunden
    }

    private ArrayList<Location> buildPath(Location start, Location goal, Map<Location, Location> previous) {
        LinkedList<Location> path = new LinkedList<>();
        for (Location at = goal; at != null; at = previous.get(at)) {
            path.addFirst(at);
        }
        return new ArrayList<>(path);
    }


    public void getRewardForOrder(Order order) {
        this.headQuarter.setMoney(this.headQuarter.getMoney() + order.getReward());
        order.getCar().setPosition(headQuarter);
        order.getLocation().withoutOrders(order);
        order.setCar(null);
    }

    public void setNewCarPosition(Car car, Location location, City lastCity) {
        car.setPosition(location).setLastCity(lastCity);
        car.setStartAtLastCity(System.currentTimeMillis());
    }

    public void buyCar(String driver) {
        if (this.headQuarter.getMoney() >= this.headQuarter.getNewCarPrice()) {
            Car car = new Car().setDriver(driver);
            cars.add(car);
            car.setOwner(headQuarter).setPosition(headQuarter);
            this.headQuarter.setMoney(this.headQuarter.getMoney() - this.headQuarter.getNewCarPrice());
            this.headQuarter.setNewCarPrice(rnGenerator.nextInt(2000, 7500));
        }
    }

    public List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();
        locations.addAll(this.cities);
        locations.add(this.headQuarter);
        locations.addAll(this.streets);
        return locations;
    }

    public Car getAvailableCar() {
        for (Car car : this.cars) {
            if (car.getOrder() == null && car.getPosition() == this.headQuarter) {
                return car;
            }
        }
        return null;
    }

    public void checkOrdersValide() {
        for (Order order : this.cities.stream().map(City::getOrders).flatMap(Collection::stream).toList()) {
            if (order.getExpires() + order.getGeneratedTime() < System.currentTimeMillis()) {
                order.getLocation().withoutOrders(order);
                if (order.getCar() != null) {
                    order.getCar().setPosition(this.headQuarter);
                    order.setCar(null);
                }
            }
        }
    }

    public void startOrder(Order selectedOrder) {
        this.getAvailableCar().setOrder(selectedOrder);
        selectedOrder.getCar().setStartAtLastCity(System.currentTimeMillis());
    }

    // ===============================================================================================================
    // Save & Load
    // ===============================================================================================================
    public void saveGame() {
        try {
            SaveObject saveObject = new SaveObject()
                    .setMoney(this.headQuarter.getMoney())
                    .setNewCarPrice(this.headQuarter.getNewCarPrice())
                    .setCarDriver(cars.stream().map(Car::getDriver).toList());
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            String gameJson = objectMapper.writeValueAsString(saveObject);
            Files.createDirectories(Path.of(DATA_FOLDER));
            Files.writeString(Path.of(GAME_DATA_JSON), gameJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SaveObject loadSaveObject() {
        if (Files.notExists(Path.of(GAME_DATA_JSON))) return null;
        try {
            String json = Files.readString(Path.of(GAME_DATA_JSON));
            return objectMapper.readValue(json.getBytes(), SaveObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTimeForStreet(Street street) {
        City c1 = street.getConnects().get(0);
        City c2 = street.getConnects().get(1);
        int distance = (int) Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2));
        return (int) (distance / (float) street.getSpeedLimit() * 1000);
    }
}

