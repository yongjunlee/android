package com.mars.sketchOfSir;

import com.samsung.sdraw.example3.SPen_Example_PenSettingInfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class AppStart extends Activity {

	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
		
		//全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Boolean isFirstIn = false;
		SharedPreferences pref = this.getSharedPreferences("myActivityName", 0);
		//取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = pref.getBoolean("isFirstIn", true);
		if(!isFirstIn)
		{
			new Handler().postDelayed(new Runnable(){
				
				public void run(){
					Intent intent = new Intent (AppStart.this,SPen_Example_PenSettingInfoActivity.class);			
					startActivity(intent);			
					AppStart.this.finish();
				}
			}, 1000);
		}
		else
		 {
			new Handler().postDelayed(new Runnable(){
				
				public void run(){
					Intent intent = new Intent (AppStart.this,Viewpager.class);			
					startActivity(intent);			
					AppStart.this.finish();
				}
			}, 1000);
	 }
		Editor editor = pref.edit();
		editor.putBoolean("isFirstIn", false);
		editor.commit();	


	}
	

}
