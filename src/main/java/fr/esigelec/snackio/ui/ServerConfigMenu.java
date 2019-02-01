package fr.esigelec.snackio.ui;
import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import fr.esigelec.snackio.networking.server.SnackioNetServer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;
import java.util.List;

public class ServerConfigMenu implements Initializable {

    @FXML
    private TextField playerName;

    @FXML
    private Button submit;

    private ToggleGroup mapGroup = new ToggleGroup();

    private ToggleGroup characterGroup = new ToggleGroup();

    @FXML
    private GridPane grid;

    public ServerConfigMenu() {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        List<Enum> somethingList = Arrays.asList(CharacterFactory.CharacterType.values());
        //map.getItems().setAll(MapFactory.MapType.values());
        //character.getItems().setAll(CharacterFactory.CharacterType.values());
        HBox characterBox = new HBox();
        for (Enum character: somethingList){
            RadioButton rbCha = new RadioButton(character.toString());

            ImageView imgCha= new ImageView("http://icons.iconarchive.com/icons/vincentburton/diaguita-ceramic-bowl/128/Diaguita-Ceramic-Bowl-1-icon.png");
            imgCha.setFitHeight(40);
            imgCha.setFitWidth(40);
            rbCha.setGraphic(imgCha);
            rbCha.setToggleGroup(characterGroup);
            characterBox.getChildren().add(rbCha);
        }
        grid.add(characterBox,1,3);

        characterGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (characterGroup.getSelectedToggle() != null) {
                    RadioButton choice = (RadioButton) characterGroup.getSelectedToggle();
                    System.out.println("Character: " + choice.getText());
                }
            }
        });




        List<Enum> mapList = Arrays.asList(MapFactory.MapType.values());
        HBox mapBox = new HBox();
        for (Enum map: mapList){
            RadioButton rbMap = new RadioButton(map.toString());
            ImageView imgMap= new ImageView("http://icons.iconarchive.com/icons/vincentburton/diaguita-ceramic-bowl/128/Diaguita-Ceramic-Bowl-2-icon.png");
            imgMap.setFitWidth(40);
            imgMap.setFitHeight(40);
            rbMap.setGraphic(imgMap);
            rbMap.setToggleGroup(mapGroup);
            mapBox.getChildren().add(rbMap);
        }
        grid.add(mapBox,1,1);

        mapGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (mapGroup.getSelectedToggle() != null) {
                    RadioButton choice = (RadioButton) mapGroup.getSelectedToggle();
                    System.out.println("Map: " + choice.getText());
                }
            }
        });

        //characterGroup.getProperties().addListener();
        submit.setOnAction(this::submitServer);
    }

    public void submitServer(ActionEvent actionEvent) {
        if(mapGroup.getSelectedToggle() != null && characterGroup.getSelectedToggle() != null
                && !playerName.getText().isEmpty()){
            try {
                try {
                    try {
                        try{
                            try{
                        SnackioGame game = SnackioGame.getInstance();
                        //        // Create the local player
                                RadioButton rbCha = (RadioButton)characterGroup.getSelectedToggle();
                        Player myPlayer = new Player(playerName.getText(),
                                CharacterFactory.CharacterType.valueOf(String.valueOf(rbCha.getText())));

                        //
                        //        /////////////// NETWORK CONTROL
                        //        // Instantiate Network game engine to control gameplay
                                RadioButton rbMap = (RadioButton)mapGroup.getSelectedToggle();
                        AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer,
                                MapFactory.MapType.valueOf(String.valueOf(rbMap.getText())));
                        //        // Instantiate a NetClient to exchange with client
                        SnackioNetClient cli = new SnackioNetClient(engine);
                        NetworkGameEngine nEngine=new NetworkGameEngine(game,myPlayer,
                                MapFactory.MapType.valueOf(String.valueOf(rbMap.getText())));
                        List<InetAddress> servers = cli.getAvailableServers();
                        String[] difficultWords = new String[10];
                        difficultWords[0]="you";
                        SnackioNetServer.main(difficultWords);
                        //cli.connectServer(servers.get(0));
                        engine.startGame();
                        //000000000
                        } catch (IOException e) {
                            Log.error(e.getMessage(), e);
                        }
                        } catch (GameCannotStartException e) {
                            Log.error(e.getMessage(), e);
                        }

                    } catch (NoCharacterSetException e) {
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
    }
}

