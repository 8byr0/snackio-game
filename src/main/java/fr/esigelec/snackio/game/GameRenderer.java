package fr.esigelec.snackio.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.map.Map;
import fr.esigelec.snackio.game.overlay.MapInformationOverlay;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.networking.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * GameRenderer class is in charge of rendering all graphical elements to screen.
 * Characters, bonuses, maluses... All these objects must be managed from here.
 * Game renderer is package-private
 */
public class GameRenderer extends ApplicationAdapter {

    private static GameRenderer instance = new GameRenderer();

    // Rendering
    private boolean created = false;
    private float stateTime;

    // MAP
    private Map snackioMap;

    // GAME INFO
    MapInformationOverlay overlay;

    // CAMERA
    private OrthographicCamera cam;
    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;

    // CHARACTERS PROJECTION
    private Character activeCharacter;
    private ArrayList<Character> characters;

    // Points of interest
    private ArrayList<iPoi> pointsOfInterest;

    /**
     * Singleton implementation
     *
     * @return GameRenderer existing instance or new if not exists
     */
    public static GameRenderer getInstance() {
        return instance;
    }

    /**
     * Default constructor
     * This is private to be only created in singleton context
     */
    private GameRenderer() {
        characters = new ArrayList<>();
        pointsOfInterest = new ArrayList<>();
    }

    /**
     * Method called once libgdx is ready and running
     * Create all UI elements (Map, Characters, POI...
     */
    @Override
    public void create() {
        overlay = new MapInformationOverlay(SnackioGame.getInstance().getGameState());

        Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/dungeon.ogg"));
        music.setVolume(0.15f);
        music.play();

        // Set state time
        this.stateTime = 0f;

        // Load tileset map
        snackioMap.create();

        // Configure camera and viewport
        configureCamera();

        // Create all characters
        for (Character character : characters) {
            character.create();
        }

        // Create all POI
        for (iPoi poi : pointsOfInterest) {
            poi.create();
            poi.setRoom(this.getRandomRoom(true));
            poi.setPosition(this.getRandomPosition(poi.getRoom(), poi.getActualProjection()));
        }

        overlay.create();

        created = true;
    }

    /**
     * Get a random room from all the rooms available and loaded in this game
     *
     * @param includeMainRoom set to true if you want to include the main room (root)
     * @return Map instance one of the rooms or the root room
     */
    private Map getRandomRoom(boolean includeMainRoom) {
        Random generator = new Random();
        List<Map> roomsList = new ArrayList<Map>(this.snackioMap.getRooms().values());

        if (includeMainRoom) {
            roomsList.add(this.snackioMap);
        }

        return roomsList.get(new Random().nextInt(roomsList.size()));
    }

    /**
     * Method called continuously by libgdx to render graphical elements
     */
    @Override
    public void render() {
        // Properly position camera on player
        moveCamera(activeCharacter.getPosition().x, activeCharacter.getPosition().y);

        // Increment stateTime
        stateTime += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        snackioMap.getMap().getLayers().get("characters").getObjects().forEach((character) -> {
            if (!characters.contains(character)) {
                snackioMap.getMap().getLayers().get("characters").getObjects().remove(character);
            }
        });

        // Add Characters to map's characters layer
        for (Character character : characters) {
            if (character.created()) {
                if (character.getRoom().equals(this.snackioMap.getActiveRoom().getName())) {
                    if (snackioMap.getMap().getLayers().get("characters").getObjects().getIndex(character) == -1) {
                        snackioMap.getMap().getLayers().get("characters").getObjects().add(character);
                    }
                } else {
                    if (snackioMap.getMap().getLayers().get("characters").getObjects().getIndex(character) != -1) {
                        snackioMap.getMap().getLayers().get("characters").getObjects().remove(character);
                    }
                }
            }
        }

        // Render tiled map
        snackioMap.render();


        for (iPoi poi : pointsOfInterest) {
            if (poi.isCreated()) {
                if (poi.getRoom().getName().equals(this.snackioMap.getActiveRoom().getName())) {
                    poi.render();
                }
            } else {
                poi.create();
                poi.setRoom(this.getRandomRoom(true));
                poi.setPosition(this.getRandomPosition(poi.getRoom(), poi.getActualProjection()));
            }
        }

        overlay.render();

    }

