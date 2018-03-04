package com.hzjd.software.gaokao.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hzjd.software.gaokao.BaseActivity;
import com.hzjd.software.gaokao.BaseApplication;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.model.entity.CodeModel;
import com.hzjd.software.gaokao.model.entity.FindPasswordModel;
import com.hzjd.software.gaokao.utils.NetworkUtils;
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
public class FindPassWordActivity extends BaseActivity {

    @BindView(R.id.tv_find_phone)
    EditText tvFindPhone;
    @BindView(R.id.tv_phonecode)
    EditText tvPhonecode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_pwdensure)
    EditText etPwdensure;
    @BindView(R.id.btn_findregister_send)
    Button btnFindregisterSend;
    private FindPasswordModel model;
    private CodeModel codeModel;
    private String phone, code, password, password1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        ButterKnife.bind(this);
        setBar();
    }


    @OnClick({R.id.btn_findregister_send, R.id.btn_find})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_findregister_send:
                getCode();
                break;
            case R.id.btn_find:
                if (getValue()) {
                    doRequest();
                }
                break;
        }
    }

    public void getCode() {

        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        OkGo.post(Constants.GER_CODE)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("phone", tvFindPhone.getText().toString())
                .params("codeType", "2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        codeModel = JSON.parseObject(s.getBytes(), CodeModel.class);
                        //     codeModel = JSON.parseObject(s.getBytes(), CodeModel.class);
                        if (codeModel.status.equals("0")) {
                            final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);
                            myCountDownTimer.start();
                            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            code = codeModel.data;
                        } else if (codeModel.status.equals("1")) {
                            Toast.makeText(getApplicationContext(), codeModel.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //     mTextView2.setText(e.getMessage());
                        Toast.makeText(getApplicationContext(), BaseApplication.getInst().getString(
                                R.string.errcode_network_unavailable), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void doRequest() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        OkGo.post(Constants.RESET_PWD)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("phone", tvFindPhone.getText().toString())
                .params("password", etPassword.getText().toString())
                .params("smsCode", tvPhonecode.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), FindPasswordModel.class);
                        if (model.status.equals("0")) {
                            Intent intent = new Intent(FindPassWordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            //         Toast.makeText(getApplicationContext(),model.data,Toast.LENGTH_SHORT).show();
                            //       tvPhonecode.setText(model.msg);
                        } else if (model.status.equals("1")) {
                            Toast.makeText(getApplicationContext(), model.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //     mTextView2.setText(e.getMessage());
                        Toast.makeText(getApplicationContext(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean getValue() {
        phone = tvFindPhone.getText().toString();
        if (phone.equals("")) {
            Toast.makeText(getApplicationContext(), "请填写手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        code = tvPhonecode.getText().toString();

        if (code.equals("")) {
            Toast.makeText(getApplicationContext(), "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        password = etPassword.getText().toString();
        if (password.equals("")) {
            Toast.makeText(getApplicationContext(), "请设置新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        password1 = etPwdensure.getText().toString();
        if (password1.equals("")) {
            Toast.makeText(getApplicationContext(), "请确认新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            btnFindregisterSend.setClickable(false);
            btnFindregisterSend.setText(l / 1000 + "秒后重发");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            btnFindregisterSend.setText("重新获取");
            //设置可点击
            btnFindregisterSend.setClickable(true);
        }
    }
}
