package com.face.test.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.face.test.add.R;
import com.face.test.adapter.ImageAdapter;
import com.face.test.bean.RemoveInfo;
import com.face.test.bean.Stars;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.google.gson.Gson;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.widget.GridView;

public class PhotosFragment extends Fragment {
	private GridView mGridView;
	private ImageAdapter imageAdapter;
	private List<Stars> list;
	private HttpRequests request = null;// 在线api

	private Handler handler;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		request = new HttpRequests("99a9423512d4f19c17bd8d6b526e554c",
				"z8stpP3-HMdYhg6kAK73A2nBFwZg4Thl");
		// Toast.makeText(getActivity(), "长按更多操作", Toast.LENGTH_SHORT).show();

		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					Stars stars = new Stars();
					stars.setObjectId((String) msg.obj);
					stars.delete(getActivity(), new DeleteListener() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							initData();
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), arg1,
									Toast.LENGTH_SHORT).show();
						}
					});
					break;
				case 1:

					Toast.makeText(getActivity(), (String) msg.obj,
							Toast.LENGTH_SHORT).show();

					break;

				default:
					break;
				}

			};
		};

		View view = inflater.inflate(R.layout.gridview_photo, container, false);
		mGridView = (GridView) view.findViewById(R.id.gv_photos);
		initData();
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						getActivity(),
						list.get(position).getName()
								+ list.get(position).getFaceId(),
						Toast.LENGTH_LONG).show();

				new Thread(new MYRUN(position)).start();

			}
		});
		return view;
	}

	public class MYRUN implements Runnable {
		private int s;

		public MYRUN(int s) {
			this.s = s;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONObject jsonObject = null;
			try {
				jsonObject = request.facesetRemoveFace(new PostParameters()
						.setFacesetName("Stars1").setFaceId(
								list.get(s).getFaceId()));

				Log.i("lyj", jsonObject.toString());
				Gson gson = new Gson();
				RemoveInfo removeInfo = gson.fromJson(jsonObject.toString(),
						RemoveInfo.class);
				Log.i("lyj", removeInfo.getSuccess());
				Message message = new Message();
				if (removeInfo.getSuccess().equals("true")) {
					message.what = 0;
					message.obj = list.get(s).getObjectId();
				} else {
					message.what = 1;
					message.obj = removeInfo.getRemoved();
				}
				handler.sendMessage(message);

			} catch (Exception e) {
				Message message = new Message();
				message.what = 1;
				message.obj = e.getMessage();
				handler.sendMessage(message);
			}

		}
	}

	@SuppressLint("SimpleDateFormat")
	private void initData() {
		BmobQuery<Stars> query = new BmobQuery<Stars>();

		// Date date = null;
		// try {
		// date = new SimpleDateFormat("yyyy-MM-dd").parse("2015-05-29");
		// } catch (ParseException e) {
		// }
		// query.addWhereGreaterThan("createdAt", new BmobDate(date));
		query.setLimit(900);
		query.order("name");
		query.findObjects(getActivity(), new FindListener<Stars>() {
			@Override
			public void onSuccess(final List<Stars> object) {
				// TODO Auto-generated method stub

				PhotosFragment.this.list = object;
				Toast.makeText(getActivity(), object.size() + "",
						Toast.LENGTH_SHORT).show();

				imageAdapter = new ImageAdapter(getActivity(), object);

				mGridView.setAdapter(imageAdapter);

				// for (int i = 0; i < object.size(); i++) {
				// TmpStar tmpStar = new TmpStar();
				// tmpStar.setName(list.get(i).getName());
				// tmpStar.setUrl(list.get(i).getUrl());
				// tmpStars.add(tmpStar);
				// }
				// Gson gson = new Gson();
				// String s = gson.toJson(tmpStars);
				// Log.i("lyj", s);

			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void writeFileSdcard(String fileName, String message) {

		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(message.getBytes());
			fos.close();
			Log.i("lyj", "写入成功：");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("lyj", "写入失败：");
		}
	}

}
