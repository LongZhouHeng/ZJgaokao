package com.hzjd.software.gaokao.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hzjd.software.gaokao.BaseFragment;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.ui.activity.AboutUsActivity;
import com.hzjd.software.gaokao.ui.activity.LoginActivity;
import com.hzjd.software.gaokao.utils.DataCleanManager;
import com.hzjd.software.gaokao.utils.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Longlong on 2017/11/22.
 */

public class Fragment2 extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.share_extend)
    RelativeLayout shareExtend;
    @BindView(R.id.btn_loginout)
    Button btnLoginout;
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_name_11)
    TextView tvName11;
    private Dialog dialog;
    DataCleanManager dataClean;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater myInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert myInflater != null;
        View layout = myInflater.inflate(R.layout.fragment_2, container, false);
        unbinder = ButterKnife.bind(this, layout);
        btnBack.setVisibility(View.GONE);
        setTitlebar(layout, "我的");
        setBar(layout);
        dataClean = new DataCleanManager();
        tvName11.setText(PrefUtils.getString(getActivity().getApplication(), "phone", null));
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({ R.id.share_extend, R.id.btn_loginout, R.id.rl_share,R.id.about_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_share:

                break;
            case R.id.share_extend:

                break;
            case R.id.btn_loginout:
                showdialog1();
                break;
            case R.id.about_us:
               Intent intent = new Intent(getActivity(), AboutUsActivity.class);
               startActivity(intent);
               break;
        }
    }

    private void showdialog1() {

        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_logout, null);
        TextView tv_ensure = (TextView) localView.findViewById(R.id.tv_ensure);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(getActivity(), R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.getWindow().setGravity(Gravity.CENTER);
        // 设置全屏
        WindowManager windowManager = getActivity().getWindowManager();
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
                dataClean.clearAllCache(getActivity());
                try {
                    String num = dataClean.getTotalCacheSize(getActivity());

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                SharedPreferences userSettings = getActivity().getSharedPreferences("JDLot", 0);
                SharedPreferences.Editor editor = userSettings.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                finishActivity();
                dialog.dismiss();
            }
        });
    }


}
