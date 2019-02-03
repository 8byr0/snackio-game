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
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.lwjgl.system.CallbackI;


import java.util.Timer;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimerTask;

public class MainMenu  implements Initializable {
    @FXML
    private Button openSoloMenuButton;

    @FXML
    private Button openMultiMenuButton;
    private Stage stage;

    public Image IMAGE;

    public  int COLUMNS  =   10;
    public  int COUNT    =  9;
    public  int OFFSET_X =  0;
    public int OFFSET_Y =  135;
    public int WIDTH    = 64;
    public int HEIGHT   = 64;
    @FXML public ImageView imageViewLeft,imageViewRight;
    public  Animation animationViewLeft,animationViewRight;
    public TranslateTransition characterLeftEnter,characterRightEnter,characterLeftTranslateTransition,characterRightTranslateTransition,multiTranslateTransition,soloTranslateTransition;

    public void transition() {
        TranslateTransition transitionMultiButton = new TranslateTransition();
        transitionMultiButton.setDuration(Duration.millis(1000));
        transitionMultiButton.setToY(50);
        transitionMultiButton.setAutoReverse(true);
        transitionMultiButton.setCycleCount(10);
        transitionMultiButton.setNode(openMultiMenuButton);
        transitionMultiButton.play();
    }
    public void setCharacterAnimation(){
        IMAGE = new Image("sprites/golden_knight.png");
        imageViewLeft.setOpacity(0);
        imageViewRight.setOpacity(0);

        imageViewLeft.setImage(IMAGE);
        imageViewRight.setImage(IMAGE);

        imageViewLeft.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        imageViewRight.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));

        animationViewLeft = new SpriteAnimation(imageViewLeft, Duration.millis(500), COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
        animationViewRight = new SpriteAnimation(imageViewRight, Duration.millis(500), COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);

        animationViewLeft.setCycleCount(Animation.INDEFINITE);
        animationViewRight.setCycleCount(Animation.INDEFINITE);

        characterLeftTranslateTransition = new TranslateTransition();
        characterRightTranslateTransition = new TranslateTransition();
        characterLeftTranslateTransition.setByX(600);
        characterRightTranslateTransition.setByX(-600);
        characterLeftTranslateTransition.setDuration(Duration.millis(5000));
        characterRightTranslateTransition.setDuration(Duration.millis(5000));
        characterLeftTranslateTransition.setAutoReverse(true);
        characterRightTranslateTransition.setAutoReverse(true);
        characterLeftTranslateTransition.setNode(imageViewLeft);
        characterRightTranslateTransition.setNode(imageViewRight);

        characterLeftEnter = new TranslateTransition();
        characterLeftEnter.setByX(openMultiMenuButton.getLayoutX());
        characterLeftEnter.setDuration(Duration.millis(2000));
        characterLeftEnter.setAutoReverse(true);
        characterLeftEnter.setNode(imageViewLeft);

        characterRightEnter = new TranslateTransition();
        characterRightEnter.setByX(openSoloMenuButton.getLayoutX()+openSoloMenuButton.getPrefWidth()-700);
        characterRightEnter.setDuration(Duration.millis(2000));
        characterRightEnter.setAutoReverse(true);
        characterRightEnter.setNode(imageViewRight);

        multiTranslateTransition = new TranslateTransition();
        //the transition will set to be auto reversed by setting this to true
        multiTranslateTransition.setAutoReverse(true);
        //setting the duration for the Translate transition
        multiTranslateTransition.setDuration(Duration.millis(5000));
        //shifting the X coordinate of the centre of the circle by 400
        multiTranslateTransition.setByX(600);
        multiTranslateTransition.setNode(openMultiMenuButton);

        soloTranslateTransition = new TranslateTransition();
        soloTranslateTransition.setByX(-600);
        soloTranslateTransition.setDuration(Duration.millis(5000));
        soloTranslateTransition.setCycleCount(500);
        soloTranslateTransition.setAutoReverse(true);
        soloTranslateTransition.setNode(openSoloMenuButton);


    }

    public void setAnimationOn(){
        openMultiMenuButton.setDisable(true);
        openMultiMenuButton.setStyle("-fx-opacity: 0.6");
        imageViewLeft.setOpacity(1);
        imageViewRight.setOpacity(1);
        characterLeftEnter.play();
        characterRightEnter.play();
        animationViewLeft.play();
        animationViewRight.play();
        Timeline startTimeline = new Timeline(new KeyFrame(
                Duration.millis(2200),
                ae -> {
                    characterLeftEnter.stop();
                    characterRightEnter.stop();
                    characterLeftTranslateTransition.play();
                    characterRightTranslateTransition.play();
                    multiTranslateTransition.play();
                    soloTranslateTransition.play();

                }));
        startTimeline.play();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(7200),
                ae -> {
                    animationViewLeft.stop();
                    characterLeftTranslateTransition.stop();
                    characterRightTranslateTransition.stop();
                    multiTranslateTransition.stop();
                    soloTranslateTransition.stop();
                    imageViewLeft.setOpacity(0);
                }));
        timeline.play();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("ivr="+imageViewRight.getX()+" anc=");
        setCharacterAnimation();
        System.out.println("x open multi "+openMultiMenuButton.getLayoutX());
        Timer timer2 = new Timer(true);
        Timer timer = new Timer(true);
        TimerTask timerTask0 = new TimerTask() {@Override public void run() {
            openMultiMenuButton.setTranslateX(1);
            openMultiMenuButton.setStyle("-fx-opacity: 1");

        }};
        //timer.scheduleAtFixedRate(timerTask0, 0, 5000);
        openMultiMenuButton.setOnMouseEntered(event -> {
            if(!openMultiMenuButton.isDisable()) {
                openMultiMenuButton.setTranslateX(1);
                openMultiMenuButton.setStyle("-fx-opacity: 1");
            }
        });

        openMultiMenuButton.setOnMouseExited(event -> {
            if(!openMultiMenuButton.isDisable()) {
                openMultiMenuButton.setTranslateX(0);
                openMultiMenuButton.setStyle("-fx-opacity: 0.6");
            }
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

    private void completeTask() {
        try {
            //assuming it takes 20 secs to complete the task
            transition();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void openMultiMenu(ActionEvent actionEvent) {
        setAnimationOn();
        Timeline getOut = new Timeline(new KeyFrame(Duration.millis(7200), ae -> { MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU); }));
        getOut.play();
    }
    public void setButtonTransition(){
        FadeTransition ft = new FadeTransition(Duration.millis(5000), openMultiMenuButton);
        FadeTransition ft2 = new FadeTransition(Duration.millis(5000), openSoloMenuButton);
        ft.setFromValue(1.0);
        ft2.setFromValue(1.0);
        ft.setToValue(0);
        ft2.setToValue(0);
        ft.setAutoReverse(false);
        ft2.setAutoReverse(false);
        ft.play();
        ft2.play();
        Timeline timeline2 = new Timeline(new KeyFrame(
                Duration.millis(5000),
                ae -> {
                    ft.stop();
                    ft2.stop();
                }));
        timeline2.play();


        System.out.println("avant");
    }
}
