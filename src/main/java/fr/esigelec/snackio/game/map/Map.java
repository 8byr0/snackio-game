package fr.esigelec.snackio.game.map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import fr.esigelec.snackio.game.GameRenderer;

public class Map implements ApplicationListener {
    // Graphical objects
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Persisted properties
    private int mapWidthInPixels;
    private int mapHeightInPixels;
    private String mapPath; // Default value
    // CAMERA
    private OrthographicCamera cam;

    public Map(){
        this("maps/snackio.tmx");
    }
    public Map(String mapPath){
        this.mapPath = mapPath;
        // Load tileset map
        loadMap();
        configureMap();

        // Retrieve Game camera
        cam = GameRenderer.getInstance().getCamera();

        // Initialize map renderer
        renderer = new OrthogonalTiledMapRenderer(map, 1f);

    }

    @Override
    public void create() {
    }

    /**
     * Load default map from assets
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

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Link camera to tiled map
        renderer.setView(cam);

        // Render map
        renderer.render();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }

    public TiledMap getMap() {
        return map;
    }

    public int getMapWidthInPixels() {
        return mapWidthInPixels;
    }

    public int getMapHeightInPixels() {
        return mapHeightInPixels;
    }
}
