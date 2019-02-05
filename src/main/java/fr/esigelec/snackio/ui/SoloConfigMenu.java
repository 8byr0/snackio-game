package fr.esigelec.snackio.ui;

import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.core.AbstractGameEngine;
import fr.esigelec.snackio.core.NetworkGameEngine;
import fr.esigelec.snackio.core.Player;
import fr.esigelec.snackio.core.SoloGameEngine;
import fr.esigelec.snackio.core.exceptions.GameCannotStartException;
import fr.esigelec.snackio.core.exceptions.NoCharacterSetException;
import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.core.exceptions.UnhandledControllerException;
import fr.esigelec.snackio.game.SnackioGame;
import fr.esigelec.snackio.game.character.CharacterFactory;
import fr.esigelec.snackio.game.map.MapFactory;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SoloConfigMenu implements Initializable {
    private static final int SQUARE_SIDE = 67;
    @FXML
    private Button join;
    @FXML
    private TextField playerName;

    private ToggleGroup characterGroup = new ToggleGroup();
    private ToggleGroup mapGroup = new ToggleGroup();

    private Player myPlayer;

    private ToggleButton perChoice,mapChoice;

    @FXML
    private GridPane grid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MAIN_MENU);
        showImageCharacter();
        showImageMap();
        join.setOnMouseEntered(event -> {
            join.setTranslateX(1);
            join.setStyle("-fx-opacity: 1");
        });
        join.setOnMouseExited(event -> {
            join.setStyle("-fx-opacity: 0.6");
            join.setTranslateX(0);
        });
        join.setOnAction(this::connection);

    }

    public void connection(ActionEvent actionEvent) {
        if(!playerName.getText().isEmpty() && mapGroup.getSelectedToggle() !=null && characterGroup.getSelectedToggle() != null){
            startGameSolo();
        }
    }

    public void showImageCharacter(){
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
        grid.add(characterBox,1,1);
        characterGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (characterGroup.getSelectedToggle() != null) {
                    if(perChoice!= null){
                        perChoice.setStyle("-fx-border-color: white");
                    }
                    perChoice = (ToggleButton) characterGroup.getSelectedToggle();
                    ((ToggleButton) characterGroup.getSelectedToggle()).setStyle("-fx-background-color: turquoise");
                }
            }
        });
    }
    public void showImageMap(){
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
        grid.add(mapBox,1,2);

        mapGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (mapGroup.getSelectedToggle() != null) {
                    if(mapChoice!= null){
                        mapChoice.setStyle("-fx-border-color: white");
                    }
                    mapChoice = (ToggleButton) mapGroup.getSelectedToggle();
                    ((ToggleButton) mapGroup.getSelectedToggle()).setStyle("-fx-background-color: turquoise");
                }
            }
        });
    }

    public void startGameSolo () {
        try {
            try {
                try {
                    try {
                    SnackioGame game = SnackioGame.getInstance();

                    ToggleButton rbCha = (ToggleButton) characterGroup.getSelectedToggle();
                    ToggleButton rbMap = (ToggleButton) mapGroup.getSelectedToggle();


                    myPlayer = new Player(playerName.getText(), CharacterFactory.CharacterType.valueOf(String.valueOf(rbCha.getId())));

                    myPlayer.getCharacter().setPosition(100,900);

                    AbstractGameEngine engine = new SoloGameEngine(game, myPlayer, MapFactory.MapType.valueOf(String.valueOf(rbMap.getId())), 5);

                    engine.startGame();

                    join.setDisable(false);
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
