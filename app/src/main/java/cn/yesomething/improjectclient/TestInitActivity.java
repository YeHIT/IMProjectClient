package cn.yesomething.improjectclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.yesomething.improjectclient.chat.ChatActivity;
import cn.yesomething.improjectclient.login.PopoLoginActivity;
import cn.yesomething.improjectclient.manager.IMManager;

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




}