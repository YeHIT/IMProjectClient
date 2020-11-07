package cn.yesomething.improjectclient.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tencent.imsdk.v2.V2TIMCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MyServerManager;


public class PopoLoginActivity extends Activity {
    Handler loginHandler;
    @BindView(R.id.login_username) EditText _usernameText;
    @BindView(R.id.login_psw) EditText _pswText;
    @BindView(R.id.btn_login)TextView _loginButton;
    @BindView(R.id.btn_login_signup)Button _gotoSignUp_Button;
    @BindView(R.id.login_back) ImageView _loginBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        //绑定回退事件
        _loginBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //登录事件绑定
        _loginButton.setOnClickListener(v -> loginView());
        _gotoSignUp_Button.setOnClickListener(v -> {
            // 从登录界面跳转到注册界面
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 登录功能实现
     */
    public void loginView() {
        //防止重复点击
        _loginButton.setEnabled(false);
        //加载圈圈动画
        final ProgressDialog progressDialog = new ProgressDialog(PopoLoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("登录中...");
        progressDialog.show();
        //获取用户名及密码
        String userName = _usernameText.getText().toString();
        String password = _pswText.getText().toString();
        //登录逻辑实现
        loginHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //登录成功
                        if(responseCode.equals("200")){
                            //使用TIMSDK登录
                            IMManager.login(PopoLoginActivity.this, userName, new V2TIMCallback() {
                                @Override
                                public void onError(int i, String s) {
                                    progressDialog.dismiss();//圈圈动画丢弃
                                    onLoginFailed("登录异常,请稍后再试");
                                }
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();//圈圈动画丢弃
                                    onLoginSuccess();
                                }
                            });
                        }
                        else {
                            String errorMessage = jsonObject.getString("errorMessage");
                            progressDialog.dismiss();//圈圈动画丢弃
                            onLoginFailed(errorMessage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    progressDialog.dismiss();//圈圈动画丢弃
                    onLoginFailed("异常错误请稍后再试");
                }
                return false;
            }
        });
        MyServerManager.login(loginHandler,userName,password);
    }

    /**
     * 登陆成功提示
     */
    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "登录成功！", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 登陆失败提示
     */
    public void onLoginFailed(String failMessage) {
        Toast.makeText(getBaseContext(), failMessage, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    /**
     * 返回上一个界面
     */
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
}