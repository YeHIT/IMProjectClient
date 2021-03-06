package cn.yesomething.improjectclient.chat;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.net.URL;

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    //加了两个状态 消息已读 和 消息未读
    public static final int TYPE_NOT_READ = 0;
    public static final int TYPE_READ = 1;

    private String content;
    //消息 发出 接收
    private int type;
    //消息已读未读属性
    private int read_type;
    //消息发送时间属性
    private String msg_time;
    //消息情绪
    private double msg_emotion;



    //默认发送生成图片信息
    public Msg(int type ,int read_type, String msg_time){
        this.content = null;
        this.type = type;
        this.read_type = read_type;
        this.msg_time = msg_time;
        this.msg_emotion = 0;
    }

    //默认生成文字信息
    public Msg(String content, int type ,int read_type, String msg_time){
        this.content = content;
        this.type = type;
        this.read_type = read_type;
        this.msg_time = msg_time;
        this.msg_emotion = 0;
    }

    public Msg(String content, int type ,int read_type, String msg_time , double msg_emotion){
        this.content = content;
        this.type = type;
        this.read_type = read_type;
        this.msg_time = msg_time;
        this.msg_emotion = msg_emotion;
    }

    public Msg(String content, int type ){
        this.content = content;
        this.type = type;
        this.read_type = Msg.TYPE_NOT_READ;
        this.msg_time = "what time is it?";
        this.msg_emotion = 0;
    }


    public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }

    public int getReadType(){
        return read_type;
    }

    public String getMsgtime(){
        return msg_time;
    }

    public void ChangeMsgReadType(int read_type){
        this.read_type = read_type;
    }

    public double getMsgEmotion(){
        return msg_emotion;
    }

    public void ChangeMsgEmotion(int emotion){
        this.msg_emotion = emotion;
    }
}
