package com.hzjd.software.gaokao.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hzjd.software.gaokao.R;
import com.hzjd.software.gaokao.view.WheelView;
import com.hzjd.software.gaokao.view.wheelview.AbstractWheelTextAdapter;


public class DialogFactory {

	public static Dialog getTrainSingleDialog(Context context,
											  AbstractWheelTextAdapter adapter, final IConfirmListener listener) {
		final Dialog mDialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		View mDialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_single_wheel, null);
		final WheelView wheelview = (WheelView) mDialogView
				.findViewById(R.id.wheelview);
		TextView cancle;
		TextView finish;
		cancle = (TextView) mDialogView.findViewById(R.id.cancle);
		finish = (TextView) mDialogView.findViewById(R.id.finish);
		wheelview.setVisibleItems(7);
		// wheelview.addChangingListener(new OnWheelChangedListener() {
		//
		// @Override
		// public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// // sexTag = newValue + "";
		//
		// }
		// });
		wheelview.setViewAdapter(adapter);
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bg:
					mDialog.dismiss();
					break;
				case R.id.cancle:
					mDialog.dismiss();
					listener.onCancel(wheelview.getCurrentItem());
					break;
				case R.id.finish:
					mDialog.dismiss();
					listener.onConfirm(wheelview.getCurrentItem());
					break;

