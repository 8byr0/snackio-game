package fr.esigelec.snackio.core;

import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.state.MultiplayerGameState;

/**
 * NetworkGameEngine is the engine that handles interactions between SnackioNetServer and SnackioGame
 */
public class NetworkGameEngine extends AbstractGameEngine {

    /**
     * Default constructor
     *
     * @param game snackioGame that will be managed by this engine
     */
    public NetworkGameEngine(SnackioGame game, Player player) throws NoCharacterSetException, UnhandledControllerException {
        super(game, player);
    }

    /**
     * Call this method when the Engine is properly configured
     */
    @Override
    public void startGame() throws GameCannotStartException {
        super.startGame();
    }

//    /**
//     * Update the position of a Player identified by its id
//     *
//     * @param id        id of the Player to update
//     * @param position  new position
//     * @param direction new direction
//     */
//    @Override
//    public void updatePlayerPosition(int id, Position position, Direction direction) throws NoCharacterSetException {
//        Player player = this.game.getPlayer(id);
//
//        if (null != player) {
//            player.setMoving(true);
//            player.setPosition(position);
//            player.setDirection(direction);
//            player.setMoving(false);
//        }
//    }
}
