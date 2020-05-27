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

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        connect();
    }

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

    public void disconnect() {
        try {
            dout.writeUTF("EXIT");
            dout.flush();
            socket.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

}