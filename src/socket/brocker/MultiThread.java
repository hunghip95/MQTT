package socket.brocker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ASUS on 20/11/2016.
 */
public class MultiThread extends Thread{

    public ObjectInputStream is = null;
    public ObjectOutputStream os = null;

    public Socket socketOfServer;

    public MultiThread(Socket socketOfServer) {
        this.socketOfServer = socketOfServer;
    }

    @Override
    public void run() {
        try {
            is = new ObjectInputStream(socketOfServer.getInputStream());
            os = new ObjectOutputStream(socketOfServer.getOutputStream());

            String check = (String) is.readObject();

            if (check.equals("Subscriber")){
                Thread threadSub = new ThreadSub(socketOfServer, is, os);
                threadSub.start();
            }
            else{
                if (check.equals("Publisher")){
                    Thread threadPub = new ThreadPub(socketOfServer, is, os);
                    threadPub.start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
