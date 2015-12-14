package com.example.lunbotu.widget;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.example.lunbotu.ADBean;
import com.example.lunbotu.ImageUtil;
import com.example.lunbotu.ImageUtil.ImageCallback;

public class TuTu2 {
	private static String TAG = "tutu2";
	private List<ADBean> listADbeans2;
	private ViewFlow viewFlow;
	private ImageUtil imageUtil;
	private Context mContext;
	private ImageAdapter imageAdapter;
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if(what ==1){
				//imageAdapter.notifyImages(listADbeans2);
			}
		};
	};
	
	public TuTu2(Context mContext,ViewFlow viewFlow, List<ADBean> listADbeans2, ImageAdapter imageAdapter) {
		this.mContext = mContext;
		this.viewFlow = viewFlow;
		this.imageAdapter = imageAdapter;
		this.listADbeans2 = listADbeans2;
		imageUtil = new ImageUtil(mContext);
		//getNetImage();
	}
	public void getNetImage() {
		for(ADBean ad:listADbeans2){
			String url = ad.getImgUrl();
			if(url ==null || TextUtils.isEmpty(url)){
				return;
			}
			String imageName = url.substring(url.lastIndexOf("/")+1, url.length());
			if(!imageName.endsWith(".jpg") && !imageName.endsWith(".png")){
				imageName += ".png";
			}
			String imagePath = ImageUtil.IMAGE_PATH+imageName;
			if(url!= null && !TextUtils.isEmpty(url)){
				imageUtil.loadImage(imageName, url, true, new ImageCallback() {
					
					@Override
					public void onStart(String imageUrl) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onFailed(String imageUrl) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void loadImage(Bitmap bitmap, String imageUrl) {
						/*for(ADBean ad:listADbeans2){
							if(imageUrl.equals(ad.getImgUrl())){
								ad.getmImageView().setImageBitmap(bitmap);
							}
						}
						handler.sendEmptyMessage(1);*/
						imageAdapter.notifyImages(imageUrl, bitmap);
						
					};
				});
			}
		}
		
	}
	/**
	 * 设置指示器
	 * @param indic
	 */
	public void setFlowIndicator(CircleFlowIndicator indic){
		viewFlow.setFlowIndicator(indic);
	}
	/**
	 * 设置间隔时间
	 * @param time
	 */
	public void setTimeSpan(long time){
		viewFlow.setTimeSpan(time);
	}
	/**
	 * 设置初始位置
	 * @param position
	 */
	public void setSelection(int position){
		viewFlow.setSelection(position);
	}
	/**
	 * 开始
	 */
	public void startAutoFlowTimer(){
		viewFlow.startAutoFlowTimer();
	}
}
