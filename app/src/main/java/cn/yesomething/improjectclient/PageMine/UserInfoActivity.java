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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.MainActivity;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yesomething.improjectclient.MainActivity;
import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.login.SignUpActivity;
import cn.yesomething.improjectclient.login.SignUpActivity;

import static android.util.Base64.URL_SAFE;


public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserInfoActivity";
    private TextView tv_user_name;
    private ImageView iv_head_icon;
    private ImageView iv_head_icon_show;
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



        setListener();
    }

    private void SelectContactPage() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    //初始化控件
    private void init() {
        tv_info_back = (TextView) findViewById(R.id.tv_info_backward);
        tv_info_back.setVisibility(View.GONE);
        tv_save = (TextView) findViewById(R.id.tv_info_forward);
        tv_save.setVisibility(View.GONE);
        //tv_main_title  可以通过settext修改文字
        //tv_main_title = (TextView) findViewById(R.id.tv_title);
        //tv_main_title.setText("个人");

        //rl_title_bar = (RelativeLayout) findViewById(R.id.);
        //rl_title_bar.setBackgroundColor(Color.parseColor("##FF9900"));
        rl_nickName = (RelativeLayout) findViewById(R.id.rl_info_nickName);
        tv_nickName = (TextView) findViewById(R.id.tv_info_nickName);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_info_sex);
        tv_sex = (TextView) findViewById(R.id.tv_info_sex);
        //rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
        //tv_signature = (TextView) findViewById(R.id.tv_signature);
        tv_user_name = (TextView) findViewById(R.id.tv_info_user_name);

        iv_head_icon = (ImageView)findViewById(R.id.iv_info_head_icon);
        iv_head_icon_show = (ImageView)findViewById(R.id.iv_user_pic_show);



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
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT < 19) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                } else {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                }
                this.startActivityForResult(intent,1);
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
        switch (requestCode) {
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    try {
                        imageUri= data.getData();
                        //将拍摄的照片显示出来
                        Toast.makeText(this,imageUri.toString(),Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().
                                openInputStream(imageUri));
                        iv_head_icon.setImageBitmap(bitmap);
                        //test bitmap 转 string后，再转回bitmap显示
                        Log.e(TAG, "bitmap to Base64string Result: \n"+BitmapToString(bitmap));
                        Bitmap bitmapFromStr = Base64ToBitmap(BitmapToString(bitmap));
                        iv_head_icon_show.setImageBitmap(bitmapFromStr);
                        //test

                    } catch(FileNotFoundException e ) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机的系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4系统的及以上用此方法处理照片
                        handleImageOnKitkat(data);
                    } else {
                        // 4.4一下的使用这个方法处理照片
                        handleImageBeforeKitkat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id  = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+ "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection );
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse
                        ("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri,直接获取图片路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection) {
        String path = null;
        //通过uri 和 selection 获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images
                        .Media.DATA));

            }
            cursor.close();
        }
        return path;
    }

    /**
     * 根据图片的路径显示图片
     */
    private  void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            iv_head_icon.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * bitmap转base64 str
     */
    public static String BitmapToString(Bitmap bitmap){
        String string = null;
        ByteArrayOutputStream btString = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG ,100,btString);
        byte[] bytes = btString.toByteArray();
        string = Base64.encodeToString(bytes, URL_SAFE);
        return string;
    }
    public static Bitmap Base64ToBitmap(String base64String){
        byte[] decode = Base64.decode(base64String.toString().trim(), URL_SAFE);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode,0,decode.length);
        return bitmap;
    }
}