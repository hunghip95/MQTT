package socket.object;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by ASUS on 20/11/2016.
 */
public class User implements Serializable{

    public String userName;
    public Socket socketOfServer;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private User(String userName, Socket socketOfServer) {
        this.userName = userName;
        this.socketOfServer = socketOfServer;
    }

    public ArrayList<String> topics = new ArrayList<>();

    public User() {

    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Socket getSocketOfServer() {
        return socketOfServer;
    }

    public void setSocketOfServer(Socket socketOfServer) {
        this.socketOfServer = socketOfServer;
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void addTopic(String topic) {
        this.topics.add(topic) ;
    }

//    public synchronized ObjectOutputStream getOut() {
//        if ( null == out ) {
//            try {
//                out = new ObjectOutputStream(socketOfServer.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return out;
//    }


    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }
}
