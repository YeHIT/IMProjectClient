package cn.yesomething.improjectclient.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import cn.yesomething.improjectclient.R;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private static final String TAG = "ChatActivity";
    private RecyclerView msgRecyclerView;
    private List<Msg> msgList = new ArrayList<>();
    private EmojiconEditText mEditEmojicon;//类似于TextView 的，只是它能在上面展示表情
    private boolean hasClick;
    private MsgAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);

        //初始化界面，比如显示之前五条的聊天记录，目前还没聊天记录，所以为空
        initMsg();

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
            // 表情按钮
            case R.id.imageView: {// 表情按钮
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
            case R.id.send_chat: {//点击发送，将 mEditEmojicon文本框内的text发出
                String content = "你好"+mEditEmojicon.getText().toString() + "你好";

                String mContent = StringEscapeUtils.escapeJava(content);
                String umContent = StringEscapeUtils.unescapeJava(mContent);
                Log.e(TAG, "onClick: " + content);
                Log.e(TAG, "onClick: " + mContent);
                Log.e(TAG, "onClick: " + umContent);
                if (!"".equals(content)) {//当输入content不为空的时候
                    sendMsg(content, Msg.TYPE_SENT);//封装起来，只要输入string就好了
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
                sendMsg("你好!", Msg.TYPE_RECEIVED);
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

    /*用于聊天的界面的初始化，比如用于显示最近的几条聊天记录，
    因为目前还没有聊天记录，所以这里先不加*/
    private void initMsg(){
        /*Msg msg1 = new Msg("Hello.",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg3 = new Msg("哈哈哈哈哈哈",Msg.TYPE_RECEIVED);
        msgList.add(msg3);*/
        /*sendMsg("Hello.",Msg.TYPE_RECEIVED);
        sendMsg("哈哈哈哈哈哈",Msg.TYPE_RECEIVED);*/

    }

    /***
     *
     * @param content  消息的String
     * @param type_Msg 消息类型 (int) TYPE_SENT（发送）  TYPE_RECEIVED（接收）
     */
    public void sendMsg(String content,int type_Msg){
        Msg msg = new Msg(content,type_Msg);
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
}