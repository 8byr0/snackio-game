package fr.esigelec.snackio.ui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MultiMenu implements Initializable {
    @FXML
    private Button openJoinMenuButton;

    @FXML
    private Button openHostMenuButton;
    private Stage stage;
    FadeTransition openJoinMenuButtonAnimation,openHostMenuButtonAnimation;
    Timeline timeline;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openJoinMenuButton.setStyle("-fx-opacity: 0");
        openHostMenuButton.setStyle("-fx-opacity: 0");
        introAnimation();
        Snippet.setPreviousLocation(MenuController.Menus.MAIN_MENU);
        openJoinMenuButton.setOnAction(this::openJoinMenu);
        openHostMenuButton.setOnAction(this::openHostMenu);

    }
    public void introAnimation(){
        openHostMenuButton.setDisable(true);
        openJoinMenuButton.setDisable(true);
        openJoinMenuButtonAnimation = new FadeTransition(Duration.millis(1000), openJoinMenuButton);
        openHostMenuButtonAnimation = new FadeTransition(Duration.millis(1000), openHostMenuButton);

        openJoinMenuButtonAnimation.setFromValue(0);
        openHostMenuButtonAnimation.setFromValue(0);

        openJoinMenuButtonAnimation.setToValue(0.6);
        openHostMenuButtonAnimation.setToValue(0.6);

        openJoinMenuButtonAnimation.setAutoReverse(true);
        openHostMenuButtonAnimation.setAutoReverse(true);

        openJoinMenuButtonAnimation.setCycleCount(500);
        openHostMenuButtonAnimation.setCycleCount(500);

        openJoinMenuButtonAnimation.setDuration(Duration.INDEFINITE);
        openHostMenuButtonAnimation.setDuration(Duration.INDEFINITE);

        openJoinMenuButtonAnimation.play();
        openHostMenuButtonAnimation.play();

        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    openJoinMenuButtonAnimation.stop();
                    openHostMenuButtonAnimation.stop();
                    openJoinMenuButton.setStyle("-fx-opacity: 0.6");
                    openHostMenuButton.setStyle("-fx-opacity: 0.6");
                    openHostMenuButton.setDisable(false);
                    openJoinMenuButton.setDisable(false);
                    setButtonAnimation();
                }));
        timeline.play();
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

    public void openJoinMenu(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.JOIN_ROOM_MENU);

    }


    public void openHostMenu(ActionEvent actionEvent) {
        MenuController.getInstance(stage).openMenu(MenuController.Menus.SERVER_CONFIG_MENU);
    }
}
