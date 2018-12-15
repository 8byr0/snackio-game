package fr.esigelec.snackio.core.models;

import fr.esigelec.snackio.game.character.motion.Direction;
import fr.esigelec.snackio.networking.Position;

public  interface AbstractPlayer {
    public abstract Position getPosition();
    public abstract void setPosition(Position position);

    Direction getDirection();
}
