package cn.yesomething.improjectclient.chat;

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    //加了两个状态 消息已读 和 消息未读
    public static final int TYPE_NOT_READ = 0;
    public static final int TYPE_READ = 1;
    private String content;
    private int type;
    //消息已读未读属性
    private int read_type;
    //消息发送时间属性
    private String msg_time;

    public Msg(String content, int type ,int read_type, String msg_time){
        this.content = content;
        this.type = type;
        this.read_type = read_type;
        this.msg_time = msg_time;
    }

    public Msg(String content, int type ){
        this.content = content;
        this.type = type;
        this.read_type = Msg.TYPE_NOT_READ;
        this.msg_time = "what time is it?";
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
}
