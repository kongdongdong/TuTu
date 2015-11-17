# 轮播图
可以加载本地图片或者网络资源的轮播图，一行代码调用，图片三级缓存，节省流量，使用方便。

1、你只要写好布局就行，布局写成啥样你自己决定，扩展性强，满足多样化需求
例如：



    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
     >


            <android.support.v4.view.ViewPager
                android:id="@+id/ad_viewPage"
                android:layout_width="wrap_content"
                android:layout_height="150dp"
                android:layout_centerHorizontal="true" />

            <LinearLayout
                android:layout_width="fill_parent"
                android:layout_height="wrap_content"
                android:layout_alignBottom="@id/ad_viewPage"
                android:background="#44000000"
                android:gravity="center"
                android:orientation="vertical" >

                <TextView
                    android:id="@+id/tv_msg"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:textColor="#ffffff"
                    android:textSize="18sp" />

                <LinearLayout
                    android:id="@+id/ll_dian"
                    android:layout_width="fill_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:orientation="horizontal"
                    android:padding="3dp"  >
                </LinearLayout>
            </LinearLayout>
    </RelativeLayout>


2、添加轮播图对象
	
	这是轮播图对象
    private String id;
	private String adName;//广告词
	private String imgUrl;//网络图片资源
	private int imgPath=-1;//本地图片资源
	private ImageView mImageView;




	初始化轮播图对象并添加到list里面

	List<ADBean> listADbeans;
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
		
    listADbeans = new ArrayList<ADBean>();
		for(int i =0;i<5;i++){
			ADBean bean = new ADBean();
			bean.setAdName(des[i]);//广告文字
			bean.setId(i+"");
			bean.setImgUrl(urls[i]);//添加网络图片资源，如果不需要可以不用添加
			bean.setImgPath(ids[i]);//添加本地图片资源，如果不需要可以不用添加，如果网络资源和本地资源同时添加，默认使用的是本地资源，所以建议本地资源和网络资源添加一个
			listADbeans.add(bean);
		}



3、然后你只要把布局添加进去就好了，同时开启轮播图

    TuTu tu = new TuTu(ad_viewPage, tv_msg, ll_dian, mContext, listADbeans);//把布局添加进去
	tu.startViewPager(4000);//动态设置滑动间隔，并且开启轮播图

4、在activity销毁时也罢要把轮播图销毁

    /**
	 * 销毁轮播图
	 */
	@Override
	protected void onDestroy() {
		if(tu!=null){
			tu.destroyView();
		}
		super.onDestroy();
	}






