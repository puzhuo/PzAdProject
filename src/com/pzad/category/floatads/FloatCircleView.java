package com.pzad.category.floatads;

import com.pzad.Constants;
import com.pzad.services.FloatWindowService.OnFloatViewEventListener;
import com.pzad.utils.SystemMeasurementUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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
	
	private Rect textBound;
	private RectF arcBound;
	
	private int radius;
	private static final float radiusRate = 0.9F;
	
	private float xDown;
	private float yDown;
	
	private float xCollapseRange;
	
	private int percentage;
	
	private Paint paint;
	
	private int statusBarHeight;
	
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
			
			updateWindowLayout((int) (e2.getRawX() - xDown), (int) (e2.getRawY() - yDown) - getStatusBarHeight());
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
		
		textBound = new Rect();
		arcBound = new RectF();
		
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
		radius *= radiusRate;
		
		centerX = width / 2;
		centerY = height / 2;
		
		float b = radius * 0.85F;
		arcBound.top = centerY - b;
		arcBound.bottom = centerY + b;
		arcBound.left = centerX - b;
		arcBound.right = centerX + b;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		if(xCollapseRange == 0){
			paint.setShadowLayer(6, 0, 3, 0x66000000);//<---------------------------------------------------shadow layer for background
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(0xFFFFFFFF);
			canvas.drawCircle(centerX, centerY, radius, paint);//<------------------------------------------background circle
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(radius * 0.1F);
			paint.setColor(0xFFEEEEEE);
			paint.setShadowLayer(0,0,0,0);//<---------------------------------------------------------------cancel shadow layer
			canvas.drawCircle(centerX, centerY, radius * 0.95F, paint);//<----------------------------------outlet
			paint.setColor(0xFFCCCCCC);
			canvas.drawCircle(centerX, centerY, radius * 0.85F, paint);//<----------------------------------percentage circle background
			paint.setColor(Constants.GLOBAL_HIGHLIGHT_COLOR);
			canvas.drawArc(arcBound, -90, -(float) (3.6 * percentage), false, paint);//<--------------------percentage circle
			
			String p = percentage + "%";
			paint.setColor(getColorByPercent(percentage));
			paint.setStyle(Paint.Style.FILL);
			paint.setTextSize(radius * 0.6F);
			paint.getTextBounds(p, 0, p.length(), textBound);
			canvas.drawText(p, centerX - textBound.width() / 2, centerY + textBound.height() / 2, paint);//<percentage
		}
	}
	
	public void setPercentage(int percentage){
		this.percentage = percentage;
		invalidate();
	}
	
	private boolean isInRadius(int x, int y){
		int dx = x - centerX;
		int dy = y - centerY;
		
		return dx * dx + dy * dy <= radius * radius;
	}
	
	private int getColorByPercent(int percentage){
		if(percentage < 50){
			return Constants.GLOBAL_HIGHLIGHT_COLOR;
		}else if(percentage < 80){
			return 0xFFEF7D1A;
		}else{
			return 0xFFD31E00;
		}
	}
	
	private int getStatusBarHeight(){
		if(statusBarHeight == 0){
			statusBarHeight = SystemMeasurementUtil.getStatusBarHeight(getContext());
		}
		
		return statusBarHeight;
	}
}
