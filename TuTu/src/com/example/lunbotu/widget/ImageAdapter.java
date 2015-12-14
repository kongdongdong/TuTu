/*
 * Copyright (C) 2011 Patrik �kerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunbotu.widget;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.lunbotu.ADBean;
import com.example.lunbotu.R;

public class ImageAdapter extends BaseAdapter {
	List<ADBean> listADbeans=null;
	private Context mContext;
	private LayoutInflater mInflater;
	private int[] ids = {R.drawable.fore, R.drawable.two,R.drawable.five};

	public ImageAdapter(Context context,List<ADBean> listADbeans) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listADbeans = listADbeans;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;   //返回很大的值使得getView中的position不断增大来实现循环
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.image_item, null);
		}
		//ImageView imageView = ((ImageView) convertView.findViewById(R.id.imgView));
		ImageView imageView = listADbeans.get(position%listADbeans.size()).getmImageView();
		if(listADbeans.get(position%listADbeans.size()).getImgPath() != -1){
			imageView.setImageResource(listADbeans.get(position%listADbeans.size()).getImgPath());
		}else{
			if(listADbeans.get(position%listADbeans.size()).getBitmap() != null){
				System.out.println("不为空。。。");
				imageView.setImageBitmap(listADbeans.get(position%listADbeans.size()).getBitmap());
			}
		}
		//imageView.setImageResource(ids[position%ids.length]);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		return imageView;
	}
	/**
	 * 判断view和instantiateItem返回的对象是否一样
	 * Object : 时instantiateItem返回的对象
	 */
	public boolean isViewFromObject(View view, Object object) {
		if(view == object){
			return true;
		}else{
			return false;
		}
	}

	
	/**
	 * 实例化两张图片,最多只能装三张图片
	 */
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		//释放资源,
		container.removeView((View) object);
	}
	
	public void notifyImages(String imageUrl,Bitmap bitmap){
		for(ADBean ad:listADbeans){
			if(imageUrl.equals(ad.getImgUrl())){
				System.out.println(imageUrl);
				ad.setBitmap(bitmap);
			}
		}
	}
	
}
