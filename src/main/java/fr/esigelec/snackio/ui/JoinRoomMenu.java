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
import fr.esigelec.snackio.game.character.texture.AnimatedCharacterSkin;
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
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class JoinRoomMenu  implements Initializable {
    private static final int SQUARE_SIDE = 67;
    @FXML
    private Button join;
    @FXML
    private TextField playerName;
    @FXML
    private Button refresh;
    private ToggleGroup characterGroup = new ToggleGroup();

    @FXML
    private ChoiceBox server_box;

    private ToggleButton perChoice;

    @FXML
    private GridPane grid;

    public JoinRoomMenu() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Snippet.setPreviousLocation(MenuController.Menus.MULTI_MENU);
        server("getInformation");
        showImageCharacter();

        join.setOnMouseEntered(event -> {
            join.setTranslateX(1);
            join.setStyle("-fx-opacity: 1");
        });
        join.setOnMouseExited(event -> {
            join.setStyle("-fx-opacity: 0.6");
            join.setTranslateX(0);
        });

        refresh.setOnMouseEntered(event -> {
            refresh.setTranslateX(1);
            refresh.setStyle("-fx-opacity: 1");
        });
        refresh.setOnMouseExited(event -> {
            refresh.setStyle("-fx-opacity: 0.6");
            refresh.setTranslateX(0);
        });

        refresh.setOnAction(this::refreshInfoServer);
        join.setOnAction(this::connection);

    }

    public void connection(ActionEvent actionEvent) {
        if(!playerName.getText().isEmpty() && !server_box.getSelectionModel().isEmpty() && characterGroup.getSelectedToggle() != null){
            server("getConnection");
        }
    }

    public void refreshInfoServer(ActionEvent actionEvent) {
            server("getInformation");
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
                    System.out.println("color="+perChoice.getStyle());
                    ((ToggleButton) characterGroup.getSelectedToggle()).setStyle("-fx-background-color: turquoise");
                    System.out.println("Character: " + perChoice.getId());
                }
            }
        });
    }

    public void server (String info) {
        try {
            try {
                try {
                    SnackioGame game = SnackioGame.getInstance();
                    //        // Create the local player
                    Player myPlayer = new Player("Hugues", CharacterFactory.CharacterType.GOLDEN_KNIGHT);
                    if(info=="getConnection"){
                        ToggleButton rbCha = (ToggleButton) characterGroup.getSelectedToggle();
                        myPlayer = new Player(playerName.getText(),
                                CharacterFactory.CharacterType.valueOf(String.valueOf(rbCha.getId())));

                    }
                    //
                    //        /////////////// NETWORK CONTROL
                    //        // Instantiate Network game engine to control gameplay
                    AbstractGameEngine engine = new NetworkGameEngine(game, myPlayer, MapFactory.MapType.DESERT_CASTLE);
                    //        // Instantiate a NetClient to exchange with client
                    SnackioNetClient cli = new SnackioNetClient(engine);
                    List<InetAddress> servers = cli.getAvailableServers();

                    //        logger.debug(servers);
                    if(servers.size() > 0) {
                        join.setDisable(false);
                        //Pour se connecter au server
                        if(info=="getConnection"){
                            System.out.println("connection");
                            cli.connectServer(servers.get(0));
                        }
                        //Pour recupérer les différents serveurs existants dans
                        if(info=="getInformation"){
                            System.out.println("information");
                            server_box.getItems().setAll(servers);
                        }
                    }else{
                        join.setDisable(true);
                        System.out.println("Aucun server");
                        server_box.getItems().clear();
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
