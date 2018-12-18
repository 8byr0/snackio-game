package fr.esigelec.snackio.game.state;

import fr.esigelec.snackio.core.GameMode;
import fr.esigelec.snackio.game.map.MapFactory;

public abstract class AbstractGameState {
    protected MapFactory.MapType mapType;
    protected GameMode mode;

    public AbstractGameState(MapFactory.MapType type, GameMode mode){
        this.mapType = type;
        this.mode = mode;
    }

    public MapFactory.MapType getMapType() {
        return mapType;
    }

    public void setMapType(MapFactory.MapType mapType) {
        this.mapType = mapType;
    }

    public GameMode getGameMode() {
        return mode;
    }

    public void setGameMode(GameMode mode) {
        this.mode = mode;
    }
}
