package c4q.nyc.ramonaharrison.meshnyc;

/**
 * Created by alvin2 on 8/1/15.
 */
public class Message {
    private String id;
    private String intention;
    private int isSent;
    private String name;
    private String timeStamp;
    private String messageContent;

    public Message(String id, String intention, int isSent, String username, String timeStamp, String messageContent) {
        this.id = id;
        this.intention = intention;
        this.isSent = isSent;
        this.name = username;
        this.timeStamp = timeStamp;
        this.messageContent = messageContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntention() {
        return intention;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public int getIsSent() {
        return isSent;
    }

    public void setIsSent(int isSent) {
        this.isSent = isSent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
