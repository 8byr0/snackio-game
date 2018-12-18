package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.map.Map;
import fr.esigelec.snackio.game.map.MapRoom;
import fr.esigelec.snackio.networking.Position;

/**
 * This interface defines behavior of PointOfInterest and AnimatedPointOfInterest
 */
public interface iPoi{
    void create();
    boolean isCreated();
    void render();
    void dispose();
    Rectangle getActualProjection();
    void execute(Character character);
    void setPosition(Position position);

    void setRoom(Map randomRoom);

    Map getRoom();
}
