package cn.yesomething.improjectclient.PageConversation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.yesomething.improjectclient.PageContact.ContactAdapter;
import cn.yesomething.improjectclient.PageContact.DividerItemDecoration;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.chat.ChatActivity;

public class ConversationFragment extends Fragment {
    private static final String TAG = "ConversationFragment";
    private Context mContext;
    private RecyclerView conversationView;

    private String[] mConversationNames; // 联系人名称字符串数组
    private List<Conversation> mConversationList; // 对话List
    private LinearLayoutManager layoutManager;

    private ConversationAdapter adapter;

    public ConversationFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.conversation_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        Log.e(TAG, "ConversationFragment: 加载联系人数据");
        mConversationList= new ArrayList<>();
        //向会话列表传数据
        Date d1 = new Date(System.currentTimeMillis());
        Conversation con1= new Conversation("黄建晔",d1,"我是聪明仔");
        mConversationList.add(con1);
//        Toast.makeText(mContext, "add " + mConversationList.get(0).getmName(), Toast.LENGTH_SHORT).show();
        Date d2 = new Date(System.currentTimeMillis()- (long)2 * 24 * 60 * 60 * 1000);
        Conversation con2= new Conversation("黄小晔",d2,"我是笨笨仔");
        mConversationList.add(con2);
        //-------
        conversationView = (RecyclerView) view.findViewById(R.id.conversation_list_main);
        layoutManager = new LinearLayoutManager(mContext);

        adapter = new ConversationAdapter(mContext, mConversationList);

        conversationView.setLayoutManager(layoutManager);
        conversationView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        Log.e(TAG, "ConversationFragment: 设置adapter");
        conversationView.setAdapter(adapter);

        //点击会话item时的回调函数
        adapter.setOnItemClickListener(new ConversationAdapter.OnItemClickListener() {
            @Override
            public void onClick(String friendname) {
                Toast.makeText(mContext, "click " + friendname, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
    }
}