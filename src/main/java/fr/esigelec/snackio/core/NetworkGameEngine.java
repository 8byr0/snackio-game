package fr.esigelec.snackio.core;

import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.listeners.PlayerAddedListener;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.networking.Position;

import java.util.ArrayList;

/**
 * NetworkGameEngine is the engine that handles interactions between SnackioNetServer and SnackioGame
 */
public class NetworkGameEngine implements IGameEngine {
    private SnackioGame game;
    private ArrayList<PlayerAddedListener> playerAddedListeners = new ArrayList<>();
    private Player player;

    /**
     * Default constructor
     *
     * @param game snackioGame that will be managed by this engine
     */
    public NetworkGameEngine(SnackioGame game, Player player) throws NoCharacterSetException, UnhandledControllerException {
        this.game = game;
        this.player = player;
        this.game.addPlayer(player, true);
    }

    /**
     * Add a point of interest to game
     *
     * @param poi point of interest to add to the game
     */
    @Override
    public void addPointOfInterest(iPoi poi) {
        System.out.println("Server asked to add a new poi");
        game.addPointOfInterest(poi);
    }

    /**
     * Remove a point of interest from the game
     *
     * @param poi point of interest to remove from the game
     */
    @Override
    public void removePointOfInterest(iPoi poi) {
        game.removePointOfInterest(poi);
    }

    /**
     * Get the Game's player
     *
     * @return Player instance
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Add a listener that will be triggered when a new player joins the game
     *
     * @param listener the listener
     */
    @Override
    public void addPlayerAddedListener(PlayerAddedListener listener) {
        this.playerAddedListeners.add(listener);
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
    @Override
    public void addPlayer(Player player) throws NoCharacterSetException, UnhandledControllerException {
        game.addPlayer(player, false);
        triggerPlayerAddedListeners(player);
    }

    /**
     * Update the position of a Player identified by its id
     *
     * @param id        id of the Player to update
     * @param position  new position
     * @param direction new direction
     */
    @Override
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
