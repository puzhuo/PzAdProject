package com.pzad.adapter;

import java.util.List;

import com.pzad.entities.AppInfo;
import com.pzad.widget.AppBlock;
import com.pzad.widget.AppDetailBlock;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DetailListViewAdapter extends BaseAdapter {
	
	private Context context;
	private List<AppInfo> datas;
	
	public DetailListViewAdapter(Context context, List<AppInfo> datas){
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public AppInfo getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = new AppDetailBlock(context);
		}
		
		AppDetailBlock block = (AppDetailBlock) convertView;
		block.setAppInfo(getItem(position));
		
		return convertView;
	}

}
