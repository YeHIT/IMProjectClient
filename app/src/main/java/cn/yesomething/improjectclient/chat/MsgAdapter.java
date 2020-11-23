package cn.yesomething.improjectclient.chat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
    private List<Msg> mMsgList;
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

            leftMsgImg = (ImageView) view.findViewById(R.id.left_img);
            rightMsgImg = (ImageView) view.findViewById(R.id.right_img);


        }

    }

    public MsgAdapter(List<Msg> msgList){
        mMsgList=msgList;

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
        //文本
        if (msg.getInfoType() == Msg.TYPE_TEXT) {
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
            }
        }
        //图片
        else {
            if (msg.getType() == Msg.TYPE_RECEIVED) {
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftMsg.setVisibility(View.GONE);
                holder.leftMsgImg.setVisibility(View.VISIBLE);
                holder.leftMsgImg.setImageBitmap(msg.getMsgbitmap());
                if (msg.getReadType() == Msg.TYPE_NOT_READ) {
                    holder.left_read_or_not.setText("未读");
                } else {
                    holder.left_read_or_not.setText("已读");
                }
                holder.leftMsgTime.setText(msg.getMsgtime());
            } else {
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightMsg.setVisibility(View.GONE);
                holder.rightMsgImg.setVisibility(View.VISIBLE);
                holder.rightMsgImg.setImageBitmap(msg.getMsgbitmap());
                if (msg.getReadType() == Msg.TYPE_NOT_READ) {
                    holder.right_read_or_not.setText("未读");
                } else {
                    holder.right_read_or_not.setText("已读");
                }
                holder.rightMsgTime.setText(msg.getMsgtime());
            }

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
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
