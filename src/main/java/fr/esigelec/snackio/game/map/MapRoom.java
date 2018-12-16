package fr.esigelec.snackio.game.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.networking.Position;

/**
 * MapRoom is a map than can be accessed from another map.
 * Let's consider it's a kind of room inside the map.
 * <p>
 * To create a new room, you need to add to your `triggers` map layer a shape(Polygon or Rectangle)
 * which property `Type` is `DOOR`.
 * You must also specify a name for the door object.
 * <p>
 * This name is the one that will trigger the room and must be set at MapRoom init like this:
 * {@code
 * // Create the base map
 * Map desertCastleMap = new Map("maps/snackio.tmx");
 * // Add map rooms
 * MapRoom cave = new MapRoom("maps/snackio_cave.tmx", "CAVE", desertCastleMap);
 * desertCastleMap.addRoom(cave);
 * }
 * Don't forget to pass a reference to the main Map !
 * This reference will be used to go back to the main map when the room's door is triggered.
 * To do so, the room must also have a `triggers` layer and contain a shape with the type `DOOR` and the name `BACK_TO_MAIN`
 */
public class MapRoom extends Map {
    private Map mapContainer;

    /**
     * Constructor to create a map from a given map path
     * This is package-private because Map must only be created from Factory
     *
     * @param mapPath absolute map path
     */
    MapRoom(String mapPath, String roomName, Map container) {
        super(mapPath, roomName);
        this.mapContainer = container;
    }

    @Override
    public void setActiveRoom(String doorName) {
        this.mapContainer.setActiveRoom(doorName);
    }

    @Override
    public void addRoom(MapRoom room) {
        System.out.println("Adding a Room to another Room is not allowed.");
    }
}
