package fr.esigelec.snackio.ui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class CharacterSnippet {
    @FXML
    private ImageView aze,right,left;
    int a=2;
    public void start(int a){
        Image set;

        switch (a){
            case 1:
                set = new Image("/img/1.png");
                break;
            case 2:
                set = new Image("/img/2.png");
                break;
            case 3:
                set = new Image("/img/3.png");
                break;
            case 4:
                set = new Image("/img/4.png");
                break;
            default:
                set = new Image("/img/exit.png");
        }
        aze.setImage(set);
    }
    public void initialize() {
        left.setOnMouseClicked(event -> { if(a-1>0){ a=a-1;start(a); } });
        right.setOnMouseClicked(event -> { if(a+1<=4){ a=a+1;start(a); } });
        left.setOnMouseEntered(event -> {left.setOpacity(1);});
        left.setOnMouseExited(event -> {left.setOpacity(0.8);});
        right.setOnMouseEntered(event -> {right.setOpacity(1);});
        right.setOnMouseExited(event -> {right.setOpacity(0.8);});
    }
}
