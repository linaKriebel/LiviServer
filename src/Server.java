import models.ClientCommand;
import models.ServerCommand;
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
    private AI ai;
    private ExecutorService executorService;

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
            events.add(new GameEvent(ServerCommand.START));
            startAI();
        } else if (message == ClientCommand.EXIT) {
            Player playerToRemove = getPlayerById(id);
            players.remove(playerToRemove);
            events.add(new GameEvent(ServerCommand.EXIT, id, ItemType.PLAYER));
        } else if (message == ClientCommand.COUNTDOWN) {
            events.add(new GameEvent(ServerCommand.END));
            players.clear();
            stopAI();
        } else {
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
        ai = new AI(this);
        executorService.execute(ai);
    }

    private void stopAI() {
        ai.running = false;
    }

    public World getWorld() {
        return world;
    }

    private Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

}
