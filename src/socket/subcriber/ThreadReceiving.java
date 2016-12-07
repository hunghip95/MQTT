package socket.subcriber;

import socket.object.SendingTopic;
import socket.object.Topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import static socket.subcriber.Subscriber.*;

/**
 * Created by ASUS on 20/11/2016.
 */
public class ThreadReceiving extends Thread {
    public Socket socketOfClient;

    public ThreadReceiving(Socket socketOfClient) {
        this.socketOfClient = socketOfClient;
    }

    @Override
    public void run() {
        try {
            String message = (String) is.readObject();
            if (message.equals("New user")){
                ArrayList<Topic> topics = (ArrayList<Topic>) is.readObject();
                System.out.println(is.readObject());
                System.out.printf("--You can Sub:");
                for (Topic topic : topics){
                    System.out.printf(topic.getTopicName() + " ");
                }
                System.out.println();
                this.receive();
            }
            else{
                if (message.equals("Existed user")){
                    ArrayList<String> subTopic = (ArrayList<String>) is.readObject();
                    listSubTopic = subTopic;
                    System.out.printf("--You Subscribed: ");
                    for (String  topic : subTopic){
                        System.out.printf(topic + " ");
                    }
                    System.out.println();
                    ArrayList<Topic> topicCanSub = (ArrayList<Topic>) is.readObject();
                    System.out.printf("--You can Sub: ");
                    for (Topic topic : topicCanSub){
                        System.out.printf(topic.getTopicName() + " ");
                    }
                    System.out.println();
                    String status = (String) is.readObject();
                    if (status.equals("Welcome!")){
                        System.out.println(status);
                    }
                    else {
                        ArrayList<SendingTopic> sendingTopics = (ArrayList<SendingTopic>) is.readObject();
                        System.out.println("--Missing Topics:");
                        for (SendingTopic sendingTopic : sendingTopics){
                            System.out.println(sendingTopic.getTopicName() + ": " + sendingTopic.getContent());
                        }
                    }
                    this.receive();
                }
            }

        } catch (IOException e) {
            System.out.println("Server offline !");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void receive() throws IOException, ClassNotFoundException {
        while (true){
            String message = (String) is.readObject();
            if (message.equals("Sub ok")){
                String topicName = (String) is.readObject();
                System.out.println("--Added " + topicName);
                listSubTopic.add(topicName);
                System.out.printf("--List Topics now: ");
                for (String topic : listSubTopic){
                    System.out.printf(topic + " ");
                }
                System.out.println();
            }
            else {
                if (message.equals("Faild")){
                    String faild = (String) is.readObject();
                    System.out.println(faild);
                }
                else {
                    if (message.equals("Remove done")){
                        String topicName = (String) is.readObject();
                        listSubTopic.remove(topicName);
                        System.out.println("--Removed " + topicName);
                        System.out.printf("--List topics now: ");
                        if (listSubTopic.size() > 0){
                            for (String string : listSubTopic){
                                System.out.printf(string + " ");
                            }
                            System.out.println();
                        }
                        else System.out.println("no Topic");
                    }
                    else {
                        if (message.equals("Publisher")){
                            SendingTopic sendingTopic = (SendingTopic) is.readObject();
                            System.out.println(sendingTopic.getTopicName() + ": " + sendingTopic.getContent());
                            os.writeObject("Received");
                            os.flush();
                            os.writeObject(sendingTopic);
                            os.flush();
                        }
                    }
                }
            }
        }
    }
}
