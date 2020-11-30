package cn.yesomething.improjectclient.addfriend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.FriendsManager;
import cn.yesomething.improjectclient.manager.MyServerManager;


public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView addUserId;
    private TextView addUserDes;
    boolean isFriend;
    @BindView(R.id.contact_new_back)
    Button _btnBack;

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
                /*if(!"".equals(contentID)){
                    Toast.makeText(this,contentID,Toast.LENGTH_SHORT).show();
                    FriendsManager.addFriend(contentID,contentDes);
                }
                else{
                    Toast.makeText(this,"Empty!",Toast.LENGTH_SHORT).show();
                }
                break;*/
                testUserAddFriend(contentID,contentDes);
                break;

            }
            default:{
                break;
            }
        }
    }

    public void testUserAddFriend(String userName,String contentDes){
        //一开始时记得声明handler
        Handler userSelectHandler = null;
        //todo 输入要查询用户的用户名
//        String userName = "xx";
        //用于获取最终的数据并展示
        userSelectHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                //网络正常
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //正常获取到数据
                        if(responseCode.equals("200")){
                            jsonObject = jsonObject.getJSONObject("user");
                            //todo 利用数据展示
                            testFriendSelect(userName,contentDes);
                            //Toast.makeText(AddFriendActivity.this ,userName,Toast.LENGTH_SHORT).show();
                            //FriendsManager.addFriend(userName,contentDes);


                        }
                        else if(responseCode.equals("503")){
                            Toast.makeText(AddFriendActivity.this ,"没有此用户"+userName,Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //todo 错误信息处理
                            String errorMessage = jsonObject.getString("errorMessage");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //todo 网络异常判断
                else {
                }
                return false;
            }
        });
        MyServerManager.userSelect(userSelectHandler,userName);
    }

    //好友关系查询
    public void testFriendSelect(String friendName,String contentDes){
        //一开始时记得声明handler
        Handler selectFriendHandler = null;

        //用于获取最终的数据并展示
        selectFriendHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                //网络正常
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        System.out.println(responseCode);
                        //todo 说明存在好友关系
                        if(responseCode.equals("200")){
                            Toast.makeText(AddFriendActivity.this ,friendName+"已经为你的好友",Toast.LENGTH_SHORT).show();
                        }
                        //不存在好友关系
                        else if (responseCode.equals("510")) {
                            Toast.makeText(AddFriendActivity.this ,"添加好友"+friendName,Toast.LENGTH_SHORT).show();
                            FriendsManager.addFriend(friendName,contentDes);
                        }
                        else{
                            //todo 错误信息处理
                            String errorMessage = jsonObject.getString("errorMessage");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //todo 网络异常判断
                else {
                }
                return false;
            }
        });
        MyServerManager.friendSelect(selectFriendHandler,friendName);
    }
}