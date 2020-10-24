package cn.yesomething.improjectclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMFriendApplication;
import com.tencent.imsdk.v2.V2TIMFriendshipListener;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.Date;
import java.util.List;

import cn.yesomething.improjectclient.login.PopoLoginActivity;
import cn.yesomething.improjectclient.manager.FriendsManager;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MessageManager;
import cn.yesomething.improjectclient.manager.UserManager;

public class TestInitActivity extends Activity {
    private static final String TAG = "TestInitActivity";
    public static final String action = "jason.broadcast.action";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_init);
        //todo 暂放
        IMManager.initSDKConfig(this);

        Log.e(TAG, "onCreate: "+IMManager.isLoginIMService());
        if(IMManager.isLoginIMService()){
            showLoginUser();
            //初始化监听器
            initListener();
            //查看当前好友列表
            FriendsManager.deleteAllFriends();
            // 跳转到聊天界面
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else {
           // 从登录界面跳转到注册界面
            Intent intent = new Intent(getApplicationContext(), PopoLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 初始化各项监听器
     */
    private void initListener(){
        initConversationListener();
        initFriendsListener();
    }

    /**
     * 初始化会话监听器
     */
    private void initConversationListener(){
        MessageManager.setConversationListener(new V2TIMConversationListener() {
            @Override
            public void onNewConversation(List<V2TIMConversation> conversationList) {
                Log.e(TAG, new Date()+"onNewConversation: 收到新会话" );
            }

            @Override
            public void onConversationChanged(List<V2TIMConversation> conversationList) {
                Log.e(TAG, new Date()+"onConversationChanged: 会话改变了");
            }
        });
        Log.e(TAG, "initConversationListener: 初始化会话监听器成功" );
        MessageManager.getConversationList(0, 50,
                new V2TIMValueCallback<V2TIMConversationResult>() {
                    @Override
                    public void onError(int code, String desc) {
                        // 拉取会话列表失败
                    }
                    @Override
                    public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                        Log.e(TAG, "onSuccess: 拉取会话列表成功！" );
                    }
        });
    }

    /**
     * 初始化好友监听器
     */
    private void initFriendsListener(){
        //设置好友申请需要验证
        FriendsManager.setFriendNeedConfirm();
        FriendsManager.setFriendListener(new V2TIMFriendshipListener(){
            //好友申请新增通知,自己申请/别人申请
            @Override
            public void onFriendApplicationListAdded(List<V2TIMFriendApplication> applicationList) {
                Intent intent = new Intent(action);
                intent.putExtra("content","hello");
                sendBroadcast(intent);

                Log.e(TAG, "onFriendApplicationListAdded: 新好友申请" );
            }
            //好友申请被拒绝
            @Override
            public void onFriendApplicationListDeleted(List<String> userIDList) {
            }
        });
        Log.e(TAG, "initConversationListener: 初始化好友监听器成功" );
    }

    /**
     * 展示当前登录的用户信息
     */
    private void showLoginUser(){
        //查看已登录用户的信息
        UserManager.getUserInfo(IMManager.getLoginUser(),new V2TIMValueCallback<List<V2TIMUserFullInfo>>(){

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                V2TIMUserFullInfo v2TIMUserFullInfo = v2TIMUserFullInfos.get(0);
                Log.e(TAG, "onSuccess: "+v2TIMUserFullInfo.toString() );
            }
        });
    }


}