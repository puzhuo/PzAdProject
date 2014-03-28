package com.pzad.widget;

import com.pzad.Constants;
import com.pzad.entities.AppInfo;
import com.pzad.graphics.PzRoundCornerDrawable;
import com.pzad.utils.CalculationUtil;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppBlock extends RelativeLayout{

	private UrlImageView appIconView;
	private TextView appName;
	
	private View dividerHorizontal;
	private View dividerVertical;
	
	private AppInfo appInfo;
	
	public AppBlock(Context context){
		this(context, null);
	}
	
	public AppBlock(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public AppBlock(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		appIconView = new UrlImageView(context, attrs, defStyle);
		appIconView.setId(1024 * 256);
		int size = CalculationUtil.dip2px(context, 50);
		RelativeLayout.LayoutParams appIconLayoutParams = new RelativeLayout.LayoutParams(size, size);
		appIconLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		int margin = CalculationUtil.dip2px(context, 15);
		appIconLayoutParams.setMargins(margin, margin, margin, margin / 2);
		
		addView(appIconView, appIconLayoutParams);
		
		ColorStateList appNameStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_pressed}}, new int[]{Constants.GLOBAL_HIGHLIGHT_COLOR, 0xFF000000});
		
		appName = new TextView(context, attrs, defStyle);
		appName.setSingleLine(true);
		appName.setEllipsize(TruncateAt.END);
		appName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		appName.setTextColor(appNameStateList);
		appName.setGravity(Gravity.CENTER);
		appName.setPadding(appName.getPaddingLeft() + 2, appName.getPaddingTop(), appName.getPaddingRight() + 2, appName.getPaddingBottom() + 10);
		RelativeLayout.LayoutParams appNameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		appNameLayoutParams.addRule(RelativeLayout.BELOW, appIconView.getId());
		int appNameMargin = CalculationUtil.dip2px(context, 5);
		appNameLayoutParams.setMargins(appNameMargin, 0, appNameMargin, 0);
		
		addView(appName, appNameLayoutParams);
		
		int backgroundCorner = CalculationUtil.dip2px(context, 4);
		StateListDrawable stateDrawable = new StateListDrawable();
		stateDrawable.addState(new int[]{android.R.attr.state_pressed}, new PzRoundCornerDrawable(backgroundCorner, 0xFFF5F5F5, false));
		//int lightHighLight = (Constants.GLOBAL_HIGHLIGHT_COLOR & 0xFFFFFF) | 0x10000000;
		stateDrawable.addState(new int[]{-android.R.attr.state_pressed}, new PzRoundCornerDrawable(backgroundCorner, 0, false));
		
		/*
		
		int dividerMargin = CalculationUtil.dip2px(context, 20);
		dividerVertical = new View(context, attrs, defStyle);
		dividerVertical.setBackgroundColor(Constants.GLOBAL_SHADOW_COLOR);
		RelativeLayout.LayoutParams verticalDividerParams = new RelativeLayout.LayoutParams(1, RelativeLayout.LayoutParams.FILL_PARENT);
		verticalDividerParams.setMargins(0, dividerMargin, 0, dividerMargin);
		verticalDividerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		addView(dividerVertical, verticalDividerParams);
		
		dividerHorizontal = new View(context, attrs, defStyle);
		dividerHorizontal.setBackgroundColor(Constants.GLOBAL_SHADOW_COLOR);
		RelativeLayout.LayoutParams horizontalDividerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 1);
		horizontalDividerParams.setMargins(dividerMargin, 0, dividerMargin, 0);
		horizontalDividerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addView(dividerHorizontal, horizontalDividerParams);
		 */
		
		setBackgroundDrawable(stateDrawable);
	}
	
	public void setAppInfo(final AppInfo appInfo){
		if(appInfo != null){
			this.appInfo = appInfo;
			appIconView.setImageUrl(appInfo.getIcon());
			appName.setText(appInfo.getName());
			setClickable(true);
			setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(appInfo.getDownloadLink()));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(intent);
				}
			});
		}
	}
	
	public void setVerticalDividerVisible(int visibility){
		dividerVertical.setVisibility(visibility);
	}
	
	public void setHorizontalDividerVisible(int visibility){
		dividerHorizontal.setVisibility(visibility);
	}
}
