package com.hzjd.software.gaokao.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hzjd.software.gaokao.BaseActivity;
import com.hzjd.software.gaokao.BaseApplication;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.model.entity.CodeModel;
import com.hzjd.software.gaokao.model.entity.Regidtermodel;
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
 * Created by Longlong on 2017/12/4.
 */

@SuppressLint("Registered")
public class Register3Activity extends BaseActivity {

    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_pwdensure)
    EditText etPwdensure;
    private String phone,code;
    private Regidtermodel model;
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

        setContentView(R.layout.activity_register_3);
        ButterKnife.bind(this);
        setBar();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        phone = bundle.getString("phone");
        code = bundle.getString("phonecode");

    }

    @OnClick(R.id.btn_register_3)
    public void onViewClicked() {
        if (!TextUtils.isEmpty(etPassword.getText().toString())&& !TextUtils.isEmpty(etPwdensure.getText().toString())){
            doRequest();
        }else {
            Toast.makeText(getApplicationContext(),"请填写密码",Toast.LENGTH_SHORT).show();
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
        OkGo.post(Constants.REGISTER)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("phone", phone)
                .params("password", etPassword.getText().toString())
                .params("ssmCode", code)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), Regidtermodel.class);
                        if (model.status.equals("0")){
                            saveUserMsg();
                            Intent intent = new Intent(Register3Activity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),model.msg,Toast.LENGTH_SHORT).show();
                            //       tvPhonecode.setText(model.msg);
                        }else if (model.status.equals("1")){
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
        PrefUtils.putString(getApplication(), "phono", phone);
        PrefUtils.putString(getApplication(), "pwd", etPassword.getText().toString());
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
