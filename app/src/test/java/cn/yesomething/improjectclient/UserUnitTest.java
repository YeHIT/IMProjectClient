package cn.yesomething.improjectclient;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import cn.yesomething.improjectclient.manager.UrlManager;
import cn.yesomething.improjectclient.utils.MyConnectionToServer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserUnitTest {
    @Test
    public void testGetSig() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName","111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.generateUserSigUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }

    @Test
    public void testUserLoginSuccess() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName","111");
            jsonObject.put("userPassword","111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.userLoginUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }
    @Test
    public void testUserLoginFailByNoUser() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName","111222222");
            jsonObject.put("userPassword","111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.userLoginUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }
    @Test
    public void testUserLoginFailByInputError() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName","111");
            jsonObject.put("userPassword","112221");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.userLoginUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }



    @Test
    public void userRegister() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName","111");
            jsonObject.put("userPassword","111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.userRegisterUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }

    @Test
    public void userUpdate() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName","111");
            jsonObject.put("userPassword","111");
            jsonObject.put("userSex",0);
            jsonObject.put("userNickname","Ye");
            jsonObject.put("userBirthday","2020-10-01");
//            jsonObject.put("userPicture","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.userUpdateUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }

    @Test
    public void userSelect() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName","111");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.userSelectUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }

    @Test
    public void userWordCloudGenerate(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName","denwade");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.userWordCloudGenerateUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }
}