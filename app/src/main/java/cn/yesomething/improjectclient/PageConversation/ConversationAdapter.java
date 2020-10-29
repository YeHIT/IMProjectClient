package cn.yesomething.improjectclient.PageConversation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.yesomething.improjectclient.PageContact.ContactAdapter;
import cn.yesomething.improjectclient.PageConversation.Conversation;
import cn.yesomething.improjectclient.R;

public class ConversationAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final String TAG = "ConversationAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String[] mConversationNames; // 联系人名称字符串数组
    private List<Conversation> mConversationList; // 对话List（根据时间排序）

    public ConversationAdapter(Context context, List<Conversation> ConversationList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mConversationList = ConversationList;

        handleConversation();//排序
    }

    private void handleConversation() {//根据最后一条消息时间时间排序
        mConversationList = new ArrayList<>();
        Collections.sort(mConversationList,
                new Comparator<Conversation>() {
                    @Override
                    public int compare(Conversation o1, Conversation o2) {
                        return (int)(o1.getmTime().getTime()-o2.getmTime().getTime());//Date类型比较差值是 微秒级
                    }
                });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConversationHolder(mLayoutInflater.inflate(R.layout.item_character, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            //实现clicklistener接口回调 返回对话联系人名字
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
//                    onItemClickListener.onClick(position);
                    onItemClickListener.onClick(mConversationList.get(position).getmName());
                }
            }
        });
    }

    public class ConversationHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ConversationHolder(View view) {
            super(view);
            Log.e(TAG, "ConversationHolder: " );
            mTextView = (TextView) view.findViewById(R.id.contact_name);
        }
    }

    @Override
    public int getItemCount() {
        return mConversationList == null ? 0 : mConversationList.size();
    }



    /**
     * 定义RecyclerView选项单击事件的回调接口
     */
    public interface OnItemClickListener{//也可以不在这个activity或者是fragment中来声明接口，可以在项目中单独创建一个interface，就改成static就OK
        //参数（父组件，当前单击的View,单击的View的位置，数据）
        void onClick(String friendname);

    }
    private OnItemClickListener onItemClickListener;//声明一下接口
    //提供setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
