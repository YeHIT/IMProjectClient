package cn.yesomething.improjectclient.addfriend;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.FriendsManager;


public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView addUserId;
    private TextView addUserDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//防止出现两个toolbar
        setContentView(R.layout.new_friend_add_activity);

        addUserId = (TextView) findViewById(R.id.add_user_id);
        //findViewById(R.id.add_user_id).setOnClickListener(this);
        addUserDes = (TextView) findViewById(R.id.add_description);
        //findViewById(R.id.add_description).setOnClickListener(this);
        findViewById(R.id.add_friend_back).setOnClickListener(this);
        findViewById(R.id.add_friend_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回上个界面
            //这里不需要跳转到新活动，只用这个让这个活动finish后回到上一个活动
            case R.id.add_friend_back:{
               /* Intent intent = new Intent(this, AddFriendActivity.class);
                startActivity(intent);*/
                this.finish();
                break;
            }
            //发送好友申请
            case R.id.add_friend_send:{
                String contentID = addUserId.getText().toString();
                String contentDes = addUserDes.getText().toString();
                if(!"".equals(contentID)){
                    Toast.makeText(this,contentID,Toast.LENGTH_SHORT).show();
                    FriendsManager.addFriend(contentID,contentDes);
                }
                else{
                    Toast.makeText(this,"Empty!",Toast.LENGTH_SHORT).show();
                }
                break;

            }
            default:{
                break;
            }
        }
    }
}