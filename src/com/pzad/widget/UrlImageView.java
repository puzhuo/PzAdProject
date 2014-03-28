package com.pzad.widget;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.pzad.concurrency.PzExecutorFactory;
import com.pzad.concurrency.PzThread;
import com.pzad.utils.image.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

public class UrlImageView extends ImageView{
	
	private String url;

	public UrlImageView(Context context){
		this(context, null);
	}
	
	public UrlImageView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public UrlImageView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	public void setImageUrl(final String url){
		this.url = url;
		new PzThread<Bitmap>(){

			@Override
			public Bitmap run() {
				Bitmap result = ImageLoader.getBitmap(UrlImageView.this.getContext(), url);
				/*
				try {
					URL url = new URL(UrlImageView.this.url);
					URLConnection urlConnection = url.openConnection();
					urlConnection.connect();
					InputStream is = urlConnection.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is);
					result = BitmapFactory.decodeStream(bis);
					
					is.close();
					bis.close();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 */
				return result;
			}

			@Override
			public void onFinish(Bitmap result) {
				if(result != null){
					UrlImageView.this.setImageBitmap(result);
				}
			}
			
		}.executeOnExecutor(PzExecutorFactory.getImageLoadThreadPool());
	}
}
