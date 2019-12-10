import java.util.HashMap;

public class World {

    private final int width = 20;
    private final int height = 20;

    private GameItem[][] gameField ; //in world grid coordinates

    public World() {
        gameField = new GameItem[width][height];

        GameItem player1 = new GameItem(ItemType.PLAYER, 1);
        GameItem player2 = new GameItem(ItemType.PLAYER, 2);

        GameItem ball1 = new GameItem(ItemType.BALL, 1);
        GameItem ball2 = new GameItem(ItemType.BALL, 2);

        GameItem obstacle1 = new GameItem(ItemType.OBSTACLE, 1);

        //set up the level
        gameField[2][2] = player1; //starting position for player 1
        gameField[6][6] = player2; //starting position for player 2
        gameField[7][8] = ball1;
        gameField[3][3] = ball2;
        gameField[5][5] = obstacle1;
    }


    public HashMap<GameItem, Field> processMove(int playerId, String direction) {
        GameItem player = getGameItem(ItemType.PLAYER, playerId);
        Direction dir = Direction.valueOf(direction.toUpperCase());

        Field currentPosition = getPosition(ItemType.PLAYER, playerId); //the position the player is currently on
        Field newPosition = getNextField(currentPosition, dir); //the position the player will move to

        //TODO register player event
        return move(currentPosition, newPosition, dir, player);
    }

    private HashMap<GameItem,Field> move(Field currentPlayerPosition, Field newPlayerPosition, Direction direction, GameItem player){
        HashMap<GameItem, Field> updatedPositions = new HashMap<>();
        Field playerPosition = null;

        if(newPlayerPosition != null) {
            System.out.println("Next field: " + newPlayerPosition.x + ", " + newPlayerPosition.y);

            //next field exists

            if (gameField[newPlayerPosition.x][newPlayerPosition.y] != null) {

                //there is something next to the player in the movement direction
                GameItem itemInPlayerMovementDirection = gameField[newPlayerPosition.x][newPlayerPosition.y];
                //find out what
                ItemType type = itemInPlayerMovementDirection.getType();

                if (type.equals(ItemType.PLAYER)) {
                    //field is occupied by another player, no movement possible
                    playerPosition = currentPlayerPosition;
                }

                if (type.equals(ItemType.BALL)) {
                    Field potentialBallPosition = getNextField(newPlayerPosition, direction);

                    //check if the ball would be moved out of the gameField
                    if (potentialBallPosition != null) {

                        //ball can be moved, if the field is not occupied
                        if (gameField[potentialBallPosition.x][potentialBallPosition.y] != null) {
                            //field is occupied, player and ball can not be moved
                            playerPosition = currentPlayerPosition;
                        } else {
                            //field is free, move ball and player to their new positions
                            GameItem ball = gameField[newPlayerPosition.x][newPlayerPosition.y];
                            playerPosition = newPlayerPosition;
                            updateGameField(newPlayerPosition, potentialBallPosition, ball);
                            //TODO register ball event
                            updatedPositions.put(ball, potentialBallPosition);
                        }
                    }
                }

                if (type.equals(ItemType.OBSTACLE)) {
                    //field is an obstacle, no movement possible
                    playerPosition = currentPlayerPosition;
                }
            } else {
                //nothing in the way, the player can move to the next field
                playerPosition = newPlayerPosition;
            }
        } else {
            System.out.println("Next field: border");

            //player stands at the border and can not move
            playerPosition = currentPlayerPosition;
        }

        updateGameField(currentPlayerPosition, playerPosition, player);
        updatedPositions.put(player, playerPosition);

        return updatedPositions;
    }

    private void updateGameField(Field oldPos, Field newPos, GameItem object) {
        gameField[oldPos.x][oldPos.y] = null;
        gameField[newPos.x][newPos.y] = object;
    }

    private Field getPosition(ItemType type, int id) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (gameField[x][y] != null) {
                    if (gameField[x][y].getType() == type && gameField[x][y].getId() == id) {
                        return new Field(x, y);
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

                    GameItem item = gameField[x][y];
                    if (item.getType() == type && item.getId() == id) {
                        return item;
                    }
                }
            }
        }

        //the item is not in the game (anymore)
        return null;
    }

    private Field getNextField(Field position, Direction direction){
        Field nextField = null;

        switch(direction){
            case LEFT:
                if (position.x > 0) nextField = new Field(position.x -1, position.y);
                break;
            case RIGHT:
                if (position.x < width-1) nextField = new Field(position.x +1, position.y);
                break;
            case UP:
                if (position.y > 0) nextField = new Field(position.x, position.y -1);
                break;
            case DOWN:
                if (position.y < height-1) nextField = new Field(position.x, position.y +1);
                break;
        }
        //return null, if the next field would be outside the gameField
        return nextField;

    }
}