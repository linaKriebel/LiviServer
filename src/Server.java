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
        world = new World(players);
        int i = 1;
        try {
            server = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(200);
            while (true) {
                Player player = new Player(this, server.accept(), i);
                executorService.execute(player);
                players.add(player);
                handle(ClientCommand.REGISTER, i);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void handle(ClientCommand message, int id) {
        List<GameEvent> events = new ArrayList<>();

        switch(message){
            case REGISTER:
                events.add(new GameEvent(ServerCommand.REGISTER, id, ItemType.PLAYER));
                break;
            case START:
                world.generate();
                events.add(new GameEvent(ServerCommand.START, world.players, world.balls, world.obstacles, world.holes));
                //events.add(new GameEvent(ServerCommand.START));
                startAI();
                break;
            case EXIT:
                events.add(new GameEvent(ServerCommand.EXIT, id, ItemType.PLAYER));
                Player playerToRemove = getPlayerById(id);
                players.remove(playerToRemove);
                if(players.isEmpty()) stopAI();
                break;
            case COUNTDOWN:
                events.add(new GameEvent(ServerCommand.END));
                stopAI();
                //players.clear(); is that necessary ?
                break;
            case LEFT:
            case RIGHT:
            case UP:
            case DOWN:
                events = world.processMove(id, message);
                break;
        }

        // send the answer(s) to each client instance
        for (Player player : players) {
            for (GameEvent event : events) {
                try {
                    player.sendMessage(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //also send the answer(s) to the ai
        if(ai != null) {
            for (GameEvent event : events) {
                ai.handleMessage(event);
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
