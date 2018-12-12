package fr.esigelec.snackio.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;


public class GameRenderer implements ApplicationListener {
    private static final double ZOOM_SCALE = 0.8;
    private static final float PAN_SPEED = 16;
    private static final float ROTATE_SPEED = 0.5f;

    private OrthographicCamera cam;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private int mapWidthInPixels;
    private int mapHeightInPixels;

    @Override
    public void create() {
        loadMap();
        configureMap();

        configureCamera();

        renderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void render() {
        // Get cam change and update
        handleInput();
        cam.update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Refresh viewport
        renderer.setView(cam);
        renderer.render();

    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom += ZOOM_SCALE;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= ZOOM_SCALE;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-PAN_SPEED, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(PAN_SPEED, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -PAN_SPEED, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, PAN_SPEED, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.rotate(-ROTATE_SPEED, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.rotate(ROTATE_SPEED, 0, 0, 1);
        }

        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, mapHeightInPixels / cam.viewportWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, mapWidthInPixels - effectiveViewportWidth / 2f);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, mapHeightInPixels - effectiveViewportHeight / 2f);
    }

    private void configureCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(300, 300 * (h / w));
        cam.zoom = 12f;
        cam.update();

    }

    private void loadMap() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("maps/carteVide.tmx", TiledMap.class);
        manager.finishLoading();
        map = manager.get("maps/carteVide.tmx", TiledMap.class);
    }

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
        cam.viewportWidth = 30f;
        cam.viewportHeight = 30f * height / width;
        cam.update();
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        map.dispose();
    }

    @Override
    public void pause() {
    }

}