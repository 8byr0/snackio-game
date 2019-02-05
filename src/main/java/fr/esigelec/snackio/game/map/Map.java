package fr.esigelec.snackio.game.map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import fr.esigelec.snackio.game.GameRenderer;
import fr.esigelec.snackio.networking.Position;

import java.util.HashMap;

/**
 * Map object than can be used by GameRenderer
 */
public class Map extends ApplicationAdapter {

    // Graphical objects
    private TiledMap map;
    private CustomTiledMapRenderer renderer;
    private boolean created = false;

    // Map info
    private int mapWidthInPixels;
    private int mapHeightInPixels;
    private String mapPath; // Default value
    protected String name;

    // CAMERA
    private OrthographicCamera cam;

    // ROOMS
    private HashMap<String, MapRoom> rooms;
    private MapRoom activeRoom = null;

    /**
     * Constructor to create a map from a given map path
     * This is package-private because Map must only be created from Factory
     *
     * @param mapPath absolute map path
     */
    Map(String mapPath, String mapName) {
        this.mapPath = mapPath;
        rooms = new HashMap<>();
        this.name = mapName;
    }

    /**
     * Implementation of ApplicationListener's create() method
     * Called once when libgdx is ready
     */
    @Override
    public void create() {
        // Load tileset map
        loadMap();
        configureMap();

        // Retrieve Game camera
        cam = GameRenderer.getInstance().getCamera();

        Batch batch = new SpriteBatch();
        // Initialize map renderer
        renderer = new CustomTiledMapRenderer(map, 1f, batch);

        rooms.forEach((name, room) -> {
            if (!room.isCreated()) {
                room.create();
            }
        });

        created = true;
    }

    /**
     * Load map from assets
     */
    private void loadMap() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(mapPath, TiledMap.class);
        manager.finishLoading();
        map = manager.get(mapPath, TiledMap.class);
    }

    /**
     * Retrieve map information from TMX default file
     */
    private void configureMap() {
        // Read properties
        MapProperties properties = map.getProperties();

        // Map properties
        int tileWidth = properties.get("tilewidth", Integer.class);
        int tileHeight = properties.get("tileheight", Integer.class);
        int mapWidthInTiles = properties.get("width", Integer.class);
        int mapHeightInTiles = properties.get("height", Integer.class);
        mapWidthInPixels = mapWidthInTiles * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
    }

    /**
     * Method triggered continuously when rendering game
     */
    @Override
    public void render() {
        if (null != activeRoom) {
            activeRoom.render();
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Link camera to tiled map
            renderer.setView(cam);

            // Render map
            renderer.render();
        }
    }

    /**
     * Dispose all elements created in create() method
     */
    @Override
    public void dispose() {
        if (null != map) {
            map.dispose();
        }
        if (null != renderer) {
            renderer.dispose();
        }
        rooms.forEach((id, room) -> room.dispose());
    }

    /**
     * Get the map instance
     *
     * @return Tiled Map instance
     */
    public TiledMap getMap() {
        if (null != activeRoom) {
            return activeRoom.getMap();
        }
        return map;
    }

    /**
     * Get the width of the Map in pixels
     *
     * @return width
     */
    public int getMapWidthInPixels() {
        return mapWidthInPixels;
    }

    /**
     * Get the height of the Map in pixels
     *
     * @return height
     */
    public int getMapHeightInPixels() {
        return mapHeightInPixels;
    }

    /**
     * Add a room to this map
     *
     * @param room the room
     */
    void addRoom(MapRoom room) {
        rooms.put(room.getName().toUpperCase(), room);
    }

    /**
     * Set the active room of the map
     * Call this method when user triggers a door on the map
     *
     * @param doorName name of the triggered door
     */
    public void setActiveRoom(String doorName) {
        if (doorName.toUpperCase().equals("BACK_TO_MAIN")) {
            this.activeRoom = null;
        } else {
            /*
             * todo :the name of the room is hard code cause the original code return null.
             * */
            this.activeRoom = this.rooms.get("INSIDE");
            if (!this.activeRoom.isCreated()) {
                this.activeRoom.create();
            }
        }
    }

    /**
     * Returns if the create() method has already been called
     *
     * @return true or false
     */
    protected boolean isCreated() {
        return created;
    }

    public Map getActiveRoom() {
        return (this.activeRoom != null) ? this.activeRoom : this;
    }

    public Map getRoom(String roomName) {
        MapRoom found = this.rooms.get(roomName);
        if (null == found) {
            return this;
        }
        return found;
    }

    /**
     * Get the position of a Door based on its name
     * TODO throw exception if door not found
     *
     * @param doorName the name of the door
     * @return the Position of the door on this map
     */
    public Position getDoorPosition(String doorName) {
        Position pos = new Position();
        boolean found = false;
        for (PolygonMapObject object : getMap().getLayers().get("triggers").getObjects().getByType(PolygonMapObject.class)) {
            // Loop through polygons
            if (object.getName().toUpperCase().equals(doorName)) {
                pos.x = object.getPolygon().getX();
                pos.y = object.getPolygon().getY();
                found = true;
                break;
            }
        }
        if (!found) {
            for (RectangleMapObject object : getMap().getLayers().get("triggers").getObjects().getByType(RectangleMapObject.class)) {
                // Loop through rectangles
                if (object.getName().toUpperCase().equals(doorName)) {
                    pos.x = object.getRectangle().getX();
                    pos.y = object.getRectangle().getY();
                    break;
                }
            }
        }
        return pos;
    }

    /**
     * Get the name of this map
     *
     * @return the name of this map
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this map
     *
     * @param name the new name to give to this map
     */
    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, MapRoom> getRooms() {
        return rooms;
    }
}
