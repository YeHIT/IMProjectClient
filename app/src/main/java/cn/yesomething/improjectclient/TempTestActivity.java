package cn.yesomething.improjectclient;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MyServerManager;

//todo 使用时直接复制函数内部的代码然后修改
public class TempTestActivity extends Activity {
    //用户信息更新
    public void testUserUpdate(){
        //todo 一开始时记得声明handler
        Handler userUpdateHandler = null;
        //用户名
        String userName = IMManager.getLoginUser();
        //todo 以下信息需要根据实际获取
        //用户昵称
        String userNickname = "xxx";
        //用户性别
        Integer userSex = 1;
        //用户标签
        ArrayList<String> userTags = new ArrayList<>();
        userTags.add("ni1");
        userTags.add("ni2");
        userTags.add("ni3");
        //用于获取最终的数据并展示
        userUpdateHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                //网络正常
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //正常获取到数据
                        if(responseCode.equals("200")){
                            //todo 利用数据展示
                            String userName = jsonObject.getString("userName");
                            String userPassword = jsonObject.getString("userPassword");
                            Integer userSex = jsonObject.getInt("userSex");
                            String userNickname = jsonObject.getString("userNickname");
                            String userBirthday = jsonObject.getString("userBirthday");
                            //用户头像为网络地址url
                            String userPicture = jsonObject.getString("userPicture");
                            //获取用户历史头像
                            ArrayList<String> userHistoricalPictures = new ArrayList<>();
                            JSONArray picturesArray = jsonObject.getJSONArray("userHistoricalPictures");
                            for (int i = 0; i < picturesArray.length(); i++) {
                                userHistoricalPictures.add(picturesArray.getString(i));
                            }
                            //获取用户标签信息
                            ArrayList<String> userTags = new ArrayList<>();
                            JSONArray tagsArray = jsonObject.getJSONArray("userTags");
                            for (int i = 0; i < tagsArray.length(); i++) {
                                userTags.add(tagsArray.getString(i));
                            }

                        }
                        else {
                            //todo 错误信息处理
                            String errorMessage = jsonObject.getString("errorMessage");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //todo 网络异常判断
                else {
                }
                return false;
            }
        });
        MyServerManager.userUpdate(userUpdateHandler,userName,userNickname,userSex,userTags);
    }

    //用户头像上传
    public void testUserPictureUpdate(){
        //一开始时记得声明handler
        Handler userPictureUpdateHandler = null;
        //用户名
        String userName = IMManager.getLoginUser();
        //todo 头像图片的base64编码
        String base64String = "xxxx";
        //用于获取最终的数据并展示
        userPictureUpdateHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                //网络正常
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //正常获取到数据
                        if(responseCode.equals("200")){
                            //todo 利用数据展示
                            String userName = jsonObject.getString("userName");
                            //用户头像为网络地址url
                            String userPicture = jsonObject.getString("userPicture");
                        }
                        else {
                            //todo 错误信息处理
                            String errorMessage = jsonObject.getString("errorMessage");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //todo 网络异常判断
                else {
                }
                return false;
            }
        });
        MyServerManager.userPictureUpdate(userPictureUpdateHandler,userName,base64String);
    }

    //用户词云生成
    public void testUserWordCloudGenerate(){
        //一开始时记得声明handler
        Handler userWordCloudGenerateHandler = null;
        //用户名
        String userName = IMManager.getLoginUser();
        //用于获取最终的数据并展示
        userWordCloudGenerateHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                //网络正常
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //正常获取到数据
                        if(responseCode.equals("200")){
                            //todo 利用数据展示
                            String userName = jsonObject.getString("userName");
                            //词云图片为网络地址url
                            String wordCloudPicture = jsonObject.getString("wordCloudPicture");
                            //获取用户标签信息
                            ArrayList<String> userTags = new ArrayList<>();
                            JSONArray tagsArray = jsonObject.getJSONArray("tags");
                            for (int i = 0; i < tagsArray.length(); i++) {
                                userTags.add(tagsArray.getString(i));
                            }
                        }
                        else {
                            //todo 错误信息处理
                            String errorMessage = jsonObject.getString("errorMessage");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //todo 网络异常判断
                else {
                }
                return false;
            }
        });
        MyServerManager.userWordCloudGenerate(userWordCloudGenerateHandler,userName);
    }

    //用户资料查询
    public void testUserSelect(){
        //一开始时记得声明handler
        Handler userSelectHandler = null;
        //todo 输入要查询用户的用户名
        String userName = "xx";
        //用于获取最终的数据并展示
        userSelectHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                //网络正常
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //正常获取到数据
                        if(responseCode.equals("200")){
                            //todo 利用数据展示
                            String userName = jsonObject.getString("userName");
                            String userPassword = jsonObject.getString("userPassword");
                            Integer userSex = jsonObject.getInt("userSex");
                            String userNickname = jsonObject.getString("userNickname");
                            String userBirthday = jsonObject.getString("userBirthday");
                            //用户头像为网络地址url
                            String userPicture = jsonObject.getString("userPicture");
                            //获取用户历史头像
                            ArrayList<String> userHistoricalPictures = new ArrayList<>();
                            JSONArray picturesArray = jsonObject.getJSONArray("userHistoricalPictures");
                            for (int i = 0; i < picturesArray.length(); i++) {
                                userHistoricalPictures.add(picturesArray.getString(i));
                            }
                            //获取用户标签信息
                            ArrayList<String> userTags = new ArrayList<>();
                            JSONArray tagsArray = jsonObject.getJSONArray("userTags");
                            for (int i = 0; i < tagsArray.length(); i++) {
                                userTags.add(tagsArray.getString(i));
                            }

                        }
                        else {
                            //todo 错误信息处理
                            String errorMessage = jsonObject.getString("errorMessage");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //todo 网络异常判断
                else {
                }
                return false;
            }
        });
        MyServerManager.userWordCloudGenerate(userSelectHandler,userName);
    }

}
