package cn.yesomething.improjectclient.PageContact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.chat.ChatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {



    private RecyclerView contactList;
    private String[] contactNames;
    private LinearLayoutManager layoutManager;
    private LetterListView LetterList;
    private ContactAdapter adapter;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        init(view);
        return view;
    }
    public void init(View view){
        contactNames = new String[] {"POPO助手", "李道一", "邓广博", "黄建晔1","黄建晔2","黄建晔3","赵敏", "123",};
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
            public void onClick(String friendname) {
                Toast.makeText(mContext, "click " + friendname, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
    }

}