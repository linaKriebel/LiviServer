package models;

import java.awt.*;
import java.io.Serializable;

public class GameItem implements Serializable {

    private ItemType type;
    private int id;
    private Field coordinates;
    private Color color;
    private int score;

    public GameItem(ItemType type, int id, Field coordinates, Color color) {
        this.type = type;
        this.id = id;
        this.coordinates = coordinates;
        this.color = color;
        this.score = 0;
    }

    public ItemType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public Field getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Field coordinates) {
        this.coordinates = coordinates;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getScore() {
        return score;
    }

    public void score() {
        this.score += 1;
    }
}
