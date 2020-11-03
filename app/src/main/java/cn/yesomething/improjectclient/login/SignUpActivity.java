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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.MyServerManager;

public class SignUpActivity extends Activity implements View.OnFocusChangeListener{
    Handler registerHandler = null;
    @BindView(R.id.signup_username) EditText _usernameText;
    @BindView(R.id.signup_psw) EditText _pswText;
    @BindView(R.id.signup_psw_check) EditText _pswCheckText;
    @BindView(R.id.btn_create_account)TextView _create_account_Button;
    @BindView(R.id.btn_backto_login) Button _backToLogin_Button;
    @BindView(R.id.signup_back) ImageView _signUpBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signup);
        ButterKnife.bind(this);
        //绑定监控输入事件
        _usernameText.setOnFocusChangeListener(this::onFocusChange);
        _pswText.setOnFocusChangeListener(this::onFocusChange);
        _pswCheckText.setOnFocusChangeListener(this::onFocusChange);
        //绑定回退事件
        _signUpBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //注册事件绑定
        _create_account_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        // 回到登录界面
        _backToLogin_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PopoLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void signUp() {
        //判断用户名密码的合法性
        if (!validate(_usernameText.getId()) || !validate(_pswText.getId()) || !validate(_pswCheckText.getId())) {
            onSignUpFailed("输入不合法");
            return;
        }
        //防止重复点击
        _create_account_Button.setEnabled(false);
        //加载动画
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("创建账号中...");
        progressDialog.show();
        //获取用户名及密码
        String name = _usernameText.getText().toString();
        String password = _pswText.getText().toString();
        // 注册逻辑实现
        registerHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //注册成功
                        if(responseCode.equals("200")){
                            progressDialog.dismiss();//圈圈动画丢弃
                            onSignUpSuccess();
                        }
                        else {
                            String errorMessage = jsonObject.getString("errorMessage");
                            progressDialog.dismiss();//圈圈动画丢弃
                            onSignUpFailed(errorMessage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    onSignUpFailed("异常错误请稍后再试");
                }
                return false;
            }
        });
        MyServerManager.register(registerHandler,name,password);
    }

    /**
     * 注册成功提示
     */
    public void onSignUpSuccess() {
        _create_account_Button.setEnabled(true);
        Toast.makeText(getBaseContext(),"注册成功！", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),PopoLoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 注册失败提示
     * @param failMessage 失败提示消息
     */
    public void onSignUpFailed(String failMessage) {
        Toast.makeText(getBaseContext(), failMessage, Toast.LENGTH_LONG).show();
        _create_account_Button.setEnabled(true);
    }

    /**
     * 检查输入合法性
     * @param viewId 需要判定的组件
     * @return 合法则返回true
     */
    public boolean validate(int viewId) {
        boolean valid = true;
        String name = _usernameText.getText().toString();
        String password = _pswText.getText().toString();
        String reEnterPassword = _pswCheckText.getText().toString();
        switch (viewId){
            case R.id.signup_username:{
                //账号长度判定
                if (name.isEmpty() || name.length() < 3) {
                    _usernameText.setError("账号长度至少为3");
                    valid = false;
                }
                else {
                    _usernameText.setError(null);
                }
                break;
            }
            case R.id.signup_psw:{
                //密码长度判定
                if (password.isEmpty() || password.length() < 8 || password.length() > 16) {
                    _pswText.setError("密码需在8到16位之间");
                    valid = false;
                }
                else {
                    _pswText.setError(null);
                }
                break;
            }
            case R.id.signup_psw_check:{
                //密码重复验证
                if (reEnterPassword == null || reEnterPassword.equals("") || !reEnterPassword.equals(password)) {
                    _pswCheckText.setError("两次输入的密码不一致");
                    valid = false;
                }
                else {
                    _pswCheckText.setError(null);
                }
                break;
            }
        }
        return valid;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //失去聚焦则判定输入
        if(hasFocus == false){
            validate(v.getId());
        }
    }
}