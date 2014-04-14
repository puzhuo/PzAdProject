package com.pzad.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.util.AttributeSet;
import android.view.View;

public class SeparateView extends View{
	
	private int width;
	private int height;

	private Path path;
	private Paint paint;
	
	private int color;
	
	public SeparateView(Context context, int color){
		this(context);
		this.color = color;
	}
	
	public SeparateView(Context context){
		this(context, null);
	}
	
	public SeparateView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public SeparateView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		path = new Path();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		
		if(height < 1){
			height = 1;
			super.setMeasuredDimension(width, height);
		}
		
		path.reset();
		path.moveTo(getPaddingLeft(), height / 2);
		path.lineTo(width - getPaddingRight(), height / 2);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		paint.setColor(color);
		canvas.drawPath(path, paint);
	}
}
