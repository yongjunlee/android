package com.samsung.sdraw.example3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PalleteScrollView extends ScrollView {
	
	private Context mContext;
	public boolean touchInThumb = false;
	
	private int MATRIX_MAX_Y = 193;
	private float GAP_PER_SCROLL = 3.73f;
	private int THUMB_BASIC_TOP_MARGIN = 65;
	
	public PalleteScrollView(Context context) {
		super(context);
		mContext = context;
	}
	
	public PalleteScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public PalleteScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(!touchInThumb){
			Matrix m = canvas.getMatrix();
			float[] value = new float[9];
			m.getValues(value);
			int topMargin = THUMB_BASIC_TOP_MARGIN - (int)((value[5] - MATRIX_MAX_Y) * GAP_PER_SCROLL); 
//			((EditActivity)mContext).setScrollThumbY(topMargin);
		}
	}
}
