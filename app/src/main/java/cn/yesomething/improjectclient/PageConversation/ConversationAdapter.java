package cn.yesomething.improjectclient.PageConversation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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
    Bitmap right_bitmap;
    private List<Conversation> mConversationList; // 对话List（根据时间排序）
    private SimpleDateFormat Date_format,Time_format,use_format;
    public ConversationAdapter(Context context, List<Conversation> ConversationList) {

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mConversationList = ConversationList;
        //获取当前用户名
        spUserName= IMManager.getLoginUser();

        Date_format = new SimpleDateFormat("MM-dd");
        Time_format= new SimpleDateFormat("hh:mm");

        handleConversation();//排序
        getFriendIcon(null,spUserName,0);
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
       // Log.e(TAG, "handleConversation: 根据时间顺序排序完成");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//viewType默认为0
        return new ConversationHolder(mLayoutInflater.inflate(R.layout.conversation_item, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String name = mConversationList.get(position).getmName();

        ((ConversationHolder) holder).mNameView.setText(name);
        ((ConversationHolder) holder).mTextView.setText(mConversationList.get(position).getLatestMessage());
//        ((ConversationHolder) holder).mIconView.setBackground(mContext.getDrawable(R.drawable.user_pic));

        use_format = chooseTimeFormat(mConversationList.get(position).getmTime());
        ((ConversationHolder) holder).mTimeView.setText(use_format.format(mConversationList.get(position).getmTime()));
        getFriendIcon(holder,name,position);//加载头像


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            //实现clicklistener接口回调 返回对话联系人名字
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(mConversationList.get(position).getmName(),mConversationList.get(position).getIcon(),right_bitmap);
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
        //Log.e(TAG, "chooseTimeFormat: "+"相隔的天数="+ Integer.toString(day));
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
            //Log.e(TAG, "ConversationHolder: 初始化控件" );
            mNameView = (TextView) view.findViewById(R.id.name);
            mTextView = (TextView) view.findViewById(R.id.message);
            mIconView = (ImageView) view.findViewById(R.id.img_icon);
            mTimeView = (TextView) view.findViewById(R.id.time);

        }
    }

    @Override
    public int getItemCount() {
        return mConversationList == null ? 0 : mConversationList.size();
    }
    //用户资料查询
    public void getFriendIcon(RecyclerView.ViewHolder holder,String userName, int position){
        //一开始时记得声明handler
        Handler userSelectHandler = null;
        Target mTarget = new Target() {
            //图片加载成功
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    Log.e(TAG, "onBitmapLoaded: 获取到bitmap 用户id：" + userName );
                    if(spUserName.equals(userName)){//保存本人头像
                        right_bitmap =  bitmap;
                    }else{//设置conversation对象的头像
                        ((ConversationAdapter.ConversationHolder) holder).mIconView.setImageBitmap(bitmap);
                        mConversationList.get(position).setIcon(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {                                }
        };

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
                                Drawable drawable_default_icon = mContext.getResources().getDrawable((R.drawable.user_pic));

                                if(spUserName.equals(userName)){//本人
                                    Log.e(TAG, "handleMessage: 加载本人头像："+userName+ " 头像未设置，加载默认头像");
                                }else{//conversation对象
                                    ((ConversationAdapter.ConversationHolder) holder).mIconView.setImageDrawable(drawable_default_icon);
                                    mConversationList.get(position).setIcon(null);
                                    Log.e(TAG, "handleMessage: 加载头像： 好友id："+userName+ " 的头像未设置，加载默认头像");

                                }
                            }
                            else{
                                Picasso.with(mContext)
                                        .load(userPicture)
                                        .resize(80,80)
                                        .into(mTarget);
                                Log.e(TAG, "handleMessage: 加载头像： 好友id："+userName+ " 的头像已设置，加载好友头像");
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
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 30) {  //循环判断如果压缩后图片是否大于40kb,大于继续压缩
            Log.e(TAG, "compressImage: 压缩中  "+"size："+Integer.toString(baos.toByteArray().length / 1024));
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
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
