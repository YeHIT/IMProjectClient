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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.PageMine.UserInfoActivity;
import cn.yesomething.improjectclient.addfriend.ContactNewFriendActivity;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private mainFragmentPagerAdapter FragmentPagerAdapter;
    private List<Fragment> fragmentList; //保存界面的view

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

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
        viewPager.setCurrentItem(PAGE_ONE);
        Toast.makeText(this,"click ConversationPage",Toast.LENGTH_SHORT).show();
    }
    //viewpager选中通讯录列表界面
    private void SelectContactPage(){
        viewPager.setCurrentItem(PAGE_TWO);
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

        FragmentPagerAdapter = new mainFragmentPagerAdapter(this,getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(FragmentPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (viewPager.getCurrentItem()) {
            case PAGE_ONE:
                Log.e(TAG, "onPageSelected: "+"1" );
                _btnContactPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.contact_normal,0,0);
                _btnConversationPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.conversation_selected,0,0);
                _btnMinePage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.myself_normal,0,0);

                break;
            case PAGE_TWO:
                Log.e(TAG, "onPageSelected: "+"2" );
                _btnContactPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.contact_selected,0,0);
                _btnConversationPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.conversation_normal,0,0);
                _btnMinePage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.myself_normal,0,0);

                _btnConversationPage.setChecked(true);
                break;
            case PAGE_THREE:
                Log.e(TAG, "onPageSelected: "+"3" );
                _btnContactPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.contact_normal,0,0);
                _btnConversationPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.conversation_normal,0,0);
                _btnMinePage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.myself_selected,0,0);


                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case PAGE_ONE:
                    Log.e(TAG, "onPageScrollStateChanged: "+"1" );
                    _btnContactPage.setChecked(true);
                    break;
                case PAGE_TWO:
                    Log.e(TAG, "onPageScrollStateChanged: "+"2" );

                    _btnConversationPage.setChecked(true);
                    break;
                case PAGE_THREE:
                    _btnMinePage.setChecked(true);
                    break;
            }
        }
    }

}


