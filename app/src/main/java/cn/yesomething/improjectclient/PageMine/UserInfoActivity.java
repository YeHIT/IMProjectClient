package cn.yesomething.improjectclient.PageMine;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import cn.yesomething.improjectclient.R;


public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_user_name;
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
    private static  final int  CHANGE_NICKNAME = 1;//修改昵称的自定义常量
    private static  final int  CHANGE_SIGNATURE = 2;//修改签名的自定义常量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_fragemnt);
        //设置界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //spUserName = AnalysisUtils.readLoginUserName(this);
        spUserName="denwade";

        init();

        //从数据库中获取数据
        initData();

        setListener();
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



    }

    //todo 从数据库中获取数据
    private  void initData(){
        UserBean bean = null;
        //bean  = DBUtils.getInstance(this).getUserInfo(spUserName);
        //首先判断一下数据库中是否有数据
        if(bean ==null){
            bean = new UserBean();
            bean.userName = spUserName; //用户名不可以修改
            bean.nickName = "这个是你的昵称";
            //默认为男
            bean.sex = "男";
            //bean.signature = "这个是你的签名";
        }
        setValue(bean);
    }

    //为界面控件设置值
    public void setValue(UserBean bean) {
        tv_nickName.setText(bean.nickName);
        tv_sex.setText(bean.sex);
        //tv_signature.setText(bean.signature);
        tv_user_name.setText(bean.userName);
    }

    //设置界面的点击监听事件
    private void setListener() {
        tv_info_back.setOnClickListener(this);
        rl_nickName.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
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

            //昵称的点击事件
            case R.id.rl_info_nickName:
                String name = tv_nickName.getText().toString();//获取昵称控件上的数据
                Bundle bdName = new Bundle();
                bdName.putString("content",name);  //传递界面上的昵称数据
                bdName.putString("title","昵称");  //传递界面的标题
                bdName.putInt("flag",1);  //flag 传递1表示是昵称
                //跳转到个人资料修改界面
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_NICKNAME,bdName);

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

    private void enterActivityForResult(Class<?> to,int requestCode,Bundle b){
        Intent i = new Intent(this,to);  //to标识需要跳转到的界面
        i.putExtras(b);  //b表示跳转时传递的参数
        startActivityForResult(i,requestCode);  //requestCode表示一个请求码
    }


    //资料修改以后回传数据到界面
    private String new_info;  //最新数据
    @Override
    protected  void  onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case CHANGE_NICKNAME:
                if(data!=null){
                    new_info = data.getStringExtra("nickName");//从个人资料界面回传过来的数据
                    if(TextUtils.isEmpty(new_info)||new_info==null){
                        return;
                    }
                    tv_nickName.setText(new_info);
                    //更新数据库中的呢称字段
                    //DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("nickName", new_info,spUserName);
                }

                break;
            /*case CHANGE_SIGNATURE:

                if(data!=null){
                    new_info = data.getStringExtra("signature");//从个人资料界面回传过来的数据
                    if(TextUtils.isEmpty(new_info)||new_info==null){
                        return;
                    }
                    tv_signature.setText(new_info);
                    //更新数据库中的签名字段
                    //DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("signature", new_info,spUserName);
                }

                break;*/
            default:
                break;
        }

    }
}