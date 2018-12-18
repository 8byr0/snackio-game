package fr.esigelec.snackio.game.state;

import fr.esigelec.snackio.core.GameMode;
import fr.esigelec.snackio.game.map.MapFactory;

public class MultiplayerGameState extends AbstractGameState {
    public MultiplayerGameState(MapFactory.MapType type) {
        super(type, GameMode.FIGHT_TO_DEATH);

    }
}
