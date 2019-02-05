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
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SoloConfigMenu implements Initializable {
    private static final int SQUARE_SIDE = 70;
    @FXML
    private Button join;

    private ToggleGroup characterGroup = new ToggleGroup();
    private ToggleGroup mapGroup = new ToggleGroup();

    private Player myPlayer;
    private Timeline timeline;

    @FXML
    private AnchorPane anchor,mainAnchorPane;

    private ToggleButton perChoice,mapChoice;

    private FadeTransition showAnchor;

    @FXML
    private GridPane grid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MAIN_MENU);
        showImageCharacter();
        showImageMap();
        animation(1,0);
        join.setOnMouseEntered(event -> {
            if(mapGroup.getSelectedToggle() !=null && characterGroup.getSelectedToggle() != null) {
                join.setTranslateX(1);
                join.setStyle("-fx-opacity: 1");
                join.setOnAction(this::connection);
            }
        });
        join.setOnMouseExited(event -> {
            join.setStyle("-fx-opacity: 0.6");
            join.setTranslateX(0);
        });

    }

    public void connection(ActionEvent actionEvent) {
        if(mapGroup.getSelectedToggle() !=null && characterGroup.getSelectedToggle() != null){
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
            rbCha.setStyle("-fx-background-color: gray");
            characterBox.getChildren().add(rbCha);
        }
        grid.add(characterBox,1,1);
        characterGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (characterGroup.getSelectedToggle() != null) {
                    if(perChoice!= null){
                        perChoice.setStyle("-fx-background-color: gray");
                    }
                    perChoice = (ToggleButton) characterGroup.getSelectedToggle();
                    ((ToggleButton) characterGroup.getSelectedToggle()).setStyle("-fx-background-color: white;");
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
            rbMap.setStyle("-fx-background-color: gray");
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
                        mapChoice.setStyle("-fx-border-color: gray");
                    }
                    mapChoice = (ToggleButton) mapGroup.getSelectedToggle();
                    ((ToggleButton) mapGroup.getSelectedToggle()).setStyle("-fx-background-color: white");
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


                    myPlayer = new Player("", CharacterFactory.CharacterType.valueOf(String.valueOf(rbCha.getId())));

                    myPlayer.getCharacter().setPosition(100,900);

                    AbstractGameEngine engine = new SoloGameEngine(game, myPlayer, MapFactory.MapType.valueOf(String.valueOf(rbMap.getId())), 5);
                    //MenuController.getStage().initModality(APPLICATION_MODAL);
                    engine.startGame();
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

    public void animation(double startValue,double endValue){
       anchor.setStyle("-fx-opacity:"+startValue);
        anchor.setDisable(true);
        showAnchor = new FadeTransition(Duration.millis(1000), anchor);
        showAnchor.setFromValue(startValue);

        showAnchor.setToValue(endValue);

        showAnchor.setAutoReverse(true);

        showAnchor.setCycleCount(500);

        showAnchor.setDuration(Duration.INDEFINITE);

        showAnchor.play();

            timeline = new Timeline(new KeyFrame(
                    Duration.millis(1000),
                    ae -> {
                        showAnchor.stop();
                        mainAnchorPane.getChildren().remove(anchor);
                    }));
            timeline.play();


    }
}
