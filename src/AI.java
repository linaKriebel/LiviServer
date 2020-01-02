import models.*;
import java.util.List;

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

        while (running) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ClientCommand direction = null;

            if (!balls.isEmpty()) {
                //if there are balls in the game, move towards one of them
                GameItem ball = balls.get(0);
                direction = calculateDirection(ball);
            } else {
                //reload all balls from the server world
                balls = server.getWorld().balls;
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

        if (world.ballCanBeMovedInDirection(ballToChase, direction)) {
            return direction;
        } else {
            balls.remove(0);
            return null;
        }
    }

    public void handleMessage(GameEvent message) {
        if (message.getCommand() == ServerCommand.REMOVE) {
            balls = server.getWorld().balls;
        }
    }
}
