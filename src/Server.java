import models.ClientCommand;
import models.Command;
import models.GameEvent;
import models.ItemType;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket server;
    private List<Player> players;
    private World world;
    ExecutorService executorService;


    public Server(int port) {
        players = new ArrayList<>();
        world = new World();
        int i = 1;
        try {
            server = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(200);
            while (true) {
                //players
                Player player = new Player(this, server.accept(), i);
                executorService.execute(player);
                players.add(player);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void handle(ClientCommand message, int id) {
        List<GameEvent> events = new ArrayList<>();
        if (message == ClientCommand.START) {
            events.add(new GameEvent(Command.START));
            startAI();
        } else if (message == ClientCommand.END) {
            Player playerToRemove = getPlayerById(id);
            players.remove(playerToRemove);
            events.add(new GameEvent(Command.END, id, ItemType.PLAYER));
        }
        else {
            events = world.processMove(id, message);
        }
        for (Player player : players) {
            try {
                for (GameEvent event : events) {
                    player.sendMessage(event);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startAI() {
        AI ai = new AI(this);
        executorService.execute(ai);
    }

    public World getWorld() {
        return world;
    }

    private Player getPlayerById (int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

}
