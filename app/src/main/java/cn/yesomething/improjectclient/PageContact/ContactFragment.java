package cn.yesomething.improjectclient.PageContact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.chat.ChatActivity;
import cn.yesomething.improjectclient.login.PopoLoginActivity;
import cn.yesomething.improjectclient.manager.FriendsManager;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MyServerManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {
    private Handler getFriendListHandler;
    private RecyclerView contactList;
    private ArrayList<String> contactNames;
    private LinearLayoutManager layoutManager;
    private LetterListView LetterList;
    private ContactAdapter adapter;

    View mView;
    private Context mContext;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        if(contactNames!= null ){
            getFriendList();
        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context mContext) {//当fragment附着在activity上时保存上下文
        super.onAttach(mContext);
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.contact_fragment, container, false);
        getFriendList();
        return mView;
    }

    //初始化界面
    public void init(View view){
        contactList = (RecyclerView) view.findViewById(R.id.contact_list_main);
        LetterList = (LetterListView) view.findViewById(R.id.letter_view_main);
        layoutManager = new LinearLayoutManager(mContext);
        adapter = new ContactAdapter(mContext, contactNames);
        contactList.setLayoutManager(layoutManager);
        contactList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        contactList.setAdapter(adapter);
        //定义点击导航栏箭头、字母或#时的回调
        LetterList.setCharacterListener(new LetterListView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                layoutManager.scrollToPositionWithOffset(adapter.getScrollPosition(character), 0);
            }

            @Override
            public void clickArrow() {
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onClick(String friendName) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), ChatActivity.class);
                intent.putExtra("friendName",friendName);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取好友列表
     */
    public void getFriendList(){
        getFriendListHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                contactNames = new ArrayList<>();
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //获取好友列表成功
                        if(responseCode.equals("200")){
                            //展示好友列表
                            JSONArray jsonArray = jsonObject.getJSONArray("friendsList");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject tempObj = jsonArray.getJSONObject(i);
                                String friendName = tempObj.getString("friendId");
                                contactNames.add(friendName);
                            }
                            init(mView);
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            String errorMessage = jsonObject.getString("errorMessage");
                            Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(mContext, "异常错误请稍后再试", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        MyServerManager.selectFriendsList(getFriendListHandler);
    }
}