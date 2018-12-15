package fr.esigelec.snackio.core.models;

import fr.esigelec.snackio.game.character.*;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.listeners.MoveListener;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.motion.KeyboardController;
import fr.esigelec.snackio.game.character.motion.MotionController;
import fr.esigelec.snackio.game.character.motion.NetworkController;
import fr.esigelec.snackio.networking.Position;

public class Player implements AbstractPlayer {
    private int id;
    private Character character;

    public Player(){

    }

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

    @Override
    public Position getPosition() {
        return this.character.getPosition();
    }

    @Override
    public void setPosition(Position position) {
        this.character.setPosition(position);
    }

    @Override
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
