package fr.esigelec.snackio.core;

import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.networking.models.IRMIExecutablePlayer;
import fr.esigelec.snackio.game.character.*;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.motion.KeyboardController;
import fr.esigelec.snackio.game.character.motion.IMotionController;
import fr.esigelec.snackio.game.character.motion.NetworkController;
import fr.esigelec.snackio.networking.Position;

/**
 * A Player has to be created for each physical person joining the Game.
 * It is linked to a Character, which is its graphical projection on the map
 * of the Game.
 * {@code
 * // Create a Game
 * SnackioGame game = SnackioGame.getInstance();
 * <p>
 * // Create a Player named 'Hugues' whose Character is GOLDEN_KNIGHT
 * Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.GOLDEN_KNIGHT);
 * <p>
 * // Instantiate Network game engine to control gameplay
 * AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer);
 * // Instantiate a NetClient to exchange with client
 * SnackioNetClient cli = new SnackioNetClient(engine);
 * <p>
 * // Start the game with my player
 * game.start(myPlayer);
 * }
 */
public class Player implements IRMIExecutablePlayer {
    private int id;
    private Character character;
    private int lives = 3;

    /**
     * Class constructor
     */
    public Player() {

    }

    public int getLives() {
        return lives;
    }

    /**
     * Class constructor specifying name and Character
     * Character must be one of {@link CharacterFactory.CharacterType}
     *
     * @param name      name of the player
     * @param character character of the player
     * @throws UnhandledCharacterTypeException When the given Character cannot be processed by the factory
     */
    public Player(String name, CharacterFactory.CharacterType character) throws UnhandledCharacterTypeException {
        this.character = CharacterFactory.getCharacter(character);
    }

    /**
     * Get the ID of this player.
     *
     * @return int identifier
     */
    public int getID() {
        return id;
    }

    /**
     * Set the ID of this player
     *
     * @param id new ID of the player
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Set the motion controller that will be used by the Player to control his Character
     * The controller must be one of {@link IMotionController}
     *
     * @param controller The controller triggered during game rendering
     * @throws UnhandledControllerException Thrown when the controller has no candidate in this method.
     */
    public void setMotionController(IMotionController controller) throws UnhandledControllerException {
        if (controller == IMotionController.KEYBOARD) {
            this.character.setMotionController(new KeyboardController());
        } else if (controller == IMotionController.NETWORK) {
            this.character.setMotionController(new NetworkController());
        } else {
            throw new UnhandledControllerException(controller);
        }
    }

    /**
     * Get the Player's Character.
     *
     * @return Character used by the Player
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public Character getCharacter() throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        return character;
    }

    /**
     * Add a listener to the player's character movements.
     * This listener will be triggered each time the position of the Player's Character is updated
     *
     * @param listener the MoveListener to be triggered
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public void addMoveListener(final Runnable listener) throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        character.addMoveListener((position) -> listener.run());
    }

    /**
     * Add a listener to the player's character room state.
     * This listener will be triggered each time the Player's Character changes rooms
     *
     * @param listener the RoomChangeListener to be triggered
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public void addRoomChangeListener(final Runnable listener) throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        character.addRoomChangeListener((room) -> listener.run());
    }

    /**
     * Get the position of the Player's Character
     *
     * @return the position (see {@link Position} of the Character on the Game's map
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public Position getPosition() throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        return this.character.getPosition();
    }

    /**
     * Set the position of the Player's Character on the map
     *
     * @param position position (see {@link Position} of the Character on the Game's map
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public void setPosition(Position position) throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        this.character.setPosition(position);
    }

    /**
     * Get the direction of the Player's Character on the map.
     * see {@link Direction} for possible values
     *
     * @return direction one of NORTH, SOUTH, EAST, WEST
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public Direction getDirection() throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        return this.character.getDirection();
    }

    /**
     * Set the direction of the Player's Character on the map.
     * see {@link Direction} for possible values
     *
     * @param direction one of NORTH, SOUTH, EAST, WEST
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public void setDirection(Direction direction) throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        this.character.setDirection(direction);
    }

    /**
     * Set if the Player's Character is moving or not
     * When moving is set to true, the Character's skin will load walking animations
     *
     * @param moving true to make the Character move, false otherwise
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public void setMoving(boolean moving) throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        this.character.setMoving(moving);
    }

    /**
     * Get the actual room in which the player is (as String)
     * @return String the name of the room
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public String getRoom() throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        return this.character.getRoom();
    }

    /**
     * Set the actual room in which the player is (as String)
     * @param room the name of the room
     * @throws NoCharacterSetException Exception thrown when the Character of this player is null
     */
    public void setRoom(String room) throws NoCharacterSetException {
        if (null == character) {
            throw new NoCharacterSetException();
        }
        this.character.setRoom(room);
    }
}
