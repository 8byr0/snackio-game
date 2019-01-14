package fr.esigelec.snackio.ui;
import com.badlogic.gdx.Gdx;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import com.badlogic.gdx.Input;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class ControllerConfig {
    @FXML public TextField leftT;
    int left_key;
    int right_key;
    int up_key;
    int down_key;

    public ControllerConfig(){
        left_key=Input.Keys.LEFT;
        right_key=Input.Keys.RIGHT;
        up_key=Input.Keys.UP;
        down_key=Input.Keys.DOWN;
    }
    public void initialize() {
        leftT.setOnKeyPressed(this::setLeft);
    }

    public int getLeft()  {
        return left_key;
    }

    public void setLeft(KeyEvent e)  {
        System.out.println( e.getCode());
        if (e.getCode().toString()=="ENTER"){
            leftT.setDisable(true);
        }
        else {
            leftT.setEditable(false);
            leftT.setText(e.getCode().toString());
        }

    }

}
