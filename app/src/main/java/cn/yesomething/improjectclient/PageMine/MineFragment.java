package cn.yesomething.improjectclient.PageMine;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.IMManager;
import cn.yesomething.improjectclient.manager.UrlManager;
import cn.yesomething.improjectclient.utils.MyConnectionToServer;


public class MineFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private TextView tv_user_name;
    private ImageView iv_head_icon;
    private ImageView iv_user_pic_show;
    private Activity mineActivity;
    //private TextView tv_signature;
    //private RelativeLayout rl_signature;
    private TextView tv_sex;
    private RelativeLayout rl_sex;
    private TextView tv_nickName;
    private RelativeLayout rl_nickName;
    private TextView tv_info_back;
    private TextView tv_save;
    private ImageView iv_account_action;
    private String spUserName;

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
        startActivity(intent);
        mineActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 跳转到账号切换/退出界面
     */
    private void AccountAction() {
        Intent intent = new Intent(mineActivity, AccountActionActivity.class);
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
        mineActivity.finish();
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
        //初始化头像  举例
        //1: 确定网址
        String url =  "http://msn-img-nos.yiyouliao.com/inforec-20201030-d03c72163b181e10000e912d0482a604.jpg?time=1604026310&signature=E80BBC3A927EC074D025A1A0BF0E3F00";
        Picasso.with(mContext)
                .load(url)
                .resize(70,70)
                .into(iv_user_pic_show);
        Picasso.with(mContext)
                .load(url)
                .resize(70,70)
                .into(iv_head_icon);
        Log.e("Pis","--------------------><----------------------");
        String str=null;
        if(str!=null){
            iv_head_icon.setImageBitmap(stringToBitmap(str));
        }


        //初始化用户名


        //初始化昵称

        //初始化性别

        //初始化签名
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
                Toast.makeText(mContext,items[which],Toast.LENGTH_SHORT).show();;
                setSex(items[which]);
            }
        });
        builder.show();
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
                /*Log.i(TAG, "是否裁剪: " + pictureBean.isCut());
                Log.i(TAG, "原图地址: " + pictureBean.getPath());*/
                Log.i("SELECTOR", "图片 Uri: " + pictureBean.getUri());
                if (pictureBean.isCut()) {
                    iv_head_icon.setImageBitmap(BitmapFactory.decodeFile(pictureBean.getPath()));
                    iv_user_pic_show.setImageBitmap(BitmapFactory.decodeFile(pictureBean.getPath()));
                    //Bitmap bm = BitmapFactory.decodeFile(pictureBean.getPath());
                    //Log.e("bitmap",String.valueOf(BitmapFactory.decodeFile(pictureBean.getPath())));
                } else {
                    iv_head_icon.setImageURI(pictureBean.getUri());
                    iv_user_pic_show.setImageURI(pictureBean.getUri());
                    //Bitmap bm = BitmapFactory.decodeFile(pictureBean.getPath());
                    //Log.e("bitmap",bitmapToString(bm));
                }
                //将图片转化为bitmap
                Bitmap bm = BitmapFactory.decodeFile(pictureBean.getPath());
                //bitmap转化为Base64 String
                String bitmap2string = bitmapToString(bm);
                //把String传送到服务器
                Log.e("bitmap",bitmap2string);
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("userName","denwade");
//                    jsonObject.put("base64String",bitmap2string);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                MyConnectionToServer.getConnectionThread(null, UrlManager.myServer + UrlManager.userPictureUpdateUrl,
//                        jsonObject.toString()).start();

            }
        }
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
}