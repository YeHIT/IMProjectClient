package cn.yesomething.improjectclient.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import cn.yesomething.improjectclient.login.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.UrlManager;
import cn.yesomething.improjectclient.utils.MyConnectionToServer;


public class PopoLoginActivity extends Activity {
    private static final String TAG = "PopoLoginActivity";
    private static final int REQUEST_SIGNUP = 0;
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
        _loginButton.setOnClickListener(v -> loginView());
        _gotoSignUp_Button.setOnClickListener(v -> {
            // Start the Signup activity
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);//启动singup活动，返回时需要signup的数据
            finish();
            //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
//        initView();
    }


    public void loginView() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        //加载圈圈动画
        final ProgressDialog progressDialog = new ProgressDialog(PopoLoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("loading...");
        progressDialog.show();

        String email = _usernameText.getText().toString();
        String password = _pswText.getText().toString();
        Toast.makeText(getBaseContext(), email, Toast.LENGTH_LONG).show();
        // TODO: 此处添加身份验证逻辑

        //email为账户str password为密码str

        //身份验证通过后，登陆成功
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();//圈圈动画丢弃
                    }
                }, 3000);
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
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                Toast.makeText(getBaseContext(), "Login ！", Toast.LENGTH_LONG).show();
                this.finish();
            }
        }
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


    /**
     * 根据用户名密码判断能否登录
     * @param userName 用户名
     * @param userPassword 用户密码
     */
    private void login(String userName,String userPassword){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",userName);
            jsonObject.put("userPassword",userPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "onClick: " + jsonObject.toString() );

        loginHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if( Integer.parseInt(msg.obj.toString())== 0){
                    Toast.makeText(PopoLoginActivity.this,
                            "Fail",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(PopoLoginActivity.this,
                            "Success",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        MyConnectionToServer.getConnectionThread(loginHandler,
                UrlManager.myServer+UrlManager.userLoginUrl,
                jsonObject.toString()).start();
    }

}