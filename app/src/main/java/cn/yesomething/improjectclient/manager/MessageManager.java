package cn.yesomething.improjectclient.manager;

import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import cn.yesomething.improjectclient.utils.DifferentMessageGetter;

//管理消息相关
public class MessageManager {
    private static final String TAG = "MessageManager";

    /**
     * 发送信息(不需要回调函数)
     * @param text 要发送的信息内容
     * @param userId 发给的用户
     */
    public static void sendTextMessage(String text, String userId){
        sendTextMessage(text,userId,null);
    }

    /**
     * 发送纯文字信息
     * @param text 要发送的信息内容
     * @param userId 发给的用户
     * @param callback 回调函数
     */
    public static void sendTextMessage(String text, String userId, V2TIMValueCallback<V2TIMMessage> callback){
        V2TIMManager.getInstance().sendC2CTextMessage(text,userId,callback);
    }


    /**
     * 添加简单信息监听器
     * @param listener 简单信息监听器
     */
    public static void addSimpleMsgListener(V2TIMSimpleMsgListener listener){
        V2TIMManager.getInstance().addSimpleMsgListener(listener);
    }

    /**
     * 添加增强版信息监听器
     * @param listener 增强版信息监听器
     */
    public static void addAdvancedMsgListener(V2TIMAdvancedMsgListener listener){
        V2TIMManager.getMessageManager().addAdvancedMsgListener(listener);
    }

    /**
     * 根据传入的V2TIMMessage对象返回所需的Message对象
     * @param message 传入的V2TIMMessage对象
     * @param <T> 返回所需的对象类型
     * @return 返回所需的Message对象
     */
    public static <T> T getMessage(V2TIMMessage message){
        return (T)DifferentMessageGetter.getMessage(message);
    }

}
