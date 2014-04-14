package com.pzad.graphics;

public class PzHalfRoundCornerDrawable extends PzRoundCornerDrawable {
	
	private int direction;
	
	public static final int DIRECTION_LEFT = 0x1;
	public static final int DIRECTION_RIGHT = 0x2;
	public static final int DIRECTION_TOP = 0x4;
	public static final int DIRECTION_BOTTOM = 0x8;
	
	private int exceed;

	public PzHalfRoundCornerDrawable(int corner, int backgroundColor, boolean drawShadow, int direction) {
		super(corner, backgroundColor, drawShadow);
		this.exceed = corner * 2;
		this.direction = direction;
	}
	
	public PzHalfRoundCornerDrawable(int corner, int strokeColor, int strokeWidth, int direction){
		super(corner, strokeColor, strokeWidth);
		this.exceed = corner * 2;
		this.direction = direction;
	}
	
	public PzHalfRoundCornerDrawable(int corner, int backgroundColor, int shadowRadius, int shadowX, int shadowY, int shadowColor, int direction){
		super(corner, backgroundColor, shadowRadius, shadowX, shadowY, shadowColor);
		this.exceed = corner * 2;
		this.direction = direction;
	}
	
	@Override
	public void setBounds(int left, int top, int right, int bottom){
		
		if((direction & DIRECTION_LEFT) != 0) left -= exceed;
		if((direction & DIRECTION_TOP) != 0) top -= exceed;
		if((direction & DIRECTION_RIGHT) != 0) right += exceed;
		if((direction & DIRECTION_BOTTOM) != 0) bottom += exceed;
		
		super.setBounds(left, top, right, bottom);
	}
}
