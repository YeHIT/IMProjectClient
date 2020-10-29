package cn.yesomething.improjectclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.yesomething.improjectclient.PageConversation.ConversationFragment;
import cn.yesomething.improjectclient.login.SignUpActivity;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.PageContact.LetterListView;

import cn.yesomething.improjectclient.PageMine.UserInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        initViewPager();
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

    //加载viewpager
    public void initViewPager(){
        fragmentList = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //添加Fragment
        fragmentList.add(new ContactFragment());
        //fragmentList.add(new ConversationFragment());
        fragmentList.add(new ContactFragment());
        //fragmentList.add(new MineFragment());
        FragmentPagerAdapter = new mainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(FragmentPagerAdapter);
        //viewPager.addOnPageChangeListener(this);
        //viewPager.setCurrentItem(1);
    }
}


