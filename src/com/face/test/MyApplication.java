package com.face.test;

import java.util.List;

import cn.bmob.v3.Bmob;

import com.face.test.Utils.CrashHandler;
import com.face.test.add.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyApplication extends Application {
	private ImageLoaderConfiguration configuration;
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions options;
	private static List<Bitmap> bitmaps;

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		configuration = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(3)
				.discCacheFileCount(100).build();
		imageLoader.init(configuration);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.loading).cacheInMemory(true)
				.cacheOnDisc(true).build();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		Bmob.initialize(getApplicationContext(),
				"6bb1226b16bb29f5b8e3b71621af32fc");
	}


	public static void displayImage(String url, ImageView imageView) {
		imageLoader.displayImage(url, imageView, options);
	}

	

	public static List<Bitmap> getBitmaps() {
		return bitmaps;
	}

	public static void setBitmaps(List<Bitmap> bitmaps) {
		MyApplication.bitmaps = bitmaps;
	}

}
