package com.pzad.graphics;

public class PzHalfRoundCornerDrawable extends PzRoundCornerDrawable {
	
	private int direction;
	
	public static final int DIRECTION_LEFT = 0x1;
	public static final int DIRECTION_RIGHT = 0x2;
	public static final int DIRECTION_TOP = 0x4;
	public static final int DIRECTION_BOTTOM = 0x8;

	public PzHalfRoundCornerDrawable(int corner, int backgroundColor, boolean drawShadow, int direction) {
		super(corner, backgroundColor, drawShadow);
		this.direction = direction;
	}
	
	public PzHalfRoundCornerDrawable(int corner, int strokeColor, int strokeWidth, int direction){
		super(corner, strokeColor, strokeWidth);
		this.direction = direction;
	}
	
	public PzHalfRoundCornerDrawable(int corner, int backgroundColor, int shadowRadius, int shadowX, int shadowY, int shadowColor, int direction){
		super(corner, backgroundColor, shadowRadius, shadowX, shadowY, shadowColor);
		this.direction = direction;
	}
	
	@Override
	public void setBounds(int left, int top, int right, int bottom){
		
		if((direction & DIRECTION_LEFT) != 0) left -= 20;
		if((direction & DIRECTION_TOP) != 0) top -= 20;
		if((direction & DIRECTION_RIGHT) != 0) right += 20;
		if((direction & DIRECTION_BOTTOM) != 0) bottom += 20;
		
		super.setBounds(left, top, right, bottom);
	}
}
