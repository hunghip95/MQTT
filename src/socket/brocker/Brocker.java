package socket.brocker;

import socket.object.Topic;
import socket.object.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by ASUS on 20/11/2016.
 */
public class Brocker {

    static String interup = "no";
    static int test = 0;
    static ArrayList<User> listUsers = new ArrayList<>();
    static ArrayList<Topic> listTopics = new ArrayList<>();
//    static Map<Socket,String> listUser = new HashMap<Socket,String>();
//    static Map<Integer,Integer> a = new HashMap<Integer,Integer>();
//    static ArrayList<User> listUser = new ArrayList<User>();

    public static void main(String args[]) throws IOException {

        ServerSocket listener = null;
        BufferedReader dataIn = new BufferedReader(new InputStreamReader( System.in) );
        try {
            listener = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        System.out.println("Server in waiting to accept user...");

        listTopics.add(new Topic("Topic 1"));
        listTopics.add(new Topic("Topic 2"));
        listTopics.add(new Topic("Topic 3"));

        while (interup.equals("no")){

            Socket socketOfServer = listener.accept();
            if ( socketOfServer != null ) {
                new MultiThread(socketOfServer).start();

            }
        }

        System.out.println(test);
        System.out.println("Sever stopped!");
    }
}
