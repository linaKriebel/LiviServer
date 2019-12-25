import models.Command;
import models.GameEvent;

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

    public synchronized void handle(String message, int id) {
        List<GameEvent> events = new ArrayList<>();
        if (message.equals("start")) {
            events.add(new GameEvent(Command.START));
            startAI();
        }else {
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

}
