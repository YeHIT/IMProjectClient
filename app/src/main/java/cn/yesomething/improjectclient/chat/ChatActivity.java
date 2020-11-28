package cn.yesomething.improjectclient.chat;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMTextElem;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        initChatListener();
        //初始化界面，比如显示之前五条的聊天记录，目前还没聊天记录，所以为空
        initMsg(10);

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
                //当输入content不为空且长度不超过上限时
                if (!"".equals(content) && mContent.length() < 830) {
                    //禁用按钮
                    findViewById(R.id.send_chat).setEnabled(false);
                    mEditEmojicon.setText("");
                    Log.e(TAG, "onClick: " + mContent.length() );
                    sendMessageHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
                        @Override
                        public boolean handleMessage(@NonNull Message msg) {
                            String response = (String) msg.obj;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String responseCode = jsonObject.getString("responseCode");
                                //登录成功
                                if(responseCode.equals("200")){
                                    jsonObject = jsonObject.getJSONObject("message");
                                    Double messageEmotionalScore = jsonObject.getDouble("messageEmotionalScore");
                                    String messageTime = jsonObject.getString("messageTime");
                                    String messageContent = jsonObject.getString("processedContent");
                                    if(messageContent == null || "null".equals(messageContent)){
                                        messageContent = jsonObject.getString("messageContent");
                                    }
                                    MessageManager.sendTextMessage(messageTime + messageContent,friendName);
                                    showMsg(messageContent,Msg.TYPE_SENT,messageEmotionalScore,messageTime);
                                    adapter.notifyItemRangeChanged(0,msgList.size(),"send");
                                }
                                else {
                                    String errorMessage = jsonObject.getString("errorMessage");
                                    Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                //禁用按钮
                                findViewById(R.id.send_chat).setEnabled(true);
                            }
                            return false;
                        }
                    });
                    String userId = IMManager.getLoginUser();
                    String toId = friendName;
                    Date messageDate = new Date();
                    MyServerManager.sendMessage(sendMessageHandler,userId,toId,messageDate,mContent);
                }
                //输入为空时toast提示输入为空
                else if("".equals(content)){
                    Toast.makeText(this, "输入的消息不能为空!", Toast.LENGTH_SHORT).show();
                }
                else if(mContent.length() > 830){
                    Toast.makeText(this, "输入的消息过长!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // 输入框
            case R.id.editEmojicon: {
                findViewById(R.id.emojicons).setVisibility(View.GONE);//点击输入框时表情框会消失
                hasClick = !hasClick;
                break;
            }
            // 返回按钮
            case R.id.bt_back:{
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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
                            String messageContent = tempObj.getString("processedContent");
                            if(messageContent == null || "null".equals(messageContent)){
                                messageContent = tempObj.getString("messageContent");
                            }
                            String messageTime = tempObj.getString("messageTime");
                            Double messageEmotionalScore = tempObj.getDouble("messageEmotionalScore");
                            //消息是朋友发来的则自己为接收者
                            if(fromId.equals(friendName)){
                                showMsg(messageContent,Msg.TYPE_RECEIVED,messageEmotionalScore,messageTime);
                            }
                            else {
                                showMsg(messageContent,Msg.TYPE_SENT,messageEmotionalScore,messageTime);
                            }
                        }
                        //消息初始化完成时发送已读回执
                        markMessageAsRead();
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

    /***
     * 发送文字消息
     * @param content  消息的String
     * @param type_Msg 消息类型 (int) TYPE_SENT（发送）  TYPE_RECEIVED（接收）
     */
    public void showMsg(String content,int type_Msg ,Double msg_emotion,String messageTime){
        //格式转换
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        messageTime = simpleDateFormat.format(new Date(Long.parseLong(messageTime)));
        //解密消息
        String umContent = StringEscapeUtils.unescapeJava(content);
        umContent = StringEscapeUtils.unescapeJava(umContent);
        //这里是因为把 Msg类 重新构造了，加多了两个属性
        Msg msg = new Msg(umContent,type_Msg,Msg.TYPE_NOT_READ,messageTime,msg_emotion);
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
            //收到新消息
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {
                Handler listenNewMessageHandler ;
                V2TIMTextElem v2TIMTextElem = MessageManager.getMessage(msg);
                String content = v2TIMTextElem.getText();
                //消息时间
                Long messageDate = Long.parseLong(content.substring(0,13));
                //配置handler
                listenNewMessageHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
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
                                for(int i = 0; i < maxLength ; i++){
                                    JSONObject tempObj = jsonArray.getJSONObject(i);
                                    String fromId = tempObj.getString("fromId");
                                    String messageContent = tempObj.getString("processedContent");
                                    if(messageContent == null || "null".equals(messageContent)){
                                        messageContent = jsonObject.getString("messageContent");
                                    }
                                    String messageTime = tempObj.getString("messageTime");
                                    Double messageEmotionalScore = tempObj.getDouble("messageEmotionalScore");
                                    //消息是朋友发来的则自己为接收者
                                    if(fromId.equals(friendName)){
                                        showMsg(messageContent,Msg.TYPE_RECEIVED,messageEmotionalScore,messageTime);
                                    }
                                    else {
                                        showMsg(messageContent,Msg.TYPE_SENT,messageEmotionalScore,messageTime);
                                    }
                                }
                                //收到消息发送已读回执
                                markMessageAsRead();
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
                MyServerManager.selectMessageList(listenNewMessageHandler,IMManager.getLoginUser(),friendName,messageDate,messageDate);
                super.onRecvNewMessage(msg);
            }

            //收到消息已读通知
            @Override
            public void onRecvC2CReadReceipt(List<V2TIMMessageReceipt> receiptList) {
                // 由于接收方一次性可能会收到多个已读回执，所以这里采用了数组的回调形式
                for (V2TIMMessageReceipt v2TIMMessageReceipt : receiptList) {
                    // 消息接收者 receiver
                    String userID = v2TIMMessageReceipt.getUserID();
                    if(userID.equals(friendName)){
                        ChangeMsgReadType(Msg.TYPE_RECEIVED);
                        Context context = ChatActivity.this;
                        ActivityManager mAm = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                        String activityName = mAm.getRunningTasks(1).get(0).topActivity.getClassName();
                        if("cn.yesomething.improjectclient.chat.ChatActivity".equals(activityName)){
                            markMessageAsRead();
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置已读回执并将对方的信息设为已读
     */
    private void markMessageAsRead(){
        MessageManager.markMessageAsRead(friendName);
        ChangeMsgReadType(Msg.TYPE_SENT);
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
        adapter.notifyItemRangeChanged(0,msgList.size());
    }
}