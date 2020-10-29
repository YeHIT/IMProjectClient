package cn.yesomething.improjectclient;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
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
        long start = 1603350183001L;
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
        //解析返回的json
        try {
            JSONObject readJsonObject = new JSONObject(responseJson);
            JSONArray readJsonArray = readJsonObject.getJSONArray("messageList");
            String readContent = (String) readJsonArray.getJSONObject(0).get("messageContent");
            String readTime = new SimpleDateFormat().format(new Date( (long)readJsonArray.getJSONObject(0).get("messageTime")));
            String umReadContent = StringEscapeUtils.unescapeJava(readContent);
            umReadContent = StringEscapeUtils.unescapeJava(umReadContent);
            System.out.println(umReadContent);
            System.out.println(readTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertMessage(){
        String date = Long.toString( new Date().getTime());
        JSONObject jsonObject = new JSONObject();
        String content = "你好啊\uD83D\uDE31androidHello";
        //加密
        String mContent = StringEscapeUtils.escapeJava(content);
        try {
            jsonObject.put("fromId","11");
            jsonObject.put("toId","22");
            jsonObject.put("messageContent",mContent);
            jsonObject.put("messageTime",date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String responseJson = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.messageInsertUrl,
                jsonObject.toString());
        System.out.println(responseJson);
    }
}
