package fr.esigelec.snackio.core.models;

import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.game.character.listeners.PlayerAddedListener;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.networking.Position;

public interface IGameEngine {
    void addPointOfInterest(iPoi poi);
    void removePointOfInterest(iPoi poi);

    Player getPlayer();

    void addPlayer(Player player);

    void updatePlayerPosition(int id, Position position, Direction direction);

    void addPlayerAddedListener(PlayerAddedListener listener);
}
