public class World {

    private final int width = 10;
    private final int height = 10;

    private GameItem[][] gameField ; //in gameField coordinates

    public World() {
        gameField = new GameItem[width][height];
        GameItem player1 = new GameItem(ItemType.PLAYER, 1);
        GameItem player2 = new GameItem(ItemType.PLAYER, 2);
        GameItem ball = new GameItem(ItemType.BALL, 1);
        gameField[2][2] = player1;
        gameField[6][6] = player2;
        gameField[7][8] = ball;
    }


    public Coordinate processMove(int playerId, String direction) {
        GameItem player = getGameItem(ItemType.PLAYER, playerId);

        Coordinate position = getPosition(ItemType.PLAYER, playerId);
        Coordinate potentialBallPosition = null;
        Coordinate old = position;
        Coordinate newPos = old;
        ItemType type;

        switch (direction) {
            case "left":
                if (position.x == 0) return old;

                position.x -= 1;
                if (position.x -1 != 0) {
                    // ball is am rand
                    potentialBallPosition = new Coordinate(position.x -1, position.y);
                }

                if (gameField[position.x][position.y] != null) {
                    type = gameField[position.x][position.y].getType();
                    newPos = approveMovement(position, old, type, potentialBallPosition);
                    updateGameField(old, newPos, player);

                } else {
                    updateGameField(old, position, player);
                }
                break;
            case "right":
                if (position.x == width-1) return old;
                position.x += 1;
                if (position.x + 1 != width-1) {
                    // ball is am rand
                    potentialBallPosition = new Coordinate(position.x +1, position.y);
                }

                if (gameField[position.x][position.y] != null) {
                    type = gameField[position.x][position.y].getType();
                    newPos = approveMovement(position,old, type, potentialBallPosition);
                    updateGameField(old, newPos, player);

                } else {
                    updateGameField(old, position, player);
                }
                break;
            case "up":
                if (position.y == height-1) return old;
                position.y -= 1;
                if (position.y != height-1) { potentialBallPosition = new Coordinate(position.x, position.y - 1); }

                if (gameField[position.x][position.y] != null) {
                    type = gameField[position.x][position.y].getType();
                    newPos = approveMovement(position, old, type, potentialBallPosition);
                    updateGameField(old, newPos, player);

                } else {
                    updateGameField(old, position, player);
                }
                break;
            case "down":
                if (position.y == 0) return old;

                position.y += 1;
                if (position.y -1 != 0) {
                    // ball is am rand
                    potentialBallPosition = new Coordinate(position.x , position.y + 1);
                }

                if (gameField[position.x][position.y] != null) {
                    type = gameField[position.x][position.y].getType();
                    newPos = approveMovement(position, old, type, potentialBallPosition);
                    updateGameField(old, newPos, player);

                } else {
                    updateGameField(old, position, player);
                }
                break;
        }

        return newPos;

    }

    private Coordinate approveMovement(Coordinate position, Coordinate oldPosition, ItemType type, Coordinate potentialBallPosition) {
        Coordinate toReturn= null;

        if (type.equals(ItemType.PLAYER)) {
            // cant move
            toReturn = oldPosition;
        }
        if (type.equals(ItemType.BALL)) {
            // can ball be moved or not -> can player be moved or not
            if (potentialBallPosition == null) {
                toReturn = oldPosition;
            }else {
                GameItem ball = gameField[position.x][position.y];
                if(gameField[potentialBallPosition.x][potentialBallPosition.y] != null) {
                    toReturn = position;
                    //TODO send new ball position
                    updateGameField(position, potentialBallPosition, ball);
                }
            }
        }
        if (type.equals(ItemType.OBSTACLE)) {
            //cant move
            toReturn = oldPosition;
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

    private GameItem getGameItem(ItemType type, int id) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (gameField[x][y] != null) {
                    if (gameField[x][y].getType() == type && gameField[x][y].getId() == id) {
                        return gameField[x][y];
                    }
                }
            }
        }
        return null;
    }
}
