package cn.yesomething.improjectclient.PageContact;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.utils.Utils;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ContactAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String[] mContactNames; // 联系人名称字符串数组
    private List<String> mContactList; // 联系人名称List（转换成拼音）
    private List<Contact> resultList; // 最终结果（包含分组的字母）
    private List<String> characterList; // 字母List


    public enum ITEM_TYPE {//ITEM_TYPE_CHARACTER表示字母，ITEM_TYPE_CONTACT表示具体的联系人
        ITEM_TYPE_CHARACTER,
        ITEM_TYPE_CONTACT
    }

    public ContactAdapter(Context context, String[] contactNames) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mContactNames = contactNames;

        handleContact();
    }

    private void handleContact() {//对联系人进行排序和分类的操作
        mContactList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < mContactNames.length; i++) {
            String pinyin = Utils.getPingYin(mContactNames[i]);
            map.put(pinyin, mContactNames[i]);
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

            resultList.add(new Contact(map.get(name), ITEM_TYPE.ITEM_TYPE_CONTACT.ordinal()));// Contact(String name, int type)
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
        } else if (holder instanceof ContactHolder) {
            ((ContactHolder) holder).mTextView.setText(resultList.get(position).getmName());
            switch(resultList.get(position).getmName()){
                case "黄大晔":
                    ((ContactHolder) holder).mImageView.setBackground(mContext.getDrawable(R.drawable.user_pic));
                    break;
                case "黄建晔":
                    ((ContactHolder) holder).mImageView.setBackground(mContext.getDrawable(R.drawable.user_pic_b2));
                    break;
                case"邓广博":
                    ((ContactHolder) holder).mImageView.setBackground(mContext.getDrawable(R.drawable.user_pic));
                    break;
                case "POPO助手":
                    ((ContactHolder) holder).mImageView.setBackground(mContext.getDrawable(R.drawable.popo));
                    break;
                case "蔡敏敏":
                    ((ContactHolder) holder).mImageView.setBackground(mContext.getDrawable(R.drawable.user_pic_g1));
                    break;
                default:
                    ((ContactHolder) holder).mImageView.setBackground(mContext.getDrawable(R.drawable.user_pic_g2));

            }


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
            Log.e(TAG, "CharacterHolder: " );
            mTextView = view.findViewById(R.id.character);
        }
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        ContactHolder(View view) {
            super(view);
            Log.e(TAG, "ContactHolder: " );
            mTextView = (TextView) view.findViewById(R.id.contact_name);
            mImageView= view.findViewById(R.id.contact_pic);
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