package fr.esigelec.snackio.game.pois;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.Character;

// TODO
public class RandomItem extends PointOfInterest {
    public static byte BONUS_TYPE_SPPEDUP = 0;
    public static byte BONUS_TYPE_SPEEDDOWN = 1;

    public RandomItem() {super("poi/speed_bonus.png");}



    /**
     * Method triggered when the RandomItem is touched by a Character
     * @param character Character who meet the RandomItem
     */
    @Override
    public void execute(Character character) {


        // TODO find another way to execute this (should be ok with listeners)
        SnackioGame.getInstance().getRandomItem(this, character);
    }
}
