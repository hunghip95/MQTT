package socket.brocker;

import socket.object.SendingTopic;
import socket.object.Topic;
import socket.object.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static socket.brocker.Broker.listTopics;
import static socket.brocker.Broker.listUsers;

/**
 * Created by ASUS on 20/11/2016.
 */
public class SubscriberSite {
    ObjectInputStream is = null;
    ObjectOutputStream os = null;
    User user = null;

    public Socket socketOfServer;

    public SubscriberSite(Socket socketOfServer, ObjectInputStream is, ObjectOutputStream os) {
        this.socketOfServer = socketOfServer;
        this.os = os;
        this.is = is;
    }


    public void start() {
        try {
            String userName = (String) is.readObject();
            if (this.checkNewUser(userName)){
                this.createUser(userName);
            }
            while (true){
                String control = (String) is.readObject();
                if (control.equals("Sub")){
                    this.subTopic();
                }
                else {
                    if (control.equals("Unsub")){
                       this.unsubTopic();
                    }
                    else {
                        if (control.equals("Received")){
                            SendingTopic sendingTopic = (SendingTopic) is.readObject();
                            user.getSendingTopics().remove(user.findSendingTopic(sendingTopic));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("One Client offline !");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean removeTopic(String topicName, User user) {
        for (Topic topic: listTopics){
            if (topic.getTopicName().equals(topicName)){
                topic.removeUser(user);
                return true;
            }
        }
        return false;
    }

    public synchronized void addUser(User user){
        listUsers.add(user);
    }

    public boolean checkTopic(String topicName, User user){
        int check = 1;
        for (String topic : user.getTopics()){
            if (topic.equals(topicName)) check = 0;
        }

        int checkTopic = 0;
        if (check == 1){
            for (Topic topic : listTopics){
                if (topic.getTopicName().equals(topicName)) checkTopic = 1;
            }
        }

        if (checkTopic==0) check = 0;

        if (check==0) return false;
        else return true;
    }

    public ArrayList<Topic> coppyListTopic(ArrayList<Topic> listTopics){
        ArrayList<Topic> listOutTopics = new ArrayList<>();
        for (Topic topic: listTopics){
            Topic newTopic = new Topic(topic.getTopicName());
            listOutTopics.add(newTopic);
        }
        return listOutTopics;
    }

    public boolean checkNewUser(String userName) throws IOException {
        for (User userCheck : listUsers){
            if (userCheck.getUserName().equals(userName) ){
                userCheck.setSocketOfServer(socketOfServer);
                userCheck.setIn(is);
                userCheck.setOut(os);
                user = userCheck;
                System.out.println("Updated user: " + userName);

                os.writeObject("Existed user");
                os.flush();
                os.writeObject(userCheck.getTopics());
                os.flush();

                ArrayList<Topic> topicCanSub = new ArrayList<>();
                int checkTopic;
                for (Topic topic : listTopics){
                    checkTopic = 0;
                    for (String topic1 : userCheck.getTopics()){
                        if (topic.getTopicName().equals(topic1)) checkTopic = 1;
                    }
                    if (checkTopic == 0){
                        topicCanSub.add(topic);
                    }
                }

                os.writeObject(this.coppyListTopic(topicCanSub));
                os.flush();

                if (userCheck.getSendingTopics().size()>0){
                    os.writeObject("Missing Topics");
                    os.flush();
                    ArrayList<SendingTopic> sendingTopics = new ArrayList<>();
                    for (SendingTopic sendingTopic : userCheck.getSendingTopics()){
                        sendingTopics.add(sendingTopic);
                    }

                    for (SendingTopic sendingTopic : sendingTopics){
                        userCheck.getSendingTopics().remove(sendingTopic);
                    }
                    os.writeObject(sendingTopics);
                    os.flush();
                }
                else {
                    os.writeObject("Welcome!");
                    os.flush();
                }
                System.out.printf("List users now: ");
                for (User user1 : listUsers){
                    System.out.printf(user1.getUserName()+":" + user1.getSocketOfServer() + "|");
                }
                System.out.println();
                return false;
            }
        }
        return true;
    }

    private void createUser(String userName) throws IOException {
        user = new User();
        user.setIn(is);
        user.setOut(os);
        user.setUserName(userName);
        user.setSocketOfServer(socketOfServer);
        this.addUser(user);
        System.out.println("Added user: " + userName);
        os.writeObject("New user");
        os.flush();
        os.writeObject(this.coppyListTopic(listTopics));
        os.flush();

        os.writeObject("Welcome!");
        os.flush();
        System.out.printf("List users now: ");
        for (User user1 : listUsers){
            System.out.printf(user1.getUserName()+":" + user1.getSocketOfServer() + " ");
        }
        System.out.println();
    }

    public void subTopic() throws IOException, ClassNotFoundException {
        String topicName = (String) is.readObject();
        System.out.println("Received " + topicName);
        if (this.checkTopic(topicName,user)==true){
            user.addTopic(topicName);
            os.writeObject("Sub ok");
            os.flush();
            os.writeObject(topicName);
            os.flush();
            for (Topic topic : listTopics){
                if (topic.getTopicName().equals(topicName)){
                    topic.addUser(user);
                    System.out.printf(topic.getTopicName() + ": ");
                    for (User user1 : topic.getUsers()){
                        System.out.printf(user1.getUserName() + " ");
                    }
                    System.out.println();
                }
            }
        }
        else {
            os.writeObject("Faild");
            os.flush();
            os.writeObject("Subscription failed, check Topic name");
            os.flush();
        }
    }

    private void unsubTopic() throws IOException, ClassNotFoundException {
        String topicName = (String) is.readObject();
        if (user.removeTopic(topicName)){
            if (this.removeTopic(topicName, user)){
                os.writeObject("Remove done");
                os.flush();
                os.writeObject(topicName);
                os.flush();
                System.out.printf(user.getUserName() + " has list Topics now: ");
                if (user.getTopics().size() > 0){
                    for (String topic : user.getTopics()){
                        System.out.printf(topic + " ");
                    }
                    System.out.println();
                }
                else System.out.println("no Topic");

            }
        }
    }
}
