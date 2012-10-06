package com.samsung.sdraw.example3;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mars.sketchOfSir.R;

class ColorSelectView extends View{
	private static final int COLOR_ROW_NUM = 2;
	private static final int COLOR_COLUMN_NUM = 8;
	private static final int COLOR_NUM_MAX = COLOR_COLUMN_NUM * COLOR_ROW_NUM;
	private static final int CUSTOM_COLOR_IDX = COLOR_NUM_MAX - 1;
	private static final int WINDOW_BORDER_WIDTH = 0;
	private static final int ITEM_BORDER_WIDTH = 1;
	private static final int ITEM_GAPX = 2;
	private static final int ITEM_GAPY = 3;
	private static final int USER_COLOR = 0xFEFFFFFF;
	
	private int		mSeletedItem = 0;
	private int 	mColorSet[] = {
			Color.rgb(0xFF, 0xFF, 0xFF), Color.rgb(0xFD, 0xFF, 0x2D), Color.rgb(0xFF, 0x83, 0x5D), Color.rgb(0xFF, 0x3B, 0x5B), 
			Color.rgb(0xFF, 0x49, 0xC9), Color.rgb(0xCA, 0x85, 0xFF), Color.rgb(0x38, 0xA8, 0xFF), Color.rgb(0x33, 0x67, 0xFD),
			Color.rgb(0x16, 0xCC, 0x79), Color.rgb(0x01, 0x94, 0x2E), Color.rgb(0x04, 0x67, 0x2E), Color.rgb(0xA6, 0xA5, 0xA5),
			Color.rgb(0x73, 0x72, 0x72), Color.rgb(0x30, 0x30, 0x30), DEFAULT_COLOR, USER_COLOR,
	};
	protected static final int DEFAULT_COLOR = Color.rgb(0x13, 0x13, 0x13);
	private Rect 	mCanvasRect;
	private Rect	mItemRect, mSelectRect;
	
	private Paint	mBorderPaint;
	private Paint	mColorPaint;
	
	private OnColorChangedListener colorChangeListener = null;
	private Drawable mDrSeleteBox = getResources().getDrawable(R.drawable.pen_color_focus);
    private Drawable mDrColorShadow = getResources().getDrawable(R.drawable.pen_color_shadow);
    private Drawable mDrUserColor = getResources().getDrawable(R.drawable.penmemo_color_8);
	
	public ColorSelectView(Context context) {
		super(context);
		initView();
	}
	
