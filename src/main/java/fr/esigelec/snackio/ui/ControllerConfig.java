package fr.esigelec.snackio.ui;
import fr.esigelec.snackio.game.character.motion.KeyboardController;
import fr.esigelec.snackio.networking.client.SnackioNetClient;
import javafx.scene.input.KeyEvent;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import com.badlogic.gdx.Input;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class ControllerConfig {
    @FXML public TextField rig_button_textfield,left_button_textfield,right_button_textfield,up_button_textfield,down_button_textfield;

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
