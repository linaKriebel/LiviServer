import models.ClientCommand;
import models.Field;
import models.GameItem;
import models.ItemType;

import java.util.Stack;

public class PathFinder {

    private GameItem[][] gameField;
    private int height;
    private int width;

    private Stack<ClientCommand> stack = new Stack();

    private int[][] map;
    private final int TRIED = 1;
    private final int PATH = 2;

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

    private boolean isValid(int x, int y) {
        if (isOnGameField(x, y) && isOpen(x, y) && !isTried(x, y)) {
            return true;
        }
        return false;
    }

    private boolean isOpen(int x, int y) {
        if(gameField[x][y] != null){
            if(gameField[x][y].getId() == 0 && gameField[x][y].getType() == ItemType.PLAYER) {
                return true;
            }
        }
        return gameField[x][y] == null;
    }

    private boolean isTried(int x, int y) {
        return map[x][y] == TRIED;
    }

    private boolean isOnGameField(int x, int y) {
        return x >= 0 && x < height && y >= 0 && y < width;
    }
}
