package com.pzad.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class PzStarView extends View{
	
	private int width;
	private int height;
	
	private int radius;
	private float radiusRate = 0.95F;
	
	private Paint paint;
	
	private int centerX;
	private int centerY;
	
	private float rate;
	
	private Path starOutlet;
	private Path halfStarOutlet;
	
	public PzStarView(Context context){
		this(context, null);
	}
	
	public PzStarView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public PzStarView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xFFFF9900);
		starOutlet = new Path();
		halfStarOutlet = new Path();
	}
	
	public void setRate(float rate){
		this.rate = rate;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		
		radius = width > height ? height : width;
		radius /= 2;
		radius *= radiusRate;
		
		centerX = width / 2;
		centerY = height / 2;
		
		starOutlet.reset();
		halfStarOutlet.reset();
		
		float startP = (float) (-Math.PI / 2);
		float perP = (float) ((Math.PI * 2) / 10);
		
		float[] correctPoint = centerRadiusPoint(centerX, centerY, startP, radius);
		starOutlet.moveTo(correctPoint[0], correctPoint[1]);
		halfStarOutlet.moveTo(correctPoint[0], correctPoint[1]);
		for(int i = 1; i < 10; i++){
			if(i % 2 == 0){
				correctPoint = centerRadiusPoint(centerX, centerY, startP + (i * perP), radius);
			}else{
				correctPoint = centerRadiusPoint(centerX, centerY, startP + (i * perP), radius / 1.8F);
			}
			
			starOutlet.lineTo(correctPoint[0], correctPoint[1]);
			if(i >= 5) halfStarOutlet.lineTo(correctPoint[0], correctPoint[1]);
		}
		
		starOutlet.close();
		halfStarOutlet.close();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		if(starOutlet != null){
			paint.setColor(0xFFCCCCCC);
			canvas.drawPath(starOutlet, paint);
			
			paint.setColor(0xFFFF9900);
			if(rate > 0.3F && rate < 0.6F){
				canvas.drawPath(halfStarOutlet, paint);
			}else if(rate >= 0.6F){
				canvas.drawPath(starOutlet, paint);
			}
		}
	}
	
	private float[] centerRadiusPoint(int centerX, int centerY, double angle, double radius){
		float x = (float) (radius * Math.cos(angle) + centerX);
	    float y = (float) (radius * Math.sin(angle) + centerY);
		
		return new float[]{x, y};
	}
}
