package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.character.Character;

/**
 * This interface defines behavior of PointOfInterest and AnimatedPointOfInterest
 */
public interface iPoi{
    void create();
    void render();
    void dispose();
    Rectangle getActualProjection();
    void execute(Character character);
}
