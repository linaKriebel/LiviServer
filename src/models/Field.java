package models;

import java.io.Serializable;

public class Field implements Serializable {

    public int x;
    public int y;

    public Field(int x, int y){
        this.x = x;
        this.y = y;
    }
}
