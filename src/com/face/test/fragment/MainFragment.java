package com.face.test.fragment;

import com.face.test.add.R;
import com.face.test.Utils.BitmapUtil;
import com.facepp.http.HttpRequests;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainFragment extends Fragment{
	public static final int DECTOR_SUCCESS = 0;
	public static final int COMPARE_SUCCESS = 1;
	public static final int COMPARE_FAIL = 2;
	public static final int DECTOR_FAIL = 3;


	private HttpRequests request = null;// 在线api

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.main_fuqixiang, container, false);
		request = new HttpRequests("99a9423512d4f19c17bd8d6b526e554c",
				"z8stpP3-HMdYhg6kAK73A2nBFwZg4Thl");
		
		ImageView imageView=(ImageView) view.findViewById(R.id.fuqi_imageview);
		Bitmap bitmap=BitmapUtil.convertViewToBitmap(view);
		Bitmap bitmap2=blurImage(bitmap);
		imageView.setImageBitmap(bitmap2);
		
		
		
	
		return view;
	}


	


	
	private    Bitmap blurImage(Bitmap bmp)  
    {  
        int width = bmp.getWidth();  
        int height = bmp.getHeight();  
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  
          
        int pixColor = 0;  
          
        int newR = 0;  
        int newG = 0;  
        int newB = 0;  
          
        int newColor = 0;  
          
        int[][] colors = new int[9][3];  
        for (int i = 1, length = width - 1; i < length; i++)  
        {  
            for (int k = 1, len = height - 1; k < len; k++)  
            {  
                for (int m = 0; m < 9; m++)  
                {  
                    int s = 0;  
                    int p = 0;  
                    switch(m)  
                    {  
                    case 0:  
                        s = i - 1;  
                        p = k - 1;  
                        break;  
                    case 1:  
                        s = i;  
                        p = k - 1;  
                        break;  
                    case 2:  
                        s = i + 1;  
                        p = k - 1;  
                        break;  
                    case 3:  
                        s = i + 1;  
                        p = k;  
                        break;  
                    case 4:  
                        s = i + 1;  
                        p = k + 1;  
                        break;  
                    case 5:  
                        s = i;  
                        p = k + 1;  
                        break;  
                    case 6:  
                        s = i - 1;  
                        p = k + 1;  
                        break;  
                    case 7:  
                        s = i - 1;  
                        p = k;  
                        break;  
                    case 8:  
                        s = i;  
                        p = k;  
                    }  
                    pixColor = bmp.getPixel(s, p);  
                    colors[m][0] = Color.red(pixColor);  
                    colors[m][1] = Color.green(pixColor);  
                    colors[m][2] = Color.blue(pixColor);  
                }  
                  
                for (int m = 0; m < 9; m++)  
                {  
                    newR += colors[m][0];  
                    newG += colors[m][1];  
                    newB += colors[m][2];  
                }  
                  
                newR = (int) (newR / 9F);  
                newG = (int) (newG / 9F);  
                newB = (int) (newB / 9F);  
                  
                newR = Math.min(255, Math.max(0, newR));  
                newG = Math.min(255, Math.max(0, newG));  
                newB = Math.min(255, Math.max(0, newB));  
                  
                newColor = Color.argb(255, newR, newG, newB);  
                bitmap.setPixel(i, k, newColor);  
                  
                newR = 0;  
                newG = 0;  
                newB = 0;  
            }  
        }  
          
        return bitmap;  
    } 

}
