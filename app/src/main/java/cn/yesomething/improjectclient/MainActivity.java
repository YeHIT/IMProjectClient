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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

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
//        _btnConversationPage.setOnClickListener(v -> SelectConversationPage());
//        _btnContactPage.setOnClickListener(v -> SelectContactPage());
//        _btnMinePage.setOnClickListener(v -> SelectMinePage());
        _btnAddFriend.setOnClickListener(v->AddFriend());
        initViewPager();
        IntentFilter filter = new IntentFilter(TestInitActivity.action);
        registerReceiver(newFriendReceiver,filter);
    }

    //viewpager选中对话列表界面
    private void SelectConversationPage(){

    }
    //viewpager选中通讯录列表界面
    private void SelectContactPage(){

    }
    //viewpager选中用户信息设置界面
    private void SelectMinePage(){

    }
    private void AddFriend(){
        Intent friendIntent = new Intent(this, ContactNewFriendActivity.class);
        startActivity(friendIntent);
    }


    //加载viewpager
    public void initViewPager(){
        fragmentList = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentList.add(new ContactFragment());
        fragmentList.add(new ContactFragment());
        //fragmentList.add(new MineFragment());
        FragmentPagerAdapter = new mainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(FragmentPagerAdapter);
        //viewPager.addOnPageChangeListener(this);
        //viewPager.setCurrentItem(1);
    }
}


