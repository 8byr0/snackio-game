package fr.esigelec.snackio.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import fr.esigelec.snackio.game.GameRenderer;
import fr.esigelec.snackio.game.character.listeners.MoveListener;
import fr.esigelec.snackio.game.character.listeners.RoomChangeListener;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.motion.iCharacterController;
import fr.esigelec.snackio.game.character.texture.AnimatedCharacterSkin;
import fr.esigelec.snackio.game.pois.PointOfInterest;
import fr.esigelec.snackio.networking.Position;

import java.util.ArrayList;

/**
 * Character instance is the GUI projection of a player on the map.
 */
public class Character extends MapObject {
    AnimatedCharacterSkin weapon;

    public void addWeapon(AnimatedCharacterSkin weapon) {
        this.weapon = weapon;
    }

    public enum CharacterStatus {
        // STATIC
        STATIC_NORTH,
        STATIC_WEST,
        STATIC_SOUTH,
        STATIC_EAST,
        //MOVING
        MOVING_NORTH,
        MOVING_WEST,
        MOVING_SOUTH,
        MOVING_EAST,
        // ATTACKING
        ATTACKING_NORTH,
        ATTACKING_EAST,
        ATTACKING_WEST,
        ATTACKING_SOUTH
    }

    public enum StepSound {
        LEFT,
        RIGHT
    }

    // State attributes
    private float stateTime = 0f;
    private boolean created = false;
    private ArrayList<PointOfInterest> activePointsOfInterest;
    private String room;

    // Motion control
    private int speed = 5;
    private boolean moving = false;
    private boolean attacking = false;
    private Position position = new Position(150, 150);
    private iCharacterController motionController;
    private Direction direction = Direction.NORTH;

    // Sound attributes
    private StepSound lastlyPlayed = StepSound.LEFT;
    private Music leftStepSound;
    private Music rightStepSound;

    // Listeners
    private ArrayList<MoveListener> moveListeners = new ArrayList<>();
    private ArrayList<RoomChangeListener> roomChangeListeners = new ArrayList<>();

    // 2D rendering
    private Camera cam;
    private SpriteBatch batch;

    private AnimatedCharacterSkin walkingSkin;
    private AnimatedCharacterSkin attackingSkin;

    private ShapeRenderer shapeRenderer;

    /**
     * No-args constructor for KyroNet
     */
    public Character() {

    }

    /**
     * Default Character constructor
     * It is package-private because it should only be called by the CharacterFactory
     */
    Character(AnimatedCharacterSkin skin, AnimatedCharacterSkin attackingSkin) {
        activePointsOfInterest = new ArrayList<>();
        this.walkingSkin = skin;
        this.attackingSkin = attackingSkin;
    }

    /**
     * Tells if the character has already been created in the game engine
     *
     * @return true or false
     */
    public boolean created() {
        return created;
    }

    /**
     * Set the direction of the character
     *
     * @param direction One of Direction.class (NORTH, SOUTH, EAST, WEST)
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Get the actual direction of the character
     *
     * @return {Direction} one of NORTH, SOUTH, EAST, WEST
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Set the moving state of the character
     * This method also triggers all listeners that asked to be notified.
     * When a character is in moving state, his projection will be keyframe animation
     *
     * @param moving boolean
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
        if (moving) {
            triggerMoveListeners();
            if (!leftStepSound.isPlaying() && !rightStepSound.isPlaying()) {
                if (lastlyPlayed == StepSound.LEFT) {
                    rightStepSound.play();
                    lastlyPlayed = StepSound.RIGHT;
                } else {
                    leftStepSound.play();
                    lastlyPlayed = StepSound.LEFT;
                }
            }
        }
    }

    /**
     * Set the moving state of the character
     * This method also triggers all listeners that asked to be notified.
     * When a character is in moving state, his projection will be keyframe animation
     *
     * @param attacking boolean
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    /**
     * Trigger move listeners methods
     */
    private void triggerMoveListeners() {
        for (MoveListener listener : moveListeners) {
            listener.movePerformed(position);
        }
    }

    /**
     * Trigger room change listeners methods
     */
    private void triggerRoomChangeListeners() {
        for (RoomChangeListener listener : roomChangeListeners) {
            listener.movePerformed(this.getRoom());
        }
    }

    /**
     * Implementation of ApplicationListener's interface.
     * This method is called once when libgdx engine is ready and running.
     */
    public void create() {
        leftStepSound = Gdx.audio.newMusic(Gdx.files.internal("sound/step_left.ogg"));
        rightStepSound = Gdx.audio.newMusic(Gdx.files.internal("sound/step_right.ogg"));
        batch = new SpriteBatch();
        this.cam = GameRenderer.getInstance().getCamera();
        shapeRenderer = new ShapeRenderer();
        walkingSkin.create();
        attackingSkin.create();
        weapon.create();
        created = true;
    }

