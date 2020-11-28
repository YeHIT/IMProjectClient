package cn.yesomething.improjectclient.PageConversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.CellIdentity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMTextElem;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.yesomething.improjectclient.PageContact.DividerItemDecoration;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.chat.ChatActivity;
import cn.yesomething.improjectclient.manager.MessageManager;

public class ConversationFragment extends Fragment {
    private static final String TAG = "ConversationFragment";
    private Context mContext;
    private RecyclerView conversationView;
    private View view;
    // 会话List
    private List<Conversation> mConversationList;
    private LinearLayoutManager layoutManager;
    private ConversationAdapter adapter;

    public ConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        if(view != null){
            getConversationList(view);
        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {//当fragment附着在activity上时保存上下文
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.conversation_fragment, container, false);
        //初始化会话监听器
        initConversationListener(view);
        //获取会话列表
        getConversationList(view);
        return view;
    }

    private void init(View view) {
        conversationView = (RecyclerView) view.findViewById(R.id.conversation_list_main);
        layoutManager = new LinearLayoutManager(mContext);
        adapter = new ConversationAdapter(mContext, mConversationList);
        conversationView.setLayoutManager(layoutManager);
        conversationView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        //设置adapter
        conversationView.setAdapter(adapter);
        //点击会话item时的回调函数
        adapter.setOnItemClickListener(new ConversationAdapter.OnItemClickListener() {
            @Override
            public void onClick(String friendName) {
                Toast.makeText(mContext, "click " + friendName, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra("friendName",friendName);
                intent.setClass(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 初始化会话监听器
     */
    private void initConversationListener(View view){
        MessageManager.setConversationListener(new V2TIMConversationListener() {
            @Override
            public void onNewConversation(List<V2TIMConversation> conversationList) {
                Log.e(TAG, new Date()+"onNewConversation: 收到新会话" );
                getConversationList(view);
            }

            @Override
            public void onConversationChanged(List<V2TIMConversation> conversationList) {
                Log.e(TAG, new Date()+"onConversationChanged: 会话改变了");
                getConversationList(view);
            }
        });
    }

    /**
     * 获取会话列表
     */
    private void getConversationList(View view){
        MessageManager.getConversationList(0, 50,
                new V2TIMValueCallback<V2TIMConversationResult>() {
                    @Override
                    public void onError(int code, String desc) {
                        // 拉取会话列表失败
                    }
                    @Override
                    public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                        Log.e(TAG, "onSuccess: 拉取会话列表成功！" );
                        mConversationList= new ArrayList<>();
                        for (V2TIMConversation timConversation : v2TIMConversationResult.getConversationList()) {
                            String userName = timConversation.getUserID();
                            //未读消息数量
                            Integer unreadCount = timConversation.getUnreadCount();
                            V2TIMTextElem lastMessage = timConversation.getLastMessage().getTextElem();
                            Long messageTime = null;
                            String textContent = null;
                            //消息为空则进入下一个循环
                            if(lastMessage == null){
                                continue;
                            }
                            else {
                                textContent = lastMessage.getText();
                            }
                            try {
                                messageTime = Long.parseLong(textContent.substring(0,13));
                                //如果0-13是时间才会执行以下语句
                                textContent = textContent.substring(13);
                            }
                            catch ( NumberFormatException | StringIndexOutOfBoundsException e){
                                textContent = textContent;
                            }
                            //如果没有消息时间则进入下一次循环
                            if(messageTime == null){
                                continue;
                            }
                            else {
                                //向会话列表传数据
                                Date messageDate = new Date(messageTime);
                                String umContent = StringEscapeUtils.unescapeJava(textContent);
                                umContent = StringEscapeUtils.unescapeJava(umContent);
                                Conversation conversation= new Conversation(userName,messageDate,umContent);
                                mConversationList.add(conversation);
                            }
                        }
                        init(view);
                    }
                });
    }
}