	public ColorSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private void initView() {
		mBorderPaint = new Paint();
		mBorderPaint.setColor(Color.BLACK);
		mBorderPaint.setStrokeWidth(WINDOW_BORDER_WIDTH);
		mBorderPaint.setStyle(Paint.Style.STROKE);
		
		mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	public void setInitialValue(OnColorChangedListener listener, int color) {
		colorChangeListener = listener;
		setSelectBoxPos(color);
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int paddingL = getPaddingLeft();
		int paddingR = getPaddingRight();
		int paddingT = getPaddingTop();
		int paddingB = getPaddingBottom();

		mCanvasRect = new Rect(paddingL, paddingT, w-paddingR, h-paddingB);
		
		int itemW = (int)((mCanvasRect.width()-WINDOW_BORDER_WIDTH*2)/(float)COLOR_COLUMN_NUM) - 1;
		mItemRect = new Rect(paddingL, paddingT, paddingL + itemW, paddingT + itemW);
		
		mSelectRect = new Rect(mItemRect.left-ITEM_BORDER_WIDTH, mItemRect.top-ITEM_BORDER_WIDTH, 
				mItemRect.right+ITEM_BORDER_WIDTH, mItemRect.bottom + ITEM_BORDER_WIDTH);

		mDrColorShadow.setBounds(mItemRect);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP: {
			if(mSeletedItem - COLOR_COLUMN_NUM >= 0 && mSeletedItem - COLOR_COLUMN_NUM < COLOR_NUM_MAX){
				mSeletedItem  = mSeletedItem - COLOR_COLUMN_NUM;
				invalidate();
				return true;
			}
		} break;
		case KeyEvent.KEYCODE_DPAD_DOWN: {
			if(mSeletedItem + COLOR_COLUMN_NUM >= 0 && mSeletedItem + COLOR_COLUMN_NUM < COLOR_NUM_MAX){
				mSeletedItem  = mSeletedItem + COLOR_COLUMN_NUM;
				invalidate();
				return true;
			}
		} break;
		case KeyEvent.KEYCODE_DPAD_LEFT: {
			if(mSeletedItem - 1 >= 0 && mSeletedItem - 1 < COLOR_NUM_MAX){
				mSeletedItem  = mSeletedItem - 1;
				invalidate();
				return true;
			}
		} break;
		case KeyEvent.KEYCODE_DPAD_RIGHT: {
			if(mSeletedItem + 1 >= 0 && mSeletedItem + 1 < COLOR_NUM_MAX){
				mSeletedItem  = mSeletedItem + 1;
				invalidate();
				return true;
			}
		} break;
		case KeyEvent.KEYCODE_ENTER: {
			if (colorChangeListener != null)
				colorChangeListener.colorChanged(mColorSet[mSeletedItem]);
			return true;
		}
		default:
			break;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(getPaddingLeft() + WINDOW_BORDER_WIDTH, getPaddingTop() + WINDOW_BORDER_WIDTH);
		
		ViewGroup.LayoutParams params = getLayoutParams();
		int w = (getWidth()-WINDOW_BORDER_WIDTH*2)/COLOR_COLUMN_NUM;
		float rowNumF = (float)mColorSet.length/(float)COLOR_COLUMN_NUM;
		int rowNum = mColorSet.length/COLOR_COLUMN_NUM;
		if(rowNumF > rowNum){
			rowNum++;
		}
		params.height = w * rowNum;
		setLayoutParams(params);
		
		onDrawColorSet(canvas);
		drawSeleteBox(canvas);
		canvas.restore();
	}

	@SuppressWarnings("unused")
	private void onDrawColorSet(Canvas canvas){
		if (mCanvasRect != null && WINDOW_BORDER_WIDTH > 0)
			canvas.drawRect(mCanvasRect, mBorderPaint);
		
		if (mCanvasRect != null) {
			Rect dr = new Rect(mItemRect);
			int color;
			for (int i=0; i<COLOR_ROW_NUM; i++) {
				for (int j=0; j<COLOR_COLUMN_NUM; j++) {
					color = mColorSet[i*COLOR_COLUMN_NUM + j];
					mColorPaint.setColor(color);
					if ( color == USER_COLOR) {
						mDrUserColor.setBounds(dr);
						mDrUserColor.draw(canvas);
					} else {
						canvas.drawRect(dr, mColorPaint);
						mDrColorShadow.setBounds(dr);
						mDrColorShadow.draw(canvas);
					}
					dr.offset(dr.width() + ITEM_GAPX, 0);
				}
				dr.offset(-dr.left+getPaddingLeft(), dr.height() + ITEM_GAPY);
			}
			
		}
	}
	
	private void drawSeleteBox(Canvas canvas) {
		int x =  mSeletedItem%COLOR_COLUMN_NUM;
		int y =  mSeletedItem/COLOR_COLUMN_NUM;
		
		mSelectRect.set(mItemRect);
		mSelectRect.offset((mSelectRect.width() + ITEM_GAPX)*x, 
				(mSelectRect.height() + ITEM_GAPY)*y);
		
		mSelectRect.left -= ITEM_BORDER_WIDTH;
		mSelectRect.top -= ITEM_BORDER_WIDTH;
		mSelectRect.right += ITEM_BORDER_WIDTH;
		mSelectRect.bottom += ITEM_BORDER_WIDTH;
		
		mDrSeleteBox.setBounds(mSelectRect);
		mDrSeleteBox.draw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int colorIdx = mSeletedItem;
		int action = event.getAction();
		if (action == MotionEvent.ACTION_MOVE) {
			if (getParent() != null) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		}
		
		switch(event.getAction()) {
			case MotionEvent.ACTION_UP:
			break;
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				colorIdx = getColorIdx(event.getX(), event.getY());
				break;
			default:
				break;
    	}
		if (colorIdx == CUSTOM_COLOR_IDX) {
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				mSeletedItem = colorIdx;
		} else {
			mSeletedItem = colorIdx;
		}
				

		if (colorChangeListener != null)
			colorChangeListener.colorChanged(mColorSet[mSeletedItem]);
		invalidate();

		return true;
	}
	
	private int getColorIdx(float x, float y){
		int i, j;
		for (i=1; i<=COLOR_COLUMN_NUM; i++) {
			if (x < i * mItemRect.width())
				break;
		}
		for (j=1; j<=COLOR_ROW_NUM; j++) {
			if (y < j * mItemRect.height())
				break;
		}

		if (i > COLOR_COLUMN_NUM) i = COLOR_COLUMN_NUM;
		if (j > COLOR_ROW_NUM) j = COLOR_ROW_NUM;

		return (j-1) * COLOR_COLUMN_NUM + (i-1);
	}
	
	private void setSelectBoxPos(int color){
//		if((0xFF000000 | color) == PenSettingPreView.COLOR_EMBOSS_BLACK)
//			color = Color.BLACK;
		for(int i = 0; i < mColorSet.length; i++) {
			if(color == mColorSet[i]) {
				mSeletedItem = i;
				break;
			}
		}
		invalidate();
	}
	
	public void setColor(int color) {
		if ((color & 0xFF000000) != 0xFE000000) {
//			if(color == PenSettingPreView.COLOR_EMBOSS_BLACK)
//				color = Color.BLACK;
			for(int i = 0; i < mColorSet.length; i++) {
				if(color == mColorSet[i]) {
					mSeletedItem = i;
					break;
				}
			}
		} else { // User color.. 
			mSeletedItem = CUSTOM_COLOR_IDX;
			mColorSet[CUSTOM_COLOR_IDX] = color;
		}
		
		invalidate();
	}
	
	public int getColor(){
		return mColorSet[mSeletedItem];
	}
	
	public void setColorSet(int[] colorSet){
		mColorSet = colorSet;
	}

	public interface OnColorChangedListener {
        public void colorChanged(int color);
    }
	
	public int getColorSetSize(){
	    return mColorSet.length;
	}
	
	public boolean isUserColor(){
	    if(mColorSet[mColorSet.length-1] == USER_COLOR){
	        return true;
	    }
	    else{
	        return false;
	    }
	}
}