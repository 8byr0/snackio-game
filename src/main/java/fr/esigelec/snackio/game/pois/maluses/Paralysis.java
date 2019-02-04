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
 * A Paralysis malus is a malus which will make the character move to the opposite direction
 */

public class Paralysis extends PointOfInterest{

    public Paralysis() {
        super("poi/paralysis.png");
    }

    @Override
    public void execute(Character character) {
        if (!character.isPOIActive(this)) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/speed_malus.wav"));

            music.play();
            int initialSpeed = character.getSpeed();

            Paralysis self = this;

            int speedCoefficient = -1;
            character.setSpeed(initialSpeed * speedCoefficient);

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
