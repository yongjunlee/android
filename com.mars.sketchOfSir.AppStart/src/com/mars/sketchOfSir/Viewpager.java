package com.mars.sketchOfSir;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.samsung.sdraw.example3.SPen_Example_PenSettingInfoActivity;

public class Viewpager extends Activity {
	
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;
	private ImageView mPageImg;
	private int currIndex = 0;
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView mPage3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpage);
		
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPagerTitleStrip = (PagerTitleStrip)findViewById(R.id.pagertitle);
        
        mPageImg = (ImageView) findViewById(R.id.page_now);    
        
        mPage0 = (ImageView)findViewById(R.id.page0);
        mPage1 = (ImageView)findViewById(R.id.page1);
        mPage2 = (ImageView)findViewById(R.id.page2);
        mPage3 = (ImageView)findViewById(R.id.page3);
        
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.view1, null);
        View view2 = mLi.inflate(R.layout.view2, null);
        View view3 = mLi.inflate(R.layout.view3, null);
        View view4 = mLi.inflate(R.layout.view4, null);
        
        //ÿ��ҳ���view����
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        
        final ArrayList<String> titles = new ArrayList<String>();
        titles.add("��");
        titles.add("��");
        titles.add("��");
        titles.add("��");
        
        
        //���ViewPager������������
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			@Override
			public CharSequence getPageTitle(int position) {
				return titles.get(position);
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mViewPager.setAdapter(mPagerAdapter);
    }
	
	public class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation = null;
			switch (arg0) {
			case 0:				
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				if (currIndex == arg0+1) {
					animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
				} /*else if (currIndex == 2) {
					animation = new TranslateAnimation(20*2, 0, 0, 0);
				}*/
				break;
			case 1:
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
				if (currIndex == arg0-1) {
					animation = new TranslateAnimation(20*(arg0-1), 20*arg0, 0, 0);	
					
				} else if (currIndex == arg0+1) {
					animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
				}
				break;
			case 2:
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
				if (currIndex == arg0-1) {
					animation = new TranslateAnimation(20*(arg0-1), 20*arg0, 0, 0);
				} else if (currIndex == arg0+1) {
					animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
				}
				break;
			case 3:
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
				if (currIndex == arg0-1) {
					animation = new TranslateAnimation(20*(arg0-1), 20*arg0, 0, 0);
					
				} else if (currIndex == arg0+1) {
					animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			mPageImg.startAnimation(animation);
		}
	}
	public void startbutton(View v) {  
      	Intent intent = new Intent();
		intent.setClass(Viewpager.this,SPen_Example_PenSettingInfoActivity.class);
		startActivity(intent);
		this.finish();
      }  
	
}
