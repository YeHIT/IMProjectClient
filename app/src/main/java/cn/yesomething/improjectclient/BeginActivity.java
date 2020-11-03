package cn.yesomething.improjectclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import cn.yesomething.improjectclient.login.PopoLoginActivity;
import cn.yesomething.improjectclient.manager.IMManager;

public class BeginActivity extends Activity {
    private static final String TAG = "BeginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_begin);
        //初始化总配置
        IMManager.initSDKConfig(this);
        //若已登录则跳转到主界面
        if(IMManager.isLoginIMService()){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        //未登录则到登录界面
        else {
            Intent intent = new Intent(getApplicationContext(), PopoLoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

}