    /**
     * Move camera to a given position
     *
     * @param x x
     * @param y y
     */
    private void moveCamera(float x, float y) {
        if ((x > CAMERA_WIDTH / 2) || (y > CAMERA_HEIGHT / 2)) {
            cam.position.set(x, y, 0);
            cam.update();
        }

    }

    /**
     * Detect if there's a collision between character coordinates and objects' layer
     * This method should only be used in a preventive way.
     * E.g: projection of estimated position without moving character.
     *
     * @return true if there's a collision
     */
    public boolean isCharacterColliding(Character character, Rectangle playerProjection, Rectangle feetsProjection) {
        boolean colliding = false;

        // is Character triggering a door ?
        MapObject triggeredDoor = this.isCharacterTriggeringADoor(feetsProjection);
        if (null != triggeredDoor) {
            String doorName = triggeredDoor.getName();
            String roomName = triggeredDoor.getProperties().get("room_name").toString();
            String destinationDoorName = triggeredDoor.getProperties().get("destination_name").toString();
            String destinationDirection = triggeredDoor.getProperties().get("destination_direction").toString();

            System.out.println("DOOR TRIGGERED ! " + doorName + " in room " + roomName);

            snackioMap.setActiveRoom(roomName);

            Map room = snackioMap.getRoom(roomName);
            Position newPlayerPosition = room.getDoorPosition(destinationDoorName);
            switch (destinationDirection) {
                case "NORTH":
                    newPlayerPosition.y += 20;
                    break;
                case "SOUTH":
                    newPlayerPosition.y -= 20;
                    break;
                case "EAST":
                    newPlayerPosition.x += 20;
                    break;
                case "WEST":
                    newPlayerPosition.x -= 20;
                    break;
            }
            character.setPosition(newPlayerPosition);

            character.setRoom(this.snackioMap.getActiveRoom().getName());
        }

        // is Character colliding obstacles ?
        MapObjects obstacles = snackioMap.getMap().getLayers().get("obstacles").getObjects();

        if (null != isCharacterCollidingMapObject(obstacles, feetsProjection)) {
            colliding = true;
        }

        // is Character colliding other Characters ?
        if (this.isCharacterCollidingCharacter(character, playerProjection)) {
            colliding = true;
        }

        // is Character triggering POI ?
        isCharacterTriggeringPOI(character, playerProjection);

        return colliding;
    }

    /**
     * Returns if a Character is colliding another Character on the loaded map
     *
     * @param player           the player
     * @param playerProjection the player's Character projection
     * @return true if collision
     */
    private boolean isCharacterCollidingCharacter(Character player, Rectangle playerProjection) {
        boolean colliding = false;
        for (Character character : characters) {
            if (Intersector.overlaps(character.getActualProjection(), playerProjection)) {
                if (character != player) {
                    // Make sure that both characters are on same layer
                    if (character.getRoom().equals(player.getRoom())) {
                        colliding = true;
                        break;
                    }
                }
            }
        }
        return colliding;
    }


    /**
     * Execute POI callback method if selected Character is overlapping a POI
     *
     * @param character           the character
     * @param characterProjection the Character's projection
     */
    private void isCharacterTriggeringPOI(Character character, Rectangle characterProjection) {
        for (iPoi poi : pointsOfInterest) {
            if (character.getRoom().equals(poi.getRoom().getName())) {
                if (Intersector.overlaps(poi.getActualProjection(), characterProjection)) {
                    poi.execute(character);
                    break;
                }
            }
        }
    }

    /**
     * Returns if a Character is triggering a door on the loaded map
     *
     * @param feetsProjection the Character's feets' projection
     * @return true if trigger detected
     */
    private MapObject isCharacterTriggeringADoor(Rectangle feetsProjection) {
        MapObjects triggers = snackioMap.getMap().getLayers().get("triggers").getObjects();

        MapObject triggered = this.isCharacterCollidingMapObject(triggers, feetsProjection);
        if (null != triggered) {
            if (triggered.getProperties().get("type").toString().toUpperCase().equals("DOOR")) {
                return triggered;
            }
        }
        return null;
    }

