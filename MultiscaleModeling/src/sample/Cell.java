package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;


public class Cell {

    private  boolean  state;
    private Color color;
    boolean ifNb;

    public  Cell(){

    // this.color = Color.BLACK;
        this.state = false;

    }

    public  Cell(boolean state, Color color){

        this.state = state;
        this.color = color;
        ifNb= false;

    }

    public boolean isIfNb() {
        return ifNb;
    }

    public void setIfNb(boolean ifNb) {
        this.ifNb = ifNb;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Color getColor() {

        return color;

    }

    public void setColor(Color color) {
        this.color = color;

       // this.setFill(color);
        //this.setColor(color);
        //this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
