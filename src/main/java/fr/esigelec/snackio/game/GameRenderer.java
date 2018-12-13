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
import fr.esigelec.snackio.game.character.KeyboardController;
import fr.esigelec.snackio.game.map.Map;
import fr.esigelec.snackio.game.pois.PointOfInterest;

import java.util.ArrayList;

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

    // CHARACTERS PROJECTION
    private Character myDefaultCharacter;
    private ShapeRenderer shapeRenderer;

    private ArrayList<Character> characters;
    private ArrayList<PointOfInterest> pointsOfInterest;

    private GameRenderer() {
        characters = new ArrayList<>();
        pointsOfInterest = new ArrayList<>();
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
        myDefaultCharacter.setMotionController(new KeyboardController());
        myDefaultCharacter.create();

        for(Character character : characters){
            character.create();
        }
        for(PointOfInterest poi : pointsOfInterest){
            poi.create();
        }

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


        for(PointOfInterest poi: pointsOfInterest){
            poi.render();
        }

        // Render character
        myDefaultCharacter.render();

        for(Character character: characters){
            character.render();
        }

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
     * @return true if there's a collision
     */
//    public boolean isCharacterColliding(float x, float y) {
    public boolean isCharacterColliding(Character player, Rectangle playerProjection, Rectangle feetsProjection) {
        int objectLayerId = 2;
        MapLayer collisionObjectLayer = snackioMap.getMap().getLayers().get(objectLayerId);
        MapObjects objects = collisionObjectLayer.getObjects();
        boolean colliding = false;

        for (PolygonMapObject obj : objects.getByType(PolygonMapObject.class)) {
            Polygon poly = obj.getPolygon();

            if (isCollision(poly, feetsProjection)) {
                colliding = true;
                break;
            }
        }


        for(Character character : characters){
            if(Intersector.overlaps(character.getActualProjection(), playerProjection)){
                colliding = true;
                break;
            }
        }

        for(PointOfInterest poi : pointsOfInterest){
            if(Intersector.overlaps(poi.getActualProjection(), playerProjection)){
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
        for(Character character : characters){
            character.dispose();
        }
        myDefaultCharacter.dispose();
        snackioMap.dispose();


        System.exit(0);
    }

    @Override
    public void pause() {
    }

    public void addPointOfInterest(PointOfInterest poi) {
        pointsOfInterest.add(poi);
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }
}