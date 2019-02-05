package fr.esigelec.snackio.ui;
import fr.esigelec.snackio.game.character.motion.KeyboardController;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.badlogic.gdx.Input;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.Duration;



public class ControllerConfig {
    @FXML private TextField left_button_textfield,right_button_textfield,up_button_textfield,down_button_textfield;
    @FXML private ImageView upImageView;
    @FXML private ImageView downImageView;


    public void getUpBottonTimeline(){
        KeyFrame kf1 = new KeyFrame(Duration.seconds(1), new KeyValue(upImageView.opacityProperty(), 0));
        KeyFrame kf2 = new KeyFrame(Duration.seconds(1), new KeyValue(upImageView.opacityProperty(), 1));
        Timeline uptimelineOn = new Timeline(kf1,kf2);
        uptimelineOn.setCycleCount(1);
        kf1 = new KeyFrame(Duration.seconds(1), new KeyValue(downImageView.opacityProperty(), 0));
        kf2 = new KeyFrame(Duration.seconds(1), new KeyValue(downImageView.opacityProperty(), 1));
        Timeline downtimelineOn = new Timeline(kf1,kf2);
        downtimelineOn = new Timeline(kf1, kf2);
        downtimelineOn.setCycleCount(1);
        //downtimelineOn.setAutoReverse(true);
    }
    public void initialize() {
        left_button_textfield.setOnKeyPressed(this::leftConfig);
        right_button_textfield.setOnKeyPressed(this::rightConfig);
        down_button_textfield.setOnKeyPressed(this::downConfig);
        up_button_textfield.setOnKeyPressed(this::upConfig);
        if(Input.Keys.toString(KeyboardController.getLeftButton())!="Unknown"){left_button_textfield.setText(Input.Keys.toString(KeyboardController.getLeftButton()));}
        if(Input.Keys.toString(KeyboardController.getRightButton())!="Unknown"){right_button_textfield.setText(Input.Keys.toString(KeyboardController.getRightButton()));}
        if(Input.Keys.toString(KeyboardController.getDownButton())!="Unknown"){down_button_textfield.setText(Input.Keys.toString(KeyboardController.getDownButton()));}
        if(Input.Keys.toString(KeyboardController.getUpButton())!="Unknown"){up_button_textfield.setText(Input.Keys.toString(KeyboardController.getUpButton()));}
        right_button_textfield.setEditable(false);
        down_button_textfield.setEditable(false);
        up_button_textfield.setEditable(false);
        left_button_textfield.setEditable(false);
    }

    public Map initHashMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        for(int i=7;i<=54;i++){
            map.put(Input.Keys.toString(i),i);
        }
        return map;

    }

    public void leftConfig(KeyEvent e) {
        Map map = initHashMap();
        if (map.containsKey(e.getCode().getName())) {
            left_button_textfield.setText(e.getCode().getName());
            KeyboardController.setLeftButton((Integer) map.get(left_button_textfield.getText()));
        }
    }
    public void rightConfig(KeyEvent e)  {

        Map map = initHashMap();
        if (map.containsKey(e.getCode().getName())) {
            right_button_textfield.setText(e.getCode().getName());
            KeyboardController.setRightButton((Integer) map.get(right_button_textfield.getText()));
        }
    }
    public void downConfig(KeyEvent e)  {
        Map map = initHashMap();
        if (map.containsKey(e.getCode().getName())) {
            down_button_textfield.setText(e.getCode().getName());
            KeyboardController.setDownButton((Integer) map.get(down_button_textfield.getText()));
        }
    }
    public void upConfig(KeyEvent e)  {
        Map map = initHashMap();
        if (map.containsKey(e.getCode().getName())) {
            up_button_textfield.setText(e.getCode().getName());
            KeyboardController.setUpButton((Integer) map.get(up_button_textfield.getText()));
        }
    }


}
