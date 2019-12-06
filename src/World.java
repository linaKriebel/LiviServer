public class World {

    private final int width = 10;
    private final int height = 10;

    private GameItem[][] gameField = new GameItem[width][height]; //in gameField coordinates

    public Coordinate processMove(int playerId, String direction) {
        GameItem player = new GameItem(ItemType.PLAYER, playerId);

        Coordinate position = getPosition(ItemType.PLAYER, playerId);
        Coordinate old = position;
        Coordinate newPos = old;
        ItemType type;

        switch (direction) {
            case "left":
                //TODO check left border
                position.x -= 1;
                type = gameField[position.x][position.y].getType();

                if (gameField[position.x][position.y] != null) {
                    newPos = approveMovement(position, type);
                } else {
                    updateGameField(old, position, player);
                }
                break;
            case "right":
                //TODO check right border
                position.x += 1;
                type = gameField[position.x][position.y].getType();

                if (gameField[position.x][position.y] != null) {
                    newPos = approveMovement(position, type);
                } else {
                    updateGameField(old, position, player);
                }
                break;
            case "up":
                //TODO check bottom
                position.y += 1;
                type = gameField[position.x][position.y].getType();

                if (gameField[position.x][position.y] != null) {
                    newPos = approveMovement(position, type);
                } else {
                    updateGameField(old, position, player);
                }
                break;
            case "down":
                //TODO check top
                position.y -= 1;
                type = gameField[position.x][position.y].getType();

                if (gameField[position.x][position.y] != null) {
                    newPos = approveMovement(position, type);
                } else {
                    updateGameField(old, position, player);
                }
                break;
        }

        return newPos;

    }

    private Coordinate approveMovement(Coordinate position, ItemType type) {
        Coordinate toReturn= null;

        if (type.equals(ItemType.PLAYER)) {
            // cant move
            toReturn = position;
        }
        if (type.equals(ItemType.BALL)) {
            // can ball be moved or not -> can player be moved or not
        }
        if (type.equals(ItemType.OBSTACLE)) {
            //cant move
            toReturn = position;
        }

        return toReturn;

    }

    private void updateGameField(Coordinate oldPos, Coordinate newPos, GameItem object) {
        gameField[oldPos.x][oldPos.y] = null;
        gameField[newPos.x][newPos.y] = object;
    }

    private Coordinate getPosition(ItemType type, int id) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (gameField[x][y] != null) {
                    if (gameField[x][y].getType() == type && gameField[x][y].getId() == id) {
                        return new Coordinate(x, y);
                    }
                }
            }
        }
        return null;
    }

}
