package fr.esigelec.snackio.core.models;

import fr.esigelec.snackio.game.character.*;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.listeners.MoveListener;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.motion.KeyboardController;
import fr.esigelec.snackio.game.character.motion.MotionController;
import fr.esigelec.snackio.game.character.motion.NetworkController;
import fr.esigelec.snackio.networking.Position;

/**
 * A Player has to be created for each physical person joining the Game.
 * It is linked to a Character, which is its graphical projection on the map
 * of the Game.
 * {@code
 * // Create a Player named 'Hugues' whose Character is GOLDEN_KNIGHT
 * Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.GOLDEN_KNIGHT);
 *
 * //
 * }
 */
public class Player {
    private int id;
    private Character character;

    /**
     * Class constructor
     */
    public Player(){

    }

    /**
     * Class constructor specifying name and Character
     * @param name name of the player
     * @param character character of the player
     */
    public Player(String name, CharacterFactory.CharacterType character){
        this.character = CharacterFactory.getCharacter(character);

    }

    public void setMotionController(MotionController controller){
        if(controller == MotionController.KEYBOARD){
            this.character.setMotionController(new KeyboardController());
        }else if(controller == MotionController.NETWORK){
            this.character.setMotionController(new NetworkController());
        }
    }

    public Character getCharacter() {
        return character;
    }

    public void addMoveListener(final Runnable listener) {
        character.addMoveListener(new MoveListener() {
            @Override
            public void movePerformed (Position pos) {
                listener.run();
            }
        });
    }

    public Position getPosition() {
        return this.character.getPosition();
    }

    public void setPosition(Position position) {
        this.character.setPosition(position);
    }

    public Direction getDirection() {
        return this.character.getDirection();
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setDirection(Direction direction) {
        this.character.setDirection(direction);
    }

    public void setMoving(boolean moving) {
        this.character.setMoving(moving);
    }
}
