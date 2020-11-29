package cn.yesomething.improjectclient.chat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.CookieHandler;
import java.util.List;

import cn.yesomething.improjectclient.R;


public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private static final String TAG = "MsgAdapter";
    private List<Msg> mMsgList;
    private Bitmap friendIcon;
    private Bitmap myIcon;
    public Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout leftLayout;
        private LinearLayout rightLayout;
        private TextView leftMsg;
        private TextView left_read_or_not;
        private TextView leftMsgTime;
        private TextView rightMsg;
        private TextView right_read_or_not;
        private TextView rightMsgTime;
        private ImageView leftMsgImg;
        private ImageView rightMsgImg;
        private ImageView leftEmotion;
        private ImageView rightEmotion;
        private ImageView leftIcon;
        private ImageView rightIcon;


        public ViewHolder(View view){
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
            left_read_or_not = (TextView) view.findViewById(R.id.received_read_or_not);
            right_read_or_not = (TextView) view.findViewById(R.id.send_read_or_not);

            leftMsgTime = (TextView) view.findViewById(R.id.left_msg_time);
            rightMsgTime = (TextView) view.findViewById(R.id.right_msg_time);

            leftEmotion = (ImageView) view.findViewById(R.id.received_emotion);
            rightEmotion = (ImageView) view.findViewById(R.id.send_emotion);
            leftMsgImg = (ImageView) view.findViewById(R.id.left_img);
            rightMsgImg = (ImageView) view.findViewById(R.id.right_img);

            leftIcon = (ImageView) view.findViewById(R.id.conversation_friend_pic);
            rightIcon = (ImageView) view.findViewById(R.id.conversation_my_pic);

        }

    }

    public MsgAdapter(Context context, List<Msg> msgList, Bitmap leftIcon,Bitmap RightIcon){
        mContext = context;
        mMsgList=msgList;
        friendIcon = leftIcon;
        myIcon = RightIcon;
        Log.e(TAG, "MsgAdapter:friendIcon "+friendIcon);
        Log.e(TAG, "MsgAdapter:myIcon "+myIcon );
        if(friendIcon == null) Log.e(TAG, "onClick:friendIcon null ");
        else  Log.e(TAG, "onClick:friendIcon !not! null ");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        double emotion = msg.getMsgEmotion();
        //holder.leftMsgImg.setImageDrawable(mContext.getResources().getDrawable((R.drawable.pic1)));
        //holder.rightMsgImg.setImageBitmap(myIcon);
        if(friendIcon==null)
            holder.leftIcon.setImageDrawable(mContext.getDrawable(R.drawable.user_pic));
        else
            holder.leftIcon.setImageBitmap(friendIcon);

        if(myIcon==null)
            holder.rightIcon.setImageDrawable(mContext.getDrawable(R.drawable.user_pic));
        else
            holder.rightIcon.setImageBitmap(myIcon);

        if (msg.getType() == Msg.TYPE_RECEIVED) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            if (msg.getReadType() == Msg.TYPE_NOT_READ) {
                holder.left_read_or_not.setText("未读");
            } else {
                holder.left_read_or_not.setText("已读");
            }
            holder.leftMsgTime.setText(msg.getMsgtime());
            if(emotion>0.7){
                holder.leftEmotion.setImageResource(R.drawable.greenlight);
            }
            else if(emotion<=0.7 && emotion >=-0.7){
                holder.leftEmotion.setImageResource(R.drawable.yellowlight);
            }
            else{
                holder.leftEmotion.setImageResource(R.drawable.redlight);
            }

        } else {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            if (msg.getReadType() == Msg.TYPE_NOT_READ) {
                holder.right_read_or_not.setText("未读");
            } else {
                holder.right_read_or_not.setText("已读");
            }
            holder.rightMsgTime.setText(msg.getMsgtime());
            if(emotion>0.7){
                holder.rightEmotion.setImageResource(R.drawable.greenlight);
            }
            else if(emotion<=0.7 && emotion >=-0.7){
                holder.rightEmotion.setImageResource(R.drawable.yellowlight);
            }
            else{
                holder.rightEmotion.setImageResource(R.drawable.redlight);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        holder.leftMsgImg.setImageBitmap(friendIcon);
        holder.rightMsgImg.setImageBitmap(myIcon);
        if (payloads.size() < 1) {
            //payloads参数为0，直接执行之前的不带payloads的onBindViewHolder()
            onBindViewHolder(holder,position);
            Log.e("nopayloads","new holder");
            Log.e("nopayloads","-----------------------------------------");
        } else {
            for (Object payload:payloads) {
                switch (String.valueOf(payload)){
                    case "send":
                        //holder.left_read_or_not.setText("已读");
                        if(holder.rightLayout.getVisibility()==View.GONE){
                            holder.left_read_or_not.setText("已读");
                            Log.e("payloads",holder.getPosition()+1+"left已读");
                            Log.e("payloads","-----------------------------------------");
                        }
                        break;
                    case "recieve":
                        if(holder.leftLayout.getVisibility()==View.GONE){
                            holder.right_read_or_not.setText("已读");
                            Log.e("payloads",holder.getPosition()+1+"right已读");
                            Log.e("payloads","-----------------------------------------");

                        }
                        break;
                    default:
                        break;
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }


}
