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
					String sdState = Environment.getExternalStorageState(); // �ж�sd���Ƿ����

					// ���SD���Ƿ����
					if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
						Toast.makeText(this, "SD��δ׼���ã�", Toast.LENGTH_SHORT).show();
						break;
					}

					//��ȡϵͳͼƬ�洢·��
					File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
					// Make sure the Pictures directory exists.
					path.mkdirs();
					
					//���ݵ�ǰʱ������ͼƬ����
					Calendar c = Calendar.getInstance();
					String name = "" 
							+ c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH) 
							+ c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND)
							 + ".png";
					
					//�ϳ�����·����ע�� / �ָ���
					String string = path.getPath() + "/" + name;
					skView.saveToFile(string);
					Toast.makeText(this, "����ɹ���\n�ļ������ڣ�" + string, Toast.LENGTH_LONG).show();
				} catch (FileNotFoundException e) {
					Toast.makeText(this, "����ʧ�ܣ�\n" + e, Toast.LENGTH_LONG).show();
				}
				break;
			}
		}
	}


    
}