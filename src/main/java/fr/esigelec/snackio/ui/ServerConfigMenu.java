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
import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;
import java.util.List;

public class ServerConfigMenu implements Initializable {

    private static final int SQUARE_SIDE = 67;
    @FXML
    private TextField playerName;

    @FXML
    private TextField serverName;

    @FXML
    private ColumnConstraints mapsFitLimit,characterFitLimit;

    @FXML
    private Button submit;

    private ToggleGroup mapGroup = new ToggleGroup();

    private ToggleGroup characterGroup = new ToggleGroup();
    private ToggleButton perChoice,mapChoice;

    @FXML
    private GridPane grid;

    public ServerConfigMenu() {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);



        //        //show all the choices of the characters
        List<Enum> somethingList = Arrays.asList(CharacterFactory.CharacterType.values());
        HBox characterBox = new HBox();
        characterBox.setSpacing(5);
        for (Enum character: somethingList){
            ToggleButton rbCha = new ToggleButton();

            ImageView imgCha= new ImageView("sprites/menu_"+character.toString()+".png");
            imgCha.setFitHeight(SQUARE_SIDE);
            imgCha.setFitWidth(SQUARE_SIDE);
            rbCha.setGraphic(imgCha);
            rbCha.setId(character.toString());
            rbCha.setToggleGroup(characterGroup);
            rbCha.setStyle("-fx-background-color: white");
            rbCha.setStyle("-fx-arc-width: 0");
            rbCha.setStyle("-fx-arc-height: 0");
            characterBox.getChildren().add(rbCha);
        }
        grid.add(characterBox,1,3);
        characterGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (characterGroup.getSelectedToggle() != null) {
                    if(perChoice!= null){
                        perChoice.setStyle("-fx-border-color: white");
                    }
                     perChoice = (ToggleButton) characterGroup.getSelectedToggle();
                    System.out.println("color="+perChoice.getStyle());
                    ((ToggleButton) characterGroup.getSelectedToggle()).setStyle("-fx-background-color: turquoise");
                    System.out.println("Character: " + perChoice.getId());
                }
            }
        });





        //           //show all the choices of the maps
        List<Enum> mapList = Arrays.asList(MapFactory.MapType.values());
        HBox mapBox = new HBox();
        HBox mapNameBox= new HBox();
        for (Enum map: mapList){
            ToggleButton rbMap = new ToggleButton();
            Text name= new Text();
            ImageView imgMap= new ImageView("maps/"+map.toString()+".png");
            imgMap.setFitWidth(SQUARE_SIDE);
            imgMap.setFitHeight(SQUARE_SIDE);
            rbMap.setId(map.toString());
            name.setText(map.toString());
            rbMap.setGraphic(imgMap);
            rbMap.setToggleGroup(mapGroup);
            mapBox.getChildren().add(rbMap);
            mapNameBox.getChildren().add(name);
        }
        grid.add(mapBox,1,1);
        grid.add(mapNameBox,1,2);

        mapGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (mapGroup.getSelectedToggle() != null) {
                    if(mapChoice!= null){
                        mapChoice.setStyle("-fx-border-color: white");
                    }
                    mapChoice = (ToggleButton) mapGroup.getSelectedToggle();
                    ((ToggleButton) mapGroup.getSelectedToggle()).setStyle("-fx-background-color: turquoise");
                    System.out.println("Map: " + mapChoice.getId());
                }
            }
        });





        submit.setOnAction(this::submitServer);
    }

    public void submitServer(ActionEvent actionEvent) {
        if(mapGroup.getSelectedToggle() != null && characterGroup.getSelectedToggle() != null
                && !playerName.getText().isEmpty() && !serverName.getText().isEmpty()){
            try {
                try {
                    try {
                        try{
                            try{
                        SnackioGame game = SnackioGame.getInstance();
                        //        // Create the local player
                                ToggleButton rbCha = (ToggleButton) characterGroup.getSelectedToggle();
                        Player myPlayer = new Player(playerName.getText(),
                                CharacterFactory.CharacterType.valueOf(String.valueOf(rbCha.getId())));

                        //
                        //        /////////////// NETWORK CONTROL
                        //        // Instantiate Network game engine to control gameplay
                                ToggleButton rbMap = (ToggleButton) mapGroup.getSelectedToggle();
                        AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer,
                                MapFactory.MapType.valueOf(String.valueOf(rbMap.getId())));
                        //        // Instantiate a NetClient to exchange with client
                        SnackioNetClient cli = new SnackioNetClient(engine);
                        NetworkGameEngine nEngine=new NetworkGameEngine(game,myPlayer,
                                MapFactory.MapType.valueOf(String.valueOf(rbMap.getId())));
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
        else {
            Label notCompleted = new Label("information not completed!!");
            notCompleted.setTextFill(Color.web("#0076a3"));
            grid.add(notCompleted, 0, 4);
        }
    }
}

