package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameEvent implements Serializable {

    private ServerCommand command;
    private int itemId;
    private ItemType itemType;
    private Field field;

    private List<GameItem> players = Collections.synchronizedList(new ArrayList<>());
    private List<GameItem> balls = Collections.synchronizedList(new ArrayList<>());
    private List<GameItem> obstacles = Collections.synchronizedList(new ArrayList<>());
    private List<GameItem> holes = Collections.synchronizedList(new ArrayList<>());

    public GameEvent(ServerCommand command) {
        this.command = command;
    }

    public GameEvent(ServerCommand command, int itemId, ItemType itemType, Field field) {
        this.command = command;
        this.itemId = itemId;
        this.itemType = itemType;
        this.field = field;
    }

    public GameEvent(ServerCommand command, int itemId, ItemType itemType) {
        this.command = command;
        this.itemId = itemId;
        this.itemType = itemType;
    }

    public GameEvent(ServerCommand command, List<GameItem> players, List<GameItem> balls, List<GameItem> obstacles, List<GameItem> holes){
        this.command = command;
        this.players = players;
        this.balls = balls;
        this.obstacles = obstacles;
        this.holes = holes;
    }

    public void setPlayers(List<GameItem> players) {
        this.players = players;
    }

    public void setBalls(List<GameItem> balls) {
        this.balls = balls;
    }

    public void setObstacles(List<GameItem> obstacles) {
        this.obstacles = obstacles;
    }

    public void setHoles(List<GameItem> holes) {
        this.holes = holes;
    }

    public ServerCommand getCommand() {
        return command;
    }

    public int getItemId() {
        return itemId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Field getField() {
        return field;
    }

    public List<GameItem> getPlayers() {
        return players;
    }

    public List<GameItem> getBalls() {
        return balls;
    }

    public List<GameItem> getObstacles() {
        return obstacles;
    }

    public List<GameItem> getHoles() {
        return holes;
    }
}
