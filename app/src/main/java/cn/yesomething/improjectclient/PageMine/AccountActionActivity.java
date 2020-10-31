package cn.yesomething.improjectclient.PageMine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.login.PopoLoginActivity;

public class AccountActionActivity extends AppCompatActivity {
    private static final String TAG = "AccountActionActivity";
    @BindView(R.id.btn_change_account)
    TextView _btnChangeAccount;
    @BindView(R.id.btn_exit)
    TextView _btnExit;
    @BindView(R.id.tv_backward)
    TextView _btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//防止出现两个toolbar
        setContentView(R.layout.mine_account_action);

        ButterKnife.bind(this);
        _btnChangeAccount.setOnClickListener(v -> changeAccount());
        _btnExit.setOnClickListener(v -> exit());
        _btnBack.setOnClickListener(v -> this.finish());
    }

    private void exit() {
        Intent intent = new Intent(getApplicationContext(), PopoLoginActivity.class);
        startActivity(intent);
        //现在先不加动画
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);//左滑动画效果
        finish();
    }

    private void changeAccount() {
        Toast.makeText(this,"切换账号要在本地文件记录本机登录过的账号",Toast.LENGTH_SHORT).show();

    }
}