    /**
     * Implementation of ApplicationListener's method.
     * The render() method is called continuously by libgdx to perform
     * rendering.
     */
    public void render(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        batch.setProjectionMatrix(cam.combined);
        handleMotionController();
        if (isAttacking()) {
            batch.draw(attackingSkin.getCurrentFrame(direction, moving, attacking), position.x, position.y);
        } else {
            batch.draw(walkingSkin.getCurrentFrame(direction, moving, attacking), position.x, position.y);
        }
        batch.draw(weapon.getCurrentFrame(direction, moving, attacking), position.x, position.y);

        weapon.render();
        walkingSkin.render();
        attackingSkin.render();
    }

    /**
     * Call motion controller's execute method to handle input.
     */
    private void handleMotionController() {
        if (null != motionController) {
            motionController.execute(this);
        }
    }

    /**
     * Set the position of the character on the map
     *
     * @param x x position
     * @param y y position
     */
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    /**
     * Set the position of the character on the map
     *
     * @param position Position of the character
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Get the position of a character on the map
     *
     * @return Position (x,y)
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Dispose all elements created in create() method
     */
    public void dispose() {
        batch.dispose();
        walkingSkin.dispose();
        leftStepSound.dispose();
        rightStepSound.dispose();
    }

    /**
     * Returns wether a given POI is active on this character or not.
     *
     * @param poi reference to PointOfInterest
     * @return true if active, false otherwise
     */
    public boolean isPOIActive(PointOfInterest poi) {
        return this.activePointsOfInterest.contains(poi);
    }

    /**
     * Add a poi to this character
     *
     * @param poi the PointOfInterest to add
     */
    public void addActivePOI(PointOfInterest poi) {
        this.activePointsOfInterest.add(poi);
    }

    /**
     * Remove an active poi from this character
     * (Usually called in poi's post-execution callback)
     *
     * @param poi the PointOfInterest to remove
     */
    public void removeActivePOI(PointOfInterest poi) {
        if (isPOIActive(poi)) {
            this.activePointsOfInterest.remove(poi);
        }
    }

    /**
     * Get the speed of this character
     *
     * @return {int} the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Set the motion controller of this character that will be called during rendering
     *
     * @param motionController a concrete implementation of iCharacterController interface
     */
    public void setMotionController(iCharacterController motionController) {
        this.motionController = motionController;
    }

    /**
     * Get the geometrical projection of this character
     *
     * @return Rectangle
     */
    public Rectangle getActualProjection() {
        return new Rectangle(position.x + 16, position.y, 32, 43);
    }

    /**
     * Get the geometrical projection of this character's feets
     * Feets projection is used to detect collisions with environment
     *
     * @return Rectangle 32*16
     */
    public Rectangle getFeetsProjection(float x, float y) {
        return new Rectangle(x, y, 32, 16);
    }

    /**
     * Get the geometrical projection of this character
     * TODO move this in a dedicated geometrical class
     *
     * @return Rectangle
     */
    public Rectangle getFullProjection(float x, float y) {
        return new Rectangle(x, y, 32, 43);
    }

    /**
     * Set the speed of this character
     *
     * @param speed int value to attribute
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Add a move listener to this character
     * MoveListener will be triggered each time the character moves
     *
     * @param moveListener Reference to MoveListener implementation
     */
    public void addMoveListener(MoveListener moveListener) {
        moveListeners.add(moveListener);
    }

    /**
     * Add a RoomChangeListener listener to this character
     * RoomChangeListener will be triggered each time the character changes room
     *
     * @param listener Reference to RoomChangeListener implementation
     */
    public void addRoomChangeListener(RoomChangeListener listener) {
        roomChangeListeners.add(listener);
    }

    /**
     * Returns if the character is moving or not
     *
     * @return true if moving, false otherwise
     */
    public boolean isMoving() {
        return moving;
    }

    /**
     * Returns if the character is moving or not
     *
     * @return true if moving, false otherwise
     */
    public boolean isAttacking() {
        return attacking;
    }

    /**
     * Set the room of this character
     *
     * @param roomName the room name
     */
    public void setRoom(String roomName) {
        this.room = roomName;
        this.triggerRoomChangeListeners();
    }

    /**
     * Get the actual room of this Character
     *
     * @return the room name
     */
    public String getRoom() {
        return this.room;
    }
}
