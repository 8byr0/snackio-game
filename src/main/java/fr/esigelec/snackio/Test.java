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
        logger.info("TEST info");
        logger.debug("TEST");
        // Launch the stuff
        MenuController controller = MenuController.getInstance(primaryStage);

        controller.openMenu(MenuController.Menus.MAIN_MENU);

        Properties props = new Properties();
        try {
            final InputStream inStream = Test.class.getClassLoader().getResourceAsStream("log4j.properties");
            if (inStream != null) {
                props.load(inStream);
            } else {
                System.out.println("not on classpath");
            }
        } catch (IOException e) {
            logger.error("Exception ", e);
        }

        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            System.out.println(entry);
        }
    }

}
