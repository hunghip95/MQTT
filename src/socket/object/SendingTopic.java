package socket.object;

import java.io.Serializable;

/**
 * Created by ASUS on 27/11/2016.
 */
public class SendingTopic implements Serializable {
    String topicName;
    String content;

    public SendingTopic(String topicName, String content) {
        this.topicName = topicName;
        this.content = content;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
