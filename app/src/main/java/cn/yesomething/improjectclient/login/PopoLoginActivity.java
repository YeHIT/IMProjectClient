package cn.yesomething.improjectclient.login;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yesomething.improjectclient.R;
import cn.yesomething.improjectclient.manager.UrlManager;
import cn.yesomething.improjectclient.utils.JellyInterpolator;
import cn.yesomething.improjectclient.utils.MyConnectionToServer;


public class PopoLoginActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "PopoLoginActivity";
    Handler loginHandler;
    private TextView mBtnLogin;

    private EditText userNameEdit;

    private EditText passwordEdit;

    private TextView mTvSignup;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popo_login);
        initView();
    }

    private void initView() {
        //登录按钮
        mBtnLogin = findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = findViewById(R.id.input_layout_name);
        mPsw = findViewById(R.id.input_layout_psw);
        //注册文本
        mTvSignup = findViewById(R.id.sign_up);
        //用户密码栏
        userNameEdit = (EditText) findViewById(R.id.input_name);
        passwordEdit = (EditText) findViewById(R.id.input_psw);
        //注册监听器
        mBtnLogin.setOnClickListener(this);
        mTvSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.main_btn_login:{
                //按钮1相应操作
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                String userName = userNameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                login(userName,password);
                inputAnimator(mInputLayout, mWidth, mHeight);
                break;
            }


            case R.id.sign_up:{
                Toast.makeText(PopoLoginActivity.this,
                        "Sign up!",Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }


    }

    /**
     * 根据用户名密码判断能否登录
     * @param userName 用户名
     * @param userPassword 用户密码
     */
    private void login(String userName,String userPassword){
        //拼接json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName",userName);
            jsonObject.put("userPassword",userPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "onClick: " + jsonObject.toString() );

        loginHandler = new Handler(Looper.myLooper(),new Handler.Callback(){
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if( Integer.parseInt(msg.obj.toString())== 0){
                    Toast.makeText(PopoLoginActivity.this,
                            "Fail",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(PopoLoginActivity.this,
                            "Success",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        MyConnectionToServer.getConnectionThread(loginHandler,
                UrlManager.myServer+UrlManager.userLoginUrl,
                jsonObject.toString()).start();
    }




    /**
     * 输入框的动画效果
     * @param view 控件
     * @param w 宽
     * @param h 高
     */
    private void inputAnimator(final View view, float w, float h) {

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        recovery();
                    }
                }, 2000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    /**
     * 出现进度动画
     * @param view 控件
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view, animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }


    /**
     * 恢复初始状态
     */
    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }
}