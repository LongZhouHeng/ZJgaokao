package com.hzjd.software.gaokao.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hzjd.software.gaokao.BaseActivity;
import com.hzjd.software.gaokao.BaseApplication;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.MainActivity;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.model.entity.CodeModel;
import com.hzjd.software.gaokao.model.entity.LoginModel;
import com.hzjd.software.gaokao.utils.NetworkUtils;
import com.hzjd.software.gaokao.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Longlong on 2017/11/22.
 */


@SuppressLint("Registered")
public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_login_phone)
    EditText tvLoginPhone;
    @BindView(R.id.tv_login_passaord)
    EditText tvLoginPassaord;
    private String phone ,password,uid ,utoken;
    private LoginModel model;
    private String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //控制底部虚拟键盘
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setBar();


    }

    @OnClick({R.id.btn_login, R.id.tv_login_register, R.id.tv_forget_password, R.id.iv_wechat, R.id.iv_qq, R.id.iv_weibo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //登录
            case R.id.btn_login:
                doRequest();
                break;
            //注册
            case R.id.tv_login_register:
                Intent intent = new Intent(this,Register1Activity.class);
                startActivity(intent);
                break;
            //忘记密码
            case R.id.tv_forget_password:
                Intent intent1 = new Intent(this,FindPassWordActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_wechat:
                Toast.makeText(getApplicationContext(),"暂未开放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_qq:
                Toast.makeText(getApplicationContext(),"暂未开放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_weibo:
                Toast.makeText(getApplicationContext(),"暂未开放",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void doRequest() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        OkGo.post(Constants.LOGIN)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("phone", tvLoginPhone.getText().toString())
                .params("password", tvLoginPassaord.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), LoginModel.class);
                        if (model.status.equals("0")&&model.data.updateStatus.equals("0")){
                            uid = model.data.uid;
                            utoken = model.data.utoken;
                            state = model.data.updateStatus;
                            //保存uid utoken
                            saveUserMsg();
                            Intent intent_login = new Intent(LoginActivity.this, CompilePersonActivity.class);
                            startActivity(intent_login);
                            finish();
                        }else if (model.status.equals("0")&&model.data.updateStatus.equals("1")){
                            uid = model.data.uid;
                            utoken = model.data.utoken;
                            state = model.data.updateStatus;
                            saveUserMsg();
                            Intent intent_login = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent_login);
                            finish();
                        }
                        if (model.status.equals("1")){
                            Toast.makeText(getApplicationContext(),model.msg,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //     mTextView2.setText(e.getMessage());
                        Toast.makeText(getApplicationContext(),model.msg,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 将用户信息保存到配置文件
    private void saveUserMsg() {
        PrefUtils.putString(getApplication(), "uid", uid);
        PrefUtils.putString(getApplication(), "utoken", utoken);
        PrefUtils.putString(getApplication(), "state", state);
        PrefUtils.putString(getApplication(), "phone", tvLoginPhone.getText().toString());
 //       PrefUtils.putString(getApplication(), "uid", uid);
//        PrefUtils.putString(getApplication(),"inviteNum",inviteNum);
//        PrefUtils.putString(getApplication(), "member_truename", name);
//        PrefUtils.putString(getApplication(), "member_token", member_token);
//        PrefUtils.putString(getApplication(), "member_is_pwd", member_is_pwd);
//        PrefUtils.putString(getApplication(), "member_pwd_life", member_pwd_life);
        // PrefUtils.putString(getApplication(), "member_token", member_token);
        //PrefUtils.putString(getApplication(),);
    }
}
