package fr.esigelec.snackio.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.map.Map;

/**
 * GameRenderer class is in charge of rendering all graphical elements to screen.
 * Characters, bonuses, maluses... All these objects must be managed from here.
 */
public class GameRenderer extends ApplicationAdapter {

    private static GameRenderer instance = new GameRenderer();

    /**
     * Singleton implementation
     * @return GameRenderer existing instance or new if not exists
     */
    public static GameRenderer getInstance() {
        return instance;
    }

    private float stateTime;

    // MAP
    private Map snackioMap;

    // CAMERA
    private OrthographicCamera cam;
    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;

    // CHARACTER PROJECTION
    private Character myDefaultCharacter;
    private ShapeRenderer shapeRenderer;


    private GameRenderer() {

    }


    @Override
    public void create() {
        // Set state time
        this.stateTime = 0f;

        // Load tileset map
        snackioMap = new Map();

        // Configure camera and viewport
        configureCamera();

        // Load a sample character
        myDefaultCharacter = CharacterFactory.getCharacter(CharacterFactory.CharacterType.INDIANA);

        // Initialize map renderer
        shapeRenderer = new ShapeRenderer();

    }

    @Override
    public void render() {
        // Properly position camera on player
        moveCamera(myDefaultCharacter.getPosition().x, myDefaultCharacter.getPosition().y);

        // Increment stateTime
        stateTime += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render tiled map
        snackioMap.render();

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
    public boolean isCollision(float x, float y) {
        int objectLayerId = 2;
        MapLayer collisionObjectLayer = snackioMap.getMap().getLayers().get(objectLayerId);
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
        return Intersector.overlapConvexPolygons(rPoly, p);
    }

    /**
     * Configure Scene's camera behavior, zoom, viewport...
     */
    private void configureCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        if(null == cam){
            cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        cam.setToOrtho(false, w, h);
        cam.zoom = 1f;
        cam.update();

    }

    public OrthographicCamera getCamera(){
        if(null==cam){
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

    @Override
    public void dispose() {
        myDefaultCharacter.dispose();
        snackioMap.dispose();


        System.exit(0);
    }

    @Override
    public void pause() {
    }

}