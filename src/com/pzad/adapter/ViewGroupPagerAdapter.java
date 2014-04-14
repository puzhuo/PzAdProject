package com.pzad.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.pzad.entities.AppInfo;
import com.pzad.entities.NewsInfo;
import com.pzad.utils.CalculationUtil;

public class ViewGroupPagerAdapter extends PagerAdapter{
	
	public static final int TYPE_SIMPLE = 0;
	public static final int TYPE_ALL = 1;
	
	private Context context;
	private int type;

	private List<View> container;
	private List<Map<String, Integer>> states;
	private List<Adapter> adapters;
	private List<AppInfo> appInfos;
	private List<NewsInfo> newsInfos;
	
	public ViewGroupPagerAdapter(Context context, int type, List<AppInfo> appInfos, List<NewsInfo> newsInfos){
		this.context = context;
		this.type = type;
		this.appInfos = appInfos;
		this.newsInfos = newsInfos;
		
		container = new ArrayList<View>();
		states = new ArrayList<Map<String, Integer>>();
		adapters = new ArrayList<Adapter>();
	}
	
	@Override
	public View instantiateItem(ViewGroup container, int position){
		if(this.container.size() > position){
			View view = this.container.get(position);
			if(view != null){
				
				return view;
			}
		}
		View newInstance = getItem(position);
		while(this.container.size() <= position){
			this.container.add(null);
		}
		
		this.container.set(position, newInstance);
		if(newInstance instanceof ListView && states.size() > position){
			((ListView) newInstance).setSelectionFromTop(states.get(position).get("index"), states.get(position).get("top"));
		}
		if(newInstance instanceof GridView && states.size() > position){
			((GridView) newInstance).setSelection(states.get(position).get("index"));
			//((GridView) newInstance).scrollBy(0, -states.get(position).get("top"));
		}
		
		if(newInstance instanceof AdapterView){
			while(this.adapters.size() <= position){
				this.adapters.add(null);
			}
			this.adapters.set(position, ((AdapterView) newInstance).getAdapter());
		}
		
		container.addView(newInstance);
		return newInstance;
	}
	
    public View getItem(int position){
        if(type == TYPE_SIMPLE) position++;
        
        switch(position){
        case 0:
        	ListView newsList = new ListView(context);
        	int newsListViewPadding = CalculationUtil.dip2px(context,  10);
        	
        	newsList.setBackgroundColor(0);
        	newsList.setVerticalFadingEdgeEnabled(false);
        	newsList.setFadingEdgeLength(0);
        	newsList.setVerticalScrollBarEnabled(false);
        	newsList.setDivider(null);
        	newsList.setDividerHeight(0);
        	newsListViewPadding *= 0.8F;
        	newsList.setPadding(newsListViewPadding, 0, newsListViewPadding, 0);
        	NewsListViewAdapter newsAdapter = new NewsListViewAdapter(context, newsInfos);
        	newsList.setAdapter(newsAdapter);
        	
        	return newsList;
        case 1:
        	GridView gridView = new GridView(context);
    		int gridViewPadding = CalculationUtil.dip2px(context, 10);
    		
    		gridView.setBackgroundColor(0);
    		gridView.setNumColumns(3);
    		gridView.setVerticalFadingEdgeEnabled(false);
    		gridView.setFadingEdgeLength(0);
    		gridView.setVerticalScrollBarEnabled(false);
    		gridView.setVerticalSpacing(gridViewPadding);
    		gridView.setHorizontalSpacing(gridViewPadding);
    		gridViewPadding *= 0.8F;
    		gridView.setPadding(gridViewPadding, 0, gridViewPadding, 0);
    		DetailGridViewAdapter gridViewAdapter = new DetailGridViewAdapter(context, appInfos);
    		gridView.setAdapter(gridViewAdapter);
        	
    		return gridView;
        case 2:
        	ListView listView = new ListView(context);
        	int listViewPadding = CalculationUtil.dip2px(context, 10);
        	
        	listView.setBackgroundColor(0);
        	listView.setVerticalFadingEdgeEnabled(false);
        	listView.setFadingEdgeLength(0);
        	listView.setVerticalScrollBarEnabled(false);
        	listView.setDivider(null);
        	listView.setDividerHeight(0);
        	listViewPadding *= 0.8F;
        	listView.setPadding(listViewPadding, 0, listViewPadding, 0);
        	DetailListViewAdapter listViewAdapter = new DetailListViewAdapter(context, appInfos);
        	listView.setAdapter(listViewAdapter);
        	
        	return listView;
        case 3:
        	
        	return new View(context);
        }
        
        return null;
    }
	
	@Override
	public int getCount() {
		return type == TYPE_SIMPLE ? 3 : 4;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (View) object == view;
	}
	
	@Override
	public Parcelable saveState(){
		Bundle parcel = new Bundle();
		
		for(int i = 0; i < container.size(); i++){
			if(container.get(i) instanceof AdapterView){
				AdapterView av = (AdapterView) container.get(i);
				View v = av.getChildAt(0);
				int top = (v == null) ? 0 : v.getTop();
				parcel.putInt("index" + i, av.getFirstVisiblePosition());
				parcel.putInt("top" + i, top);
			}else{
				parcel.putInt("index" + i, 0);
				parcel.putInt("top" + i, 0);
			}
		}
		
		parcel.putInt("count", container.size());
		
		return parcel;
	}
	
	@Override
	public void restoreState(Parcelable state, ClassLoader loader){
		Bundle bundle = (Bundle) state;
		int count = bundle.getInt("count");
		for(int i = 0; i < count; i++){
			View v = getItem(i);
			if(v instanceof ListView){
				((ListView) v).setSelectionFromTop(bundle.getInt("index" + i), bundle.getInt("top" + i));
			}
			container.add(v);
			
		}
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object){
		View v = (View) object;
		while(states.size() <= position){
			states.add(null);
		}
		
		if(v instanceof AbsListView){
			int index = ((AbsListView) v).getFirstVisiblePosition();
			View child = ((AbsListView) v).getChildAt(0);
			int top = child == null ? 0 : child.getTop();
			
			Map<String, Integer> map  = new HashMap<String, Integer>();
			map.put("index", index);
			map.put("top", top);
			
			states.set(position, map);
		}else{
			states.set(position, null);
		}
		
		container.removeView(v);
		this.container.set(position, null);
		if(this.adapters.size() > position && this.adapters.get(position) != null) this.adapters.set(position, null);
	}

	public void notifyChildDataChanged(){
		if(adapters != null && adapters.size() > 0){
			for(Adapter a : adapters){
				if(a instanceof BaseAdapter){
					((BaseAdapter) a).notifyDataSetChanged();
				}
			}
		}
	}
}
