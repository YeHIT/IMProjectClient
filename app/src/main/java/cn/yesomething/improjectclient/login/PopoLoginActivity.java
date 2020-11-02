package cn.yesomething.improjectclient.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tencent.imsdk.v2.V2TIMCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.BeginActivity;
import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MyServerManager;


public class PopoLoginActivity extends Activity {
    private static final String TAG = "PopoLoginActivity";
    Handler loginHandler;
    @BindView(R.id.login_username) EditText _usernameText;
    @BindView(R.id.login_psw) EditText _pswText;
    @BindView(R.id.btn_login)TextView _loginButton;
    @BindView(R.id.btn_login_signup)Button _gotoSignUp_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        //登录事件绑定
        _loginButton.setOnClickListener(v -> loginView());
        _gotoSignUp_Button.setOnClickListener(v -> {
            // 从登录界面跳转到注册界面
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * 登录功能实现
     */
    public void loginView() {
        //判断用户名密码的合法性
        if (!validate()) {
            onLoginFailed("输入不合法");
            return;
        }
        //防止重复点击
        _loginButton.setEnabled(false);
        //加载圈圈动画
        final ProgressDialog progressDialog = new ProgressDialog(PopoLoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        //获取用户名及密码
        String userName = _usernameText.getText().toString();
        String password = _pswText.getText().toString();
        //登录逻辑实现
        loginHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String responseCode = jsonObject.getString("responseCode");
                    //登录成功
                    if(responseCode.equals("200")){
                        //todo 接入TIMSDK
                        IMManager.login(PopoLoginActivity.this, userName, new V2TIMCallback() {
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(getBaseContext(), "登录异常,请稍后再试", Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();//圈圈动画丢弃
                                Toast.makeText(getBaseContext(), "登录成功！", Toast.LENGTH_LONG).show();
                                //todo 测试用的跳转
                                Intent intent = new Intent(getApplicationContext(), BeginActivity.class);
                                startActivity(intent);
                                finish();
//                                onLoginSuccess();
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
        Toast.makeText(getBaseContext(), "Login successfully", Toast.LENGTH_LONG).show();
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

    /**
     * 判断输入的账号和密码的合法性
     * 账号不含有特殊字符
     * 密码长度
     * @return valid
     */
    public boolean validate() {
        boolean valid = true;

        String email = _usernameText.getText().toString();
        String password = _pswText.getText().toString();

        //判断 是否输入email address
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _usernameText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _usernameText.setError(null);
//        }
        //判断密码长度是否8-16位
        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            _pswText.setError("between 8 and 16 alphanumeric characters");
            valid = false;
        } else {
            _pswText.setError(null);
        }

        return valid;
    }




}