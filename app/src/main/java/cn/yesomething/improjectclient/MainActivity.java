package cn.yesomething.improjectclient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.yesomething.improjectclient.manager.IMManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IMManager.initSDKConfig(this);

    }
}


