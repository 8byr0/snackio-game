package fr.esigelec.snackio.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.kryonet.Listener;
import fr.esigelec.snackio.networking.Position;

import java.util.ArrayList;

/**
 * Character instance is the GUI projection of a player on the map
 */
public class Character extends Actor implements ApplicationListener {

    enum CharacterStatus {
        // STATIC
        STATIC_NORTH,
        STATIC_WEST,
        STATIC_SOUTH,
        STATIC_EAST,
        //MOVING
        MOVING_NORTH,
        MOVING_WEST,
        MOVING_SOUTH,
        MOVING_EAST
    }

    private Position position = new Position(150, 150);

    private int speed = 5;
    private int lives = 3;
    private boolean active = true;
    private boolean invincible = false;
    private Direction direction = Direction.NORTH;
    private boolean moving;
    private Animation<TextureRegion>[] animations;
    private float stateTime = 0f;

    private ArrayList<Listener.ThreadedListener> onMoveListeners = new ArrayList<>();
    private ArrayList<Listener.ThreadedListener> onLiveLostListeners = new ArrayList<>();

    private Texture characterTexture;
    private TextureRegion[][] character;
    private SpriteBatch batch;
    private Camera cam;

    public Character(Camera cam) {
        // Fill character frames array
        characterTexture = new Texture(Gdx.files.internal("sprites/inspector.png"));

        character = new TextureRegion[9][4];
        animations = new Animation[8];
        processCharacter();
        batch = new SpriteBatch();
        this.cam = cam;
//        batch.setProjectionMatrix(cam.combined);

//        batch.setProjectionMatrix(cam.combined);
    }

    public Character(CharacterState state) {

    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void processCharacter() {
        // LOAD STATIC TEXTURES
        // Static NORTH
        loadCharacter(characterTexture, 0, 1, 0, CharacterStatus.STATIC_NORTH);
        // Static WEST
        loadCharacter(characterTexture, 0, 1, 1, CharacterStatus.STATIC_WEST);
        // Static SOUTH
        loadCharacter(characterTexture, 0, 1, 2, CharacterStatus.STATIC_SOUTH);
        // Static EAST
        loadCharacter(characterTexture, 0, 1, 3, CharacterStatus.STATIC_EAST);

        // LOAD ANIMATIONS
        // Walking NORTH
        loadCharacter(characterTexture, 1, 9, 0, CharacterStatus.MOVING_NORTH);
        // Walking WEST
        loadCharacter(characterTexture, 1, 9, 1, CharacterStatus.MOVING_WEST);
        // Walking SOUTH
        loadCharacter(characterTexture, 1, 9, 2, CharacterStatus.MOVING_SOUTH);
        // Walking EAST
        loadCharacter(characterTexture, 1, 9, 3, CharacterStatus.MOVING_EAST);

    }

    private void loadCharacter(Texture spriteSheet, int firstFrameIndex, int lastFrameIndex, int rowIndex, CharacterStatus state) {
        int FRAME_COLS = 9;
        int FRAME_ROWS = 4;

        TextureRegion[][] mappedSprite = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / FRAME_COLS,
                spriteSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] animationFrames = new TextureRegion[lastFrameIndex - firstFrameIndex];
        int index = 0;

        for (int currentFrameIndex = firstFrameIndex; currentFrameIndex < lastFrameIndex; currentFrameIndex++) {
            animationFrames[index++] = mappedSprite[rowIndex][currentFrameIndex];
        }

        animations[state.ordinal()] = new Animation<>(0.025f, animationFrames);
    }

    @Override
    public void render() {
        stateTime += Gdx.graphics.getDeltaTime();
        batch.setProjectionMatrix(cam.combined);

        handleInput();
        batch.begin();
        batch.draw(getCurrentFrame(stateTime), position.x, position.y);
        batch.end();
    }

    private void handleInput() {
        this.moving = false;
        GameRenderer engine = GameRenderer.getInstance();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (!engine.isCollision(position.x - speed + 16, position.y)) {
                position.x -= speed;
                setDirection(Direction.WEST);
                setMoving(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!engine.isCollision(position.x + speed + 16, position.y)) {
                position.x += speed;

                setDirection(Direction.EAST);
                setMoving(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (!engine.isCollision(position.x + 16, position.y - speed)) {
                position.y -= speed;

                setDirection(Direction.SOUTH);
                setMoving(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (!engine.isCollision(position.x + 16, position.y + speed)) {
                position.y += speed;

                setDirection(Direction.NORTH);
                setMoving(true);
            }
        }
    }

    TextureRegion getCurrentFrame(float stateTime) {
        TextureRegion currentFrame = null;
        if (moving) {
            switch (direction) {
                case NORTH:
                    currentFrame = animations[4].getKeyFrame(stateTime, true);
                    if (null == currentFrame) {
                        System.out.println("NOTHING");
                    }
                    break;
                case WEST:
                    currentFrame = animations[5].getKeyFrame(stateTime, true);
                    break;
                case SOUTH:
                    currentFrame = animations[6].getKeyFrame(stateTime, true);
                    break;
                case EAST:
                    currentFrame = animations[7].getKeyFrame(stateTime, true);
                    break;
            }
        } else {
            switch (direction) {
                case NORTH:
                    currentFrame = animations[0].getKeyFrame(stateTime, true);
                    break;
                case WEST:
                    currentFrame = animations[1].getKeyFrame(stateTime, true);
                    break;
                case SOUTH:
                    currentFrame = animations[2].getKeyFrame(stateTime, true);
                    break;
                case EAST:
                    currentFrame = animations[3].getKeyFrame(stateTime, true);
                    break;
            }
        }

        if (null == currentFrame) {
            System.out.println("NOTHING");
        }
        return currentFrame;
    }

    /**
     * Class storing states of all capacities of a given character
     */
    private class CharacterState {
        public CharacterState() {

        }
    }

    Position getPosition() {
        return position;
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
