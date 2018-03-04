package com.hzjd.software.gaokao.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hzjd.software.gaokao.BaseActivity;
import com.hzjd.software.gaokao.BaseApplication;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.MainActivity;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.model.entity.CheckPhonemodel;
import com.hzjd.software.gaokao.model.entity.UpdateGradeModel;
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
 * Created by Longlong on 2017/12/1.
 */

@SuppressLint("Registered")
public class CompilePersonActivity extends BaseActivity {
    //分数
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    //排名
    @BindView(R.id.tv_rank)
    TextView tvRank;
    //段位
    @BindView(R.id.tv_class)
    TextView tvClass;
    private Dialog dialog;
    private String grade, rank,batch;
    private UpdateGradeModel model;
    private String score,ranking;
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

        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);
        setBar();
    }


    @OnClick({R.id.tv_redactgrade, R.id.tv_redact,R.id.rl_satart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_redactgrade:
                getGradeorRank();
                break;
            case R.id.tv_redact:
                getBatch();
                break;
            case R.id.rl_satart:
                if (!TextUtils.isEmpty(tvGrade.getText().toString())&&!TextUtils.isEmpty(tvRank.getText().toString())
                        &&!TextUtils.isEmpty(tvClass.getText().toString())){
                    WaringDialog();
                }else {
                    Toast.makeText(getApplicationContext(),"请填写分数、名次及段位",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getGradeorRank() {

        View localView = LayoutInflater.from(this).inflate(R.layout.dialog_redactrank, null);
        ImageView tv_ensure = localView.findViewById(R.id.tv_ensure);
        ImageView tv_cancel = localView.findViewById(R.id.tv_cancel);
        final EditText et_grade = localView.findViewById(R.id.et_grade);
        final EditText et_rank = localView.findViewById(R.id.et_rank);
        dialog = new Dialog(this, R.style.dialog);
        //初始化
        dialog.setContentView(localView);
        //设置位置
        dialog.getWindow().setGravity(Gravity.CENTER);
        // 设置全屏
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);

        dialog.show();
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_grade.getText().toString()) && !TextUtils.isEmpty(et_rank.getText().toString())) {
                    grade = et_grade.getText().toString();
                    rank = et_rank.getText().toString();
                    tvGrade.setText(grade);
                    tvRank.setText(rank);
                    System.out.println("SSSSSSS------======" + grade);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "分数及名字不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getBatch() {

        View localView = LayoutInflater.from(this).inflate(R.layout.dialog_choice_batch, null);
        TextView tv_batch1 = (TextView) localView.findViewById(R.id.tv_batch1);
        TextView tv_batch2 = (TextView) localView.findViewById(R.id.tv_batch2);
        TextView tv_batch3 = (TextView) localView.findViewById(R.id.tv_batch3);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(this, R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        // 设置全屏
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        tv_batch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClass.setText("第一段");
                batch = "1";
                dialog.dismiss();

            }
        });
        tv_batch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClass.setText("第二段");
                batch = "2";
                dialog.dismiss();

            }
        });
        tv_batch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClass.setText("第三段");
                batch = "3";
                dialog.dismiss();

            }
        });
    }
    private void WaringDialog() {

        View localView = LayoutInflater.from(this).inflate(R.layout.dialog_waring, null);

        TextView tv_ensure = (TextView) localView.findViewById(R.id.tv_ensure);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(this, R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.getWindow().setGravity(Gravity.CENTER);
        // 设置全屏
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    doRequest();
                    dialog.dismiss();


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
        OkGo.post(Constants.MODIFY_GRADE_RANK)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("score", grade)
                .params("ranking", rank)
                .params("uid", PrefUtils.getString(getApplication(),"uid",null))
                .params("token",PrefUtils.getString(getApplication(),"utoken",null))
                .params("batchNum", batch)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), UpdateGradeModel.class);
                        if (model.status.equals("0")){
                            saveUserMsg();
                            Intent intent = new Intent(CompilePersonActivity.this, MainActivity.class);
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
                        Toast.makeText(getApplicationContext(), BaseApplication.getInst().getString(
                                R.string.errcode_network_unavailable),Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // 将用户信息保存到配置文件
    private void saveUserMsg() {
        PrefUtils.putString(getApplication(), "grade", tvGrade.getText().toString());
        PrefUtils.putString(getApplication(), "ranking", tvRank.getText().toString());
 //       PrefUtils.putString(getApplication(), "phone", tvLoginPhone.getText().toString());
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
