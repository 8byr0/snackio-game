package fr.esigelec.snackio.networking.models;

import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.networking.Position;

/**
 * This Interface declares methods that are allowed to be called by the Server using RMI protocol.
 */
public interface IRMIExecutablePlayer {
    void setID(int id);

    int getID();

    Position getPosition() throws NoCharacterSetException;

    Direction getDirection() throws NoCharacterSetException;

    String getRoom() throws NoCharacterSetException;

    void addMoveListener(final Runnable listener) throws NoCharacterSetException;

    void addRoomChangeListener(final Runnable listener) throws NoCharacterSetException;

}
