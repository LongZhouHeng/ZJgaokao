package com.hzjd.software.gaokao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.hzjd.software.gaokao.R;


public class ImageViewCanvas extends android.support.v7.widget.AppCompatImageView {

	/**
	 * 默认边框宽度为0
	 */
	private static final int DEFAULT_BORDER_WIDTH = 0;
	/**
	 * 默认边框颜色透明
	 */
	private static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;
	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 1;

	private final RectF mDrawableRect = new RectF();
	private final RectF mBorderRect = new RectF();

	private final Matrix mShaderMatrix = new Matrix();
	private final Paint mBitmapPaint = new Paint();
	private final Paint mBorderPaint = new Paint();

	private int mBorderColor = DEFAULT_BORDER_COLOR;
	private int mBorderWidth = DEFAULT_BORDER_WIDTH;

	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private int mBitmapWidth;
	private int mBitmapHeight;

	private float mDrawableRadius;
	private float mBorderRadius;

	private boolean mReady;
	private boolean mSetupPending;

	private String name;
	private PorterDuffXfermode porterDuffXfermode;

	public ImageViewCanvas(Context context) {
		super(context);
		super.setScaleType(SCALE_TYPE);
		mBorderWidth = 2;
		mBorderColor = Color.WHITE;
		mReady = true;
		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}

	}

	public ImageViewCanvas(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ImageViewCanvas(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		super.setScaleType(SCALE_TYPE);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircleImageView, defStyle, 0);

		mBorderWidth = a.getDimensionPixelSize(
				R.styleable.CircleImageView_border_width_size, DEFAULT_BORDER_WIDTH);
		mBorderColor = a.getColor(R.styleable.CircleImageView_border_color_white,
				DEFAULT_BORDER_COLOR);

		a.recycle();

		mReady = true;

		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}
	}

	public void setName(String name) {
		this.name = name;
		invalidate();
	}

	@Override
	public ScaleType getScaleType() {
		return SCALE_TYPE;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType != SCALE_TYPE) {
			throw new IllegalArgumentException(String.format(
					"ScaleType %s not supported.", scaleType));
		}
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// if (getDrawable() == null) {
	// return;
	// }
	//
	// canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius,
	// mBitmapPaint);
	// canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius,
	// mBorderPaint);
	// if (!TextUtils.isEmpty(name)) {
	// Paint paint = new Paint();
	// paint.setColor(Color.BLACK);
	// paint.setAntiAlias(true);// 去除锯齿
	// paint.setFilterBitmap(true);// 对位图进行滤波处理
	// paint.setTextSize(20);
	// canvas.drawText(name, getWidth() / 2 - name.length() * 10,
	// getHeight() - 8, paint);
	// }
	// }

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}

		canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius,
				mBitmapPaint);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius,
				mBorderPaint);
		if (!TextUtils.isEmpty(name)) {
			Log.i("name", "onDraw: "+name);
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setAntiAlias(true);// 去除锯齿
			paint.setFilterBitmap(true);// 对位图进行滤波处理
			paint.setTextSize(getWidth() / 5);

			float textLength = paint.measureText(name);
			
			
			Paint paint1 = new Paint();
			paint1.setColor(Color.parseColor("#80000000"));
			paint1.setAntiAlias(true);// 去除锯齿
			paint1.setStyle(Paint.Style.FILL);// 充满
			/**
			 * 圆和矩形重合部分
			 */
			PorterDuff.Mode MODE = PorterDuff.Mode.SRC_ATOP;
			porterDuffXfermode = new PorterDuffXfermode(MODE);
			paint1.setXfermode(porterDuffXfermode);
//			绘制矩形
	/*		canvas.drawRect( getWidth() / 2 - textLength / 2, getHeight() - getWidth() / 10 - getWidth() / 5,
					getWidth() / 2 + textLength / 2, getHeight()- getWidth() / 10, paint1);*/
//		绘制文本
		/*	canvas.drawText(name, getWidth() / 2 - textLength / 2, getHeight()
					- getWidth() / 10, paint);*/

			canvas.drawRect( 0, getHeight() - getWidth() / 10 - getWidth() / 5,
					getWidth(), getHeight(), paint1);

			canvas.drawText(name, getWidth() / 2 - textLength / 2, getHeight()
					- getWidth() / 10-2, paint);



		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int borderColor) {
		if (borderColor == mBorderColor) {
			return;
		}

		mBorderColor = borderColor;
		mBorderPaint.setColor(mBorderColor);
		invalidate();
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		if (borderWidth == mBorderWidth) {
			return;
		}

		mBorderWidth = borderWidth;
		setup();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mBitmap = getBitmapFromDrawable(drawable);
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	private void setup() {
		if (!mReady) {
			mSetupPending = true;
			return;
		}

		if (mBitmap == null) {
			return;
		}

		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);

		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);

		PorterDuff.Mode MODE = PorterDuff.Mode.SRC_ATOP;
		porterDuffXfermode = new PorterDuffXfermode(MODE);
		mBorderPaint.setXfermode(porterDuffXfermode);

		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();


		mBorderRect.set(0, 0, getWidth(), getHeight());
		mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2,
				(mBorderRect.width() - mBorderWidth) / 2);

		mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width()
				- mBorderWidth, mBorderRect.height() - mBorderWidth);
		mDrawableRadius = Math.min(mDrawableRect.height() / 2,
				mDrawableRect.width() / 2);

		updateShaderMatrix();
		invalidate();
	}

	private void updateShaderMatrix() {
		float scale;
		float dx = 0;
		float dy = 0;

		mShaderMatrix.set(null);

		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
				* mBitmapHeight) {
			scale = mDrawableRect.height() / (float) mBitmapHeight;
			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
		} else {
			scale = mDrawableRect.width() / (float) mBitmapWidth;
			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
		}

		mShaderMatrix.setScale(scale, scale);
		mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth,
				(int) (dy + 0.5f) + mBorderWidth);

		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

}
