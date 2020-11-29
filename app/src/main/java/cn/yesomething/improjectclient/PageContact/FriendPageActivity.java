package cn.yesomething.improjectclient.PageContact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.PageConversation.ConversationAdapter;
import cn.yesomething.improjectclient.PageMine.WordCloudActivity;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.chat.ChatActivity;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MyServerManager;

public class FriendPageActivity extends AppCompatActivity {
    private static final String TAG = "FriendPageActivity";
    private Context mContext;
    private String username;
    private String userIconURL;
    private String spUserName;

    Bitmap right_bitmap,left_bitmap;

    @BindView(R.id.login_name)
    TextView tv_login_name;
    @BindView(R.id.tv_show_id)
    TextView tv_Show_id;
    @BindView(R.id.tv_info_nickName)
    TextView tv_nickname;
    @BindView(R.id.tv_info_sex)
    TextView tv_sex;
    @BindView(R.id.tv_backward)
    TextView tv_back;
    @BindView(R.id.iv_user_pic_show)
    ImageView iv_head_icon;
    @BindView(R.id.rl_chat)
    RelativeLayout rl_gotochat;
    @BindView(R.id.rl_wordcloud)
    RelativeLayout rl_gotowordcloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mContext = this;
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//防止出现两个toolbar
        setContentView(R.layout.activity_friend_page);

        username = getIntent().getStringExtra("friendName");
        Log.e(TAG, "onCreate: friendName"+username );
        //获取当前用户名
        spUserName= IMManager.getLoginUser();
        Log.e(TAG, "onCreate: myName"+spUserName );
        ButterKnife.bind(this);
        initlistener();
        testUserSelect(username);
        testUserSelect(spUserName);
    }

    private void initlistener() {
        rl_gotochat.setOnClickListener(v->gotochat());
        tv_back.setOnClickListener(v->this.finish());
        //rl_gotowordcloud.setOnClickListener(v->gotowordcloud());
    }

    private void gotochat() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("friendName",tv_login_name.getText().toString());
        intent.putExtra("lefticon",left_bitmap);
        intent.putExtra("righticon",right_bitmap);
        startActivity(intent);
    }

    private void gotowordcloud() {
        Intent intent = new Intent(this, WordCloudActivity.class);
        intent.putExtra("username",tv_login_name.getText().toString());
        intent.putExtra("usernickname",tv_Show_id.getText().toString());
        intent.putExtra("url",userIconURL);
        //startActivity(intent);
    }


    public void testUserSelect(String userName){
        //一开始时记得声明handler
        Handler userSelectHandler = null;
        //用于获取最终的数据并展示
        userSelectHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                Log.e(TAG, "handleMessage: "+response );

                Target mTarget = new Target() {
                    //图片加载成功
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            Log.e(TAG, "onBitmapLoaded: 获取到bitmap 用户id：" + userName );
                            if(spUserName.equals(userName)){//保存本人头像
                                right_bitmap =  bitmap;
                            }else{//设置conversation对象的头像
                                iv_head_icon.setImageBitmap(bitmap);
                                left_bitmap =  bitmap;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {                                }
                };

                //网络正常
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //正常获取到数据
                        if(responseCode.equals("200")){
                            jsonObject = jsonObject.getJSONObject("user");
                            //todo 利用数据展示
                            String userName = jsonObject.getString("userName");
                            Integer userSex = jsonObject.getInt("userSex");
                            String userNickname = jsonObject.getString("userNickname");


                            //用户头像为网络地址url
                            String userPicture = jsonObject.getString("userPicture");
                            userIconURL = userPicture;//保存url，以便后续传给子活动

                            if("default.jpg".equals(userPicture)){//默认头像
                                Drawable drawable_default_icon = mContext.getResources().getDrawable((R.drawable.user_pic));
                                if(spUserName.equals(userName)){//本人
                                    Log.e(TAG, "handleMessage: 加载本人头像："+userName+ " 头像未设置，加载默认头像");
                                }else{//conversation对象
                                    tv_nickname.setText(userNickname);
                                    tv_login_name.setText(userName);
                                    tv_Show_id.setText("昵称："+userNickname);
                                    if(userSex==0){tv_sex.setText("男"); }
                                    else{tv_sex.setText("女");}
                                    iv_head_icon.setImageDrawable(drawable_default_icon);
                                    Log.e(TAG, "handleMessage: 加载头像： 好友id："+userName+ " 的头像未设置，加载默认头像");
                                }
                            }
                            else{
                                if(spUserName.equals(userName)){//本人
                                    //啥也不做
                                }else{//conversation对象
                                    tv_nickname.setText(userNickname);
                                    tv_login_name.setText(userName);
                                    tv_Show_id.setText("昵称："+userNickname);
                                    if(userSex==0){tv_sex.setText("男"); }
                                    else{tv_sex.setText("女");}
                                    Picasso.with(mContext)
                                            .load(userPicture)
                                            .resize(80,80)
                                            .into(mTarget);
                                    Log.e(TAG, "handleMessage: 加载头像： 好友id："+userName+ " 的头像已设置，加载好友头像");
                                 }
                            }

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
}