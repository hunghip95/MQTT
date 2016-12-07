package socket.brocker;

import socket.object.Topic;
import socket.object.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ASUS on 20/11/2016.
 */
public class Broker {

    static String interup = "no";
    static ArrayList<User> listUsers = new ArrayList<>();
    static ArrayList<Topic> listTopics = new ArrayList<>();

    public static void main(String args[]) throws IOException {

        ServerSocket listener = null;
        try {
            listener = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        System.out.println("Server is waiting to accept user...");

        listTopics.add(new Topic("Templature"));
        listTopics.add(new Topic("Humidity"));
        listTopics.add(new Topic("WindStatus"));

        while (interup.equals("no")){
            Socket socketOfServer = listener.accept();
            if ( socketOfServer != null ) {
                Thread thread = new MultiThread(socketOfServer);
                thread.start();
            }
        }

        System.out.println("Sever stopped!");
    }
}
