package fr.esigelec.snackio.game.util;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.esigelec.snackio.game.GameRenderer;

/**
 * A static texture is a texture that is not animated
 */
public class StaticTexture extends ApplicationAdapter {
    private int WIDTH;
    private int HEIGHT;

    private String pathToIcon;
    private Texture texture;
    private SpriteBatch batch;
    private OrthographicCamera cam;

    /**
     * Create a StaticTexture instance base on an image
     *
     * @param pathToIcon path to texture image
     * @param width      width of the image
     * @param height     height of the image
     */
    public StaticTexture(String pathToIcon, int width, int height) {
        this.pathToIcon = pathToIcon;
        WIDTH = width;
        HEIGHT = height;
    }

    /**
     * Method called by libgdx when creating this PointOfInterest
     */
    @Override
    public void create() {
        this.texture = new Texture(Gdx.files.internal(pathToIcon));

        batch = new SpriteBatch();
        cam = GameRenderer.getInstance().getCamera();
    }

    /**
     * Method called continuously when rendering game
     * @param x x position
     * @param y y position
     */
    public void render(float x, float y) {
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        batch.draw(texture, x, y, WIDTH, HEIGHT);
        batch.end();
    }

    /**
     * Behavior when this object is destroyed
     */
    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }

    /**
     * Get the width of the texture
     *
     * @return width value
     */
    public float getHeight() {
        return HEIGHT;
    }

    /**
     * Get the height of the texture
     *
     * @return height value
     */
    public float getWidth() {
        return WIDTH;
    }
}
