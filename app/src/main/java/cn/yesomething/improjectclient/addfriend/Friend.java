package cn.yesomething.improjectclient.addfriend;

public class Friend {

    private String addUserID;
    private String addUserDes;

    public Friend(String addUserID,String addUserDes){
        this.addUserID = addUserID;
        this.addUserDes = addUserDes;
    }

    public String getAddUserDes() {
        return addUserDes;
    }

    public String getAddUserID(){
        return  addUserID;
    }
}
