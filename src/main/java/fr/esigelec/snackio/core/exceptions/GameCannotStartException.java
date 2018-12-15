package fr.esigelec.snackio.core.exceptions;

/**
 * Exception thrown when the Game cannot start.
 */
public class GameCannotStartException extends Exception {
    /**
     * Class constructor
     */
    public GameCannotStartException(){

    }

    /**
     * Class constructor with known reason
     * @param reason the reason why the game did not start
     */
    public GameCannotStartException(String reason){
        super(reason);
    }
}
