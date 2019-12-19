package models;

public class GameItem {

    private ItemType type;
    private int id;

    public GameItem(ItemType type, int id) {
        this.type = type;
        this.id = id;
    }

    public ItemType getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
