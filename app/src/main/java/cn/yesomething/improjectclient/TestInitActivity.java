package cn.yesomething.improjectclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.List;

import cn.yesomething.improjectclient.chat.ChatActivity;
import cn.yesomething.improjectclient.login.PopoLoginActivity;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.UserManager;

public class TestInitActivity extends Activity {
    private static final String TAG = "TestInitActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_init);
        //todo 暂放
        IMManager.initSDKConfig(this);
        Log.e(TAG, "onCreate: "+IMManager.isLoginIMService());
        if(IMManager.isLoginIMService()){
            showLoginUser();
            // 跳转到聊天界面
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
        }
        else {
           // 从登录界面跳转到注册界面
            Intent intent = new Intent(getApplicationContext(), PopoLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

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