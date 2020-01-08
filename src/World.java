import maze.Cell;
import maze.MazeGenerator;
import models.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private List<Player> registeredPlayers;

    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    private GameItem[][] gameField ; // world grid coordinates
    private final Color[] colors = {null, Color.MAGENTA, Color.YELLOW, Color.RED, Color.ORANGE};
    private int idCount = 0;

    public List<GameItem> players = new ArrayList<>();
    public List<GameItem> balls  = new ArrayList<>();
    public List<GameItem> obstacles = new ArrayList<>();
    public List<GameItem> holes  = new ArrayList<>();

    public World(List<Player> registeredPlayers) {
        this.registeredPlayers = registeredPlayers;
        this.gameField = new GameItem[WIDTH][HEIGHT];
    }

    public void generate(){

        // generate random maze and set obstacles

        MazeGenerator mazeGenerator = new MazeGenerator(WIDTH/4,WIDTH/4);
        Cell[][] board = mazeGenerator.carve();

        List<GameItem> newObstacles = new ArrayList<>();

        for(int x=0; x<WIDTH/4; x++){
            for(int y=0; y<WIDTH/4; y++){

                Cell currentCell = board[x][y];

                int fieldX = x*4;
                int fieldY = y*4;

                if (y > 0 && currentCell.northWall) {
                    GameItem obstacle1 = new GameItem(ItemType.OBSTACLE, 0, new Field(fieldX, fieldY), Color.GRAY);
                    newObstacles.add(obstacle1);
                    gameField[fieldX][fieldY] = obstacle1;

                    GameItem obstacle2 = new GameItem(ItemType.OBSTACLE, 0, new Field(fieldX+1, fieldY), Color.GRAY);
                    newObstacles.add(obstacle2);
                    gameField[fieldX+1][fieldY] = obstacle2;

                    GameItem obstacle3 = new GameItem(ItemType.OBSTACLE, 0, new Field(fieldX+2, fieldY), Color.GRAY);
                    newObstacles.add(obstacle3);
                    gameField[fieldX+2][fieldY] = obstacle3;

                    GameItem obstacle4 = new GameItem(ItemType.OBSTACLE, 0, new Field(fieldX+3, fieldY), Color.GRAY);
                    newObstacles.add(obstacle4);
                    gameField[fieldX+3][fieldY] = obstacle4;
                }

                if (x > 0 && currentCell.westWall) {
                    GameItem obstacle1 = new GameItem(ItemType.OBSTACLE, 0, new Field(fieldX, fieldY), Color.GRAY);
                    newObstacles.add(obstacle1);
                    gameField[fieldX][fieldY] = obstacle1;

                    GameItem obstacle2 = new GameItem(ItemType.OBSTACLE, 0, new Field(fieldX, fieldY+1), Color.GRAY);
                    newObstacles.add(obstacle2);
                    gameField[fieldX][fieldY+1] = obstacle2;

                    GameItem obstacle3 = new GameItem(ItemType.OBSTACLE, 0, new Field(fieldX, fieldY+2), Color.GRAY);
                    newObstacles.add(obstacle3);
                    gameField[fieldX][fieldY+2] = obstacle3;

                    GameItem obstacle4 = new GameItem(ItemType.OBSTACLE, 0, new Field(fieldX, fieldY+3), Color.GRAY);
                    newObstacles.add(obstacle4);
                    gameField[fieldX][fieldY+3] = obstacle4;
                }
            }
        }
        obstacles.addAll(newObstacles);


        // create surrounding border obstacles (static)

        for(int x = 0; x< WIDTH; x++){
            GameItem obstacle1 = new GameItem(ItemType.OBSTACLE, 0, new Field(x, 0), Color.GRAY);
            obstacles.add(obstacle1);
            gameField[x][0] = obstacle1;

            GameItem obstacle2 = new GameItem(ItemType.OBSTACLE, 0, new Field(x, HEIGHT-1), Color.GRAY);
            obstacles.add(obstacle2);
            gameField[x][HEIGHT-1] = obstacle2;
        }

        for(int y = 0; y< HEIGHT; y++){
            GameItem obstacle1 = new GameItem(ItemType.OBSTACLE, 0, new Field(0,y), Color.GRAY);
            obstacles.add(obstacle1);
            gameField[0][y] = obstacle1;

            GameItem obstacle2 = new GameItem(ItemType.OBSTACLE, 0, new Field(WIDTH-1, y), Color.GRAY);
            obstacles.add(obstacle2);
            gameField[WIDTH-1][y] = obstacle2;
        }


        // create ai and players

        Field aiStartingField = getRandomFreeField();
        GameItem ai = new GameItem(ItemType.PLAYER, 0, aiStartingField, Color.CYAN);
        players.add(ai);
        gameField[aiStartingField.x][aiStartingField.y] = ai;

        // create only registered players
        for (Player registeredPlayer : registeredPlayers){
            Field playerStartingField = getRandomFreeField();
            GameItem player = new GameItem(ItemType.PLAYER, registeredPlayer.getId(), playerStartingField, colors[registeredPlayer.getId()]);
            players.add(player);
            gameField[playerStartingField.x][playerStartingField.y] = player;
        }

        // create balls
        int numberOfBalls = registeredPlayers.size() * 2;
        for(int i=0; i<numberOfBalls; i++) {
            Field ballStartingField = getRandomFreeBallField();
            GameItem ball = new GameItem(ItemType.BALL, idCount, ballStartingField, Color.WHITE);
            balls.add(ball);
            gameField[ballStartingField.x][ballStartingField.y] = ball;
            idCount++;
        }

        idCount = 0; // reset


        // create holes

        int numberOfHoles = registeredPlayers.size();
        for(int i=0; i<numberOfHoles; i++) {
            Field holeField = getRandomFreeField();
            GameItem hole = new GameItem(ItemType.HOLE, idCount, holeField, Color.GREEN);
            holes.add(hole);
            gameField[holeField.x][holeField.y] = hole;
            idCount++;
        }
    }

    private Field getRandomFreeField(){
        Random rand = new Random();
        int x = rand.nextInt(WIDTH);
        int y = rand.nextInt(HEIGHT);

        while(gameField[x][y] != null) {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
        }

        return new Field(x,y);
    }

    private Field getRandomFreeBallField(){
        Random rand = new Random();
        boolean checkNeighbors = false;
        int x = 0;
        int y = 0;

        while (!checkNeighbors) {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
            if (gameField[x][y] == null && gameField[x+1][y] == null && gameField[x-1][y] == null && gameField[x][y+1] == null && gameField[x][y-1] == null) {
                checkNeighbors = true;
            }

        }
        return new Field(x,y);
    }

    public List<GameEvent> processMove(int playerId, ClientCommand direction) {
        GameItem player = getGameItem(ItemType.PLAYER, playerId);

        Field currentPosition = getPosition(ItemType.PLAYER, playerId); //the position the player is currently on
        Field newPosition = getNextField(currentPosition, direction); //the next field in the given direction, null if outside the gameField

        return move(currentPosition, newPosition, direction, player);
    }

    private List<GameEvent> move(Field currentPlayerPosition, Field newPlayerPosition, ClientCommand direction, GameItem player){
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
                                // ball is in hole, remove ball from field
                                GameItem ball = gameField[newPlayerPosition.x][newPlayerPosition.y];
                                updateGameField(newPlayerPosition, potentialBallPosition, null);
                                balls.remove(ball);
                                playerPosition = newPlayerPosition;

                                gameEvents.add(new GameEvent(ServerCommand.REMOVE, ball.getId(), ball.getType()));
                                gameEvents.add(new GameEvent(ServerCommand.SCORE, player.getId(), player.getType()));

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

                            gameEvents.add(new GameEvent(ServerCommand.MOVE, ball.getId(), ball.getType(), potentialBallPosition));
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
            //player stands at the border and can not move
            playerPosition = currentPlayerPosition;
        }

        updateGameField(currentPlayerPosition, playerPosition, player);
        gameEvents.add(new GameEvent(ServerCommand.MOVE , player.getId(), player.getType(), playerPosition));

        return gameEvents;
    }

    private void updateGameField(Field oldPos, Field newPos, GameItem object) {
        gameField[oldPos.x][oldPos.y] = null;
        if (object != null) {
            gameField[newPos.x][newPos.y] = object;
        }
    }

    public Field getPosition(ItemType type, int id) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
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
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
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

    private Field getNextField(Field position, ClientCommand direction){
        Field nextField = null;

        switch(direction){
            case LEFT:
                if (position.x > 0) nextField = new Field(position.x -1, position.y);
                break;
            case RIGHT:
                if (position.x < WIDTH -1) nextField = new Field(position.x +1, position.y);
                break;
            case UP:
                if (position.y > 0) nextField = new Field(position.x, position.y -1);
                break;
            case DOWN:
                if (position.y < HEIGHT -1) nextField = new Field(position.x, position.y +1);
                break;
        }
        //return null, if the next field would be outside the gameField
        return nextField;

    }

    public boolean ballCanBeMovedInDirection(GameItem ball, ClientCommand direction) {

        boolean ballCanBeMoved;

        Field ballPosition = getPosition(ball.getType(), ball.getId());
        Field potentialBallPosition = getNextField(ballPosition, direction);

        if (potentialBallPosition != null) {
            //ball can be moved, if the field is not occupied
            GameItem potentialBallField = gameField[potentialBallPosition.x][potentialBallPosition.y];
            if (potentialBallField != null) {

                if (potentialBallField.getType() == ItemType.HOLE) {
                    ballCanBeMoved = true;
                } else {
                    //field is occupied, player and ball can not be moved
                    ballCanBeMoved = false;
                }
            } else {
                ballCanBeMoved = true;
            }
        } else {
            ballCanBeMoved = false;
        }

        return ballCanBeMoved;
    }

    private void random(){
        final int OBSTACLE_NUMBER = 12;

        for (int i = 0; i < OBSTACLE_NUMBER; i++) {
            //Get random position for the next bomb
            Random rand = new Random();
            int row = rand.nextInt(WIDTH);
            int col = rand.nextInt(HEIGHT);
            while(gameField[row][col] != null) { //if this position is a bomb
                //we get new position
                row = rand.nextInt(WIDTH);
                col = rand.nextInt(HEIGHT);
            }
            GameItem obstacle = new GameItem(ItemType.OBSTACLE, idCount, new Field(row, col), Color.GRAY);
            gameField[row][col] = obstacle;
            obstacles.add(obstacle);
        }
    }
}
