package com.mars.sketchOfSir;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mars.sketchView.SketchView_1;
import com.samsung.samm.common.SObjectStroke;
import com.samsung.spen.settings.SettingStrokeInfo;
import com.samsung.spensdk.SCanvasView;

public class SketchOfSirActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private SCanvasView skView;
	private SettingStrokeInfo strokeInfo = new SettingStrokeInfo();
	private View nibView;
	private PopupWindow popupWindow ;
	public static int screenW, screenH;
	public Button B2_Btn;
	public Button B4_Btn;
	public Button B6_Btn;
	public Button BH_Btn;
	private String string;
	
    
    public void onCreate(Bundle savedInstanceState) {
    	
    	
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    	                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
    	screenW = getWindowManager().getDefaultDisplay().getWidth();
		screenH = getWindowManager().getDefaultDisplay().getHeight();
      
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        skView = (SCanvasView)findViewById(R.id.skView);
		
		strokeInfo.setStrokeWidth(6);
		strokeInfo.setStrokeStyle(SObjectStroke.SAMM_STROKE_STYLE_CRAYON);
		skView.setSettingViewStrokeInfo(strokeInfo);

		skView = (SCanvasView)findViewById(R.id.skView);
        findViewById(R.id.penBtn).setOnClickListener(this);
        findViewById(R.id.eraserBtn).setOnClickListener(this);
        findViewById(R.id.undoBtn).setOnClickListener(this);
        findViewById(R.id.redoBtn).setOnClickListener(this);
        findViewById(R.id.clearBtn).setOnClickListener(this);
        findViewById(R.id.renrenBtn).setOnClickListener(this);
        findViewById(R.id.saveBtn).setOnClickListener(this);
        
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		nibView = layoutInflater.inflate(R.layout.nibchange, null);
		
		strokeInfo.setStrokeWidth(6);
		strokeInfo.setStrokeStyle(SObjectStroke.SAMM_STROKE_STYLE_CRAYON);
		skView.setSettingViewStrokeInfo(strokeInfo);
		skView.setBackgroundColor(Color.WHITE);
		B2_Btn = (Button)nibView.findViewById(R.id.B2_Btn);
		B2_Btn.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strokeInfo.setStrokeWidth(6);
				strokeInfo.setStrokeStyle(SObjectStroke.SAMM_STROKE_STYLE_CRAYON);
				skView.setSettingViewStrokeInfo(strokeInfo);
				popupWindow.dismiss();
			}
			
		});
		B4_Btn = (Button)nibView.findViewById(R.id.B4_Btn);
		B4_Btn.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strokeInfo.setStrokeWidth(8);
				strokeInfo.setStrokeStyle(SObjectStroke.SAMM_STROKE_STYLE_CRAYON);
				skView.setSettingViewStrokeInfo(strokeInfo);
				popupWindow.dismiss();
			}
			
		});
		B6_Btn = (Button)nibView.findViewById(R.id.B6_Btn);
		B6_Btn.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strokeInfo.setStrokeWidth(10);
				strokeInfo.setStrokeStyle(SObjectStroke.SAMM_STROKE_STYLE_CRAYON);
				skView.setSettingViewStrokeInfo(strokeInfo);
				popupWindow.dismiss();
				
			}
			
		});
		BH_Btn = (Button)nibView.findViewById(R.id.BH_Btn);
		BH_Btn.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strokeInfo.setStrokeWidth(11);
				strokeInfo.setStrokeStyle(SObjectStroke.SAMM_STROKE_STYLE_CRAYON);
				skView.setSettingViewStrokeInfo(strokeInfo);
				popupWindow.dismiss();
			}
			
		});
		
    }

    
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.mi_exit :
				finish();
				System.exit(0);
				break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	
	public void onClick(View v) {
		// TODO Auto-generated method stub			System.out.println("OK___!!");
		switch (v.getId()) {
		
		    case R.id.penBtn :
		    	showWindow(v);
		    
		    	break;

		    case R.id.eraserBtn :
		    	strokeInfo.setStrokeWidth(20);
				strokeInfo.setStrokeStyle(SObjectStroke.SAMM_STROKE_STYLE_ERASER);
				skView.setSettingViewStrokeInfo(strokeInfo);
		    	break;
		    	
		    case R.id.undoBtn :
		    	skView.undo();
		    	break;
		    	
		    case R.id.redoBtn :
		    	skView.redo();
		    	break;
		    	
		    case R.id.renrenBtn :
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
				string = path.getPath() + "/" + name;
				skView.saveSAMMFile(string);
				
		    	Intent intent=new Intent(Intent.ACTION_SEND); 
		    	intent.setType("image/png");
		    	File f = new File(string);
		    	Uri u = Uri.fromFile(f);
		    	intent.putExtra(Intent.EXTRA_STREAM, u);
		    	intent.putExtra(Intent.EXTRA_SUBJECT, "����"); 
		    	intent.putExtra(Intent.EXTRA_TEXT, "I would like to share this with you..."); 
		    	startActivity(Intent.createChooser(intent, getTitle())); 
		    	break;
		
			case R.id.clearBtn :
				skView.clearSCanvasView();
				break;

			case R.id.saveBtn : {
				String sdStates = Environment.getExternalStorageState(); // �ж�sd���Ƿ����

				// ���SD���Ƿ����
				if (!sdStates.equals(android.os.Environment.MEDIA_MOUNTED)) {
					Toast.makeText(this, "SD��δ׼���ã�", Toast.LENGTH_SHORT).show();
					break;
				}

				//��ȡϵͳͼƬ�洢·��
				File paths = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				// Make sure the Pictures directory exists.
				paths.mkdirs();
				
				//���ݵ�ǰʱ������ͼƬ����
				Calendar cs = Calendar.getInstance();
				String names = "" 
						+ cs.get(Calendar.YEAR) + cs.get(Calendar.MONTH) + cs.get(Calendar.DAY_OF_MONTH) 
						+ cs.get(Calendar.HOUR_OF_DAY) + cs.get(Calendar.MINUTE) + cs.get(Calendar.SECOND)
						 + ".png";
				
				//�ϳ�����·����ע�� / �ָ���
				string = paths.getPath() + "/" + names;
				if(skView.saveSAMMFile(string))
				Toast.makeText(this, "����ɹ���\n�ļ������ڣ�" + string, Toast.LENGTH_LONG).show();		
				break; 
			}
		}
	}

	private void showWindow(View parent) {
		// TODO Auto-generated method stub
		popupWindow = new PopupWindow(nibView, 480, 150);
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// ��ʾ��λ��Ϊ:��Ļ�Ŀ�ȵ�һ��-PopupWindow�ĸ߶ȵ�һ��
		popupWindow.showAsDropDown(parent, 0, 0);


	}


    
}