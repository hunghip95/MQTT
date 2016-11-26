package socket.publisher;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ASUS on 21/11/2016.
 */
public class Publisher {

    static ObjectOutputStream os = null;
    static ObjectInputStream is = null;

    public static void main(String[] args) {

        // Địa chỉ máy chủ.
        final String serverHost = "localhost";

        Socket socketOfClient = null;

        BufferedReader dataIn = new BufferedReader(new InputStreamReader( System.in) );


        try {
            socketOfClient = new Socket("127.0.0.1", 9999);

            os = new ObjectOutputStream(socketOfClient.getOutputStream());
            is = new ObjectInputStream(socketOfClient.getInputStream());

            os.writeObject("Publisher");
            os.flush();

            while (true){
                System.out.println("Select Topic: ");
                String topicName = dataIn.readLine();

                System.out.println("Type content: ");
                String content = dataIn.readLine();

                os.writeObject(topicName);
                os.flush();

                os.writeObject(content);
                os.flush();
            }

        }
        catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        }
        catch (IOException e) {
            System.err.println("IOException:  " + e);
        }

    }
}
