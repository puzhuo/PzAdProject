package com.pzad.category.api;

import com.pzad.category.AdsArgs;
import com.pzad.category.entities.Statistic;

//所有类型广告生命周期接口，广告类应实现这个接口以便manager统一管理
public interface AdsCycleAPI {
	//启动广告
	public void start(AdsArgs... args);
	//统计
	public Statistic getStatistic();
	//just in case
	public void stop();
}
