package fr.esigelec.snackio.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.map.Map;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.game.pois.iPoi;

import java.util.ArrayList;

/**
 * GameRenderer class is in charge of rendering all graphical elements to screen.
 * Characters, bonuses, maluses... All these objects must be managed from here.
 * Game renderer is package-private
 * TODO add a method to get an empty position randomly and a method to know if a position is empty
 */
public class GameRenderer extends ApplicationAdapter {

    private static GameRenderer instance = new GameRenderer();

    // Rendering
    private boolean created = false;
    private float stateTime;
    private ShapeRenderer shapeRenderer;

    // MAP
    private Map snackioMap;

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
        Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/dungeon.ogg"));
        music.setVolume(0.15f);
        music.play();

        // Set state time
        this.stateTime = 0f;

        // Load tileset map
        snackioMap = MapFactory.getMap(MapFactory.MapType.DESERT_CASTLE);

        // Configure camera and viewport
        configureCamera();

        // Create all characters
        for (Character character : characters) {
            character.create();
        }

        // Create all POI
        for (iPoi poi : pointsOfInterest) {
            poi.create();
        }

        // Initialize map renderer
        shapeRenderer = new ShapeRenderer();
        created = true;
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

        // Render tiled map
        snackioMap.render();

        float x = activeCharacter.getPosition().x;
        float y = activeCharacter.getPosition().y;
        shapeRenderer.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //I'm using the Filled ShapeType, but remember you have three of them
        shapeRenderer.setColor(Color.LIGHT_GRAY);

        shapeRenderer.rect(x + 16, y, 32, 16); //assuming you have created those x, y, width and height variables
        shapeRenderer.end();


        for (iPoi poi : pointsOfInterest) {
            poi.render();
        }

        // Render all created characters
        for (Character character : characters) {
            if (character.created()) {
                character.render();
            }
        }

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
    public boolean isCharacterColliding(Character player, Rectangle playerProjection, Rectangle feetsProjection) {
        MapObjects objects = snackioMap.getMap().getLayers().get("obstacles").getObjects();

        boolean colliding = false;

        for (PolygonMapObject obj : objects.getByType(PolygonMapObject.class)) {
            Polygon poly = obj.getPolygon();

            if (isCollision(poly, feetsProjection)) {
                colliding = true;
                break;
            }
        }


        for (Character character : characters) {
            if (Intersector.overlaps(character.getActualProjection(), playerProjection)) {
                if (character != player) {
                    colliding = true;
                    break;
                }
            }
        }

        for (iPoi poi : pointsOfInterest) {
            if (Intersector.overlaps(poi.getActualProjection(), playerProjection)) {
                poi.execute(player);
                break;
            }
        }

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();

            if (Intersector.overlaps(rectangle, feetsProjection)) {
                colliding = true;
                break;
            }

        }
        return colliding;
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

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = Gdx.graphics.getWidth();
        cam.viewportHeight = Gdx.graphics.getHeight();// * height / width;
        cam.update();
    }

    @Override
    public void resume() {
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

    @Override
    public void pause() {
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
}