package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.character.Character;

public interface iPoi{
    void create();
    void render();
    void dispose();
    Rectangle getActualProjection();
    void execute(Character character);
}
