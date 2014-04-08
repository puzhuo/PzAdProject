package com.pzad.category.bannerads;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.pzad.broadcast.StatisticReceiver;
import com.pzad.entities.BannerInfo;
import com.pzad.entities.Statistic;
import com.pzad.widget.UrlImageView;

public class BannerImageView extends UrlImageView{
	
	private BannerInfo data;
	
	private int width;
	private int height;
	
	private int intrinsicWidth;
	private int intrinsicHeight;
	
	public BannerImageView(Context context){
		this(context, null);
	}
	
	public BannerImageView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public BannerImageView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.setScaleType(ScaleType.MATRIX);
	}
	
	public void setBannerInfo(final BannerInfo data){
		this.data = data;
		setImageUrl(data.getPicture());
		setClickable(true);
		setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent bannerIntent = new Intent();
				bannerIntent.setAction(StatisticReceiver.ACTION_RECEIVE_STATISTIC);
				Statistic bannerS = new Statistic();
				bannerS.setName(data.getName(), Statistic.TYPE_BANNER);
				bannerS.setBrowseDetailCount(1);
				bannerIntent.putExtra(StatisticReceiver.NAME, bannerS);
				
				getContext().sendBroadcast(bannerIntent);
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(BannerImageView.this.data.getLink()));
				BannerImageView.this.getContext().startActivity(intent);
			}
		});
	}
	
	public BannerInfo getBannerInfo(){
		return data;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		
		float rate = (float) width / (float) intrinsicWidth;
		height = (int) ((float) intrinsicHeight * rate);
		setMeasuredDimension(width, height);
	}
	
	@Override
	public void setImageBitmap(Bitmap bitmap){
		if(bitmap != null){
			intrinsicWidth = bitmap.getWidth();
			intrinsicHeight = bitmap.getHeight();
			super.setImageBitmap(bitmap);
			Matrix matrix = new Matrix();
			float rate = (float) width / (float) intrinsicWidth;
			matrix.postScale(rate, rate);
			setImageMatrix(matrix);
			requestLayout();
		}
	}

}
