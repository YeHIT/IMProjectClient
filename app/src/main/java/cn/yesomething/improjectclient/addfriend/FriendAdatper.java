package cn.yesomething.improjectclient.addfriend;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.FriendsManager;
import cn.yesomething.improjectclient.manager.MyServerManager;

public class FriendAdatper extends RecyclerView.Adapter<FriendAdatper.ViewHolder> {
    private static final String TAG = "FriendAdatper";
    private List<Friend> mFriendList;
    private Handler addFriendHandler;
    private Context mContext;
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

    public FriendAdatper(Context context,List<Friend> friendsList){
        mContext = context;
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
                //添加好友
                addFriend(friend,holder);
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
    private void addFriend(Friend friend,ViewHolder holder){
        addFriendHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String responseCode = jsonObject.getString("responseCode");
                    //登录成功
                    if(responseCode.equals("200")){
                        Toast.makeText(mContext, "同意了 "+friend.getAddUserID()+"的好友申请", Toast.LENGTH_LONG).show();
                        FriendsManager.acceptFriendApplication(friend.getAddUserID(),friend.getAddUserDes());
                        //通过和拒绝按钮消失
                        holder.friendAgreeBtn.setVisibility(View.GONE);
                        holder.friendRefuseBtn.setVisibility(View.GONE);
                        //文本显示
                        holder.friendAgreeOrNot.setVisibility(View.VISIBLE);
                        holder.friendAgreeOrNot.setText("已通过");
                    }
                    else {
                        String errorMessage = jsonObject.getString("errorMessage");
                        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        MyServerManager.addFriend(addFriendHandler,friend.getAddUserID());
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
