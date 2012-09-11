package com.mars.sketchView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.CompressFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SketchView extends View {
	
	
	private int screenWidth, screenHeight;
	private int vige = 150;
	private Bitmap TemBitmap;        //����ÿ�λ��ʹ������ʱͼ��
	private Bitmap ImBitmap;         //���洦����ͼ��
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;// �����Ļ���
	private Paint mPaint;// ��ʵ�Ļ���
	private float mX, mY;//��ʱ������
	private static final float TOUCH_TOLERANCE = 0;
	public int clr_bg, clr_fg;

	
	public SketchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		screenWidth = 800;
		screenHeight = 1280;
		
		clr_bg = Color.WHITE;
		clr_fg = Color.BLACK;
		
		//����ÿ�λ滭������ͼ��
		TemBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(TemBitmap);
		
		//�Ի��ʽ�������
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
	    mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// �������Ե
		mPaint.setStrokeCap(Paint.Cap.SQUARE);// ��״
		mPaint.setStrokeWidth(5);// ���ʿ��
		mPaint.setColor(clr_fg);

		mCanvas.drawColor(clr_bg);
	}

	public SketchView(Context context) {
		super(context);
	}
	
	@Override
	 public void onDraw(Canvas canvas) {
	  canvas.drawColor(clr_bg);
	  // ��ǰ���Ѿ���������ʾ����
	  mPaint.setColor(clr_fg);
	  canvas.drawBitmap(TemBitmap, 0, 0, mBitmapPaint);
	  if (mPath != null) {
	   // ʵʱ����ʾ
	   canvas.drawPath(mPath, mPaint);
	  }
	 }

	
	//��ջ���
	public void clear() {
		mPath.reset();
		mCanvas.drawColor(clr_bg);
		invalidate();
	}
	
	//����滭��ͼƬ
	public void saveToFile(String filename) throws FileNotFoundException {
		File f = new File(filename);
		if(f.exists())
			throw new RuntimeException("�ļ���" + filename + " �Ѵ��ڣ�");
			
		FileOutputStream fos = new FileOutputStream(new File(filename));
		//�� bitmap ѹ����������ʽ��ͼƬ����
		TemBitmap.compress(CompressFormat.PNG, 50, fos);
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//                                  ���沿���ǻ����㷨���Ժ�Ҫ�޸ĵĲ���
	
	private void touch_start(float x, float y) {
	  mPath.moveTo(x, y);
	  mX = x;
	  mY = y;
	 }

	 private void touch_move(float x, float y) {
	  float dx = Math.abs(x - mX);
	  float dy = Math.abs(mY - y);

	  //�������������ֵ�Ż���·��
	  if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
	   // ��x1,y1��x2,y2��һ�����������ߣ���ƽ��(ֱ����mPath.lineToҲ�ǿ��Ե�)
	   mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
	   mX = x;
	   mY = y;
	  }
	  //touch_up();
	 }

	 private void touch_up() {
	  mPath.lineTo(mX, mY);
	  mPaint.setColor(clr_fg);
	  mCanvas.drawPath(mPath, mPaint);
	  }

	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
	  float x = event.getX();
	  float y = event.getY();

	  switch (event.getAction()) {
	  case MotionEvent.ACTION_DOWN:
	   // ÿ��down��ȥ����newһ��Path
	   mPath = new Path();

	   touch_start(x, y);
	   invalidate();
	   break;
	  case MotionEvent.ACTION_MOVE:
	   touch_move(x, y);
	   invalidate();
	   break;
	  case MotionEvent.ACTION_UP:
	   touch_up();
	   invalidate();
	   break;
	  }
	  return true;
	 }

}

