package cn.yesomething.improjectclient.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.yesomething.improjectclient.R;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    private RecyclerView msgRecyclerView;private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private EmojiconEditText mEditEmojicon;
    private Button send;
    private Button test;
    private Button back;
    private boolean hasClick;
    private MsgAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        initMsg();
        //inputText = (EditText) findViewById(R.id.input_text);
        //send = (Button) findViewById(R.id.send);
        mEditEmojicon = (EmojiconEditText) findViewById(R.id.editEmojicon);
        mEditEmojicon.setOnClickListener(this);
        findViewById(R.id.send_chat).setOnClickListener(this);
        findViewById(R.id.bt_test).setOnClickListener(this);
        findViewById(R.id.imageView).setOnClickListener(this);
        //findViewById(R.id.bt_back).setOnClickListener(this);
        //test = (Button) findViewById(R.id.bt_test);
        //back = (Button) findViewById(R.id.bt_back);

        setEmojiconFragment(false);

        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        /*
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                }
            }
        });*/

        /*
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendrandomText();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView: // 表情按钮
                if (hasClick) {
                    findViewById(R.id.emojicons).setVisibility(View.GONE);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                    findViewById(R.id.emojicons).setVisibility(View.VISIBLE);
                }
                hasClick = !hasClick;
                break;
            case R.id.send_chat:
                String content = mEditEmojicon.getText().toString();
                if(!"".equals(content)){
                    /*Msg msg = new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    mEditEmojicon.setText("");*/
                    sendMsg(content,Msg.TYPE_SENT);//封装起来，只要输入string就好了
                }
                else{
                    Toast.makeText(this,"Empty!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editEmojicon: // 输入框
                findViewById(R.id.emojicons).setVisibility(View.GONE);
                hasClick = !hasClick;
                break;
            case R.id.bt_test:
                sendrandomText();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                break;
            case R.id.bt_back:
                //Intent intent = new Intent(ChatActivity.this, TestActivity.class);
                //startActivity(intent);
                break;
            default:
                break;


        }
    }

    private void initMsg(){
        /*Msg msg1 = new Msg("Hello.",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg3 = new Msg("哈哈哈哈哈哈",Msg.TYPE_RECEIVED);
        msgList.add(msg3);*/
        /*sendMsg("Hello.",Msg.TYPE_RECEIVED);
        sendMsg("哈哈哈哈哈哈",Msg.TYPE_RECEIVED);*/
    }

    private void sendrandomText(){
        Msg msg = new Msg("test!",Msg.TYPE_RECEIVED);
        msgList.add(msg);
        adapter.notifyItemChanged(msgList.size()-1);
        msgRecyclerView.scrollToPosition(msgList.size()-1);
    }

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

    /***
     *
     * @param content  消息的String
     * @param type_Msg 消息类型 (int) TYPE_SENT  TYPE_RECEIVED
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
}