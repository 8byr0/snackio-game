package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.Character;

/**
 * Coin graphical behavior
 */
public class Coin extends AnimatedPointOfInterest {
    /**
     * Method triggered when this coin is fetched by a Character
     * @param character Character who fetched the coin
     */
    @Override
    public void execute(Character character) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/coin.wav"));
        music.play();

        // TODO find another way to execute this (should be ok with listeners)
        SnackioGame.getInstance().coinFound(this, character);
    }
}
