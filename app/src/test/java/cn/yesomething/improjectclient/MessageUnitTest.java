package cn.yesomething.improjectclient;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.yesomething.improjectclient.manager.UrlManager;
import cn.yesomething.improjectclient.utils.MyConnectionToServer;

public class MessageUnitTest {
    @Test
    public void testSelectMessageByTime() throws ParseException {
        JSONObject jsonObject = new JSONObject();
        long start = 1603205024738L;
        long endTime =new Date().getTime();
        String formatStartTime = new SimpleDateFormat().format(start);
        String formatEndTime = new SimpleDateFormat().format(endTime);
        System.out.println(formatStartTime + "-----" +formatEndTime);
        try {
            jsonObject.put("fromId","11");
            jsonObject.put("toId","22");
            jsonObject.put("messageStartTime",formatStartTime);
            jsonObject.put("messageEndTime",formatEndTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.messageSelectUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }

    @Test
    public void testInsertMessage(){
        String date = Long.toString( new Date().getTime());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fromId","11");
            jsonObject.put("toId","22");
            jsonObject.put("messageContent","androidHello");
            jsonObject.put("messageTime",date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.messageInsertUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }
}
