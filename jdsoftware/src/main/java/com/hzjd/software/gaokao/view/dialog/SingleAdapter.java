/**
 * Author: S.J.H
 *
 * Date: 2016/7/20
 */
package com.hzjd.software.gaokao.view.dialog;

import android.content.Context;


import com.hzjd.software.gaokao.view.wheelview.AbstractWheelTextAdapter;

import java.util.ArrayList;

/**
 * 商店滚轮适配器
 */
public class SingleAdapter extends AbstractWheelTextAdapter {
    public Context context;
    public ArrayList<String> dates;

    public SingleAdapter(Context context, ArrayList<String> dates) {
        super(context);
        this.dates = dates;
    }

    public String getText(int index) {
        return dates.get(index);
    }

    @Override
    protected CharSequence getItemText(int index) {
        return dates.get(index);

    }

    @Override
    public int getItemsCount() {
        return dates.size();
    }
}
