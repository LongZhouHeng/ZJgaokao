package com.hzjd.software.gaokao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hzjd.software.gaokao.ui.activity.CompilePersonActivity;
import com.hzjd.software.gaokao.ui.activity.LoginActivity;
import com.hzjd.software.gaokao.utils.PrefUtils;

/**
 * Created by Longlong on 2017/12/4.
 */

@SuppressLint("Registered")
public class StartActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //延迟2S跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PrefUtils.getString(getApplication(),"uid",null) !=null&&
                    PrefUtils.getString(getApplication(),"utoken",null) !=null){
                    if ( PrefUtils.getString(getApplication(),"state",null).equals("1")){

                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else if ( PrefUtils.getString(getApplication(),"state",null).equals("0")){

                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }else {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }

}
