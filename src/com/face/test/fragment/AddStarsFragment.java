package com.face.test.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

import com.face.test.Utils.BitmapUtil;
import com.face.test.Utils.Util;
import com.face.test.add.R;
import com.face.test.bean.FaceInfos;
import com.face.test.bean.Stars;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddStarsFragment extends Fragment {

	private HttpRequests request = null;// 在线api
	private FaceInfos faceInfos;
	private List<Stars> starssss;
	private Handler handler;

	private Bitmap mBitmap;
	private BmobFile bmobFile;

	private int i = 0;

	private TextView textView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.blankactivity, container, false);
		textView = (TextView) view.findViewById(R.id.textView1);
		request = new HttpRequests("99a9423512d4f19c17bd8d6b526e554c",
				"z8stpP3-HMdYhg6kAK73A2nBFwZg4Thl");
		initData();

		handler = new Handler() {
			public void handleMessage(Message msg) {
				
				switch (msg.what) {
				case 0:
					File file = BitmapUtil.saveBitmap(mBitmap);
					bmobFile = new BmobFile(file);
					bmobFile.upload(getActivity(), new UploadFileListener() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							Stars stars = new Stars();
							stars.setFaceId(faceInfos.getFace().get(0)
									.getFace_id());
							stars.setName(starssss.get(i).getName());
							stars.setBmobFile(bmobFile);
							stars.save(getActivity());
							textView.setText(textView.getText()
									+ starssss.get(i).getName()
									+ starssss.get(i).getUrl()
									+ faceInfos.getFace().get(0).getFace_id()+"\n");
							i++;
							if (i>=starssss.size()) {
								textView.setText(textView.getText()+"end");
								return;
							}
							
							new Thread(connectNet).start();
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							textView.setText(textView.getText()+starssss.get(i).getName()+arg1+"\n");
							i++;
							if (i>=starssss.size()) {
								textView.setText(textView.getText()+"end");
								return;
							}
							new Thread(connectNet).start();
						}
					});

					break;
				case 2:
					textView.setText(textView.getText()+starssss.get(i).getName()+"error\n");
					i++;
					if (i>=starssss.size()) {
						textView.setText(textView.getText()+"end");
						return;
					}
					new Thread(connectNet).start();
					break;
				default:
					break;
				}
			};
		};

		new Thread(connectNet).start();

		return view;
	}

	private Runnable connectNet = new Runnable() {
		@Override
		public void run() {
			try {

				// 以下是取得图片的两种方法
				// ////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap
				byte[] data = getImage(starssss.get(i).getUrl());
				if (data != null) {
					mBitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);// bitmap
					JSONObject jsonObject = null;
					try {
						jsonObject = request
								.detectionDetect(new PostParameters()
										.setImg(data));
						Log.i("lyj", jsonObject.toString());
						Gson gson = new Gson();
						faceInfos = gson.fromJson(jsonObject.toString(),
								FaceInfos.class);
						if (faceInfos.getFace().size() <= 0) {
							Message message = new Message();
							message.what = 2;
							handler.sendMessage(message);
							
						} else {
							String face1 = faceInfos.getFace().get(0)
									.getFace_id();

							jsonObject = request
									.facesetAddFace(new PostParameters()
											.setFaceId(face1).setFacesetName(
													"Stars1"));

							Message message = new Message();
							message.obj = jsonObject.toString();
							message.what = 0;
							handler.sendMessage(message);

						}

					} catch (FaceppParseException e) {
						e.printStackTrace();
						Message message = new Message();
						message.what = 2;
						handler.sendMessage(message);
					}

				} else {
					Message message = new Message();
					message.what = 2;
					handler.sendMessage(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message message = new Message();
				message.what = 2;
				handler.sendMessage(message);
			}

		}

	};

	public byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	private void initData() {
		String result = Util.getFromAssets(getActivity(), "stars.txt");
		Gson gson = new Gson();
		starssss = gson.fromJson(result, new TypeToken<List<Stars>>() {
		}.getType());

	}

}
