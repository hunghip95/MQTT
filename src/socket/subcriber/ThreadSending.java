package socket.subcriber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static socket.subcriber.Subcriber.os;
import static socket.subcriber.Subcriber.userName;

/**
 * Created by ASUS on 20/11/2016.
 */
public class ThreadSending extends  Thread{

    public Socket socketOfClient;


    public ThreadSending(Socket socketOfClient) {
        this.socketOfClient = socketOfClient;

    }

    @Override
    public void run() {
        try {
//            ObjectOutputStream out = new ObjectOutputStream(socketOfClient.getOutputStream());
            BufferedReader dataIn = new BufferedReader(new InputStreamReader( System.in) );

            System.out.println("In thread sending");

            os.writeObject(userName);
            os.flush();

            while (true){
                System.out.println("[Sub]/[Unsub] ?");
                String control = dataIn.readLine();

                System.out.println("Select Topic:");
                String topic = dataIn.readLine();

                os.writeObject(control);
                os.flush();

                os.writeObject(topic);
                os.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
