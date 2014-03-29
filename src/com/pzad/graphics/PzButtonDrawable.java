package com.pzad.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class PzButtonDrawable extends Drawable {
	
	private int top;
	private int bottom;
	private int left;
	private int right;
	
	private int width;
	private int height;
	
	private int centerX;
	private int centerY;
	
	private int radius;
	private float radiusRate = 0.95F;
	
	private Paint paint;
	
	private int backgroundColor;
	
	public PzButtonDrawable(int backgroundColor){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.backgroundColor = backgroundColor;
	}
	
	@Override
	public void setBounds(int left, int top, int right, int bottom){
		super.setBounds(left, top, right, bottom);
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		width = right - left;
		height = bottom - top;
		
		centerX = width / 2;
		centerY = height / 2;
		
		radius = width > height ? height : width;
		radius /= 2;
		radius *= radiusRate;
	}

	@Override
	public void draw(Canvas canvas) {
		paint.setColor(backgroundColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(radius * 0.1F);
		int halfLength = (int) (radius * 0.5F);
		canvas.drawLine(centerX + halfLength, centerY - halfLength, centerX - halfLength, centerY + halfLength, paint);
		canvas.drawLine(centerX - halfLength, centerY - halfLength, centerX + halfLength, centerY + halfLength, paint);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSPARENT;
	}

	@Override
	public void setAlpha(int alpha) {}

	@Override
	public void setColorFilter(ColorFilter cf) {}

}
