package cn.yesomething.improjectclient.addfriend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.yesomething.improjectclient.R;

public class FriendAdatper extends RecyclerView.Adapter<FriendAdatper.ViewHolder> {

    private List<Friend> mFriendList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View friendview;
        ImageView friendImage;
        TextView friendID;
        TextView friendDes;
        Button friednAgreeAdd;
        Button friendRefuseAdd;
        TextView friendAgreeOrNot;

        public ViewHolder(View view){
            super(view);
            friendview = view;
            friendImage = (ImageView) view.findViewById(R.id.add_friend_image);
            friendID = (TextView) view.findViewById(R.id.add_friend_name);
            friendDes = (TextView) view.findViewById(R.id.add_friend_description);
            friednAgreeAdd = (Button) view.findViewById(R.id.add_friend_agree);
            friendRefuseAdd = (Button) view.findViewById(R.id.add_friend_refuse);
            friendAgreeOrNot = (TextView) view.findViewById(R.id.add_pass_or_not);
        }
    }

    public FriendAdatper(List<Friend> friendsList){
        mFriendList = friendsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_new_friend_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        //点击通过按钮
        holder.friednAgreeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positon = holder.getAdapterPosition();
                Friend friend = mFriendList.get(positon);
                //通过和拒绝按钮消失
                holder.friendRefuseAdd.setVisibility(View.GONE);
                holder.friednAgreeAdd.setVisibility(View.GONE);
                //文本显示
                holder.friendAgreeOrNot.setVisibility(View.VISIBLE);
                holder.friendAgreeOrNot.setText("已通过");
            }
        });
        //点击不通过按钮
        holder.friendRefuseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positon = holder.getAdapterPosition();
                Friend friend = mFriendList.get(positon);
                //通过和拒绝按钮消失
                holder.friendRefuseAdd.setVisibility(View.GONE);
                holder.friednAgreeAdd.setVisibility(View.GONE);
                //文本显示
                holder.friendAgreeOrNot.setVisibility(View.VISIBLE);
                holder.friendAgreeOrNot.setText("已拒绝");
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = mFriendList.get(position);
        holder.friendID.setText(friend.getAddUserID());
        holder.friendDes.setText(friend.getAddUserDes());
        holder.friendImage.setImageResource(R.drawable.emoji_261d);
}

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }
}
