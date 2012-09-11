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
	private Bitmap TemBitmap;        //保存每次画笔过后的临时图像
	private Bitmap ImBitmap;         //保存处理后的图像
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;// 画布的画笔
	private Paint mPaint;// 真实的画笔
	private float mX, mY;//临时点坐标
	private static final float TOUCH_TOLERANCE = 0;
	public int clr_bg, clr_fg;

	
	public SketchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		screenWidth = 800;
		screenHeight = 1280;
		
		clr_bg = Color.WHITE;
		clr_fg = Color.BLACK;
		
		//保存每次绘画出来的图形
		TemBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(TemBitmap);
		
		//对画笔进行设置
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
	    mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
		mPaint.setStrokeCap(Paint.Cap.SQUARE);// 形状
		mPaint.setStrokeWidth(5);// 画笔宽度
		mPaint.setColor(clr_fg);

		mCanvas.drawColor(clr_bg);
	}

	public SketchView(Context context) {
		super(context);
	}
	
	@Override
	 public void onDraw(Canvas canvas) {
	  canvas.drawColor(clr_bg);
	  // 将前面已经画过得显示出来
	  mPaint.setColor(clr_fg);
	  canvas.drawBitmap(TemBitmap, 0, 0, mBitmapPaint);
	  if (mPath != null) {
	   // 实时的显示
	   canvas.drawPath(mPath, mPaint);
	  }
	 }

	
	//清空画布
	public void clear() {
		mPath.reset();
		mCanvas.drawColor(clr_bg);
		invalidate();
	}
	
	//保存绘画到图片
	public void saveToFile(String filename) throws FileNotFoundException {
		File f = new File(filename);
		if(f.exists())
			throw new RuntimeException("文件：" + filename + " 已存在！");
			
		FileOutputStream fos = new FileOutputStream(new File(filename));
		//将 bitmap 压缩成其他格式的图片数据
		TemBitmap.compress(CompressFormat.PNG, 50, fos);
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//                                  下面部分是画线算法，以后要修改的部分
	
	private void touch_start(float x, float y) {
	  mPath.moveTo(x, y);
	  mX = x;
	  mY = y;
	 }

	 private void touch_move(float x, float y) {
	  float dx = Math.abs(x - mX);
	  float dy = Math.abs(mY - y);

	  //触摸间隔大于阈值才绘制路径
	  if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
	   // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
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
	   // 每次down下去重新new一个Path
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

