package fr.esigelec.snackio.core.exceptions;

import fr.esigelec.snackio.game.character.CharacterFactory;

/**
 * Exception thrown when a given Character is unhandled by a method
 */
public class UnhandledCharacterTypeException extends Exception {
    /**
     * Class constructor
     */
    public UnhandledCharacterTypeException(){
        super("");
    }

    /**
     * Class constructor
     */
    public UnhandledCharacterTypeException(CharacterFactory.CharacterType type){
        super("The given character type (" + type.toString() + ")is unhandled and cannot be processed.");
    }

    /**
     * Class constructor with known reason
     * @param reason additional details
     */
    public UnhandledCharacterTypeException(String reason){
        super(reason);
    }
}
