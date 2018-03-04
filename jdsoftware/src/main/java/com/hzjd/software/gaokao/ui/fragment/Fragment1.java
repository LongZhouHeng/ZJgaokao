package com.hzjd.software.gaokao.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hzjd.software.gaokao.BaseApplication;
import com.hzjd.software.gaokao.BaseFragment;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.MainActivity;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.model.entity.HomePageModel;
import com.hzjd.software.gaokao.model.entity.UpdateGradeModel;
import com.hzjd.software.gaokao.ui.activity.CompilePersonActivity;
import com.hzjd.software.gaokao.ui.activity.QueryActivity;
import com.hzjd.software.gaokao.utils.AndroidBug54971Workaround;
import com.hzjd.software.gaokao.utils.NetworkUtils;
import com.hzjd.software.gaokao.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Longlong on 2017/11/22.
 */

public class Fragment1 extends BaseFragment {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    Unbinder unbinder;
    @BindView(R.id.count_down_1)
    TextView countDown1;
    @BindView(R.id.count_down_2)
    TextView countDown2;
    @BindView(R.id.count_down_3)
    TextView countDown3;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_rank)
    TextView tvRank;
    @BindView(R.id.tv_class)
    TextView tvClass;
    private HomePageModel model;
    private UpdateGradeModel model_update;
    private String role,batch,yesterYear,lastYear;
    private Dialog dialog;
    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater myInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = myInflater.inflate(R.layout.fragment_1, container, false);   ;
        unbinder = ButterKnife.bind(this, layout);
        btnBack.setVisibility(View.GONE);
        setTitlebar(layout, "浙江高考报考宝典");
        setBar(layout);
        doRequest();
        getAdresseMAC(getActivity());
        System.out.println("XXXXXXXXXX----+++++====="+PrefUtils.getString(getActivity().getApplication(), "uid", null)+"--------"+
                PrefUtils.getString(getActivity().getApplication(), "utoken", null));

        return layout;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_giveup, R.id.tv_sprint, R.id.tv_reliable, R.id.tv_underline,R.id.btn_modify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_modify:
                getBatch();
                break;
            case R.id.tv_giveup:
                Intent intent_giveup = new Intent(getActivity(), QueryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("intent", "放弃志愿");
                bundle.putString("score", tvGrade.getText().toString());
                bundle.putString("rank", tvRank.getText().toString());
                bundle.putString("batch", batch);
                bundle.putString("role", role);
                bundle.putString("yesterYear",yesterYear);
                bundle.putString("lastYear",lastYear);
                intent_giveup.putExtras(bundle);
                startActivity(intent_giveup);
                break;
            case R.id.tv_sprint:
                Intent intent_sprint = new Intent(getActivity(), QueryActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("intent", "冲刺志愿");
                bundle1.putString("score", tvGrade.getText().toString());
                bundle1.putString("rank", tvRank.getText().toString());
                bundle1.putString("batch", batch);
                bundle1.putString("role", role);
                bundle1.putString("yesterYear",yesterYear);
                bundle1.putString("lastYear",lastYear);
                intent_sprint.putExtras(bundle1);
                startActivity(intent_sprint);
                break;
            case R.id.tv_reliable:
                Intent intent_reliable = new Intent(getActivity(), QueryActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("intent", "稳妥志愿");
                bundle2.putString("score", tvGrade.getText().toString());
                bundle2.putString("rank", tvRank.getText().toString());
                bundle2.putString("batch", batch);
                bundle2.putString("role", role);
                bundle2.putString("yesterYear",yesterYear);
                bundle2.putString("lastYear",lastYear);
                intent_reliable.putExtras(bundle2);
                startActivity(intent_reliable);
                break;
            case R.id.tv_underline:
                Intent intent_underline = new Intent(getActivity(), QueryActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("intent", "保底志愿");
                bundle3.putString("score", tvGrade.getText().toString());
                bundle3.putString("rank", tvRank.getText().toString());
                bundle3.putString("batch",batch);
                bundle3.putString("role", role);
                bundle3.putString("yesterYear",yesterYear);
                bundle3.putString("lastYear",lastYear);
                intent_underline.putExtras(bundle3);
                startActivity(intent_underline);
                break;
        }
    }

    public void doRequest() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        OkGo.post(Constants.HOME_PAGE)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("uid", PrefUtils.getString(getActivity().getApplication(), "uid", null))
                .params("utoken", PrefUtils.getString(getActivity().getApplication(), "utoken", null))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), HomePageModel.class);
                        if (model.status.equals("0")) {
                            tvGrade.setText(model.data.score);
                            tvRank.setText(model.data.ranking);
                            if (model.data.batchNum.equals("1")) {
                                tvClass.setText("第一段");
                            }
                            if (model.data.batchNum.equals("2")) {
                                tvClass.setText("第二段");
                            }
                            if (model.data.batchNum.equals("3")) {
                                tvClass.setText("第三段");
                            }
                            String countdown = model.data.countdown;
                            countDown1.setText(countdown.substring(0, 1));
                            countDown2.setText(countdown.substring(1, 2));
                            countDown3.setText(countdown.substring(2));
                            batch = model.data.batchNum;
                            role = model.data.role;
                            yesterYear = model.data.yesterYear;
                            lastYear = model.data.lastYear;
                        } else if (model.status.equals("1")) {
                            Toast.makeText(getActivity().getApplication(), model.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //     mTextView2.setText(e.getMessage());
                        Toast.makeText(getActivity().getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void ModifyBatch() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(getActivity())) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        OkGo.post(Constants.MODIFY_GRADE_RANK)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("uid", PrefUtils.getString(getActivity().getApplication(),"uid",null))
                .params("token",PrefUtils.getString(getActivity().getApplication(),"utoken",null))
                .params("batchNum", batch)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model_update = JSON.parseObject(s.getBytes(), UpdateGradeModel.class);
                        if (model_update.status.equals("0")){
                          doRequest();
                        }else if (model_update.status.equals("1")){
                            Toast.makeText(getActivity().getApplicationContext(),model_update.msg,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //     mTextView2.setText(e.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(), "数据有误",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getBatch() {

        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choice_batch, null);
        TextView tv_batch1 = (TextView) localView.findViewById(R.id.tv_batch1);
        TextView tv_batch2 = (TextView) localView.findViewById(R.id.tv_batch2);
        TextView tv_batch3 = (TextView) localView.findViewById(R.id.tv_batch3);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(getActivity(), R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
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
        tv_batch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClass.setText("第一段");
                batch = "1";
                ModifyBatch();
                dialog.dismiss();

            }
        });
        tv_batch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClass.setText("第二段");
                batch = "2";
                ModifyBatch();
                dialog.dismiss();

            }
        });
        tv_batch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClass.setText("第三段");
                batch = "3";
                ModifyBatch();
                dialog.dismiss();

            }
        });
    }

    public static String getAdresseMAC(Context context) {
        WifiManager wifiMan = (WifiManager)context.getSystemService(Context.WIFI_SERVICE) ;
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if(wifiInf !=null && marshmallowMacAddress.equals(wifiInf.getMacAddress())){
            String result = null;
            try {
                result= getAdressMacByInterface();
                System.out.println("QQQQQQQ----==="+result);
                if (result != null){
                    return result;
                } else {
                    result = getAddressMacByFile(wifiMan);
                    return result;
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        } else{
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                return wifiInf.getMacAddress();
            } else {
                return "";
            }
        }
        return marshmallowMacAddress;
    }
    private static String getAdressMacByInterface(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }


}
