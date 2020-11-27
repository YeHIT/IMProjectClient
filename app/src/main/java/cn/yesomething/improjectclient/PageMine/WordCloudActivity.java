package cn.yesomething.improjectclient.PageMine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MyServerManager;

public class WordCloudActivity extends AppCompatActivity {

    @BindView(R.id.tv_backward)
    TextView _btnBack;
    @BindView(R.id.iv_wordcloud)
    ImageView _ivWordCloud;
    @BindView(R.id.tag0)
    LinearLayout _tagitem0;
    @BindView(R.id.tag1)
    LinearLayout _tagitem1;
    @BindView(R.id.tag2)
    LinearLayout _tagitem2;

    @BindView(R.id.login_name)
    TextView _tvname;

    @BindView(R.id.iv_user_pic_show)
    ImageView _ivpic;

    String wordCloudPictureURL;

    String username,userurl;
    ArrayList<String> userTags;//标签组

    private static final String TAG = "WordCloudActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//防止出现两个toolbar
        setContentView(R.layout.mine_word_cloud_page);


        ButterKnife.bind(this);
        _btnBack.setOnClickListener(v -> this.finish());
        username = getIntent().getStringExtra("username");
        userurl = getIntent().getStringExtra("url");
        //动态加载词云
        initview();
        getwordcloud();


    }

    private void initview() {
        if("default.jpg".equals(userurl)){//默认头像
            _ivpic.setImageDrawable(getResources().getDrawable((R.drawable.user_pic)));
        }
        else{
            Picasso.with(this)
                    .load(userurl)
                    .resize(70,70)
                    .into(_ivpic);
        }
        _tvname.setText(username);
    }

    private void init(String url) {//加载词云以及标签



        //-------------------------
        //todo 使用时将下面这行取消注释
        //String url =wordCloudPictureURL;
        //没有后端，先常值代替
        //todo 使用时删除下面这行
        //String url = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1392116398,3574857050&fm=26&gp=0.jpg";


        Picasso.with(this)
                .load(url)
                .resize(400,400)
                .into(_ivWordCloud);
        Toast.makeText(this,"加载词云图片完毕",Toast.LENGTH_SHORT).show();
        Log.e(TAG, "url: " +wordCloudPictureURL);
        _ivWordCloud.setBackground(this.getDrawable(R.drawable.wordcloud2));//在这个地方加载图片


        //加载标签
        initTagList(userTags);
        //---
    }

    private void getwordcloud(){
        //一开始时记得声明handler
        Handler userWordCloudGenerateHandler = null;
        //用户名
        String userName = IMManager.getLoginUser();
        //用于获取最终的数据并展示
        userWordCloudGenerateHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                //网络正常
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("responseCode");
                        Log.e(TAG, "handleMessage: "+responseCode );

                        //正常获取到数据
                        if(responseCode.equals("200")){
                            //todo 利用数据展示
                            //词云图片为网络地址url
                            wordCloudPictureURL = jsonObject.getString("wordCloudPicture");
                            Log.e(TAG, "handleMessage: "+wordCloudPictureURL );


                            //------------
                            //获取用户标签信息
                            userTags = new ArrayList<>();
                            JSONArray tagsArray = jsonObject.getJSONArray("tags");
                            for (int i = 0; i < tagsArray.length(); i++) {
                                userTags.add(tagsArray.getString(i));
                            }
                            Log.e(TAG, "handleMessage: " + userTags );

                            init(wordCloudPictureURL);//更新界面
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
        MyServerManager.userWordCloudGenerate(userWordCloudGenerateHandler,userName);
    }

    private void initTagList(ArrayList<String> MuserTags){
        String[] viewlist={"v0","v1","v2"};
        //todo size 先设置为常值3,使用时注释下行
        int size = 3;
        // size合法取值0-3，最多设置3个标签
        //todo 动态获取标签列表,使用时取消下行注释
        //size = MuserTags.size()

        for(int i=0;i<size;i++){
            View v = (i==0)?(View)findViewById(R.id.tag0):
                        (i==1)?(View)findViewById(R.id.tag1):
                                (View)findViewById(R.id.tag2);
            //v0.setVisibility(View.INVISIBLE);
            TextView tv_tag0 = (TextView)v.findViewById(R.id.tv_info_forward);

            //todo  设置测试文字标签"测试v[i]",使用时注释下行
            tv_tag0.setText("测试"+viewlist[i]);//测试
            //todo 动态获取标签列表,使用时取消下行注释
            //tv_tag0.setText(MuserTags.indexOf(i));//设置标签
        }

        //todo：
        // 少于3个标签的情况需要隐藏多余item

        // 待补充
    }
}