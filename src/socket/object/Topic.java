package socket.object;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ASUS on 20/11/2016.
 */
public class Topic implements Serializable{
    public String topicName;

    public ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> pendingUsers = new ArrayList<>();

    public Topic(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public synchronized void addUser(User user) {
        this.users.add(user);
    }

    public boolean removeUser(User user) {
        if (users.remove(user)) return true;
        return false;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getPendingUsers() {
        return pendingUsers;
    }

    public synchronized void addPendingUser(User pendingUser) {
        this.pendingUsers.add(pendingUser);
    }
}
