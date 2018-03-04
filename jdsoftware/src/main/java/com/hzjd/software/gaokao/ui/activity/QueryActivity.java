package com.hzjd.software.gaokao.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.hzjd.software.gaokao.BaseActivity;
import com.hzjd.software.gaokao.Constants;
import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.adapter.QueryResultAdapter;
import com.hzjd.software.gaokao.model.entity.HomePageModel;
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
 * Created by Longlong on 2017/12/1.
 */

@SuppressLint("Registered")
public class QueryActivity extends BaseActivity {
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @BindView(R.id.tv_academy)
    TextView tvAcademy;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.tv_academys)
    TextView tvAcademys;
    @BindView(R.id.tv_majors)
    TextView tvMajors;
    @BindView(R.id.tv_score1)
    TextView tvScore1;
    @BindView(R.id.ll_buy)
    LinearLayout llBuy;
    @BindView(R.id.tv_lastyear)
    TextView tvLastyear;
    @BindView(R.id.tv_academys1)
    TextView tvAcademys1;
    @BindView(R.id.tv_majors1)
    TextView tvMajors1;
    @BindView(R.id.tv_score11)
    TextView tvScore11;
    @BindView(R.id.tv_academys2)
    TextView tvAcademys2;
    @BindView(R.id.tv_majors2)
    TextView tvMajors2;
    @BindView(R.id.tv_score12)
    TextView tvScore12;
    @BindView(R.id.ll_aacdmy1)
    LinearLayout llAacdmy1;
    @BindView(R.id.ll_aacdmy2)
    LinearLayout llAacdmy2;
    @BindView(R.id.ll_aacdmy3)
    LinearLayout llAacdmy3;
    private String title, score, rank, batch, selectType, uid, utoken, role, yesterYear, lastYear;
    private int pageNum = 1;
    private String pageSize = "20";
    private int flag;
    private QueryResultModel model;
    private HomePageModel Rolemodel;
    private ArrayList<ResultListBean> resulrlist;
    private QueryResultAdapter mAdapter;
    private String price;
    private int color;
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

        setContentView(R.layout.acrivty_query);
        ButterKnife.bind(this);
        setBar();
      //   color = Color.argb(255, 253, 250, 235);
        lv = (ListView) findViewById(R.id.lv);
        resulrlist = new ArrayList<ResultListBean>();
        mAdapter = new QueryResultAdapter(this, resulrlist);
        lv.setAdapter(mAdapter);
        Bundle bundle = getIntent().getExtras();
        yesterYear = bundle.getString("yesterYear");
        lastYear = bundle.getString("lastYear");
        if (bundle.getString("intent").equals("放弃志愿")) {
            title = bundle.getString("intent");
            score = bundle.getString("score");
            rank = bundle.getString("rank");
            batch = bundle.getString("batch");
            setTitlebar(title);
            System.out.println("QQQQQQQQ-----=====" + rank + batch);
            tvAcademy.setText("放弃");
            tvMajor.setText("放弃");
            selectType = "1";
            flag = 1;

        }
        if (bundle.getString("intent").equals("冲刺志愿")) {
            title = bundle.getString("intent");
            score = bundle.getString("score");
            rank = bundle.getString("rank");
            batch = bundle.getString("batch");
            setTitlebar(title);
            tvAcademy.setText("冲刺");
            tvMajor.setText("冲刺");
            selectType = "2";
            flag = 2;

        }
        if (bundle.getString("intent").equals("稳妥志愿")) {
            title = bundle.getString("intent");
            score = bundle.getString("score");
            rank = bundle.getString("rank");
            batch = bundle.getString("batch");
            setTitlebar(title);
            tvAcademy.setText("稳妥");
            tvMajor.setText("稳妥");
            selectType = "3";
            flag = 3;

        }
        if (bundle.getString("intent").equals("保底志愿")) {
            title = bundle.getString("intent");
            score = bundle.getString("score");
            rank = bundle.getString("rank");
            batch = bundle.getString("batch");
            setTitlebar(title);
            tvAcademy.setText("保底");
            tvMajor.setText("保底");
            selectType = "4";
            flag = 4;
        }
        System.out.println("SSSSSSSSS----=====" + batch);
        //      getresult();
        getRefresh();
        getRole();


    }

    @OnClick({R.id.tv_seach, R.id.btn_openkey})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_seach:
                if (role.equals("2")) {
                    Intent intent = new Intent(this, SearchAtivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("batch", batch);
                    bundle.putString("rank", rank);
                    bundle.putString("lastyear1", lastYear);
                    bundle.putString("yesteryear1", yesterYear);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "请先解锁功能后再进行搜索", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_openkey:
                Intent intent1_buy = new Intent(this, BuyFunctionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("price", price);
                intent1_buy.putExtras(bundle);
                startActivityForResult(intent1_buy, 1);
                break;
        }
    }

    private void getRefresh() {
        refresh.setLoadMore(true);
        refresh.finishRefreshLoadMore();
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //   Toast.makeText(getActivity(), "pull refresh", Toast.LENGTH_LONG).show();
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefresh();
                    /*    if (!NetworkUtils.isNetworkAvaliable(getActivity())) {

                        } else {*/
                        pageNum = 1;
                        //          tvPagenumber.setText(pageNum);
                        getRole();
                        //                 }

                    }
                }, 1500);

            }

            @Override
            public void onfinish() {
                //  Toast.makeText(getActivity(), "finish", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                //     Toast.makeText(getActivity(), "load more", Toast.LENGTH_LONG).show();
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefreshLoadMore();
                        /*if (!NetworkUtils.isNetworkAvaliable(getActivity())) {

                        } else {*/
                        pageNum++;
                        getRole();
                        //            }


                    }
                }, 1500);
            }
        });
        refresh.autoRefresh();
    }


    public void getRole() {
        if (isLoading) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaliable(this)) {
            toastIfActive(R.string.errcode_network_unavailable);
            return;
        }
        OkGo.post(Constants.HOME_PAGE)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("uid", PrefUtils.getString(this.getApplication(), "uid", null))
                .params("utoken", PrefUtils.getString(this.getApplication(), "utoken", null))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Rolemodel = JSON.parseObject(s.getBytes(), HomePageModel.class);
                        if (Rolemodel.status.equals("0")) {
                            role = Rolemodel.data.role;
                            price = Rolemodel.data.price;
                            Bundle bundle = getIntent().getExtras();
                            if (bundle.getString("intent").equals("放弃志愿")) {
                                title = bundle.getString("intent");
                                score = bundle.getString("score");
                                rank = bundle.getString("rank");
                                batch = bundle.getString("batch");
                                role = Rolemodel.data.role;
                                yesterYear = bundle.getString("yesterYear");
                                lastYear = bundle.getString("lastYear");
                                setTitlebar(title);
                                tvLastyear.setText(lastYear);
                                System.out.println("QQQQQQQQ-----=====" + rank + batch);
                                tvAcademy.setText("放弃");
                                tvMajor.setText("放弃");
                                selectType = "1";
                                flag = 1;

                            }
                            if (bundle.getString("intent").equals("冲刺志愿")) {
                                title = bundle.getString("intent");
                                score = bundle.getString("score");
                                rank = bundle.getString("rank");
                                batch = bundle.getString("batch");
                                role = Rolemodel.data.role;
                                yesterYear = bundle.getString("yesterYear");
                                lastYear = bundle.getString("lastYear");
                                setTitlebar(title);
                                tvLastyear.setText(lastYear);

                                tvAcademy.setText("冲刺");
                                tvMajor.setText("冲刺");
                                selectType = "2";
                                flag = 2;

                            }
                            if (bundle.getString("intent").equals("稳妥志愿")) {
                                title = bundle.getString("intent");
                                score = bundle.getString("score");
                                rank = bundle.getString("rank");
                                batch = bundle.getString("batch");
                                role = Rolemodel.data.role;
                                yesterYear = bundle.getString("yesterYear");
                                lastYear = bundle.getString("lastYear");
                                setTitlebar(title);
                                tvLastyear.setText(lastYear);

                                tvAcademy.setText("稳妥");
                                tvMajor.setText("稳妥");
                                selectType = "3";
                                flag = 3;

                            }
                            if (bundle.getString("intent").equals("保底志愿")) {
                                title = bundle.getString("intent");
                                score = bundle.getString("score");
                                rank = bundle.getString("rank");
                                batch = bundle.getString("batch");
                                role = Rolemodel.data.role;
                                yesterYear = bundle.getString("yesterYear");
                                lastYear = bundle.getString("lastYear");
                                setTitlebar(title);
                                tvLastyear.setText(lastYear);
                                tvAcademy.setText("保底");
                                tvMajor.setText("保底");
                                selectType = "4";
                                flag = 4;
                            }
                            if (flag == 1) {
                                selectType = "1";
                                rank = bundle.getString("rank");
                                batch = bundle.getString("batch");
                                uid = PrefUtils.getString(QueryActivity.this.getApplication(), "uid", null);
                                utoken = PrefUtils.getString(QueryActivity.this.getApplication(), "utoken", null);
                                doRequest();
                            }
                            if (flag == 2) {
                                selectType = "2";
                                rank = bundle.getString("rank");
                                batch = bundle.getString("batch");
                                uid = PrefUtils.getString(QueryActivity.this.getApplication(), "uid", null);
                                utoken = PrefUtils.getString(QueryActivity.this.getApplication(), "utoken", null);
                                doRequest();
                            }
                            if (flag == 3) {
                                selectType = "3";
                                rank = bundle.getString("rank");
                                batch = bundle.getString("batch");
                                uid = PrefUtils.getString(QueryActivity.this.getApplication(), "uid", null);
                                utoken = PrefUtils.getString(QueryActivity.this.getApplication(), "utoken", null);
                                doRequest();
                            }
                            if (flag == 4) {
                                selectType = "4";
                                rank = bundle.getString("rank");
                                batch = bundle.getString("batch");
                                uid = PrefUtils.getString(QueryActivity.this.getApplication(), "uid", null);
                                utoken = PrefUtils.getString(QueryActivity.this.getApplication(), "utoken", null);
                                doRequest();
                            }
                        } else if (Rolemodel.status.equals("1")) {
                            Toast.makeText(QueryActivity.this.getApplication(), Rolemodel.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //     mTextView2.setText(e.getMessage());
                        Toast.makeText(QueryActivity.this.getApplication(), "数据有误", Toast.LENGTH_SHORT).show();
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
        OkGo.post(Constants.QUERY_RESULT)
                .tag(this)
                .cacheKey("cachePostKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("nameNumber", rank)
                .params("batchNum ", batch)
                .params("selectType", selectType)
                .params("uid", uid)
                .params("token", utoken)
                .params("pageNum", String.valueOf(pageNum))
                .params("pageSize", pageSize)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        model = JSON.parseObject(s.getBytes(), QueryResultModel.class);
                        if (model.status.equals("0")) {
                            if (role.equals("2")) {

                                if (pageNum == 1) {
                                    resulrlist.clear();
                                }
                                resulrlist.addAll((ArrayList<ResultListBean>) model.data);
                                mAdapter.notifyDataSetChanged();
                                llBuy.setVisibility(View.GONE);
                                refresh.setVisibility(View.VISIBLE);

                            } else {

                                refresh.setVisibility(View.GONE);
                                llBuy.setVisibility(View.VISIBLE);
                                resulrlist.clear();
                                resulrlist.addAll((ArrayList<ResultListBean>) model.data);

                                if (resulrlist.size()<1){
                                    llAacdmy1.setVisibility(View.GONE);
                                    llAacdmy2.setVisibility(View.GONE);
                                    llAacdmy3.setVisibility(View.GONE);
                                }
                                if (resulrlist.size()<2&&resulrlist.size()>0){
                                    llAacdmy1.setBackgroundResource(R.color.text_color);
                                    llAacdmy1.setVisibility(View.VISIBLE);
                                    llAacdmy2.setVisibility(View.GONE);
                                    llAacdmy3.setVisibility(View.GONE);
                                    String str1 = model.data.get(0).collegeName;
                                    String str2 = model.data.get(0).majorName;
                                    String str3 = model.data.get(0).lastYearLine;

                                    tvAcademys.setText(str1);
                                    tvMajors.setText(str2);
                                    tvScore1.setText(str3);


                                }
                                if (resulrlist.size()<3&&resulrlist.size()>1){
                    //                int color = Color.argb(255, 247, 247, 241);
                                    llAacdmy3.setVisibility(View.GONE);
                                    llAacdmy1.setVisibility(View.VISIBLE);
                                    llAacdmy2.setVisibility(View.VISIBLE);
                                    llAacdmy1.setBackgroundResource(R.color.text_color);
                                    String str1 = model.data.get(0).collegeName;
                                    String str2 = model.data.get(0).majorName;
                                    String str3 = model.data.get(0).lastYearLine;

                                    tvAcademys.setText(str1);
                                    tvMajors.setText(str2);
                                    tvScore1.setText(str3);


                                    String str4 = model.data.get(1).collegeName;
                                    String str5 = model.data.get(1).majorName;
                                    String str6 = model.data.get(1).lastYearLine;

                                    tvAcademys1.setText(str4);
                                    tvMajors1.setText(str5);
                                    tvScore11.setText(str6);



                                }
                                if (resulrlist.size()>2) {
                      //              int color = Color.argb(255, 247, 247, 241);
                                    llAacdmy1.setVisibility(View.VISIBLE);
                                    llAacdmy2.setVisibility(View.VISIBLE);
                                    llAacdmy3.setVisibility(View.VISIBLE);
                                    llAacdmy1.setBackgroundResource(R.color.text_color);
                                    llAacdmy3.setBackgroundResource(R.color.text_color);
                                    String str1 = model.data.get(0).collegeName;
                                    String str2 = model.data.get(0).majorName;
                                    String str3 = model.data.get(0).lastYearLine;

                                    tvAcademys.setText(str1);
                                    tvMajors.setText(str2);
                                    tvScore1.setText(str3);


                                    String str4 = model.data.get(1).collegeName;
                                    String str5 = model.data.get(1).majorName;
                                    String str6 = model.data.get(1).lastYearLine;

                                    tvAcademys1.setText(str4);
                                    tvMajors1.setText(str5);
                                    tvScore11.setText(str6);


                                    String str7 = model.data.get(2).collegeName;
                                    String str8 = model.data.get(2).majorName;
                                    String str9 = model.data.get(2).lastYearLine;

                                    tvAcademys2.setText(str7);
                                    tvMajors2.setText(str8);
                                    tvScore12.setText(str9);

                                }
                            }

                        } else if (model.status.equals("1")) {
                            Toast.makeText(getApplicationContext(), model.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //     mTextView2.setText(e.getMessage());
                        Toast.makeText(getApplicationContext(), model.msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            getRefresh();
            getRole();
        }
        if (resultCode == 2) {

        }


    }
}