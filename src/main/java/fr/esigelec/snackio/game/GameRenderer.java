package fr.esigelec.snackio.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import org.lwjgl.openal.AL;


public class GameRenderer extends ApplicationAdapter {

    private static GameRenderer instance = new GameRenderer();
    private ShapeRenderer shapeRenderer;

    static GameRenderer getInstance() {
        return instance;
    }

    private OrthographicCamera cam;

    // MAP
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;
    private int mapWidthInPixels;
    private int mapHeightInPixels;

    // CHARACTER PROJECTION
    private float stateTime;
    private Character myDefaultCharacter;


    private GameRenderer() {

    }


    @Override
    public void create() {
        // Set state time
        stateTime = 0f;

        // Load tileset map
        loadMap();
        configureMap();

        // Configure camera and viewport
        configureCamera();

        // Load a sample character
        myDefaultCharacter = new Character(cam);

        // Initialize map renderer
        renderer = new OrthogonalTiledMapRenderer(map);
        shapeRenderer = new ShapeRenderer();

    }

    @Override
    public void render() {
        // Properly position camera on player
        moveCamera(myDefaultCharacter.getPosition().x, myDefaultCharacter.getPosition().y);

        // Increment stateTime
        stateTime += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Link camera to tiled map
        renderer.setView(cam);

        // Render map
        renderer.render();

        float x = myDefaultCharacter.getPosition().x;
        float y = myDefaultCharacter.getPosition().y;
        shapeRenderer.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //I'm using the Filled ShapeType, but remember you have three of them
        shapeRenderer.setColor(Color.LIGHT_GRAY);

        shapeRenderer.rect(x+16,y,32,16); //assuming you have created those x, y, width and height variables
        shapeRenderer.end();


        // Render character
        myDefaultCharacter.render();

    }

    /**
     * Move camera to a given position
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
     * @param x x position of the character
     * @param y y position of the character
     * @return true if there's a collision
     */
    boolean isCollision(float x, float y) {
        int objectLayerId = 2;
        MapLayer collisionObjectLayer = map.getLayers().get(objectLayerId);
        MapObjects objects = collisionObjectLayer.getObjects();
        boolean colliding = false;

        for (PolygonMapObject obj : objects.getByType(PolygonMapObject.class)) {
            Polygon poly = obj.getPolygon();

            Rectangle playerProjection = new Rectangle(x, y, 32, 16);
            if (isCollision(poly, playerProjection)) {
                colliding = true;
                break;
            }
        }
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();

            Rectangle playerProjection = new Rectangle(x, y, 32, 16);
            if (Intersector.overlaps(rectangle, playerProjection)) {
                colliding = true;
                break;
            }

        }
        return colliding;
    }
    private boolean isCollision(Polygon p, Rectangle r) {
        Polygon rPoly = new Polygon(new float[] { 0, 0, r.width, 0, r.width,
                r.height, 0, r.height });
        rPoly.setPosition(r.x, r.y);
        if (Intersector.overlapConvexPolygons(rPoly, p))
            return true;
        return false;
    }

    /**
     * Configure Scene's camera behavior, zoom, viewport...
     */
    private void configureCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.setToOrtho(false, w, h);
        cam.zoom = 1f;
        cam.update();

    }

    /**
     * Load default map from assets
     */
    private void loadMap() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("maps/snackio.tmx", TiledMap.class);
        manager.finishLoading();
        map = manager.get("maps/snackio.tmx", TiledMap.class);
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

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = Gdx.graphics.getWidth();
        cam.viewportHeight = Gdx.graphics.getHeight();// * height / width;
        cam.update();
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        myDefaultCharacter.dispose();
        map.dispose();
        renderer.dispose();

        System.exit(0);
    }

    @Override
    public void pause() {
    }

    public int getMapHeightInPixels() {
        return mapHeightInPixels;
    }

    public void setMapHeightInPixels(int mapHeightInPixels) {
        this.mapHeightInPixels = mapHeightInPixels;
    }

    public int getMapWidthInPixels() {
        return mapWidthInPixels;
    }

    public void setMapWidthInPixels(int mapWidthInPixels) {
        this.mapWidthInPixels = mapWidthInPixels;
    }
}