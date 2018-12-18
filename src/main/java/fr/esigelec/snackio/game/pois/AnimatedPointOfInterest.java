package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.GameRenderer;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.map.Map;
import fr.esigelec.snackio.networking.Position;

/**
 * Animated POI has the same behavior as a static POI but its graphical behavior is animated
 * TODO when a position is specified, the room should also be handled
 */
public abstract class AnimatedPointOfInterest extends ApplicationAdapter implements iPoi {
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
    private Map room;

    private boolean created = false;

    @Override
    public void create() {
        batch = new SpriteBatch();

        cam = GameRenderer.getInstance().getCamera();
        pathToIcon = "poi/speed_bonus.png";

        animationFrames = new TextureRegion[8];

        for (int itr=1; itr <=8; ++itr ) {
            animationFrames[itr-1] = new TextureRegion(new Texture(Gdx.files.internal("poi/coin/coin0"+Integer.toString(itr)+".png")));
        }

        coinAnimation = (Animation)new Animation(0.1f, animationFrames);

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
     * @param stateTime state to get
     * @return the Frame to display at given time
     */
    private TextureRegion getCurrentFrame(float stateTime){
        return (TextureRegion)coinAnimation.getKeyFrame(stateTime, true);
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
     * @return Rectangle corresponding to projection
     */
    public Rectangle getActualProjection() {
        return new Rectangle(position.x, position.y, WIDTH, HEIGHT);
    }

    /**
     * Set the position of this PointOfInterest on the map
     * @param x x position
     * @param y y position
     */
    public void setPosition(float x, float y){
        position.x = x;
        position.y = y;
    }

    /**
     * Set the position of this PointOfInterest on the map
     *
     * @param position position on the map
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public boolean isCreated() {
        return created;
    }

    @Override
    public void setRoom(Map room){
        this.room = room;
    }

    @Override
    public Map getRoom(){
        return this.room;
    }
}
