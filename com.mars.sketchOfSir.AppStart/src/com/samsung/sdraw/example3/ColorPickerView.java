package com.samsung.sdraw.example3;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.mars.sketchOfSir.R;


class ColorPickerView extends ImageView {
	private Bitmap						mSpectrum;
	private onColorChangedListener 		mColorChangeListener;
	private Drawable 					mCursorDrawable;
	private Rect						mCursorRect;
	private int							mDrawableW, mDrawableH;
	private Paint						mBorderPaint;
	
	public ColorPickerView(Context context) {
		super(context);
		initView();
	}
	public ColorPickerView(Context context, AttributeSet attr) {
		super(context, attr);
		initView();
	}
	public ColorPickerView(Context context, AttributeSet attr, int defstyle) {
		super(context, attr, defstyle);
		initView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_MOVE) {
			if (getParent() != null) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		}
		
		int color = Color.WHITE;
		int mCurX, mCurY;
		mCurX = (int)event.getX();
		mCurY = (int)event.getY();
		if (mCurX < 0) mCurX = 0;
		if (mCurY < 0) mCurY = 0;

		if (mSpectrum != null) { 
			if (mSpectrum.getWidth() <= mCurX) mCurX = mSpectrum.getWidth() - 1;
			if (mSpectrum.getHeight() <= mCurY) mCurY = mSpectrum.getHeight()- 1;
			
			color = mSpectrum.getPixel(mCurX, mCurY);
		}

		mCursorRect.set(mCurX-mDrawableW/2, mCurY-mDrawableH/2, mCurX + mDrawableW/2, mCurY+mDrawableH/2);
		if (mCursorRect.left < 0)
			mCursorRect.offset(-mCursorRect.left, 0);
		if (mCursorRect.right > getWidth())
			mCursorRect.offset(getWidth()-mCursorRect.right, 0);
		if (mCursorRect.top < 0)
			mCursorRect.offset(0, -mCursorRect.top);
		if (mCursorRect.bottom > getHeight())
			mCursorRect.offset(0, getHeight()-mCursorRect.bottom);
		
		mCursorDrawable.setBounds(mCursorRect);
		
		if (mColorChangeListener != null) {
			if ((color & 0xFFFFFF) == 0xFFFFFF)
				color = 0xFEFEFE;
			mColorChangeListener.onColorChanged((0xFE << 24) | (0xFFFFFF & color), mCurX, mCurY);
		}
		invalidate();
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mCursorDrawable.draw(canvas);
		
		canvas.drawRect(0, 0, getWidth(), getHeight(), mBorderPaint);
	}
	
	public void setOnColorChangeListener (onColorChangedListener listener) {
		mColorChangeListener = listener;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if (w != oldw || h != oldh) {
			if (mSpectrum != null) {
				mSpectrum.recycle();
				mSpectrum = null;
			}
			mSpectrum = makeSpectrum(w, h);
			setImageBitmap(mSpectrum);
		}
	}
	
	private Bitmap makeSpectrum(int w, int h){
		int colors[] = new int[] {
			0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF,
			0xFF0000FF, 0xFFFF00FF
		};
		
		float positions[] = new float[] {
			0, 0.2f, 0.4f, 0.6f, 0.8f, 1
		};
		
		float pos1[] = new float[] {
			0, 0.5f, 1
		};
		if(!(w > 0 && h > 0)){
			return null;
		}
		Bitmap spec = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(spec);
		Shader shaderA = new LinearGradient(0, 0, w, 0, colors, positions, TileMode.CLAMP);
		Paint mOvalHueSat = new Paint(Paint.ANTI_ALIAS_FLAG);
		mOvalHueSat.setShader(shaderA);
		mOvalHueSat.setStyle(Paint.Style.FILL);
		mOvalHueSat.setDither(true);
		c.drawRect(new Rect(0, 0, w, 1), mOvalHueSat);
		
		int drawnColors1[][] = new int[w][3];
		
		for(int i = 0; i < w; i++){
			drawnColors1[i][0] = 0xffffffff;
			drawnColors1[i][1] = spec.getPixel(i, 0);
			drawnColors1[i][2] = 0xff000000;
		}
		spec.recycle();
		
		Bitmap spectrum = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas last = new Canvas(spectrum);
		
		Shader shaderB1;
		Paint satu = new Paint(Paint.ANTI_ALIAS_FLAG);
		satu.setStyle(Paint.Style.FILL);
		satu.setDither(true);
		
		for(int i = 0; i < w; i++){
			shaderB1 = new LinearGradient(0, 0, 0, h, drawnColors1[i], pos1, TileMode.CLAMP);
			
			satu.setShader(shaderB1);
			last.drawLine(i, 0, i, h, satu);
		}
		return spectrum;
	}
	
	private void initView() {
		DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
		
		mCursorDrawable = getResources().getDrawable(R.drawable.penmemo_color_select_kit);
		mDrawableW = (int)((float)mCursorDrawable.getIntrinsicWidth() * (displayMetrics.density / 2.0f));
		mDrawableH = (int)((float)mCursorDrawable.getIntrinsicHeight() * (displayMetrics.density / 2.0f));
		mCursorRect = new Rect(0, 0, mDrawableW, mDrawableH);
		mCursorDrawable.setBounds(mCursorRect);
		
		mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBorderPaint.setColor(0xFF8C8C8C);
		mBorderPaint.setStrokeWidth(2);
		mBorderPaint.setStyle(Style.STROKE);
	}
	
	public interface onColorChangedListener {
        public void onColorChanged(int color, int x, int y);
    }
}
