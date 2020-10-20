package cn.yesomething.improjectclient.PageContact;
import java.io.Serializable;

public class Contact implements Serializable {//联系人类
    private String mName;
    private int mType;

    public Contact(String name, int type) {
        mName = name;
        mType = type;
    }

    public String getmName() {
        return mName;
    }

    public int getmType() {
        return mType;
    }

}