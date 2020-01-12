import models.*;
import java.util.List;
import java.util.Stack;

public class AI implements Runnable {

    private Server server;
    private final int ID = 0;
    private List<GameItem> balls;

    public volatile boolean running = true;

    public AI(Server server) {
        this.server = server;
        this.balls = server.getWorld().balls;
    }

    @Override
    public void run() {

        PathFinder test = new PathFinder(server.getWorld().gameField);
        Stack<ClientCommand> stack = test.findPath(server.getWorld().getPosition(ItemType.PLAYER, ID), server.getWorld().balls.get(0).getCoordinates());

        while (running) {

            try {
                // determines speed of AI
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ClientCommand direction = null;

            if(!stack.isEmpty()) {
                direction = stack.pop();
            } else {
                try{
                    balls.remove(0);
                } catch (IndexOutOfBoundsException e) {
                    // ignore
                }

                if (balls.isEmpty()) {
                    //reload all balls from the server world
                    balls = server.getWorld().balls;
                } else {
                    //if there are balls in the game, move towards one of them
                    GameItem ball = balls.get(0);
                    stack = test.findPath(server.getWorld().getPosition(ItemType.PLAYER, ID), ball.getCoordinates());
                }
            }

            if (direction != null) {
                server.handle(direction, ID);
            }
        }
    }

    private ClientCommand calculateDirection(GameItem ballToChase) {

        World world = server.getWorld();

        Field currentPosition = world.getPosition(ItemType.PLAYER, ID);
        Field ballPosition = world.getPosition(ItemType.BALL, ballToChase.getId());

        ClientCommand direction = null;

        if (currentPosition.y != ballPosition.y) {
            int dist = ballPosition.y - currentPosition.y;

            if (dist > 0) direction = ClientCommand.DOWN;
            if (dist < 0) direction = ClientCommand.UP;
        } else {
            int dist = ballPosition.x - currentPosition.x;

            if (dist > 0) direction = ClientCommand.RIGHT;
            if (dist < 0) direction = ClientCommand.LEFT;
        }

        if (!world.ballCanBeMovedInDirection(ballToChase, direction)) {
            balls.remove(0);
            direction = null;
        }

        return direction;
    }

    public void handleMessage(GameEvent message) {
        if (message.getCommand() == ServerCommand.REMOVE && message.getItemType() == ItemType.BALL) {
            balls = server.getWorld().balls;
        }
    }
}
