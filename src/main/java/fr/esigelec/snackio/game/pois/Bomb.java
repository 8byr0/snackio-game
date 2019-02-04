package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.Character;


public class Bomb extends AnimatedPointOfInterest {

    public Bomb(){
        super("poi/bomb/bomb", ".gif",2);

    }

    /**
     * Method triggered when the bomb is touched by a Character
     * @param character Character who meet the bomb
     */
    @Override
    public void execute(Character character) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/explosion.ogg"));
        music.play();

        // TODO find another way to execute this (should be ok with listeners)
        SnackioGame.getInstance().bombTouched(this, character);
    }
}
