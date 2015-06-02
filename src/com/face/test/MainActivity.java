package com.face.test;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.face.test.add.R;
import com.face.test.fragment.AddStarsFragment;
import com.face.test.fragment.FuqixiangFragment;
import com.face.test.fragment.PhotosFragment;
import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;

public class MainActivity extends SherlockFragmentActivity {

	public static Bitmap curBitmap[] = new Bitmap[2];
	public static String[] TITLES;
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_drawlayout);
		
	
	}

	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 0, "Add");  
		menu.add(0, 2, 0, "Fuqi");  
		menu.add(0, 3, 0, "Photos");  
		 
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		Fragment mianFragment = null ;
		
		switch (item.getItemId()) {
		case 1:
			mianFragment = new AddStarsFragment();
			break;
		case 2:
			mianFragment = new FuqixiangFragment();
			break;
		case 3:
			mianFragment = new PhotosFragment();
			break;
		case 4:
			
			break;

		default:
			break;
		}
		
	  
		getSupportFragmentManager().beginTransaction()
				.add(R.id.drawer_content, mianFragment).commit();
		
		return true;
	}

	

}
