package cn.yesomething.improjectclient.addfriend;

import android.util.Log;
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
import cn.yesomething.improjectclient.manager.FriendsManager;

public class FriendAdatper extends RecyclerView.Adapter<FriendAdatper.ViewHolder> {
    private static final String TAG = "FriendAdatper";
    private List<Friend> mFriendList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View friendView;
        ImageView friendImage;
        TextView friendID;
        TextView friendDes;
        Button friendAgreeBtn;
        Button friendRefuseBtn;
        TextView friendAgreeOrNot;

        public ViewHolder(View view){
            super(view);
            friendView = view;
            friendImage = (ImageView) view.findViewById(R.id.add_friend_image);
            friendID = (TextView) view.findViewById(R.id.add_friend_name);
            friendDes = (TextView) view.findViewById(R.id.add_friend_description);
            friendAgreeBtn = (Button) view.findViewById(R.id.add_friend_agree);
            friendRefuseBtn = (Button) view.findViewById(R.id.add_friend_refuse);
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
                .inflate(R.layout.new_friend_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        //点击通过按钮
        holder.friendAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positon = holder.getAdapterPosition();
                Friend friend = mFriendList.get(positon);
                Log.e(TAG, "同意了 "+friend.getAddUserID()+"的好友申请" );
                FriendsManager.acceptFriendApplication(friend.getAddUserID(),friend.getAddUserDes());
                //通过和拒绝按钮消失
                holder.friendAgreeBtn.setVisibility(View.GONE);
                holder.friendRefuseBtn.setVisibility(View.GONE);
                //文本显示
                holder.friendAgreeOrNot.setVisibility(View.VISIBLE);
                holder.friendAgreeOrNot.setText("已通过");
            }
        });
        //点击不通过按钮
        holder.friendRefuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positon = holder.getAdapterPosition();
                Friend friend = mFriendList.get(positon);
                Log.e(TAG, "拒绝了 "+friend.getAddUserID()+"的好友申请" );
                FriendsManager.refuseFriendApplication(friend.getAddUserID(),friend.getAddUserDes());
                //通过和拒绝按钮消失
                holder.friendAgreeBtn.setVisibility(View.GONE);
                holder.friendRefuseBtn.setVisibility(View.GONE);
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
