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
import com.hzjd.software.gaokao.model.entity.CheckPhonemodel;
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
public class Register1Activity extends BaseActivity {
    @BindView(R.id.tv_login_phone)
    EditText tvLoginPhone;
    private CheckPhonemodel model;
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

        setContentView(R.layout.activity_register_1);
        ButterKnife.bind(this);
        setBar();
    }

    @OnClick(R.id.btn_register_1)
    public void onViewClicked() {
        if (!TextUtils.isEmpty(tvLoginPhone.getText().toString())&&(tvLoginPhone.getText().toString()).length()==11){
            doRequest();
        }else {
            Toast.makeText(getApplicationContext(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
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
        OkGo.post(Constants.CHECK_PHONE)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("phone", tvLoginPhone.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), CheckPhonemodel.class);
                        if (model.status.equals("0")){
                            Intent intent = new Intent(Register1Activity.this,Register2Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("phonenum",tvLoginPhone.getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
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
}
