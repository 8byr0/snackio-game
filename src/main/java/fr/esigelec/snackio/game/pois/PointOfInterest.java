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
public abstract class PointOfInterest extends AbstractPointOfInterest implements iPoi {
    // PROPERTIES
    protected int durationInSeconds = 10;

    // TEXTURE / RENDERING
    private StaticTexture image;
    private String pathToImage;

    public PointOfInterest(String pathToImage){
        this.pathToImage = pathToImage;
    }

    /**
     * Method called by libgdx when creating this PointOfInterest
     */
    @Override
    public void create() {
        image = new StaticTexture(pathToImage, 32, 32);
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


}
