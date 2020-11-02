package cn.yesomething.improjectclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.yesomething.improjectclient.PageContact.ContactAdapter;
import cn.yesomething.improjectclient.PageContact.ContactFragment;
import cn.yesomething.improjectclient.PageContact.DividerItemDecoration;
import cn.yesomething.improjectclient.login.SignUpActivity;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.PageContact.LetterListView;

import cn.yesomething.improjectclient.PageMine.UserInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.PageContact.ContactFragment;
import cn.yesomething.improjectclient.addfriend.ContactNewFriendActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private mainFragmentPagerAdapter FragmentPagerAdapter;
    private List<Fragment> fragmentList; //保存界面的view


    @BindView(R.id.conversation)
    RadioButton _btnConversationPage;
    @BindView(R.id.contact)
    RadioButton _btnContactPage;
    @BindView(R.id.mine)
    RadioButton _btnMinePage;
    @BindView(R.id.add_friend_btn)
    ImageButton _btnAddFriend;
    @BindView(R.id.new_friend_coming)
    TextView _txtFriendComing;

    BroadcastReceiver newFriendReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            _txtFriendComing.setVisibility(View.VISIBLE);
            Log.e(TAG, "onReceive: "+context );
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//防止出现两个toolbar
        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);
        //IMManager.initSDKConfig(this);
        //设置监听器
        _btnConversationPage.setOnClickListener(v -> SelectConversationPage());
        _btnContactPage.setOnClickListener(v -> SelectContactPage());
        _btnMinePage.setOnClickListener(v -> SelectMinePage());
        _btnAddFriend.setOnClickListener(v->AddFriend());
        initViewPager();
        IntentFilter filter = new IntentFilter(BeginActivity.action);
        registerReceiver(newFriendReceiver,filter);
    }

    //viewpager选中对话列表界面
    private void SelectConversationPage(){
        Toast.makeText(this,"click ConversationPage",Toast.LENGTH_SHORT).show();
    }
    //viewpager选中通讯录列表界面
    private void SelectContactPage(){
        Toast.makeText(this,"click ContactPage",Toast.LENGTH_SHORT).show();
    }
    //viewpager选中用户信息设置界面
    private void SelectMinePage(){
        Toast.makeText(this,"click MinePage",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),UserInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void AddFriend(){
        Intent friendIntent = new Intent(this, ContactNewFriendActivity.class);
        startActivity(friendIntent);
    }



    //加载viewpager
    public void initViewPager(){
        fragmentList = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        FragmentPagerAdapter = new mainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(FragmentPagerAdapter);
        //viewPager.addOnPageChangeListener(this);
        //viewPager.setCurrentItem(1);
    }
}


