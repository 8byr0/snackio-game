package fr.esigelec.snackio.game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.pois.Coin;
import fr.esigelec.snackio.game.pois.PointOfInterest;
import fr.esigelec.snackio.game.pois.bonuses.SpeedBonus;
import fr.esigelec.snackio.game.pois.iPoi;
import fr.esigelec.snackio.game.pois.maluses.SpeedMalus;

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
//        // Add some characters
//        Character inspector = CharacterFactory.getCharacter(CharacterFactory.CharacterType.INSPECTOR);
//        inspector.setPosition(250,300);
//        addCharacter(inspector);
//
//        Character nude_man = CharacterFactory.getCharacter(CharacterFactory.CharacterType.NUDE_MAN);
//        nude_man.setPosition(300,250);
//        addCharacter(nude_man);
//
//        Character bald_man = CharacterFactory.getCharacter(CharacterFactory.CharacterType.BALD_MAN);
//        bald_man.setPosition(200,200);
//        addCharacter(bald_man);

        // Add a few bonuses / maluses
        PointOfInterest speedBonus = new SpeedBonus();
        speedBonus.setPosition(800,800);
        addPointOfInterest(speedBonus);

        PointOfInterest speedMalus = new SpeedMalus();
        speedMalus.setPosition(900,900);
        addPointOfInterest(speedMalus);

        Coin testCoin = new Coin();
        testCoin.setPosition(750,200);
        addPointOfInterest(testCoin);
    }

    public void start(){
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        Lwjgl3Application app = new Lwjgl3Application(gameRenderer, config);
    }

    public void coinFound(Coin coin, Character character){
        System.out.println("YEAH, coin found!");
        gameRenderer.removePointOfInterest(coin);
    }

    public void addPointOfInterest(iPoi poi) {
        gameRenderer.addPointOfInterest(poi);
    }

    public void removePointOfInterest(iPoi poi) {
        gameRenderer.removePointOfInterest(poi);
    }

    public void addCharacter(Character character, boolean active){
        gameRenderer.addCharacter(character,active);
    }

    public void addPlayer(Player me, boolean active) {
        this.addCharacter(me.getCharacter(), active);
    }
}
