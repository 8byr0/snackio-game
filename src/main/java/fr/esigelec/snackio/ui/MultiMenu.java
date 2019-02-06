package fr.esigelec.snackio.ui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MultiMenu implements Initializable {
    private static final double MIN_OPACITY = 0,MAX_OPACITY=0.6;
    @FXML
    private Button openJoinMenuButton;

    @FXML
    private Button openHostMenuButton;
    private Stage stage;
    FadeTransition openJoinMenuButtonAnimation,openHostMenuButtonAnimation;
    Timeline timeline;

    AudioClip audio;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        audio= new AudioClip(getClass().getResource("/sound/buttonMenuPress.wav").toString());
        animation(MIN_OPACITY,MAX_OPACITY,"intro");
        Snippet.setPreviousLocation(MenuController.Menus.MAIN_MENU);
        openJoinMenuButton.setOnAction(this::openJoinMenu);
        openHostMenuButton.setOnAction(this::openHostMenu);

    }

    public void openJoinMenu(ActionEvent actionEvent) {
        audio.play();
        animation(MAX_OPACITY,MIN_OPACITY,"outro");
        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    MenuController.getInstance(stage).openMenu(MenuController.Menus.JOIN_ROOM_MENU);
                }));
        timeline.play();
    }

    public void openHostMenu(ActionEvent actionEvent) {
        audio.play();
        animation(MAX_OPACITY,MIN_OPACITY,"outro");
        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    MenuController.getInstance(stage).openMenu(MenuController.Menus.SERVER_CONFIG_MENU);
                }));
        timeline.play();
    }

    public void animation(double startValue,double endValue,String animationState){
        openJoinMenuButton.setStyle("-fx-opacity:"+startValue);
        openHostMenuButton.setStyle("-fx-opacity:"+startValue);
        openHostMenuButton.setDisable(true);
        openJoinMenuButton.setDisable(true);
        openJoinMenuButtonAnimation = new FadeTransition(Duration.millis(1000), openJoinMenuButton);
        openHostMenuButtonAnimation = new FadeTransition(Duration.millis(1000), openHostMenuButton);
        openJoinMenuButtonAnimation.setFromValue(startValue);
        openHostMenuButtonAnimation.setFromValue(startValue);

        openJoinMenuButtonAnimation.setToValue(endValue);
        openHostMenuButtonAnimation.setToValue(endValue);

        openJoinMenuButtonAnimation.setAutoReverse(true);
        openHostMenuButtonAnimation.setAutoReverse(true);

        openJoinMenuButtonAnimation.setCycleCount(500);
        openHostMenuButtonAnimation.setCycleCount(500);

        openJoinMenuButtonAnimation.setDuration(Duration.INDEFINITE);
        openHostMenuButtonAnimation.setDuration(Duration.INDEFINITE);

        openJoinMenuButtonAnimation.play();
        openHostMenuButtonAnimation.play();


        if(animationState=="intro"){
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(1000),
                    ae -> {
                        openJoinMenuButtonAnimation.stop();
                        openHostMenuButtonAnimation.stop();
                        openJoinMenuButton.setStyle("-fx-opacity:"+endValue);
                        openHostMenuButton.setStyle("-fx-opacity:"+endValue);
                        openHostMenuButton.setDisable(false);
                        openJoinMenuButton.setDisable(false);
                        setButtonAnimation();
                    }));
            timeline.play();
        }
        if(animationState=="outro"){
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(1000),
                    ae -> {
                        openJoinMenuButtonAnimation.stop();
                        openHostMenuButtonAnimation.stop();
                        openJoinMenuButton.setStyle("-fx-opacity:"+endValue);
                        openHostMenuButton.setStyle("-fx-opacity:"+endValue);
                        setButtonAnimation();
                    }));
            timeline.play();
        }

    }
    public void setButtonAnimation(){
        openJoinMenuButton.setOnMouseEntered(event -> {
            openJoinMenuButton.setTranslateX(1);
            openJoinMenuButton.setStyle("-fx-opacity: 1");
        });
        openJoinMenuButton.setOnMouseExited(event -> {
            openJoinMenuButton.setTranslateX(0);
            openJoinMenuButton.setStyle("-fx-opacity: 0.6");
        });
        openHostMenuButton.setOnMouseEntered(event -> {
            openHostMenuButton.setTranslateX(1);
            openHostMenuButton.setStyle("-fx-opacity: 1");
        });
        openHostMenuButton.setOnMouseExited(event -> {
            openHostMenuButton.setTranslateX(0);
            openHostMenuButton.setStyle("-fx-opacity: 0.6");
        });
    }


}
