package org.pawles.checkers.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class serverThread extends Thread {
    private Socket socket;
    private static Scanner socketIn;
    private static Writer socketOut;

    public serverThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

            socketIn = new Scanner(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());

            // place for code

            /*OutputStream output = socket.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            InputStream input = socket.getInputStream();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // place for code which operate on exact player input/output


            out.writeObject("Sample user output"); // the way you send something to a client
            String example = (String) in.readObject(); // the way you read something from user
            System.out.println("Hello world!"); // prints on server console

            to connect to a thread from user app use:

            Socket socket = new Socket("localhost", 1234);

            then

            OutputStream output = socket.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            InputStream input = socket.getInputStream();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            and

            out.writeObject(); will send smth to serverThread
            in.readObject(); will read smth from serverThread
            */


        } catch (IOException e) {
            System.out.println("Server thread IO exception: "+e.getMessage());
        }
    }
}
