package socket.brocker;

import socket.object.SendingTopic;
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
public class PublisherSite {
    public Socket socketOfServer;
    private ObjectOutputStream os;
    private ObjectInputStream is;

    public PublisherSite(Socket socketOfServer, ObjectInputStream is, ObjectOutputStream os) {
        this.socketOfServer = socketOfServer;
        this.is = is;
        this.os = os;
    }

    public void start() {
        while (true){
            try {
                String topicName = (String) is.readObject();
                String content = (String) is.readObject();

                for (Topic topic : listTopics){
                    if (topic.getTopicName().equals(topicName)){
                        for (User user : topic.getUsers()){
                            ObjectOutputStream outputStream = user.getOut();

                            SendingTopic sendingTopic = new SendingTopic(topicName,content);
                            user.addSendingTopic(sendingTopic);
                            try {
                                outputStream.writeObject("Publisher");
                                outputStream.flush();

                                outputStream.writeObject(sendingTopic);
                                outputStream.flush();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                 }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
