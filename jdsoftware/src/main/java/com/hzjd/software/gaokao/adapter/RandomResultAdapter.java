package com.hzjd.software.gaokao.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.model.entity.ResultListBean;

import java.util.ArrayList;

/**
 * Created by Longlong on 2017/12/7.
 */

public class RandomResultAdapter extends BaseAdapter {

    private ArrayList<ResultListBean> resulrlist;
    private Activity mActivity;
    public RandomResultAdapter(FragmentActivity mActivity, ArrayList<ResultListBean> resulrlist) {

        this.mActivity = mActivity;
        this.resulrlist = resulrlist;
    }

    @Override
    public int getCount() {
        return resulrlist.size();
    }

    @Override
    public Object getItem(int position) {
        return resulrlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.listitem_randomresult, null);
            holder = ViewHolder.findAndCacheViews(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final  ResultListBean addList = resulrlist.get(position);

        holder.tv_academys.setText(addList.collegeName);

        holder.tv_majors.setText(addList.majorName);
        holder.tv_score1.setText(addList.lastYearLine);
        holder.tv_extend.setText(addList.suggest);
        //   这里不用循环也可以的，因为getView方法本身就会去循环listView来画item，这里的
        //   循环i和position是等价的，i的 值就是position的值
        for (int i = 0; i < getCount(); i++) {
            if (position == i) {
                if (i % 2 == 0) {

                    holder.ll_item.setBackgroundResource(R.color.text_color);
                } else {
                    holder.ll_item.setBackgroundResource(R.color.white);
                }
            }
        }
        return convertView;
    }


    public static class ViewHolder {


        TextView tv_academys;
        TextView tv_majors;
        TextView tv_score1;
        LinearLayout ll_item;
        TextView tv_extend;
        private static ViewHolder findAndCacheViews(View view) {
            ViewHolder holder = new ViewHolder();
            holder.tv_academys = (TextView) view.findViewById(R.id.tv_academys);
            holder.tv_majors = (TextView) view.findViewById(R.id.tv_majors);
            holder.tv_score1 = (TextView) view.findViewById(R.id.tv_score1);
            holder.tv_extend = (TextView) view.findViewById(R.id.tv_extend);
            holder.ll_item = (LinearLayout)view.findViewById(R.id.ll_item);
            view.setTag(holder);
            return holder;
        }
    }



}
