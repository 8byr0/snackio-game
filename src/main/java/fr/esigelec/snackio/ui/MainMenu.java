package fr.esigelec.snackio.ui;

import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.SoloGameEngine;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.map.MapFactory;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.lwjgl.system.CallbackI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu  implements Initializable {
    @FXML
    private Button openSoloMenuButton;
    @FXML
    private ImageView imageViewHover;

    @FXML
    private AnchorPane re;

    @FXML
    private Button openMultiMenuButton;
    private Stage stage;


    public MainMenu() {
    }
    public void transition() {
        TranslateTransition transitionMultiButton = new TranslateTransition();
        transitionMultiButton.setDuration(Duration.millis(1000));
        transitionMultiButton.setToY(50);
        transitionMultiButton.setAutoReverse(true);
        transitionMultiButton.setCycleCount(2);
        transitionMultiButton.setNode(openMultiMenuButton);
        transitionMultiButton.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image set = new Image("/img/multi.png");
        ImageView imset = new ImageView(set);

        openMultiMenuButton.setOnMouseEntered(event -> {
            openMultiMenuButton.setTranslateX(1);
            openMultiMenuButton.setStyle("-fx-opacity: 1");
            imageViewHover.setImage(set);
        });

        openMultiMenuButton.setOnMouseExited(event -> {

            openMultiMenuButton.setTranslateX(0);
            openMultiMenuButton.setStyle("-fx-opacity: 0.6");
        });
        openSoloMenuButton.setOnMouseEntered(event -> {
            openSoloMenuButton.setTranslateX(1);
            openSoloMenuButton.setStyle("-fx-opacity: 1");
        });
        openSoloMenuButton.setOnMouseExited(event -> {
            openSoloMenuButton.setTranslateX(0);
            openSoloMenuButton.setStyle("-fx-opacity: 0.6");
        });
        openMultiMenuButton.setOnAction(this::openMultiMenu);
        openSoloMenuButton.setOnAction(this::openSoloMenu);
    }

    public void openSoloMenu(ActionEvent actionEvent) {
        //transition();
        try {
            try {
        try {
        try {
            SnackioGame game = SnackioGame.getInstance();

            // Create the local player
            Player myPlayer = new Player("Bob", CharacterFactory.CharacterType.INSPECTOR);
            myPlayer.getCharacter().setPosition(100,900);

            /////////////// NETWORK CONTROL
            // Instantiate Network game engine to control gameplay
            AbstractGameEngine engine = new SoloGameEngine(game, myPlayer, MapFactory.MapType.DESERT_CASTLE, 5);
            // Instantiate a NetClient to exchange with client

            engine.startGame();

        } catch (NoCharacterSetException e) {
            Log.error(e.getMessage(), e);
        }
        } catch (GameCannotStartException e) {
            Log.error(e.getMessage(), e);
        }
        } catch (UnhandledControllerException e) {
            Log.error(e.getMessage(), e);
        }
    } catch (
    UnhandledCharacterTypeException e) {
        Log.error(e.getMessage(), e);
    }

    }

    public void openMultiMenu(ActionEvent actionEvent) {
        //transition();
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);
    }
}
