package de.uniks.pmws2324.tiny.model.dto;

import java.util.List;

public class SaveObject {
    int money;
    int newCarPrice;
    List<String> carDriver;

    public SaveObject setMoney(int money) {
        this.money = money;
        return this;
    }

    public SaveObject setCarDriver(List<String> carDriver) {
        this.carDriver = carDriver;
        return this;
    }

    public SaveObject setNewCarPrice(int newCarPrice) {
        this.newCarPrice = newCarPrice;
        return this;
    }

    public int getMoney() {
        return money;
    }

    public int getNewCarPrice() {
        return newCarPrice;
    }

    public List<String> getCarDriver() {
        return carDriver;
    }

}
