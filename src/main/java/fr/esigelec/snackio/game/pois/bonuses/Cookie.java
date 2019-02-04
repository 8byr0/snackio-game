package fr.esigelec.snackio.game.pois.bonuses;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.pois.PointOfInterest;
import fr.esigelec.snackio.game.util.StaticTexture;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Cookie extends PointOfInterest {
    public Cookie() {super("poi/cookie.png");
    }

    @Override
    public void execute(Character character) {
        if (!character.isPOIActive(this)) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/speed_bonus.wav"));

            music.play();
            int initialSpeed = character.getSpeed();

            Cookie self = this;

            int speedCoefficient = 1;
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
        SnackioGame.getInstance().cookieFound(this, character);
    }
}
