package com.pzad.category.floatads;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class FloatDetailView extends RelativeLayout{
	
	private int width;
	private int height;
	
	private RectF rect;
	private Paint paint;
	
	private WindowManager.LayoutParams layoutParams;
	
	public FloatDetailView(Context context){
		this(context, null);
	}
	
	public FloatDetailView(Context context, AttributeSet attrs){
		super(context, attrs);
		
		rect = new RectF();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xFFFFFFFF);
		paint.setShadowLayer(7, 5, 5, 0xFF000000);
	}
	
	public void setParams(WindowManager.LayoutParams layoutParams){
		this.layoutParams = layoutParams;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		
		rect.top = 10;
		rect.left = 10;
		rect.right = width - 10;
		rect.bottom = height - 10;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		canvas.drawRoundRect(rect, 40, 40, paint);
		
	}
}
