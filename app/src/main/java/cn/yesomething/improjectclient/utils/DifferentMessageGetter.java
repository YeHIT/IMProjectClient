package cn.yesomething.improjectclient.utils;

import com.tencent.imsdk.v2.V2TIMMessage;

//用于获取不同类型的消息
public class DifferentMessageGetter {

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
