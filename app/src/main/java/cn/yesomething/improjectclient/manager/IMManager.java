package cn.yesomething.improjectclient.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

//管理配置及用户相关
public class IMManager {
    private static final String TAG = "IMManager";
    //处理userSig的handler
    private static Handler userSigHandler;
    //项目的appId
    private static int SDKAppID = 1400433315;
    public static final int TIMSDK_LOG_NONE = 0;
    public static final int TIMSDK_LOG_DEBUG = 3;
    public static final int TIMSDK_LOG_INFO = 4;
    public static final int TIMSDK_LOG_WARN = 5;
    public static final int TIMSDK_LOG_ERROR = 6;

    /**
     * 判断用户当前是否登录
     * @return 用户已登录返回true
     */
    public static boolean isLoginIMService(){
        int loginStatus = V2TIMManager.getInstance().getLoginStatus();
        return loginStatus == V2TIMManager.V2TIM_STATUS_LOGINED ||
                loginStatus == V2TIMManager.V2TIM_STATUS_LOGINING;
    }

    /**
     * 获取当前已登录的用户id
     * @return 当前已登录的用户id
     */
    public static String getLoginUser(){
       return V2TIMManager.getInstance().getLoginUser();
    }

    /**
     * 初始化IMSDK配置,默认打印日志等级为打印info以上,支持自行传入参数修改等级
     * @param context 传入context,一般为this
     */
    public static void initSDKConfig(Context context){
        initSDKConfig(context,IMManager.TIMSDK_LOG_INFO);
    }

    /**
     * 初始化IMSDK配置
     * @param context 传入context,一般为this
     * @param logLevel TIMSDK需要打印的日志等级
     */
    public static void initSDKConfig(Context context,int logLevel){
        // 1. 从 IM 控制台获取应用 SDKAppID，详情请参考 SDKAppID。
        // 2. 初始化 config 对象
        V2TIMSDKConfig config = new V2TIMSDKConfig();
        // 3. 指定 log 输出级别，详情请参考 SDKConfig。
        config.setLogLevel(logLevel);
        // 4. 初始化 SDK 并设置 V2TIMSDKListener 的监听对象。
        // initSDK 后 SDK 会自动连接网络，网络连接状态可以在 V2TIMSDKListener 回调里面监听。
        V2TIMManager.getInstance().initSDK(context, SDKAppID, config, new V2TIMSDKListener() {
            // 5. 监听 V2TIMSDKListener 回调
            @Override
            public void onConnecting() {
                // 正在连接到腾讯云服务器
                Log.e(TAG, "正在连接到腾讯云服务器 ");
            }
            @Override
            public void onConnectSuccess() {
                // 已经成功连接到腾讯云服务器
                Log.e(TAG, "已经成功连接到腾讯云服务器 ");
            }
            @Override
            public void onConnectFailed(int code, String error) {
                // 连接腾讯云服务器失败
                Log.e(TAG, "连接腾讯云服务器失败");
            }
            @Override
            public void onKickedOffline() {
                Log.e(TAG, "当前用户被踢下线");
            }

            @Override
            public void onUserSigExpired() {
                Log.e(TAG, "登录票据已经过期");
            }
        });
    }

    /**
     * 用户登录
     * @param context 需要展示错误信息的界面,一般传入this
     * @param userName 用户名
     * @param callback 回调函数
     */
    public synchronized static void login(Context context,String userName, V2TIMCallback callback){
        //若此时已处于登录状态则直接调用回调函数的onSuccess
        if(isLoginIMService()){
            callback.onSuccess();
        }
        //未登录则调用登录函数
        else{
            userSigHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    String response = (msg != null) ? (String) msg.obj : null;
                    if(response != null){
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String responseCode = jsonObject.getString("responseCode");
                            //登录成功
                            if(responseCode.equals("200")){
                                //SDK登录
                                String userSig = jsonObject.getString("userSig");
                                V2TIMManager.getInstance().login(userName,userSig, callback);
                            }
                            else {
                                String errorMessage = jsonObject.getString("errorMessage");
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(context, "异常错误请稍后再试", Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
            });
            //请求userSig线程启动
            MyServerManager.genTestUserSig(userSigHandler,userName);
        }
    }

    /**
     * 用户登出(不需要回调函数)
     */
    public static void logout(){
        logout(null);
    }

    /**
     * 用户登出
     * @param callback 登出的回调函数
     */
    public static void logout(V2TIMCallback callback){
        V2TIMManager.getInstance().logout(callback);
    }

    /**
     * 修改用户的文字资料
     * @param userNickname 用户昵称
     * @param userSex 用户性别
     * @param userBirthday 用户生日
     * @return 本次修改的结果
     */
    public static Boolean updateUserTextInfo(String userNickname, Integer userSex,Date userBirthday){
        return true;
    }


}
