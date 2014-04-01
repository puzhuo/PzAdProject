package com.pzad.widget;

import com.pzad.Constants;
import com.pzad.utils.CalculationUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class ProgressBar extends View {

	private float progress = 1.0F;
	
	private int viewWidth;
	private int viewHeight;
	
	private Paint paint;
	private Shader moveShader;
	private RectF rect;
	private Matrix matrix;
	
	private boolean growDirection;
	private float growOffset;
	
	private int progressBarHeight;
	
	private int color;
	private int backgroundColor;
	
	public ProgressBar(Context context){
		this(context, null);
	}
	
	public ProgressBar(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public ProgressBar(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		rect = new RectF();
		matrix = new Matrix();
		
		growDirection = false;
		
		color = Constants.GLOBAL_HIGHLIGHT_COLOR;
		backgroundColor = Constants.GLOBAL_SHADOW_COLOR;
		
        progressBarHeight = CalculationUtil.dip2px(context, 4);
		
		moveShader = new LinearGradient(0, viewHeight - progressBarHeight - 1, progressBarHeight * 2, viewHeight + progressBarHeight, new int[]{0x90FFFFFF, 0x90FFFFFF, 0x00000000,0x00000000}, new float[]{0F, 0.5F, 0.5F, 1F}, Shader.TileMode.REPEAT);
		
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public void setProgress(float progress){
		this.progress = progress;
		
		invalidate();
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		if(heightMeasureSpec < progressBarHeight){
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(progressBarHeight, MeasureSpec.EXACTLY);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		
		viewWidth = MeasureSpec.getSize(widthMeasureSpec);
		viewHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		rect.left = 0;
		rect.top = 0;
		rect.right = viewWidth;
		rect.bottom = viewHeight;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		
		paint.setColor(backgroundColor);
		
		canvas.drawRect(0, 0, viewWidth, viewHeight, paint);
		
		paint.setColor(color);
		
		canvas.drawRect(rect, paint);
		growOffset = rect.top;
		if(growDirection){
			growOffset -= 2F;
		}else{
			growOffset += 2F;
		}
		if(growOffset <= rect.top){
			growDirection = false;
		}else if(growOffset >= rect.bottom){
			growDirection = true;
		}
		matrix.postTranslate(0, -growOffset);
		moveShader.setLocalMatrix(matrix);
		paint.setShader(moveShader);
		
		rect.right = viewWidth * progress;
		
		canvas.drawRect(rect, paint);
		
		paint.setShader(null);
		
		invalidate();
	}
}
