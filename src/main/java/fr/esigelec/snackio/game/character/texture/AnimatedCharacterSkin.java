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
    private TextureMapping animationMapping;
    private int COLS;
    private int ROWS;

    // State attributes
    private float stateTime = 0f;

    // 2D rendering
    private String pathToSprite;
    private Texture characterTexture;
    private float frameDuration;
    private HashMap<Character.CharacterStatus, Animation> animationsHashMap;

    /**
     * No-args constructor for KyroNet
     */
    public AnimatedCharacterSkin() {

    }

    /**
     * Class constructor to instantiate an AnimatedCharacterSkin
     * TODO pass a mapped config of texture to define which frame belongs to which direction / animation
     *
     * @param pathToTexture Absolute path to texture img file (e.g. `sprites/character.png`)
     * @param cols          number of columns in the texture
     * @param rows          number of rows in the texture
     */
    public AnimatedCharacterSkin(String pathToTexture, int cols, int rows, float frameDuration) {
        this.pathToSprite = pathToTexture;
        this.COLS = cols;
        this.ROWS = rows;
        animationsHashMap = new HashMap<>();
        this.frameDuration = frameDuration;
    }

    /**
     * Class constructor to instantiate an AnimatedCharacterSkin
     * TODO pass a mapped config of texture to define which frame belongs to which direction / animation
     *
     * @param mapping       Absolute path to texture img file (e.g. `sprites/character.png`)
     * @param frameDuration number of columns in the texture
     */
    public AnimatedCharacterSkin(TextureMapping mapping, float frameDuration) {
        this.animationMapping = mapping;
        this.animationsHashMap = new HashMap<>();
        this.frameDuration = frameDuration;
    }

    /**
     * Method called during GUI creation (called once)
     */
    @Override
    public void create() {
        characterTexture = new Texture(Gdx.files.internal(animationMapping.pathToTexture));
        loadAllAnimations();

    }

    /**
     * Method called during GUI render (called continuously)
     */
    @Override
    public void render() {
        stateTime += Gdx.graphics.getDeltaTime();
    }


    /**
     * Load character views and animated view from given texture
     */
    private void loadAllAnimations() {
        for (Character.CharacterStatus status : Character.CharacterStatus.values()) {
            if (status == Character.CharacterStatus.MOVING_EAST ||
                    status == Character.CharacterStatus.MOVING_WEST ||
                    status == Character.CharacterStatus.MOVING_SOUTH ||
                    status == Character.CharacterStatus.MOVING_NORTH) {
                this.animationsHashMap.put(status, getAnimation(characterTexture, animationMapping.movingMapping.firstFrameIndex, animationMapping.movingMapping.lastFrameIndex, animationMapping.getIndexOfStatus(status)));
            } else if (status == Character.CharacterStatus.ATTACKING_EAST ||
                    status == Character.CharacterStatus.ATTACKING_NORTH ||
                    status == Character.CharacterStatus.ATTACKING_SOUTH ||
                    status == Character.CharacterStatus.ATTACKING_WEST) {
                this.animationsHashMap.put(status, getAnimation(characterTexture, animationMapping.attackingMapping.firstFrameIndex, animationMapping.attackingMapping.lastFrameIndex, animationMapping.getIndexOfStatus(status)));
            } else {
                this.animationsHashMap.put(status, getAnimation(characterTexture, animationMapping.staticMapping.firstFrameIndex, animationMapping.staticMapping.lastFrameIndex, animationMapping.getIndexOfStatus(status)));

            }
        }
//        // LOAD STATIC TEXTURES
//        // Static NORTH
//        this.animationsHashMap.put(Character.CharacterStatus.STATIC_NORTH, getAnimation(characterTexture, animationMapping.staticMapping.firstFrameIndex, animationMapping.staticMapping.lastFrameIndex, animationMapping.repartition.northIndex));
//        // Static WEST
//        this.animationsHashMap.put(Character.CharacterStatus.STATIC_WEST, getAnimation(characterTexture, 0, 1, 1));
//        // Static SOUTH
//        this.animationsHashMap.put(Character.CharacterStatus.STATIC_SOUTH, getAnimation(characterTexture, 0, 1, 2));
//        // Static EAST
//        this.animationsHashMap.put(Character.CharacterStatus.STATIC_EAST, getAnimation(characterTexture, 0, 1, 3));
//
//        // LOAD ANIMATIONS
//        // Walking NORTH
//        this.animationsHashMap.put(Character.CharacterStatus.MOVING_NORTH, getAnimation(characterTexture, 1, COLS, 0));
//        // Walking WEST
//        this.animationsHashMap.put(Character.CharacterStatus.MOVING_WEST, getAnimation(characterTexture, 1, COLS, 1));
//        // Walking SOUTH
//        this.animationsHashMap.put(Character.CharacterStatus.MOVING_SOUTH, getAnimation(characterTexture, 1, COLS, 2));
//        // Walking EAST
//        this.animationsHashMap.put(Character.CharacterStatus.MOVING_EAST, getAnimation(characterTexture, 1, COLS, 3));
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
                spriteSheet.getWidth() / animationMapping.cols,
                spriteSheet.getHeight() / animationMapping.rows);

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
    public TextureRegion getCurrentFrame(Direction direction, boolean moving, boolean attacking) {
        TextureRegion currentFrame = null;
        if (moving && !attacking) {
            switch (direction) {
                case NORTH:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.MOVING_NORTH).getKeyFrame(stateTime, true);
                    break;
                case WEST:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.MOVING_WEST).getKeyFrame(stateTime, true);
                    break;
                case SOUTH:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.MOVING_SOUTH).getKeyFrame(stateTime, true);
                    break;
                case EAST:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.MOVING_EAST).getKeyFrame(stateTime, true);
                    break;
            }
        } else if (attacking){
            switch (direction) {
                case NORTH:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.ATTACKING_NORTH).getKeyFrame(stateTime, true);
                    break;
                case WEST:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.ATTACKING_WEST).getKeyFrame(stateTime, true);
                    break;
                case SOUTH:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.ATTACKING_SOUTH).getKeyFrame(stateTime, true);
                    break;
                case EAST:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.ATTACKING_EAST).getKeyFrame(stateTime, true);
                    break;
            }
        }
        else {
            switch (direction) {
                case NORTH:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.STATIC_NORTH).getKeyFrame(stateTime, true);
                    break;
                case WEST:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.STATIC_WEST).getKeyFrame(stateTime, true);
                    break;
                case SOUTH:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.STATIC_SOUTH).getKeyFrame(stateTime, true);
                    break;
                case EAST:
                    currentFrame = (TextureRegion) animationsHashMap.get(Character.CharacterStatus.STATIC_EAST).getKeyFrame(stateTime, true);
                    break;
            }
        }
        return currentFrame;

        }

    /**
     * Set the duration of each frame
     *
     * @param duration the duration // TODO find which unit it is
     */
    public void setFrameDuration(float duration) {
        this.frameDuration = duration;
    }
}
