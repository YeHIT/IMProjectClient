package cn.yesomething.improjectclient.PageMine;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.squareup.picasso.Picasso;
import com.wildma.pictureselector.FileUtils;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.R;


public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_user_name;
    private ImageView iv_head_icon;
    private ImageView iv_user_pic_show;
    //private TextView tv_signature;
    //private RelativeLayout rl_signature;
    private TextView tv_sex;
    private RelativeLayout rl_sex;
    private TextView tv_nickName;
    private RelativeLayout rl_nickName;
    private TextView tv_info_back;
    private TextView tv_save;
    //private TextView tv_main_title;
    //private RelativeLayout rl_title_bar;
    private String spUserName;

    private Uri imageUri;

    public static final int TAKE_PHOTO = 1;
    private static  final int  CHOOSE_PHOTO = 2;
    @BindView(R.id.contact_mine)
    RadioButton _btnContactPagemine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//防止出现两个toolbar
        setContentView(R.layout.mine_fragment);
        //设置界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //spUserName = AnalysisUtils.readLoginUserName(this);
        spUserName="denwade";
        ButterKnife.bind(this);
        _btnContactPagemine.setOnClickListener(v -> SelectContactPage());
        init();
        initUserInfo();
        setListener();
    }

    private void SelectContactPage() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //初始化个人信息
    private void initUserInfo(){
        //初始化头像  举例
        //1: 确定网址
        String url =  "http://msn-img-nos.yiyouliao.com/inforec-20201030-d03c72163b181e10000e912d0482a604.jpg?time=1604026310&signature=E80BBC3A927EC074D025A1A0BF0E3F00";;
        Picasso.with(this)
                .load(url)
                .resize(70,70)
                .into(iv_user_pic_show);
        Picasso.with(this)
                .load(url)
                .resize(70,70)
                .into(iv_head_icon);
        Log.e("Pis","--------------------><----------------------");
        //初始化用户名


        //初始化昵称

        //初始化性别

        //初始化签名
    }

    //初始化控件
    private void init() {
        tv_info_back = (TextView) findViewById(R.id.tv_info_backward);
        tv_info_back.setVisibility(View.GONE);
        tv_save = (TextView) findViewById(R.id.tv_info_forward);
        tv_save.setVisibility(View.GONE);
        rl_nickName = (RelativeLayout) findViewById(R.id.rl_info_nickName);
        tv_nickName = (TextView) findViewById(R.id.tv_info_nickName);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_info_sex);
        tv_sex = (TextView) findViewById(R.id.tv_info_sex);
        //rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
        //tv_signature = (TextView) findViewById(R.id.tv_signature);
        tv_user_name = (TextView) findViewById(R.id.tv_info_user_name);
        iv_user_pic_show = (ImageView)findViewById(R.id.iv_user_pic_show);
        iv_head_icon = (ImageView)findViewById(R.id.iv_info_head_icon);

    }




    //设置界面的点击监听事件
    private void setListener() {
        tv_info_back.setOnClickListener(this);
        rl_nickName.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        iv_head_icon.setOnClickListener(this);
        //rl_signature.setOnClickListener(this);
    }

    //控件的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回键的点击事件
            case R.id.tv_info_backward:
                this.finish();
                break;
            //性别的点击事件
            case R.id.rl_info_sex:
                String sex = tv_sex.getText().toString();
                sexDialog(sex);
                break;
            /*//签名的点击事件
            case R.id.rl_signature:
                String signature = tv_signature.getText().toString();//获取签名控件上的数据
                Bundle bdSignature = new Bundle();
                bdSignature.putString("content",signature);//传递界面上的签名数据
                bdSignature.putString("title","签名"); //传递界面的标题
                bdSignature.putInt("flag",2);//flag 传递2表示是签名
                //跳转到个人资料修改界面
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_SIGNATURE,bdSignature);
                break;*/
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("性别"); //设置标题
        builder.setSingleChoiceItems(items, sexFlag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(UserInfoActivity.this,items[which],Toast.LENGTH_SHORT).show();;
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
    private String new_info;  //最新数据
    @Override
    protected  void  onActivityResult(int requestCode,int resultCode,Intent data){
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

                //实际开发中将图片上传到服务器成功后需要删除全部缓存图片（即裁剪后的无用图片）
                FileUtils.deleteAllCacheImage(this);
            }
        }
    }

    /**
     * Base64字符串转换成图片
     *
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
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public void selectPicture(View view) {
        PictureSelector
                .create(UserInfoActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(false);
    }
}