package socket.brocker;

import socket.object.Topic;
import socket.object.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static socket.brocker.Brocker.listTopics;
import static socket.brocker.Brocker.listUsers;

/**
 * Created by ASUS on 20/11/2016.
 */
public class ThreadSub extends Thread{
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

    public Socket socketOfServer;

    public ThreadSub(Socket socketOfServer, ObjectInputStream in, ObjectOutputStream out) {
        this.socketOfServer = socketOfServer;
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        try {

            User user = null;

            String userName = (String) in.readObject();

            int check =0;

            for (User userCheck : listUsers){
                if (userCheck.getUserName().equals(userName) ){
                    check++;
                    userCheck.setSocketOfServer(socketOfServer);
                    userCheck.setIn(in);
                    userCheck.setOut(out);
                    user = userCheck;
                    System.out.println("Updated user: " + userName);

                    out.writeObject("Existed user");
                    out.flush();
                    out.writeObject(userCheck.getTopics());
                    out.flush();

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

                    out.writeObject(this.coppyListTopic(topicCanSub));
                    out.flush();

                    System.out.printf("List users now: ");
                    for (User user1 : listUsers){
                        System.out.printf(user1.getUserName()+":" + user1.getSocketOfServer() + "|");
                    }
                    System.out.println();
                }
            }
            if (check == 0){
                user = new User();
                user.setIn(in);
                user.setOut(out);
                user.setUserName(userName);
                user.setSocketOfServer(socketOfServer);
                this.addUser(user);
                System.out.println("Added user: " + userName);
                out.writeObject("New user");
                out.flush();
                out.writeObject(this.coppyListTopic(listTopics));
                out.flush();

                System.out.printf("List users now: ");
                for (User user1 : listUsers){
                    System.out.printf(user1.getUserName()+":" + user1.getSocketOfServer() + " ");
                }
                System.out.println();
            }
            int i =0;
            while (true){
                String control = (String) in.readObject();
                if (control.equals("Sub")){
                    String topicName = (String) in.readObject();
                    System.out.println("Received " + topicName);
                    if (this.checkTopic(topicName,user)==true){
                        user.addTopic(topicName);
                        out.writeObject("Notification");
                        out.flush();
                        out.writeObject("Sub ok");
                        out.flush();
                        out.writeObject(topicName);
                        out.flush();
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
                        out.writeObject("Faild");
                        out.flush();
                        out.writeObject("Subscribe false, check Topic name");
                        out.flush();
                    }
                }
//                else {
//                    if (control.equals("Unsub")){
//                        out.writeObject("Unsub");
//                        out.flush();
//                    }
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

}
