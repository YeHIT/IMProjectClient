package cn.yesomething.improjectclient.chat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMTextElem;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private RecyclerView msgRecyclerView;
    private List<Msg> msgList = new ArrayList<>();
    private EmojiconEditText mEditEmojicon;//类似于TextView 的，只是它能在上面展示表情
    private boolean hasClick;
    private MsgAdapter adapter;
    //当前聊天界面的用户名
    String userId = "denwade";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        //配置聊天监听器
        initChatListener();
        //初始化界面，比如显示之前五条的聊天记录，目前还没聊天记录，所以为空
        initMsg();

        //todo 获取当前聊天界面的用户名
        userId = "denwade";

        //mEditEmojicon 就是 输入框，类似于TextVIew的东西
        mEditEmojicon = (EmojiconEditText) findViewById(R.id.editEmojicon);
        mEditEmojicon.setOnClickListener(this);
        findViewById(R.id.send_chat).setOnClickListener(this);
        findViewById(R.id.bt_test).setOnClickListener(this);
        findViewById(R.id.imageView).setOnClickListener(this);
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
            case R.id.imageView: {
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
                    sendMessageHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
                        @Override
                        public boolean handleMessage(@NonNull Message msg) {
                            String response = (String) msg.obj;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String responseCode = jsonObject.getString("responseCode");
                                //登录成功
                                if(responseCode.equals("200")){
                                    MessageManager.sendTextMessage(mContent,userId);
                                    showMsg(mContent,Msg.TYPE_SENT);
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
                    String userId = IMManager.getLoginUser();
                    String toId = "denwade";
                    Date messageDate = new Date();
                    MyServerManager.sendMessage(sendMessageHandler,userId,toId,messageDate,mContent);
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
                showMsg("你好!", Msg.TYPE_RECEIVED);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                break;
            }
            // 返回按钮
            case R.id.bt_back:{
                //Intent intent = new Intent(ChatActivity.this, TestActivity.class);
                //startActivity(intent);
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * 用于聊天的界面的初始化，比如用于显示最近的几条聊天记录,
     * 因为目前还没有聊天记录，所以这里先不加。
     */
    private void initMsg(){
        /*Msg msg1 = new Msg("Hello.",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg3 = new Msg("哈哈哈哈哈哈",Msg.TYPE_RECEIVED);
        msgList.add(msg3);*/
        /*sendMsg("Hello.",Msg.TYPE_RECEIVED);
        sendMsg("哈哈哈哈哈哈",Msg.TYPE_RECEIVED);*/

    }

    /***
     * 发送消息
     * @param content  消息的String
     * @param type_Msg 消息类型 (int) TYPE_SENT（发送）  TYPE_RECEIVED（接收）
     */
    public void showMsg(String content,int type_Msg){
        //解密消息
        String umContent = StringEscapeUtils.unescapeJava(content);
        Msg msg = new Msg(umContent,type_Msg);
        msgList.add(msg);
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
                showMsg(textElem.getText(),Msg.TYPE_RECEIVED);
                super.onRecvNewMessage(msg);
            }
        });
    }
}