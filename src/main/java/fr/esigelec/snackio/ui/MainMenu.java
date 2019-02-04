package fr.esigelec.snackio.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.audio.Wav;
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
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
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
    private AnchorPane introScene,mainAnchorPane;
    @FXML
    private Rectangle up_shape,down_shape;
    @FXML
    private Button openSoloMenuButton;
    private Music leftStepSound;

    @FXML
    private ImageView coin;
    @FXML
    private Button openMultiMenuButton;
    private Stage stage;

    public Image IMAGE;

    public  int COLUMNS  =   10;
    public  int COUNT    =  9;
    public  int OFFSET_X =  0;
    public int OFFSET_Y =  195;
    public int WIDTH    = 64;
    public int HEIGHT   = 64;
    @FXML public ImageView imageViewLeft,imageViewRight,imageViewIntro;
    public  Animation animationViewLeft,animationViewRight,animationViewIntro;
    public TranslateTransition characterLeftEnter,characterIntroEnter,characterRightEnter,characterLeftTranslateTransition,characterRightTranslateTransition,multiTranslateTransition,soloTranslateTransition;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //intro();
        mainAnchorPane.getChildren().remove(introScene);
        outro();
        setButtonAnimations();
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
                        //MenuController.getStage().close();
                        engine.startGame();
                        //MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU);

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
        setAnimationOn();
        Timeline getOut = new Timeline(new KeyFrame(Duration.millis(4400),
                ae -> MenuController.getInstance(stage).openMenu(MenuController.Menus.MULTI_MENU)
        ));
        getOut.play();
    }

    public void setButtonAnimations(){
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
            if(!openSoloMenuButton.isDisable()) {
                openSoloMenuButton.setTranslateX(1);
                openSoloMenuButton.setStyle("-fx-opacity: 1");
            }
        });
        openSoloMenuButton.setOnMouseExited(event -> {
            if(!openSoloMenuButton.isDisable()) {
                openSoloMenuButton.setTranslateX(0);
                openSoloMenuButton.setStyle("-fx-opacity: 0.6");
            }
        });
    }















    public void intro(){

        IMAGE = new Image("sprites/inspector.png");
        imageViewIntro.setImage(IMAGE);
        imageViewIntro.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        FadeTransition up = new FadeTransition(Duration.millis(1000), coin);
        up.setFromValue(0);
        up.setToValue(1);
        up.setAutoReverse(true);
        up.setCycleCount(500);
        up.setDuration(Duration.INDEFINITE);
        animationViewIntro =  new SpriteAnimation(imageViewIntro, Duration.millis(500), COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);


        animationViewIntro.setCycleCount(Animation.INDEFINITE);
        characterIntroEnter = new TranslateTransition();
        characterIntroEnter.setByX(coin.getLayoutX()+imageViewIntro.getFitWidth()-10);
        characterIntroEnter.setDuration(Duration.millis(4000));
        characterIntroEnter.setNode(imageViewIntro);
        characterIntroEnter.setAutoReverse(true);
        imageViewIntro.setOpacity(1);
        characterIntroEnter.play();
        animationViewIntro.play();
        Timeline startTimeline = new Timeline(new KeyFrame(
                Duration.millis(4000),
                ae -> {
                    characterIntroEnter.stop();
                    animationViewIntro.stop();
                    characterIntroEnter.setByY(coin.getFitHeight()+imageViewIntro.getY()-700);
                    characterIntroEnter.setByX(0);
                    animationViewIntro =  new SpriteAnimation(imageViewIntro, Duration.millis(500), COUNT, COLUMNS, OFFSET_X, 0, WIDTH, HEIGHT);
                    animationViewIntro.setCycleCount(Animation.INDEFINITE);
                }));
        startTimeline.play();
        Timeline coinAppear = new Timeline(new KeyFrame(
                Duration.millis(7000),
                ae -> up.play()
                ));
        coinAppear.play();
        ////rajouter le gars qui se retourne dans tous les sens

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(8000),
                ae -> {
                    characterIntroEnter.play();
                    animationViewIntro.play();

                }));
        timeline.play();
        FadeTransition shape = new FadeTransition(Duration.millis(3000), up_shape);
        Timeline coinDesappear = new Timeline(new KeyFrame(
                Duration.millis(11500),
                ae -> {
                    up.stop();
                    coin.setVisible(false);
                    characterIntroEnter.stop();
                    characterIntroEnter.setByY(-coin.getFitHeight()-150);
                    characterIntroEnter.play();

                    shape.setFromValue(1);
                    shape.setToValue(0);
                    shape.play();


                }));
        coinDesappear.play();
        Timeline timeline2 = new Timeline(new KeyFrame(
                Duration.millis(15000),
                ae -> {
                    shape.stop();
                    characterIntroEnter.stop();
                    animationViewIntro.stop();
                    imageViewIntro.setVisible(false);
                    shape.setNode(down_shape);
                    shape.play();
                }));
        timeline2.play();
        Timeline timeline3 = new Timeline(new KeyFrame(
                Duration.millis(17000),
                ae -> {
                    shape.stop();
                    mainAnchorPane.getChildren().remove(introScene);
                    outro();
                }));
        timeline3.play();

    }

    public void outro(){
        IMAGE = new Image("sprites/bald_man.png");
        imageViewLeft.setOpacity(0);
        imageViewRight.setOpacity(0);

        imageViewLeft.setImage(IMAGE);
        imageViewRight.setImage(IMAGE);

        imageViewLeft.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        imageViewRight.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));

        animationViewLeft = new SpriteAnimation(imageViewLeft, Duration.millis(500), COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
        animationViewRight = new SpriteAnimation(imageViewRight, Duration.millis(500), COUNT, COLUMNS, OFFSET_X, 74, WIDTH, HEIGHT);

        animationViewLeft.setCycleCount(Animation.INDEFINITE);
        animationViewRight.setCycleCount(Animation.INDEFINITE);

        characterLeftTranslateTransition = new TranslateTransition();
        characterRightTranslateTransition = new TranslateTransition();
        characterLeftTranslateTransition.setByX(800);
        characterRightTranslateTransition.setByX(-800);
        characterLeftTranslateTransition.setDuration(Duration.millis(3000));
        characterRightTranslateTransition.setDuration(Duration.millis(3000));
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
        multiTranslateTransition.setDuration(Duration.millis(3000));
        //shifting the X coordinate of the centre of the circle by 400
        multiTranslateTransition.setByX(800);
        multiTranslateTransition.setNode(openMultiMenuButton);

        soloTranslateTransition = new TranslateTransition();
        soloTranslateTransition.setByX(-800);
        soloTranslateTransition.setDuration(Duration.millis(3000));
        soloTranslateTransition.setAutoReverse(true);
        soloTranslateTransition.setNode(openSoloMenuButton);

    }

    public void setAnimationOn(){
        openMultiMenuButton.setDisable(true);
        openSoloMenuButton.setDisable(true);
        openMultiMenuButton.setStyle("-fx-opacity: 0.6");
        imageViewLeft.setOpacity(1);
        imageViewRight.setOpacity(1);
        characterLeftEnter.play();
        characterRightEnter.play();
        animationViewLeft.play();
        animationViewRight.play();
        /*the characters coming from the left and the right must come near to the buttons before its all move.
        This  timer:
         wait the end of the first animations (characterLeftEnter & characterRightEnter) and stop them.
         Start the other animation.
         */
        Timeline startTimeline = new Timeline(new KeyFrame(
                Duration.millis(2100),
                ae -> {
                    characterLeftEnter.stop();
                    characterRightEnter.stop();
                    characterLeftTranslateTransition.play();
                    characterRightTranslateTransition.play();
                    multiTranslateTransition.play();
                    soloTranslateTransition.play();

                }));
        startTimeline.play();
        //Animations Stop timer
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(5200),
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
}
