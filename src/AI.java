import models.ClientCommand;
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
            server.handle(calculateDirection(), ID);
        }

    }

    private ClientCommand calculateDirection(){

        World world = server.getWorld();
        Field currentPosition = world.getPosition(ItemType.PLAYER, ID);
        Field ballPosition = world.getPosition(ItemType.BALL, 1);

        ClientCommand direction = ClientCommand.RIGHT;

        if(currentPosition.y != ballPosition.y){
            int dist = ballPosition.y - currentPosition.y;

            if(dist > 0) direction = ClientCommand.DOWN;
            if(dist < 0) direction = ClientCommand.UP;
        } else {
            int dist = ballPosition.x - currentPosition.x;

            if(dist > 0) direction = ClientCommand.RIGHT;
            if(dist < 0) direction = ClientCommand.LEFT;
        }

        return direction;
    }
}
