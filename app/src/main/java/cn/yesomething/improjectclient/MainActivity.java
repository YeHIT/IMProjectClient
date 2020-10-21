package cn.yesomething.improjectclient;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.yesomething.improjectclient.PageContact.ContactAdapter;
import cn.yesomething.improjectclient.PageContact.ContactFragment;
import cn.yesomething.improjectclient.PageContact.DividerItemDecoration;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.PageContact.LetterListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView contactList;
    private String[] contactNames;
    private LinearLayoutManager layoutManager;
    private LetterListView LetterList;
    private ContactAdapter adapter;

    private ViewPager viewPager;
    private NewFragmentPagerAdapter FragmentPagerAdapter;
    private List<Fragment> fragmentList; //保存界面的view


    @BindView(R.id.conversation)
    RadioButton _btnConversationPage;
    @BindView(R.id.contact)
    RadioButton _btnContactPage;
    @BindView(R.id.mine)
    RadioButton _btnMinePage;


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
        contactNames = new String[] {"POPO助手", "李道一", "邓广博", "黄建晔","赵敏", "123",};
        contactList = (RecyclerView) findViewById(R.id.contact_list_main);
        LetterList = (LetterListView) findViewById(R.id.letter_view_main);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ContactAdapter(this, contactNames);

        contactList.setLayoutManager(layoutManager);
        contactList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        contactList.setAdapter(adapter);
        //定义点击导航栏箭头、字母或#时的回调
        LetterList.setCharacterListener(new LetterListView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                layoutManager.scrollToPositionWithOffset(adapter.getScrollPosition(character), 0);
            }

            @Override
            public void clickArrow() {
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
        initViewPager();
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
    //加载viewpager
    public void initViewPager(){
        fragmentList = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentList.add(new ContactFragment());
        //fragmentList.add(new MineFragment());
        FragmentPagerAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(FragmentPagerAdapter);
        //viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }
}


