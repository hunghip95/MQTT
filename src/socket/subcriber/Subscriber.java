package socket.subcriber;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by ASUS on 20/11/2016.
 */
public class Subscriber {
    static ObjectOutputStream os = null;
    static ObjectInputStream is = null;
    static String userName;
    static ArrayList<String> listSubTopic = new ArrayList<>();

    public static void main(String[] args) {

        // Địa chỉ máy chủ.
        final String serverHost = "localhost";

        Socket socketOfClient = null;

        BufferedReader dataIn = new BufferedReader(new InputStreamReader( System.in) );
        String filename, check = "co";


        try {
            socketOfClient = new Socket("127.0.0.1", 9999);

            os = new ObjectOutputStream(socketOfClient.getOutputStream());
            is = new ObjectInputStream(socketOfClient.getInputStream());

            os.writeObject("Subscriber");
            os.flush();

            System.out.println("Type your user name: ");
            userName = dataIn.readLine();

            Thread threadSending = new ThreadSending(socketOfClient);
            Thread threadReceiving = new ThreadReceiving(socketOfClient);

            threadSending.start();
            threadReceiving.start();

            while (true){

            }
        }
        catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        }
        catch (IOException e) {
            System.err.println("IOException:  " + e);
        }

        System.out.println("Ended Client's session, Good bye");
    }
}
