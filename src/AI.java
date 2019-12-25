import models.ClientCommands;
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

    private ClientCommands calculateDirection(){

        World world = server.getWorld();
        Field currentPosition = world.getPosition(ItemType.PLAYER, ID);
        Field ballPosition = world.getPosition(ItemType.BALL, 1);

        ClientCommands direction = ClientCommands.RIGHT;

        if(currentPosition.y != ballPosition.y){
            int dist = ballPosition.y - currentPosition.y;

            if(dist > 0) direction = ClientCommands.DOWN;
            if(dist < 0) direction = ClientCommands.UP;
        } else {
            int dist = ballPosition.x - currentPosition.x;

            if(dist > 0) direction = ClientCommands.RIGHT;
            if(dist < 0) direction = ClientCommands.LEFT;
        }

        return direction;
    }
}
