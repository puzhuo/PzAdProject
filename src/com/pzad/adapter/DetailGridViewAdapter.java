package com.pzad.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pzad.entities.AppInfo;
import com.pzad.net.ApkDownloadProvider;
import com.pzad.net.api.Downloadable;
import com.pzad.widget.AppBlock;

public class DetailGridViewAdapter extends BaseAdapter {
	
	private Context context;
	private List<AppInfo> datas;
	
	public DetailGridViewAdapter(Context context, List<AppInfo> datas){
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
			convertView = new AppBlock(context);
			if(convertView instanceof Downloadable){
				ApkDownloadProvider.getInstance(context).registerDownloadable(((Downloadable) convertView));
			}
		}
		
		AppBlock block = (AppBlock) convertView;
		block.setAppInfo(getItem(position));
		
		block.onDownloadComplete(null, false, null);
		
		return convertView;
	}

}
