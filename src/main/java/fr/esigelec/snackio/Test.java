package fr.esigelec.snackio;

import fr.esigelec.snackio.ui.MenuController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


public class Test extends Application {
    public static final Logger logger = LogManager.getLogger(Test.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Snackio game started");


        logger.info("Initialisation started...");
        logger.info("Fetching menu controller");
        MenuController controller = MenuController.getInstance(primaryStage);

        logger.info("Opening main menu");
        controller.openMenu(MenuController.Menus.MAIN_MENU);

        logger.info("...Initialisation finished");
    }

}
