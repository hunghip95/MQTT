package socket.subcriber;

import socket.object.Topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import static socket.subcriber.Subcriber.is;
import static socket.subcriber.Subcriber.listSubTopic;

/**
 * Created by ASUS on 20/11/2016.
 */
public class ThreadReceiving extends Thread {
//    ObjectInputStream in = null;

    public Socket socketOfClient;

    public ThreadReceiving(Socket socketOfClient) {
        this.socketOfClient = socketOfClient;

    }

    @Override
    public void run() {
        try {
//            is = new ObjectInputStream(socketOfClient.getInputStream());
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(System.in));

            String message = (String) is.readObject();
            if (message.equals("New user")){
                ArrayList<Topic> topics = (ArrayList<Topic>) is.readObject();
                System.out.printf("You can Sub:");
                for (Topic topic : topics){
                    System.out.printf(topic.getTopicName() + " ");
                }
                this.receive();
            }
            else{
                if (message.equals("Existed user")){
                    ArrayList<String> subTopic = (ArrayList<String>) is.readObject();
                    listSubTopic = subTopic;
                    System.out.printf("You Subscribed: ");
                    for (String  topic : subTopic){
                        System.out.printf(topic + " ");
                    }
                    System.out.println();
                    ArrayList<Topic> topicCanSub = (ArrayList<Topic>) is.readObject();
                    System.out.printf("You can Sub: ");
                    for (Topic topic : topicCanSub){
                        System.out.printf(topic.getTopicName() + " ");
                    }
                    this.receive();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void receive() throws IOException, ClassNotFoundException {
        while (true){
            String message = (String) is.readObject();
            if (message.equals("Notification")){
                String status = (String) is.readObject();
                if (status.equals("Sub ok")) {
                    String topicName = (String) is.readObject();
                    System.out.println("Added " + topicName);
                    listSubTopic.add(topicName);
                    System.out.printf("List Topics now: ");
                    for (String topic : listSubTopic){
                        System.out.printf(topic + " ");
                    }
                    System.out.println();
                }
            }
            else {
                if (message.equals("Faild")){
                    String faild = (String) is.readObject();
                    System.out.println(faild);
                }
                else {
                    if (message.equals("Publisher")){
                        System.out.println(is.readObject());
                    }
                }
            }
        }
    }
}
