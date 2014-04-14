package com.pzad.entities;

import java.io.Serializable;

public class Statistic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8178010307070922374L;
	
	public static final int TYPE_BANNER = 0x1;
	public static final int TYPE_APP = 0x2;
	public static final int TYPE_FLOAT_WINDOW = 0x4;
	public static final int TYPE_TUI_WINDOW = 0x8;
	
	public static final String WINDOW_NAME_ENTIRE = "entire";
	public static final String WINDOW_NAME_NEWS = "news";
	public static final String WINDOW_NAME_RECOMMEND = "recommend";
	public static final String WINDOW_NAME_APP = "app";
	public static final String WINDOW_NAME_TOOL = "tool";
	
	private int downloadCount;
	private int installationCount;
	private int browseDetailCount;
	private int exhibitionCount;
	
	private String name;
	private String title;
	private String subTitle;
	
	public String getTitle(){
		return title;
	}
	
	public Statistic setTitle(String title){
		this.title = title;
		
		return this;
	}
	
	public String getSubTitle(){
		return subTitle;
	}
	
	public Statistic setSubTitle(String subTitle){
		this.subTitle = subTitle;
		
		return this;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public Statistic setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
		
		return this;
	}
	
	public int getInstallationCount(){
		return installationCount;
	}
	
	public Statistic setInstallationCount(int installationCount){
		this.installationCount = installationCount;
		
		return this;
	}

	public int getBrowseDetailCount() {
		return browseDetailCount;
	}

	public Statistic setBrowseDetailCount(int browseDetailCount) {
		this.browseDetailCount = browseDetailCount;
		
		return this;
	}

	public String getName() {
		return name;
	}

	public Statistic setName(String name, int type) {
		this.name = name + ":" + type;
		
		return this;
	}

	public int getExhibitionCount() {
		return exhibitionCount;
	}

	public Statistic setExhibitionCount(int exhibitionCount) {
		this.exhibitionCount = exhibitionCount;
		
		return this;
	}

	@Override
	public String toString() {
		return "Statistic [downloadCount=" + downloadCount
				+ ", installationCount=" + installationCount
				+ ", browseDetailCount=" + browseDetailCount
				+ ", exhibitionCount=" + exhibitionCount + ", name=" + name
				+ ", title=" + title + ", subTitle=" + subTitle + "]";
	}
	
	

}
