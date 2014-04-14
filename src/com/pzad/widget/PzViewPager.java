package com.pzad.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.pzad.Constants;

public class PzViewPager extends ViewPager{
	
	private Paint paint;
	private GradientDrawable fadeOutDrawable;
	private GradientDrawable fadeInDrawable;
	
	private int width;
	private int height;
	
	public PzViewPager(Context context){
		this(context, null);
	}
	
	public PzViewPager(Context context, AttributeSet attrs){
		super(context, attrs);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xFFFF0000);
		
		fadeOutDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Constants.GLOBAL_SHADOW_COLOR, 0});
		fadeOutDrawable.setCornerRadius(50);
		
		fadeInDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0, Constants.GLOBAL_SHADOW_COLOR});
		fadeInDrawable.setCornerRadius(50);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		
		fadeOutDrawable.setBounds(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingTop() + (int) (height * 0.2F));
		fadeInDrawable.setBounds(getPaddingLeft(), (int) (height - getPaddingBottom() - (height * 0.2F)), getPaddingRight(), height - getPaddingBottom());
	}
	
	@Override
	public void dispatchDraw(Canvas canvas){
		super.dispatchDraw(canvas);
		
		fadeOutDrawable.draw(canvas);
		fadeInDrawable.draw(canvas);
		
		canvas.drawLine(0, 0, width, height, paint);
	}

}
