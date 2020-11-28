package cn.yesomething.improjectclient.PageConversation;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.yesomething.improjectclient.PageContact.ContactAdapter;
import cn.yesomething.improjectclient.PageConversation.Conversation;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MyServerManager;

public class ConversationAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final String TAG = "ConversationAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String spUserName;
    Bitmap left_bitmap,right_bitmap;
    private List<Conversation> mConversationList; // 对话List（根据时间排序）
    private SimpleDateFormat Date_format,Time_format,use_format;
    public ConversationAdapter(Context context, List<Conversation> ConversationList) {

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mConversationList = ConversationList;

        Date_format = new SimpleDateFormat("MM-dd");
        Time_format= new SimpleDateFormat("hh:mm");

        handleConversation();//排序
    }

    private void handleConversation() {//根据最后一条消息时间时间排序
        //mConversationList = new ArrayList<>();
        Collections.sort(mConversationList,
                new Comparator<Conversation>() {
                    @Override
                    public int compare(Conversation o1, Conversation o2) {
                        return (int)(o2.getmTime().getTime()-o1.getmTime().getTime());//Date类型比较差值是 微秒级
                    }
                });
        Log.e(TAG, "handleConversation: 根据时间顺序排序完成");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//viewType默认为0
        Log.e(TAG, "onCreateViewHolder:聊天界面添加新项");
        return new ConversationHolder(mLayoutInflater.inflate(R.layout.conversation_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: 设置会话列表数据");
        String name = mConversationList.get(position).getmName();

        ((ConversationHolder) holder).mNameView.setText(name);
        ((ConversationHolder) holder).mTextView.setText(mConversationList.get(position).getLatestMessage());
//        ((ConversationHolder) holder).mIconView.setBackground(mContext.getDrawable(R.drawable.user_pic));

        use_format = chooseTimeFormat(mConversationList.get(position).getmTime());
        ((ConversationHolder) holder).mTimeView.setText(use_format.format(mConversationList.get(position).getmTime()));
        getFriendIcon(holder,name);//加载头像

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            //实现clicklistener接口回调 返回对话联系人名字
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
//                    onItemClickListener.onClick(position);
                    onItemClickListener.onClick(mConversationList.get(position).getmName(),left_bitmap,right_bitmap);
                }
            }
        });
    }
    private  Bitmap getIconDrawable(RecyclerView.ViewHolder holder){
        ((ConversationAdapter.ConversationHolder) holder).mIconView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(((ConversationAdapter.ConversationHolder) holder).mIconView.getDrawingCache());
        ((ConversationAdapter.ConversationHolder) holder).mIconView.setDrawingCacheEnabled(false);
        return bitmap;
    }
    private SimpleDateFormat chooseTimeFormat(Date date) {
//        Date now = new Date(System.currentTimeMillis());
        int day = (int)(System.currentTimeMillis()-date.getTime())/(24 * 60 * 60 * 1000);
        Log.e(TAG, "chooseTimeFormat: "+"相隔的天数="+ Integer.toString(day));
        switch (day){
            case 0:return Time_format;
            //差值在七天内显示星期，还没做
            case 1:return Date_format;/*星期几*/
            //---
            case 7:return Date_format;/*星期几*/

            default:return Date_format;//其余显示日期
        }

    }


    public class ConversationHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        TextView mNameView;
        ImageView mIconView;
        TextView mTimeView;
        ConversationHolder(View view) {
            super(view);
            Log.e(TAG, "ConversationHolder: 初始化控件" );
            mNameView = (TextView) view.findViewById(R.id.name);
            mTextView = (TextView) view.findViewById(R.id.message);
            mIconView = (ImageView) view.findViewById(R.id.img_icon);
            mTimeView = (TextView) view.findViewById(R.id.time);

        }
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: "+"mConversationList.size = "+Integer.toString(mConversationList.size()));
        return mConversationList == null ? 0 : mConversationList.size();
    }
    //用户资料查询
    public void getFriendIcon(RecyclerView.ViewHolder holder,String userName){
        //一开始时记得声明handler
        Handler userSelectHandler = null;
        //todo 输入要查询用户的用户名
//        String userName = "xx";
        //用于获取最终的数据并展示
        userSelectHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                //网络正常
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        //正常获取到数据
                        if(responseCode.equals("200")){
                            jsonObject = jsonObject.getJSONObject("user");
                            //todo 利用数据展示
                            String userName = jsonObject.getString("userName");
                            //用户头像为网络地址url
                            String userPicture = jsonObject.getString("userPicture");
                            //加载url为图片
                            if("default.jpg".equals(userPicture)){//默认头像
                                ((ConversationAdapter.ConversationHolder) holder).mIconView.setImageDrawable(mContext.getResources().getDrawable((R.drawable.user_pic)));
                                Log.e(TAG, "handleMessage: 加载头像： 好友id："+userName+ " 的头像未设置，加载默认头像");

                            }
                            else{
                                Picasso.with(mContext)
                                        .load(userPicture)
                                        .resize(80,80)
                                        .into(((ConversationAdapter.ConversationHolder) holder).mIconView);

                            }
                            //获取当前用户名
                            spUserName= IMManager.getLoginUser();
                            if(spUserName.equals(userName)){
                                right_bitmap =  getIconDrawable(holder);//获取当前用户头像缓存
                            }else{
                                left_bitmap = getIconDrawable(holder);//获取对方头像缓存
                            }
                        }
                        else {
                            //todo 错误信息处理
                            String errorMessage = jsonObject.getString("errorMessage");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //todo 网络异常判断
                else {
                }
                return false;
            }
        });
        MyServerManager.userSelect(userSelectHandler,userName);
    }


    /**
     * 定义RecyclerView选项单击事件的回调接口
     */
    public interface OnItemClickListener{//也可以不在这个activity或者是fragment中来声明接口，可以在项目中单独创建一个interface，就改成static就OK
        //参数（父组件，当前单击的View,单击的View的位置，数据）
        void onClick(String friendname, Bitmap left_bitmap_icon, Bitmap right_bitmap_icon);

    }
    private OnItemClickListener onItemClickListener;//声明一下接口
    //提供setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
