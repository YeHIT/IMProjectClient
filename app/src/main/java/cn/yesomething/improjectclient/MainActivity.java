package cn.yesomething.improjectclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMFriendApplication;
import com.tencent.imsdk.v2.V2TIMFriendshipListener;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.addfriend.ContactNewFriendActivity;
import cn.yesomething.improjectclient.manager.FriendsManager;
import cn.yesomething.improjectclient.manager.MessageManager;
import cn.yesomething.improjectclient.utils.Redbot;
import cn.yesomething.improjectclient.utils.Softkeybroad;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private mainFragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragmentList; //保存界面的view
    //几个代表页面的常量
    public static final int CONVERSATION_PAGE = 0;
    public static final int CONTACT_PAGE = 1;
    public static final int MINE_PAGE = 2;

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
    @BindView(R.id.toolbar)
    Toolbar _toolbar;
    @BindView(R.id.bottom)
    LinearLayout bottom_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//

        //情况A(有margin时)
        new Redbot(this)
                .setBadgeType(Redbot.Type.TYPE_POINT)
                .setBadgeOverlap(false)
                .bindToTargetView(_btnAddFriend);
        setContentView(R.layout.main_activity);
        //初始化监听器
        initListener();
        ButterKnife.bind(this);
        //设置监听器
        _btnConversationPage.setOnClickListener(v -> SelectConversationPage());
        _btnContactPage.setOnClickListener(v -> SelectContactPage());
        _btnMinePage.setOnClickListener(v -> SelectMinePage());
        _btnAddFriend.setOnClickListener(v->AddFriend());
        setSoftKeyBoardListener();


        //设置界面切换
        initViewPager();
    }

    /**
     * 添加软键盘监听
     */
    private void setSoftKeyBoardListener() {
        Softkeybroad mSoftkeybroad= new Softkeybroad(this);
        //软键盘状态监听
        mSoftkeybroad.setListener(new Softkeybroad.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Log.e(TAG, "keyBoardShow: 软键盘show" );
                bottom_bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                //软键盘已经隐藏,做逻辑
                Log.e(TAG, "keyBoardShow: 软键盘hide" );
                bottom_bar.setVisibility(View.VISIBLE);

            }
        });
    }

    //viewpager选中对话列表界面
    private void SelectConversationPage(){
        viewPager.setCurrentItem(CONVERSATION_PAGE);
    }

    //viewpager选中通讯录列表界面
    private void SelectContactPage(){
        viewPager.setCurrentItem(CONTACT_PAGE);
    }

    //viewpager选中用户信息设置界面
    private void SelectMinePage(){
        viewPager.setCurrentItem(MINE_PAGE);
    }

    //切换到添加好友界面
    private void AddFriend(){
        Intent friendIntent = new Intent(this, ContactNewFriendActivity.class);
        startActivity(friendIntent);
    }

    //加载viewpager
    public void initViewPager(){
        fragmentList = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentPagerAdapter = new mainFragmentPagerAdapter(this,getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        //选择会话界面
        onPageSelected(CONVERSATION_PAGE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (viewPager.getCurrentItem()) {
            case CONVERSATION_PAGE:
                _btnContactPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.contact_normal,0,0);
                _btnConversationPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.conversation_selected,0,0);
                _btnMinePage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.myself_normal,0,0);
                _toolbar.setVisibility(View.VISIBLE);
                break;
            case CONTACT_PAGE:
                _btnContactPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.contact_selected,0,0);
                _btnConversationPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.conversation_normal,0,0);
                _btnMinePage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.myself_normal,0,0);
                _btnConversationPage.setChecked(true);
                _toolbar.setVisibility(View.VISIBLE);
                break;
            case MINE_PAGE:
                _btnContactPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.contact_normal,0,0);
                _btnConversationPage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.conversation_normal,0,0);
                _btnMinePage.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.myself_selected,0,0);
                _toolbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case CONVERSATION_PAGE:
                    _btnContactPage.setChecked(true);
                    break;
                case CONTACT_PAGE:
                    _btnConversationPage.setChecked(true);
                    break;
                case MINE_PAGE:
                    _btnMinePage.setChecked(true);
                    break;
            }
        }
    }

    /**
     * 初始化各项监听器
     */
    private void initListener(){
        initFriendsListener();
    }



    /**
     * 初始化好友监听器
     */
    private void initFriendsListener(){
        //设置好友申请需要验证
        FriendsManager.setFriendNeedConfirm();
        FriendsManager.setFriendListener(new V2TIMFriendshipListener(){
            //好友申请新增通知,自己申请/别人申请
            @Override
            public void onFriendApplicationListAdded(List<V2TIMFriendApplication> applicationList) {
                Log.e(TAG, "onFriendApplicationListAdded: 新好友申请" );
            }
            //好友申请被拒绝
            @Override
            public void onFriendApplicationListDeleted(List<String> userIDList) {
            }
        });
        Log.e(TAG, "initConversationListener: 初始化好友监听器成功" );
    }
}


