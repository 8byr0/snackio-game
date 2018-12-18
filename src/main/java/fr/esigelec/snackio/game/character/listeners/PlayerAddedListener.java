package fr.esigelec.snackio.game.character.listeners;

import fr.esigelec.snackio.core.Player;

/**
 * Interface triggered when a Player is added to GameEngine
 * TODO move this elsewhere
 */
public interface PlayerAddedListener {
    /**
     * Method triggered when a Player is added
     * @param player the newly added Player
     */
    void playerAdded(Player player);
}
