package cn.yesomething.improjectclient.PageContact;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.MyServerManager;
import cn.yesomething.improjectclient.utils.Utils;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ContactAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<String> mContactNames; // 联系人名称字符串数组
    private List<String> mContactList; // 联系人名称List（转换成拼音）
    private List<Contact> resultList; // 最终结果（包含分组的字母）
    private List<String> characterList; // 字母List


    public enum ITEM_TYPE {//ITEM_TYPE_CHARACTER表示字母，ITEM_TYPE_CONTACT表示具体的联系人
        ITEM_TYPE_CHARACTER,
        ITEM_TYPE_CONTACT
    }

    public ContactAdapter(Context context, ArrayList<String> contactNames) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mContactNames = contactNames;

        handleContact();
    }

    private void handleContact() {//对联系人进行排序和分类的操作
        mContactList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < mContactNames.size(); i++) {
            String pinyin = Utils.getPingYin(mContactNames.get(i));
            map.put(pinyin, mContactNames.get(i));
            mContactList.add(pinyin);
        }
        Collections.sort(mContactList, new ContactComparator());

        resultList = new ArrayList<>();
        characterList = new ArrayList<>();

        for (int i = 0; i < mContactList.size(); i++) {
            String name = mContactList.get(i);
            String character = (name.charAt(0) + "").toUpperCase(Locale.ENGLISH);
            if (!characterList.contains(character)) {
                if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                    characterList.add(character);
                    resultList.add(new Contact(character, ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()));
                } else {
                    if (!characterList.contains("#")) {
                        characterList.add("#");
                        resultList.add(new Contact("#", ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()));
                    }
                }
            }

            resultList.add(new Contact(map.get(name), ITEM_TYPE.ITEM_TYPE_CONTACT.ordinal()));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()) {
            return new CharacterHolder(mLayoutInflater.inflate(R.layout.item_character, parent, false));
        } else {
            return new ContactHolder(mLayoutInflater.inflate(R.layout.item_contact, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).mTextView.setText(resultList.get(position).getmName());
            Log.e(TAG, "onBindViewHolder: CharacterHolder"+resultList.get(position).getmName());
        } else if (holder instanceof ContactHolder) {
            String name = resultList.get(position).getmName();
            ((ContactHolder) holder).mTextView.setText(name);
            testUserSelect(holder,name);//加载头像
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {//实现clicklistener接口回调
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
//                    onItemClickListener.onClick(position);
                    onItemClickListener.onClick(resultList.get(position).getmName());
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return resultList.get(position).getmType();
    }

    @Override
    public int getItemCount() {
        return resultList == null ? 0 : resultList.size();
    }

    public class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        CharacterHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.character);
        }
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        ContactHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.contact_name);
            mImageView= (ImageView)view.findViewById(R.id.contact_pic);
        }
    }

    public int getScrollPosition(String character) {
        if (characterList.contains(character)) {
            for (int i = 0; i < resultList.size(); i++) {
                if (resultList.get(i).getmName().equals(character)) {
                    return i;
                }
            }
        }
        return -1; // -1不会滑动
    }

    //用户资料查询
    public void testUserSelect(RecyclerView.ViewHolder holder,String userName){
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
                                ((ContactHolder) holder).mImageView.setImageDrawable(mContext.getResources().getDrawable((R.drawable.user_pic)));
                                Log.e(TAG, "handleMessage: 加载头像： 好友id："+userName+ " 的头像未设置，加载默认头像");
                            }
                            else{
                                Picasso.with(mContext)
                                        .load(userPicture)
                                        .resize(70,70)
                                        .into(((ContactHolder) holder).mImageView);
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
        void onClick(String friendname);

    }
    private OnItemClickListener onItemClickListener;//声明一下接口
    //提供setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}