package models;

import java.io.Serializable;

public class GameEvent implements Serializable {

    private ServerCommand command;
    private int itemId;
    private ItemType itemType;
    private Field field;

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
}
