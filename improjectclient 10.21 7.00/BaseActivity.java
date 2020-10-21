package cn.yesomething.improjectclient;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.tencent.imsdk.v2.V2TIMSDKListener;

public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    private static V2TIMSDKListener mV2TIMSDKListener = new V2TIMSDKListener() {
        @Override
        public void onKickedOffline() {
            Log.i(TAG, "您的帐号已在其它终端登录");
            super.onKickedOffline();
        }

        @Override
        public void onUserSigExpired() {
            Log.i(TAG, "账号已过期，请重新登录");
            super.onUserSigExpired();
        }
    };

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }
}