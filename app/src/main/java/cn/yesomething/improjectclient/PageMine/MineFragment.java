package cn.yesomething.improjectclient.PageMine;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.MyServerManager;
import cn.yesomething.improjectclient.manager.UrlManager;
import cn.yesomething.improjectclient.utils.MyConnectionToServer;


public class MineFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MineFragment";
    private Context mContext;
    private TextView tv_user_name;
    private ImageView iv_head_icon;
    private ImageView iv_user_pic_show;
    private Activity mineActivity;
    //private RelativeLayout rl_signature;
    private TextView tv_sex;
    private RelativeLayout rl_sex;
    private TextView tv_nickName;
    private RelativeLayout rl_nickName;
    private TextView tv_info_back;
    private TextView tv_save;
    private ImageView iv_account_action;
    private String spUserName;
    private String userIconURL;

    @BindView(R.id.contact_mine)
    RadioButton _btnContactPagemine;
    @BindView(R.id.conversation_mine)
    RadioButton _btnConversationPagemine;
    @BindView(R.id.mine_mine)
    RadioButton _btnMinePagemine;
    @BindView(R.id.btn_account_action)
    ImageView _btnAccoutnAction;
    @BindView(R.id.tv_backward)
    TextView _btnBack;
    @BindView(R.id.tv_wordcloud)
    TextView _btnWordcloud;
    @BindView(R.id.login_name)
    TextView tv_login_name;

    @BindView(R.id.tv_show_id)
    TextView tv_Show_id;
    @BindView(R.id.tv_info_nickName)
    TextView tv_Info_nickName;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {//当fragment附着在activity上时保存上下文
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        mineActivity = getActivity();
        //获取当前用户名
        spUserName= IMManager.getLoginUser();
        ButterKnife.bind(this,view);
        //初始化控件
        init(view);
        //绑定监听
        setListener();
        //初始化用户信息
        initUserInfo();
        return view;
    }

    /**
     * 跳转到词云界面
     */
    private void WordCloud() {
        Intent intent = new Intent(mineActivity, WordCloudActivity.class);
        intent.putExtra("username",tv_login_name.getText().toString());
        intent.putExtra("usernickname",tv_Show_id.getText().toString());
        intent.putExtra("url",userIconURL);
        startActivity(intent);
        mineActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 跳转到账号切换/退出界面
     */
    private void AccountAction() {
        Intent intent = new Intent(mineActivity, AccountActionActivity.class);
        intent.putExtra("username",tv_login_name.getText().toString());
        intent.putExtra("usernickname",tv_Show_id.getText().toString());
        intent.putExtra("url",userIconURL);
        Log.e(TAG, "SelectContactPage:username: "+tv_login_name.getText().toString());
        startActivity(intent);
        mineActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 切换到联系人界面
     */
    private void SelectContactPage() {
        Intent intent = new Intent(mineActivity, MainActivity.class);
        startActivity(intent);
        mineActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    /**
     * 切换到会话界面
     */
    private void SelectConversationPage() {
        Intent intent = new Intent(mineActivity, MainActivity.class);
        startActivity(intent);
        mineActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        mineActivity.finish();
    }

    //初始化个人信息
    private void initUserInfo(){
        //初始化用户信息
        testUserSelect(spUserName);
    }

    /**
     * 初始化控件
     */
    private void init(View view) {
        tv_info_back = (TextView) view.findViewById(R.id.tv_backward);
        tv_info_back.setVisibility(View.GONE);
        tv_save = (TextView) view.findViewById(R.id.tv_info_forward);
        tv_save.setVisibility(View.GONE);
        rl_nickName = (RelativeLayout) view.findViewById(R.id.rl_info_nickName);
        tv_nickName = (TextView) view.findViewById(R.id.tv_info_nickName);
        rl_sex = (RelativeLayout) view.findViewById(R.id.rl_info_sex);
        tv_sex = (TextView) view.findViewById(R.id.tv_info_sex);
        tv_user_name = (TextView) view.findViewById(R.id.tv_info_user_name);
        iv_user_pic_show = (ImageView)view.findViewById(R.id.iv_user_pic_show);
        iv_head_icon = (ImageView)view.findViewById(R.id.iv_info_head_icon);
        _btnContactPagemine.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.contact_normal,0,0);
        _btnConversationPagemine.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.conversation_normal,0,0);
        _btnMinePagemine.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.myself_selected,0,0);
    }

    /**
     * 设置界面的点击监听事件
     */
    private void setListener() {
        //绑定按钮监听
        _btnContactPagemine.setOnClickListener(v -> SelectContactPage());
        _btnConversationPagemine.setOnClickListener(v -> SelectConversationPage());
        _btnAccoutnAction.setOnClickListener(v -> AccountAction());
        _btnWordcloud.setOnClickListener(v -> WordCloud());
        _btnBack.setOnClickListener(v -> mineActivity.finish());
        //绑定标签监听
        tv_info_back.setOnClickListener(this);
        rl_nickName.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        iv_head_icon.setOnClickListener(this);
        tv_Info_nickName.setOnClickListener(v->setNickName());

    }

    //控件的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //性别的点击事件
            case R.id.rl_info_sex:
                String sex = tv_sex.getText().toString();
                sexDialog(sex);
                break;
            case R.id.iv_info_head_icon:
                selectPicture(v);
            default:
                break;
        }
    }

    /**
     * 修改昵称弹窗
     */
    private  void setNickName(){
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 获取布局
        View view2 = View.inflate(mContext, R.layout.mine_setnickname_dialog, null);
        final EditText username = (EditText) view2.findViewById(R.id.edit_nickname);
        final Button button = (Button) view2.findViewById(R.id.btn_save_nickname);
        // 设置参数
        builder.setTitle("修改昵称")
                .setView(view2);
        username.setHint("原昵称："+tv_Info_nickName.getText().toString());
        // 创建对话框
        final AlertDialog alertDialog = builder.create();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String nickname = username.getText().toString().trim();
                Log.e(TAG, "onClick: "+nickname );
                testUserUpdate(nickname,null,null);
                tv_Info_nickName.setText(nickname);
                alertDialog.dismiss();// 对话框消失
            }

        });
        alertDialog.show();
    }

    //修改性别的弹出框
    private void sexDialog(String sex) {
        int sexFlag = 0;
        if("男".equals(sex)){
            sexFlag = 0;
        }else if("女".equals(sex)){
            sexFlag = 1;
        }
        final String items[] = {"男","女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("性别"); //设置标题

        builder.setSingleChoiceItems(items, sexFlag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //todo 此处上传items[which]，其中items[] = {"男","女"}
                testUserUpdate(null,which,null);

                //------
                setSex(items[which]);
            }
        });
        builder.show();
    }

    //用户信息更新
    public void testUserUpdate(String userNickname,Integer userSex,ArrayList<String> userTags){
        //todo 一开始时记得声明handler
        Handler userUpdateHandler = null;
        //用户名
        String userName = IMManager.getLoginUser();
        //todo 以下信息需要根据实际获取
        //用于获取最终的数据并展示
        userUpdateHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
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
                            initUserInfo();
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
        MyServerManager.userUpdate(userUpdateHandler,userName,userNickname,userSex,userTags);
    }

    //更新界面上的性别数据
    private void setSex(String sex) {
        tv_sex.setText(sex);
    }


    //资料修改以后回传数据到界面
    @Override
    public void  onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                //将图片转化为bitmap
                Bitmap bm = BitmapFactory.decodeFile(pictureBean.getPath());
                //bitmap转化为Base64 String
                String bitmap2string = bitmapToString(bm);
                //上传头像
                testUserPictureUpdate(pictureBean,bitmap2string);
            }
        }
    }

    /**
     * 用户头像上传
     */
    public void testUserPictureUpdate(PictureBean pictureBean,String base64String){
        //一开始时记得声明handler
        Handler userPictureUpdateHandler = null;
        //用户名
        String userName = IMManager.getLoginUser();
        //用于获取最终的数据并展示
        userPictureUpdateHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
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
                            //todo 利用数据展示
                            //用户头像为网络地址url
                            String userPicture = jsonObject.getString("userPicture");
                            userIconURL = userPicture;
                            Log.e(TAG, "handleMessage: " + userPicture );
                            //展示
                            Picasso.with(mContext)
                                    .load(userIconURL)
                                    .resize(70,70)
                                    .into(iv_user_pic_show);
                            Picasso.with(mContext)
                                    .load(userIconURL)
                                    .resize(70,70)
                                    .into(iv_head_icon);

                        }
                        else {
                            //todo 错误信息处理
                            String errorMessage = jsonObject.getString("errorMessage");
                            //恢复上次头像
                            Picasso.with(mContext)
                                    .load(userIconURL)
                                    .resize(70,70)
                                    .into(iv_user_pic_show);
                            Picasso.with(mContext)
                                    .load(userIconURL)
                                    .resize(70,70)
                                    .into(iv_head_icon);

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
        MyServerManager.userPictureUpdate(userPictureUpdateHandler,userName,base64String);
    }

    /**
     * Base64字符串转换成图片
     * @param string
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片转换成base64字符串
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    private void selectPicture(View view) {
        PictureSelector
                .create(MineFragment.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(false);
    }

    public void testUserSelect(String userName){
        //一开始时记得声明handler
        Handler userSelectHandler = null;
        //用于获取最终的数据并展示
        userSelectHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String response = (msg != null) ? (String) msg.obj : null;
                Log.e(TAG, "handleMessage: "+response );
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
                            Integer userSex = jsonObject.getInt("userSex");
                            String userNickname = jsonObject.getString("userNickname");
                            tv_nickName.setText(userNickname);
                            tv_user_name.setText(userName);
                            tv_login_name.setText(userName);
                            tv_Show_id.setText("昵称："+userNickname);
                            if(userSex==0){tv_sex.setText("男"); }
                            else{tv_sex.setText("女");}
                            //用户头像为网络地址url
                            String userPicture = jsonObject.getString("userPicture");
                            userIconURL = userPicture;//保存url，以便后续传给子活动
                            if("default.jpg".equals(userIconURL)){//默认头像
                                iv_head_icon.setImageDrawable(getResources().getDrawable((R.drawable.user_pic)));
                                iv_user_pic_show.setImageDrawable(getResources().getDrawable((R.drawable.user_pic)));
                            }
                            else{//加载用户头像
                                Log.e(TAG, "handleMessage: " + userIconURL);
                                Picasso.with(mContext)
                                        .load(userIconURL)
                                        .resize(70,70)
                                        .into(iv_user_pic_show);
                                Picasso.with(mContext)
                                        .load(userIconURL)
                                        .resize(70,70)
                                        .into(iv_head_icon);
                            }
                            //获取用户历史头像
                            ArrayList<String> userHistoricalPictures = new ArrayList<>();
                            JSONArray picturesArray = jsonObject.getJSONArray("userHistoricalPictures");
                            for (int i = 0; i < picturesArray.length(); i++) {
                                userHistoricalPictures.add(picturesArray.getString(i));
                            }
                            //获取用户标签信息
                            ArrayList<String> userTags = new ArrayList<>();
                            JSONArray tagsArray = jsonObject.getJSONArray("userTags");
                            for (int i = 0; i < tagsArray.length(); i++) {
                                userTags.add(tagsArray.getString(i));
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

}