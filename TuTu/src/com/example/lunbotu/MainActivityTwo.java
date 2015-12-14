package com.example.lunbotu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lunbotu.widget.CircleFlowIndicator;
import com.example.lunbotu.widget.ImageAdapter;
import com.example.lunbotu.widget.TuTu2;
import com.example.lunbotu.widget.ViewFlow;

public class MainActivityTwo extends Activity{

	private ViewPager ad_viewPage;
	/**
	 * 显示的文字Textview
	 */
	private TextView tv_msg;
	/**
	 * 添加小圆点的线性布局
	 */
	private LinearLayout ll_dian;
	/**
	 * 轮播图对象列表
	 */
	List<ADBean> listADbeans;
	List<ADBean> listADbeans2;
	/**
	 * 本地图片资源
	 */
	private int[] ids = { R.drawable.one, R.drawable.two, R.drawable.three,
			R.drawable.fore, R.drawable.five };
	/**
	 * 显示文字
	 */
	private String[] des = { "1111111", "22222222", "3333333", "4444444444","55555555555" };
	/**
	 * 网络资源
	 */
	private String[] urls = { "http://a.hiphotos.baidu.com/image/pic/item/0bd162d9f2d3572ce98282e18e13632762d0c3af.jpg",
			"http://d.hiphotos.baidu.com/image/pic/item/1b4c510fd9f9d72aebede7a1d62a2834359bbb85.jpg",
			"http://h.hiphotos.baidu.com/image/pic/item/91ef76c6a7efce1be2f4f15cad51f3deb58f654c.jpg",
			"http://h.hiphotos.baidu.com/image/w%3D230/sign=3e9ec55457fbb2fb342b5f117f4b2043/e850352ac65c1038343303cbb0119313b07e896e.jpg",
			"http://e.hiphotos.baidu.com/image/pic/item/d53f8794a4c27d1e3625e52d18d5ad6edcc438dc.jpg" };
	
	private TuTu tu;
	private Context mContext;
	private ViewFlow viewFlow;
	private ImageAdapter imageAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		initView();
		//initAD();
		initAD2();
	}
	private void initAD2() {
		listADbeans2 = new ArrayList<ADBean>();
		for(int i =0;i<5;i++){
			ADBean bean = new ADBean();
			bean.setmImageView(new ImageView(mContext));
			bean.setAdName(des[i]);
			bean.setId(i+"");
			bean.setImgUrl(urls[i]);//网络图片
			//bean.setImgPath(ids[i]);
			listADbeans2.add(bean);
		}
		
		imageAdapter = new ImageAdapter(this,listADbeans2);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setAdapter(imageAdapter);
		viewFlow.setmSideBuffer(listADbeans2.size()); // 实际图片张数， 我的ImageAdapter实际图片张数为3
		
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		TuTu2 tutu2 = new TuTu2(this,viewFlow,listADbeans2,imageAdapter);
		tutu2.setFlowIndicator(indic);
		tutu2.setTimeSpan(4000);
		tutu2.setSelection(3 * 1000); // 设置初始位置
		tutu2.startAutoFlowTimer(); // 启动自动播放
		tutu2.getNetImage();
		/*viewFlow.setFlowIndicator(indic);
		viewFlow.setTimeSpan(4000);
		viewFlow.setSelection(3 * 1000); // 设置初始位置
		viewFlow.startAutoFlowTimer(); // 启动自动播放
*/		
	}
	/**
	 * 初始化轮播图
	 */
	private void initAD() {
		listADbeans = new ArrayList<ADBean>();
		for(int i =0;i<5;i++){
			ADBean bean = new ADBean();
			bean.setAdName(des[i]);
			bean.setId(i+"");
			bean.setImgUrl(urls[i]);
			//bean.setImgPath(ids[i]);
			listADbeans.add(bean);
		}
		tu = new TuTu(ad_viewPage, tv_msg, ll_dian, mContext, listADbeans);
		tu.startViewPager(4000);//动态设置滑动间隔，并且开启轮播图
	}
	
	/**
	 * 初始化布局
	 */
	private void initView() {
		ad_viewPage = (ViewPager)findViewById(R.id.ad_viewPage);
		tv_msg = (TextView)findViewById(R.id.tv_msg);
		ll_dian = (LinearLayout)findViewById(R.id.ll_dian);
		
	}
	/**
	 * 销毁轮播图
	 */
	@Override
	protected void onDestroy() {  
		tu.destroyView();
		super.onDestroy();
	}
	
}
