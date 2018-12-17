package fr.esigelec.snackio.core.exceptions;

import fr.esigelec.snackio.game.character.motion.IMotionController;

/**
 * Exception thrown when a given controller is not available
 */
public class UnhandledControllerException extends Exception {
    /**
     * Class constructor
     */
    public UnhandledControllerException(){
        super("The given controller is unhandled and cannot be set for this Player / Character.");
    }

    /**
     * Class constructor
     */
    public UnhandledControllerException(IMotionController controller){
        super("The given controller (" + controller.toString() + ")is unhandled and cannot be set for this Player / Character.");
    }

    /**
     * Class constructor with known reason
     * @param reason additional details
     */
    public UnhandledControllerException(String reason){
        super(reason);
    }
}
