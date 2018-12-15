package fr.esigelec.snackio.game.character.listeners;

import fr.esigelec.snackio.networking.Position;

/**
 * Interface triggered when a character moves
 */
public interface MoveListener {
    /**
     * Method executed when a move is performed by Character
     * @param position new position of the Character
     */
    void movePerformed(Position position);
}
