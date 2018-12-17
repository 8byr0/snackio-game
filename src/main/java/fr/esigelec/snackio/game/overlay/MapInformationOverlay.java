package fr.esigelec.snackio.game.overlay;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.esigelec.snackio.game.state.AbstractGameState;
import fr.esigelec.snackio.game.state.CoinQuestGameState;
import org.w3c.dom.css.Rect;

import java.awt.*;


public class MapInformationOverlay extends ApplicationAdapter {

    // PLAYER INFO

    // GAME INFO
    AbstractGameState state;

    // RENDERING
    SpriteBatch batch;
    BitmapFont font;
    OrthographicCamera cam;

    /**
     * Default class constructor
     */
    public MapInformationOverlay(AbstractGameState state) {
        this.state = state;
    }

    @Override
    public void create() {

        cam = new OrthographicCamera();

        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam.setToOrtho(false, w, h);
        cam.zoom = 1f;
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        font.draw(batch, "Coins fetched : " + ((CoinQuestGameState)state).getFetchedCoins() + "/" + ((CoinQuestGameState)state).getCoinsToFetch(), 100, 100);
        batch.end();
    }
}
