package cn.yesomething.improjectclient.addfriend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.imsdk.v2.V2TIMFriendApplication;
import com.tencent.imsdk.v2.V2TIMFriendApplicationResult;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.List;

import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.FriendsManager;

public class ContactNewFriendActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ContactNewFriendActivit";
    private List<Friend> friendList = new ArrayList<>();

    @Override
    public void onResume() {
        initFriend();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//防止出现两个toolbar
        setContentView(R.layout.new_friend_activity);
        //添加好友按钮
        findViewById(R.id.contact_new_add_friend).setOnClickListener(this);
        findViewById(R.id.contact_new_back).setOnClickListener(this);
        //初始化添加好友消息
        initFriend();
    }

    /**
     * 初始化当前界面的好友申请消息列表
     */
    private void initFriend(){
        //获取好友申请消息列表并展示
        FriendsManager.getFriendApplicationList(new V2TIMValueCallback<V2TIMFriendApplicationResult>(){
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError: " + s );
            }

            @Override
            public void onSuccess(V2TIMFriendApplicationResult v2TIMFriendApplicationResult) {
                friendList = new ArrayList<>();
                int applicationNum = v2TIMFriendApplicationResult.getUnreadCount();
                Log.e(TAG, "onSuccess: 获取好友申请列表成功" + applicationNum);
                //展示好友申请列表
                List<V2TIMFriendApplication> applicationList = v2TIMFriendApplicationResult.getFriendApplicationList();
                for (int i = 0; i < applicationNum; i++) {
                    V2TIMFriendApplication application = applicationList.get(i);
                    addNewFriendMsg(application.getUserID(),application.getAddWording());
                }
            }
        });
    }


    /**
     * 根据好友名与好友申请信息展示
     * @param addUserID 好友名
     * @param addUserDes 好友申请信息
     */
    public void addNewFriendMsg(String addUserID,String addUserDes){
        //验证信息压缩，只显示前20个字符
        if(addUserDes.length()>20){
            addUserDes = addUserDes.substring(0,20)+"......";
        }
        Friend friend = new Friend(addUserID, addUserDes);
        //从头部加item，也就是新的消息优先放在顶部
        friendList.add(0,friend);
        //刷新布局
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contact_friend_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FriendAdatper friendadatper = new FriendAdatper(this,friendList);
        recyclerView.setAdapter(friendadatper);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contact_new_add_friend:{
                Intent intent = new Intent(this, AddFriendActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.contact_new_back:{
                this.finish();
                break;
            }
            default:{
                break;
            }
        }
    }
}