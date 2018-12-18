package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.map.Map;
import fr.esigelec.snackio.game.map.MapRoom;
import fr.esigelec.snackio.game.util.StaticTexture;
import fr.esigelec.snackio.networking.Position;

/**
 * Abstract PointOfInterest class that must be inherited for any Bonus/Malus implemented
 */
public abstract class PointOfInterest extends ApplicationAdapter implements iPoi {
    // PROPERTIES
    protected int durationInSeconds = 10;

    // TEXTURE / RENDERING
    protected StaticTexture image;

    // MOTION
    private Position position = new Position(400, 400);

    // STATUS
    private boolean created = false;
    private Map room;

    /**
     * Abstract method to implement when inheriting PointOfInterest
     * The content of this method is the code executed when a Character triggers
     * a PointOfInterest
     *
     * @param character Character that triggered the PointOfInterest
     */
    public abstract void execute(Character character);

    /**
     * Method called by libgdx when creating this PointOfInterest
     */
    @Override
    public void create() {
        image = new StaticTexture("poi/speed_bonus.png", 32, 32);
        image.create();
        this.created = true;
    }

    /**
     * Render graphical elements
     */
    @Override
    public void render() {
        image.render(position.x, position.y);
    }

    /**
     * Dispose all graphical components
     */
    @Override
    public void dispose() {
        image.dispose();
    }

    /**
     * Get the projection (Rectangle) of this PointOfInterest on the Map
     *
     * @return Rectangle
     */
    public Rectangle getActualProjection() {
        return new Rectangle(position.x, position.y, image.getWidth(), image.getHeight());
    }

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

    @Override
    public boolean isCreated() {
        return created;
    }

    @Override
    public void setRoom(Map room){
        this.room = room;
    }

    @Override
    public Map getRoom(){
        return this.room;
    }
}
