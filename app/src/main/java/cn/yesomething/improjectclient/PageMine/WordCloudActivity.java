package cn.yesomething.improjectclient.PageMine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.R;

public class WordCloudActivity extends AppCompatActivity {

    @BindView(R.id.tv_backward)
    TextView _btnBack;
    @BindView(R.id.iv_wordcloud)
    ImageView _ivWordCloud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//防止出现两个toolbar
        setContentView(R.layout.mine_word_cloud_page);


        ButterKnife.bind(this);
        _btnBack.setOnClickListener(v -> this.finish());
        init();

    }

    private void init() {//加载词云以及标签
        //加载标签
        //有点复杂 还没做
        //---

        //加载词云
        Toast.makeText(this,"加载词云图片完毕",Toast.LENGTH_SHORT).show();
        _ivWordCloud.setBackground(this.getDrawable(R.drawable.wordcloud2));//在这个地方加载图片
    }
}