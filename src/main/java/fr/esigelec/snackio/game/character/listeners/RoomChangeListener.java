package fr.esigelec.snackio.game.character.listeners;

/**
 * Interface triggered when a character changes room
 */
public interface RoomChangeListener {
    /**
     * Method executed when a room change is performed by Character
     * @param newRoom new room of the Character
     */
    void movePerformed(String newRoom);
}
