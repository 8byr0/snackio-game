package fr.esigelec.snackio.core.exceptions;

/**
 * Exception thrown when a Player has no Character set
 */
public class NoCharacterSetException extends Exception {
    /**
     * Class constructor
     */
    public NoCharacterSetException(){
        super("Player has no Character set.");
    }

    /**
     * Class constructor with known reason
     * @param reason additional details
     */
    public NoCharacterSetException(String reason){
        super(reason);
    }
}
