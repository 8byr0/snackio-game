package fr.esigelec.snackio.game.pois;

import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.Character;

public class Coin extends AnimatedPointOfInterest {

    @Override
    public void execute(Character character) {
        SnackioGame.getInstance().coinFound(this, character);
    }
}
