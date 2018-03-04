package com.hzjd.software.gaokao.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hzjd.software.gaokao.BaseActivity;
import com.hzjd.software.gaokao.BaseApplication;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.model.entity.CodeModel;
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
public class Register2Activity extends BaseActivity {

    @BindView(R.id.tv_phonecode)
    EditText tvPhonecode;
    @BindView(R.id.btn_register_send)
    Button btnRegisterSend;
    private String phonenum;
    private CodeModel model;
    private String codetype = "1";
    private String code;
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

        setContentView(R.layout.activity_register_2);
        ButterKnife.bind(this);
        setBar();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        phonenum = bundle.getString("phonenum");
        System.out.println("AAAAAAAA-----=====" + phonenum);
    }

    @OnClick({R.id.btn_register_send, R.id.btn_register_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register_send:
                doRequest();
                break;
            case R.id.btn_register_2:

                if (!TextUtils.isEmpty(tvPhonecode.getText().toString())&&code.equals(tvPhonecode.getText().toString())) {
                    Intent intent = new Intent(this, Register3Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", phonenum);
                    bundle.putString("phonecode", tvPhonecode.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "请输入正确的证码", Toast.LENGTH_SHORT).show();
                }
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
        OkGo.post(Constants.GER_CODE)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("phone", phonenum)
                .params("codeType", "1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), CodeModel.class);
                        if (model.status.equals("0")) {
                            final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);
                            myCountDownTimer.start();
                            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            code = model.data;
                            System.out.println("---------"+code);
                        } else if (model.status.equals("1")) {
                            Toast.makeText(getApplicationContext(), model.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //     mTextView2.setText(e.getMessage());
                        Toast.makeText(getApplicationContext(),model.msg, Toast.LENGTH_SHORT).show();
                    }
                });
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
            btnRegisterSend.setClickable(false);
            btnRegisterSend.setText(l / 1000 + "秒后重发");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            btnRegisterSend.setText("重新获取");
            //设置可点击
            btnRegisterSend.setClickable(true);
        }
    }

}
