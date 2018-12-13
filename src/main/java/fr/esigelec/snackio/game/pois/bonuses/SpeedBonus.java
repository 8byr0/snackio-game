package fr.esigelec.snackio.game.pois.bonuses;

import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.pois.PointOfInterest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("Duplicates")
public class SpeedBonus extends PointOfInterest {

    @Override
    public void execute(Character character) {
        if(!character.isPOIActive(this)) {
            int initialSpeed = character.getSpeed();

            SpeedBonus self = this;

            int speedCoefficient = 2;
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
