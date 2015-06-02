package com.face.test.adapter;

import java.util.List;

import com.face.test.MyApplication;
import com.face.test.add.R;
import com.face.test.bean.Stars;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageAdapter extends BaseAdapter {
	private Activity activity;
	private static LayoutInflater inflater;
	private List<Stars> list;
	private int width;
	

	@SuppressWarnings({ "deprecation" })
	public ImageAdapter(Activity a, final List<Stars> urls) {
		activity = a;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list = urls;
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.lv_item, null);
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.imageView1);
		
		imageView.setLayoutParams(new LinearLayout.LayoutParams(width / 3,
				width / 3));
		if (list.get(position).getBmobFile()!=null) {
			MyApplication.displayImage(list.get(position).getBmobFile().getFileUrl(), imageView);
		}else {
			imageView.setImageResource(R.drawable.demo);
		}
		
		
		return convertView;
	}


}
