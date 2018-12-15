package fr.esigelec.snackio.game.character.texture;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.motion.Direction;

import java.util.HashMap;

/**
 * Class loading a spriteSheet image and providing Animations
 */
public class AnimatedCharacterSkin extends ApplicationAdapter {
    private int COLS;
    private int ROWS;

    // State attributes
    private float stateTime = 0f;

    // 2D rendering
    private String pathToSprite;
    private Texture characterTexture;
    private float frameDuration = 0.025f;
    private HashMap<Character.CharacterStatus, Animation> animationsHashMap;

    /**
     * No-args constructor for KyroNet
     */
    public AnimatedCharacterSkin() {

    }

    public AnimatedCharacterSkin(String pathToTexture, int cols, int rows) {
        this.pathToSprite = pathToTexture;
        this.COLS = cols;
        this.ROWS = rows;
        animationsHashMap = new HashMap<>();
    }

    @Override
    public void create() {
        characterTexture = new Texture(Gdx.files.internal(pathToSprite));
        loadAllAnimations();

    }

    @Override
    public void render() {
        stateTime += Gdx.graphics.getDeltaTime();
    }


    /**
     * Load character views and animated view from given texture
     */
    private void loadAllAnimations() {
        // LOAD STATIC TEXTURES
        // Static NORTH
        this.animationsHashMap.put(Character.CharacterStatus.STATIC_NORTH, getAnimation(characterTexture, 0,1,0));
        // Static WEST
        this.animationsHashMap.put(Character.CharacterStatus.STATIC_WEST, getAnimation(characterTexture, 0,1,1));
        // Static SOUTH
        this.animationsHashMap.put(Character.CharacterStatus.STATIC_SOUTH, getAnimation(characterTexture, 0,1,2));
        // Static EAST
        this.animationsHashMap.put(Character.CharacterStatus.STATIC_EAST, getAnimation(characterTexture, 0,1,3));

        // LOAD ANIMATIONS
        // Walking NORTH
        this.animationsHashMap.put(Character.CharacterStatus.MOVING_NORTH, getAnimation(characterTexture, 1,9,0));
        // Walking WEST
        this.animationsHashMap.put(Character.CharacterStatus.MOVING_WEST, getAnimation(characterTexture, 1,9,1));
        // Walking SOUTH
        this.animationsHashMap.put(Character.CharacterStatus.MOVING_SOUTH, getAnimation(characterTexture, 1,9,2));
        // Walking EAST
        this.animationsHashMap.put(Character.CharacterStatus.MOVING_EAST, getAnimation(characterTexture, 1,9,3));
    }

    /**
     * Load a defined view from given texture into animation
     *
     * @param spriteSheet     Texture with sprites
     * @param firstFrameIndex Index of the first keyframe
     * @param lastFrameIndex  index of the last keyframe
     * @param rowIndex        Row number
     */
    private Animation getAnimation(Texture spriteSheet, int firstFrameIndex, int lastFrameIndex, int rowIndex) {
        TextureRegion[][] mappedSprite = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / COLS,
                spriteSheet.getHeight() / ROWS);

        TextureRegion[] animationFrames = new TextureRegion[lastFrameIndex - firstFrameIndex];
        int index = 0;

        for (int currentFrameIndex = firstFrameIndex; currentFrameIndex < lastFrameIndex; currentFrameIndex++) {
            animationFrames[index++] = mappedSprite[rowIndex][currentFrameIndex];
        }

        return new Animation<>(frameDuration, animationFrames);
    }

    /**
     * Get the frame of an animation
     *
     * @return animation
     */
    public TextureRegion getCurrentFrame(Direction direction, boolean moving) {
        TextureRegion currentFrame = null;
        if (moving) {
            switch (direction) {
                case NORTH:
                    currentFrame = (TextureRegion)animationsHashMap.get(Character.CharacterStatus.MOVING_NORTH).getKeyFrame(stateTime, true);
                    break;
                case WEST:
                    currentFrame = (TextureRegion)animationsHashMap.get(Character.CharacterStatus.MOVING_WEST).getKeyFrame(stateTime, true);
                    break;
                case SOUTH:
                    currentFrame = (TextureRegion)animationsHashMap.get(Character.CharacterStatus.MOVING_SOUTH).getKeyFrame(stateTime, true);
                    break;
                case EAST:
                    currentFrame = (TextureRegion)animationsHashMap.get(Character.CharacterStatus.MOVING_EAST).getKeyFrame(stateTime, true);
                    break;
            }
        } else {
            switch (direction) {
                case NORTH:
                    currentFrame = (TextureRegion)animationsHashMap.get(Character.CharacterStatus.STATIC_NORTH).getKeyFrame(stateTime, true);
                    break;
                case WEST:
                    currentFrame = (TextureRegion)animationsHashMap.get(Character.CharacterStatus.STATIC_WEST).getKeyFrame(stateTime, true);
                    break;
                case SOUTH:
                    currentFrame = (TextureRegion)animationsHashMap.get(Character.CharacterStatus.STATIC_SOUTH).getKeyFrame(stateTime, true);
                    break;
                case EAST:
                    currentFrame = (TextureRegion)animationsHashMap.get(Character.CharacterStatus.STATIC_EAST).getKeyFrame(stateTime, true);
                    break;
            }
        }
        return currentFrame;
    }


    public void setFrameDuration(float duration) {
        this.frameDuration = duration;
    }
}
