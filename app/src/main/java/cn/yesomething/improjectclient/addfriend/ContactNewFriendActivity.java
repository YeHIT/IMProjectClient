package cn.yesomething.improjectclient.addfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.yesomething.improjectclient.R;

public class ContactNewFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Friend> friendList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friend_activity);

        findViewById(R.id.contact_new_add_friend).setOnClickListener(this);
        findViewById(R.id.contact_new_back).setOnClickListener(this);
        //初始化添加好友消息
        initFriend();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contact_friend_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FriendAdatper friendadatper = new FriendAdatper(friendList);
        recyclerView.setAdapter(friendadatper);

    }

    private void initFriend(){
        addnewfriendMsg("denwade","Hello! make a friend!");
        addnewfriendMsg("dgb","Hello! ");
        addnewfriendMsg("hjy","嗦开嗦开!");
        addnewfriendMsg("ldy","不会真有人不加我吧？不会吧不会吧不会吧不会吧不会吧不会吧不会吧不会吧");
    }


    public void addnewfriendMsg(String addUserID,String addUserDes){
        //验证信息压缩，只显示前20个字符
        if(addUserDes.length()>20){
            addUserDes = addUserDes.substring(0,20)+"......";
        }
        Friend friend = new Friend(addUserID, addUserDes);
        //从头部加item，也就是新的消息优先放在顶部
        friendList.add(0,friend);//
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

            }
            default:{
                break;
            }
        }
    }
}