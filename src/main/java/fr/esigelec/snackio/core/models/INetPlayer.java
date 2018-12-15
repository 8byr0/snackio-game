package fr.esigelec.snackio.core.models;

import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.networking.Position;

/**
 * INetPlayer is an interface that defines all the methods exposed
 * to client.
 * When a client is connected to the Server, it can execute any of these methods.
 */
public interface INetPlayer {
    void registerPlayer (Player localPlayer);

    void updatePlayerMotion(int id, Position position, Direction direction);

}