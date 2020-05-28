package nl.ictm2a4.javagame.raspberrypi;

import java.io.*;
import java.net.*;
import java.lang.*;

public class Client {

    private Socket socket;
    private DataOutputStream dout;
    private DataInputStream din;

    private String ip;
    private int port;

    /**
     * Initiate a client
     * @param ip IP of the server
     * @param port Port of the server
     */
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        connect();
    }

    /**
     * Create a socket connection to the server
     */
    private void connect() {
        try {
            socket = new Socket(ip, port);

            dout = new DataOutputStream(socket.getOutputStream());
            din = new DataInputStream(socket.getInputStream());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Send a request to the server to get the button status of the RaspberryPI
     * @return RaspberyPI button status
     */
    public String getButtonStatus() {
        String str = "";

        try {
            dout.writeUTF("GET");
            dout.flush();

            str = din.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Close the socket connection with the server
     */
    public void disconnect() {
        try {
            dout.writeUTF("EXIT");
            dout.flush();
            socket.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the connection status of the socket conneciton with the RaspberryPI
     * @return Connection status with RaspberryPI
     */
    public boolean isConnected() {
        return socket.isConnected();
    }

}