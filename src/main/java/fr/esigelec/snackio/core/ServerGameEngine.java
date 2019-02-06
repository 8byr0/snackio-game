package fr.esigelec.snackio.core;

import fr.esigelec.snackio.game.SnackioGame;

public class ServerGameEngine {
    private int duration = -1; // Not limited

    private Thread gameThread;

    public ServerGameEngine() {
        gameThread = new Thread(() -> {
            SnackioGame game = SnackioGame.getInstance();
//            game.getRandomPosition();
//            game.getRandomPOI();
        });
    }

    public void startGame() {
        gameThread.start();
    }
}
