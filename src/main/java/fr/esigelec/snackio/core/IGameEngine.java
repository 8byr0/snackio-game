package fr.esigelec.snackio.core;

import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.listeners.PlayerAddedListener;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.networking.Position;

public interface IGameEngine {
    void addPointOfInterest(iPoi poi);
    void removePointOfInterest(iPoi poi);

    Player getPlayer();

    void addPlayer(Player player) throws NoCharacterSetException, UnhandledControllerException;

    void updatePlayerPosition(int id, Position position, Direction direction) throws NoCharacterSetException;

    void addPlayerAddedListener(PlayerAddedListener listener);

    void startGame() throws GameCannotStartException;
}
