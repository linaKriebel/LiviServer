import models.Direction;
import models.Field;
import models.ItemType;

public class AI implements Runnable {

    private Server server;
    private final int ID = 0;

    public AI(Server server){
        this.server = server;
    }


    @Override
    public void run() {

        while(true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String message = calculateDirection().toString();
            server.handle(message, ID);
        }

    }

    private Direction calculateDirection(){

        World world = server.getWorld();
        Field currentPosition = world.getPosition(ItemType.PLAYER, ID);
        Field ballPosition = world.getPosition(ItemType.BALL, 1);

        Direction direction = Direction.RIGHT;

        if(currentPosition.y != ballPosition.y){
            int dist = ballPosition.y - currentPosition.y;

            if(dist > 0) direction = Direction.DOWN;
            if(dist < 0) direction = Direction.UP;
        } else {
            int dist = ballPosition.x - currentPosition.x;

            if(dist > 0) direction = Direction.RIGHT;
            if(dist < 0) direction = Direction.LEFT;
        }

        return direction;
    }
}
