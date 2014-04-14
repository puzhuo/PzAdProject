package com.pzad.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pzad.Constants;
import com.pzad.entities.AppInfo;
import com.pzad.entities.Statistic;
import com.pzad.graphics.PzHalfRoundCornerDrawable;
import com.pzad.net.ApkDownloadProvider;
import com.pzad.net.api.Downloadable;
import com.pzad.services.FloatWindowService;
import com.pzad.utils.ActivityLoader;
import com.pzad.utils.CalculationUtil;
import com.pzad.utils.StatUtil;

public class AppDetailBlock extends RelativeLayout implements Downloadable {
	
	private UrlImageView icon;
	
	private TextView appName;
	private TextView description;
	private PzRatingBar ratingBar;
	private PzProgressBar progressBar;
	
	private RelativeLayout boundLayout;
	private RelativeLayout downloadButton;
	private TextView downloadText;
	
	private AppInfo appInfo;
	
	public AppDetailBlock(Context context){
		this(context, null);
	}
	
	public AppDetailBlock(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public AppDetailBlock(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		int backgroundCorner = CalculationUtil.dip2px(context, 4);
		StateListDrawable downloadStateDrawable = new StateListDrawable();
		downloadStateDrawable.addState(new int[]{android.R.attr.state_pressed}, new PzHalfRoundCornerDrawable(backgroundCorner, 0xFFEEEEEE, 1, 0, 0, 0x33000000, PzHalfRoundCornerDrawable.DIRECTION_LEFT));
		downloadStateDrawable.addState(new int[]{-android.R.attr.state_pressed}, new PzHalfRoundCornerDrawable(backgroundCorner, 0xFFFFFFFF, 3, 0, 2, 0x33000000, PzHalfRoundCornerDrawable.DIRECTION_LEFT));
		
		int iconMargin = CalculationUtil.dip2px(context, 15);
		downloadButton = new RelativeLayout(context, attrs, defStyle);
		downloadButton.setBackgroundDrawable(downloadStateDrawable);
		downloadButton.setId(1024 * 768);
		downloadButton.setPadding(iconMargin, 0, iconMargin + 10, 0);
		RelativeLayout.LayoutParams downloadParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.FILL_PARENT);
		downloadParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		addView(downloadButton, downloadParams);
		
        ColorStateList downloadTextStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_pressed}}, new int[]{Constants.GLOBAL_HIGHLIGHT_COLOR, 0xFF000000});
		
		downloadText = new TextView(context, attrs, defStyle);
		downloadText.setText("下载");
		downloadText.setTextColor(downloadTextStateList);
		downloadText.setGravity(Gravity.CENTER);
		RelativeLayout.LayoutParams downloadTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, CalculationUtil.dip2px(context, 80));
		
		downloadButton.addView(downloadText, downloadTextParams);
		
		
		StateListDrawable boundStateDrawable = new StateListDrawable();
		boundStateDrawable.addState(new int[]{android.R.attr.state_pressed}, new PzHalfRoundCornerDrawable(backgroundCorner, 0xFFEEEEEE, 1, 0, 0, 0x33000000, PzHalfRoundCornerDrawable.DIRECTION_RIGHT));
		boundStateDrawable.addState(new int[]{-android.R.attr.state_pressed}, new PzHalfRoundCornerDrawable(backgroundCorner, 0xFFFFFFFF, 3, 0, 2, 0x33000000, PzHalfRoundCornerDrawable.DIRECTION_RIGHT));
		
		
		boundLayout = new RelativeLayout(context, attrs, defStyle);
		boundLayout.setId(1024 * 251);
		boundLayout.setPadding(0, 0, CalculationUtil.dip2px(context, 5), 0);
		boundLayout.setBackgroundDrawable(boundStateDrawable);
		RelativeLayout.LayoutParams boundLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		boundLayoutParams.addRule(RelativeLayout.LEFT_OF, downloadButton.getId());
		
		addView(boundLayout, boundLayoutParams);
		
		downloadParams.addRule(RelativeLayout.ALIGN_TOP, boundLayout.getId());
		downloadParams.addRule(RelativeLayout.ALIGN_BOTTOM, boundLayout.getId());
		downloadButton.setLayoutParams(downloadParams);
		
		icon = new UrlImageView(context, attrs, defStyle);
		icon.setId(1024 * 256);
		int size = CalculationUtil.dip2px(context, 80);
		RelativeLayout.LayoutParams iconLayoutParams = new RelativeLayout.LayoutParams(size, size);
		iconLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		icon.setPadding(iconMargin, iconMargin, iconMargin, iconMargin);
		
		boundLayout.addView(icon, iconLayoutParams);
		
		RelativeLayout descLayout = new RelativeLayout(context, attrs, defStyle);
		RelativeLayout.LayoutParams descLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		descLayoutParams.addRule(RelativeLayout.LEFT_OF, downloadButton.getId());
		descLayoutParams.addRule(RelativeLayout.RIGHT_OF, icon.getId());
		descLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		
		boundLayout.addView(descLayout, descLayoutParams);
		
		ColorStateList appNameStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_pressed}}, new int[]{Constants.GLOBAL_HIGHLIGHT_COLOR, 0xFF000000});
		
		appName = new TextView(context, attrs, defStyle);
		appName.setId(1024 * 255);
		appName.setTextColor(appNameStateList);
		RelativeLayout.LayoutParams appNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		appNameParams.addRule(RelativeLayout.ALIGN_TOP, icon.getId());
		
		descLayout.addView(appName, appNameParams);
		
		ColorStateList descriptionStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_pressed}}, new int[]{(Constants.GLOBAL_HIGHLIGHT_COLOR & 0xFFFFFF) | 0x66000000, 0x66000000});
		
		description = new TextView(context, attrs, defStyle);
		description.setId(1024 * 254);
		description.setSingleLine(true);
		description.setEllipsize(TruncateAt.END);
		description.setTextColor(descriptionStateList);
		description.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		RelativeLayout.LayoutParams descriptionParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		descriptionParams.addRule(RelativeLayout.BELOW, appName.getId());
		descriptionParams.addRule(RelativeLayout.ALIGN_LEFT, appName.getId());
		
		descLayout.addView(description, descriptionParams);
		
		ratingBar = new PzRatingBar(context, attrs);
		ratingBar.setRating(Math.round(Math.random() * 10) * 0.5F);
		RelativeLayout.LayoutParams ratingParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, CalculationUtil.dip2px(context, 15));
		ratingParams.addRule(RelativeLayout.ALIGN_LEFT, appName.getId());
		ratingParams.addRule(RelativeLayout.BELOW, description.getId());
		ratingParams.addRule(RelativeLayout.LEFT_OF, downloadButton.getId());
		ratingParams.setMargins(0, 0, CalculationUtil.dip2px(context, 80), 0);
		
		descLayout.addView(ratingBar, ratingParams);
		
		progressBar = new PzProgressBar(context, attrs);
		RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		progressParams.addRule(RelativeLayout.ALIGN_LEFT, appName.getId());
		progressParams.addRule(RelativeLayout.BELOW, description.getId());
		progressParams.addRule(RelativeLayout.LEFT_OF, downloadButton.getId());
		progressParams.setMargins(0, CalculationUtil.dip2px(context, 5), CalculationUtil.dip2px(context, 4), 0);
		
		descLayout.addView(progressBar, progressParams);
		progressBar.setVisibility(View.GONE);
		
		View divider = new View(context, attrs, defStyle);
		divider.setBackgroundColor((Constants.GLOBAL_SHADOW_COLOR & 0xFFFFFF) | 0x33000000);
		RelativeLayout.LayoutParams dividerParams = new RelativeLayout.LayoutParams(1, RelativeLayout.LayoutParams.FILL_PARENT);
		int dividerMargin = CalculationUtil.dip2px(context, 15);
		dividerParams.setMargins(0, dividerMargin, 0, dividerMargin);
		dividerParams.addRule(RelativeLayout.ALIGN_LEFT, downloadButton.getId());
		dividerParams.addRule(RelativeLayout.ALIGN_TOP, boundLayout.getId());
		dividerParams.addRule(RelativeLayout.ALIGN_BOTTOM, boundLayout.getId());
		
		addView(divider, dividerParams);
		
	}
	
	public AppInfo getAppInfo(){
		return appInfo;
	}
	
	public void setAppInfo(final AppInfo appInfo){
		
		if(appInfo != null){
			this.appInfo = appInfo;
			icon.setImageUrl(appInfo.getIcon());
			appName.setText(appInfo.getName());
			description.setText(appInfo.getDescription());
			boundLayout.setClickable(true);
			boundLayout.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					
					StatUtil.sendStatistics(getContext(), new Statistic().setName(appInfo.getName(), Statistic.TYPE_APP).setBrowseDetailCount(1));
					
					Intent detailHideIntent = new Intent(FloatWindowService.ACTION_HIDE_FLOAT_DETAIL);
					detailHideIntent.setData(new Uri.Builder().scheme("package").build());
					getContext().sendBroadcast(detailHideIntent);
					
					ActivityLoader.startOfficialBrowser(getContext(), appInfo.getDetailLink()); 
				}
			});
			
			downloadButton.setClickable(true);
			downloadButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					
					ApkDownloadProvider.getInstance(getContext()).runNewTask(appInfo.getDownloadLink(), appInfo.getName());
					/*
					Intent downloadStsIntent = new Intent();
					downloadStsIntent.setAction(StatisticReceiver.ACTION_RECEIVE_STATISTIC);
					
					Statistic downloadS = new Statistic();
					downloadS.setName(appInfo.getName(), Statistic.TYPE_APP);
					
					if(downloadText.getText().equals("下载")){
						downloadS.setDownloadCount(1);
						
						if(progressBar.getVisibility() != View.VISIBLE){
							new FileLoader(getContext(), appInfo.getDownloadLink(), appInfo.getName()){
								
								@Override
								public void onPreExecute(){
									ratingBar.setVisibility(View.INVISIBLE);
									progressBar.setVisibility(View.VISIBLE);
								}
								
								@Override
								public void onProgress(float progress){
									progressBar.setProgress(progress);
								}
								
								@Override
								public void onFinish(File result){
									ratingBar.setVisibility(View.VISIBLE);
									progressBar.setVisibility(View.INVISIBLE);
									if(result != null && result.exists()){
										downloadText.setText("安装");
										installFile = result;
									}
								}
								
							}.executeOnExecutor(PzExecutorFactory.getApkLoadThreadPool());
						}
					}else{
						if(installFile != null && installFile.exists()){
							
							PackageManager pm = getContext().getPackageManager();
							PackageInfo pkgInfo = pm.getPackageArchiveInfo(installFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
							if(pkgInfo != null){
								ApplicationInfo applicationInfo = pkgInfo.applicationInfo;
								applicationInfo.sourceDir = installFile.getAbsolutePath();
								applicationInfo.publicSourceDir = applicationInfo.sourceDir;
								
								Intent installationIntent = new Intent(FloatWindowService.ACTION_INSTALLATION_PROCESS);
								installationIntent.putExtra("package_name", applicationInfo.packageName);
								installationIntent.putExtra("app_name", appInfo.getName());
								installationIntent.setData(new Uri.Builder().scheme("package").build());
								
								getContext().sendBroadcast(installationIntent);
							}
							
							Intent installIntent = new Intent(Intent.ACTION_VIEW);
							installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							installIntent.setDataAndType(Uri.fromFile(installFile), "application/vnd.android.package-archive");
							getContext().startActivity(installIntent);
							Intent hideIntent = new Intent(FloatWindowService.ACTION_HIDE_FLOAT_DETAIL);
							hideIntent.putExtra("installation", true);
							hideIntent.putExtra("app_info", appInfo);
							hideIntent.putExtra("file_path", installFile);
							hideIntent.setData(new Uri.Builder().scheme("package").build());
							getContext().sendBroadcast(hideIntent);
						}
					}
					
					downloadStsIntent.putExtra(StatisticReceiver.NAME, downloadS);
					getContext().sendBroadcast(downloadStsIntent);
					 */
					
					
					
					/*
					getContext().sendBroadcast(new Intent(FloatWindowService.ACTION_HIDE_FLOAT_DETAIL));
					
					Intent downloadIntent = new Intent(Intent.ACTION_VIEW);
					downloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					downloadIntent.setData(Uri.parse(appInfo.getDownloadLink()));
					AppDetailBlock.this.getContext().startActivity(downloadIntent);
					 */
				}
			});
		}
	}

	@Override
	public void refreshProgress(String downloadLink, float progress) {
		if(appInfo != null && appInfo.getDownloadLink().equals(downloadLink)){
			if(progressBar.getVisibility() == View.GONE){
				progressBar.setVisibility(View.VISIBLE);
				ratingBar.setVisibility(View.INVISIBLE);
			}
			progressBar.setProgress(progress);
		}
	}

	@Override
	public void onDownloadStart(String downloadLink) {
		if(appInfo != null && appInfo.getDownloadLink().equals(downloadLink)){
			progressBar.setVisibility(View.VISIBLE);
			ratingBar.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onDownloadComplete(String downloadLink, boolean isFileComplete, String resultFilePath) {
		progressBar.setVisibility(View.GONE);
		ratingBar.setVisibility(View.VISIBLE);
	}
	
	@Override
	public String getDownloadLink(){
		if(appInfo != null) return appInfo.getDownloadLink();
		
		return null;
	}
	
	@Override
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		ApkDownloadProvider.getInstance(getContext()).removeDownloadable(this);
	}
}
