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
import com.badlogic.gdx.maps.MapLayer;
import fr.esigelec.snackio.core.GameMode;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.game.state.AbstractGameState;
import fr.esigelec.snackio.game.state.CoinQuestGameState;
import fr.esigelec.snackio.game.state.MultiplayerGameState;


public class MapInformationOverlay extends ApplicationAdapter {
    private static final int COIN_WIDTH = 32;
    private static final int COIN_HEIGHT = 32;
    private static final int HEART_WIDTH = 32;
    private static final int HEART_HEIGHT = 32;
    private float stateTime = 0f;

    // PLAYER INFO

    // GAME INFO
    AbstractGameState state;

    // RENDERING
    SpriteBatch batch;
    BitmapFont font;
    OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private TextureRegion[] animationFrames;
    private Animation coinAnimation;

    private TextureRegion[] animationFramesHeart;
    private Animation heartAnimation;

    /**
     * Default class constructor
     */
    public MapInformationOverlay(AbstractGameState state) {
        this.state = state;
    }

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();

        cam = new OrthographicCamera();

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2.5f);

        //         //heart animation
        animationFramesHeart = new TextureRegion[1];

        for (int itr = 1; itr <= 1; ++itr) {
            animationFramesHeart[itr - 1] = new TextureRegion(new Texture(Gdx.files.internal("poi/heart/heart"
                    + Integer.toString(itr) + ".png")));
        }

        heartAnimation = (Animation) new Animation(0.1f, animationFramesHeart);


        //         //coin animation
        animationFrames = new TextureRegion[8];

        for (int itr = 1; itr <= 8; ++itr) {
            animationFrames[itr - 1] = new TextureRegion(new Texture(Gdx.files.internal("poi/coin/coin0"
                    + Integer.toString(itr) + ".png")));
        }

        coinAnimation = (Animation) new Animation(0.1f, animationFrames);
    }

    @Override
    public void render() {
        stateTime += Gdx.graphics.getDeltaTime();

        renderCamera();




        if (state instanceof CoinQuestGameState) {
            shapeRenderer.setProjectionMatrix(cam.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(80, 80, 100, 10);
            shapeRenderer.end();


            batch.setProjectionMatrix(cam.combined);
            batch.begin();
            batch.draw(getCurrentFrame(stateTime), 82, 85, COIN_WIDTH, COIN_HEIGHT);

            //show the coin information
            font.draw(batch, ((CoinQuestGameState) state).getFetchedCoins()
                    + "/" + ((CoinQuestGameState) state).getCoinsToFetch()
                    , 120, 115);
            batch.end();
        }

        if(state instanceof MultiplayerGameState){
            batch.setProjectionMatrix(cam.combined);
            batch.begin();
            for (int n = 0; n < ((MultiplayerGameState) state).getActivePlayer().getLives(); n++){
                batch.draw((TextureRegion) heartAnimation.getKeyFrame(stateTime, true), 430+n*40, 18,
                        HEART_WIDTH, HEART_HEIGHT);
            }

            //show the player information
            int offset = 1;
            for(Player player : ((MultiplayerGameState) state).getPlayers()){
                font.draw(batch, player.toString(), 200, 150 + offset * 50);
                offset += 1;
                for (int n = 0; n < player.getLives(); n++){
                    batch.draw((TextureRegion) heartAnimation.getKeyFrame(stateTime, true), 350+n*40, 70 + offset * 50,
                            HEART_WIDTH, HEART_HEIGHT);
                }

            }
            font.draw(batch, "Votre salle: " + ((MultiplayerGameState) state).getRoomName(), 200, 100);
            font.draw(batch, "Vies restants: ", 200,50);
            batch.end();
        }



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

    private void ExtraInfoDisplay(){
        //batch.draw();
    }
}
