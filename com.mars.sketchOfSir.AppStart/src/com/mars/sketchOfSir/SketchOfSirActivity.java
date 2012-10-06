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

public class SketchOfSirActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private SketchView_1 skView;
	private View nibView;
	private PopupWindow popupWindow ;
	public static int screenW, screenH;
	public Button B2_Btn;
	public Button B4_Btn;
	public Button B6_Btn;
	public Button BH_Btn;
	private String string;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	//ȫ����ʾ
    	//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    	//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
    	screenW = getWindowManager().getDefaultDisplay().getWidth();
		screenH = getWindowManager().getDefaultDisplay().getHeight();
      
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		skView = (SketchView_1)findViewById(R.id.skView);
        findViewById(R.id.penBtn).setOnClickListener(this);
        findViewById(R.id.eraserBtn).setOnClickListener(this);
        findViewById(R.id.clearBtn).setOnClickListener(this);
        findViewById(R.id.saveBtn).setOnClickListener(this);
        findViewById(R.id.renrenBtn).setOnClickListener(this);
        
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		nibView = layoutInflater.inflate(R.layout.nibchange, null);
		B2_Btn = (Button)nibView.findViewById(R.id.B2_Btn);
		B2_Btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				skView.NibChange(6);
				popupWindow.dismiss();
			}
			
		});
		B4_Btn = (Button)nibView.findViewById(R.id.B4_Btn);
		B4_Btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				skView.NibChange(8);
				popupWindow.dismiss();
			}
			
		});
		B6_Btn = (Button)nibView.findViewById(R.id.B6_Btn);
		B6_Btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				skView.NibChange(10);
				popupWindow.dismiss();
				
			}
			
		});
		BH_Btn = (Button)nibView.findViewById(R.id.BH_Btn);
		BH_Btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				skView.NibChange(11);
				popupWindow.dismiss();
			}
			
		});
		
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
		// TODO Auto-generated method stub			System.out.println("OK___!!");
		switch (v.getId()) {
		
		    case R.id.penBtn :
		    	showWindow(v);
		    	
		    	skView.ClrChange(Color.BLACK);
		    	skView.NibChange(5);
		    	break;

		    case R.id.eraserBtn :
		    	skView.ClrChange(Color.WHITE);
		    	skView.NibChange(10);
		    	break;
		    	
		    case R.id.renrenBtn :
		    	Toast.makeText(this, "renren share", Toast.LENGTH_LONG).show();

		    	Intent intent=new Intent(Intent.ACTION_SEND); 
		    	//intent.setType("text/plain"); //���ı�
		    	intent.setType("image/png");
                //File f = new File(Environment.getExternalStorageDirectory()
                //		+"/DCIM/Camera/123.jpg");
		    	     File f = new File(string);
		    	Uri u = Uri.fromFile(f);
		    	intent.putExtra(Intent.EXTRA_STREAM, u);
		    	intent.putExtra(Intent.EXTRA_SUBJECT, "����"); 
		    	intent.putExtra(Intent.EXTRA_TEXT, "I would like to share this with you..."); 
		    	startActivity(Intent.createChooser(intent, getTitle())); 
		    	Toast.makeText(this, "renren share", Toast.LENGTH_LONG).show();	
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
					string = path.getPath() + "/" + name;
					skView.saveToFile(string);
					Toast.makeText(this, "����ɹ���\n�ļ������ڣ�" + string, Toast.LENGTH_LONG).show();
				} catch (FileNotFoundException e) {
					Toast.makeText(this, "����ʧ�ܣ�\n" + e, Toast.LENGTH_LONG).show();
				}
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
		
		
		
		//if(popupWindow != null) 
		//{
		//	popupWindow.dismiss();
		//}

	}


    
}