package fr.esigelec.snackio.game.pois.maluses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.pois.PointOfInterest;
import fr.esigelec.snackio.game.pois.bonuses.SpeedBonus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A speed malus is a malus which will reduce by 2 the speed of a Character when triggered
 */
@SuppressWarnings("Duplicates")
public class SpeedMalus extends PointOfInterest {

    @Override
    public void execute(Character character) {
        if(!character.isPOIActive(this)) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/speed_malus.wav"));

            music.play();
            int initialSpeed = character.getSpeed();

            SpeedMalus self = this;

            int speedCoefficient = 2;
            character.setSpeed(initialSpeed / speedCoefficient);

            Timer timer = new Timer(this.durationInSeconds * 1000, new ActionListener() {
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
    }
}
