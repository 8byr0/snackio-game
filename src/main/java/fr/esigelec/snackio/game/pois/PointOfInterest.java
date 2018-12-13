package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.GameRenderer;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.networking.Position;

public abstract class PointOfInterest implements ApplicationListener {
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    protected int durationInSeconds = 10;
    public abstract void execute(Character character);
    private String pathToIcon;
    private Texture texture;
    private SpriteBatch batch;

    private Position position = new Position(400,400);
    private OrthographicCamera cam;

    @Override
    public void create() {
        batch = new SpriteBatch();

        cam = GameRenderer.getInstance().getCamera();
        pathToIcon = "poi/speed_bonus.png";
        texture = new Texture(Gdx.files.internal(pathToIcon));

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        batch.draw(texture, position.x, position.y, WIDTH, HEIGHT);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public Rectangle getActualProjection() {
        return new Rectangle(position.x, position.y, WIDTH, HEIGHT);
    }
}
