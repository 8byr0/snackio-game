package fr.esigelec.snackio.game.pois.maluses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.pois.PointOfInterest;
import fr.esigelec.snackio.game.pois.bonuses.SpeedBonus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A Paralysis malus is a malus which will make the character stop moving
 */


public class Freeze extends PointOfInterest{
    public Freeze() {
        super("poi/freeze.png");
    }

    @Override
    public void execute(Character character) {
        if (!character.isPOIActive(this)) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/frozen.wav"));

            music.play();
            int initialSpeed = character.getSpeed();

            Freeze self = this;

            int speedCoefficient = 0;
            character.setSpeed(initialSpeed * speedCoefficient);

            Timer timer = new Timer(this.durationInSeconds * 400, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    character.setSpeed(initialSpeed);
                    character.removeActivePOI(self);
                }
            });
            timer.setRepeats(false); // Only execute once
            timer.start();

            character.addActivePOI(this);
        }
        SnackioGame.getInstance().freezeTouched(this, character);
    }
}
