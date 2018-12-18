package fr.esigelec.snackio.core;

import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.pois.Coin;
import fr.esigelec.snackio.game.state.CoinQuestGameState;

import javax.swing.*;


public class SoloGameEngine extends AbstractGameEngine {
    /**
     * Default constructor
     *
     * @param game snackioGame that will be managed by this engine
     */
    public SoloGameEngine(SnackioGame game, Player player, MapFactory.MapType type, int coinsToFetch) throws NoCharacterSetException, UnhandledControllerException {
        super(game, player, type);

        gameState = new CoinQuestGameState(type, coinsToFetch);

        Thread gameThread = new Thread(() -> {

            // Add the first coin
            Coin firstCoin = new Coin();
            this.game.addPointOfInterest(firstCoin);

            this.game.addPoiTriggeredListener((poi, triggeringPlayer) -> {
                if (((CoinQuestGameState) gameState).getFetchedCoins() < ((CoinQuestGameState) gameState).getCoinsToFetch() - 1) {
                    Coin anotherCoin = new Coin();
                    this.game.addPointOfInterest(anotherCoin);
                    ((CoinQuestGameState) gameState).incrementFetchedPoints();
                } else {
                    ((CoinQuestGameState) gameState).incrementFetchedPoints();

                    JOptionPane jop1;
                    jop1 = new JOptionPane();
                    jop1.showMessageDialog(null, "YEAH ! You finished the game", "Game finished", JOptionPane.INFORMATION_MESSAGE);

                }

            });
        });
        gameThread.start();
    }


    /**
     * Call this method when the Engine is properly configured
     */
    @Override
    public void startGame() throws GameCannotStartException {
        super.startGame();
    }

}
