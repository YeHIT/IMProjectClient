package cn.yesomething.improjectclient.PageConversation;

import java.io.Serializable;
import java.util.Date;


public class Conversation implements Serializable {//联系人类
    private String mName;
    private Date mTime;//yyyy-MM-dd hh:mm:ss
    private String latestMessage;

    public Conversation(String name, Date time, String message) {
        mName = name;
        mTime = time;
        latestMessage = message;
    }

    public String getmName() {
        return mName;
    }

    public Date getmTime() {
        return mTime;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

}