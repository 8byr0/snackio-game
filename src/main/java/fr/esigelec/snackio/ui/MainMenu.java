package fr.esigelec.snackio.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.audio.Wav;
import com.esotericsoftware.minlog.Log;
import fr.esigelec.snackio.Snackio;
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
    private Rectangle upCurtain,downCurtain;
    @FXML
    private Button openSoloMenuButton;
    private Music leftStepSound;

    private static  boolean flashBack;

    @FXML
    private ImageView coin,coinBait;
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
    @FXML public ImageView imageViewLeft,imageViewRight,characterIntroImageView;
    public  Animation animationViewLeft,animationViewRight,characterIntroWalk,characterIntroTurn,coinToss,coinBaitToss;
    public TranslateTransition characterLeftEnter,characterIntroMove,characterRightEnter,characterLeftTranslateTransition,characterRightTranslateTransition,multiTranslateTransition,soloTranslateTransition;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!flashBack){
            intro();
        }else{
            mainAnchorPane.getChildren().remove(introScene);
            outro();
        }
        setButtonAnimations();
        openMultiMenuButton.setOnAction(this::openMultiMenu);
        openSoloMenuButton.setOnAction(this::openSoloMenu);
    }

    public void openSoloMenu(ActionEvent actionEvent) {
        setOutroOn();
        Timeline startGame = new Timeline(new KeyFrame(Duration.millis(3200),
                ae ->MenuController.getInstance(stage).openMenu(MenuController.Menus.SOLO_CONFIG_MENU)));
        startGame.play();

    }


    public void openMultiMenu(ActionEvent actionEvent) {
        setOutroOn();
        Timeline getOut = new Timeline(new KeyFrame(Duration.millis(3200),
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














    /*Here Is the Intro.
    Explaination: One of the game character enter in the screen and search a coin. When the coin appear, he go through it
    and then the game name and the menu appear.
     */
    public void intro(){
        characterIntroImageView.setImage(new Image("sprites/inspector.png"));
        characterIntroImageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        flashBack=true;

        coinToss =  new SpriteAnimation(coin, Duration.millis(200), COUNT, 9, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
        coinToss.setCycleCount(Animation.INDEFINITE);
        coinToss.play();
        coinBaitToss =  new SpriteAnimation(coinBait, Duration.millis(300), COUNT, 9, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
        coinBaitToss.setCycleCount(Animation.INDEFINITE);
        coinBaitToss.play();

        FadeTransition bait = new FadeTransition(Duration.millis(500), coinBait);
        bait.setFromValue(0);
        bait.setToValue(1);
        bait.setAutoReverse(true);
        bait.setCycleCount(500);
        bait.setDuration(Duration.INDEFINITE);
        bait.play();

        //ANIMATION 1: CHARACTER ENTER THE SCENE (Character Move from left to right)

        characterIntroMove = new TranslateTransition();
        characterIntroMove.setByX(coin.getLayoutX()+characterIntroImageView.getFitWidth()-10);
        characterIntroMove.setDuration(Duration.millis(1500));
        characterIntroMove.setNode(characterIntroImageView);
        characterIntroMove.setAutoReverse(true);

        //We make the man walk

        characterIntroWalk =  new SpriteAnimation(characterIntroImageView, Duration.millis(500), COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
        characterIntroWalk.setCycleCount(Animation.INDEFINITE);
        characterIntroTurn = new SpriteAnimation(characterIntroImageView, Duration.millis(700), 4, 5, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
        characterIntroTurn.setCycleCount(Animation.INDEFINITE);
        Timeline startCharacterMoveSignal = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae ->{

                    characterIntroMove.play();
                    characterIntroWalk.play();
                }));

        startCharacterMoveSignal.play();

        //
        Timeline stopBait = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae ->{
                    bait.stop();
                    coinBaitToss.stop();
                    coinBait.setVisible(false);
                }));
        stopBait.play();
        //ANIMATION 2 : WE MAKE HIM STOP WALKING
        Timeline characterStopWalking = new Timeline(new KeyFrame(
                Duration.millis(2600),
                ae ->{

                    characterIntroWalk.stop();
                    characterIntroTurn.play();
                }));
        characterStopWalking.play();

        //ANIMATION 3 : COIN APPEAR ON THE SCENE
        FadeTransition coinAppear = new FadeTransition(Duration.millis(500), coin);
        coinAppear.setFromValue(0);
        coinAppear.setToValue(1);
        coinAppear.setAutoReverse(true);
        coinAppear.setCycleCount(500);
        coinAppear.setDuration(Duration.INDEFINITE);

        Timeline coinAppearSignal = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> {
                    characterIntroTurn.play();
                    characterIntroWalk.stop();
                    characterIntroWalk =  new SpriteAnimation(characterIntroImageView, Duration.millis(500), COUNT, COLUMNS, OFFSET_X, 0, WIDTH, HEIGHT);
                    characterIntroWalk.setCycleCount(Animation.INDEFINITE);
                    coinAppear.play();
                }));

        coinAppearSignal.play();

        //ANIMATION 4: THE CHARACTER GO UP AND SAVE THE COIN
        Timeline goUpCharacterMoveSignal = new Timeline(new KeyFrame(
                Duration.millis(3500),
                ae -> {
                    //SETTING  ANIMATION 2: the character go up to save the coin (Character Move from down to up)
                    characterIntroTurn.stop();
                    characterIntroMove.setByY(coin.getFitHeight()+characterIntroImageView.getY()-700);
                    characterIntroMove.setByX(0);
                    characterIntroWalk.play();
                    characterIntroMove.play();
                }));
        goUpCharacterMoveSignal.play();
        FadeTransition removeCurtain = new FadeTransition(Duration.millis(1000), upCurtain);
        Timeline endCharacterMoveSignal = new Timeline(new KeyFrame(
                Duration.millis(5000),
                ae -> {
                    characterIntroMove.stop();
                    removeCurtain.setFromValue(1);
                    removeCurtain.setToValue(0);
                    removeCurtain.play();
                    coinAppear.stop();
                    coin.setVisible(false);
                    characterIntroMove.setDuration(Duration.millis(2000));
                    characterIntroMove.setByY(-coin.getFitHeight()-150);
                    characterIntroMove.play();
                }));
        endCharacterMoveSignal.play();


        Timeline endAnimation = new Timeline(new KeyFrame(
                Duration.millis(6000),
                ae -> {
                    removeCurtain.stop();
                    removeCurtain.setDuration(Duration.millis(1500));

                    removeCurtain.setNode(downCurtain);
                    removeCurtain.play();
                    coinToss.stop();

                }));
       endAnimation.play();

       Timeline accessMainMenu= new Timeline(new KeyFrame(
               Duration.millis(8000),
               ae -> {
                   mainAnchorPane.getChildren().remove(introScene);
                   outro();
               }));
       accessMainMenu.play();
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
        characterLeftTranslateTransition.setDuration(Duration.millis(2000));
        characterRightTranslateTransition.setDuration(Duration.millis(2000));
        characterLeftTranslateTransition.setAutoReverse(true);
        characterRightTranslateTransition.setAutoReverse(true);
        characterLeftTranslateTransition.setNode(imageViewLeft);
        characterRightTranslateTransition.setNode(imageViewRight);

        characterLeftEnter = new TranslateTransition();
        characterLeftEnter.setByX(openMultiMenuButton.getLayoutX());
        characterLeftEnter.setDuration(Duration.millis(1000));
        characterLeftEnter.setAutoReverse(true);
        characterLeftEnter.setNode(imageViewLeft);

        characterRightEnter = new TranslateTransition();
        characterRightEnter.setByX(openSoloMenuButton.getLayoutX()+openSoloMenuButton.getPrefWidth()-700);
        characterRightEnter.setDuration(Duration.millis(1000));
        characterRightEnter.setAutoReverse(true);
        characterRightEnter.setNode(imageViewRight);

        multiTranslateTransition = new TranslateTransition();
        //the transition will set to be auto reversed by setting this to true
        multiTranslateTransition.setAutoReverse(true);
        //setting the duration for the Translate transition
        multiTranslateTransition.setDuration(Duration.millis(2000));
        //shifting the X coordinate of the centre of the circle by 400
        multiTranslateTransition.setByX(800);
        multiTranslateTransition.setNode(openMultiMenuButton);

        soloTranslateTransition = new TranslateTransition();
        soloTranslateTransition.setByX(-800);
        soloTranslateTransition.setDuration(Duration.millis(2000));
        soloTranslateTransition.setAutoReverse(true);
        soloTranslateTransition.setNode(openSoloMenuButton);

    }

    public void setOutroOn(){
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
                Duration.millis(1100),
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
                Duration.millis(3200),
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
