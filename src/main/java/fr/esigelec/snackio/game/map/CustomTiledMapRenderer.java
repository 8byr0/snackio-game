package fr.esigelec.snackio.game.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import fr.esigelec.snackio.game.character.Character;

/**
 * This custom tiled map renderer allows to trick actual rendering classes and
 * handle the drawing of Character objects
 */
public class CustomTiledMapRenderer extends OrthogonalTiledMapRenderer {

    /**
     * Overrided class constructor
     * @param map the TiledMap on which this renderer applies
     * @param unitScale unit scale of the map
     * @param batch batch renderer
     */
    CustomTiledMapRenderer (TiledMap map, float unitScale, Batch batch) {
        super(map, unitScale);
    }

    /**
     * Overrides the default behavior
     * When a Character is fetched to be rendered, custom code executed
     * @param object current MapObject to render
     */
    @Override
    public void renderObject(MapObject object) {
        super.renderObject(object);

        if(object instanceof Character) {
            ((Character) object).render(batch);
        }
    }
}
