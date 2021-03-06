package cn.yesomething.improjectclient.manager;

import android.util.Log;

import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMValueCallback;


//管理SDK消息相关
public class MessageManager {
    private static final String TAG = "MessageManager";
    /**
     * 发送纯文字信息(不需要回调函数)
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
    public static void sendTextMessage(String text, String userId, V2TIMSendCallback<V2TIMMessage> callback){
        V2TIMMessage message = V2TIMManager.getMessageManager().createTextMessage(text);
        V2TIMManager.getMessageManager().sendMessage(message,userId,null,
                V2TIMMessage.V2TIM_PRIORITY_DEFAULT,false,null,callback);
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

    //用于获取不同类型的消息
    public static class DifferentMessageGetter {
        /**
         * 根据传入的V2TIMMessage对象返回所需的Message对象
         * @param message 传入的V2TIMMessage对象
         * @param <T> 返回所需的对象类型
         * @return 返回所需的Message对象
         */
        public static <T> T getMessage(V2TIMMessage message){
            switch (message.getElemType()){
                case V2TIMMessage.V2TIM_ELEM_TYPE_TEXT: return (T)message.getTextElem();
                case V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM: return (T)message.getCustomElem();
                case V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE: return (T)message.getImageElem();
                case V2TIMMessage.V2TIM_ELEM_TYPE_SOUND: return (T)message.getSoundElem();
                case V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO: return (T)message.getVideoElem();
                case V2TIMMessage.V2TIM_ELEM_TYPE_FILE: return (T) message.getFileElem();
                case V2TIMMessage.V2TIM_ELEM_TYPE_LOCATION: return (T)message.getLocationElem();
                case V2TIMMessage.V2TIM_ELEM_TYPE_FACE: return (T)message.getFaceElem();
                default:return null;
            }
        }
    }

    //设置会话监听器
    public static void setConversationListener(V2TIMConversationListener listener){
        V2TIMManager.getConversationManager().setConversationListener(listener);
    }

    /**
     * 获取会话列表
     * @param nextSeq	分页拉取的游标，第一次默认取传 0，后续分页拉传上一次分页拉取成功回调里的 nextSeq
     * @param count	分页拉取的个数，一次
     * @param callback 回调函数
     */
    public static void getConversationList(long nextSeq, int count, V2TIMValueCallback<V2TIMConversationResult> callback){
        V2TIMManager.getConversationManager().getConversationList(nextSeq,count,callback);
    }

    /**
     * 设置消息已读
     * @param userName	需要设置已读的好友名
     */
    public static void markMessageAsRead(String userName){
        V2TIMManager.getMessageManager().markC2CMessageAsRead(userName,new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                // 设置消息已读失败
            }
            @Override
            public void onSuccess() {
                // 设置消息已读成功
            }
        });
    }
}

