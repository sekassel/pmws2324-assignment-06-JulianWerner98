package de.uniks.pmws2324.tiny.service;

import de.uniks.pmws2324.tiny.model.City;
import de.uniks.pmws2324.tiny.model.Street;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService {
    private final Random rnGenerator = new Random();
    private final GameService gameService;

    public TimerService(GameService gameService) {
        this.gameService = gameService;
        changeRandomThings();
    }

    private void changeRandomThings() {
        if(rnGenerator.nextBoolean()) {
            // generate order
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    gameService.generateOrder();
                    changeRandomThings();
                }
            }, rnGenerator.nextInt(1000, 5000));
        } else {
            // block street
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Street street = gameService.getStreets().get(rnGenerator.nextInt(gameService.getStreets().size()));
                    if(street.isBlocked()) {
                        street.setBlocked(false);
                    } else {
                        Boolean otherWay = true;
                        for (City city : gameService.getCities()) {
                            street.setBlocked(true);
                            if(gameService.getPath(city, gameService.getHeadQuarter()) == null) {
                                street.setBlocked(false);
                                otherWay = false;
                                break;
                            }
                        }
                        if (otherWay) {
                            street.setBlocked(true);
                        }
                    }
                    changeRandomThings();
                }
            }, rnGenerator.nextInt(1000, 10000));
        }
    }

}
