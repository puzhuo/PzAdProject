package com.pzad.category.floatads;

import com.pzad.services.FloatWindowService.OnFloatViewEventListener;
import com.pzad.utils.PLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatCircleView extends View {

	private int width;
	private int height;
	private int centerX;
	private int centerY;
	
	private int radius;
	
	private float xDown;
	private float yDown;
	
	private float xCollapseRange;
	
	private Paint paint;
	
	private WindowManager windowManager;
	private WindowManager.LayoutParams layoutParams;
	
	private OnFloatViewEventListener onFloatViewEventListener;
	
	private GestureDetector gestureDetector;
	private SimpleOnGestureListener onGestureListener = new SimpleOnGestureListener(){
		@Override
		public boolean onDown(MotionEvent event){
			if(isInRadius((int) event.getX(), (int) event.getY())){
				xDown = event.getX();
				yDown = event.getY();
				return true;
			}else{
				return false;
			}
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
			
			updateWindowLayout((int) (e2.getRawX() - e1.getX()), (int) (e2.getRawY() - e1.getY()));
			//PLog.d(xDown + "", yDown + "");
			return true;
		}
		
		@Override
		public boolean onSingleTapConfirmed(MotionEvent event){
			if(onFloatViewEventListener != null) onFloatViewEventListener.onCircleViewTap();
			return true;
		}
		
		@Override
		public void onLongPress(MotionEvent event){
			if(onFloatViewEventListener != null) onFloatViewEventListener.onCircleViewLongTap();
		}
	};
	
	private OnTouchListener onTouchListener = new OnTouchListener(){
		@Override
		public boolean onTouch(View v, MotionEvent event){
			return gestureDetector.onTouchEvent(event);
		}
	};
	
	public FloatCircleView(Context context){
		this(context, null);
	}
	
	public FloatCircleView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public FloatCircleView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xFFFFFFFF);
		
		gestureDetector = new GestureDetector(context, onGestureListener);
		setOnTouchListener(onTouchListener);
	}
	
	public void setOnFloatViewEventListener(OnFloatViewEventListener listener){
		this.onFloatViewEventListener = listener;
	}
	
	public void setParams(WindowManager.LayoutParams layoutParams){
		this.layoutParams = layoutParams;
	}
	
	public void updateWindowLayout(int x, int y){
		if(windowManager == null){
			windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		}
		
		layoutParams.x = x;
		layoutParams.y = y;
		
		windowManager.updateViewLayout(this, layoutParams);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		
		radius = width > height ? height / 2 : width / 2;
		
		centerX = width / 2;
		centerY = height / 2;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		if(xCollapseRange == 0){
			canvas.drawCircle(centerX, centerY, radius, paint);
		}
	}
	
	private boolean isInRadius(int x, int y){
		int dx = x - centerX;
		int dy = y - centerY;
		
		return dx * dx + dy * dy == radius * radius;
	}
}
