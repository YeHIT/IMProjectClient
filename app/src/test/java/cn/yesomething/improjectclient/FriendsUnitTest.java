package cn.yesomething.improjectclient;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import cn.yesomething.improjectclient.manager.UrlManager;
import cn.yesomething.improjectclient.utils.MyConnectionToServer;

public class FriendsUnitTest {
    @Test
    public void testAddFriends(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friendId","xy");
            jsonObject.put("userId","xyz");
            jsonObject.put("friendName","heihei");
            jsonObject.put("friendType","");
            jsonObject.put("friendGroupType","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.friendsAddUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }

    @Test
    public void testUpdateFriends(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friendId","xy");
            jsonObject.put("userId","xyz");
            jsonObject.put("friendName","heihei");
            jsonObject.put("friendType","");
            jsonObject.put("friendGroupType","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.friendsUpdateUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }

    @Test
    public void testSelectFriends(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friendId","xy");
            jsonObject.put("userId","xyz");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.friendsSelectUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }

    @Test
    public void testSelectFriendsList(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId","111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.friendsListSelectUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }
}
