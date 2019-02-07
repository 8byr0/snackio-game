package fr.esigelec.snackio.game.overlay;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.esigelec.snackio.Snackio;
import fr.esigelec.snackio.core.GameMode;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.state.AbstractGameState;
import fr.esigelec.snackio.game.state.CoinQuestGameState;


public class MapInformationOverlay extends ApplicationAdapter {

    private static final int HEART_WIDTH = 50;
    private static final int HEART_HEIGHT = 50;

    private static final int COIN_WIDTH = 32;
    private static final int COIN_HEIGHT = 32;
    private float stateTime = 0f;
    private Texture texture;

    // PLAYER INFO

    // GAME INFO
    AbstractGameState state;

    // RENDERING
    SpriteBatch batch;
    BitmapFont font;
    OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private TextureRegion[] animationFrames;
    private Texture heart;
    private TextureRegion heartRegion;
    private Animation coinAnimation;

    /**
     * Default class constructor
     */
    public MapInformationOverlay(AbstractGameState state) {
        this.state = state;
    }

    @Override
    public void create() {
//        heart = new Texture(Gdx.files.internal("poi/heart.png"));
//        heartRegion = new TextureRegion(heart,0,0,HEART_WIDTH,HEART_HEIGHT);
        shapeRenderer = new ShapeRenderer();

        cam = new OrthographicCamera();

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2.5f);

        animationFrames = new TextureRegion[8];

        for (int itr = 1; itr <= 8; ++itr) {
            animationFrames[itr - 1] = new TextureRegion(new Texture(Gdx.files.internal("poi/coin/coin0" + Integer.toString(itr) + ".png")));
        }

        texture = new Texture(Gdx.files.internal("poi/heart.png"));


        coinAnimation = (Animation) new Animation(0.1f, animationFrames);

    }

    @Override
    public void render() {
        stateTime += Gdx.graphics.getDeltaTime();

        renderCamera();
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(80, 80, 100, 10);
        shapeRenderer.end();


        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        if (state.getGameMode() == GameMode.COINS_QUEST) {
            batch.draw(getCurrentFrame(stateTime), 82, 85, COIN_WIDTH, COIN_HEIGHT);
            batch.draw(texture, 70, Gdx.graphics.getHeight()-85, HEART_WIDTH, HEART_HEIGHT);

            font.draw(batch, ((CoinQuestGameState) state).getFetchedCoins() + "/" + ((CoinQuestGameState) state).getCoinsToFetch(), 120, 115);
            font.draw(batch,"X " + SnackioGame.getInstance().lives,125,Gdx.graphics.getHeight()-50);
        }
        batch.end();

    }

    private void renderCamera() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam.setToOrtho(false, w, h);
        cam.zoom = 1f;
        cam.update();
    }

    /**
     * Get the current frame of this animation
     *
     * @param stateTime state to get
     * @return the Frame to display at given time
     */
    private TextureRegion getCurrentFrame(float stateTime) {
        return (TextureRegion) coinAnimation.getKeyFrame(stateTime, true);
    }

}
