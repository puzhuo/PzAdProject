package com.pzad.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;

import com.pzad.Constants;
import com.pzad.entities.Statistic;
import com.pzad.utils.StatUtil;

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
	
	public void setViewPager(final ViewPager viewPager){
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
					String name = null;
					int type;
					if(viewPager.getAdapter().getCount() == 4){
						type = Statistic.TYPE_FLOAT_WINDOW;
						switch(position){
						case 0:
							name = Statistic.WINDOW_NAME_NEWS;
							break;
						case 1:
							name = Statistic.WINDOW_NAME_RECOMMEND;
							break;
						case 2:
							name = Statistic.WINDOW_NAME_APP;
							break;
						case 3:
							name = Statistic.WINDOW_NAME_TOOL;
							break;
						}
			        }else{
			        	type = Statistic.TYPE_TUI_WINDOW;
			        	switch(position){
			        	case 0:
			        		name = Statistic.WINDOW_NAME_RECOMMEND;
			        		break;
			        	case 1:
			        		name = Statistic.WINDOW_NAME_APP;
			        		break;
			        	}
			        }
					
					if(name != null) StatUtil.sendStatistics(PzPagerIndicator.this.getContext(), new Statistic().setName(name, type).setExhibitionCount(1));
				}
				
			});
		}
	}
	
	@Override
	public void onDraw(Canvas canvas){
		canvas.drawRect(offset, 0, offset + widthPerPage, height, paint);
	}
}
