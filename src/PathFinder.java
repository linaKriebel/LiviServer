import models.ClientCommand;
import models.Field;
import models.GameItem;
import models.ItemType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class PathFinder {

    final static int TRIED = 2;
    final static int PATH = 3;

    public static void main(String[] args) {
        List<Player> ai = new ArrayList<>();
        ai.add(new Player(0));
        World world = new World(ai);
        world.generate();

        PathFinder maze = new PathFinder(world.gameField);
        maze.findPath(new Field(1,1), world.balls.get(0).getCoordinates());
        System.out.println(maze.toString());
    }

    private GameItem[][] gameField;
    private int height;
    private int width;

    private int[][] map;
    private Stack<ClientCommand> stack = new Stack();

    public PathFinder(GameItem[][] gameField) {
        this.gameField = gameField;
        this.height = gameField.length;
        this.width = gameField[0].length;

        this.map = new int[height][width];
    }

    public Stack<ClientCommand> findPath(Field from, Field to) {
        traverse(from, to);
        return stack;
    }

    private boolean traverse(Field start, Field target) {

        int x = start.x;
        int y = start.y;

        if (!isValid(x, y)) {
            return false;
        }

        if (isEnd(start, target)) {
            map[x][y] = PATH;
            return true;
        } else {
            map[x][y] = TRIED;
        }

        if (traverse(new Field(x - 1, y), target)) {
            map[x - 1][y] = PATH;
            stack.push(ClientCommand.LEFT);
            return true;
        }

        if (traverse(new Field(x, y + 1), target)) {
            map[x][y + 1] = PATH;
            stack.push(ClientCommand.DOWN);
            return true;
        }

        if (traverse(new Field(x + 1, y), target)) {
            map[x + 1][y] = PATH;
            stack.push(ClientCommand.RIGHT);
            return true;
        }

        if (traverse(new Field(x, y - 1), target)) {
            map[x][y - 1] = PATH;
            stack.push(ClientCommand.UP);

            return true;
        }

        return false;
    }

    private boolean isEnd(Field current, Field target) {
        return current.x == target.x - 1 && current.y == target.y - 1;
    }

    private boolean isValid(int i, int j) {
        if (inRange(i, j) && isOpen(i, j) && !isTried(i, j)) {
            return true;
        }

        return false;
    }

    private boolean isOpen(int i, int j) {
        if(gameField[i][j] != null){
            if(gameField[i][j].getId() == 0 && gameField[i][j].getType() == ItemType.PLAYER) {
                return true;
            }
        }

        return gameField[i][j] == null;
    }

    private boolean isTried(int i, int j) {
        return map[i][j] == TRIED;
    }

    private boolean inRange(int i, int j) {
        return inHeight(i) && inWidth(j);
    }

    private boolean inHeight(int i) {
        return i >= 0 && i < height;
    }

    private boolean inWidth(int j) {
        return j >= 0 && j < width;
    }

    public String toString() {
        String s = "";
        for (int[] row : map) {
            s += Arrays.toString(row) + "\n";
        }

        return s;
    }
}
