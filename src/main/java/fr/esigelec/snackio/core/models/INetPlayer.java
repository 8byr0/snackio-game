package fr.esigelec.snackio.core.models;

import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.networking.Position;

/**
 * INetPlayer is an interface that defines all the methods exposed
 * to client.
 * When a client is connected to the Server, it can execute any of these methods.
 */
public interface INetPlayer {
    /**
     * Method called when a new player registers on the server
     *
     * @param localPlayer the player that just registered
     */
    void registerPlayer (Player localPlayer);

    /**
     * Method to notify the server that the Player associated to this NetPlayer insatance has been updated
     *
     * @param id ID of the updated Player
     * @param position new Position on the Game's map
     * @param direction new Direction on the Game's map
     */
    void updatePlayerMotion(int id, Position position, Direction direction);

}