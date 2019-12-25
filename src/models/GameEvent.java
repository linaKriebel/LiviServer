package models;

import java.io.Serializable;

public class GameEvent implements Serializable {

    private Command command;
    private int itemId;
    private ItemType itemType;
    private Field field;

    public GameEvent(Command command) {
        this.command = command;
    }

    public GameEvent(Command command, int itemId, ItemType itemType, Field field) {
        this.command = command;
        this.itemId = itemId;
        this.itemType = itemType;
        this.field = field;
    }

    public GameEvent(Command command, int itemId, ItemType itemType) {
        this.command = command;
        this.itemId = itemId;
        this.itemType = itemType;
    }

    public Command getCommand() {
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
}
