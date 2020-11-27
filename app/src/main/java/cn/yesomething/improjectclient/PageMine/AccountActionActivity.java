package cn.yesomething.improjectclient.PageMine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.PageContact.ContactAdapter;
import cn.yesomething.improjectclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.login.PopoLoginActivity;
import cn.yesomething.improjectclient.manager.IMManager;

public class AccountActionActivity extends AppCompatActivity {
    private static final String TAG = "AccountActionActivity";
    @BindView(R.id.btn_change_account)
    TextView _btnChangeAccount;
    @BindView(R.id.btn_exit)
    TextView _btnExit;
    @BindView(R.id.tv_backward)
    TextView _btnBack;
    @BindView(R.id.login_name)
    TextView _tvname;
    @BindView(R.id.show_nickname)
    TextView _tvnickname;
    @BindView(R.id.iv_user_pic_show)
    ImageView _ivpic;
    String username,usernickname,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//防止出现两个toolbar
        setContentView(R.layout.mine_account_action);

        ButterKnife.bind(this);
        _btnChangeAccount.setOnClickListener(v -> changeAccount());
        _btnExit.setOnClickListener(v -> exit());
        _btnBack.setOnClickListener(v -> this.finish());

        //Intent intent =getIntent();
        /*取出Intent中附加的数据*/
        username = getIntent().getStringExtra("username");
        usernickname = getIntent().getStringExtra("usernickname");
        url = getIntent().getStringExtra("url");
        Log.e(TAG, "onCreate:username: "+username+ " nickname:"+usernickname+"  url:"+url );
        initview();
    }

    private void initview() {
        if("default.jpg".equals(url)){//默认头像
            _ivpic.setImageDrawable(getResources().getDrawable((R.drawable.user_pic)));
        }
        else{
            Picasso.with(this)
                    .load(url)
                    .resize(70,70)
                    .into(_ivpic);
        }
        _tvnickname.setText(usernickname);
        _tvname.setText(username);
    }

    private void exit() {
        IMManager.logout();
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