package fr.esigelec.snackio.core.models;

import fr.esigelec.snackio.game.character.*;
import fr.esigelec.snackio.game.character.Character;

public class Player {
    private String name;
    private Character character;

    public Player(String name, CharacterFactory.CharacterType character, MotionController motionController){
        this.name = name;
        this.character = CharacterFactory.getCharacter(character);
        if(motionController == MotionController.KEYBOARD){
            this.character.setMotionController(new KeyboardController());
        }else if(motionController == MotionController.NETWORK){
            this.character.setMotionController(new NetworkController());
        }
    }

    public Character getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
