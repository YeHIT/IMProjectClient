package cn.yesomething.improjectclient.PageContact;

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

import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.List;

import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.chat.ChatActivity;
import cn.yesomething.improjectclient.manager.FriendsManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView contactList;
    private ArrayList<String> contactNames;
    private LinearLayoutManager layoutManager;
    private LetterListView LetterList;
    private ContactAdapter adapter;
    View mView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onAttach(Activity activity) {//当fragment附着在activity上时保存上下文
        super.onAttach(activity);
        this.mContext = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.contact_fragment, container, false);
        getFriendList();
        Log.e("HEHE", "Hello");
        return mView;
    }
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
                Toast.makeText(mContext, "click " + friendName, Toast.LENGTH_SHORT).show();
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
        FriendsManager.getFriendList(new V2TIMValueCallback<List<V2TIMFriendInfo>>(){


            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<V2TIMFriendInfo> v2TIMFriendInfos) {
                contactNames = new ArrayList<>();
                for (V2TIMFriendInfo friendInfo: v2TIMFriendInfos){
                    contactNames.add(friendInfo.getUserID());
                    Log.e(TAG, "onSuccess: "+friendInfo.getUserID() );
                }
                init(mView);
                adapter.notifyDataSetChanged();
            }
        });
    }
}