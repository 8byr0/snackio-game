package fr.esigelec.snackio.game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.pois.PointOfInterest;
import fr.esigelec.snackio.game.pois.bonuses.SpeedBonus;

/**
 * High level Game class.
 */
public class SnackioGame  {
    private static SnackioGame instance = null;
    private GameRenderer gameRenderer = GameRenderer.getInstance();

    public static SnackioGame getInstance(){
        if(null == instance){
            instance = new SnackioGame();
        }
        return instance;
    }

    private SnackioGame(){
        Character inspector = CharacterFactory.getCharacter(CharacterFactory.CharacterType.INSPECTOR);
        inspector.setPosition(250,300);
        addCharacter(inspector);
        Character nude_man = CharacterFactory.getCharacter(CharacterFactory.CharacterType.NUDE_MAN);
        nude_man.setPosition(300,250);
        addCharacter(nude_man);
        Character bald_man = CharacterFactory.getCharacter(CharacterFactory.CharacterType.BALD_MAN);
        bald_man.setPosition(200,200);
        addCharacter(bald_man);

        PointOfInterest speedBonus = new SpeedBonus();
        addPointOfInterest(speedBonus);

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        Lwjgl3Application app = new Lwjgl3Application(gameRenderer, config);


    }

    public void addPointOfInterest(PointOfInterest poi) {
        gameRenderer.addPointOfInterest(poi);
    }

    public void addCharacter(Character character){
        gameRenderer.addCharacter(character);
    }
}
