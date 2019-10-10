package sample;


import javafx.scene.control.Button;

public class Embryo  {

    private int Id;
    private int state;


    public Embryo(int id, String color, int state) {
        Id = id;
        this.state = state;
    }


    public void setId(int id) {
        Id = id;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
