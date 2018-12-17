package fr.esigelec.snackio.game;

import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnackioGameTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstance() {
    }

    @Test
    void startFailedDueToMissingPlayer() {
        SnackioGame game = SnackioGame.getInstance();
        assertThrows(GameCannotStartException.class, game::start);
    }

    @Test
    void coinFound() {
    }

    @Test
    void addPointOfInterest() {
    }

    @Test
    void removePointOfInterest() {
    }

    @Test
    void addPlayer() {
    }

    @Test
    void getPlayer() {
    }

    @Test
    void getPlayer1() {
    }
}