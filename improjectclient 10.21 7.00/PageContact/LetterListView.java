package cn.yesomething.improjectclient.PageContact;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.yesomething.improjectclient.R;

//联系人界面侧边字母导航栏
public class LetterListView extends LinearLayout {
    private Context mContext;
    private CharacterClickListener mListener;//用来在字母、箭头或#被点击时回调
    //从上到下，第一个是一个箭头，用来滑动到联系人列表的顶部
    // 下面依次是26个英文字母
    // 最后是一个#字符
    public LetterListView(Context context,AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setOrientation(VERTICAL);//设置方向

        initView();
    }

    private void initView() {
        addView(buildImageLayout());//构造字母导航栏最上端的箭头
        //a-z依次添加到列表
        for (char i = 'A'; i <= 'Z'; i++) {
            final String character = i + "";
            TextView letter = buildTextLayout(character);
            addView(letter);
        }
        addView(buildTextLayout("#"));
    }

    private TextView buildTextLayout(final String character) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);

        TextView letter = new TextView(mContext);
        letter.setLayoutParams(layoutParams);
        letter.setGravity(Gravity.CENTER);
        letter.setClickable(true);
        letter.setTextColor(this.getResources().getColor(R.color.iron));
        letter.setText(character);

        letter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.clickCharacter(character);
                }
            }
        });
        return letter;
    }
    //构造箭头
    private ImageView buildImageLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);

        ImageView iv = new ImageView(mContext);
        iv.setLayoutParams(layoutParams);

        iv.setBackgroundResource(R.mipmap.arrow);

        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.clickArrow();
                }
            }
        });
        return iv;
    }

    public void setCharacterListener(CharacterClickListener listener) {
        mListener = listener;
    }

    public interface CharacterClickListener {
        void clickCharacter(String character);

        void clickArrow();
    }
}