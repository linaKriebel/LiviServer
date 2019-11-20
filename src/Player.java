import java.io.*;
import java.net.Socket;

public class Player implements Runnable {

    private Socket socket;
    private int id;

    public Player(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        //receive message from client
        while (true) {
            InputStream is = null;
            try {
                is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String receivedMessage = br.readLine();
                System.out.println("Message received from client is " + receivedMessage);

                String sendMessage = "ok" + "\n";

                if (receivedMessage.equals("down")) {
                    sendMessage = "not ok" + "\n";
                }
                //Send response back to the client
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(sendMessage);
                System.out.println("Message sent to the client is " + sendMessage);
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Socket getSocket() {
        return socket;
    }
}
