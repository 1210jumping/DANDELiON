package com.example.myeducationapp.DAO;

import com.example.myeducationapp.Global;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author u7532738 Jinhan Tan
 * Message class
 */
public class Message {

    private String fromUserId;
    private String toUserId;
    private String message;
    private String dateTime;

    private String fromName;
    private String toName;

    private boolean from;

    /**
     * to string
     * @return
     */
    @Override
    public String toString() {
        return "Message{" +
                "fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", message='" + message + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", fromName='" + fromName + '\'' +
                ", toName='" + toName + '\'' +
                ", from=" + from +
                '}';
    }

    /**
     * constructor
     * @param fromUserId
     * @param toUserId
     * @param message
     */
    public Message(String fromUserId, String toUserId, String message) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.message = message;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime=format.format(calendar.getTime());
        this.fromName= Global.getUserById(fromUserId).getName();
        this.toName=Global.getUserById(toUserId).getName();
    }

    /**
     * default constructor
     */
    public Message(){}

    /**
     * whether the message is sent by the user or received by the user
     * @return
     */
    public boolean isFrom() {
        from=toUserId.equals(Global.currentUser.getId());
        return from;
    }

    /**
     *
     * @param from
     */
    public void setFrom(boolean from) {
        this.from = from;
    }

    /**
     *
     * @return
     */
    public String getFromName() {
        return fromName;
    }

    /**
     *
     * @return
     */
    public String getToName() {
        return toName;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @return
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     *
     * @return
     */
    public String getFromUserId() {
        return fromUserId;
    }

    /**
     *
     * @return
     */
    public String getToUserId() {
        return toUserId;
    }
}
