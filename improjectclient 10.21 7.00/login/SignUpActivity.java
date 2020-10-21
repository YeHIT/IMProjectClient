package com.example.yesomething.improjectclient.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.login.PopoLoginActivity;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    @BindView(R.id.signup_username) EditText _usernameText;
    @BindView(R.id.signup_psw) EditText _pswText;
    @BindView(R.id.signup_psw_check) EditText _pswCheckText;
    @BindView(R.id.btn_create_account)TextView _create_account_Button;
    @BindView(R.id.btn_backto_login) Button _backtoLogin_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signup);
        ButterKnife.bind(this);

        _create_account_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _backtoLogin_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), PopoLoginActivity.class);
                startActivity(intent);
                finish();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _create_account_Button.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _usernameText.getText().toString();
        String password = _pswText.getText().toString();
        String reEnterPassword = _pswCheckText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _create_account_Button.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(),PopoLoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

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