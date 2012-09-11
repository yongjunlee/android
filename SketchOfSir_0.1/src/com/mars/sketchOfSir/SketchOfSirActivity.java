package com.mars.sketchOfSir;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mars.sketchView.SketchView;

public class SketchOfSirActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private SketchView skView;
	public static int screenW, screenH;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	screenW = getWindowManager().getDefaultDisplay().getWidth();
		screenH = getWindowManager().getDefaultDisplay().getHeight();
      
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		skView = (SketchView)findViewById(R.id.skView);
        findViewById(R.id.penBtn).setOnClickListener(this);
        findViewById(R.id.eraserBtn).setOnClickListener(this);
        findViewById(R.id.clearBtn).setOnClickListener(this);
        findViewById(R.id.saveBtn).setOnClickListener(this);
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.mi_exit :
				finish();
				System.exit(0);
				break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		    case R.id.penBtn :
		    	skView.clr_fg = Color.BLACK;
		    	break;
		    	
		    case R.id.eraserBtn :
		    	skView.clr_fg = Color.WHITE;
		    	break;
		
			case R.id.clearBtn :
				skView.clear();
				break;

			case R.id.saveBtn : {
				try {
					String sdState = Environment.getExternalStorageState(); // 判断sd卡是否存在

					// 检查SD卡是否可用
					if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
						Toast.makeText(this, "SD卡未准备好！", Toast.LENGTH_SHORT).show();
						break;
					}

					//获取系统图片存储路径
					File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
					// Make sure the Pictures directory exists.
					path.mkdirs();
					
					//根据当前时间生成图片名称
					Calendar c = Calendar.getInstance();
					String name = "" 
							+ c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH) 
							+ c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND)
							 + ".png";
					
					//合成完整路径，注意 / 分隔符
					String string = path.getPath() + "/" + name;
					skView.saveToFile(string);
					Toast.makeText(this, "保存成功！\n文件保存在：" + string, Toast.LENGTH_LONG).show();
				} catch (FileNotFoundException e) {
					Toast.makeText(this, "保存失败！\n" + e, Toast.LENGTH_LONG).show();
				}
				break;
			}
		}
	}


    
}