package fr.esigelec.snackio.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryonet.Listener;

import java.util.ArrayList;

/**
 * Character instance is the GUI projection of a player on the map
 */
public class Character {
    private int speed = 1;
    private int lives = 3;
    private boolean active = true;
    private boolean invincible = false;
    private Direction direction = Direction.NORTH;

    private ArrayList<Listener.ThreadedListener> onMoveListeners = new ArrayList<>();
    private ArrayList<Listener.ThreadedListener> onLiveLostListeners = new ArrayList<>();


    public Character(CharacterState state) {

    }

    /**
     * Class storing states of all capacities of a given character
     */
    private class CharacterState {
        public CharacterState() {

        }
    }


    private static final int FRAME_COLS = 9, FRAME_ROWS = 4;

    private Texture characterSheet;
    private SpriteBatch spriteBatch;
    private Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    float stateTime;

    public Character() {
        characterSheet = new Texture(Gdx.files.internal("sprites/character.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(characterSheet,
                characterSheet.getWidth() / FRAME_COLS,
                characterSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
//        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, false);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50); // Draw current frame at (50, 50)
        spriteBatch.end();
    }


}