    /**
     * Returns if a Character is colliding any map object in a given MapObjects instance
     * This method checks for Rectangle and Polygons
     *
     * @param objects         map objects
     * @param feetsProjection the Character's feets' projection
     * @return MapObject if collision or null
     */
    private MapObject isCharacterCollidingMapObject(MapObjects objects, Rectangle feetsProjection) {
        MapObject found = null;

        // Check Polygons
        for (PolygonMapObject obj : objects.getByType(PolygonMapObject.class)) {
            Polygon poly = obj.getPolygon();

            if (isCollision(poly, feetsProjection)) {
                found = obj;
                break;
            }
        }

        // Check rectangles
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();

            if (Intersector.overlaps(rectangle, feetsProjection)) {
                found = rectangleObject;
                break;
            }

        }
        return found;
    }

    /**
     * Tells wether there's a collision between a polygon and a rectangle
     *
     * @param p polygon
     * @param r rectangle
     * @return true if collision, false otherwise
     * TODO move this in a dedicated class
     */
    private boolean isCollision(Polygon p, Rectangle r) {
        Polygon rPoly = new Polygon(new float[]{0, 0, r.width, 0, r.width, r.height, 0, r.height});
        rPoly.setPosition(r.x, r.y);
        return Intersector.overlapConvexPolygons(rPoly, p);
    }

    /**
     * Configure Scene's camera behavior, zoom, viewport...
     */
    private void configureCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        if (null == cam) {
            cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        cam.setToOrtho(false, w, h);
        cam.zoom = 1f;
        cam.update();

    }

    /**
     * Get the Game's camera
     *
     * @return Camera instance
     */
    public OrthographicCamera getCamera() {
        if (null == cam) {
            configureCamera();
        }
        return cam;
    }

    /**
     * Set the Tiled Map that will be used in this game
     *
     * @param map a map instance
     */
    void setSnackioMap(Map map) {
        this.snackioMap = map;
        for (Character character : characters) {
            character.setRoom(map.getName());
        }
    }

    /**
     * Method triggered when the windows is resized
     *
     * @param width  new width
     * @param height new height
     */
    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = Gdx.graphics.getWidth();
        cam.viewportHeight = Gdx.graphics.getHeight();// * height / width;
        cam.update();
    }

    /**
     * Dispose all graphical elements created
     */
    @Override
    public void dispose() {
        for (Character character : characters) {
            character.dispose();
        }
        for (iPoi poi : pointsOfInterest) {
            poi.dispose();
        }

        snackioMap.dispose();
        System.exit(0);
    }

    /**
     * Add a Point of interest to the map
     *
     * @param poi the Point of interest to add
     */
    void addPointOfInterest(iPoi poi) {
        pointsOfInterest.add(poi);
    }

    /**
     * Remove a Point of interest from the map
     *
     * @param poi the Point of interest to remove
     */
    void removePointOfInterest(iPoi poi) {
        pointsOfInterest.remove(poi);
    }

    /**
     * Add a Character to the map
     *
     * @param character the Character to add
     * @param active    if this is true, the character will be the main one, followed by the camera
     */
    void addCharacter(Character character, boolean active) {
        if (active) {
            activeCharacter = character;
        }
        if (!character.created() && this.created) {
            new Thread(() -> Gdx.app.postRunnable(character::create)).start();
        }
        characters.add(character);
    }

    void removeCharacter(Character character) {
        this.characters.remove(character);
    }

    /**
     * Get a random position on the game's map (in a given room) for a given object projection
     * This position is ensured to be free(i.e. not an obstacle or trigger)
     *
     * @param projection the projection of the object on the map
     * @return a Position on the map
     */
    private Position getRandomPosition(Map room, Rectangle projection) {
        float mapWidth = room.getMapWidthInPixels();
        float mapHeight = room.getMapWidthInPixels();

        int tempRandomX = generateRandomInRange(0, mapWidth);
        int tempRandomY = generateRandomInRange(0, mapHeight);
        Position tempRandom = new Position(tempRandomX, tempRandomY);

        while ((null != isCharacterCollidingMapObject(room.getMap().getLayers().get("triggers").getObjects(), projection) ||
                (null != isCharacterCollidingMapObject(room.getMap().getLayers().get("obstacles").getObjects(), projection)))) {
            tempRandom.x = generateRandomInRange(0, mapWidth);
            tempRandom.y = generateRandomInRange(0, mapHeight);
        }
        return tempRandom;
    }

    /**
     * Get a random value in a given range
     * TODO move this to a dedicated class
     * @param min range min
     * @param max range max
     * @return int value between min and max
     */
    private int generateRandomInRange(float min, float max) {
        return ThreadLocalRandom.current().nextInt((int) min, (int) max);
    }
}