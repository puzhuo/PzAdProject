package com.pzad.net.api;

public interface Downloadable {
	
	public void onDownloadStart(String downloadLink);
	public void onDownloadComplete(String downloadLink, boolean isFileComplete, String resultFilePath);
	public void refreshProgress(String downloadLink, float progress);
	
}
