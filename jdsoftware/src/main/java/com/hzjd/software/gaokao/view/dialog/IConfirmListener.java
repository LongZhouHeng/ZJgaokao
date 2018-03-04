/**
 * Author: S.J.H
 *
 * Date: 2016/7/13
 */
package com.hzjd.software.gaokao.view.dialog;

/**
 * 列表item中点击监听器
 */
public interface IConfirmListener {
    public void onCancel(int position);

    public void onConfirm(int position);
}
