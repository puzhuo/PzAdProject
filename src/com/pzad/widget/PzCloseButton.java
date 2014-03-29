package com.pzad.widget;

import com.pzad.Constants;
import com.pzad.graphics.PzButtonDrawable;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.Button;

public class PzCloseButton extends Button {
	
	private int width;
	private int height;

	public PzCloseButton(Context context){
		this(context, null);
	}
	
	public PzCloseButton(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public PzCloseButton(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if(width == 0 && height == 0){
			StateListDrawable stateDrawable = new StateListDrawable();
			PzButtonDrawable buttonNormalDrawable = new PzButtonDrawable(Constants.GLOBAL_HIGHLIGHT_COLOR);
			PzButtonDrawable buttonPressDrawable = new PzButtonDrawable(0xFF000000);
			
			width = MeasureSpec.getSize(widthMeasureSpec);
			height = MeasureSpec.getSize(heightMeasureSpec);
			
			buttonNormalDrawable.setBounds(0, 0, width, height);
			buttonPressDrawable.setBounds(0, 0, width, height);
			
			stateDrawable.addState(new int[]{-android.R.attr.state_pressed}, buttonNormalDrawable);
			stateDrawable.addState(new int[]{android.R.attr.state_pressed}, buttonPressDrawable);
			
			this.setBackgroundDrawable(stateDrawable);
			
		}
	}
}
