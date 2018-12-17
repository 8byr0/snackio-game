package fr.esigelec.snackio.game.pois.listeners;

import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.pois.iPoi;

/**
 * Interface triggered when a Player triggers a POI
 */
public interface PoiTriggeredListener {
    /**
     * Method triggered when a Player triggers a POI
     * @param player the newly added Player
     */
    void poiTriggered(iPoi poi, Player player);
}
