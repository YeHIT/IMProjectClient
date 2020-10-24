package cn.yesomething.improjectclient.manager;

import android.util.Log;

import com.tencent.imsdk.v2.V2TIMFriendAddApplication;
import com.tencent.imsdk.v2.V2TIMFriendApplication;
import com.tencent.imsdk.v2.V2TIMFriendApplicationResult;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMFriendshipListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.List;

//管理SDK好友相关
public class FriendsManager {
    private static final String TAG = "FriendsManager";

    /**
     * 设置好友监听
     * @param listener 好友监听器
     */
    public static void setFriendListener(V2TIMFriendshipListener listener){
        V2TIMManager.getFriendshipManager().setFriendListener(listener);
    }

    /**
     * 添加好友
     * @param friendName 要添加的好友名
     */
    public static void addFriend(String friendName) {
        addFriend(friendName,null);
    }
    /**
     * 添加好友
     * @param friendName 要添加的好友名
     * @param addWording 添加好友时发出的申请信息
     */
    public static void addFriend(String friendName,String addWording){
        V2TIMFriendAddApplication application = new V2TIMFriendAddApplication(friendName);
        application.setAddWording(addWording);
        V2TIMManager.getFriendshipManager().addFriend(application,null);
    }

    /**
     * 同意好友申请
     * @param userName 需要同意的好友名
     * @param userAddWording 好友发出的申请消息
     */
    public static void acceptFriendApplication(String userName,String userAddWording){
        getFriendApplicationList(new V2TIMValueCallback<V2TIMFriendApplicationResult>(){
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError: " + s );
            }

            @Override
            public void onSuccess(V2TIMFriendApplicationResult v2TIMFriendApplicationResult) {
                int applicationNum = v2TIMFriendApplicationResult.getUnreadCount();
                //找出符合条件的好友申请
                List<V2TIMFriendApplication> applicationList = v2TIMFriendApplicationResult.getFriendApplicationList();
                for (V2TIMFriendApplication application : applicationList){
                    if(application.getUserID().equals(userName)
                            && application.getAddWording().equals(userAddWording)
                            && application.getType() == V2TIMFriendApplication.V2TIM_FRIEND_APPLICATION_COME_IN){
                        //默认双边好友
                        V2TIMManager.getFriendshipManager().acceptFriendApplication(application,
                                V2TIMFriendApplication.V2TIM_FRIEND_ACCEPT_AGREE_AND_ADD,null);
                    }
                }
            }
        });
    }

    /**
     * 拒绝好友申请
     * @param userName 需要拒绝的好友名
     * @param userAddWording 好友发出的申请消息
     */
    public static void refuseFriendApplication(String userName,String userAddWording){
        getFriendApplicationList(new V2TIMValueCallback<V2TIMFriendApplicationResult>(){
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError: " + s );
            }

            @Override
            public void onSuccess(V2TIMFriendApplicationResult v2TIMFriendApplicationResult) {
                int applicationNum = v2TIMFriendApplicationResult.getUnreadCount();
                //找出符合条件的好友申请
                List<V2TIMFriendApplication> applicationList = v2TIMFriendApplicationResult.getFriendApplicationList();
                for (V2TIMFriendApplication application : applicationList){
                    if(application.getUserID().equals(userName)
                            && application.getAddWording().equals(userAddWording)
                            && application.getType() == V2TIMFriendApplication.V2TIM_FRIEND_APPLICATION_COME_IN){
                        V2TIMManager.getFriendshipManager().refuseFriendApplication(application,null);
                    }
                }
            }
        });
    }

    /**
     * 设置添加好友需要验证
     */
    public static void setFriendNeedConfirm(){
        V2TIMUserFullInfo userFullInfo = new V2TIMUserFullInfo();
        userFullInfo.setAllowType(V2TIMUserFullInfo.V2TIM_FRIEND_NEED_CONFIRM);
        V2TIMManager.getInstance().setSelfInfo(userFullInfo,null);
    }

    /**
     * 获取当前好友申请列表
     * @param callback 回调函数
     */
    public static void getFriendApplicationList(V2TIMValueCallback<V2TIMFriendApplicationResult>callback){
        V2TIMManager.getFriendshipManager().getFriendApplicationList(callback);
    }


    /**
     * 获取当前用户的好友列表
     * @param callback 回调函数
     */
    public static void getFriendList(V2TIMValueCallback<List<V2TIMFriendInfo>> callback){
        V2TIMManager.getFriendshipManager().getFriendList(callback);
    }

    public static void deleteAllFriends(){
        V2TIMManager.getFriendshipManager().getFriendList(new V2TIMValueCallback<List<V2TIMFriendInfo>>(){


            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<V2TIMFriendInfo> v2TIMFriendInfos) {
                for (V2TIMFriendInfo friendInfo: v2TIMFriendInfos){
                    Log.e(TAG, "onSuccess: "+friendInfo.getUserID() );
                }
            }
        });
    }
}
