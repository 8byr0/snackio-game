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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu  implements Initializable {
    @FXML
    private Button openSoloMenuButton;

    @FXML
    private Button openMultiMenuButton;
    private Stage stage;

    public MainMenu() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openMultiMenuButton.setOnAction(this::openMultiMenu);
        openSoloMenuButton.setOnAction(this::openSoloMenu);
    }

    public void openSoloMenu(ActionEvent actionEvent) {
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
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);
    }
}
