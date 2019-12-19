import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World {

    private final int width = 20;
    private final int height = 20;

    private GameItem[][] gameField ; //in world grid coordinates

    public World() {
        gameField = new GameItem[width][height];

        GameItem ai = new GameItem(ItemType.PLAYER, 0);
        GameItem player1 = new GameItem(ItemType.PLAYER, 1);
        GameItem player2 = new GameItem(ItemType.PLAYER, 2);

        GameItem ball1 = new GameItem(ItemType.BALL, 1);
        GameItem ball2 = new GameItem(ItemType.BALL, 2);

        GameItem hole1 = new GameItem(ItemType.HOLE, 1);
        GameItem hole2 = new GameItem(ItemType.HOLE, 2);

        int idCount = 0;
        for(int x=0; x<width; x++){
            GameItem obstacle1 = new GameItem(ItemType.OBSTACLE, idCount);
            idCount++;
            GameItem obstacle2 = new GameItem(ItemType.OBSTACLE, idCount);
            idCount++;

            gameField[x][0] = obstacle1;
            gameField[x][height-1] = obstacle2;
        }

        for(int y=0; y<height; y++){
            GameItem obstacle1 = new GameItem(ItemType.OBSTACLE, idCount);
            idCount++;
            GameItem obstacle2 = new GameItem(ItemType.OBSTACLE, idCount);
            idCount++;

            gameField[0][y] = obstacle1;
            gameField[width-1][y] = obstacle2;
        }
        GameItem obstacle1 = new GameItem(ItemType.OBSTACLE, 1);

        //set up the level
        gameField[1][1] = ai;
        gameField[2][2] = player1; //TODO randomize starting position for player 1
        gameField[6][6] = player2; //TODO randomize starting position for player 2
        gameField[7][8] = ball1;
        gameField[3][3] = ball2;
        gameField[5][5] = obstacle1;
        gameField[10][10] = hole1;
        gameField[15][15] = hole2;
    }


    public List<GameEvent> processMove(int playerId, String direction) {
        GameItem player = getGameItem(ItemType.PLAYER, playerId);
        Direction dir = Direction.valueOf(direction.toUpperCase());

        Field currentPosition = getPosition(ItemType.PLAYER, playerId); //the position the player is currently on
        Field newPosition = getNextField(currentPosition, dir); //the next field in the given direction, null if outside the gameField

        return move(currentPosition, newPosition, dir, player);
    }

    private List<GameEvent> move(Field currentPlayerPosition, Field newPlayerPosition, Direction direction, GameItem player){
        List<GameEvent> gameEvents = new ArrayList<>();
        Field playerPosition = null;

        if(newPlayerPosition != null) {
            System.out.println("Next field: " + newPlayerPosition.x + ", " + newPlayerPosition.y);

            //next field exists

            if (gameField[newPlayerPosition.x][newPlayerPosition.y] != null) {

                //there is something next to the player in the movement direction
                GameItem itemInPlayerMovementDirection = gameField[newPlayerPosition.x][newPlayerPosition.y];
                //find out what
                ItemType type = itemInPlayerMovementDirection.getType();

                if (type.equals(ItemType.PLAYER) || type.equals(ItemType.OBSTACLE) || type.equals(ItemType.HOLE)) {
                    //field is occupied, no movement possible
                    playerPosition = currentPlayerPosition;
                }

                if (type.equals(ItemType.BALL)) {
                    Field potentialBallPosition = getNextField(newPlayerPosition, direction);

                    //check if the ball would be moved out of the gameField
                    if (potentialBallPosition != null) {
                        //ball can be moved, if the field is not occupied
                        GameItem potentialBallField = gameField[potentialBallPosition.x][potentialBallPosition.y];
                        if ( potentialBallField != null ) {

                            if (potentialBallField.getType() == ItemType.HOLE) {
                                // delete ball
                                GameItem ball = gameField[newPlayerPosition.x][newPlayerPosition.y];
                                updateGameField(newPlayerPosition, potentialBallPosition, null);
                                playerPosition = newPlayerPosition;
                                gameEvents.add(new GameEvent(Command.REMOVE, ball.getId(), ball.getType()));
                                gameEvents.add(new GameEvent(Command.SCORE, player.getId(), player.getType()));

                            } else {
                                //field is occupied, player and ball can not be moved
                                playerPosition = currentPlayerPosition;

                            }
                        }

                        else if (potentialBallField == null){
                            //field is free, move ball and player to their new positions
                            GameItem ball = gameField[newPlayerPosition.x][newPlayerPosition.y];
                            playerPosition = newPlayerPosition;
                            updateGameField(newPlayerPosition, potentialBallPosition, ball);

                            gameEvents.add(new GameEvent(Command.MOVE, ball.getId(), ball.getType(), potentialBallPosition));
                        }
                    } else {
                        //ball can not be moved --> player can not move either
                        playerPosition = currentPlayerPosition;
                    }
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
        gameEvents.add(new GameEvent(Command.MOVE , player.getId(), player.getType(), playerPosition));


        return gameEvents;
    }

    private void updateGameField(Field oldPos, Field newPos, GameItem object) {
        gameField[oldPos.x][oldPos.y] = null;
        if (object != null) {
            gameField[newPos.x][newPos.y] = object;
        }
    }

    public Field getPosition(ItemType type, int id) {
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
