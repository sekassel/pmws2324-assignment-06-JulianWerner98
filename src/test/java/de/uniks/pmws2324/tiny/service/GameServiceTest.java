package de.uniks.pmws2324.tiny.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameServiceTest {
    @Test
    void initGameTest() {
        /*Der Test soll die zugehörige Methode aufrufen und
        das Ergebnis mit Asserts prüfen. Es genügt, die Verbindungen der Straßen und Städte, die explizit
        gesetzten Werte des Autos und das Vorhandensein der zwei Orders mit Attributen des
        gewünschten Wertebereichs zu prüfen.*/
        GameService gameService = new GameService();
        gameService.initGame();
        assertEquals(6, gameService.().size());
    }

}