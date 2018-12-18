package fr.esigelec.snackio.core;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.networking.models.IRMIExecutablePlayer;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.listeners.PlayerAddedListener;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.networking.Position;

/**
 * This interface declares all the method that are allowed to be called by SnackioNetServer instance.
 */
public interface IGameEngine {
    void addPointOfInterest(iPoi poi);
    void removePointOfInterest(iPoi poi);

    IRMIExecutablePlayer getPlayer();

    void addPlayer(IRMIExecutablePlayer player) throws NoCharacterSetException, UnhandledControllerException;

    void updatePlayerPosition(int id, Position position, Direction direction) throws NoCharacterSetException;

    void addPlayerAddedListener(PlayerAddedListener listener);

    void startGame() throws GameCannotStartException;

    void updatePlayerRoom(int id, String room) throws NoCharacterSetException;

    void removePlayer(int id);
}
