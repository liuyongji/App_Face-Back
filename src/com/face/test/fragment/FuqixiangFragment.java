package com.face.test.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.face.test.add.R;
import com.face.test.Utils.Util;
import com.face.test.bean.FaceInfos;
import com.face.test.bean.Person;
import com.face.test.bean.RemoveInfo;
import com.face.test.bean.Stars;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FuqixiangFragment extends Fragment {

	// private TextView tv_result;
	private ProgressDialog progressBar;

	private TextView textView;
	
	private Button btn;
	private EditText editText;

	private Handler detectHandler = null;
	private HttpRequests request = null;// 在线api
	private FaceInfos faceInfos;
	// private BmobFile bmobFile;

	private List<Stars> starssss;

	private int i = 0;

	private String[] ssss;

	private List<String> list = new ArrayList<String>();

	// private List<Stars> result;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		request = new HttpRequests("99a9423512d4f19c17bd8d6b526e554c",
				"z8stpP3-HMdYhg6kAK73A2nBFwZg4Thl");
		View view = inflater.inflate(R.layout.blankactivity, container, false);
		// initView(view);
		textView = (TextView) view.findViewById(R.id.textView1);
		btn=(Button)view.findViewById(R.id.button1);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 search(editText.getText().toString());
			}
		});
		editText=(EditText)view.findViewById(R.id.editText1);

		
//		deletePerson();
		
//		new Thread(remove).start();
		// initData();

		// new Thread(addrunable).start();

		detectHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (progressBar != null) {
					progressBar.dismiss();
				}
				switch (msg.what) {
				case 0:
					Toast.makeText(getActivity(),
							faceInfos.getFace().get(0).getFace_id(),
							Toast.LENGTH_SHORT).show();
					textView.setText(textView.getText()
							+ starssss.get(i).getName()
							+ starssss.get(i).getUrl() + "\n");
					Stars stars = new Stars();
					stars.setFaceId(faceInfos.getFace().get(0).getFace_id());
					stars.setUrl(starssss.get(i).getUrl());
					stars.setName(starssss.get(i).getName());
					stars.save(getActivity());
					i++;
					if (i >= starssss.size()) {
						textView.setText(textView.getText() + "end");
						return;
					}
					new Thread(addrunable).start();

					break;

				case 1:
					textView.setText(textView.getText()
							+ starssss.get(i).getName() + "图片出错\n");
					i++;
					if (i >= starssss.size()) {
						textView.setText(textView.getText() + "end");
						return;
					}
					new Thread(addrunable).start();
					break;
				case 2:
					textView.setText((String) msg.obj);
					break;
				case 3:
					textView.setText("删除失败");
					break;
				default:
					break;
				}

			};
		};
		return view;
	}
	
	private void deletePerson(){
		BmobQuery<Person> query =new BmobQuery<Person>();
//		query.setSkip(1000);
		query.setLimit(1000);
		query.order("file");
		query.findObjects(getActivity(), new FindListener<Person>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				textView.setText(arg1);
			}

			@Override
			public void onSuccess(List<Person> persons) {
				// TODO Auto-generated method stub
				for (int i = 0; i < persons.size(); i++) {
					textView.setText(persons.size()+"");
					if (persons.get(i).getFile()==null) {
						final Person person=new Person();
						person.setObjectId(persons.get(i).getObjectId());
						person.delete(getActivity(),new DeleteListener() {
							
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								textView.setText(textView.getText()+person.getCreatedAt());
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								
							}
						});
					}
				}
				
			}
		});
	}


	private void search(String name) {
		BmobQuery<Stars> query = new BmobQuery<Stars>();

		// query.addWhereContainedIn("faceId", list);
		query.addWhereContains("name", name);
		query.findObjects(getActivity(), new FindListener<Stars>() {

			@Override
			public void onError(int arg0, String arg1) {

				Toast.makeText(getActivity(), arg0 + " " + arg1,
						Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void onSuccess(List<Stars> stars) {
				Toast.makeText(getActivity(), stars.size()+"",
						Toast.LENGTH_LONG).show();
				textView.setText(stars.size()+"");
				// starssss = stars;
				// delete();
			}
		});
	}

	private void delete() {
		List<BmobObject> stars = new ArrayList<BmobObject>();
		for (int i = 0; i < starssss.size(); i++) {
			Stars stars2 = new Stars();
			stars2.setObjectId(starssss.get(i).getObjectId());
			stars.add(stars2);
		}

		new BmobObject().deleteBatch(getActivity(), stars,
				new DeleteListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						textView.setText("批量删除成功");
					}

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), code + "批量删除失败:" + msg,
								Toast.LENGTH_LONG).show();
					}
				});
	}

	private void initIDs() {
		String result = Util.getFromAssets(getActivity(), "ids.txt");
		Log.i("lyj", result);
		ssss = result.split(" ");
		for (int i = 200; i < ssss.length; i++) {
			list.add(ssss[i]);
		}
		Toast.makeText(getActivity(), ssss.length + "", Toast.LENGTH_LONG)
				.show();
	}

	private void initData() {
		String result = Util.getFromAssets(getActivity(), "stars.txt");
		Gson gson = new Gson();
		starssss = gson.fromJson(result, new TypeToken<List<Stars>>() {
		}.getType());

		Toast.makeText(getActivity(), starssss.size() + "", Toast.LENGTH_LONG)
				.show();
	}

	Runnable remove = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONObject jsonObject = null;
			try {
				jsonObject = request.facesetRemoveFace(new PostParameters()
						.setFacesetName("Stars").setFaceId(ssss));

				Log.i("lyj", jsonObject.toString());
				Gson gson = new Gson();
				RemoveInfo removeInfo = gson.fromJson(jsonObject.toString(),
						RemoveInfo.class);
				Message message = new Message();
				if (removeInfo.getSuccess().equals("true")) {
					message.what = 2;
					message.obj = removeInfo.getRemoved();
				} else {
					message.what = 3;
				}
				detectHandler.sendMessage(message);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	Runnable addrunable = new Runnable() {

		@Override
		public void run() {
			JSONObject jsonObject = null;
			try {
				jsonObject = request.detectionDetect(new PostParameters()
						.setUrl(starssss.get(i).getUrl()));

				Log.i("lyj", jsonObject.toString());

				Gson gson = new Gson();
				faceInfos = gson.fromJson(jsonObject.toString(),
						FaceInfos.class);
				if (faceInfos.getFace().size() == 0) {
					Message message = new Message();

					message.what = 1;
					detectHandler.sendMessage(message);
					return;
				}
				String face1 = faceInfos.getFace().get(0).getFace_id();
				jsonObject = request.facesetAddFace(new PostParameters()
						.setFaceId(face1).setFacesetName("Stars"));

				Log.i("lyj", jsonObject.toString());

				Message message = new Message();

				message.what = 0;
				detectHandler.sendMessage(message);

			} catch (FaceppParseException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				Message message = new Message();
				message.what = 1;
				detectHandler.sendMessage(message);
			}

		}
	};

}
