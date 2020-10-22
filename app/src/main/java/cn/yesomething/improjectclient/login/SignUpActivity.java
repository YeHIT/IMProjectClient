package cn.yesomething.improjectclient.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.MyServerManager;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    Handler registerHandler = null;
    @BindView(R.id.signup_username) EditText _usernameText;
    @BindView(R.id.signup_psw) EditText _pswText;
    @BindView(R.id.signup_psw_check) EditText _pswCheckText;
    @BindView(R.id.btn_create_account)TextView _create_account_Button;
    @BindView(R.id.btn_backto_login) Button _backToLogin_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signup);
        ButterKnife.bind(this);
        //注册事件绑定
        _create_account_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        _backToLogin_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 回到登录界面
                Intent intent = new Intent(getApplicationContext(), PopoLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void signUp() {
        //判断用户名密码的合法性
        if (!validate()) {
            onSignUpFailed("输入不合法");
            return;
        }
        //防止重复点击
        _create_account_Button.setEnabled(false);
        //加载动画
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        //获取用户名及密码
        String name = _usernameText.getText().toString();
        String password = _pswText.getText().toString();

        // 注册逻辑实现
        registerHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (String) msg.obj;
                Log.e(TAG, "handleMessage: " + response);
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
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(),PopoLoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 注册失败提示
     */
    public void onSignUpFailed(String failMessage) {
        Toast.makeText(getBaseContext(), failMessage, Toast.LENGTH_LONG).show();

        _create_account_Button.setEnabled(true);
    }

    /**
     * 检查输入合法性
     * @return
     */
    public boolean validate() {
        boolean valid = true;

        String name = _usernameText.getText().toString();
        String password = _pswText.getText().toString();
        String reEnterPassword = _pswCheckText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _pswText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _pswText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _pswCheckText.setError("Password Do not match");
            valid = false;
        } else {
            _pswCheckText.setError(null);
        }

        return valid;
    }
}