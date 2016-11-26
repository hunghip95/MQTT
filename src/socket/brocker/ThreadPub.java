package socket.brocker;

import socket.object.Topic;
import socket.object.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static socket.brocker.Brocker.listTopics;

/**
 * Created by ASUS on 20/11/2016.
 */
public class ThreadPub extends Thread{
    public Socket socketOfServer;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ThreadPub(Socket socketOfServer, ObjectInputStream in, ObjectOutputStream out) {
        this.socketOfServer = socketOfServer;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            while (true){
                String topicName = (String) in.readObject();
                String content = (String) in.readObject();

                for (Topic topic : listTopics){
                    if (topic.getTopicName().equals(topicName)){
                        for (User user : topic.getUsers()){
                            ObjectOutputStream outputStream = user.getOut();
                            outputStream.writeObject("Publisher");
                            outputStream.flush();
                            outputStream.writeObject(content);
                            outputStream.flush();

                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
