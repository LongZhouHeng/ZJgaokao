package com.hzjd.software.gaokao.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.hzjd.software.gaokao.BaseActivity;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.model.entity.AliPayModel;
import com.hzjd.software.gaokao.model.entity.CancelOrdermodel;
import com.hzjd.software.gaokao.model.entity.WeChatPayModel;
import com.hzjd.software.gaokao.utils.NetworkUtils;
import com.hzjd.software.gaokao.utils.PayResult;
import com.hzjd.software.gaokao.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Longlong on 2017/12/4.
 */

@SuppressLint("Registered")
public class BuyFunctionActivity extends BaseActivity {

    @BindView(R.id.tv_buymoney)
    TextView tvBuymoney;
    private Dialog dialog;
    private AliPayModel aliPayModel;
    private WeChatPayModel weChatPayModel;
    private CancelOrdermodel cancelOrdermodel;
    private String authInfo;
    private static final int SDK_PAY_FLAG = 1;
    private String trade_no;
    private String price;
    private String appIds, partnerIds, nonceStrs, timeStamps, signs, prepayIds,packageValues;
    // IWXAPI 是第三方app和微信通信的openapi接口
    public IWXAPI api;
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

        setContentView(R.layout.activity_buyfunction);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        price = bundle.getString("price");
        tvBuymoney.setText(price);
        setTitlebar("查询购买");
        setBar();
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(BuyFunctionActivity.this, Constants.APP_ID, true);
        // 将该app注册到微信
        api.registerApp(Constants.APP_ID);
    }
    //返回按钮
    public void btn_back_1(View v) {
        Intent intent = new Intent(this, QueryActivity.class);
        setResult(1, intent);
        finish();
    }

    @OnClick({R.id.btn_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //功能解锁购买
            case R.id.btn_buy:
                getPayMethod();
                break;
        }
    }
    //选择支付方式
    private void getPayMethod() {
        View localView = LayoutInflater.from(this).inflate(R.layout.dialog_buyfunction, null);
        TextView tv_batch2 = (TextView) localView.findViewById(R.id.tv_alipay);
        TextView tv_batch3 = (TextView) localView.findViewById(R.id.tv_wechat);
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

        tv_batch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAliPay();
                dialog.dismiss();

            }
        });
        tv_batch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeChatPay();
                dialog.dismiss();

            }
        });
    }
    //获取微信加签
    public void getWeChatPay() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        OkGo.post(Constants.ORDER_WECHAT)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("body", "功能支付")
                .params("subject", "报考宝典·功能全解锁")
                .params("total_amount", price)
                .params("uid", PrefUtils.getString(this.getApplication(), "uid", null))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        weChatPayModel = JSON.parseObject(s.getBytes(), WeChatPayModel.class);
                        if (weChatPayModel.status.equals("0")) {
                            PrefUtils.putString(getApplication(), "trade_num", weChatPayModel.data.out_trade_no);
                            appIds = Constants.APP_ID;
                            partnerIds = Constants.MCH_ID;
                            nonceStrs = weChatPayModel.data.nonce_str;
                            timeStamps = weChatPayModel.data.timestamp;
                            signs = weChatPayModel.data.sign;
                            prepayIds = weChatPayModel.data.prepay_id;
                            packageValues = "Sign=WXPay";
                            try {
                                PayReq req = new PayReq();

                                req.appId = appIds;
                                req.partnerId = partnerIds;
                                req.prepayId = prepayIds;
                                req.nonceStr = nonceStrs;
                                req.timeStamp = timeStamps;
                                req.packageValue = packageValues;
                                req.sign = signs;
                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                api.sendReq(req);
                                Log.e("orion", "req.checkArgs() = " + req.checkArgs());
                            } catch (Exception e) {
                                Log.e("PAY_GET", "异常：" + e.getMessage());
                                Toast.makeText(BuyFunctionActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        } else if (weChatPayModel.status.equals("1")) {
                            Toast.makeText(BuyFunctionActivity.this.getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(BuyFunctionActivity.this.getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }
   //获取支付宝加签
    public void getAliPay() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        OkGo.post(Constants.ALIAPY_ORDER)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("body", "功能支付")
                .params("subject", "报考宝典·功能全解锁")
                .params("total_amount", price)
                .params("uid", PrefUtils.getString(this.getApplication(), "uid", null))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        aliPayModel = JSON.parseObject(s.getBytes(), AliPayModel.class);
                        if (aliPayModel.status.equals("0")) {
                            authInfo = aliPayModel.data.sign;
                            trade_no = aliPayModel.data.out_trade_no;
                            Alipay();
                        } else if (aliPayModel.status.equals("1")) {
                            Toast.makeText(BuyFunctionActivity.this.getApplication(), aliPayModel.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(BuyFunctionActivity.this.getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //调起支付宝支付
    public void Alipay() {

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(BuyFunctionActivity.this);
                Map<String, String> result = alipay.payV2(authInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(BuyFunctionActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

                    } else if (TextUtils.equals(resultStatus, "6001")){

                        Cancel_Order();
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        //            Toast.makeText(Pay_Activity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(BuyFunctionActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }

        ;
    };
   //取消订单支付
    public void Cancel_Order() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        OkGo.post(Constants.CANCEL_ORDER)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("out_trade_no", trade_no)
                .params("uid", PrefUtils.getString(this.getApplication(), "uid", null))
                .params("utoken", PrefUtils.getString(this.getApplication(), "utoken", null))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        cancelOrdermodel = JSON.parseObject(s.getBytes(), CancelOrdermodel.class);
                        if (cancelOrdermodel.status.equals("0")) {
                            Toast.makeText(BuyFunctionActivity.this.getApplication(), cancelOrdermodel.data, Toast.LENGTH_SHORT).show();
                        } else if (cancelOrdermodel.status.equals("1")) {
                            Toast.makeText(BuyFunctionActivity.this.getApplication(), cancelOrdermodel.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(BuyFunctionActivity.this.getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
