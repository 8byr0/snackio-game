package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.ApplicationAdapter;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.map.Map;
import fr.esigelec.snackio.networking.Position;

/**
 * This class handles all the common attributes of both PointOfInterest and AnimatedPointOfInterest
 */
public abstract class AbstractPointOfInterest extends ApplicationAdapter implements iPoi {
    // MOTION
    protected Position position = new Position(400, 400);

    // STATUS
    protected boolean created = false;
    protected Map room;


    /**
     * Abstract method to implement when inheriting PointOfInterest
     * The content of this method is the code executed when a Character triggers
     * a PointOfInterest
     *
     * @param character Character that triggered the PointOfInterest
     */
    public abstract void execute(Character character);


    /**
     * Set the position of this PointOfInterest on the map
     *
     * @param x x position on the Map
     * @param y y position on the Map
     */
    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Set the position of this PointOfInterest on the map
     *
     * @param position Position of the POI on the map
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Returns if the create() method has already been called
     * @return true or false
     */
    @Override
    public boolean isCreated() {
        return created;
    }

    /**
     * Set the room in which this POI is intended to be displayed
     * @param room the room instance
     */
    @Override
    public void setRoom(Map room){
        this.room = room;
    }

    /**
     * Get the room in which this POI is rendered
     *
     * @return Map instance
     */
    @Override
    public Map getRoom(){
        return this.room;
    }
}
