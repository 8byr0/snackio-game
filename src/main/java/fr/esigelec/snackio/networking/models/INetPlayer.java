package fr.esigelec.snackio.networking.models;

import fr.esigelec.snackio.core.IGameState;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.state.AbstractGameState;
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
    void registerPlayer (IRMIExecutablePlayer localPlayer);

    /**
     * Method to notify the server that the Player associated to this NetPlayer instance has been updated
     *
     * @param id ID of the updated Player
     * @param position new Position on the Game's map
     * @param direction new Direction on the Game's map
     */
    void updatePlayerMotion(int id, Position position, Direction direction);

    /**
     * Method to notify the server that the Player associated to this NetPlayer instance has been updated
     *
     * @param id ID of the updated Player
     * @param room new room name on the Game's map
     */
    void updatePlayerRoom(int id, String room);

    String getServerName();

    AbstractGameState getGameState();
}