				default:
					break;
				}
			}
		};
		mDialogView.findViewById(R.id.bg).setOnClickListener(onClickListener);
		cancle.setOnClickListener(onClickListener);
		finish.setOnClickListener(onClickListener);
		mDialog.setContentView(mDialogView);
		return mDialog;
	}

	public static Dialog getTwoWheelDialog(Context context,
                                           AbstractWheelTextAdapter adapter1,
                                           AbstractWheelTextAdapter adapter2,
                                           final IThreeConfirmListener listener) {
		final Dialog mDialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		View mDialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_two_wheel, null);
		final WheelView wheelview1 = (WheelView) mDialogView
				.findViewById(R.id.wheelview1);
		final WheelView wheelview2 = (WheelView) mDialogView
				.findViewById(R.id.wheelview2);
		TextView cancle;
		TextView finish;
		cancle = (TextView) mDialogView.findViewById(R.id.cancle);
		finish = (TextView) mDialogView.findViewById(R.id.finish);
		wheelview1.setVisibleItems(7);
		wheelview1.setViewAdapter(adapter1);
		wheelview2.setVisibleItems(7);
		wheelview2.setViewAdapter(adapter2);
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bg:
					mDialog.dismiss();
					break;
				case R.id.cancle:
					mDialog.dismiss();
					break;
				case R.id.finish:
					mDialog.dismiss();
					listener.onConfirm(wheelview1.getCurrentItem(),
							wheelview2.getCurrentItem(), 0);
					break;
				default:
					break;
				}
			}
		};
		mDialogView.findViewById(R.id.bg).setOnClickListener(onClickListener);
		cancle.setOnClickListener(onClickListener);
		finish.setOnClickListener(onClickListener);
		mDialog.setContentView(mDialogView);
		return mDialog;
	}

	public static Dialog getThreeWheelDialog(Context context,
                                             AbstractWheelTextAdapter adapter1,
                                             AbstractWheelTextAdapter adapter2,
                                             AbstractWheelTextAdapter adapter3,
                                             final IThreeConfirmListener listener) {
		final Dialog mDialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		View mDialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_three_wheel, null);
		final WheelView wheelview1 = (WheelView) mDialogView
				.findViewById(R.id.wheelview1);
		final WheelView wheelview2 = (WheelView) mDialogView
				.findViewById(R.id.wheelview2);
		final WheelView wheelview3 = (WheelView) mDialogView
				.findViewById(R.id.wheelview3);
		TextView cancle;
		TextView finish;
		cancle = (TextView) mDialogView.findViewById(R.id.cancle);
		finish = (TextView) mDialogView.findViewById(R.id.finish);
		wheelview1.setVisibleItems(7);
		wheelview1.setViewAdapter(adapter1);
		wheelview2.setVisibleItems(7);
		wheelview2.setViewAdapter(adapter2);
		wheelview3.setVisibleItems(7);
		wheelview3.setViewAdapter(adapter3);
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bg:
					mDialog.dismiss();
					break;
				case R.id.cancle:
					mDialog.dismiss();
					break;
				case R.id.finish:
					mDialog.dismiss();
					listener.onConfirm(wheelview1.getCurrentItem(),
							wheelview2.getCurrentItem(),
							wheelview3.getCurrentItem());
					break;

				default:
					break;
				}
			}
		};
		mDialogView.findViewById(R.id.bg).setOnClickListener(onClickListener);
		cancle.setOnClickListener(onClickListener);
		finish.setOnClickListener(onClickListener);
		mDialog.setContentView(mDialogView);
		return mDialog;
	}

	public static Dialog getConfirmDialog(Context context, String text,
                                          String left, String right, final IConfirmListener listener) {
		final Dialog mDialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		View mDialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_confirm, null);
		TextView dialog_msg;
		Button dialog_left = (Button) mDialogView
				.findViewById(R.id.dialog_left);
		Button dialog_right = (Button) mDialogView
				.findViewById(R.id.dialog_right);
		dialog_left.setText(left);
		dialog_right.setText(right);
		dialog_msg = (TextView) mDialogView.findViewById(R.id.dialog_msg);
		dialog_msg.setText(text);

		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.dialog_bg:
					// mDialog.dismiss();
					break;
				case R.id.dialog_left:
					mDialog.dismiss();
					listener.onCancel(0);
					break;
				case R.id.dialog_right:
					mDialog.dismiss();
					listener.onConfirm(0);
					break;
				case R.id.dialog_content:
					break;
				default:
					break;
				}
			}
		};
		mDialogView.findViewById(R.id.dialog_bg).setOnClickListener(
				onClickListener);
		mDialogView.findViewById(R.id.dialog_content).setOnClickListener(
				onClickListener);
		dialog_left.setOnClickListener(onClickListener);
		dialog_right.setOnClickListener(onClickListener);
		mDialog.setContentView(mDialogView);
		return mDialog;
	}

	public static Dialog getConfirmDialog(Context context, String text,
                                          final IConfirmListener listener) {

		return getConfirmDialog(context, text, "取消", "确定", listener);
	}

	public static Dialog getConfirm2Dialog(Context context, String text,
                                           final IConfirmListener listener) {
		final Dialog mDialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		View mDialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_confirm_ok, null);
		TextView dialog_msg;
		dialog_msg = (TextView) mDialogView.findViewById(R.id.dialog_msg);
		dialog_msg.setText(text);

		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.dialog_bg:
					// mDialog.dismiss();
					break;
				case R.id.dialog_confirm:
					mDialog.dismiss();
					listener.onConfirm(0);
					break;
				case R.id.dialog_content:
					break;
				default:
					break;
				}
			}
		};
		mDialogView.findViewById(R.id.dialog_bg).setOnClickListener(
				onClickListener);
		mDialogView.findViewById(R.id.dialog_content).setOnClickListener(
				onClickListener);
		mDialogView.findViewById(R.id.dialog_confirm).setOnClickListener(
				onClickListener);
		mDialog.setContentView(mDialogView);
		return mDialog;
	}

	public static Dialog getPicImgDialog(Context context,
                                         final OnClickListener listener) {
		final Dialog mDialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		View mDialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_pic_img, null);
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.dialog_bg:
				case R.id.cancel:
					mDialog.dismiss();
					break;
				case R.id.camera:
				case R.id.gallery:
					mDialog.dismiss();
					listener.onClick(v);
					break;
				default:
					break;
				}
			}
		};
		mDialogView.findViewById(R.id.camera).setOnClickListener(
				onClickListener);
		mDialogView.findViewById(R.id.gallery).setOnClickListener(
				onClickListener);
		mDialogView.findViewById(R.id.dialog_bg).setOnClickListener(
				onClickListener);
		mDialogView.findViewById(R.id.cancel).setOnClickListener(
				onClickListener);
		mDialog.setContentView(mDialogView);
		return mDialog;
	}

	public static Dialog getTipDialog(Context context, String text1,
                                      String text2) {
		final Dialog mDialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		View mDialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_tip, null);
		TextView title = (TextView) mDialogView.findViewById(R.id.title);
		TextView content = (TextView) mDialogView.findViewById(R.id.content);
		title.setText(text1);
		content.setText(text2);

		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.close:
					mDialog.dismiss();
					break;
				default:
					break;
				}
			}
		};
		mDialogView.findViewById(R.id.close)
				.setOnClickListener(onClickListener);
		mDialog.setContentView(mDialogView);
		return mDialog;
	}



}
