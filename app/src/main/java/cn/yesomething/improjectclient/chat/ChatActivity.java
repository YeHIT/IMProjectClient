package cn.yesomething.improjectclient.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMTextElem;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MessageManager;
import cn.yesomething.improjectclient.manager.MyServerManager;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private static final String TAG = "ChatActivity";
    private Handler sendMessageHandler;
    private Handler getMessageListHandler;
    private RecyclerView msgRecyclerView;
    private List<Msg> msgList = new ArrayList<>();
    private EmojiconEditText mEditEmojicon;//类似于TextView 的，只是它能在上面展示表情
    private boolean hasClick;
    private MsgAdapter adapter;
    private String friendName;
    @BindView(R.id.bt_back)
    ImageView _btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){getSupportActionBar().hide(); }//隐藏原生actionbar
        setContentView(R.layout.chat_main);
        friendName = getIntent().getStringExtra("friendName");


        ButterKnife.bind(this);
        _btnBack.setOnClickListener(v -> this.finish());
        //配置聊天监听器
//        initChatListener();
        //初始化界面，比如显示之前五条的聊天记录，目前还没聊天记录，所以为空
//        initMsg(10);

        //mEditEmojicon 就是 输入框，类似于TextVIew的东西
        mEditEmojicon = (EmojiconEditText) findViewById(R.id.editEmojicon);
        mEditEmojicon.setOnClickListener(this);
        findViewById(R.id.bt_back).setOnClickListener(this);
        findViewById(R.id.send_chat).setOnClickListener(this);
        findViewById(R.id.bt_test).setOnClickListener(this);
        findViewById(R.id.send_icon).setOnClickListener(this);
        //Emoji库的设置
        setEmojiconFragment(false);

        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 表情按钮的点击事件
            case R.id.send_icon: {
                if (hasClick) {
                    findViewById(R.id.emojicons).setVisibility(View.GONE);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                    findViewById(R.id.emojicons).setVisibility(View.VISIBLE);
                }
                hasClick = !hasClick;
                break;
            }
            // todo 发送信息
            //点击发送，将 mEditEmojicon文本框内的text发出
            case R.id.send_chat: {
                //获取消息以及对应的加密消息
                String content = mEditEmojicon.getText().toString();
                //加密
                String mContent = StringEscapeUtils.escapeJava(content);
                if (!"".equals(content)) {//当输入content不为空的时候
                    /*sendMessageHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
                        @Override
                        public boolean handleMessage(@NonNull Message msg) {
                            String response = (String) msg.obj;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String responseCode = jsonObject.getString("responseCode");
                                //登录成功
                                if(responseCode.equals("200")){
                                    MessageManager.sendTextMessage(mContent,friendName);
                                    showMsg(mContent,Msg.TYPE_SENT);
                                    adapter.notifyItemRangeChanged(0,msgList.size(),"send");
                                }
                                else {
                                    String errorMessage = jsonObject.getString("errorMessage");
                                    Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    });*/
                    //把上面的最重要的提取出来，就是 showMsg()
                    showMsg(mContent,Msg.TYPE_SENT,0);
                    //消息列表已读未读变化
                    MsgListTypeChange(Msg.TYPE_SENT);
                    String userId = IMManager.getLoginUser();
                    String toId = friendName;
                    Date messageDate = new Date();
                    //MyServerManager.sendMessage(sendMessageHandler,userId,toId,messageDate,mContent);

                } else {//否则toast提示输入为空
                    Toast.makeText(this, "Empty!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // 输入框
            case R.id.editEmojicon: {
                findViewById(R.id.emojicons).setVisibility(View.GONE);//点击输入框时表情框会消失
                hasClick = !hasClick;
                break;
            }
            // 测试按钮，点击会接收一条消息
            case R.id.bt_test: {
                showMsg("你好!", Msg.TYPE_RECEIVED,1);
                //消息列表已读未读变化
                MsgListTypeChange(Msg.TYPE_RECEIVED);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                break;
            }
            // 返回按钮
            case R.id.bt_back:{
                //showMsg("你好!", Msg.TYPE_RECEIVED);
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * 用于聊天的界面的初始化，比如用于显示最近的几条聊天记录,
     */
    private void initMsg(int maxMessageNum){
        getMessageListHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String responseCode = jsonObject.getString("responseCode");
                    //登录成功
                    if(responseCode.equals("200")){
                        JSONArray jsonArray = jsonObject.getJSONArray("messageList");
                        int maxLength = jsonArray.length();
                        //若总数组长度大于所需长度则起始位置i取 maxLength+1-maxMessageNum
                        //示例总长度为12 maxLength为11 最大所需信息为10 此时起始位置应为11-10+1即2
                        int i = (maxLength > maxMessageNum) ? maxLength + 1 - maxMessageNum: 0;
                        for(; i < maxLength ; i++){
                            JSONObject tempObj = jsonArray.getJSONObject(i);
                            String fromId = tempObj.getString("fromId");
                            String messageContent = tempObj.getString("messageContent");
                            String messageTime = tempObj.getString("messageTime");
                            Double messageEmotionalScore = tempObj.getDouble("messageEmotionalScore");
                            //消息是朋友发来的则自己为接收者
                            if(fromId.equals(friendName)){
                                showMsg(messageContent,Msg.TYPE_RECEIVED,messageEmotionalScore);
                            }
                            else {
                                showMsg(messageContent,Msg.TYPE_SENT,messageEmotionalScore);
                            }
                        }
                    }
                    else {
                        String errorMessage = jsonObject.getString("errorMessage");
                        Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        MyServerManager.selectMessageList(getMessageListHandler,friendName);
    }

    //adapter刷新item，修改recyclerview里面的变化的item
    //消息列表更新
    public void MsgListTypeChange(int Type){
        if(Type == Msg.TYPE_SENT){
            //将左侧接受到的消息设为已读
            adapter.notifyItemRangeChanged(0,msgList.size(),"send");
        }
        else{
            //右侧发送消息已被对方读了
            adapter.notifyItemRangeChanged(0,msgList.size(),"recieve");
        }
    }

    /***
     * 发送文字消息
     * @param content  消息的String
     * @param type_Msg 消息类型 (int) TYPE_SENT（发送）  TYPE_RECEIVED（接收）
     */
    public void showMsg(String content,int type_Msg ,double msg_emotion){
        //解密消息
        String umContent = StringEscapeUtils.unescapeJava(content);
        umContent = StringEscapeUtils.unescapeJava(umContent);
        //Msg msg = new Msg(umContent,type_Msg);
        //这里是因为把 Msg类 重新构造了，加多了两个属性
        Msg msg = new Msg(umContent,type_Msg,Msg.TYPE_NOT_READ,getRecentTime(),msg_emotion);
        msgList.add(msg);
        //修改 msglist列表里的消息的 已读未读属性，根据是发送的还是接收的来修改不用的消息
        ChangeMsgReadType(type_Msg);
        if(msgList.size()>0){
            adapter.notifyItemChanged(msgList.size()-1);
            msgRecyclerView.scrollToPosition(msgList.size()-1);
        }
        mEditEmojicon.setText("");
    }

    //下面三个重写是原本就有的，不用管
    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);
    }

    @Override
    public void onBackPressed() {
        if(hasClick){
            findViewById(R.id.emojicons).setVisibility(View.GONE);
            hasClick = !hasClick;
        }else {
            super.onBackPressed();
        }
    }

    /*表情库的设置*/
    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    /**
     * 配置聊天监听器
     */
    private void initChatListener(){
        MessageManager.addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {
                V2TIMTextElem textElem = MessageManager.getMessage(msg);
                Log.e(TAG, "onRecvNewMessage: " + textElem.getText());
                showMsg(textElem.getText(),Msg.TYPE_RECEIVED,0);
                super.onRecvNewMessage(msg);

            }
        });
    }

    //改变消息状态
    //这里设置只是为了体现 未读->已读 功能
    //实际上是进入聊天对话栏 就要不对方发送过来的未读消息设置为已读
    //这里我只是 通过showMsg()发送消息 去触发 未读->已读 功能
    private void ChangeMsgReadType(int type_Msg){
        //你现在接收到消息，意味着你之前发送的消息已读，将所有对方发送的 消息的属性 设置为 已读
        if(type_Msg == Msg.TYPE_RECEIVED){
            for(Msg msg:msgList){
                if(msg.getType()==Msg.TYPE_SENT){
                    msg.ChangeMsgReadType(Msg.TYPE_READ);
                }
            }
        }
        else {
            for(Msg msg:msgList){
                if(msg.getType()==Msg.TYPE_RECEIVED){
                    msg.ChangeMsgReadType(Msg.TYPE_READ);
                }
            }
        }
    }

    //就是获得当前系统的时间
    public String getRecentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return  simpleDateFormat.format(date);
    }


}