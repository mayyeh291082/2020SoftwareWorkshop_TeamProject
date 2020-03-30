package serverClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class to listen for connections from clients, accept them and pass the client's socket into a new thread of the server.
 */
public class Server implements GameConstants {

    private static final int PORT= 5554;
    private DBConnect db= new DBConnect();

    /**
     * Main method to run the server
     */
    public static void main(String[] args) {
         new Server();
    }


    /**
     * Server constructor that comprises a server socket listening out for requests on a specific port number.
     * When a client requests to connect, the server accepts this request and creates a 'normal' socket for
     * this connection to the client so that the server socket can go back to listening out for requests and isn't
     * blocked by that client's connection.
     *
     * For every client that connects, they are fed into the GameHandler and thus a thread is created to handle this
     * client's requests.
     */
    public Server(){

        //try with resources (resources will automatically close when try block finishes)
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server is running with port number " + PORT);

            //infinite while loop to continuously listen for connection requests from clients.
            while (true) {
                System.out.println("waiting for a user to join");

                //connect a client
                Socket player = serverSocket.accept();
                System.out.println("First player has joined the game from " + player.getInetAddress().getHostAddress());

                //start a thread for the server and pass in the database connection and the client socket
                GameHandler game = new GameHandler(player, db);
                game.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

}



