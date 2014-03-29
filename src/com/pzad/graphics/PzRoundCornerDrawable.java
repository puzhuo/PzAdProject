package com.pzad.graphics;

import com.pzad.Constants;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class PzRoundCornerDrawable extends Drawable {
	
	private int left;
	private int top;
	private int right;
	private int bottom;
	
	private int width;
	private int height;
	
	private int centerX;
	private int centerY;
	
	private RectF bound;
	
	private int corner;
	private int backgroundColor;
	private boolean drawShadow;
	
	private Paint paint;
	
	public PzRoundCornerDrawable(int corner, int strokeColor, int strokeWidth){
		this(corner, strokeColor, false);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
	}
	
	public PzRoundCornerDrawable(int corner, int backgroundColor, int shadowRadius, int shadowX, int shadowY, int shadowColor){
		this(corner, backgroundColor, true);
		paint.setShadowLayer(shadowRadius, shadowX, shadowY, shadowColor);
	}
	
	public PzRoundCornerDrawable(int corner, int backgroundColor, boolean drawShadow){
		this.corner = corner;
		this.backgroundColor = backgroundColor;
		this.drawShadow = drawShadow;
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(backgroundColor);
		if(drawShadow) paint.setShadowLayer(7, 0, 4, Constants.GLOBAL_SHADOW_COLOR);
	}
	
	@Override
	public void setBounds(int left, int top, int right, int bottom){
		super.setBounds(left, top, right, bottom);
		
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		if(drawShadow){
			left += 10;
			top += 10;
			right -= 10;
			bottom -= 10;
		}
		
		width = right - left;
		height = bottom - top;
		
		centerX = width / 2;
		centerY = height / 2;
		
		bound  = new RectF(left, top, right, bottom);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRoundRect(bound, corner, corner, paint);
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return PixelFormat.TRANSPARENT;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}

}
