package fr.esigelec.snackio.core;

import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.core.models.IRMIExecutablePlayer;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.listeners.PlayerAddedListener;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.game.state.AbstractGameState;
import fr.esigelec.snackio.networking.Position;

import java.util.ArrayList;

/**
 * This interface declares all the method that are allowed to be called by SnackioNetServer instance.
 */
public abstract class AbstractGameEngine implements IGameEngine{
    protected SnackioGame game;
    private ArrayList<PlayerAddedListener> playerAddedListeners = new ArrayList<>();
    protected Player player;
    protected AbstractGameState gameState;

    /**
     * Default constructor
     *
     * @param game snackioGame that will be managed by this engine
     */
    public AbstractGameEngine(SnackioGame game, Player player, MapFactory.MapType type) throws NoCharacterSetException, UnhandledControllerException {
        this.game = game;
        this.player = player;
        this.game.addPlayer(player, true);
        this.game.setMap(type);
    }

    /**
     * Add a point of interest to game
     *
     * @param poi point of interest to add to the game
     */
    
    public void addPointOfInterest(iPoi poi) {
        game.addPointOfInterest(poi);
    }

    /**
     * Remove a point of interest from the game
     *
     * @param poi point of interest to remove from the game
     */
    
    public void removePointOfInterest(iPoi poi) {
        game.removePointOfInterest(poi);
    }

    /**
     * Get the Game's player
     *
     * @return Player instance
     */
    public IRMIExecutablePlayer getPlayer() {
        return player;
    }

    /**
     * Add a listener that will be triggered when a new player joins the game
     *
     * @param listener the listener
     */
    public void addPlayerAddedListener(PlayerAddedListener listener) {
        this.playerAddedListeners.add(listener);
    }

    /**
     * Call this method when the Engine is properly configured
     */
    public void startGame() throws GameCannotStartException {
        // Start the game with my player
        game.start(gameState);
    }

    /**
     * Update the room of a Player identified by its id
     * @param id id of the player
     * @param room name of the room
     */
    public void updatePlayerRoom(int id, String room) throws NoCharacterSetException {
        Player player = this.game.getPlayer(id);

        if (null != player) {
            player.setRoom(room);
        }
    }

    /**
     * Trigger all registered PlayerAdded listeners
     *
     * @param player the player newly added
     */
    private void triggerPlayerAddedListeners(Player player) {
        for (PlayerAddedListener listener : playerAddedListeners) {
            listener.playerAdded(player);
        }
    }

    /**
     * Add a player to the game
     * /!\ This is a server-triggered method so all players added by this method are passive(Network-controlled)
     *
     * @param player the player to add to the game
     */
    
    public void addPlayer(IRMIExecutablePlayer player) throws NoCharacterSetException, UnhandledControllerException {
        game.addPlayer((Player)player, false);
        triggerPlayerAddedListeners((Player)player);
    }

  /**
     * Add a player to the game
     * /!\ This is a server-triggered method so all players added by this method are passive(Network-controlled)
     *
     * @param playerID the player to add to the game
     */

    public void removePlayer(int playerID) {
        game.removePlayer(playerID);
    }

    /**
     * Update the position of a Player identified by its id
     *
     * @param id        id of the Player to update
     * @param position  new position
     * @param direction new direction
     */
    
    public void updatePlayerPosition(int id, Position position, Direction direction) throws NoCharacterSetException {
        Player player = this.game.getPlayer(id);

        if (null != player) {
            player.setMoving(true);
            player.setPosition(position);
            player.setDirection(direction);
            player.setMoving(false);
        }
    }

}
