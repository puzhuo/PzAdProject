package com.pzad.widget;

import com.pzad.Constants;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;

public class PzPagerIndicator extends View{
	
	private int width;
	private int height;
	
	private int widthPerPage;
	private float offset;
	private int count;
	
	private Paint paint;
	
	public PzPagerIndicator(Context context){
		this(context, null);
	}
	
	public PzPagerIndicator(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public PzPagerIndicator(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Constants.GLOBAL_HIGHLIGHT_COLOR);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		
		if(count != 0){
			widthPerPage = width / count;
		}
	}
	
	public void setViewPager(ViewPager viewPager){
		count = viewPager.getAdapter().getCount();
		if(width != 0){
			widthPerPage = width / count;
		}
		if(viewPager != null){
			viewPager.setOnPageChangeListener(new OnPageChangeListener(){

				@Override
				public void onPageScrollStateChanged(int state) {
				}

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					offset = (position * widthPerPage) + (positionOffset * widthPerPage);
					invalidate();
				}

				@Override
				public void onPageSelected(int position) {
				}
				
			});
		}
	}
	
	@Override
	public void onDraw(Canvas canvas){
		canvas.drawRect(offset, 0, offset + widthPerPage, height, paint);
	}
}
