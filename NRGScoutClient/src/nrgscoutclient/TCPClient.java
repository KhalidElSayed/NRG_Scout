package nrgscoutclient;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.*;

public class TCPClient {

    private Socket clientSocket;
    private int port;
    private String hostname;
    private boolean isConnected = false;

    public TCPClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        try {
            clientSocket = new Socket();
        }
        catch (Exception e) {
        }
    }

    public boolean start() {
        try {
            if (!isConnected) {
                clientSocket = new Socket(hostname, port);
                isConnected = true;
                return true;
            }
        }
        catch (Exception e) {
        }
        return false;
    }

    public boolean write(String data) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            writer.write(data + "\n!NRG Out!");
            writer.flush();
            writer.close();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void listen(final JTextArea console, final JLabel label, final JButton button) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    while (isConnected) {
                        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        try {
                            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            String clientInput = inFromClient.readLine();
                            if (clientInput != null) {
                                console.append("\nMessage from server: " + clientInput + "\n");
                                console.setCaretPosition(console.getText().length() - 1);
                            }
                        }
                        catch (IOException e) {
                            //Server disconnected
                            console.append("\nConnection disconnected by server at " + clientSocket.getInetAddress() + ".\n");
                            console.setCaretPosition(console.getText().length() - 1);
                            label.setText("Status: Disconnected");
                            label.setForeground(Color.red);
                            button.setText("Reconnect");
                            isConnected = false;
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        }
                        catch (InterruptedException ex) {
                        }
                        inFromClient.close();
                    }
                }
                catch (Exception e) {
                }
            }
        };
        //Starts the listening process on a new thread
        new Thread(r).start();
    }

    public void stop() {
        try {
            clientSocket.close();
            isConnected = false;
        }
        catch (Exception ex) {
        }
    }
}
