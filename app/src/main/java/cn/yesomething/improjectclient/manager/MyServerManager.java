package cn.yesomething.improjectclient.manager;

import android.os.Handler;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cn.yesomething.improjectclient.utils.MyConnectionToServer;

//与自己的服务器相关的服务
public class MyServerManager {

    /**
     * 向服务器发送请求UserSig签名,利用handler接受数据
     * @param userSigHandler 用于在操作完成后处理返回结果的handler
     * @param userName 传入需要生成签名的userName
     */
    public static void genTestUserSig(Handler userSigHandler,String userName){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(userSigHandler,
                UrlManager.myServer + UrlManager.generateUserSigUrl,
                jsonObject.toString())
                .start();
    }

    /**
     * 根据用户名密码判断能否登录
     * @param loginHandler 用于登录完成后处理返回结果的handler
     * @param userName 用户名
     * @param userPassword 用户密码
     */
    public static void login(Handler loginHandler, String userName, String userPassword){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",userName);
            jsonObject.put("userPassword",userPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(loginHandler,
                UrlManager.myServer+UrlManager.userLoginUrl,
                jsonObject.toString())
                .start();
    }

    /**
     * 修改用户资料
     * @param userUpdateHandler 用于登录完成后处理返回结果的handler
     * @param userName 用户名
     * @param userNickname 用户昵称
     * @param userSex 用户性别
     */
    public static void userUpdate(Handler userUpdateHandler, String userName, String userNickname,Integer userSex){
        userUpdate(userUpdateHandler,userName,userNickname,userSex,null);
    }

    /**
     * 修改用户资料
     * @param userUpdateHandler 用于登录完成后处理返回结果的handler
     * @param userName 用户名
     * @param userNickname 用户昵称
     * @param userSex 用户性别
     * @param userTags 用户标签
     */
    public static void userUpdate(Handler userUpdateHandler, String userName, String userNickname, Integer userSex, ArrayList<String> userTags){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",userName);
            jsonObject.put("userNickname",userNickname);
            jsonObject.put("userSex",userSex);
            if(userTags != null){
                jsonObject.put("userTags",userTags);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(userUpdateHandler,
                UrlManager.myServer+UrlManager.userUpdateUrl,
                jsonObject.toString())
                .start();
    }

    /**
     * 用户头像修改
     * @param userPictureUpdateHandler 用于登录完成后处理返回结果的handler
     * @param userName 用户名
     * @param base64String 头像图片的base64编码
     */
    public static void userPictureUpdate(Handler userPictureUpdateHandler, String userName,String base64String){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",userName);
            jsonObject.put("base64String",base64String);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(userPictureUpdateHandler,
                UrlManager.myServer+UrlManager.userPictureUpdateUrl,
                jsonObject.toString())
                .start();
    }

    /**
     * 用户头像修改
     * @param userWordCloudGenerateHandler 用于登录完成后处理返回结果的handler
     * @param userName 用户名
     */
    public static void userWordCloudGenerate(Handler userWordCloudGenerateHandler, String userName){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(userWordCloudGenerateHandler,
                UrlManager.myServer+UrlManager.userWordCloudGenerateUrl,
                jsonObject.toString())
                .start();
    }

    /**
     * 用户资料查询
     * @param userSelectHandler 用于登录完成后处理返回结果的handler
     * @param userName 用户名
     */
    public static void userSelect(Handler userSelectHandler, String userName){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(userSelectHandler,
                UrlManager.myServer+UrlManager.userSelectUrl,
                jsonObject.toString())
                .start();
    }

    /**
     * 调用后端完成用户注册
     * @param registerHandler 用于在操作完成后处理返回结果的handler
     * @param userName 用户名
     * @param userPassword 用户密码
     */
    public static void register(Handler registerHandler,String userName,String userPassword){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",userName);
            jsonObject.put("userPassword",userPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(registerHandler,
                UrlManager.myServer+UrlManager.userRegisterUrl,
                jsonObject.toString()).start();
    }

    /**
     * 获取当前登录者的好友列表
     * @param getFriendListHandler 用于在操作完成后处理返回结果的handler
     */
    public static void selectFriendsList(Handler getFriendListHandler){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",IMManager.getLoginUser());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(getFriendListHandler,
                UrlManager.myServer+UrlManager.friendsListSelectUrl,
                jsonObject.toString())
                .start();
    }

    /**
     * 向服务器发送当前发出的消息
     * @param sendMessageHandler 用于在操作完成后处理返回结果的handler
     * @param fromId 消息发出者
     * @param toId 消息接收者
     * @param messageTime 消息发出时间
     * @param messageContent 消息内容
     */
    public static void sendMessage(Handler sendMessageHandler, String fromId,
                                   String toId, Date messageTime,String messageContent){
        sendMessage(sendMessageHandler,fromId,toId,messageTime,messageContent,1);
    }

    /**
     * 向服务器发送当前发出的消息
     * @param sendMessageHandler 用于在操作完成后处理返回结果的handler
     * @param fromId 消息发出者
     * @param toId 消息接收者
     * @param messageTime 消息发出时间
     * @param messageContent 消息内容
     * @param messageContentType 消息类型默认为文本类型(用1表示)
     */
    public static void sendMessage(Handler sendMessageHandler, String fromId, String toId,
                                   Date messageTime,String messageContent,Integer messageContentType){
        JSONObject jsonObject = new JSONObject();
        //加密
        String mContent = StringEscapeUtils.escapeJava(messageContent);
        try {
            jsonObject.put("fromId",fromId);
            jsonObject.put("toId",toId);
            jsonObject.put("messageContent",mContent);
            jsonObject.put("messageTime",messageTime.getTime());
            jsonObject.put("messageContentType",messageContentType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(sendMessageHandler,
                UrlManager.myServer+UrlManager.messageInsertUrl,
                jsonObject.toString()).start();
    }

    /**
     * 查询同某个好友的聊天记录
     * @param getMessageListHandler 用于在操作完成后处理返回结果的handler
     * @param friendName 好友名
     */
    public static void selectMessageList(Handler getMessageListHandler,String friendName){
        selectMessageList(getMessageListHandler,IMManager.getLoginUser(),friendName,
                null,null);
    }


    /**
     * 根据双方名字以及所给的时间段获取双方聊天记录
     * @param getMessageListHandler 用于在操作完成后处理返回结果的handler
     * @param userName 当前登录的用户名
     * @param friendName 好友名
     * @param messageStartTime 消息起始时间
     * @param messageEndTime 消息结束时间
     */
    public static void selectMessageList(Handler getMessageListHandler,String userName,
                                         String friendName, Date messageStartTime,Date messageEndTime){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fromId",userName);
            jsonObject.put("toId",friendName);
            if(messageStartTime != null){
                jsonObject.put("messageStartTime",messageStartTime);
            }
            if(messageEndTime != null){
                jsonObject.put("messageEndTime",messageEndTime);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(getMessageListHandler,
                UrlManager.myServer+UrlManager.messageSelectUrl,
                jsonObject.toString()).start();
    }

    /**
     * 根据好友名添加好友
     * @param addFriendHandler 用于在操作完成后处理返回结果的handler
     * @param friendName 好友名
     */
    public static void addFriend(Handler addFriendHandler,String friendName){
        addFriend(addFriendHandler,friendName,
                null,null,null);
    }

    /**
     * 设置好友的具体信息
     * @param addFriendHandler 用于在操作完成后处理返回结果的handler
     * @param friendName 好友名
     * @param friendNickName 好友昵称
     * @param friendType 好友类型
     * @param friendGroupType 好友所在分组
     */
    public static void addFriend(Handler addFriendHandler,String friendName,
                                 String friendNickName,String friendType,String friendGroupType){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friendId",friendName);
            jsonObject.put("userId",IMManager.getLoginUser());
            if(friendNickName == null){
                jsonObject.put("friendName",IMManager.getLoginUser());
            }
            else{
                jsonObject.put("friendName",friendNickName);
            }
            if( friendType != null){
                jsonObject.put("friendType",friendType);
            }
            if( friendGroupType != null){
                jsonObject.put("friendGroupType",friendGroupType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyConnectionToServer.getConnectionThread(addFriendHandler,
                UrlManager.myServer+UrlManager.friendsAddUrl,
                jsonObject.toString()).start();

    }
}
