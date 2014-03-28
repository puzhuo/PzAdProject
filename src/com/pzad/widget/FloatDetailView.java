package com.pzad.widget;

import java.util.ArrayList;
import java.util.List;

import com.pzad.Constants;
import com.pzad.adapter.DetailGridViewAdapter;
import com.pzad.entities.AppInfo;
import com.pzad.graphics.PzRoundCornerDrawable;
import com.pzad.services.FloatWindowService.OnFloatViewEventListener;
import com.pzad.utils.CalculationUtil;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FloatDetailView extends RelativeLayout{
	
	private boolean simpleForm = true;
	
	private int width;
	private int height;
	
	private int initX;
	private int initY;
	
	private PzCloseButton closeButton;
	private GridView gridView;
	
	private DetailGridViewAdapter gridViewAdapter;
	private List<AppInfo> gridViewAdapterDatas;
	
	private WindowManager.LayoutParams layoutParams;
	
	private OnFloatViewEventListener onFloatViewEventListener;
	
	public FloatDetailView(Context context, int initX, int initY, boolean simpleForm){
		super(context, null);
		
		this.simpleForm = simpleForm;
		init(context, null);
	}
	
	public FloatDetailView(Context context, int initX, int initY){
		this(context);
		
		this.initX = initX;
		this.initY = initY;
		
		/*
		int[] screenReso = SystemMeasurementUtil.getScreenMeasurement(context);
		
		ScaleAnimation sAnimation = new ScaleAnimation(0.1F, 1.0F, 0.1F, 1.0F, Animation.RELATIVE_TO_PARENT, initX / screenReso[0] - 0.5F, Animation.RELATIVE_TO_PARENT, initY / screenReso[1] - 0.5F);
		sAnimation.setDuration(300);
		sAnimation.setFillAfter(true);
		sAnimation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				setBackgroundDrawable(new PzRoundCornerDrawable(CalculationUtil.dip2px(FloatDetailView.this.getContext(), 6), 0xFFF5F5F5, true));
				if(closeButton != null) closeButton.invalidate();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		animationController = new LayoutAnimationController(sAnimation);
		 */
	}
	
	public FloatDetailView(Context context, boolean simpleForm){
		super(context, null);
		
		this.simpleForm = simpleForm;
		init(context, null);
	}
	
	public FloatDetailView(Context context){
		this(context, null);
	}
	
	public FloatDetailView(Context context, AttributeSet attrs){
		super(context, attrs);
		
	    init(context, attrs);
		
	}
	
	private void init(Context context, AttributeSet attrs){
		LinearLayout buttonLayout = new LinearLayout(context, attrs);
		buttonLayout.setId(1024 * 65536);
		RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		ColorStateList recommendButtonStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_pressed}},
				                                                     new int[]{0xFF000000, Constants.GLOBAL_HIGHLIGHT_COLOR});
		
		Button recommendButton = new Button(context, attrs);
		recommendButton.setTextColor(recommendButtonStateList);
		recommendButton.setBackgroundColor(0);
		recommendButton.setText("推荐");
		
		ColorStateList gameButtonStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_pressed}},
                                                                     new int[]{0xFF000000, Constants.GLOBAL_HIGHLIGHT_COLOR});
		
		Button gameButton = new Button(context, attrs);
		gameButton.setTextColor(gameButtonStateList);
		gameButton.setBackgroundColor(0);
		gameButton.setText("游戏");
		
		ColorStateList appButtonStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_pressed}},
				                                               new int[]{0xFF000000, Constants.GLOBAL_HIGHLIGHT_COLOR});
		
		Button appButton = new Button(context, attrs);
		appButton.setTextColor(appButtonStateList);
		appButton.setBackgroundColor(0);
		appButton.setText("应用");
		
		View divider0 = new View(context, attrs);
		divider0.setBackgroundColor(Constants.GLOBAL_SHADOW_COLOR);
		
		View divider1 = new View(context, attrs);
		divider1.setBackgroundColor(Constants.GLOBAL_SHADOW_COLOR);
		
		LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.FILL_PARENT);
		dividerParams.setMargins(0, CalculationUtil.dip2px(context, 10), 0, CalculationUtil.dip2px(context, 10) + 10);
		
		LinearLayout.LayoutParams switchButtonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		switchButtonParams.weight = 1.0F;
		switchButtonParams.setMargins(0, 0, 0, 10);
		
		if(!simpleForm){
			ColorStateList newsButtonStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_pressed}},
					                                                new int[]{0xFF000000, Constants.GLOBAL_HIGHLIGHT_COLOR});
			
			Button newsButton = new Button(context, attrs);
			newsButton.setTextColor(newsButtonStateList);
			newsButton.setBackgroundColor(0);
			newsButton.setText("热点");
			
			View divider_1 = new View(context, attrs);
			divider_1.setBackgroundColor(Constants.GLOBAL_SHADOW_COLOR);
			
			buttonLayout.addView(newsButton, switchButtonParams);
			buttonLayout.addView(divider_1, dividerParams);
		}
		
		buttonLayout.addView(recommendButton, switchButtonParams);
		buttonLayout.addView(divider0, dividerParams);
		buttonLayout.addView(gameButton, switchButtonParams);
		buttonLayout.addView(divider1, dividerParams);
		buttonLayout.addView(appButton, switchButtonParams);
		
		addView(buttonLayout, buttonLayoutParams);
		
		gridView = new GridView(context, attrs);
		int gridViewPadding = CalculationUtil.dip2px(context, 10);
		RelativeLayout.LayoutParams contentBackgroundParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		int margin = CalculationUtil.dip2px(context, 10);
		contentBackgroundParams.setMargins(margin / 2, margin + margin + margin + margin, margin / 2, 0);
		contentBackgroundParams.addRule(RelativeLayout.ABOVE, buttonLayout.getId());
		
		addView(gridView, contentBackgroundParams);
		gridView.setBackgroundDrawable(new PzRoundCornerDrawable(CalculationUtil.dip2px(context, 2), Constants.GLOBAL_BACKGROUND_COLOR, 3, 0, 1, 0x44000000));
		gridView.setNumColumns(3);
		gridView.setVerticalFadingEdgeEnabled(false);
		gridView.setFadingEdgeLength(0);
		gridView.setVerticalScrollBarEnabled(false);
		gridView.setVerticalSpacing(gridViewPadding);
		gridView.setHorizontalSpacing(gridViewPadding);
		gridViewPadding *= 0.8F;
		gridView.setPadding(gridViewPadding, gridViewPadding, gridViewPadding, gridViewPadding);
		gridViewAdapterDatas = new ArrayList<AppInfo>();
		gridViewAdapter = new DetailGridViewAdapter(context, gridViewAdapterDatas);
		gridView.setAdapter(gridViewAdapter);
		
		closeButton = new PzCloseButton(context, attrs);
		RelativeLayout.LayoutParams closeButtonParams = new RelativeLayout.LayoutParams(CalculationUtil.dip2px(context, 30), CalculationUtil.dip2px(context, 30));
		closeButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		closeButtonParams.setMargins(0, CalculationUtil.dip2px(context, 8), CalculationUtil.dip2px(context, 8), 0);
		
		addView(closeButton, closeButtonParams);
		closeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(onFloatViewEventListener != null) onFloatViewEventListener.onDetailViewClose();
			}
		});
		
		setBackgroundDrawable(new PzRoundCornerDrawable(CalculationUtil.dip2px(FloatDetailView.this.getContext(), 6), 0xFFF5F5F5, true));
	}
	
	public void setParams(WindowManager.LayoutParams layoutParams){
		this.layoutParams = layoutParams;
	}
	
	public void setOnFloatViewEventListener(OnFloatViewEventListener listener){
		this.onFloatViewEventListener = listener;
	}
	
	public OnFloatViewEventListener getOnFloatViewEventListener(){
		return onFloatViewEventListener;
	}
	
	public void setAppInfos(List<AppInfo> datas){
		gridViewAdapterDatas.clear();
		gridViewAdapterDatas.addAll(datas);
		
		gridViewAdapter.notifyDataSetChanged();
	}
	
	public List<AppInfo> getAppInfos(){
		return gridViewAdapterDatas;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
	}
}
