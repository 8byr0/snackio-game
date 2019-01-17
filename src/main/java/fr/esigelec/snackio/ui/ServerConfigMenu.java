package fr.esigelec.snackio.ui;

import fr.esigelec.snackio.game.character.Character;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.character.texture.AnimatedCharacterSkin;
import fr.esigelec.snackio.game.map.MapFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.lwjgl.system.CallbackI;

import java.net.URL;
import java.util.*;

public class ServerConfigMenu implements Initializable {

    @FXML
    private TextField roomName;

    @FXML
    private Button submit;

    @FXML
    private ComboBox map;

    @FXML
    private ComboBox mode;

    private Stage stage;
    private Scene scene;

    @FXML
    private GridPane grid;

    public ServerConfigMenu() {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        map.getItems().setAll(MapFactory.MapType.values());
        mode.getItems().setAll(CharacterFactory.CharacterType.values());
        submit.setOnAction(this::submitServer);
    }

    public void submitServer(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);
    }
}

