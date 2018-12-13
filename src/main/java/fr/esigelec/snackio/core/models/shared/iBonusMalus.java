package fr.esigelec.snackio.core.models.shared;

import fr.esigelec.snackio.game.character.Character;

/**
 * A iBonusMalus implementation modifies a given character behavior(speed, fly capacity...)
 */
public interface iBonusMalus {
    /**
     * Code to execute when this object is triggered by a user on the map
     * @param character character on which the Bonus / Malus will be executed
     */
    void execute(Character character);
}
