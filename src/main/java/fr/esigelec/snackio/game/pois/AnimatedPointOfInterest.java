package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.GameRenderer;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.networking.Position;

public abstract class AnimatedPointOfInterest implements ApplicationListener, iPoi {
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    protected int durationInSeconds = 10;
    public abstract void execute(Character character);
    private String pathToIcon;
    private TextureRegion[]  animationFrames;
    private Animation coinAnimation;
    private SpriteBatch batch;
    private float stateTime = 0f;

    private Position position = new Position(400,400);
    private OrthographicCamera cam;

    @Override
    public void create() {
        batch = new SpriteBatch();

        cam = GameRenderer.getInstance().getCamera();
        pathToIcon = "poi/speed_bonus.png";

        animationFrames = new TextureRegion[8];
        int index = 0;

        for (int itr=1; itr <=8; ++itr ) {
            animationFrames[itr-1] = new TextureRegion(new Texture(Gdx.files.internal("poi/coin/coin0"+Integer.toString(itr)+".png")));
        }

        coinAnimation = (Animation)new Animation(0.1f, animationFrames);

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        stateTime += Gdx.graphics.getDeltaTime();
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        batch.draw(getCurrentFrame(stateTime), position.x, position.y, WIDTH, HEIGHT);
        batch.end();
    }

    private TextureRegion getCurrentFrame(float stateTime){
        return (TextureRegion)coinAnimation.getKeyFrame(stateTime, true);
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

    public void setPosition(float x, float y){
        position.x = x;
        position.y = y;
    }
}
