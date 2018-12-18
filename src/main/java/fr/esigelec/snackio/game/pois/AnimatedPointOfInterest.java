package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.GameRenderer;

/**
 * Animated POI has the same behavior as a static POI but its graphical behavior is animated
 * TODO when a position is specified, the room should also be handled
 */
public abstract class AnimatedPointOfInterest extends AbstractPointOfInterest {
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    protected int durationInSeconds = 10;

    private TextureRegion[] animationFrames;
    private Animation coinAnimation;
    private SpriteBatch batch;
    private float stateTime = 0f;

    private String iconPrefix;
    private String iconExtension;
    private int numberOfFrames;

    private OrthographicCamera cam;


    AnimatedPointOfInterest(String prefix, String extension, int numberOfFrames) {
        this.iconPrefix = prefix;
        this.iconExtension = extension;
        this.numberOfFrames = numberOfFrames;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        cam = GameRenderer.getInstance().getCamera();

        animationFrames = new TextureRegion[numberOfFrames];

        for (int itr = 1; itr <= numberOfFrames; ++itr) {
            animationFrames[itr - 1] = new TextureRegion(new Texture(Gdx.files.internal(iconPrefix + Integer.toString(itr) + iconExtension )));
        }

        coinAnimation = (Animation) new Animation(0.1f, animationFrames);

        created = true;
    }

    /**
     * Method called continuously to render the graphics
     */
    @Override
    public void render() {
        stateTime += Gdx.graphics.getDeltaTime();
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        batch.draw(getCurrentFrame(stateTime), position.x, position.y, WIDTH, HEIGHT);
        batch.end();
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

    /**
     * Method called when this object is destroyed
     */
    @Override
    public void dispose() {
        batch.dispose();
    }

    /**
     * Get the projection of this POI on the map
     *
     * @return Rectangle corresponding to projection
     */
    public Rectangle getActualProjection() {
        return new Rectangle(position.x, position.y, WIDTH, HEIGHT);
    }

}
