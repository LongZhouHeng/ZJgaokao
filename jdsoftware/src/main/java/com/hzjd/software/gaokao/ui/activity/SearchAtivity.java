package com.hzjd.software.gaokao.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.hzjd.software.gaokao.BaseActivity;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.adapter.RandomResultAdapter;
import com.hzjd.software.gaokao.model.entity.QueryResultModel;
import com.hzjd.software.gaokao.model.entity.ResultListBean;
import com.hzjd.software.gaokao.utils.NetworkUtils;
import com.hzjd.software.gaokao.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Longlong on 2017/12/8.
 */

public class SearchAtivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.btn_randomsearch)
    Button btnRandomsearch;
    @BindView(R.id.tv_lastyear1)
    TextView tvLastyear1;
    private String rank, batch, uid, utoken, collegeName;
    private int pageNum = 1;
    private String pageSize = "20";
    private QueryResultModel model;
    private ArrayList<ResultListBean> resulrlist;
    private RandomResultAdapter mAdapter;

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

        setContentView(R.layout.activity_randomquery);
        ButterKnife.bind(this);
        setBar();
        setTitlebar("搜索查询");
        Bundle bundle = getIntent().getExtras();
        batch = bundle.getString("batch");
        rank = bundle.getString("rank");
        tvLastyear1.setText(bundle.getString("lastyear1"));
        uid = PrefUtils.getString(this.getApplication(), "uid", null);
        utoken = PrefUtils.getString(this.getApplication(), "utoken", null);
        lv = (ListView) findViewById(R.id.lv);
        resulrlist = new ArrayList<ResultListBean>();
        mAdapter = new RandomResultAdapter(this, resulrlist);
        lv.setAdapter(mAdapter);

    }

    @OnClick(R.id.btn_randomsearch)
    public void onViewClicked() {

        if (!TextUtils.isEmpty(etTitle.getText().toString())) {
            collegeName = etTitle.getText().toString();
            getRefresh();
        } else {
            Toast.makeText(getApplicationContext(), "请输入查询内容", Toast.LENGTH_SHORT).show();
        }

    }

    private void getRefresh() {
        refresh.setLoadMore(true);
        refresh.finishRefreshLoadMore();
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {

                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefresh();
                        pageNum = 1;
                        doRequest();

                    }
                }, 500);

            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefreshLoadMore();
                        pageNum++;
                        doRequest();

                    }
                }, 1000);
            }
        });
        refresh.autoRefresh();
    }

    public void doRequest() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        System.out.println("QQQQQQQQ-----=====" + collegeName);
        OkGo.post(Constants.QUERY_RESULT)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("nameNumber", rank)
                .params("batchNum ", batch)
                .params("collegeName", collegeName)
                .params("uid", uid)
                .params("token", utoken)
                .params("pageNum", String.valueOf(pageNum))
                .params("pageSize", pageSize)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), QueryResultModel.class);
                        if (model.status.equals("0")) {

                            if (pageNum == 1) {
                                resulrlist.clear();
                            }
                            resulrlist.addAll((ArrayList<ResultListBean>) model.data);
                            mAdapter.notifyDataSetChanged();
                        } else if (model.status.equals("1")) {
                            Toast.makeText(getApplicationContext(), model.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                        Toast.makeText(getApplicationContext(), "数据有误", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
