package com.hzjd.software.gaokao.wxapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.hzjd.software.gaokao.BaseActivity;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.model.entity.CancelOrdermodel;
import com.hzjd.software.gaokao.ui.activity.BuyFunctionActivity;
import com.hzjd.software.gaokao.utils.NetworkUtils;
import com.hzjd.software.gaokao.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import okhttp3.Call;
import okhttp3.Response;

import static com.hs.camera.CameraView.TAG;


@SuppressLint("Registered")
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private CancelOrdermodel cancelOrdermodel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);

        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

            Log.d(TAG, "onPayFinish, errCode = " + resp.errCode + resp);

            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
            //        Toast.makeText(getApplicationContext(),"0",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_COMM:
          //          Toast.makeText(getApplicationContext(),"-1",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
               //     Toast.makeText(getApplicationContext(),"-2",Toast.LENGTH_SHORT).show();
                    Cancel_Order();
                    finish();
                    break;

                default:
                    break;
            }
        finish();
    }
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
                .params("out_trade_no", PrefUtils.getString(getApplication(),"trade_num",null))
                .params("uid", PrefUtils.getString(this.getApplication(), "uid", null))
                .params("utoken", PrefUtils.getString(this.getApplication(), "utoken", null))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        cancelOrdermodel = JSON.parseObject(s.getBytes(), CancelOrdermodel.class);
                        if (cancelOrdermodel.status.equals("0")) {
                            Toast.makeText(WXPayEntryActivity.this.getApplication(), cancelOrdermodel.data, Toast.LENGTH_SHORT).show();
                        } else if (cancelOrdermodel.status.equals("1")) {
                            Toast.makeText(WXPayEntryActivity.this.getApplication(), cancelOrdermodel.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(WXPayEntryActivity.this.getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}