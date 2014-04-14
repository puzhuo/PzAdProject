package com.pzad.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pzad.Constants;
import com.pzad.entities.NewsInfo;
import com.pzad.graphics.PzHalfRoundCornerDrawable;
import com.pzad.services.FloatWindowService;
import com.pzad.utils.ActivityLoader;
import com.pzad.utils.CalculationUtil;
import com.pzad.widget.SeparateView;

public class NewsListViewAdapter extends BaseAdapter{
	
	private List<NewsInfo> datas;
	private Context context;
	
	public NewsListViewAdapter(Context context, List<NewsInfo> datas){
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size() + datas.size() - 1;
	}

	@Override
	public NewsInfo getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position ) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public int getViewTypeCount(){
		return 2;
	}
	
	@Override
	public int getItemViewType(int position){
		return position % 2 == 0 ? 0 : 1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(getItemViewType(position) == 0){
			if(convertView == null){
				convertView = new TextView(context);
				
				ColorStateList newsNameStateList = new ColorStateList(new int[][]{new int[]{-android.R.attr.state_pressed}, new int[]{android.R.attr.state_pressed}}, new int[]{Constants.GLOBAL_HIGHLIGHT_COLOR, 0xFF000000});
				
				((TextView) convertView).setTextColor(newsNameStateList);
				((TextView) convertView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				
				int padding = CalculationUtil.dip2px(context, 15);
				convertView.setPadding(padding, padding, padding, padding);
			}
			
			TextView tv = (TextView) convertView;
			tv.setText(getItem((position + 1) / 2).getTitle());
			tv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					Intent detailHideIntent = new Intent(FloatWindowService.ACTION_HIDE_FLOAT_DETAIL);
					detailHideIntent.setData(new Uri.Builder().scheme("package").build());
					context.sendBroadcast(detailHideIntent);
					
					ActivityLoader.startOfficialBrowser(context, getItem((position + 1) / 2).getUrl());
				}
			});
			
			int backgroundCorner = CalculationUtil.dip2px(context, 4);
			StateListDrawable stateDrawable = new StateListDrawable();
			if(position == 0 || position == getCount() - 1){
				int direction;
				if(position == 0){
					direction = PzHalfRoundCornerDrawable.DIRECTION_BOTTOM;
				}else{
					direction = PzHalfRoundCornerDrawable.DIRECTION_TOP;
				}
				stateDrawable.addState(new int[]{android.R.attr.state_pressed}, new PzHalfRoundCornerDrawable(backgroundCorner, 0xFFEEEEEE, 1, 0, 0, 0x33000000, direction));
				//int lightHighLight = (Constants.GLOBAL_HIGHLIGHT_COLOR & 0xFFFFFF) | 0x10000000;
				stateDrawable.addState(new int[]{-android.R.attr.state_pressed}, new PzHalfRoundCornerDrawable(backgroundCorner, 0xFFFFFFFF, 3, 0, 2, 0x33000000, direction));
			}else{
				stateDrawable.addState(new int[]{android.R.attr.state_pressed}, new PzHalfRoundCornerDrawable(backgroundCorner, 0xFFEEEEEE, 1, 0, 0, 0x33000000, PzHalfRoundCornerDrawable.DIRECTION_BOTTOM | PzHalfRoundCornerDrawable.DIRECTION_TOP));
				//int lightHighLight = (Constants.GLOBAL_HIGHLIGHT_COLOR & 0xFFFFFF) | 0x10000000;
				stateDrawable.addState(new int[]{-android.R.attr.state_pressed}, new PzHalfRoundCornerDrawable(backgroundCorner, 0xFFFFFFFF, 3, 0, 2, 0x33000000, PzHalfRoundCornerDrawable.DIRECTION_BOTTOM | PzHalfRoundCornerDrawable.DIRECTION_TOP));
			}
			
			convertView.setBackgroundDrawable(stateDrawable);
		}else{
			if(convertView == null){
				convertView = new SeparateView(context, 0x88000000);
				convertView.setPadding(10, 0, 10, 0);
				convertView.setBackgroundDrawable(new PzHalfRoundCornerDrawable(CalculationUtil.dip2px(context, 4), 0xFFFFFFFF, 3, 0, 2, 0x33000000, PzHalfRoundCornerDrawable.DIRECTION_BOTTOM | PzHalfRoundCornerDrawable.DIRECTION_TOP));
			}
		}
		
		return convertView;
	}

}
