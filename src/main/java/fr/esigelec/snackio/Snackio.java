package fr.esigelec.snackio;

import fr.esigelec.snackio.core.models.Player;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.character.MotionController;
import fr.esigelec.snackio.networking.SnackioServer;
import fr.esigelec.snackio.ui.MenuController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class Snackio extends Application {
    private static final Logger logger = LogManager.getLogger(Snackio.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Snackio Game started");

        logger.info("Initialisation started...");
        logger.info("Fetching menu controller");
//        MenuController controller = MenuController.getInstance(primaryStage);

        logger.info("Opening main menu");
//        controller.openMenu(MenuController.Menus.MAIN_MENU);

        logger.info("...Initialisation finished");

        SnackioGame game = SnackioGame.getInstance();
        Player me = new Player("Hugues", CharacterFactory.CharacterType.BALD_MAN, MotionController.KEYBOARD);
        game.addPlayer(me, true);
        game.start();
    }

}
