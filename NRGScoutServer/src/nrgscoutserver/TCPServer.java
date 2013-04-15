package nrgscoutserver;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class TCPServer {
    //Picks a random port from 49151 to 65535

    private final int port = new java.util.Random().nextInt(16384) + 49151;
    private ServerSocket serverSocket;
    private boolean running = true;

    public TCPServer() {
        //Constructor
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        }
        catch (Exception e) {
            //Server socket cannot be created
            System.out.println("Failed to open socket on port " + port + "!");
            System.exit(0);
        }
    }

    public void start() {
        Runnable server = new Runnable() {

            public void run() {
                try {
                    //Keep looking for clients
                    while (running) {
                        //Constructor blocks until a client connects
                        Socket clientSocket = serverSocket.accept();
                        //New client has joined the server
                        System.out.println("New client connected: " + clientSocket.getInetAddress());
                        listen(clientSocket);
                    }
                }
                catch (Exception e) {
                    //Error occured
                    System.out.println("Error occured: " + e.getMessage());
                }
            }
        };
        new Thread(server).start();
    }

    private void listen(final Socket socket) {
        Runnable r = new Runnable() {

            public void run() {
                while (running) {
                    try {
                        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String line;
                        LinkedList<String> data = new LinkedList<String>();
                        while ((line = inFromClient.readLine()) == null) {
                            System.out.println(line);
                            data.add(line);
                        }
                        MainGUI.setOverridenData(data.toArray(new String[0]));
                        MainGUI.override = true;
                        System.out.println("Setting override to true");
                        inFromClient.close();
                    }
                    catch (IOException e) {
                        //Client disconnected
                        System.out.println("Client " + socket.getInetAddress() + " disconnected.");
                        break;
                    }
                }
            }
        };
        //Starts the listening process on a new thread
        new Thread(r).start();
    }

    public void stop() {
        running = false;
    }
}